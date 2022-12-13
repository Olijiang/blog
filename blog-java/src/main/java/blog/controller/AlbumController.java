package blog.controller;

import blog.config.ComResult;
import blog.entity.Album;
import blog.service.AlbumServiceImpl;
import blog.utils.JwtUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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

	@PostMapping("/update")
	public ComResult update(@RequestBody Album[] albums, @RequestHeader("token") String token){
		String authorId = Objects.requireNonNull(JwtUtil.getUserFromToken(token)).getUsername();
		return albumService.update(authorId, albums);
	}



}
