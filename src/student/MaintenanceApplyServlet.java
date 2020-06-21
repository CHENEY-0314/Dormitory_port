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
		// 璁剧疆鍝嶅簲鍐呭绫诲瀷聽聽
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
				
		try (PrintWriter out = response.getWriter()) {
			
			//http://localhost:8080/Dormitory/servlet/MaintenanceApplyServlet?s_id=201830760178&maintenance=鐢靛櫒&remark=澶囨敞&contact=13000000&time=2018:06:12:12:30
			//鑾峰緱璇锋眰涓紶鏉ョ殑瀛﹀彿銆佹ゼ鏍嬨�瀹胯垗鍙枫�缁翠慨浜嬮」銆佹椂闂淬�鑱旂郴鏂瑰紡
			String ID = request.getParameter("s_id").trim();
			String maintenance = request.getParameter("maintenance").trim();
			String remark = request.getParameter("remark").trim();
			String contact = request.getParameter("contact").trim();
			String time = request.getParameter("time").trim();
			
			//鍚戞暟鎹簱涓彃鍏ユ暟鎹�
			Map<String, String> params = new HashMap<>();
			JSONObject jsonObject = new JSONObject();

			boolean result = MainAppDAO.insertMainApp(ID, maintenance, remark, contact, time);
 
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
