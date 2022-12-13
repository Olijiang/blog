import blog.config.LocalCatch;

/**
 * @author ZGB
 * @version 1.0
 * @description: TODO
 * @date 2022/11/11 13:50
 */
public class Localtest {

	public static void main(String[] args) {

		LocalCatch.put("articleList123_11","1111");
		LocalCatch.put("articleList123_22","2222");
		LocalCatch.put("articleList123_33","3333");
		LocalCatch.removeByPre("articleList");
		System.out.println(LocalCatch.get("articleList123_11"));
		System.out.println(LocalCatch.get("articleList123_22"));
		System.out.println(LocalCatch.get("articleList123_33"));

	}

}
