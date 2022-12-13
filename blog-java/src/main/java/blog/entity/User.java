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
 * @since 2022-11-06
 */
@ApiModel(value = "User对象", description = "")
public class User implements Serializable {

    @TableId
    private String username;

    private String password;

    private String authorName;

    private String authorInfo;

    private String blogName;

    private String blogInfo;

    private Integer articleNum;

    private Integer categoryNum;

    private Integer visitNum;
    // 头像
    private String img;

    private String coverImg;

    public User() {
    }

    public User(String username, String password, String authorName, String authorInfo, String blogName, String blogInfo, Integer articleNum, Integer categoryNum, Integer visitNum, String img, String coverImg) {
        this.username = username;
        this.password = password;
        this.authorName = authorName;
        this.authorInfo = authorInfo;
        this.blogName = blogName;
        this.blogInfo = blogInfo;
        this.articleNum = articleNum;
        this.categoryNum = categoryNum;
        this.visitNum = visitNum;
        this.img = img;
        this.coverImg = coverImg;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getBlogName() {
        return blogName;
    }
    public void setBlogName(String blogName) {
        this.blogName = blogName;
    }

    public String getBlogInfo() {
        return blogInfo;
    }
    public void setBlogInfo(String blogInfo) {
        this.blogInfo = blogInfo;
    }

    public Integer getArticleNum() {
        return articleNum;
    }
    public void setArticleNum(Integer articleNum) {
        this.articleNum = articleNum;
    }

    public Integer getCategoryNum() {
        return categoryNum;
    }
    public void setCategoryNum(Integer categoryNum) {
        this.categoryNum = categoryNum;
    }

    public Integer getVisitNum() {
        return visitNum;
    }
    public void setVisitNum(Integer visitNum) {
        this.visitNum = visitNum;
    }

    public String getImg() {
        return img;
    }
    public void setImg(String img) {
        this.img = img;
    }

    public String getCoverImg() {
        return coverImg;
    }
    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public String getAuthorName() {
        return authorName;
    }
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorInfo() {
        return authorInfo;
    }

    public void setAuthorInfo(String authorInfo) {
        this.authorInfo = authorInfo;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", authorName='" + authorName + '\'' +
                ", authorInfo='" + authorInfo + '\'' +
                ", blogName='" + blogName + '\'' +
                ", blogInfo='" + blogInfo + '\'' +
                ", articleNum=" + articleNum +
                ", categoryNum=" + categoryNum +
                ", visitNum=" + visitNum +
                ", img='" + img + '\'' +
                ", coverImg='" + coverImg + '\'' +
                '}';
    }
}
