package blog.config;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author ZGB
 * @version 1.0
 * @description: TODO
 * @date 2022/12/29 22:37
 */
public class TimerTest {

	public static void main(String[] args) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				System.out.println("-------任务执行--------");
			}
		}, 2000);
	}
}
