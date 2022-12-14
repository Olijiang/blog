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
@ApiModel(value = "Category对象", description = "")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String authorId;

    private String category;

    private Integer articleNum;

}
