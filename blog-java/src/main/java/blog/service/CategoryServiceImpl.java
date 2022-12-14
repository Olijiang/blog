package blog.service;

import blog.config.LocalCatch;
import blog.entity.Category;
import blog.mapper.CategoryMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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

	public void articleAddOne(String authorId, String categoryName){
		QueryWrapper<Category> wrapper = new QueryWrapper<>();
		wrapper.eq("author_id",authorId);
		wrapper.eq("category",categoryName);
		Category category = categoryMapper.selectOne(wrapper);
		category.setArticleNum(category.getArticleNum()+1);
		categoryMapper.updateById(category);
		LocalCatch.put( "category"+category.getId(),category);
		LocalCatch.remove( "categories"+authorId);

	}
	public void articleMinusOne(String authorId, String categoryName){
		QueryWrapper<Category> wrapper = new QueryWrapper<>();
		wrapper.eq("author_id",authorId);
		wrapper.eq("category",categoryName);
		Category category = categoryMapper.selectOne(wrapper);
		category.setArticleNum(category.getArticleNum()-1);
		categoryMapper.updateById(category);
		LocalCatch.put( "category"+category.getId(),category);
		LocalCatch.remove( "categories"+authorId);
	}

	public List<Category> getCategories(String authorId) {
		List<Category> categories;
		String key = "categories"+authorId;
		if ((categories = (List<Category>) LocalCatch.get(key))==null) {
			QueryWrapper<Category> wrapper = new QueryWrapper<>();
			wrapper.eq("author_id",authorId);
			categories = categoryMapper.selectList(wrapper);
			if (categories==null) return new ArrayList<>();
			LocalCatch.put(key, categories);
			return categories;
		}
		return categories;
	}

	public Category getCategory(Integer id) {
		Category category;
		String key = "category"+id;
		if ((category = (Category) LocalCatch.get(key))==null) {
			category = categoryMapper.selectById(id);
			if (category==null) return null;
			LocalCatch.put(key, category);
			return category;
		}
		return category;
	}

	// 添加一个分类,返回总分类个数
	public int addCategory(String authorId,String categoryName){
		QueryWrapper<Category> wrapper = new QueryWrapper<>();
		wrapper.eq("author_id",authorId);
		wrapper.eq("category",categoryName);
		Category category = categoryMapper.selectOne(wrapper);
		if (category!=null) return getCategories(authorId).size();
		// 若没有这个分类
		category = new Category(null,authorId, categoryName, 0);
		categoryMapper.insert(category);
		LocalCatch.remove("categories"+authorId);
		log.info("新增分类："+category.getCategory());
		return getCategories(authorId).size()+1;
	}

	// 更新一个全新的分类数组更新分类
	// -1 身份异常， -2 分类下还有文章
	public int updateCategories(String authorId, Category[] categories) {
		// 获取当前作者的全部分类
		List<Category> categoryList = getCategories(authorId);
		LocalCatch.remove("categories" + authorId);
		Category category1;
		for (Category category : categories) {
			if (category.getId()<0){
				// 负id表示新加分类
				// 检查分类是否存在
				boolean existFlag = false;
				for (int i = 0; i < categoryList.size(); i++) {
					if (categoryList.get(i).getCategory().equals(category.getCategory())){
						categoryList.remove(i);
						existFlag = true;
						break;
					}
				}
				if (!existFlag){
					category.setId(null);
					category.setAuthorId(authorId);
					categoryMapper.insert(category);
					log.info("新增分类："+category.getCategory());
				}
			}else{
				// 验证身份， 确保当前登录用户只能操作自己的内容
				category1 = getCategory(category.getId());
				if (category1==null) return -1;
				if (!category1.getAuthorId().equals(authorId)) return -1;
				categoryMapper.updateById(category);
				log.info("更新分类：原分类："+category1 + " --> 新分类"+category);
				// 移除 categoryList 的原本分类信息
				for (int i = 0; i < categoryList.size(); i++) {
					if (categoryList.get(i).getId().equals(category.getId())){
						categoryList.remove(i);
						break;
					}
				}
			}
		}
		// 删除 categoryList 剩余的分类
		for (Category category : categoryList) {
			if (category.getArticleNum() != 0) return -2;
			categoryMapper.deleteById(category);
			LocalCatch.remove("category" + category.getId());
			log.info("删除分类：" + category.getCategory());
		}
		return 1;
	}
}
