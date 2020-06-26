package student;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

public class ExchangingServlet extends HttpServlet {
	
	//http://localhost:8080/Dormitory/servlet/ExchangingServlet?s_id=201830660178&password=123456
	
		private static final long serialVersionUID = 1L;
		@Override
		protected void doPost(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
	 
			// 设置响应内容类型  
			response.setContentType("text/html;charset=utf-8");
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
	 
			try (PrintWriter out = response.getWriter()) {
				//从浏览器url获取的参数
				String s_id = request.getParameter("s_id").trim();
				String password = request.getParameter("password").trim();
				
				Map<String, String> message = new HashMap<>();
				JSONObject jsonObject = new JSONObject();
				Boolean verifyResult = verifyLogin(s_id, password);
				if(verifyResult){    //验证通过才能进行信息查询，返回的是json格式的数据
					if(!ExchangeApplyDAO.isExchanging(s_id)) message.put("result", "success");
					else message.put("result", "failed");
				}
				else message.put("result", "failed");
				jsonObject.put("params", message);
				out.write(jsonObject.toString());
			}
		}
		@Override
		protected void doGet(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			doPost(request, response);
			}
		
		//验证用户名密码是否正确
		private Boolean verifyLogin(String userName, String password) {
			student user = studentDAO.queryUser(userName);
			//账户密码验证
			return null != user && password.equals(user.getPassword());
			}
	
}
