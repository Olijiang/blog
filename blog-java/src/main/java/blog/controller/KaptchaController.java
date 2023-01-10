package blog.controller;

import blog.config.LocalCache;
import blog.config.Result;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;

/**
 * @author ZGB
 * @version 1.0
 * @description: TODO
 * @date 2022/11/6 15:26
 */
@Api("验证码")
@RestController
@Slf4j
public class KaptchaController {
	@Autowired
	private DefaultKaptcha captchaProducer;

	@ApiOperation("获取验证码")
	@GetMapping("/getCode")
	public Result defaultKaptcha(HttpServletRequest request) {
		ByteArrayOutputStream imgOutputStream = new ByteArrayOutputStream();
		try {
			// ip 的 md5 作为验证码的识别标志
			String ip =  request.getRemoteAddr();
			String ipMD5 = DigestUtils.md5DigestAsHex(ip.getBytes()).substring(5,20);
			String verifyCode = captchaProducer.createText();
			// 记入缓存，60s 后清除
			LocalCache.put(ipMD5,verifyCode,60000L);
			java.awt.image.BufferedImage challenge = captchaProducer.createImage(verifyCode);
			ImageIO.write(challenge, "jpg", imgOutputStream);

			String imgBase64 = Base64.encodeBase64String(imgOutputStream.toByteArray());
			String PWKey = imgBase64.substring(1000,1020);
			LocalCache.put(ipMD5+"PWKey",PWKey,60000L);
			return Result.success("验证码获取成功",imgBase64);
		} catch (Exception e) {
			log.warn("验证码获取失败"+e);
			return Result.error("验证码获取失败");
		}

	}
}
