package student;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

public class ExchangeFinishServlet extends HttpServlet {

	private static final long serialVersionUID = 2L;
	
// http://localhost:8080/Dormitory/servlet/ExchangeFinishServlet?change_code=8000&s_id=201830660178&password=123456&name=陈晓杰&building=C10&room_num=145&bed_num=1&target_id=201830660175&tname=张三&tbuilding=C11&troom_num=121&tbed_num=3&time=2000:06:12:12:33
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException{

		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
				
		try (PrintWriter out = response.getWriter()) {
			
			String change_code = request.getParameter("change_code").trim();
			String s_id = request.getParameter("s_id").trim();
			String password = request.getParameter("password").trim();
			String name = request.getParameter("name").trim();
			String building = request.getParameter("building").trim();
			String room_num = request.getParameter("room_num").trim();
			String bed_num = request.getParameter("bed_num").trim();
			String target_id = request.getParameter("target_id").trim();
			String tname = request.getParameter("tname").trim();
			String t_building = request.getParameter("tbuilding").trim();
			String t_room_num = request.getParameter("troom_num").trim();
			String t_bed_num = request.getParameter("tbed_num").trim();
			String time = request.getParameter("time").trim();		

			JSONObject jsonObject = new JSONObject();
			Boolean verifyResult = verifyLogin(s_id, password);
//			boolean result = ExchangeApplyDAO.exchangeFinish(s_id, name, building, room_num, bed_num, target_id, tname, t_building, t_room_num, t_bed_num, time);
 
			if (verifyResult) {
				if(ExchangeApplyDAO.exchangeFinish(change_code, s_id, name, building, room_num, bed_num, target_id, tname, t_building, t_room_num, t_bed_num, time)) jsonObject.put("result", "success");
				else jsonObject.put("result", "failed");
				} else {
					jsonObject.put("result", "failed");
					}
			
 			out.write(jsonObject.toString());
			}
		}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException{
		doPost(request, response);
	}
	
	//验证用户名密码是否正确
	private Boolean verifyLogin(String userName, String password) {
		student user = studentDAO.queryUser(userName);
		//账户密码验证
		return null != user && password.equals(user.getPassword());
		}
	
}
