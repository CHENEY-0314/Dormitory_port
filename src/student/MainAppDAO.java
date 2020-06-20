package student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.json.JSONObject;
import DBManagerr.DBManager;

public class MainAppDAO {
	
	//添加维修申请（学号、楼号、宿舍号、维修属性、申请时间、联系方式、备注、维修状态）
	public static boolean insertApp(String id, String b, String r, String m, String t, String c){

		Connection connection = DBManager.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int result = 0;

		StringBuilder sqlStatement = new StringBuilder();
		sqlStatement.append("insert into MaintenanceRecord values (?,?,?,?,?,?,?)");    
		

		try {
			preparedStatement = connection.prepareStatement(sqlStatement.toString());
			preparedStatement.setString(1, id);
			preparedStatement.setString(2, b);
			preparedStatement.setString(3, r);
			preparedStatement.setString(4, m);
			preparedStatement.setString(5, t);
			preparedStatement.setString(6, c);
			preparedStatement.setString(7, "1");
 
			result = preparedStatement.executeUpdate();              
			
			if(result != 0){
				return true;
			} else return false;
		}
		catch (SQLException ex) {
			Logger.getLogger(MainAppDAO.class.getName()).log(Level.SEVERE, null, ex);
				return false;
			} finally {
				DBManager.closeAll(connection, preparedStatement, resultSet);   
			}
	}
	
	//判断当前用户是否可以进行维修申请（及当前无状态为1-4的申请）
	public static JSONObject IfCanApply(String s_id){

		StringBuilder sql1 = new StringBuilder();
		sql1.append("select * from MaintenanceRecord where s_id=? and mainstate<>?");
			
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
	    		jsonObject.put("result", "true");  //当前用户有正在处理的申请
	    		} else {
	    			jsonObject.put("result", "false");  //当前用户无正在处理的申请
	    			}
	    	} catch (SQLException ex) {
	    		Logger.getLogger(MainAppDAO.class.getName()).log(Level.SEVERE, null, ex);
	        	}
	    DBManager.closeAll(connection, preparedStatement, resultSet);
	    return jsonObject;
	    }
	
}
