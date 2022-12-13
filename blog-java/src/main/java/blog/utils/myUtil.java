package blog.utils;


import blog.config.PathConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * @author ZGB
 * @version 1.0
 * @description: 图片操作工具类
 * @date 2022/11/9 22:21
 */
@Slf4j
@Component
public class myUtil {

	public static boolean writeBase64Img(String base64ImgData, String storagePath) {
		byte[] bs = Base64.decodeBase64(base64ImgData);
		try (FileOutputStream fos = new FileOutputStream(storagePath)){
			fos.write(bs);
			fos.flush();
			return true;
		} catch (Exception e) {
			log.warn("base64文件保存异常"+e);
			return false;
		}
	}

	public static boolean writeTxt(String content,String storagePath) {
		File file=new File(storagePath);//定义操作文件
		try (PrintWriter pw=new PrintWriter(new FileOutputStream(file))){
			pw.write(content);
			log.info("TXT文件保存成功"+storagePath);
			return true;
		}catch (Exception e){
			log.warn("TXT文件保存失败"+e);
			return false;
		}
	}
	public static String readTxt(String path){
		/*
		* 读取 resources 下面的文件, path 为相对于 resources 目前的地址
		* */
//		String projectParentPath = getProjectParentPath();
//		String filePath = projectParentPath + dataPath +"articles" +File.separator + path;
		File file=new File(path);
		if(!file.exists()) {
			log.warn("文件不存在："+path);
			return null;
		}
		try(FileReader fileReader = new FileReader(ResourceUtils.getFile(path))) {
			int ch = 0;
			StringBuilder sb = new StringBuilder();
			while ((ch = fileReader.read()) != -1) {
				sb.append((char) ch);
			}
			return sb.toString();
		}catch (Exception e){
			log.warn("读取文件失败"+path);
			log.warn(""+e);
			return null;
		}
	}
	/**
	 * @return: 返回两个数组的并集
	 * @date: 2022/11/11 10:26
	 */
	public static String[] union(String[] arr1, String[] arr2) {
		Set<String> set = new HashSet<String>();
		if (arr1!=null) set.addAll(Arrays.asList(arr1));
		if (arr2!=null) set.addAll(Arrays.asList(arr2));
		return set.toArray(new String[0]);
	}


	public static void tmain(String[] args) {
		String projectPath = System.getProperty("user.dir");
		String[] temp = projectPath.split("\\"+File.separator);
		String projectName = temp[temp.length-1];
		projectPath = projectPath.substring(0,projectPath.length()-projectName.length());
		System.out.println(projectPath);
	}

	public static String saveImg(String imgData, String authorId){
		String projectPath = System.getProperty("user.dir");
		// 截取图片信息
//		System.out.println(imgData);
		String[] info = imgData.split(";base64,");
		// 截取图片文件后缀
		String fileSuffix = info[0].substring(11);
		// 图片数据
		String base64ImgData = info[1];
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSS", Locale.US); // 格式化时间
		String date = dtf.format(LocalDateTime.now());
		// 文件名
		String fileName = date + "_" + authorId + "." + fileSuffix;
		String storagePath = projectPath + File.separator + PathConfig.dataPath + File.separator +PathConfig.imgPath + File.separator + fileName;
		// relativePath src链接直接请求数据，需要加 dataPath 资源映射
		String relativePath = PathConfig.dataPath + "/" + PathConfig.imgPath + "/" + fileName;
		if (myUtil.writeBase64Img(base64ImgData, storagePath)){
			log.info("图片保存成功: " + relativePath);
			return relativePath;
		}else {
			log.warn("图片保存失败: " + relativePath);
			return null;
		}
	}

	public static void deleteFile(String filePath){
		String projectPath = System.getProperty("user.dir");
		try {
			File file = new File(projectPath+File.separator+filePath);
			if(file.delete()) log.info("成功删除文件 "+filePath);
			else log.warn("删除文件失败 "+ filePath );
		} catch(Exception e) {
			log.warn("删除文件失败 "+ filePath + e);
		}
	}
}
