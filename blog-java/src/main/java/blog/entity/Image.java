package blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;

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
public class Image implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String authorId;

    private Integer albumId;

    private String simplifyImg;

    private String originalImg;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }
    public Integer getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Integer albumId) {
        this.albumId = albumId;
    }
    public String getSimplifyImg() {
        return simplifyImg;
    }

    public void setSimplifyImg(String simplifyImg) {
        this.simplifyImg = simplifyImg;
    }
    public String getOriginalImg() {
        return originalImg;
    }

    public void setOriginalImg(String originnalImg) {
        this.originalImg = originnalImg;
    }

    @Override
    public String toString() {
        return "Image{" +
            "id=" + id +
            ", authorId=" + authorId +
            ", albumName=" + albumId +
            ", simplifyImg=" + simplifyImg +
            ", originalImg=" + originalImg +
        "}";
    }
}
