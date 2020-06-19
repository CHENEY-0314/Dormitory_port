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
			
//http://localhost:8080/Dormitory/servlet/ReleaseNoteServlet?
//code=1002&
//head=%E5%A4%9A%E5%AD%97%E6%B5%8B%E8%AF%95&
//content=adsfa64dsf65dsa6f54dsf564sd5f64a6df43dsaf13sa1fs3da5f4as3df84as6g2dsa1gv2c13zvzx54vg6afd8g4awfafadsf23r1fsad3f4sd6af4dsafadsfglhsdakvbzcxvknzcx,mvnkaghoawifjsldafkmasdfadsf6x54zv65cx1zv3zxvch%E8%B6%85%E7%BA%A7%E6%97%A0%E6%95%8C%E6%B5%8B%E8%AF%95%E5%8F%91%E5%93%88%E7%9A%84%E5%81%A5%E5%BA%B7%E4%BA%86%E4%BA%86%E4%BA%86%E4%BA%86%E4%BA%86%E4%BA%86%E4%BA%86%E4%BA%86%E4%BA%86%E4%BA%86%E4%BA%86%E4%BA%86%E4%BA%86%E4%BA%86%E6%89%93%E5%BC%80%E9%A1%B6%E9%A1%B6%E9%A1%B6%E9%A1%B6%E9%A1%B6%E9%A1%B6%E9%A1%B6%E9%A1%B6%E9%A1%B6%E9%A1%B6%E9%A1%B6%E9%A1%B6%E9%A1%B6%E9%A1%B6%E9%A1%B6%E9%A1%B6%E9%A1%B6%E9%9D%A0%E9%9D%A0%E9%9D%A0%E9%9D%A0%E9%9D%A0%E9%9D%A0%E9%9D%A0%E9%9D%A0%E9%9D%A0%E9%9D%A0%E9%9D%A0%E9%9D%A0%E9%9D%A0%E9%9D%A0%E9%9D%A0%E9%9D%A0%E9%9D%A0%E9%9D%A0%E9%9D%A0%E5%B0%B1%E4%BC%9A%E9%99%8D%E4%BB%B7%E6%80%A5%E6%80%A5%E6%80%A5%E6%80%A5%E6%80%A5%E6%80%A5%E6%80%A5%E6%80%A5%E6%80%A5%E6%80%A5%E6%80%A5%E6%80%A5%E6%80%A5%E6%80%A5%E6%80%A5iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii%E5%95%A6%E5%95%A6%E5%95%A6%E5%95%A6%E5%95%A6%E5%95%A6%E5%95%A6%E5%95%A6%E5%95%A6%E5%95%A6%E5%95%A6%E5%95%A6%E5%95%A6%E5%95%A6%E5%95%A6%E5%95%A6%E5%95%A6%E5%95%A6%E5%95%A6%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A%E8%B8%A9%E8%B8%A9%E8%B8%A9%E8%B8%A9%E8%B8%A9%E8%B8%A9%E8%B8%A9%E8%B8%A9%E8%B8%A9%E8%B8%A9%E8%B8%A9%E8%B8%A9%E8%B8%A9%E8%B8%A9%E8%B8%A9%E8%B8%A9%E8%B8%A9%E8%B8%A9%E4%BB%8E%E5%92%A9%E5%92%A9%E5%92%A9%E5%92%A9%E5%92%A9%E5%92%A9%E5%92%A9%E5%92%A9%E5%92%A9%E5%92%A9%E5%92%A9%E5%92%A9%E5%92%A9%E5%92%A9%E5%92%A9%E5%92%A9%E5%92%A9%E5%92%A9%E5%92%A9%E5%95%A6%E5%95%A6%E5%95%A6%E5%95%A6%E5%95%A6%E5%95%A6%E5%95%A6%E5%95%A6%E5%95%A6%E5%95%A6%E5%95%A6%E5%95%A6%E5%95%A6%E5%95%A6%E5%95%A6%E5%95%A6%E5%95%A6%E5%95%A6%E5%95%A6iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%A6%E5%95%A6%E5%95%A6%E5%95%A6%E5%95%A6%E5%95%A6%E5%95%A6%E5%95%A6%E5%95%A6%E5%95%A6%E5%95%A6%E5%95%A6%E5%95%A6%E5%95%A6%E5%95%A6%E5%95%A6%E5%95%A6%E5%95%A6%E5%95%A6%E9%92%B1%E9%92%B1%E9%92%B1%E9%92%B1%E9%92%B1%E9%92%B1%E9%92%B1%E9%92%B1%E9%92%B1%E9%92%B1%E9%92%B1%E9%92%B1%E9%92%B1%E9%92%B1%E9%92%B1%E9%92%B1%E9%92%B1%E9%92%B1%E9%92%B1&
//time=06:19:09:15
			
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
