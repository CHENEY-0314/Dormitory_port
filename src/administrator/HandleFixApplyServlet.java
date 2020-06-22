package administrator;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

public class HandleFixApplyServlet extends HttpServlet {

private static final long serialVersionUID = 2L;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException{

		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
				
		try (PrintWriter out = response.getWriter()) {
			
			String fix_code = request.getParameter("fix_code").trim();
			String s_id = request.getParameter("s_id").trim();
			String time = request.getParameter("time").trim();			

			JSONObject jsonObject = new JSONObject();

			boolean result = AdmDAO.handleFixApply(fix_code, time, s_id);
 
			if (result) {
				jsonObject.put("result", "success");
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

}
