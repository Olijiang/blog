package blog.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ZGB
 * @version 1.0
 * @description: TODO
 * @date 2022/12/13 11:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlbumDTO {

	private Integer id;

	private String authorId;

	private String albumName;

	private String coverImg;

	private Boolean changeFlag;

	private Integer nums;
}
