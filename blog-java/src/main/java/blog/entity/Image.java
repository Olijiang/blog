package blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * id: 自增id
 * <p>
 * authorId: 作者id
 * <p>
 * albumName: 相册name
 * <p>
 * simplifyImg: 缩略图地址
 * <p>
 * originalImg: 原图地址
 * <p>
 * @author ZGB
 * @since 2022-11-18
 */
@ApiModel(value = "Image对象", description = "")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Image implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String authorId;

    private Integer albumId;

    private String simplifyImg;

    private String originalImg;

    private int isPublic;
}
