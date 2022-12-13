package blog.controller;

import blog.config.ComResult;
import blog.config.LocalCatch;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;

/**
 * @author ZGB
 * @version 1.0
 * @description: TODO
 * @date 2022/11/6 15:26
 */
@Api("验证码")
@RestController
public class KaptchaController {
	@Autowired
	private DefaultKaptcha captchaProducer;

	@ApiOperation("获取验证码")
	@GetMapping("/getCode")
	public ComResult defaultKaptcha(String timeStamp) {
		ByteArrayOutputStream imgOutputStream = new ByteArrayOutputStream();
		try {
			//生产验证码字符串并保存到session中
			String verifyCode = captchaProducer.createText();
			LocalCatch.put(timeStamp,verifyCode);
			java.awt.image.BufferedImage challenge = captchaProducer.createImage(verifyCode);
			ImageIO.write(challenge, "jpg", imgOutputStream);
		} catch (Exception e) {
			return ComResult.error("验证码获取失败");
		}
		byte[] captchaOutputStream = imgOutputStream.toByteArray();

		return ComResult.success("验证码获取成功",captchaOutputStream);
	}
}
