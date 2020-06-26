package student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.json.JSONObject;
import DBManagerr.DBManager;


public class studentDAO {

   //根据s_id查询用户信息 返回一个student对象
	public static student queryUser(String s_id) { 

		Connection connection = DBManager.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
 

		StringBuilder sqlStatement = new StringBuilder();
		sqlStatement.append("select * from StudentLogin where s_id=?");    
    	

		try {
			preparedStatement = connection.prepareStatement(sqlStatement.toString());
			preparedStatement.setString(1, s_id);
 
			resultSet = preparedStatement.executeQuery();           
			
			student user = new student();
			if (resultSet.next()) {                                       
				user.setStudentNumber(resultSet.getString("s_id"));    
				user.setPassword(resultSet.getString("password"));
				return user;
				} else {
					return null;
					}
			} catch (SQLException ex) {
				Logger.getLogger(studentDAO.class.getName()).log(Level.SEVERE, null, ex);
				return null;
				} finally {
					DBManager.closeAll(connection, preparedStatement, resultSet); 
					}
		}

	//根据学生id返回其个人信息
	public static JSONObject GetStudentByStuId(String id) {
		
		//SQL锟斤拷询锟斤拷锟�
		StringBuilder sql1 = new StringBuilder();
		sql1.append("select * from Student t1 inner join LiveRecord t2 on t1.s_id=t2.s_id where t1.s_id=?");        //锟绞号ｏ拷锟侥地凤拷锟结被学锟斤拷id锟芥换
		
		Connection connection = DBManager.getConnection();
		PreparedStatement preparedStatement = null;

        Map<String, String> message = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
			preparedStatement = connection.prepareStatement(sql1.toString());
			preparedStatement.setString(1, id);
        	} catch (SQLException ex) {
        		Logger.getLogger(studentDAO.class.getName()).log(Level.SEVERE, null, ex);
        		}
        ResultSet resultSet = null;
        try {
        	resultSet = preparedStatement.executeQuery();
        	if(resultSet.next()) {
        		message.put("s_id", resultSet.getString("s_id"));
        		message.put("s_name", resultSet.getString("s_name"));
        		message.put("sex", resultSet.getString("sex"));
        		message.put("college", resultSet.getString("college"));
        		message.put("contact", resultSet.getString("contact"));

        		message.put("building", resultSet.getString("building"));
        		message.put("room_num", resultSet.getString("room_num"));
        		message.put("bed_num", resultSet.getString("bed_num"));
        		jsonObject.put("结果", message);
        		}
        	} catch (SQLException ex) {
		Logger.getLogger(studentDAO.class.getName()).log(Level.SEVERE, null, ex);
		}
	        
