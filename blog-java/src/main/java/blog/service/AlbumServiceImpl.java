package blog.service;

import blog.config.LocalCache;
import blog.config.Result;
import blog.entity.Album;
import blog.entity.Image;
import blog.mapper.AlbumMapper;
import blog.mapper.ImageMapper;
import blog.utils.myUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ZGB
 * @version 1.0
 * @description: TODO
 * @date 2022/11/21 14:14
 */
@Service
@Slf4j
public class AlbumServiceImpl {

	@Resource
	private AlbumMapper albumMapper;
	@Resource
	private ImageMapper imageMapper;

	public Result update(String authorId, Album[] albums) {
		// 获取 全部 的 id
		Integer theId = getAlbum(authorId, "全部").getId();
		// 获取当前作者的全部相册
		List<Album> rawAlbums = getAllAlbums(authorId);

		Album album1;
		for (Album album : albums) {
			if (album.getId()<0){
				// 负id表示新加标签
				album.setId(null);
				album.setAuthorId(authorId);
				String path = myUtil.saveImg(album.getCoverImg(),authorId);
				album.setCoverImg(path);
				// 不是 0 就设为公开，以防乱传数据
				if (album.getIsPublic()!=0) album.setIsPublic(1);
				albumMapper.insert(album);
				log.info("新增相册："+album.getAlbumName());
			}else{
				// 修改和删除都需要验证身份， 确保当前登录用户只能操作自己的内容
				album1 = albumMapper.selectById(album.getId());
				if (album1==null) return Result.error("相册不存在");
				if (!album1.getAuthorId().equals(authorId)) return Result.error("非法操作");
				// 重设 authorId
				album.setAuthorId(authorId);
				// 无法对”全部“的企图修改
				if (album.getId().equals(theId)){
					album.setAlbumName("全部");
				}
				if (album.getIsPublic()!=0) album.setIsPublic(1);
				if (!album.getCoverImg().equals("")){
					// 图片修改过, 删除原图片, 写入新图片
					myUtil.deleteFile(album1.getCoverImg());
					String path = myUtil.saveImg(album.getCoverImg(),authorId);
					album.setCoverImg(path);
				}else{
					album.setCoverImg(album1.getCoverImg());
				}
				albumMapper.updateById(album);
				log.info("更新相册：原相册："+album1 + " --> 新相册"+album);
				// 移除 rawAlbums 的原本相册信息
				for (int i = 0; i < rawAlbums.size(); i++) {
					if (rawAlbums.get(i).getId().equals(album.getId())){
						rawAlbums.remove(i);
						break;
					}
				}
			}
		}
		// 删除 rawAlbums 剩余的相册
		for (Album album : rawAlbums) {
			QueryWrapper<Image> wrapper = new QueryWrapper<>();
			wrapper.eq("author_id",authorId);
			wrapper.eq("album_id",getAlbum(authorId, "全部").getId());
			List<Image> images = imageMapper.selectList(wrapper);
			for (Image image : images) {
				myUtil.deleteFile(image.getOriginalImg());
				myUtil.deleteFile(image.getSimplifyImg());
				imageMapper.deleteById(image);
			}
			// 清除缓存
			LocalCache.removeByPre("imageList" + authorId);
			albumMapper.deleteById(album);
			log.info("删除相册："+album.getAlbumName());
		}
		LocalCache.remove("albums" + authorId);
		return Result.success("相册编辑成功");
	}

	// 根据 authorId 获取相册分类信息
	public List<Album> getAllAlbums(String authorId) {
		List<Album> albums;
		String key = "albums" + authorId;
		if ((albums = (List<Album>) LocalCache.get(key)) == null) {
			QueryWrapper<Album> wrapper = new QueryWrapper<Album>()
					.eq("author_id",authorId);
			albums = albumMapper.selectList(wrapper);
			if (albums==null) return new ArrayList<>();
			LocalCache.put(key, albums);
			return albums;
		}
		return albums;
	}

	public List<Album> getPublicAlbums(String authorId) {
		List<Album> albums;
		String key = "albums" + authorId + "public";
		if ((albums = (List<Album>) LocalCache.get(key)) == null) {
			QueryWrapper<Album> wrapper = new QueryWrapper<Album>()
					.eq("author_id",authorId)
					.eq("is_public",1);
			albums = albumMapper.selectList(wrapper);
			if (albums==null) return new ArrayList<>();
			LocalCache.put(key, albums);
			return albums;
		}
		return albums;
	}

	public Album getAlbum(String authorId, String albumName){
		Album album;
		String key = "album" + authorId;
		if ((album = (Album) LocalCache.get(key))==null){
			QueryWrapper<Album> wrapper = new QueryWrapper<Album>()
					.eq("author_id",authorId)
					.eq("album_name",albumName);
			album = albumMapper.selectOne(wrapper);
			if (album==null) return null;
			LocalCache.put(key,album);
			return album;
		}
		return album;
	}
}
