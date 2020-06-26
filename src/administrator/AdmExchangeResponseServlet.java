package administrator;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

public class AdmExchangeResponseServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	// http://localhost:8080/Dormitory/servlet/AdmExchangeResponseServlet?a_id=000001&password=123456&change_code=8000&s_id=201830660178&target_id=201830660174&time=2018:06:24:10:00&agree=0   (1代表同意，0代表不同意)
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		// 设置响应内容类型  
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
				
		try (PrintWriter out = response.getWriter()) {
			
			String a_id = request.getParameter("a_id").trim();
			String password = request.getParameter("password").trim();
			String change_code = request.getParameter("change_code").trim();
			String time = request.getParameter("time").trim();
			String agree = request.getParameter("agree").trim();
			String target_id = request.getParameter("target_id").trim();
			String s_id = request.getParameter("s_id").trim();
			
			Map<String, String> params = new HashMap<>();
			JSONObject jsonObject = new JSONObject();
			Boolean verifyResult = verifyLogin(a_id, password);
			if(verifyResult){    //验证通过才能进行信息查询，返回的是json格式的数据
				if(AdmDAO.admResponseExchangeApply(change_code, agree, time, target_id, s_id)) params.put("result", "success");
				else params.put("result", "failed");
			}
			jsonObject.put("params", params);
			out.write(jsonObject.toString());
		}
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException{
		doPost(request, response);
	}
	
	private Boolean verifyLogin(String a_id, String password) {
		administrator user = AdmLoginDAO.queryAdm(a_id);
		//账户密码验证
		return null != user && password.equals(user.getA_password());
		}
}
