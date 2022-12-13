package blog.mapper;

import blog.entity.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ZGB
 * @since 2022-11-06
 */
public interface ArticleMapper extends BaseMapper<Article> {

	List<Article> getArticles(String authorId, int startPage, int pageSize);
}
