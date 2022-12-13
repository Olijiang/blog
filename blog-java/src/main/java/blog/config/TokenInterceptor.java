package blog.config;

import blog.utils.JwtUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @description: 请求拦截器,需要验证token的请求
 * @date: 2022/10/21 11:21
 */

@Slf4j
public class TokenInterceptor implements HandlerInterceptor {

	// 拦截的url
	private final String[] urls = {
			"/article/.*",
			"/image/.*",
			"/album/.*",
			"/user/.*"
	};

	// 进入controller层之前拦截请求
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
		String url = request.getRequestURI();
		String token = request.getHeader("token");

//		System.out.println(url);
		if (url.equals("/")) {
			response.sendRedirect(request.getContextPath()+"/index.html");
			return false;
		}

		for (String item : this.urls) {
			if (!url.matches(item)) return true;
		}
		if (url.equals("/error")){
			try{
				response.setCharacterEncoding("UTF-8");
				response.setContentType("application/json; charset=utf-8");
				ComResult res = ComResult.error("非法请求");
				PrintWriter out = response.getWriter();
				// 返回json信息给前端
				out.append(JSON.toJSONString(res)).flush();
				return false;
			} catch (Exception e){
				e.printStackTrace();
				response.sendError(500);
				return false;
			}
		}

		if (token!=null && JwtUtil.getUserFromToken(token)!=null) {
			// 可以解析出user
			return true;
		}else {
			System.out.println("越权访问"+url);
			try{
				response.setCharacterEncoding("UTF-8");
				response.setContentType("application/json; charset=utf-8");
				ComResult res = ComResult.error(401,"请先登录在访问！");
				PrintWriter out = response.getWriter();
				// 返回json信息给前端
				out.append(JSON.toJSONString(res)).flush();
				return false;
			} catch (Exception e){
				e.printStackTrace();
				response.sendError(500);
				return false;
			}
		}
	}

}
