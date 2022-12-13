package blog.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @author ZGB
 * @version 1.0
 * @description: TODO
 * @date 2022/11/6 15:14
 */
@Component
public class KaptchaConfig {
		@Bean
		public DefaultKaptcha getDefaultKaptcha() {
			com.google.code.kaptcha.impl.DefaultKaptcha defaultKaptcha = new com.google.code.kaptcha.impl.DefaultKaptcha();
			Properties properties = new Properties();
			// 图片边框
			properties.setProperty("kaptcha.border", "no");
			// 边框颜色
			properties.setProperty("kaptcha.border.color", "black");
			//边框厚度
			properties.setProperty("kaptcha.border.thickness", "1");
			// 图片宽
			properties.setProperty("kaptcha.image.width", "100");
			// 图片高
			properties.setProperty("kaptcha.image.height", "50");
			//验证码长度
			properties.setProperty("kaptcha.textproducer.char.length", "4");
			// 取消干扰线
			properties.setProperty("kaptcha.noise.impl", "com.google.code.kaptcha.impl.NoNoise");
			//字体
			properties.setProperty("kaptcha.textproducer.font.names", "宋体");
			//字高
			properties.setProperty("kaptcha.textproducer.font.size", "30");
			//字体颜色
			properties.setProperty("kaptcha.textproducer.font.color", "black");
			//文字间隔
			properties.setProperty("kaptcha.textproducer.char.space", "5");
			//背景开始
			properties.setProperty("kaptcha.background.clear.from", "white");
			//背景渐变结束
			properties.setProperty("kaptcha.background.clear.to", "white");
			Config config = new Config(properties);
			defaultKaptcha.setConfig(config);
			return defaultKaptcha;
		}
}