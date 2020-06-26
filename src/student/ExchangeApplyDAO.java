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

public class ExchangeApplyDAO {
	
	// 筛选换宿申请
	@SuppressWarnings("finally")
	public static JSONObject ChangeScreen(String building, String[] floor, String[] bed_num){
		// 与数据库连接
		Connection connection = DBManager.getConnection();
		PreparedStatement preparedStatement = null;

		StringBuilder sqlStatement = new StringBuilder();
//			String subStmt = "select LiveRecord.s_id,building,room_num,bed_num from Intention,LiveRecord where Intention.s_id = LiveRecord.s_id";
		
		// 获得有换宿意向的宿舍信息
		sqlStatement.append("select t1.s_id, building, room_num, bed_num, contact from Intention as t1, LiveRecord as t where t1.s_id=t.s_id and occupied='0'");
		sqlStatement.append(getStringValue(building, floor, bed_num));
	    ResultSet resultSet = null;
        Map<String, String> message = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
		try {
			preparedStatement = connection.prepareStatement(sqlStatement.toString());
			System.out.print(sqlStatement);
//				preparedStatement.setString(1, getStringValue(building, floor, bed_num));
			
//			preparedStatement.setString(1, "0");
			// 根据条件设置值
			int count=1;
			if(!building.equals("0")) {preparedStatement.setString(count, building); count++;}
			if(!floor[0].equals("0")){
				for(int i = 0; i < floor.length; i++){
					 preparedStatement.setString(count, floor[i]+"%");
					 count++;
				}
			}
			if(!bed_num[0].equals("0")){
				for(int i = 0; i < bed_num.length; i++){
					preparedStatement.setString(count, bed_num[i]);
					 count++;
				}
			}
			resultSet = preparedStatement.executeQuery();
			// 根据条件筛选结果
        	for(int i = 1;resultSet.next();i++) {
        		message.put("target_id", resultSet.getString("s_id"));
    			message.put("building", resultSet.getString("building"));
        		message.put("room_num", resultSet.getString("room_num"));
        		message.put("bed_num", resultSet.getString("bed_num"));
        		message.put("contact", resultSet.getString("contact"));
        		jsonObject.put(i, message);
        	}
			} catch (SQLException ex) {
			} finally {
				DBManager.closeAll(connection, preparedStatement);
		        return jsonObject;
				}
		}
	
	// 换宿筛选辅助函数
	public static String getStringValue(String building, String[] floor, String[] bed_num){
		StringBuilder value = new StringBuilder();
		
		if(!building.equals("0")) {
			value.append(" and building=?");
			if(!floor[0].equals("0") || !bed_num[0].equals("0")) value.append(" and ");
		}
		if(!floor[0].equals("0")){
			if(building.equals("0")) value.append(" and ");
			value.append("(room_num like ?");
			if(floor.length > 1)
			for(int i = 1; i < floor.length; i++){
				value.append(" or room_num like ?");
			}
			value.append(")");
			if(!bed_num[0].equals("0")) value.append(" and ");
		}
		if(!bed_num[0].equals("0")){
			if(building.equals("0") && floor[0].equals("0")) value.append(" and ");
			value.append("(bed_num=?");
			if(bed_num.length > 1)
				for(int i = 1; i < bed_num.length; i++){
					value.append(" or bed_num=?");
				}
			value.append(")");
		}
		return value.toString();
	}
	
