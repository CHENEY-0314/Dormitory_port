package administrator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import student.ApplyRecord;
import student.ExchangeApplyDAO;
import student.MainAppDAO;
import student.MaintenanceRecord;
import DBManagerr.DBManager;


// 用于生成随机编码，方便使用
public class GetCode {

	// 获取公共通知的编号
	public static String getPublicNoteCode(){
		//生成随机数编码
		int random;//生成4位数的随机数
		String code;
		   do{
			random=(int) ((Math.random()*9+1)*1000);  //生成4位数的项目id
			code="0"+random;
		   }while(isNoteExist(code));
		 return code;
	}
	
	// 获取维修通知的编号
	public static String getFixNoteCode(){
		//生成随机数编码
		int random;//生成4位数的随机数
		String code;
		   do{
			random=(int) ((Math.random()*9+1)*1000);  //生成4位数的项目id
			code="1"+random;
		   }while(isNoteExist(code));
		 return code;
	}
	
	// 获取交换宿舍通知的编号
	public static String getDormNoteCode(){
		//生成随机数编码
		int random;//生成4位数的随机数
		String code;
		   do{
			random=(int) ((Math.random()*9+1)*1000);  //生成4位数的项目id
			code="2"+random;
		   }while(isNoteExist(code));
		 return code;
	}
	
	// 获取维修事件编号
	public static String getFixCode(){
		//生成随机数编码
		int random;
		String fix_code;
		   do{
			random=(int) ((Math.random()*9+1)*1000);
			fix_code=""+random;
		   }while(isFixExist(fix_code));
	   return fix_code;
	}
	
	// 获取换宿事件编号
	public static String getDormCode(){
		//生成随机数编码
		int random;
		String change_code;
		   do{
			random=(int) ((Math.random()*9+1)*1000);
			change_code=""+random;
		   }while(isChangeExist(change_code));
	   return change_code;
	}
	
	// 判断数据库中是否存在该编号
	private static Boolean isChangeExist(String change_code) {
		 ApplyRecord record = queryChangeCode(change_code);
		 return null != record;
	 }
		
	// 查找申请更改宿舍编号
	public static ApplyRecord queryChangeCode(String change_code) {
	   // 与数据库连接
	   Connection connection = DBManager.getConnection();
	   PreparedStatement preparedStatement = null;
	   ResultSet resultSet = null;
	  
	   // 创建SQL语句
	   StringBuilder sqlStatement = new StringBuilder();
	   sqlStatement.append("select * from ApplyRecord where change_code=?");        
	      
	   // 执行SQL 语句 ֵ
	   try {
	    preparedStatement = connection.prepareStatement(sqlStatement.toString());
	    preparedStatement.setString(1, change_code);
	  
	    resultSet = preparedStatement.executeQuery();                  
	    
	    ApplyRecord record = null;
	    if (resultSet.next()) {                                        
	     record = new ApplyRecord(
	    		 resultSet.getString("change_code"),
	    		 resultSet.getString("s_id"),
	    		 resultSet.getString("builiding"),
	    		 resultSet.getString("room_num"),
	    		 resultSet.getString("bed_num"),
	    		 resultSet.getString("contact"),
			     resultSet.getString("target_id"),
				 resultSet.getString("tbuiliding"),
				 resultSet.getString("troom_num"),
				 resultSet.getString("tbed_num"),
				 resultSet.getString("tcontact")); 
	     return record;
	     } else {
	      return null;
	      }
	    } catch (SQLException ex) {
	     Logger.getLogger(ExchangeApplyDAO.class.getName()).log(Level.SEVERE, null, ex);
	     return null;
	     } finally {
	      DBManager.closeAll(connection, preparedStatement, resultSet);               //�ر�����
	      }
	   }
	
	//判断fix_code是否还在
	private static Boolean isFixExist(String fix_code) {
		 MaintenanceRecord record = queryFixCode(fix_code);
		 return null != record;
	 }
	//通过fix_code查找对应的维修信息
	public static MaintenanceRecord queryFixCode(String fix_code) {

	   Connection connection = DBManager.getConnection();
	   PreparedStatement preparedStatement = null;
	   ResultSet resultSet = null;
	  

	   StringBuilder sqlStatement = new StringBuilder();
	   sqlStatement.append("select * from MaintenanceRecord where fix_code=?");   

	   try {
	    preparedStatement = connection.prepareStatement(sqlStatement.toString());
	    preparedStatement.setString(1, fix_code);
	  
	    resultSet = preparedStatement.executeQuery();
	    
	    MaintenanceRecord record = null;
	    if (resultSet.next()) {
	     record = new MaintenanceRecord(
	    		 resultSet.getString("fix_code"),
	    		 resultSet.getString("s_id"),
	    		 resultSet.getString("maintenance"),
	    		 resultSet.getString("remark"),
	    		 resultSet.getString("contact"));
	     return record;
	     } else {
	      return null;
	      }
	    } catch (SQLException ex) {
	     Logger.getLogger(MainAppDAO.class.getName()).log(Level.SEVERE, null, ex);
	     return null;
	     } finally {
	      DBManager.closeAll(connection, preparedStatement, resultSet);
	      }
	   }
	
	//判断Note的编号是否存在
	public static Boolean isNoteExist(String code) {
		Note record = queryNoteCode(code);
		return null != record;
		}
	
	//查询当前note_code是否存在，存在返回>0，否则==0。
	public static Note queryNoteCode(String note_code) {
	   //连接数据库
	   Connection connection = DBManager.getConnection();
	   PreparedStatement preparedStatement = null;
	   ResultSet resultSet = null;
	  
	   //SQL查询语句
	   StringBuilder sqlStatement = new StringBuilder();
	   sqlStatement.append("select * from Note where code=?");        //问号？的地方会被id替换
	      
	   //设置数据库的字段值
	   try {
	    preparedStatement = connection.prepareStatement(sqlStatement.toString());
	    preparedStatement.setString(1, note_code);
	  
	    resultSet = preparedStatement.executeQuery();                  //执行查找语句，获得返回信息
	    
	    Note record = null;
	    if (resultSet.next()) {                                        //生成record对象并返回
	     record = new Note(
	    		 resultSet.getString("code"),
	    		 resultSet.getString("head"),
	    		 resultSet.getString("content"),
	    		 resultSet.getString("time")); //设置信息
	     return record;
	     } else {
	      return null;
	      }
	    } catch (SQLException ex) {
	     Logger.getLogger(AdmDAO.class.getName()).log(Level.SEVERE, null, ex);
	     return null;
	     } finally {
	      DBManager.closeAll(connection, preparedStatement, resultSet);               //关闭连接
	      }
	   }
	
	
}
