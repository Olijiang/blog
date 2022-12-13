package blog.service;

import blog.config.ComResult;
import blog.entity.User;
import blog.mapper.UserMapper;
import blog.utils.JwtUtil;
import blog.utils.myUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

	public ComResult getUser(String authorId){
		User user = userMapper.selectById(authorId);
		if (user!=null){
			user.setPassword("");
			return ComResult.success("获取用户信息成功",user);
		}else {
			return ComResult.error("用户不存在");
		}
	}


	public ComResult updateAuthor(User user, String[] tags, String[] categories){
		//验证身份
		String authorId = Objects.requireNonNull(JwtUtil.getUserFromToken(user.getPassword())).getUsername();
		if (!authorId.equals(user.getUsername())) return ComResult.error("非本人操作");
		User user1 = userMapper.selectById(authorId);
		user.setPassword(null);
		// 更新 author 信息
		if (!user.getCoverImg().equals("")){
			// 图片修改过, 删除原图片, 写入新图片
			myUtil.deleteFile(user1.getCoverImg());
			String path = myUtil.saveImg(user.getCoverImg(),authorId);
			user.setCoverImg(path);
		}else user.setCoverImg(null);
		if (!user.getImg().equals("")){
			myUtil.deleteFile(user1.getImg());
			String path = myUtil.saveImg(user.getImg(),authorId);
			user.setImg(path);
		}else user.setImg(null);
		userMapper.updateById(user);
		log.info(authorId+"：更新信息"+user);
		// 更新 分类 和 标签
		categoryService.updateCategories(authorId,categories);
		tagService.updateTags(authorId, tags);

		return ComResult.success();
	}


}
