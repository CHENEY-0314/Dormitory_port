package student;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

public class UpdataFixApplyServlet extends HttpServlet {
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
			String fix_code = request.getParameter("fix_code").trim();	
			String maintenance = request.getParameter("maintenance").trim();
			String remark = request.getParameter("remark").trim();
			String contact = request.getParameter("contact").trim();
			String time = request.getParameter("time").trim();
			
			JSONObject jsonObject = new JSONObject();
			
			if(studentDAO.ChangeFixApply(fix_code, maintenance, remark, contact, time)){
				jsonObject.put("result", "success");
				}else{
				jsonObject.put("result", "fail");
				}
			out.write(jsonObject.toString());
			}
		}

}
