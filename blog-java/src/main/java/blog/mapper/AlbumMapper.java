package blog.mapper;

import blog.entity.Album;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author ZGB
 * @version 1.0
 * @description: TODO
 * @date 2022/11/19 19:29
 */
public interface AlbumMapper extends BaseMapper<Album> {

	List<Album> getAlbums(String authorId);

	Integer getIdByName(String authorId, String albumName);

	String[] getAlbumNames(String authorId);
}
