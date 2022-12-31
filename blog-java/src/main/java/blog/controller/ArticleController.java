package blog.controller;


import blog.config.Result;
import blog.entity.Article;
import blog.entity.ArticleDTO;
import blog.service.ArticleServiceImpl;
import blog.utils.TokenUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ZGB
 * @since 2022-11-06
 */
@RestController
@RequestMapping("/article")
public class ArticleController {

	@Resource
	private ArticleServiceImpl articleService;

	@ApiOperation("获取文章列表")
	@GetMapping("/getArticles")
	public Result getArticles(String queryWord, int startPage, int pageSize, @RequestHeader("token") String token) {
		String authorId = Objects.requireNonNull(TokenUtil.getUserFromToken(token)).getUsername();
		List<Article> articles = articleService.getAllArticles(authorId,queryWord,startPage,pageSize);
		return Result.success("获取文章成功",articles);
	}

	@ApiOperation("获取单片文章")
	@GetMapping("/getArticle")
	public Result getArticle(Integer articleId) {
		Article article = articleService.getArticle(articleId);
		if (article==null) return Result.error("文章不存在");
		return Result.success("获取文章成功",article);
	}

	@ApiOperation("添加文章")
	@PostMapping("/add")
	public Result addArticle(@RequestBody ArticleDTO articleDTO, @RequestHeader("token") String token) {
		String authorId = Objects.requireNonNull(TokenUtil.getUserFromToken(token)).getUsername();
		return articleService.addArticle(articleDTO, authorId);
	}

	@ApiOperation("更新文章")
	@PostMapping("/update")
	public Result updateArticle(@RequestBody ArticleDTO articleDTO, @RequestHeader("token") String token) {
		String authorId = Objects.requireNonNull(TokenUtil.getUserFromToken(token)).getUsername();
		return articleService.updateArticle(articleDTO,authorId);
	}

	@ApiOperation("删除文章")
	@GetMapping("/delete")
	public Result deleteArticle(Integer articleId, @RequestHeader("token") String token) {
		String authorId = Objects.requireNonNull(TokenUtil.getUserFromToken(token)).getUsername();
		return articleService.deleteArticle(articleId,authorId);
	}
}
