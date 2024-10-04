package ci.dgmp.sigomap.archivemodule.model.constants;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class DocumentsConstants
{
	public static final String UPLOADS_DIR = System.getProperty("user.home")+ File.separator + "workspace/synchronRe/docs/uploads";

	public static final long UPLOAD_MAX_SIZE = 5 * 1024 * 1024;
	public static final List<String> PHOTO_AUTHORIZED_TYPE = Arrays.asList("jpeg", "jpg", "png");
	public static final List<String> DOCUMENT_AUTHORIZED_TYPE = Arrays.asList("jpeg", "jpg", "png", "pdf", "doc", "docx");
	
	public static void main(String[] args) {
		System.out.println("user.home = "+ System.getProperty("user.home"));
		System.out.println("UPLOADS_DIR = "+ UPLOADS_DIR);
	}
}