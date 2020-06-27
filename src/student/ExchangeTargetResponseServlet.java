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

public class ExchangeTargetResponseServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// http://localhost:8080/Dormitory/servlet/ExchangeTargetResponseServlet?change_code=7011&target_id=201830660174&tname=张三&s_id=201830660178&time=2018:06:24:10:00&agree=0   (1代表同意，0代表不同意)
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		// 设置响应内容类型  
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
				
		try (PrintWriter out = response.getWriter()) {
			
			String code = request.getParameter("change_code").trim();
			String target_id = request.getParameter("target_id").trim();
			String tname = request.getParameter("tname").trim();
			String s_id = request.getParameter("s_id").trim();
			String agree = request.getParameter("agree").trim();
			String time = request.getParameter("time").trim();

			Map<String, String> params = new HashMap<>();
			JSONObject jsonObject = new JSONObject();
			
			boolean result = ExchangeApplyDAO.targetResponse(code, target_id, tname, s_id, time, agree);
 
			if (result) {
				params.put("result", "success");
				} else {
					params.put("result", "failed");
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
