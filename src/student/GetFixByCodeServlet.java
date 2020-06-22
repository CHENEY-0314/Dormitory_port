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

public class GetFixByCodeServlet extends HttpServlet {
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
			String fix_code = request.getParameter("fix_code").trim();
			
			MaintenanceRecord record=MainAppDAO.queryFixCode(fix_code);
			JSONObject jsonObject = new JSONObject();

			if(record!=null){
	        Map<String, String> message = new HashMap<>();
    		message.put("maintenance", record.getMaintenance());
    		message.put("remark", record.getRemark());
    		message.put("contact", record.getContact());
    		
    		jsonObject.put("result", message);
			}
			out.write(jsonObject.toString());
		}
	}

}
