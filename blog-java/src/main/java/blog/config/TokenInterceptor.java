package blog.config;

import blog.utils.TokenUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

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

		log.info(url);
		// 重定位到开始页面
		if (url.equals("/")) {
			response.sendRedirect(request.getContextPath()+"/index.html");
			return false;
		}
		// 处理错误请求
		if (url.equals("/error")){
			try{
				response.setCharacterEncoding("UTF-8");
				response.setContentType("application/json; charset=utf-8");
				Result res = Result.error("非法请求");
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
		// url 挨个匹配拦截列表的模式串
		boolean flag = false;
		for (String item : this.urls) {
			if (url.matches(item)){
				// 匹配成功，进入拦截阶段
				flag = true;
				break;
			}
		}
		// 放行不在列表的url
		if (!flag) return true;
		// 处理需要拦截的url
		if (token!=null && TokenUtil.getUserFromToken(token)!=null) {
			// 检测token是否快过期
			String newToken = TokenUtil.refreshToken(token);
			if (newToken!=null) response.addHeader("token",newToken);
			return true;
		}else {
			log.warn("越权访问"+url);
			try{
				response.setCharacterEncoding("UTF-8");
				response.setContentType("application/json; charset=utf-8");
				Result res = Result.error(401,"登录信息已失效,请重新登录！");
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

	@Override
	public void postHandle(
			HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView)  {


	}
}
