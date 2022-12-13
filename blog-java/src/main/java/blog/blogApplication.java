package blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author ZGB
 * @version 1.0
 * @description: TODO
 * @date 2022/11/6 14:08
 */
@SpringBootApplication
@MapperScan("blog.mapper")
@EnableConfigurationProperties
@EnableTransactionManagement
public class blogApplication {
	public static void main(String[] args) {
		SpringApplication.run(blogApplication.class, args);
	}
}
