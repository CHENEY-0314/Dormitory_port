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
import net.sf.json.JSONObject;
import DBManagerr.DBManager;

public class MainAppDAO {
	
	public static boolean insertMainApp(String id, String main, String remark, String contact, String time){	
		//连接数据库
		Connection connection = DBManager.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int result1 = 0, result2 = 0;

		StringBuilder sqlStatement1 = new StringBuilder();
		StringBuilder sqlStatement2 = new StringBuilder();
		sqlStatement1.append("insert into MaintenanceRecord values (?, ?, ?, ?, ?)");        //问号？的地方会被学号替换
		sqlStatement2.append("insert into MaintenanceRecordState values(?, ?, ?)");
		
		//生成随机数编码
		int random;//生成4位数的随机数
		String fix_code;
		   do{
			random=(int) ((Math.random()*9+1)*1000);  //生成4位数的项目id
			fix_code=""+random;
		   }while(isExist(fix_code));
		
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
	
	//判断当前用户是否可以进行维修申请（及当前无状态为1-4的申请）
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
	
	
	    //获取用户当前的维修申请
		public static JSONObject GetFixApply(String s_id){

			StringBuilder sql1 = new StringBuilder();
			sql1.append("select t2.mainstate,t2.time from MaintenanceRecord t1 inner join MaintenanceRecordState t2 on t1.fix_code=t2.fix_code where t1.s_id=? and t2.mainstate<>?");
			
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
		
	
	//判断fix_code编号是否存在
	private static Boolean isExist(String fix_code) {
			 MaintenanceRecord record = queryFixCode(fix_code);
			 return null != record;
		 }
		
	//查询当前fix_code是否存在，存在返回>0，否则==0。
	public static MaintenanceRecord queryFixCode(String fix_code) {
		   //连接数据库
		   Connection connection = DBManager.getConnection();
		   PreparedStatement preparedStatement = null;
		   ResultSet resultSet = null;
		  
		   //SQL查询语句
		   StringBuilder sqlStatement = new StringBuilder();
		   sqlStatement.append("select * from MaintenanceRecord where fix_code=?");        //问号？的地方会被id替换
		      
		   //设置数据库的字段值
		   try {
		    preparedStatement = connection.prepareStatement(sqlStatement.toString());
		    preparedStatement.setString(1, fix_code);
		  
		    resultSet = preparedStatement.executeQuery();                  //执行查找语句，获得返回信息
		    
		    MaintenanceRecord record = null;
		    if (resultSet.next()) {                                        //生成record对象并返回
		     record = new MaintenanceRecord(
		    		 resultSet.getString("fix_code"),
		    		 resultSet.getString("s_id"),
		    		 resultSet.getString("maintenance"),
		    		 resultSet.getString("remark"),
		    		 resultSet.getString("contact")); //设置信息
		     return record;
		     } else {
		      return null;
		      }
		    } catch (SQLException ex) {
		     Logger.getLogger(MainAppDAO.class.getName()).log(Level.SEVERE, null, ex);
		     return null;
		     } finally {
		      DBManager.closeAll(connection, preparedStatement, resultSet);               //关闭连接
		      }
		   }
	
	
	//学生确认验收（状态3到状态4）
	public static boolean checkUp(String fix_code, String time, String s_id){	
		//连接数据库
		Connection connection = DBManager.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int result1 = 0, result2 = 0;

		StringBuilder sqlStatement1 = new StringBuilder();
		StringBuilder sqlStatement2 = new StringBuilder();
		sqlStatement1.append("insert into MaintenanceRecordState values (?, ?, ?)");
		sqlStatement2.append("insert into Note values(?, ?, ? ,?, ?)");
		
		//生成随机数编码
		int random;//生成4位数的随机数
		String code;
		   do{
			random=(int) ((Math.random()*9+1)*1000);  //生成4位数的项目id
			code="1"+random;
		   }while(AdmDAO.isExist(code));
		
		try {
			preparedStatement = connection.prepareStatement(sqlStatement1.toString());
			preparedStatement.setString(1, fix_code);
			preparedStatement.setString(2, "4");
			preparedStatement.setString(3, time);
			result1 = preparedStatement.executeUpdate();
			
			preparedStatement.close();
			
			preparedStatement = connection.prepareStatement(sqlStatement2.toString());
			preparedStatement.setString(1, code);
			preparedStatement.setString(2, "维修申请通知");
			preparedStatement.setString(3, "您的维修申请已验收完成，如有需要，请在24小时以后再次发起申请！");
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
	
