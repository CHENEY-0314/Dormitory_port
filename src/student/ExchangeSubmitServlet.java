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

public class ExchangeSubmitServlet extends HttpServlet{

//http://localhost:8080/Dormitory/servlet/ExchangeSubmitServlet?s_id=201830660173&password=123456&target_id=201830660175&building=C11&room_num=126&bed_num=4&contact=13502246751&tbuilding=C10&troom_num=222&tbed_num=2&tcontact=13502243761&time=2000:06:12:12:33
	private static final long serialVersionUID = 0L;
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
			String target_id = request.getParameter("target_id").trim();
			String building = request.getParameter("building").trim();
			String room_num = request.getParameter("room_num").trim();
			String bed_num = request.getParameter("bed_num").trim();
			String t_building = request.getParameter("tbuilding").trim();
			String t_room_num = request.getParameter("troom_num").trim();
			String t_bed_num = request.getParameter("tbed_num").trim();
			String contact = request.getParameter("contact").trim();
			String t_contact = request.getParameter("tcontact").trim();
			String time = request.getParameter("time").trim();
 
			//密码验证结果
			Boolean verifyResult = verifyLogin(ID, password);
 
			Map<String, String> params = new HashMap<>();
			JSONObject jsonObject = new JSONObject();
 
			
			
			if (verifyResult) {
				String code = ExchangeApplyDAO.SubmitExchangeApp(ID, target_id, building, room_num, bed_num, t_building, t_room_num, t_bed_num, contact, t_contact, time);
				if(code != null) params.put("result", "success;"+code);
				else params.put("result", "failed");
				} else {
					params.put("result", "failed");
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
