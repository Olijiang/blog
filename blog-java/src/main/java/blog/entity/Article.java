package blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 
 * </p>
 *
 * @author ZGB
 * @since 2022-11-06
 */
@ApiModel(value = "Article对象", description = "")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article{

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String title;

    private String authorId;

    private String category;

    private String tag;

    private String content;

    private String digest;

    private String img;

    private String createTime;

    private String updateTime;

    private int isPublic;
}
