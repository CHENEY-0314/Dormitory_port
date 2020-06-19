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

public class ReleaseNoteServlet extends HttpServlet {
	
private static final long serialVersionUID = 3L;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException{
		// 设置响应内容类型  
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
				
		try (PrintWriter out = response.getWriter()) {
			
			
			//http://localhost:8080/Dormitory/servlet/ReleaseNoteServlet?code=0001&head=测试&content=text&time=06:19:09:15
			//获得请求中传来的编号、标题、内容、时间
			String code = request.getParameter("code").trim();
			String head = request.getParameter("head").trim();
			String content = request.getParameter("content").trim();
			String time = request.getParameter("time").trim();
			
			//向数据库中插入数据
			Map<String, String> params = new HashMap<>();
			JSONObject jsonObject = new JSONObject();

			int result = ReleaseNoteDAO.noteRelease(code, head, content, time);
 
			if (result > 0) {
				params.put("Result", "Success: "+String.valueOf(content.length()));
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
