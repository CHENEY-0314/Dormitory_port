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

public class MaintenanceApplyServlet extends HttpServlet{

	private static final long serialVersionUID = 2L;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException{
		// 设置响应内容类型  
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
				
		try (PrintWriter out = response.getWriter()) {
			
			
			//http://localhost:8080/Dormitory/servlet/MaintenanceApplyServlet?s_id=201830660178&building=C10&room_num=101&maintenance=E&time=00:00:00:00contact=1000
			//获得请求中传来的学号、楼栋、宿舍号、维修事项、时间、联系方式
			String ID = request.getParameter("s_id").trim();
			String building = request.getParameter("building").trim();
			String room_num = request.getParameter("room_num").trim();
			String maintenance = request.getParameter("maintenance").trim();
			String time = request.getParameter("time").trim();
			String contact = request.getParameter("contact").trim();
			
			
			
			//向数据库中插入数据
			Map<String, String> params = new HashMap<>();
			JSONObject jsonObject = new JSONObject();

			boolean result = MainAppDAO.insertApp(ID, building, room_num, maintenance, time, contact);
 
			if (result) {
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
			throws ServletException, IOException{
		doPost(request, response);
	}
	
}
