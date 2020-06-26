package administrator;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

public class AdmGetExchangeAppServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	// http://localhost:8080/Dormitory/servlet/AdmGetExchangeAppServlet?a_id=000001&password=123456
	
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
			String a_id = request.getParameter("a_id").trim();
			String password = request.getParameter("password").trim();
			
			JSONObject jsonObject = new JSONObject();
			Boolean verifyResult = verifyLogin(a_id, password);
			if(verifyResult){    //验证通过才能进行信息查询，返回的是json格式的数据
				jsonObject = AdmDAO.GetChangeApply();
			}
			out.write(jsonObject.toString());
		}
	}
	
	private Boolean verifyLogin(String a_id, String password) {
		administrator user = AdmLoginDAO.queryAdm(a_id);
		//账户密码验证
		return null != user && password.equals(user.getA_password());
		}
}
