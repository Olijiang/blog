package blog.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author ZGB
 * @since 2022-11-09
 */
@ApiModel(value = "Tag对象", description = "")
public class Tag implements Serializable {

    @TableId
    private String authorId;

    private String tags;

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }
    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Tag{" +
            "authorId=" + authorId +
            ", tags=" + tags +
        "}";
    }
}
