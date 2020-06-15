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

	//查询账户是否存在，存在返回具体student对象，否则为空。
		public static student queryUser(String StudentID) {  //StudentID是学生学号
			//连接数据库
			Connection connection = DBManager.getConnection();
			PreparedStatement preparedStatement = null;
			ResultSet resultSet = null;
	 
			//SQL查询语句
			StringBuilder sqlStatement = new StringBuilder();
			sqlStatement.append("select * from StudentLogin where s_id=?");        //问号？的地方会被学号替换
	    	
			//设置数据库的字段值
			try {
				preparedStatement = connection.prepareStatement(sqlStatement.toString());
				preparedStatement.setString(1, StudentID);
	 
				resultSet = preparedStatement.executeQuery();                  //执行查找语句，获得返回信息
				
				student user = new student();
				if (resultSet.next()) {                                        //生成student对象并返回
					user.setStudentNumber(resultSet.getString("s_id"));           //设置账户、密码，根据返回的内容还可以设置其他信息
					user.setPassword(resultSet.getString("password"));
					return user;
					} else {
						return null;
						}
				} catch (SQLException ex) {
					Logger.getLogger(studentDAO.class.getName()).log(Level.SEVERE, null, ex);
					return null;
					} finally {
						DBManager.closeAll(connection, preparedStatement, resultSet);               //关闭连接
						}
			}
		
	//根据学号查询学生的姓名、性别、宿舍（楼号、宿舍号、床位号），返回json格式。
		public static JSONObject GetStudentByStuId(String id) {
			
			//SQL查询语句
			StringBuilder sql1 = new StringBuilder();
			sql1.append("select * from Student t1 inner join LiveRecord t2 on t1.s_id=t2.s_id where t1.s_id=?");        //问号？的地方会被学号id替换
			
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
	
}
