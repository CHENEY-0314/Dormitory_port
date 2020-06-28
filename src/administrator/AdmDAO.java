package administrator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.json.JSONObject;
import student.ExchangeApplyDAO;
import student.MainAppDAO;
import DBManagerr.DBManager;

public class AdmDAO {
	
	public static int noteRelease(String head, String content, String time){
		
		//杩炴帴鏁版嵁搴�
		Connection connection = DBManager.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		//SQL鏌ヨ璇彞
		StringBuilder sqlStatement = new StringBuilder();
		sqlStatement.append("insert into Note values(?, ?, ?, ?, ?)");        //闂彿锛熺殑鍦版柟浼氳瀛﹀彿鏇挎崲
		
		//生成随机数编码
		String code = GetCode.getPublicNoteCode();
		
		//璁剧疆瀛楁鍊�
		try {
			preparedStatement = connection.prepareStatement(sqlStatement.toString());
			preparedStatement.setString(1, code);
			preparedStatement.setString(2, head);
			preparedStatement.setString(3, content);
			preparedStatement.setString(4, time);
			preparedStatement.setString(5, null);
 
			return preparedStatement.executeUpdate();                  //鎵ц鏌ユ壘璇彞锛岃幏寰楄繑鍥炰俊鎭�
			
		}
		catch (SQLException ex) {
			Logger.getLogger(AdmDAO.class.getName()).log(Level.SEVERE, null, ex);
				return 0;
			} finally {
				DBManager.closeAll(connection, preparedStatement, resultSet);               //鍏抽棴杩炴帴
			}
	}
	
	//用户反馈
	public static int Feedback(String content){
			
		Connection connection = DBManager.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		StringBuilder sqlStatement = new StringBuilder();
		sqlStatement.append("insert into Feedback values(?)");      
		
		try {
			preparedStatement = connection.prepareStatement(sqlStatement.toString());
			preparedStatement.setString(1, content);
 
			return preparedStatement.executeUpdate();              
			
		}
		catch (SQLException ex) {
			Logger.getLogger(AdmDAO.class.getName()).log(Level.SEVERE, null, ex);
				return 0;
			} finally {
				DBManager.closeAll(connection, preparedStatement, resultSet); 
			}
		}
	
