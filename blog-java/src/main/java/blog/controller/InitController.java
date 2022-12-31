package blog.controller;

import blog.config.Result;
import blog.config.LocalCache;
import blog.entity.*;
import blog.service.*;
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
	@Resource
	private CategoryServiceImpl categoryService;

	@ApiOperation("访问数量")
	@GetMapping("/visit")
	public void visit(String authorId){
		User user = userService.getUserById(authorId);
		if (user==null) return;
		user.setVisitNum(user.getVisitNum()+1);
		userService.updateById(user);
		LocalCache.put("user"+authorId, user);
	}

	@ApiOperation("初始化时获取作者信息")
	@GetMapping("/getAuthor")
	public Result getAuthor(String authorId){
		User user = userService.getUserById(authorId);
		if (user==null) return Result.error("用户不存在");
		user.setPassword(null);
		return Result.success("获取用户信息成功",user);
	}

	@GetMapping("/getCategories")
	public Result getCategories(String authorId){
		List<Category> categories= categoryService.getCategories(authorId);
		return Result.success("获取分类成功",categories);
	}

	@GetMapping("/getTags")
	public Result getTags(String authorId){
		return articleService.getTagsById(authorId);
	}

	@ApiOperation("初始化时获取公开文章列表信息")
	@GetMapping("/getPublicArticles")
	public Result getPublicArticles(String authorId, int startPage, int pageSize){
		List<Article> articles = articleService.getPublicArticles(authorId, startPage, pageSize);
		return Result.success("获取文章列表成功", articles);
	}

	@ApiOperation("根据文章id获取公开文章信息")
	@GetMapping("/getPublicArticle")
	public Result getPublicArticle(Integer ArticleId){
		Article article = articleService.getPublicArticle(ArticleId);
		return Result.success("获取文章成功",article);
	}

	@ApiOperation("获取文章内容")
	@GetMapping("/getContent")
	public Result getContent(String filePath){
		return articleService.getArticleContent(filePath);
	}

	@ApiOperation("获取公开图片")
	@GetMapping("/getPublicImages")
	public Result getPublicImages(String authorId, int startPage, int pageSize) {
		List<ImageDTO> images = imageService.getPublicImages(authorId, startPage, pageSize);
		return Result.success("获取图片成功", images);
	}

	@ApiOperation("根据公开相册获取图片")
	@GetMapping("/getImagesByPublicAlbum")
	public Result getImagesByPublicAlbum(String authorId, String albumName, int startPage, int pageSize) {
		if (albumName.equals("全部")){
			List<ImageDTO> images = imageService.getPublicImages(authorId, startPage, pageSize);
			return Result.success("获取图片成功", images);
		}
		List<ImageDTO> images = imageService.getImagesByPublicAlbum(authorId, albumName, startPage, pageSize);
		return Result.success("获取图片成功", images);
	}

	@ApiOperation("获取公开相册")
	@GetMapping("/getPublicAlbums")
	public Result getPublicAlbums(String authorId) {
		List<Album> albums = albumService.getPublicAlbums(authorId);
		return Result.success("获取相册类别成功", albums);
	}
}
