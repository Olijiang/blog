package blog.controller;


import blog.config.ComResult;
import blog.entity.ArticleDTO;
import blog.mapper.ArticleMapper;
import blog.service.ArticleServiceImpl;
import blog.utils.JwtUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
	@Resource
	private ArticleMapper articleMapper;

	@ApiOperation("添加文章")
	@PostMapping("/add")
	public ComResult addArticle(@RequestBody ArticleDTO articleDTO,@RequestHeader("token") String token) {
		String authorId = Objects.requireNonNull(JwtUtil.getUserFromToken(token)).getUsername();
		return articleService.addArticle(articleDTO, authorId);
	}

	@ApiOperation("更新文章")
	@PostMapping("/update")
	public ComResult updateArticle(@RequestBody ArticleDTO articleDTO,@RequestHeader("token") String token) {
		String authorId = Objects.requireNonNull(JwtUtil.getUserFromToken(token)).getUsername();
		return articleService.updateArticle(articleDTO,authorId);
	}

	@ApiOperation("删除文章")
	@GetMapping("/delete")
	public ComResult deleteArticle( Integer articleId,@RequestHeader("token") String token) {
		String authorId = Objects.requireNonNull(JwtUtil.getUserFromToken(token)).getUsername();
		return articleService.deleteArticle(articleId,authorId);
	}



}
