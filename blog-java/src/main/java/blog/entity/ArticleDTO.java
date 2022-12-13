package blog.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author ZGB
 * @version 1.0
 * @description: TODO
 * @date 2022/11/8 14:16
 */
@Data
@ApiModel("接收前端的文章对象")
public class ArticleDTO {
	private Integer id;
	private String img;
	private String title;
	private String content;
	private String category;
	private String[] tag;
}
