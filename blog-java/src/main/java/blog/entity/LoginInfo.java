package blog.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author ZGB
 * @version 1.0
 * @description: TODO
 * @date 2022/11/6 14:23
 */
@Data
@ApiModel
public class LoginInfo {

	private String username;
	private String password;
	private String code;
	private String timeStamp;
}