        DBManager.closeAll(connection, preparedStatement, resultSet);
        return jsonObject;
	}
		
	//进入换宿意向
	public static void JoinIntention(String s_id){

	Connection connection = DBManager.getConnection();
	PreparedStatement preparedStatement = null;

	StringBuilder sqlStatement = new StringBuilder();
	sqlStatement.append("insert into Intention (s_id) values (?)");

	try {
		preparedStatement = connection.prepareStatement(sqlStatement.toString());
		preparedStatement.setString(1, s_id);
		preparedStatement.executeUpdate();
		} catch (SQLException ex) {
		} finally {
			DBManager.closeAll(connection, preparedStatement);
			}
	}
	
	//判断是否已经加入换宿意向
	public static JSONObject IfInIntention(String s_id){

		StringBuilder sql1 = new StringBuilder();
		sql1.append("select s_id from Intention where s_id=?");      
			
		Connection connection = DBManager.getConnection();
		PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;

	    JSONObject jsonObject = new JSONObject();
	    
	    
	    try {
			preparedStatement = connection.prepareStatement(sql1.toString());
			preparedStatement.setString(1, s_id);
			resultSet = preparedStatement.executeQuery();          
			
	    	if (resultSet.next()) {                              
	    		jsonObject.put("result", "true");  //已经加入意向
	    		} else {
	    			jsonObject.put("result", "false");  //没有加入意向
	    			}
	    	} catch (SQLException ex) {
	    		Logger.getLogger(studentDAO.class.getName()).log(Level.SEVERE, null, ex);
	        	}
	    DBManager.closeAll(connection, preparedStatement, resultSet);
	    return jsonObject;
	    }
			
	//退出换宿意向
	public static void ExitIntention(String s_id){
		Connection connection = DBManager.getConnection();
		PreparedStatement preparedStatement = null;
		StringBuilder sqlStatement = new StringBuilder();
		sqlStatement.append("insert into Intention (s_id,occupied) values (?,?)");

		try {
			preparedStatement = connection.prepareStatement(sqlStatement.toString());
			preparedStatement.setString(1, s_id);
			preparedStatement.setString(2, "0");

			preparedStatement.executeUpdate();
			} catch (SQLException ex) {
			} finally {
				DBManager.closeAll(connection, preparedStatement);
				}
		}
		
	//获取当前学生的通知
	@SuppressWarnings("finally")
	public static JSONObject GetNote(String s_id){
		Connection connection = DBManager.getConnection();
		PreparedStatement preparedStatement = null;

		//select * from Note where target_id=? or target_id is null
		StringBuilder sqlStatement = new StringBuilder();
		sqlStatement.append("select * from `Note` where `target_id` is null or `target_id` = '' or `target_id` = ?");
	    ResultSet resultSet = null;
        Map<String, String> message = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
		try {
			preparedStatement = connection.prepareStatement(sqlStatement.toString());
			preparedStatement.setString(1,s_id);
			resultSet = preparedStatement.executeQuery();
        	for(int i = 1;resultSet.next();i++) {
        		message.put("code", resultSet.getString("code"));
        		message.put("head", resultSet.getString("head"));
        		message.put("content", resultSet.getString("content"));
        		message.put("time", resultSet.getString("time"));
        		jsonObject.put(i, message);
        		}
			} catch (SQLException ex) {
			} finally {
				DBManager.closeAll(connection, preparedStatement);
		        return jsonObject;
				}
		}
	
	//删除当前学生指定code的通知
	public static void DeleteNote(String code){
		Connection connection = DBManager.getConnection();
		PreparedStatement preparedStatement = null;

		StringBuilder sqlStatement = new StringBuilder();
		sqlStatement.append("delete from Note where code = ?");
    	try {
    		preparedStatement = connection.prepareStatement(sqlStatement.toString());
    		preparedStatement.setString(1, code);
    		preparedStatement.executeUpdate();
    		} catch (SQLException ex) {
    			
    		} finally {
    			DBManager.closeAll(connection, preparedStatement);
    			}
		}
	
	//改变当前学生联系方式
	public static void ChangeNumber(String number,String s_id){
		Connection connection = DBManager.getConnection();
		PreparedStatement preparedStatement = null;

		StringBuilder sqlStatement = new StringBuilder();
		sqlStatement.append("update Student set contact=? where s_id=?");
    	try {
    		preparedStatement = connection.prepareStatement(sqlStatement.toString());
    		preparedStatement.setString(1, number);
    		preparedStatement.setString(2, s_id);
    		preparedStatement.executeUpdate();
    		} catch (SQLException ex) {
    			
    		} finally {
    			DBManager.closeAll(connection, preparedStatement);
    			}
		}
	
	
	//根据fix_code删除维修申请
	public static boolean ChangeFixApply(String fix_code,String maintenance,String remark,String contact,String time){
		Connection connection = DBManager.getConnection();
		PreparedStatement preparedStatement = null;

		StringBuilder sqlStatement = new StringBuilder();
		sqlStatement.append("update MaintenanceRecord set maintenance=?,remark=?,contact=? where fix_code=?");
		
		StringBuilder sqlStatement2 = new StringBuilder();
		sqlStatement2.append("update MaintenanceRecordState set time=? where fix_code=?");
		
		int result1 = 0,result2=0;		
    	try {
    		preparedStatement = connection.prepareStatement(sqlStatement.toString());
    		preparedStatement.setString(1, maintenance);
    		preparedStatement.setString(2, remark);
    		preparedStatement.setString(3, contact);
    		preparedStatement.setString(4, fix_code);
			result1 = preparedStatement.executeUpdate();
			
			preparedStatement.close();
			
			preparedStatement = connection.prepareStatement(sqlStatement2.toString());
			preparedStatement.setString(1, time);
    		preparedStatement.setString(2, fix_code);

			result2 = preparedStatement.executeUpdate();

			if(result1 != 0 && result2 != 0){
				return true;
			} else return false;
    		
    		} catch (SQLException ex) {
    			return false;
    		} finally {
    			DBManager.closeAll(connection, preparedStatement);
    			}
		}
		

	
}
