package blog.service;

import blog.config.ComResult;
import blog.config.LocalCatch;
import blog.config.PathConfig;
import blog.entity.Image;
import blog.entity.ImageDTO;
import blog.mapper.AlbumMapper;
import blog.mapper.ImageMapper;
import blog.utils.myUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ZGB
 * @since 2022-11-18
 */
@Service
@Slf4j
public class ImageServiceImpl extends ServiceImpl<ImageMapper, Image> {

	@Resource
	private ImageMapper imageMapper;
	@Resource
	private AlbumMapper albumMapper;


	public ComResult addImage(ImageDTO imageDTO, String authorId) {
		Image image = new Image();
		// 设置分类ID
		image.setAlbumId(getAlbumIdByName(authorId, imageDTO.getAlbumName()));
		// 作者ID
		image.setAuthorId(authorId);
		// 文件名
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSS", Locale.US); // 格式化时间
		String date = dtf.format(LocalDateTime.now());
		String path1 = date + "_" + authorId;
		String path = saveImg(imageDTO.getSimplifyImg(),path1);
		if (path==null) return ComResult.error("图片保存失败");
		image.setSimplifyImg(path);
		// 原图
		String path2 = date + "R_" + authorId;
		path = saveImg(imageDTO.getOriginalImg(),path2);
		if (path==null) return ComResult.error("图片保存失败");
		image.setOriginalImg(path);
		LocalCatch.removeByPre("imageList" + authorId);
		imageMapper.insert(image);
		log.info("添加图片成功："+image);
		return ComResult.success("图片保存成功");
	}

	public ComResult getImages(String authorId, int startPage, int pageSize) {
		List<ImageDTO> images;
		String key = "imageList" + authorId + "-" + startPage + "-" + (startPage+pageSize);
		if ((images = (List<ImageDTO>) LocalCatch.get(key)) == null) {
			images = imageMapper.getImages(authorId,startPage,pageSize);
			if (images==null) return ComResult.error("获取图片列表失败，用户不存在");
			LocalCatch.put(key, images);
			return ComResult.success("获取图片列表成功", images);
		}
		return ComResult.success("获取图片列表成功", images);
	}

	public ComResult deleteImage(Integer[] idSet, String authorId) {
		Image image;
		for (Integer id : idSet) {
			// 查图片验证作者身份
			image = getImageById(id);
			if (!image.getAuthorId().equals(authorId))
				return ComResult.error("非法操作");
			try {
				// 删数据库记录
				imageMapper.deleteById(id);
				// 删除本地文件
				myUtil.deleteFile(image.getSimplifyImg());
				myUtil.deleteFile(image.getOriginalImg());
				log.info("删除图片成功："+image);
			}catch (Exception e){
				return ComResult.error("删除失败");
			}
		}
		// 清除缓存
		LocalCatch.removeByPre("imageList" + authorId);
		return ComResult.success("删除成功");
	}

	private Image getImageById(Integer id) {
		return imageMapper.selectById(id);
	}

	public ComResult getImagesByAlbum(String authorId, String albumName, int startPage, int pageSize) {
		List<ImageDTO> images;
		String key = "imageList" + authorId + "-" + albumName + startPage + "-" + (startPage+pageSize);
		if ((images = (List<ImageDTO>) LocalCatch.get(key)) == null) {
			images = imageMapper.getImagesByAlbum(authorId,albumName,startPage,pageSize);
			if (images==null) return ComResult.error("获取相册图片列表失败，用户不存在");
			LocalCatch.put(key, images);
			return ComResult.success("获取相册图片列表成功", images);
		}
		return ComResult.success("获取相册图片列表成功", images);
	}



	// ----------------->>>>>>>>>>>.  private 方法
	// 根据author 获取 tag 数据
	private String saveImg(String imgData, String fileName){
		String projectPath = System.getProperty("user.dir");
		// 截取图片信息
		String[] info = imgData.split(";base64,");
		// 截取图片文件后缀
		String fileSuffix = info[0].substring(11);
		// 图片数据
		String base64ImgData = info[1];
		// 文件名
		fileName = fileName + "." + fileSuffix;
		String storagePath = projectPath + File.separator + PathConfig.dataPath + File.separator +PathConfig.imgPath + File.separator + fileName;
		// relativePath src链接直接请求数据，需要加 dataPath 资源映射.
		String relativePath = PathConfig.dataPath + "/" + PathConfig.imgPath + "/" + fileName;
		if (myUtil.writeBase64Img(base64ImgData, storagePath)){
			log.info("图片保存成功:"+relativePath);
			return relativePath;
		}else {
			log.warn("图片保存失败:"+relativePath);
			return null;
		}
	}

	// 通过相册名获取相册id
	private Integer getAlbumIdByName(String authorId ,String albumName){
		return albumMapper.getIdByName(authorId, albumName);
	}
}
