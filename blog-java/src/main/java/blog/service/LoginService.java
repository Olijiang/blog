package blog.service;

import blog.config.Result;
import blog.entity.Album;
import blog.entity.LoginInfo;
import blog.entity.Tag;
import blog.entity.User;
import blog.mapper.AlbumMapper;
import blog.mapper.TagMapper;
import blog.mapper.UserMapper;
import blog.utils.TokenUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

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
	private TagMapper tagMapper;
	// PWKey 加密密码的随机盐
	public Result login(LoginInfo loginInfo, String PWKey){
		String accUsername = loginInfo.getUsername();
		String accPassword = loginInfo.getPassword();
		User user = userMapper.selectById(accUsername);
		if (user==null) return Result.error("用户不存在");
		String password = DigestUtils.md5DigestAsHex((user.getPassword()+PWKey).getBytes()).substring(1,30);
		if (password.equals(accPassword)){
			// 登录成功
			String token =  TokenUtil.generateToken(loginInfo);
			user.setPassword(token);
			log.info("用户登录："+accUsername);
			return Result.success("登录成功",user);
		}else{
			return Result.error("密码错误");
		}
	}

	@Transactional
	//  开启事务, 发生异常时会回滚操作, 因此不能用try主动捕获异常
	public Result register(LoginInfo loginInfo) {
		String username = loginInfo.getUsername();
		String password = loginInfo.getPassword();
		User user = userMapper.selectById(username);
		if (user!=null) return Result.error("用户名已被占用, 请再想一个吧");

		// 添加登录账户
		user = new User(username, password, "作者name","作者简介","博客name", "博客简介", 0, 0, 0, "","");
		userMapper.insert(user);
		// 初始化相册
		Album album = new Album(null,username,"全部","",1);
		albumMapper.insert(album);
		// 初始化分类和标签
		initTag(username);
		return Result.success("注册成功");
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
