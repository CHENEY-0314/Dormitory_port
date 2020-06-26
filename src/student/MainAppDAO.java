package student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import administrator.AdmDAO;
import administrator.GetCode;
import net.sf.json.JSONObject;
import DBManagerr.DBManager;

public class MainAppDAO {
	
	public static boolean insertMainApp(String id, String main, String remark, String contact, String time){	

		Connection connection = DBManager.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int result1 = 0, result2 = 0;

		StringBuilder sqlStatement1 = new StringBuilder();
		StringBuilder sqlStatement2 = new StringBuilder();
		sqlStatement1.append("insert into MaintenanceRecord values (?, ?, ?, ?, ?)");
		sqlStatement2.append("insert into MaintenanceRecordState values(?, ?, ?)");

		//随机生成4位数的维修code
		String fix_code = GetCode.getFixCode();
		
		try {
			preparedStatement = connection.prepareStatement(sqlStatement1.toString());
			preparedStatement.setString(1, fix_code);
			preparedStatement.setString(2, id);
			preparedStatement.setString(3, main);
			preparedStatement.setString(4, remark);
			preparedStatement.setString(5, contact);
			result1 = preparedStatement.executeUpdate();
			
			preparedStatement.close();
			
			preparedStatement = connection.prepareStatement(sqlStatement2.toString());
			preparedStatement.setString(1, fix_code);
			preparedStatement.setString(2, "1");
			preparedStatement.setString(3, time);
			result2 = preparedStatement.executeUpdate();

			if(result1 != 0 && result2 != 0){
				return true;
			} else return false;
		}
		catch (SQLException ex) {
			Logger.getLogger(MainAppDAO.class.getName()).log(Level.SEVERE, null, ex);
				return false;
			} finally {
				DBManager.closeAll(connection, preparedStatement,resultSet);   
			}
	}
	
	//判断是否可以进行维修申请（当前用户无状态为1到4的维修申请）
	public static JSONObject IfCanApply(String s_id){

		StringBuilder sql1 = new StringBuilder();
		sql1.append("select t1.fix_code from MaintenanceRecord t1 inner join MaintenanceRecordState t2 on t1.fix_code=t2.fix_code where t1.s_id=? and t2.mainstate<>?");
		
		Connection connection = DBManager.getConnection();
		PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;

	    JSONObject jsonObject = new JSONObject();
	    
	    try {
			preparedStatement = connection.prepareStatement(sql1.toString());
			preparedStatement.setString(1, s_id);
			preparedStatement.setString(2, "5");

			resultSet = preparedStatement.executeQuery();
			
	    	if (resultSet.next()) {                              
	    		jsonObject.put("result", "true");  //不能进行维修申请
	    		} else {
	    			jsonObject.put("result", "false");  //可以进行维修申请
	    			}
	    	} catch (SQLException ex) {
	    		Logger.getLogger(MainAppDAO.class.getName()).log(Level.SEVERE, null, ex);
	        	}
	    DBManager.closeAll(connection, preparedStatement, resultSet);
	    return jsonObject;
	    }
	
	
	    //获取指定id学生的当前维修申请信息
		public static JSONObject GetFixApply(String s_id){

			StringBuilder sql1 = new StringBuilder();
			sql1.append("select t2.mainstate,t2.time,t2.fix_code from MaintenanceRecord t1 inner join MaintenanceRecordState t2 on t1.fix_code=t2.fix_code where t1.s_id=? and t2.mainstate<>?");
			
			Connection connection = DBManager.getConnection();
			PreparedStatement preparedStatement = null;
		    ResultSet resultSet = null;
	        Map<String, String> message = new HashMap<>();

		    JSONObject jsonObject = new JSONObject();
		    
		    try {
				preparedStatement = connection.prepareStatement(sql1.toString());
				preparedStatement.setString(1, s_id);
				preparedStatement.setString(2, "5");
				resultSet = preparedStatement.executeQuery();
				
	        	for(int i = 1;resultSet.next();i++) {
	        		message.put("fix_code", resultSet.getString("fix_code"));
	        		message.put("mainstate", resultSet.getString("mainstate"));
	        		message.put("time", resultSet.getString("time"));
	        		jsonObject.put(i, message);
	        		}
	        	
		    	} catch (SQLException ex) {
		    		Logger.getLogger(MainAppDAO.class.getName()).log(Level.SEVERE, null, ex);
		        	}
		    DBManager.closeAll(connection, preparedStatement, resultSet);
		    return jsonObject;
		    }
	
	//学生确认验收（从状态3到状态4的跳转）
	public static boolean checkUp(String fix_code, String time, String s_id){	

		Connection connection = DBManager.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int result1 = 0, result2 = 0;

		StringBuilder sqlStatement1 = new StringBuilder();
		StringBuilder sqlStatement2 = new StringBuilder();
		sqlStatement1.append("insert into MaintenanceRecordState values (?, ?, ?)");
		sqlStatement2.append("insert into Note values(?, ?, ? ,?, ?)");

		String code = GetCode.getFixNoteCode();
		
		try {
			preparedStatement = connection.prepareStatement(sqlStatement1.toString());
			preparedStatement.setString(1, fix_code);
			preparedStatement.setString(2, "4");
			preparedStatement.setString(3, time);
			result1 = preparedStatement.executeUpdate();
			
			preparedStatement.close();
			
			preparedStatement = connection.prepareStatement(sqlStatement2.toString());
			preparedStatement.setString(1, code);
			preparedStatement.setString(2, "维修宿舍通知");
			preparedStatement.setString(3, "您已经确认验收，欢迎下次使用。（下次申请需要间隔一天）");
			preparedStatement.setString(4, time);
			preparedStatement.setString(5, s_id);

			result2 = preparedStatement.executeUpdate();

			if(result1 != 0 && result2 != 0){
				return true;
			} else return false;
		}
		catch (SQLException ex) {
			Logger.getLogger(MainAppDAO.class.getName()).log(Level.SEVERE, null, ex);
				return false;
			} finally {
				DBManager.closeAll(connection, preparedStatement,resultSet);   
			}
	}
	
	
	


}
	
