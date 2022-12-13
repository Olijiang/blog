package blog.service;

import blog.config.LocalCatch;
import blog.entity.Category;
import blog.mapper.CategoryMapper;
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
 * @since 2022-11-06
 */
@Service
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>{

	@Resource
	private CategoryMapper categoryMapper;

	public void updateCategories(String authorId, String[] categories) {
		Category category = new Category();
		category.setAuthorId(authorId);
		JSONObject json = new JSONObject();
		json.put("categories", categories);
		category.setCategories(json.toJSONString());
		categoryMapper.updateById(category);
		LocalCatch.put("categories"+authorId,categories);
		log.info(authorId+"：更新categories");
	}
}
