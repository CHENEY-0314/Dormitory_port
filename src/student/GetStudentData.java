package student;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

//此接口用于 根据学号查询学生的姓名、性别、宿舍（楼号、宿舍号、床位号）
public class GetStudentData extends HttpServlet {
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
			//从浏览器url获取的参数
			String s_id = request.getParameter("s_id").trim();
			String password = request.getParameter("password").trim();
			
			JSONObject jsonObject = new JSONObject();
			Boolean verifyResult = verifyLogin(s_id, password);
			if(verifyResult){                                     //验证通过才能进行信息查询，返回的是json格式的数据
				jsonObject = studentDAO.GetStudentByStuId(s_id);
				}else{
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