	// 提交换宿申请
	public static String SubmitExchangeApp(String ID, String target_id, String building, String room_num, String bed_num, String t_building, String t_room_num, String t_bed_num, String contact, String t_contact, String time){
		// 与数据库连接
		Connection connection = DBManager.getConnection();
		// 各种变量
		PreparedStatement preparedStatement = null;
		StringBuilder sqlStatement1 = new StringBuilder();
		StringBuilder sqlStatement2 = new StringBuilder();
		StringBuilder sqlStatement3 = new StringBuilder();
		StringBuilder sqlStatement4 = new StringBuilder();
		int result1 = 0, result2 = 0, result3 = 0, result4 = 0;
		sqlStatement1.append("insert into ApplyRecord values(?,?,?,?,?,?,?,?,?,?,?)");
		sqlStatement2.append("insert into ApplyRecordState values(?, ?, ?)");
		sqlStatement3.append("insert into Note values(?, ?, ?, ?, ?)");
		sqlStatement4.append("update Intention set occupied=? where s_id=? or s_id=?");
        
        // 生成随机编码
        String change_code = GetCode.getDormCode();
        
        try {
			preparedStatement = connection.prepareStatement(sqlStatement1.toString());
			// 向ApplyRecord表插入申请更换宿舍的信息
			preparedStatement.setString(1, change_code);
			preparedStatement.setString(2, ID);	
			preparedStatement.setString(3, building);	
			preparedStatement.setString(4, room_num);	
			preparedStatement.setString(5, bed_num);	
			preparedStatement.setString(6, contact);	
			preparedStatement.setString(7, target_id);
			preparedStatement.setString(8, t_building);	
			preparedStatement.setString(9, t_room_num);	
			preparedStatement.setString(10, t_bed_num);	
			preparedStatement.setString(11, t_contact);	
			result1 = preparedStatement.executeUpdate();
			
			preparedStatement.close();
			// 向state表插入状态信息
			preparedStatement = connection.prepareStatement(sqlStatement2.toString());
			preparedStatement.setString(1, change_code);
			preparedStatement.setString(2, time);	
			preparedStatement.setString(3, "1");	
			result2 = preparedStatement.executeUpdate();
			
			preparedStatement.close();
			// 向Note表插入被申请更换宿舍的学生的通知信息
			preparedStatement = connection.prepareStatement(sqlStatement3.toString());
			preparedStatement.setString(1, GetCode.getDormNoteCode());
			preparedStatement.setString(2, "更换宿舍邀请");	
			preparedStatement.setString(3, ID+":请求与你更换宿舍,联系方式:"+contact);	
			preparedStatement.setString(4, time);	
			preparedStatement.setString(5, target_id);
			result3 = preparedStatement.executeUpdate();
			
			preparedStatement.close();
			// 改变Intention中的occupied信息，防止有多人同时申请同一间宿舍
			preparedStatement = connection.prepareStatement(sqlStatement4.toString());
			preparedStatement.setString(1, "1");
			preparedStatement.setString(2, ID);	
			preparedStatement.setString(3, target_id);	
			result4 = preparedStatement.executeUpdate();
			
			return change_code;
			} catch (SQLException ex) {
				Logger.getLogger(ExchangeApplyDAO.class.getName()).log(Level.SEVERE, null, ex);
				return null;
			} finally {
				DBManager.closeAll(connection, preparedStatement);
				}
}
	
	
	// 换宿目标同意或不同意
	public static boolean targetResponse(String change_code, String target_id, String s_id, String time, String agree){
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
				// 发送通知
				preparedStatement1.setString(1, GetCode.getDormNoteCode());
				preparedStatement1.setString(2, "换宿申请通知");
				preparedStatement1.setString(3, target_id+":拒绝与你进行换宿");
				preparedStatement1.setString(4, time);
				preparedStatement1.setString(5, s_id);
				result1 = preparedStatement1.executeUpdate();
				
				// 改变状态
				preparedStatement3.setString(1, change_code);
				preparedStatement4.setString(1, change_code);
				result2 = preparedStatement3.executeUpdate()+preparedStatement4.executeUpdate();
				
				// 设occupied为0
				preparedStatement5.setString(1, "1");
				preparedStatement5.setString(2, s_id);	
				preparedStatement5.setString(3, target_id);	
				result2 += preparedStatement5.executeUpdate();
			}else{// 同意，状态变为2，向请求者发送通知
				// 发送通知
				preparedStatement1.setString(1, GetCode.getDormNoteCode());
				preparedStatement1.setString(2, "换宿申请通知");
				preparedStatement1.setString(3, target_id+":同意与你进行换宿，等待管理员确认");
				preparedStatement1.setString(4, time);
				preparedStatement1.setString(5, s_id);
				result1 = preparedStatement1.executeUpdate();
				preparedStatement1.close();
				
				// 改变状态
				preparedStatement2.setString(1, change_code);
				preparedStatement2.setString(2, time);
				preparedStatement2.setString(3, "2");
				result2 = preparedStatement2.executeUpdate();
				preparedStatement2.close();
			}
			preparedStatement1.close();
			preparedStatement2.close();
			preparedStatement3.close();
			preparedStatement4.close();
			preparedStatement5.close();
			return result1 != 0 && result2 != 0;
			
			} catch (SQLException ex) {
				Logger.getLogger(ExchangeApplyDAO.class.getName()).log(Level.SEVERE, null, ex);
				return false;
			} finally {
				DBManager.closeAll(connection, preparedStatement2);
				}
	}
	
	public static JSONObject getApply(String id){
		StringBuilder sql1 = new StringBuilder();
		sql1.append("SELECT * FROM ApplyRecord a NATURAL JOIN (SELECT change_code,MAX(appstate) as state,MAX(time) as time FROM ApplyRecordState GROUP BY change_code HAVING MAX(appstate)='1' or MAX(appstate)='2' or MAX(appstate) ='3' or MAX(appstate) ='4') b where target_id=?");
		
		Connection connection = DBManager.getConnection();
		PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
        Map<String, String> message = new HashMap<>();

	    JSONObject jsonObject = new JSONObject();
	    
	    try {
			preparedStatement = connection.prepareStatement(sql1.toString());
			preparedStatement.setString(1, id);
			resultSet = preparedStatement.executeQuery();
				for(int i = 1;resultSet.next();i++) {
					message.put("change_code", resultSet.getString("change_code"));
					message.put("s_id", resultSet.getString("s_id"));
					message.put("building", resultSet.getString("building"));
					message.put("room_num", resultSet.getString("room_num"));
					message.put("bed_num", resultSet.getString("bed_num"));
					message.put("contact", resultSet.getString("contact"));
					message.put("target_id", resultSet.getString("target_id"));
					message.put("tbuilding", resultSet.getString("tbuilding"));
					message.put("troom_num", resultSet.getString("troom_num"));
					message.put("tbed_num", resultSet.getString("tbed_num"));
					message.put("tcontact", resultSet.getString("tcontact"));
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
	
	public static boolean exchangeFinish(String change_code, String s_id, String t_id, String time){
		//连接数据库
		Connection connection = DBManager.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int result1 = 0, result2 = 0;

		StringBuilder sqlStatement1 = new StringBuilder();
		StringBuilder sqlStatement2 = new StringBuilder();
		sqlStatement1.append("insert into ApplyRecordState values (?, ?, ?)");
		sqlStatement2.append("insert into Note values(?, ?, ? ,?, ?)");
		
		String code = GetCode.getFixNoteCode();
		
		try {
			preparedStatement = connection.prepareStatement(sqlStatement1.toString());
			preparedStatement.setString(1, change_code);
			preparedStatement.setString(2, time);
			preparedStatement.setString(3, "4");
			result1 = preparedStatement.executeUpdate();
			
			preparedStatement.close();
			
			preparedStatement = connection.prepareStatement(sqlStatement2.toString());
			preparedStatement.setString(1, code);
			preparedStatement.setString(2, "换宿申请通知");
			preparedStatement.setString(3, "您与"+s_id+"的交换宿舍已由"+s_id+"确认完成！等待管理员进行最终确认！");
			preparedStatement.setString(4, time);
			preparedStatement.setString(5, t_id);

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
	
	@SuppressWarnings("finally")
	public static boolean isExchanging(String s_id){
		// 与数据库连接
		Connection connection = DBManager.getConnection();
		PreparedStatement preparedStatement = null;

		StringBuilder sqlStatement = new StringBuilder();
//					String subStmt = "select LiveRecord.s_id,building,room_num,bed_num from Intention,LiveRecord where Intention.s_id = LiveRecord.s_id";
		
		// 获得有换宿意向的宿舍信息
		sqlStatement.append("select occupied from Intention where s_id=?");
	    ResultSet resultSet = null;
		try {
			preparedStatement = connection.prepareStatement(sqlStatement.toString());
			preparedStatement.setString(1,s_id);
			resultSet = preparedStatement.executeQuery();
    		return resultSet.getString("occupied").equals("1");
			} catch (SQLException ex) {
			} finally {
				DBManager.closeAll(connection, preparedStatement);
		        return false;
				}
	}
}
