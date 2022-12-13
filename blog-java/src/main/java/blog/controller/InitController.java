package blog.controller;

import blog.config.ComResult;
import blog.entity.Album;
import blog.service.AlbumServiceImpl;
import blog.service.ArticleServiceImpl;
import blog.service.ImageServiceImpl;
import blog.service.UserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ZGB
 * @version 1.0
 * @description: TODO
 * @date 2022/11/8 11:14
 */
@Api("处理初始化请求和无需权限的请求")
@RestController
@RequestMapping("/init")
public class InitController {

	@Resource
	private UserServiceImpl userService;
	@Resource
	private ArticleServiceImpl articleService;
	@Resource
	private ImageServiceImpl imageService;
	@Resource
	private AlbumServiceImpl albumService;

	@ApiOperation("初始化时获取作者信息")
	@GetMapping("/getAuthor")
	public ComResult getAuthor(String authorId){
		return userService.getUser(authorId);
	}

	@GetMapping("/getCategories")
	public ComResult getCategories(String authorId){
		return articleService.getCategoryById(authorId);
	}

	@GetMapping("/getTags")
	public ComResult getTags(String authorId){
		return articleService.getTagsById(authorId);
	}

	@ApiOperation("初始化时获取文章列表信息")
	@GetMapping("/getArticles")
	public ComResult getArticles(String authorId, int startPage, int pageSize){
		return articleService.getArticles(authorId, startPage, pageSize);
	}

	@ApiOperation("根据文章id获取文章信息")
	@GetMapping("/getArticle")
	public ComResult getArticle(Integer ArticleId){
		return articleService.getArticleById(ArticleId);
	}

	@ApiOperation("获取文章内容")
	@GetMapping("/getContent")
	public ComResult getContent(String filePath){
		return articleService.getArticleContent(filePath);
	}

	@ApiOperation("获取图片")
	@GetMapping("/getImages")
	public ComResult getImages(String authorId, int startPage, int pageSize) {
		return imageService.getImages(authorId, startPage, pageSize);
	}

	@ApiOperation("根据相册获取图片")
	@GetMapping("/getImagesByAlbum")
	public ComResult getImagesByAlbum(String authorId, String albumName, int startPage, int pageSize) {
		if (albumName.equals("全部"))
			return imageService.getImages(authorId,startPage, pageSize);
		return imageService.getImagesByAlbum(authorId, albumName, startPage, pageSize);
	}

	@ApiOperation("获取相册类别")
	@GetMapping("/getAlbums")
	public ComResult getAlbums(String authorId) {
		List<Album> albums = albumService.getAlbums(authorId);
		return ComResult.success("获取相册类别成功", albums);
	}
}
