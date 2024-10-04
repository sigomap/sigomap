package ci.dgmp.sigomap.sharedmodule.utilities;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class Base64ToFileConverter
{
    public static MultipartFile convertToFile(String base64UrlString, String fileName){
        // Add padding characters ('=') at the end if necessary
        int padding = 4 - base64UrlString.length() % 4;
        if (padding % 4 != 0) {
            base64UrlString += "=".repeat(padding);
        }
        base64UrlString = base64UrlString.replace('+', '-').replace('/', '_');
        byte[] decodedBytes = Base64.getUrlDecoder().decode(base64UrlString);
        MultipartFile multipartFile = new InMemoryMultipartFile(fileName, decodedBytes);
        return multipartFile;
    }

    public static String convertToBase64UrlString(MultipartFile file) throws IOException {
        byte[] fileBytes = file.getBytes();
        String base64UrlEncodedString = Base64.getUrlEncoder().encodeToString(fileBytes);
        return base64UrlEncodedString;
    }

    public static String convertBytesToBase64UrlString(byte[] fileBytes) throws IOException {
        String base64UrlEncodedString = Base64.getUrlEncoder().encodeToString(fileBytes);
        return base64UrlEncodedString;
    }


        public static String getBase64UrlFromPath(String filePath) throws IOException {
            return convertBytesToBase64UrlString(Files.readAllBytes(Paths.get(filePath)));
        }

    public static String convertBytesToJSBase64UrlString(byte[] bytes) throws IOException {
        return convertBytesToBase64UrlString(bytes).replace("-", "+").replace("_", "/");
    }
}

 class InMemoryMultipartFile implements MultipartFile {

    private final byte[] fileContent;
    private final String fileName;

    public InMemoryMultipartFile(String fileName, byte[] fileContent) {
        this.fileContent = fileContent;
        this.fileName = fileName;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getOriginalFilename() {
        return this.fileName;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return this.fileContent == null || this.fileContent.length == 0;
    }

    @Override
    public long getSize() {
        return this.fileContent.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return this.fileContent;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(this.fileContent);
    }

     @Override
     public void transferTo(File dest) throws IOException, IllegalStateException {

     }

     @Override
    public void transferTo(java.nio.file.Path dest) throws IOException, IllegalStateException {
        // Not implemented
    }
}

