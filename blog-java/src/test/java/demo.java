import blog.blogApplication;
import blog.config.PathConfig;
import blog.entity.Image;
import blog.mapper.AlbumMapper;
import blog.mapper.ImageMapper;
import blog.service.ArticleServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ZGB
 * @version 1.0
 * @description: TODO
 * @date 2022/10/28 13:58
 */
@SpringBootTest(classes = blogApplication.class)
public class demo {

	@Resource
	private ArticleServiceImpl articleService;
	@Resource
	private PathConfig athConfig;
	@Resource
	private AlbumMapper albumMapper;
	@Resource
	private ImageMapper imageMapper;

	@Test
	public void tag(){
		String authorId = "4413";
		String authorName = "画境";
		QueryWrapper<Image> wrapper = new QueryWrapper<>();
		wrapper.eq("author_id",authorId);
		wrapper.eq("album_id",albumMapper.getIdByName(authorId,authorName));
		List<Image> images = imageMapper.selectList(wrapper);
		System.out.println(images);
	}





}
