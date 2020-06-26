package administrator;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

public class ExchangeConfirmServlet extends HttpServlet {

	private static final long serialVersionUID = 2L;
	
	// http://localhost:8080/Dormitory/servlet/ExchangeConfirmServlet?change_code=8000&s_id=201830660178&target_id=201830660174&time=2029:06:24:10:00
		@Override
		protected void doPost(HttpServletRequest request, HttpServletResponse response) 
				throws ServletException, IOException{

			response.setContentType("text/html;charset=utf-8");
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
					
			try (PrintWriter out = response.getWriter()) {
				
				String change_code = request.getParameter("change_code").trim();
				String s_id = request.getParameter("s_id").trim();
				String t_id = request.getParameter("target_id").trim();
				String time = request.getParameter("time").trim();			

				JSONObject jsonObject = new JSONObject();

				boolean result = AdmDAO.exchangeConfirm(change_code, s_id, t_id, time);
	 
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
