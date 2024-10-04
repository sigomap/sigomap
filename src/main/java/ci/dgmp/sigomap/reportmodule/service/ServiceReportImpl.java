package ci.dgmp.sigomap.reportmodule.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import ci.dgmp.sigomap.reportmodule.config.JasperReportConfig;

import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service @RequiredArgsConstructor
public class ServiceReportImpl implements IServiceReport
{
    private final JasperReportConfig jrConfig;
    private final DataSource dataSource;
    private final ResourceLoader resourceLoader;


    private void setQrCodeParam(Map<String, Object> parameters, String qrText) throws Exception
    {
        //String qrText = "Application SynchronRE : Votre Demande de placement porte sur N° Affaire : " + parameters.get("aff_id") + " Assuré : " + parameters.get("aff_assure") + " Numéro de Police : " + parameters.get("fac_numero_police");
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix;
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            bitMatrix = qrCodeWriter.encode(qrText, BarcodeFormat.QR_CODE, 200, 200, hints);
            MatrixToImageWriter.writeToStream(bitMatrix, "png", stream);
            parameters.put("qrCode", new ByteArrayInputStream(stream.toByteArray()));
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private InputStream getImages(String path) throws IOException {
        String resourcePath = "classpath:"+path ;
        Resource resource = resourceLoader.getResource(resourcePath);
        return resource.getInputStream();
    }

    @Override
    public byte[] generateReport(String reportName, Map<String, Object> parameters, List<Object> data, String qrText) throws Exception
    {
        qrText =  qrText != null ? qrText : "Application SynchronRE : Numéro Fac : " + parameters.get("aff_id") + " Assuré : " + parameters.get("aff_assure") + " Numéro de Police : " + parameters.get("fac_numero_police");
        // Génération du code QR
        String resourcePath = "classpath:"+jrConfig.reportLocation + "/" + reportName;
        Resource resource = resourceLoader.getResource(resourcePath);
        this.setQrCodeParam(parameters, qrText);
        parameters.put("logo_nre", this.getImages(jrConfig.nreLogo));
        parameters.put("logo_synchronre", this.getImages(jrConfig.synchronRelogo));
        parameters.put("visa", this.getImages(jrConfig.visa));

        JasperReport jasperReport = JasperCompileManager.compileReport(resource.getInputStream());
        // Remplissez le rapport Jasper en utilisant la connexion JDBC
        Connection connection = dataSource.getConnection();
        JRBeanCollectionDataSource jrBeanCollectionDataSource = data == null || data.isEmpty() ? null : new JRBeanCollectionDataSource(data);

        JasperPrint jasperPrint = jrBeanCollectionDataSource == null
                ? JasperFillManager.fillReport(jasperReport, parameters, connection)
                : JasperFillManager.fillReport(jasperReport, parameters, jrBeanCollectionDataSource);

        // Exportez le rapport au format PDF
        byte[] reportBytes = JasperExportManager.exportReportToPdf(jasperPrint);

        // Fermez la connexion
        connection.close();

        return reportBytes;
    }

    private String getImagesPath() throws IOException {
        String imageLocation = "classpath:"+jrConfig.imagesLocation ;
        return resourceLoader.getResource(imageLocation).getURL().getPath();
    }
}