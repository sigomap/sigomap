package ci.dgmp.sigomap.notificationmodule.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class EmailAttachment
{
    private String filename;
    private byte[] content;
    private String contentType;
}