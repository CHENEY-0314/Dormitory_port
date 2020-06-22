package student;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

public class ChangeStuContactServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
		 throws ServletException, IOException {
	 doPost(request, response);
	 }
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 设置响应内容类型  
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
 
		try (PrintWriter out = response.getWriter()) {
			String s_id = request.getParameter("s_id").trim();	
			String password = request.getParameter("password").trim();
			String number = request.getParameter("number").trim();
			
			Boolean verifyResult = verifyLogin(s_id, password);
			JSONObject jsonObject = new JSONObject();

			if(verifyResult){                                     //同样的验证通过才能删除，返回的是json格式的数据
				studentDAO.ChangeNumber(number,s_id);
				jsonObject.put("result", "success");
				}else{
				jsonObject.put("result", "fail");
				}
			out.write(jsonObject.toString());
			}
		}
	
	private Boolean verifyLogin(String s_id, String password) {
		student user = studentDAO.queryUser(s_id);
		//账户密码验证
		return null != user && password.equals(user.getPassword());
		}

}
