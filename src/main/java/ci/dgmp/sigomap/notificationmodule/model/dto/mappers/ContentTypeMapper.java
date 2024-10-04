package ci.dgmp.sigomap.notificationmodule.model.dto.mappers;

import java.util.HashMap;
import java.util.Map;

public class ContentTypeMapper {
    private static final Map<String, String> CONTENT_TYPES;

    static {
        CONTENT_TYPES = new HashMap<>();
        CONTENT_TYPES.put("jpeg", "image/jpeg");
        CONTENT_TYPES.put("jpg", "image/jpeg");
        CONTENT_TYPES.put("png", "image/png");
        CONTENT_TYPES.put("doc", "application/msword");
        CONTENT_TYPES.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        CONTENT_TYPES.put("xls", "application/vnd.ms-excel");
        CONTENT_TYPES.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        CONTENT_TYPES.put("pdf", "application/pdf");
        // Add more mappings here for other file extensions as needed
    }

    public static String getContentType(String extension) {
        String contentType = CONTENT_TYPES.get(extension.toLowerCase());
        if (contentType == null) {
            // Default to 'application/octet-stream' for unknown file types
            contentType = "application/octet-stream";
        }
        return contentType;
    }
}


