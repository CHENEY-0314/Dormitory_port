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

	//鏌ヨ璐︽埛鏄惁瀛樺湪锛屽瓨鍦ㄨ繑鍥炲叿浣搒tudent瀵硅薄锛屽惁鍒欎负绌恒�
		public static student queryUser(String s_id) {  //StudentID鏄鐢熷鍙�
			//杩炴帴鏁版嵁搴�
			Connection connection = DBManager.getConnection();
			PreparedStatement preparedStatement = null;
			ResultSet resultSet = null;
	 
			//SQL鏌ヨ璇彞
			StringBuilder sqlStatement = new StringBuilder();
			sqlStatement.append("select * from StudentLogin where s_id=?");        //闂彿锛熺殑鍦版柟浼氳瀛﹀彿鏇挎崲
	    	
			//璁剧疆鏁版嵁搴撶殑瀛楁鍊�
			try {
				preparedStatement = connection.prepareStatement(sqlStatement.toString());
				preparedStatement.setString(1, s_id);
	 
				resultSet = preparedStatement.executeQuery();                  //鎵ц鏌ユ壘璇彞锛岃幏寰楄繑鍥炰俊鎭�
				
				student user = new student();
				if (resultSet.next()) {                                        //鐢熸垚student瀵硅薄骞惰繑鍥�
					user.setStudentNumber(resultSet.getString("s_id"));        //璁剧疆璐︽埛銆佸瘑鐮侊紝鏍规嵁杩斿洖鐨勫唴瀹硅繕鍙互璁剧疆鍏朵粬淇℃伅
					user.setPassword(resultSet.getString("password"));
					return user;
					} else {
						return null;
						}
				} catch (SQLException ex) {
					Logger.getLogger(studentDAO.class.getName()).log(Level.SEVERE, null, ex);
					return null;
					} finally {
						DBManager.closeAll(connection, preparedStatement, resultSet);               //鍏抽棴杩炴帴
						}
			}

	//锟斤拷锟斤拷学锟脚诧拷询学锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟皆憋拷锟斤拷锟结（楼锟脚★拷锟斤拷锟斤拷拧锟斤拷锟轿伙拷牛锟斤拷锟斤拷锟斤拷锟絡son锟斤拷式锟斤拷
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
		//锟斤拷锟斤拷锟斤拷菘锟斤拷锟斤拷锟接讹拷锟斤拷
		Connection connection = DBManager.getConnection();
		PreparedStatement preparedStatement = null;
		//锟斤拷锟斤拷SQL锟斤拷锟斤拷
		StringBuilder sqlStatement = new StringBuilder();
		sqlStatement.append("insert into Intention (s_id) values (?)");
		//锟斤拷锟斤拷锟斤拷锟捷匡拷锟斤拷侄锟街�
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
			//SQL锟斤拷询锟斤拷锟�
			StringBuilder sql1 = new StringBuilder();
			sql1.append("select s_id from Intention where s_id=?");        //锟绞号ｏ拷锟侥地凤拷锟结被s_id锟芥换
				
			Connection connection = DBManager.getConnection();
			PreparedStatement preparedStatement = null;
		    ResultSet resultSet = null;

		    JSONObject jsonObject = new JSONObject();
		    
		    
		    try {
				preparedStatement = connection.prepareStatement(sql1.toString());
				preparedStatement.setString(1, s_id);
				resultSet = preparedStatement.executeQuery();                  //执锟叫诧拷锟斤拷锟斤拷洌拷锟矫凤拷锟斤拷锟斤拷息
				
		    	if (resultSet.next()) {                              
		    		jsonObject.put("result", "true");  //锟窖撅拷锟斤拷锟斤拷锟斤拷锟斤拷
		    		} else {
		    			jsonObject.put("result", "false");  //没锟斤拷锟斤拷锟斤拷
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
			sqlStatement.append("delete from Intention where s_id=?");

			try {
				preparedStatement = connection.prepareStatement(sqlStatement.toString());
				preparedStatement.setString(1, s_id);
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
		

	
}
