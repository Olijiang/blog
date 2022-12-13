package blog.controller;


import blog.config.ComResult;
import blog.entity.User;
import blog.service.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ZGB
 * @since 2022-11-06
 */
@RestController
@RequestMapping("/user")
public class UserController {

	@Resource
	private ObjectMapper objectMapper;
	@Resource
	private UserServiceImpl userService;



	@PostMapping("/update")
	@Transactional
	public ComResult update(@RequestBody Map<String, Object> map){

		User user = objectMapper.convertValue(map.get("author"),User.class);
		String[] tags = objectMapper.convertValue(map.get("tags"),String[].class);
		String[] categories = objectMapper.convertValue(map.get("categories"),String[].class);

		return userService.updateAuthor(user, tags, categories);
	}

}
