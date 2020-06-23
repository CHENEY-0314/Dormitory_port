package student;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

public class ChangeScreenServlet extends HttpServlet {

	private static final long serialVersionUID = 11L;
	
	// http://localhost:8080/Dormitory/servlet/ChangeScreenServlet?building=C10&floor=1;2&bed_num=0   (0代表随意)
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException{
		// 设置响应内容类型  
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
				
		try (PrintWriter out = response.getWriter()) {
			
			String building = request.getParameter("building").trim();
			String[] floor = request.getParameter("floor").trim().split(";");
			String[] bed_num = request.getParameter("bed_num").trim().split(";");

			JSONObject jsonObject = new JSONObject();
			jsonObject = ChangeApplyDAO.ChangeScreen(building, floor, bed_num);
 
 			out.write(jsonObject.toString());
			}
		}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException{
		doPost(request, response);
	}

}
