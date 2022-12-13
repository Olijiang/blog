package blog.entity;

import lombok.Data;

/**
 * albumName: 相册名
 * <p>
 * simplifyImg: 缩略图 base64
 * <p>
 * originalImg: 原图 base64
 *
 * @author ZGB
 * @version 1.0
 * @description: TODO
 * @date 2022/11/18 22:05
 */
@Data
public class ImageDTO {
	private Integer id;
	private String authorId;
	private String albumName;
	private String simplifyImg;
	private String originalImg;
}
