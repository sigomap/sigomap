package ci.dgmp.sigomap.archivemodule.model.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Base64FileDto
{
    private String base64UrlString;
    private byte [] bytes;
}
