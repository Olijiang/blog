package blog.utils;

import blog.entity.LoginInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author ZGB
 * @version 1.0
 * @description: Jwt工具类
 * @date 2022/10/21 10:31
 */
@Component
@Slf4j
public class TokenUtil {


	private final static String secret = "yg*G_f1#@0.,2Ka1.a123_k)12l1k24i9jda(*sg0..,/'123-oo0949^*63";
	private static final long expiration = 1800000; // 30分钟
	private static final long refreshGap = 180000; // 过期前刷新时间 3分钟

	/**
	 * @description: 根据用户信息生成token
	 * @param:  UserInfo userInfo
	 * @return: String token
	 * @date: 2022/10/21 11:03
	 */
	public static String generateToken(LoginInfo loginInfo) {
		//创建payload的私有声明（根据特定的业务需要添加，如果要拿这个做验证，一般是需要和jwt的接收方提前沟通好验证方式的）
		Map<String, Object> claims = new HashMap<>();
		claims.put("token", loginInfo);
		//生成签发人
		String subject = loginInfo.getUsername();
		JwtBuilder builder = Jwts.builder()
				//如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
				.setClaims(claims)
				//设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
				.setId(UUID.randomUUID().toString())
				//iat: jwt的签发时间
				.setIssuedAt(new Date())
				//代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串，可以存放什么userid，roldid之类的，作为什么用户的唯一标志。
				.setSubject(subject)
				//设置签名使用的签名算法和签名使用的秘钥
				.signWith(SignatureAlgorithm.HS256, secret)
				//设置过期时间
				.setExpiration(new Date(System.currentTimeMillis()+ expiration));
		return builder.compact();
	}
	public static String generateToken(Map<String, Object> claims) {
		//创建payload的私有声明（根据特定的业务需要添加，如果要拿这个做验证，一般是需要和jwt的接收方提前沟通好验证方式的）
		JwtBuilder builder = Jwts.builder()
				//如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
				.setClaims(claims)
				//设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
				.setId(UUID.randomUUID().toString())
				//iat: jwt的签发时间
				.setIssuedAt(new Date())
				//设置签名使用的签名算法和签名使用的秘钥
				.signWith(SignatureAlgorithm.HS256, secret)
				//设置过期时间
				.setExpiration(new Date(System.currentTimeMillis()+ expiration));
		return builder.compact();
	}

	// 从token中获取用户信息
	public static LoginInfo getUserFromToken(String token) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			Claims claims = Jwts.parser()
					.setSigningKey(secret)
					.parseClaimsJws(token)
					.getBody();
			return objectMapper.convertValue(claims.get("token"), LoginInfo.class);
		} catch (Exception e) {
			log.warn("无效token");
			return null;
		}
	}

	// 刷新Token
	public static String refreshToken(String token) {
		try {
			Claims claims = Jwts.parser()
					.setSigningKey(secret)
					.parseClaimsJws(token)
					.getBody();
			if (claims.getExpiration().getTime() - System.currentTimeMillis() < refreshGap){
				claims.setExpiration(new Date(System.currentTimeMillis()+ expiration));
				String newToken = generateToken(claims);
				log.info("token刷新成功");
				return newToken;
			}
			return null;
		} catch (Exception e) {
			log.warn("token刷新失败");
			return null;
		}
	}

	public static void main(String[] args) {
		String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyMDIwIiwiZXhwIjoxNjcyNDExMzMxLCJpYXQiOjE2NzI0MTEzMzAsImp0aSI6IjNkZWIxZDczLTI1MmUtNDRkMC05ZWVlLTBlNzkxODI2YzU1ZiIsInRva2VuIjp7InVzZXJuYW1lIjoiMjAyMCIsInBhc3N3b3JkIjoiKioqIiwiY29kZSI6InA4eW0iLCJ0aW1lU3RhbXAiOiIifX0.yIyvdbJmhUNA_RN1KFZwPiGfWd5KIGMIp9jzX26KNQQ";
		System.out.println(getUserFromToken(token));
	}
}
