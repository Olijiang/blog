/**
 * @author ZGB
 * @version 1.0
 * @description: TODO
 * @date 2022/11/11 13:50
 */
public class Localtest {

	public static void main(String[] args) throws InterruptedException {

		String[] urls = {
				"/article/.*",
				"/image/.*",
				"/album/.*",
				"/user/.*"
		};
		String url = "/article/get";
		for (String item : urls) {
			if (!url.matches(item)){
				System.out.println(url+"不匹配");

			}else{
				System.out.println(url+"匹配");
			}
		}

	}

}
