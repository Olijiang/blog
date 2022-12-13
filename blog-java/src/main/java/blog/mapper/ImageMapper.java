package blog.mapper;

import blog.entity.Image;
import blog.entity.ImageDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author ZGB
 * @version 1.0
 * @description: TODO
 * @date 2022/11/19 19:19
 */
public interface ImageMapper extends BaseMapper<Image> {

	List<ImageDTO> getImages(String authorId, int startPage, int pageSize);

	List<ImageDTO> getImagesByAlbum(String authorId, String albumName, int startPage, int pageSize);

}
