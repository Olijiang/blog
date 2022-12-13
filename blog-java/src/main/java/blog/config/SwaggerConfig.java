package blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.io.File;

/**
 * @author ZGB
 * @version 1.0
 * @description: swagger 内部继承了WebMvcConfigurationSupport这个类, 导致自定义的拦截器不会起效
 *               解决办法是重写 addInterceptors 和 addResourceHandlers方法
 * @date 2022/10/21 16:44
 */
@Configuration
//@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurationSupport {


	private final TokenInterceptor tokenInterceptor = new TokenInterceptor();

	// 添加拦截器
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(tokenInterceptor)
				.addPathPatterns("/**");
		super.addInterceptors(registry);
	}
	// 静态资源放行
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String projectPath = System.getProperty("user.dir");
		registry.addResourceHandler("/blog") // 访问路径
				.addResourceLocations("classpath:/static/index.html"); //映射后的真实路径,结尾必须加/

		registry.addResourceHandler("/**") // 访问路径
				.addResourceLocations("classpath:/static/"); //映射后的真实路径,结尾必须加/

		registry.addResourceHandler("/blogData/**")
				.addResourceLocations("file:///"+projectPath + File.separator+ "blogData" + File.separator);
		super.addResourceHandlers(registry);
	}

	@Bean
	public Docket createResApi(){
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.select()
				.apis(RequestHandlerSelectors.basePackage("blog.controller"))
				.paths(PathSelectors.any())
				.build();
	}

	private ApiInfo apiInfo(){
		return new ApiInfoBuilder()
				.title("blog接口文档")
				.description("API")
				.contact(new Contact("ZGB","xxx","xx@qq.com"))
				.version("1.0")
				.build();
	}
}
