package blog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author ZGB
 * @version 1.0
 * @description: TODO
 * @date 2022/11/17 9:49
 */
@Component
public class PathConfig {

	public static String serverIP;
	public static String dataPath;
	public static String imgPath;
	public static String articlePath;


	@Value("${blogserver.serverIP}")
	private String serverIPL;
	@Value("${blogserver.dataPath}")
	private String dataPathL;
	@Value("${blogserver.imgPath}")
	private String imgPathL;
	@Value("${blogserver.articlePath}")
	private String articlePathL;

	@PostConstruct
	private void setServerIP() {
		serverIP = this.serverIPL;
	}
	@PostConstruct
	private void setDataPath() {
		dataPath = this.dataPathL;
	}
	@PostConstruct
	private void setImgPath() {
		imgPath = this.imgPathL;
	}
	@PostConstruct
	private void setArticlePath() {
		articlePath = this.articlePathL;
	}


	public PathConfig() {
	}

	private String getServerIPL() {
		return serverIPL;
	}

	private void setServerIPL(String serverIPL) {
		this.serverIPL = serverIPL;
	}

	private String getDataPathL() {
		return dataPathL;
	}

	private void setDataPathL(String dataPathL) {
		this.dataPathL = dataPathL;
	}

	private String getImgPathL() {
		return imgPathL;
	}

	private void setImgPathL(String imgPathL) {
		this.imgPathL = imgPathL;
	}

	private String getArticlePathL() {
		return articlePathL;
	}

	private void setArticlePathL(String articlePathL) {
		this.articlePathL = articlePathL;
	}

	@Override
	public String toString() {
		return "PathConfig{" +
				"serverIP='" + serverIP + '\'' +
				", dataPath='" + dataPath + '\'' +
				", imgPath='" + imgPath + '\'' +
				", articlePath='" + articlePath + '\'' +
				'}';
	}


}
