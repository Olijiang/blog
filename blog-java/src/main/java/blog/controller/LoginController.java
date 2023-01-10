package blog.controller;

import blog.config.Result;
import blog.config.LocalCache;
import blog.entity.LoginInfo;
import blog.service.LoginService;
import blog.utils.TokenUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


/**
 * @author ZGB
 * @version 1.0
 * @description: TODO
 * @date 2022/11/6 14:30
 */
@RestController
public class LoginController {

	@Autowired
	private LoginService loginService;

	@ApiOperation("登录操作")
	@PostMapping("/login")
	public Result login(@RequestBody LoginInfo loginInfo, HttpServletRequest request){
		String ip =  request.getRemoteAddr();
		String key = DigestUtils.md5DigestAsHex(ip.getBytes()).substring(5,20);
		String PWKey = (String) LocalCache.get(key+"PWKey");
		String code = (String) LocalCache.get(key);
		if (loginInfo.getCode().equals(code)){
			return loginService.login(loginInfo,PWKey);
		}else{
			return Result.error("验证码错误");
		}
	}

	@ApiOperation("刷新token")
	@GetMapping("/refreshToken")
	public Result refreshToken(@RequestHeader("token") String token ){
		if (token!=null && TokenUtil.getUserFromToken(token)!=null){
			LoginInfo loginInfo = TokenUtil.getUserFromToken(token);
			token = TokenUtil.generateToken(loginInfo);
			return Result.success("刷新token成功",token);
		}
		return Result.error("非法访问!");
	}

	@ApiOperation("注册操作")
	@PostMapping("/register")
	public Result register(@RequestBody LoginInfo loginInfo){
		String code = (String) LocalCache.get(loginInfo.getTimeStamp());
		if (loginInfo.getCode().equals(code)){
			return loginService.register(loginInfo);
		}else{
			return Result.error("验证码错误");
		}
	}
}
