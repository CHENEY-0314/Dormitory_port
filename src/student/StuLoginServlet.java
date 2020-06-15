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

public class StuLoginServlet extends HttpServlet {

	/*
	 * 先：http://localhost:8080/Dormitory/servlet/DBManager
	 * 后：http://localhost:8080/Dormitory/servlet/StuLoginServlet?s_id=201830000001&password=123456
	*/
	
	private static final long serialVersionUID = 1L;
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
 
		// 设置响应内容类型  
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
 
		try (PrintWriter out = response.getWriter()) {
 
			//获得请求中传来的学号和密码
			String ID = request.getParameter("s_id").trim();
			String password = request.getParameter("password").trim();
 
			//密码验证结果
			Boolean verifyResult = verifyLogin(ID, password);
 
			Map<String, String> params = new HashMap<>();
			JSONObject jsonObject = new JSONObject();
 
			if (verifyResult) {
				params.put("Result", "success");
				} else {
					params.put("Result", "failed");
					}
 
			jsonObject.put("params", params);
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
