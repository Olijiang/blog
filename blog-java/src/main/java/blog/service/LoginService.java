package blog.service;

import blog.config.ComResult;
import blog.entity.*;
import blog.mapper.AlbumMapper;
import blog.mapper.CategoryMapper;
import blog.mapper.TagMapper;
import blog.mapper.UserMapper;
import blog.utils.JwtUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author ZGB
 * @version 1.0
 * @description: TODO
 * @date 2022/11/6 14:37
 */
@Slf4j
@Component
public class LoginService {

	@Resource
	private UserMapper userMapper;
	@Resource
	private AlbumMapper albumMapper;
	@Resource
	private CategoryMapper categoryMapper;
	@Resource
	private TagMapper tagMapper;

	public ComResult login(LoginInfo loginInfo){
		String username = loginInfo.getUsername();
		String password = loginInfo.getPassword();
		User user = userMapper.selectById(username);
		if (user==null) return ComResult.error("用户不存在");
		if (user.getPassword().equals(password)){
			// 登录成功
			loginInfo.setPassword("***");
			String token =  JwtUtil.generateToken(loginInfo);
			user.setPassword(token);
			log.info("用户登录："+username);
			return ComResult.success("登录成功",user);
		}else{
			return ComResult.error("密码错误");
		}
	}

	@Transactional
	//  开启事务, 发生异常时会回滚操作, 因此不能用try主动捕获异常
	public ComResult register(LoginInfo loginInfo) {
		String username = loginInfo.getUsername();
		String password = loginInfo.getPassword();
		User user = userMapper.selectById(username);
		if (user!=null) return ComResult.error("用户名已被占用, 请再想一个吧");

		// 添加登录账户
		user = new User(username, password, "作者name","作者简介","博客name", "博客简介", 0, 0, 0, "","");
		userMapper.insert(user);
		// 初始化相册
		Album album = new Album(null,username,"全部","");
		albumMapper.insert(album);
		// 初始化分类和标签
		initCategory(username);
		initTag(username);
		return ComResult.success("注册成功");
	}

	private void initCategory(String authorId) {
		Category category = new Category();
		category.setAuthorId(authorId);
		JSONObject json = new JSONObject();
		String[] temp = {};
		json.put("categories", temp);
		category.setCategories(json.toJSONString());
		categoryMapper.updateById(category);
	}
	private void initTag(String authorId){
		Tag tag = new Tag();
		tag.setAuthorId(authorId);
		JSONObject json = new JSONObject();
		String[] temp = {};
		json.put("tags", temp);
		tag.setTags(json.toJSONString());
		tagMapper.updateById(tag);
	}
}
