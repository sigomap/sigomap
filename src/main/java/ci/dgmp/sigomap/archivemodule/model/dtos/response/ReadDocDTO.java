package ci.dgmp.sigomap.archivemodule.model.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class ReadDocDTO
{
	private Long docId;
	private String docNum;
	private String docName;
	private String docDescription;
	private String docPath;
	private String docUniqueCode;
	private String docTypeName;

}
