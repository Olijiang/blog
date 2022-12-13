package blog.service;

import blog.config.LocalCatch;
import blog.entity.Tag;
import blog.mapper.TagMapper;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ZGB
 * @since 2022-11-09
 */
@Service
@Slf4j
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>{

	@Resource
	private TagMapper tagMapper;

	public void updateTags(String authorId, String[] tags) {
		Tag tag = new Tag();
		tag.setAuthorId(authorId);
		JSONObject json = new JSONObject();
		json.put("tags", tags);
		tag.setTags(json.toJSONString());
		tagMapper.updateById(tag);
		LocalCatch.put("tags"+authorId,tags);
		log.info(authorId+"：更新tags");
	}
}
