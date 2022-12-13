package blog.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ZGB
 * @version 1.0
 * @description: TODO
 * @date 2022/11/22 16:53
 */
@Controller
public class BeginController {

	@ApiOperation("页面转发")
	@RequestMapping("/blog")
	public void begin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("/index.html");
		requestDispatcher.forward(request,response);
	}
}
