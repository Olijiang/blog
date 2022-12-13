package blog.controller;


import blog.config.ComResult;
import blog.entity.ImageDTO;
import blog.service.ImageServiceImpl;
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
 * @since 2022-11-18
 */
@RestController
@RequestMapping("/image")
public class ImageController {

	@Resource
	private ImageServiceImpl albumService;

	@ApiOperation("添加图片")
	@PostMapping("/add")
	public ComResult addImage(@RequestBody ImageDTO imageDTO, @RequestHeader("token") String token) {
		String authorId = Objects.requireNonNull(JwtUtil.getUserFromToken(token)).getUsername();
		return albumService.addImage(imageDTO, authorId);
	}

	@ApiOperation("删除图片")
	@PostMapping("/delete")
	public ComResult deleteImage(@RequestBody Integer[] idSet, @RequestHeader("token") String token) {
		String authorId = Objects.requireNonNull(JwtUtil.getUserFromToken(token)).getUsername();
		return albumService.deleteImage(idSet, authorId);
	}

}
