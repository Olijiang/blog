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
public class Result {
	private int code;
	private String message;
	private Object data;

	private Result(){

	}

	private Result(int code, String msg, Object data){
		this.code = code;
		this.message = msg;
		this.data = data;
	}


	public static Result success(String msg, Object data){
		return new Result(200, msg, data);
	}

	public static Result success(String msg){
		return new Result(200, msg, null);
	}

	public static Result success(Object data){
		return new Result(200, "OK", data);
	}

	public static Result error(int code, String msg){
		return new Result(code, msg, null);
	}
	public static Result error(String msg, Object o ){
		return new Result(400, msg, 0);
	}

	public static Result error(String msg) {
		return new Result(400, msg, null);
	}

	public static Result success() {
		return new Result(200,"ok",null);
	}
}
