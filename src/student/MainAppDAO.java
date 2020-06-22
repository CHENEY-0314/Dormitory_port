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
			random=(int) ((Math.random()*9+1)*1000);  //生成5位数的项目id
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
	
	//判断编号是否存在
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
}

class MaintenanceRecord{
	String fix_code, s_id, maintenance, remark, contact;
	//构造函数
	public MaintenanceRecord(String fc, String si, String mt, String rm, String ct){
		fix_code = fc;
		s_id = si;
		maintenance = mt;
		remark = rm;
		contact = ct;
	}
	//get函数
	public final String getFixCode(){
		return fix_code;
	}
	public final String gets_id(){
		return s_id;
	}
	public final String getMaintenance(){
		return maintenance;
	}
	public final String getRemark(){
		return remark;
	}
	public final String getContact(){
		return contact;
	}
}