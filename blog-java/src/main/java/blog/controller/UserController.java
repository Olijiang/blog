package blog.controller;


import blog.config.LocalCache;
import blog.config.Result;
import blog.entity.Category;
import blog.entity.User;
import blog.service.UserServiceImpl;
import blog.utils.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ZGB
 * @since 2022-11-06
 */
@RestController
@RequestMapping("/user")
public class UserController {

	@Resource
	private ObjectMapper objectMapper;
	@Resource
	private UserServiceImpl userService;



	@PostMapping("/update")
	@Transactional
	public Result update(@RequestBody Map<String, Object> map,@RequestHeader("token") String token){
		User user = objectMapper.convertValue(map.get("author"),User.class);
		String[] tags = objectMapper.convertValue(map.get("tags"),String[].class);
		Category[] categories = objectMapper.convertValue(map.get("categories"),Category[].class);
		return userService.updateAuthor(user, tags, categories, token);
	}

	@PostMapping("/resetPwd")
	public Result resetPassword(@RequestBody Map<String, Object> map, @RequestHeader("token") String token,
	                            HttpServletRequest request){
		String authorId = Objects.requireNonNull(TokenUtil.getUserFromToken(token)).getUsername();
		// 跟登录一样，用盐PWKey加密之后的
		String oldPwd = objectMapper.convertValue(map.get("oldPwd"),String.class);
		// 没有用盐PWKey加密的原始密文，存数据库做比对使用
		String newPwd = objectMapper.convertValue(map.get("newPwd"),String.class);
		String accCode = objectMapper.convertValue(map.get("code"),String.class);

		String ip =  request.getRemoteAddr();
		String key = DigestUtils.md5DigestAsHex(ip.getBytes()).substring(5,20);
		String PWKey = (String) LocalCache.get(key+"PWKey");
		String code = (String) LocalCache.get(key);
		if (accCode.equals(code)){
			return userService.resetPassword(authorId, oldPwd, newPwd, PWKey);
		}else{
			return Result.error("验证码错误");
		}
	}
}
