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

public class ChangeApplyDAO {
	
	// 筛选换宿申请
	@SuppressWarnings("finally")
	public static JSONObject ChangeScreen(String building, String[] floor, String[] bed_num){
		// 与数据库连接
		Connection connection = DBManager.getConnection();
		PreparedStatement preparedStatement = null;

		StringBuilder sqlStatement = new StringBuilder();
//			String subStmt = "select LiveRecord.s_id,building,room_num,bed_num from Intention,LiveRecord where Intention.s_id = LiveRecord.s_id";
		
		// 获得有换宿意向的宿舍信息
		sqlStatement.append("select building, room_num, bed_num from Intention as t1, LiveRecord as t where t1.s_id=t.s_id and ");
		sqlStatement.append(getStringValue(building, floor, bed_num));
	    ResultSet resultSet = null;
        Map<String, String> message = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
		try {
			preparedStatement = connection.prepareStatement(sqlStatement.toString());
			System.out.print(sqlStatement);
//				preparedStatement.setString(1, getStringValue(building, floor, bed_num));
			
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
//	        		message.put("target_id", resultSet.getString("s_id"));
    			message.put("building", resultSet.getString("building"));
        		message.put("room_num", resultSet.getString("room_num"));
        		message.put("bed_num", resultSet.getString("bed_num"));
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
			value.append("building=?");
			if(!floor[0].equals("0") || !bed_num[0].equals("0")) value.append(" and ");
		}
		if(!floor[0].equals("0")){
			value.append("(room_num like ?");
			if(floor.length > 1)
			for(int i = 1; i < floor.length; i++){
				value.append(" or room_num like ?");
			}
			value.append(")");
			if(!bed_num[0].equals("0")) value.append(" and ");
		}
		if(!bed_num[0].equals("0")){
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
	public static boolean SubmitExchangeApp(String ID, String t_building, String t_room_num, String t_bed_num, String contact, String time){
		// 与数据库连接
		Connection connection = DBManager.getConnection();
		// 各种变量
		PreparedStatement preparedStatement = null;
		StringBuilder sqlStatement1 = new StringBuilder();
		StringBuilder sqlStatement2 = new StringBuilder();
		int result1 = 0, result2 = 0;
		sqlStatement1.append("insert into ApplyRecord values(?,?,?,?,?,?)");
		sqlStatement2.append("insert into ApplyRecordState values(?, ?, ?)");
        
        // 生成随机编码
        int random;//���4λ��������
		String change_code;
		   do{
			random=(int) ((Math.random()*9+1)*1000);  //���4λ�����Ŀid
			change_code=""+random;
		   }while(isExist(change_code));
        
        try {
			preparedStatement = connection.prepareStatement(sqlStatement1.toString());
			// 设置字段值
			preparedStatement.setString(1, change_code);
			preparedStatement.setString(2, ID);	
			preparedStatement.setString(3, t_building);	
			preparedStatement.setString(4, t_room_num);	
			preparedStatement.setString(5, t_bed_num);	
			preparedStatement.setString(6, contact);	
			result1 = preparedStatement.executeUpdate();
			
			preparedStatement.close();
			
			preparedStatement = connection.prepareStatement(sqlStatement2.toString());
			preparedStatement.setString(1, change_code);
			preparedStatement.setString(2, time);	
			preparedStatement.setString(3, "1");	
			result2 = preparedStatement.executeUpdate();
			
			return result1 != 0 && result2 != 0;
			} catch (SQLException ex) {
				Logger.getLogger(ChangeApplyDAO.class.getName()).log(Level.SEVERE, null, ex);
				return false;
			} finally {
				DBManager.closeAll(connection, preparedStatement);
				}
}
	
	// 判断数据库中是否存在该编号
	private static Boolean isExist(String change_code) {
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
	    		 resultSet.getString("contact")); 
	     return record;
	     } else {
	      return null;
	      }
	    } catch (SQLException ex) {
	     Logger.getLogger(ChangeApplyDAO.class.getName()).log(Level.SEVERE, null, ex);
	     return null;
	     } finally {
	      DBManager.closeAll(connection, preparedStatement, resultSet);               //�ر�����
	      }
	   }
	
}
