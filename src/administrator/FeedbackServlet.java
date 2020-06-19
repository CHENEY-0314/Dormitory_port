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

public class FeedbackServlet extends HttpServlet {

	// http://localhost:8080/Dormitory/servlet/FeedbackServlet?content=你的意见内容
	
	private static final long serialVersionUID = 6L;
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
 
		// 璁剧疆鍝嶅簲鍐呭绫诲瀷聽聽
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
 
		try (PrintWriter out = response.getWriter()) {
 
			//鑾峰緱璇锋眰涓紶鏉ュ弽棣堝唴瀹�
			String content = request.getParameter("content").trim();
 
			//鑾峰彇鍙嶉缁撴灉
			int result = AdmDAO.Feedback(content);
 
			Map<String, String> params = new HashMap<>();
			JSONObject jsonObject = new JSONObject();
 
			if (result > 0) {
				params.put("Result", "success: "+content.length());
				} else {
					params.put("Result", "failed");
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
	
}
