package blog.config;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author ZGB
 * @version 1.0
 * @description: 公共返回对象
 * @date 2022/10/21 11:08
 */
@Data
@Component
public class ComResult {
	private int code;
	private String message;
	private Object data;

	private ComResult(){

	}

	private ComResult(int code, String msg, Object data){
		this.code = code;
		this.message = msg;
		this.data = data;
	}


	public static ComResult success(String msg, Object data){
		return new ComResult(200, msg, data);
	}

	public static ComResult success(String msg){
		return new ComResult(200, msg, null);
	}

	public static ComResult success(Object data){
		return new ComResult(200, "OK", data);
	}

	public static ComResult error(int code, String msg){
		return new ComResult(code, msg, null);
	}
	public static ComResult error(String msg, Object o ){
		return new ComResult(400, msg, 0);
	}

	public static ComResult error(String msg) {
		return new ComResult(400, msg, null);
	}

	public static ComResult success() {
		return new ComResult(200,"ok",null);
	}
}
