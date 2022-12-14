package blog.service;

import blog.config.ComResult;
import blog.config.LocalCatch;
import blog.entity.Category;
import blog.entity.User;
import blog.mapper.UserMapper;
import blog.utils.JwtUtil;
import blog.utils.myUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Objects;

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
public class UserServiceImpl extends ServiceImpl<UserMapper, User>{

	@Resource
	private UserMapper userMapper;
	@Resource
	private CategoryServiceImpl categoryService;
	@Resource
	private TagServiceImpl tagService;

	public User getUserById(String authorId){
		User user;
		String key = "user" + authorId;
		if ((user = (User) LocalCatch.get(key)) == null) {
			user = userMapper.selectById(authorId);
			if (user==null) return null;
			LocalCatch.put(key, user);
			return user;
		}
		return user;
	}

	@Transactional
	public ComResult updateAuthor(User user, String[] tags, Category[] categories){
		//验证身份
		String authorId = Objects.requireNonNull(JwtUtil.getUserFromToken(user.getPassword())).getUsername();
		if (!authorId.equals(user.getUsername())) return ComResult.error("非本人操作");
		// 更新分类, 分类需要验证身份, 异常回滚操作
		int res = categoryService.updateCategories(authorId,categories);
		if(res==-1) throw new RuntimeException();
		if(res==-2) return ComResult.error(499,"当前分类下还有文章, 不能删除");
		// 更新标签
		tagService.updateTags(authorId, tags);
		// 更新用户信息
		User user1 = getUserById(authorId);
		user.setPassword(user1.getPassword());
		// 更新 author 信息
		if (!user.getCoverImg().equals("")){
			// 图片修改过, 删除原图片, 写入新图片
			myUtil.deleteFile(user1.getCoverImg());
			String path = myUtil.saveImg(user.getCoverImg(),authorId);
			user.setCoverImg(path);
		}else user.setCoverImg(user1.getCoverImg());
		if (!user.getImg().equals("")){
			myUtil.deleteFile(user1.getImg());
			String path = myUtil.saveImg(user.getImg(),authorId);
			user.setImg(path);
		}else user.setImg(user1.getImg());
		// 更新分类数量
		user.setCategoryNum(categories.length);
		userMapper.updateById(user);
		log.info(authorId+"：更新信息"+user);
		LocalCatch.put("user"+authorId,user);
		return ComResult.success();
	}


}
