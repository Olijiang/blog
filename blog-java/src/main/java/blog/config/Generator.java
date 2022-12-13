package blog.config;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

/**
 * @author ZGB
 * @version 1.0
 * @description: TODO
 * @date 2022/10/20 18:15
 */
public class Generator {
	private static final String URL = "jdbc:mysql:///blog?serverTimezone=Asia/Shanghai";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "123456";

	public static void tmain(String[] args) {
		String projectPath = System.getProperty("user.dir");
		System.out.println(projectPath);
		FastAutoGenerator.create(URL, USERNAME,PASSWORD )
				.globalConfig(builder -> {
					builder.author("ZGB") // 设置作者
							.enableSwagger() // 开启 swagger 模式
							.fileOverride() // 覆盖已生成文件
							.outputDir(projectPath+"\\src\\main\\java\\") // 指定输出目录
							.disableOpenDir()
							.commentDate("yyyy-MM-dd");
				})
				.packageConfig(builder -> {
					builder.parent("blog") // 设置父包名
							.moduleName(null) // 设置父包模块名
									.entity("entity")
//									.service("service")
//									.serviceImpl("service.impl")
									.serviceImpl("service")
									.mapper("mapper")
									.xml("mapper.xml")
									.controller("controller")
									.pathInfo(Collections.singletonMap(OutputFile.mapperXml, projectPath+"/src/main/resources/mapper/")); // 设置mapperXml生成路径
				})
				.strategyConfig(builder -> {
					builder.addInclude("album") // 设rag置需要生成的表名
							.addTablePrefix("t_", "c_"); // 设置过滤表前缀
				})
				.templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
				.execute();
	}
}
