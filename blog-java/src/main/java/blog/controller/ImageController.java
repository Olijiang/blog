package blog.controller;


import blog.config.Result;
import blog.entity.ImageDTO;
import blog.service.ImageServiceImpl;
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
 * @since 2022-11-18
 */
@RestController
@RequestMapping("/image")
public class ImageController {

	@Resource
	private ImageServiceImpl imageService;

	@ApiOperation("获取初始图片")
	@GetMapping("/getInitImages")
	public Result getImages(@RequestHeader("token") String token, int startPage, int pageSize) {
		String authorId = Objects.requireNonNull(TokenUtil.getUserFromToken(token)).getUsername();
		List<ImageDTO> list = imageService.getImages(authorId,0, 8);
		return Result.success("获取图片成功",list);
	}

	@ApiOperation("根据相册获取图片")
	@GetMapping("/getImagesByAlbum")
	public Result getImagesByAlbum(@RequestHeader("token") String token,String albumName, int startPage, int pageSize) {
		String authorId = Objects.requireNonNull(TokenUtil.getUserFromToken(token)).getUsername();
		List<ImageDTO> list = imageService.getImagesByAlbum(authorId, albumName, startPage, pageSize);
		return Result.success("获取图片成功",list);
	}

	@ApiOperation("添加图片")
	@PostMapping("/add")
	public Result addImage(@RequestBody ImageDTO imageDTO, @RequestHeader("token") String token) {
		String authorId = Objects.requireNonNull(TokenUtil.getUserFromToken(token)).getUsername();
		return imageService.addImage(imageDTO, authorId);
	}

	@ApiOperation("删除图片")
	@PostMapping("/delete")
	public Result deleteImage(@RequestBody Integer[] idSet, @RequestHeader("token") String token) {
		String authorId = Objects.requireNonNull(TokenUtil.getUserFromToken(token)).getUsername();
		return imageService.deleteImage(idSet, authorId);
	}

}