    //获取所有维修申请
	public static JSONObject GetFixApply(){

		StringBuilder sql1 = new StringBuilder();
		sql1.append("SELECT * FROM LiveRecord NATURAL JOIN (SELECT * FROM MaintenanceRecord a NATURAL JOIN (SELECT fix_code,MAX(mainstate) as state,MAX(time) as time FROM MaintenanceRecordState GROUP BY fix_code HAVING MAX(mainstate)='1' or MAX(mainstate) ='2') b) d");
		
		Connection connection = DBManager.getConnection();
		PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
        Map<String, String> message = new HashMap<>();

	    JSONObject jsonObject = new JSONObject();
	    
	    try {
			preparedStatement = connection.prepareStatement(sql1.toString());
			resultSet = preparedStatement.executeQuery();
				for(int i = 1;resultSet.next();i++) {
					message.put("building", resultSet.getString("building"));
					message.put("room_num", resultSet.getString("room_num"));
					message.put("fix_code", resultSet.getString("fix_code"));
					message.put("s_id", resultSet.getString("s_id"));
					message.put("maintenance", resultSet.getString("maintenance"));
					message.put("remark", resultSet.getString("remark"));
					message.put("contact", resultSet.getString("contact"));
					message.put("state", resultSet.getString("state"));
					message.put("time", resultSet.getString("time"));
					jsonObject.put(i, message);
					}
	    	} catch (SQLException ex) {
	    		Logger.getLogger(MainAppDAO.class.getName()).log(Level.SEVERE, null, ex);
	        	}
	    DBManager.closeAll(connection, preparedStatement, resultSet);
	    return jsonObject;
	    }
	
	
	//管理员确认受理学生的维修申请（状态1到状态2）
	public static boolean handleFixApply(String fix_code, String time, String s_id){	
		//连接数据库
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
			preparedStatement.setString(2, "2");
			preparedStatement.setString(3, time);
			result1 = preparedStatement.executeUpdate();
			
			preparedStatement.close();
			
			preparedStatement = connection.prepareStatement(sqlStatement2.toString());
			preparedStatement.setString(1, code);
			preparedStatement.setString(2, "维修申请通知");
			preparedStatement.setString(3, "管理员已受理您的维修申请，请留意您的手机，管理员可能会联系您确认具体事项。更多信息请前往我的维修申请页面查看。");
			preparedStatement.setString(4, time);
			preparedStatement.setString(5, s_id);

			result2 = preparedStatement.executeUpdate();

			if(result1 != 0 && result2 != 0){
				return true;
			} else return false;
		}
		catch (SQLException ex) {
			Logger.getLogger(AdmDAO.class.getName()).log(Level.SEVERE, null, ex);
				return false;
			} finally {
				DBManager.closeAll(connection, preparedStatement,resultSet);   
			}
	}
	
	//管理员确认线下的维修完成（状态2到状态3）
	public static boolean confirmFixOver(String fix_code, String time, String s_id){	
		//连接数据库
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
			preparedStatement.setString(2, "3");
			preparedStatement.setString(3, time);
			result1 = preparedStatement.executeUpdate();
			
			preparedStatement.close();
			
			preparedStatement = connection.prepareStatement(sqlStatement2.toString());
			preparedStatement.setString(1, code);
			preparedStatement.setString(2, "维修申请通知");
			preparedStatement.setString(3, "您的宿舍已维修完成，请前往我的维修申请页面确认验收，超时（三天后）将自动验收。");
			preparedStatement.setString(4, time);
			preparedStatement.setString(5, s_id);

			result2 = preparedStatement.executeUpdate();

			if(result1 != 0 && result2 != 0){
				return true;
			} else return false;
		}
		catch (SQLException ex) {
			Logger.getLogger(AdmDAO.class.getName()).log(Level.SEVERE, null, ex);
				return false;
			} finally {
				DBManager.closeAll(connection, preparedStatement,resultSet);   
			}
	}
	
	//管理员拒绝受理宿舍维修（从状态1驳回）
	public static boolean refuseFixApply(String fix_code, String time, String s_id,String content){	
		//连接数据库
		Connection connection = DBManager.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int result1 = 0, result2 = 0;

		StringBuilder sqlStatement1 = new StringBuilder();
		StringBuilder sqlStatement2 = new StringBuilder();
		sqlStatement1.append("delete from MaintenanceRecord where fix_code=?");
		sqlStatement2.append("insert into Note values(?, ?, ? ,?, ?)");

		String code = GetCode.getFixNoteCode();
		
		try {
			preparedStatement = connection.prepareStatement(sqlStatement1.toString());
			preparedStatement.setString(1, fix_code);
			result1 = preparedStatement.executeUpdate();
			
			preparedStatement.close();
			
			preparedStatement = connection.prepareStatement(sqlStatement2.toString());
			preparedStatement.setString(1, code);
			preparedStatement.setString(2, "维修申请通知");
			preparedStatement.setString(3, content);
			preparedStatement.setString(4, time);
			preparedStatement.setString(5, s_id);

			result2 = preparedStatement.executeUpdate();

			if(result1 != 0 && result2 != 0){
				return true;
			} else return false;
		}
		catch (SQLException ex) {
			Logger.getLogger(AdmDAO.class.getName()).log(Level.SEVERE, null, ex);
				return false;
			} finally {
				DBManager.closeAll(connection, preparedStatement,resultSet);   
			}
	}
	
	//获取所有的换宿申请
	public static JSONObject GetChangeApply(){

		StringBuilder sql1 = new StringBuilder();
		sql1.append("SELECT * FROM ApplyRecord a NATURAL JOIN (SELECT change_code,MAX(appstate) as state,MAX(time) as time FROM ApplyRecordState GROUP BY change_code HAVING MAX(appstate)='2' or MAX(appstate) ='3' or MAX(appstate) ='4') b");
		
		Connection connection = DBManager.getConnection();
		PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
        Map<String, String> message = new HashMap<>();

	    JSONObject jsonObject = new JSONObject();
	    
	    try {
			preparedStatement = connection.prepareStatement(sql1.toString());
			resultSet = preparedStatement.executeQuery();
				for(int i = 1;resultSet.next();i++) {
					message.put("change_code", resultSet.getString("change_code"));
					message.put("s_id", resultSet.getString("s_id"));
					message.put("name", resultSet.getString("name"));
					message.put("sex", resultSet.getString("sex"));
					message.put("building", resultSet.getString("building"));
					message.put("room_num", resultSet.getString("room_num"));
					message.put("bed_num", resultSet.getString("bed_num"));
					message.put("contact", resultSet.getString("contact"));
					message.put("target_id", resultSet.getString("target_id"));
					message.put("tname", resultSet.getString("tname"));
					message.put("tsex", resultSet.getString("tsex"));
					message.put("t_building", resultSet.getString("tbuilding"));
					message.put("t_room_num", resultSet.getString("troom_num"));
					message.put("t_bed_num", resultSet.getString("tbed_num"));
					message.put("t_contact", resultSet.getString("tcontact"));
					message.put("state", resultSet.getString("state"));
					message.put("time", resultSet.getString("time"));
					jsonObject.put(i, message);
					}
	    	} catch (SQLException ex) {
	    		Logger.getLogger(MainAppDAO.class.getName()).log(Level.SEVERE, null, ex);
	        	}
	    DBManager.closeAll(connection, preparedStatement, resultSet);
	    return jsonObject;
	    }

	public static boolean admResponseExchangeApply(String change_code, String agree, String time, String t_id, String tname, String s_id, String name) {
		// 与数据库连接
		Connection connection = DBManager.getConnection();
		// 各种变量
		PreparedStatement preparedStatement1 = null;
		PreparedStatement preparedStatement2 = null;
		PreparedStatement preparedStatement3 = null;
		PreparedStatement preparedStatement4 = null;
		PreparedStatement preparedStatement5 = null;
		StringBuilder sqlStatement1 = new StringBuilder();
		StringBuilder sqlStatement2 = new StringBuilder();
		StringBuilder sqlStatement3 = new StringBuilder();
		StringBuilder sqlStatement4 = new StringBuilder();
		StringBuilder sqlStatement5 = new StringBuilder();
		int result1 = 0, result2 = 0;
		// 不管同意或不同意，都要更新通知列表和换宿记录状态表
		sqlStatement1.append("insert into Note values(?, ?, ?, ?, ?)");
		sqlStatement2.append("insert into ApplyRecordState values(?, ?, ?)");
		sqlStatement3.append("delete from ApplyRecordState where change_code=?");
		sqlStatement4.append("delete from ApplyRecord where change_code=?");
		sqlStatement5.append("update Intention set occupied=? where s_id=? or s_id=?");
        try {
        	preparedStatement1 = connection.prepareStatement(sqlStatement1.toString());
        	preparedStatement2 = connection.prepareStatement(sqlStatement2.toString());
        	preparedStatement3 = connection.prepareStatement(sqlStatement3.toString());
        	preparedStatement4 = connection.prepareStatement(sqlStatement4.toString());
        	preparedStatement5 = connection.prepareStatement(sqlStatement5.toString());
        	
			if(agree.equals("0")){// 不同意，删除记录，向请求者发送通知
				// 1.向换宿申请者发送通知
				preparedStatement1.setString(1, GetCode.getDormNoteCode());
				preparedStatement1.setString(2, "换宿申请通知");
				preparedStatement1.setString(3, "你与"+tname+"的换宿申请被管理员拒绝!");
				preparedStatement1.setString(4, time);
				preparedStatement1.setString(5, s_id);
				result1 = preparedStatement1.executeUpdate();
				preparedStatement1.close();
				// 2.向换宿接受者发送通知
				preparedStatement1 = connection.prepareStatement(sqlStatement1.toString());
				preparedStatement1.setString(1, GetCode.getDormNoteCode());
				preparedStatement1.setString(2, "换宿申请通知");
				preparedStatement1.setString(3, "你与:"+name+"的换宿申请被管理员拒绝!");
				preparedStatement1.setString(4, time);
				preparedStatement1.setString(5, t_id);
				result1 = preparedStatement1.executeUpdate();
				preparedStatement1.close();
				
				// 改变状态
				preparedStatement3.setString(1, change_code);
				preparedStatement4.setString(1, change_code);
				result2 = preparedStatement3.executeUpdate()+preparedStatement4.executeUpdate();
				preparedStatement3.close();
				
				// 设occupied为0
				preparedStatement5.setString(1, "0");
				preparedStatement5.setString(2, s_id);	
				preparedStatement5.setString(3, t_id);	
				result2 += preparedStatement5.executeUpdate();
				preparedStatement5.close();
			}else{// 同意，状态变为2，向请求者发送通知
				// 1.向换宿申请者发送通知
				preparedStatement1.setString(1, GetCode.getDormNoteCode());
				preparedStatement1.setString(2, "换宿申请通知");
				preparedStatement1.setString(3, "管理员同意你与"+tname+"的换宿申请，请尽快完成交换宿舍。");
				preparedStatement1.setString(4, time);
				preparedStatement1.setString(5, s_id);
				result1 = preparedStatement1.executeUpdate();
				preparedStatement1.close();
				// 1.向换宿接受者发送通知
				preparedStatement1 = connection.prepareStatement(sqlStatement1.toString());
				preparedStatement1.setString(1, GetCode.getDormNoteCode());
				preparedStatement1.setString(2, "换宿申请通知");
				preparedStatement1.setString(3, "管理员同意你与"+name+"的换宿申请，请尽快完成交换宿舍。");
				preparedStatement1.setString(4, time);
				preparedStatement1.setString(5, t_id);
				result1 = preparedStatement1.executeUpdate();
				preparedStatement1.close();
				
				// 改变状态
				preparedStatement2.setString(1, change_code);
				preparedStatement2.setString(2, time);
				preparedStatement2.setString(3, "3");
				result2 = preparedStatement2.executeUpdate();
				preparedStatement2.close();
			}
			return result1 != 0 && result2 != 0;
			
			} catch (SQLException ex) {
				Logger.getLogger(ExchangeApplyDAO.class.getName()).log(Level.SEVERE, null, ex);
				return false;
			} finally {
				DBManager.closeAll(connection, preparedStatement2);
				}
	}
	
	public static boolean exchangeConfirm(String change_code, String s_id, String name, String t_id, String tname, String time){
		//连接数据库
		Connection connection = DBManager.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int result1 = 0, result2 = 0;

		StringBuilder sqlStatement1 = new StringBuilder();
		StringBuilder sqlStatement2 = new StringBuilder();
		StringBuilder sqlStatement3 = new StringBuilder();
		StringBuilder sqlStatement4 = new StringBuilder();
		sqlStatement1.append("delete from ApplyRecordState where change_code=?");
		sqlStatement2.append("delete from ApplyRecord where change_code=?");
		sqlStatement3.append("insert into Note values(?, ?, ? ,?, ?)");
		sqlStatement4.append("update Intention set occupied=? where s_id=? or s_id=?");
		
		try {
			// 在数据库中删除记录
			preparedStatement = connection.prepareStatement(sqlStatement1.toString());
			preparedStatement.setString(1, change_code);
			result1 = preparedStatement.executeUpdate();
			preparedStatement.close();
			preparedStatement = connection.prepareStatement(sqlStatement2.toString());
			preparedStatement.setString(1, change_code);
			result1 += preparedStatement.executeUpdate();
			preparedStatement.close();
			// 发送通知
			preparedStatement = connection.prepareStatement(sqlStatement3.toString());
			preparedStatement.setString(1, GetCode.getDormNoteCode());
			preparedStatement.setString(2, "换宿申请通知");
			preparedStatement.setString(3, "您与"+name+"的交换宿舍由管理员最终确认完成！本次换宿结束！");
			preparedStatement.setString(4, time);
			preparedStatement.setString(5, t_id);
			result2 = preparedStatement.executeUpdate();
			preparedStatement.close();
			preparedStatement = connection.prepareStatement(sqlStatement3.toString());
			preparedStatement.setString(1, GetCode.getDormNoteCode());
			preparedStatement.setString(2, "换宿申请通知");
			preparedStatement.setString(3, "您与"+tname+"的交换宿舍由管理员最终确认完成！本次换宿结束！");
			preparedStatement.setString(4, time);
			preparedStatement.setString(5, s_id);
			result2 += preparedStatement.executeUpdate();
			preparedStatement.close();
			// 将occupied设为0
			preparedStatement = connection.prepareStatement(sqlStatement4.toString());
			preparedStatement.setString(1, "0");
			preparedStatement.setString(2, s_id);	
			preparedStatement.setString(3, t_id);	
			result2 += preparedStatement.executeUpdate();
			
			if(result1 != 0 && result2 != 0){
				return true;
			} else return false;
		}
		catch (SQLException ex) {
			Logger.getLogger(AdmDAO.class.getName()).log(Level.SEVERE, null, ex);
				return false;
			} finally {
				DBManager.closeAll(connection, preparedStatement,resultSet);   
			}
	}
}
