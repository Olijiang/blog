package blog.controller;

import blog.config.Result;
import blog.entity.Album;
import blog.service.AlbumServiceImpl;
import blog.utils.TokenUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author ZGB
 * @version 1.0
 * @description: TODO
 * @date 2022/11/21 14:13
 */
@RestController
@RequestMapping("/album")
public class AlbumController {

	@Resource
	private AlbumServiceImpl albumService;

	@ApiOperation("获取相册")
	@GetMapping("/getAlbums")
	public Result get(@RequestHeader("token") String token){
		String authorId = Objects.requireNonNull(TokenUtil.getUserFromToken(token)).getUsername();
		List<Album> list = albumService.getAllAlbums(authorId);
		return Result.success("获取相册成功",list);
	}

	@ApiOperation("更新相册")
	@PostMapping("/update")
	public Result update(@RequestBody Album[] albums, @RequestHeader("token") String token){
		String authorId = Objects.requireNonNull(TokenUtil.getUserFromToken(token)).getUsername();
		return albumService.update(authorId, albums);
	}
}
