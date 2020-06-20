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
	public static boolean insertMainApp(String id, String building, String room, String main, String time, String contact, String remark){
		
		//连接数据库
		Connection connection = DBManager.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int result = 0;

		StringBuilder sqlStatement = new StringBuilder();
		sqlStatement.append("insert into MaintenanceRecord values (?, ?, ?, ?, ?, ?, ?, ?)");        //问号？的地方会被学号替换
		

		try {
			preparedStatement = connection.prepareStatement(sqlStatement.toString());
			preparedStatement.setString(1, id);
			preparedStatement.setString(2, building);
			preparedStatement.setString(3, room);
			preparedStatement.setString(4, main);
			preparedStatement.setString(5, time);
			preparedStatement.setString(6, contact);
			preparedStatement.setString(7, remark);
			preparedStatement.setString(8, "1");
 
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
	
	//�жϵ�ǰ�û��Ƿ���Խ���ά�����루����ǰ��״̬Ϊ1-4�����룩
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
	    		jsonObject.put("result", "true");  //��ǰ�û������ڴ��������
	    		} else {
	    			jsonObject.put("result", "false");  //��ǰ�û������ڴ��������
	    			}
	    	} catch (SQLException ex) {
	    		Logger.getLogger(MainAppDAO.class.getName()).log(Level.SEVERE, null, ex);
	        	}
	    DBManager.closeAll(connection, preparedStatement, resultSet);
	    return jsonObject;
	    }
	
}
