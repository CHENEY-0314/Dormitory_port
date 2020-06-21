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
		int random;//生成4位数的随机数
		String code;
		   do{
			random=(int) ((Math.random()*9+1)*1000);  //生成4位数的项目id
			code="0"+random;
		   }while(isExist(code));
		
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
	
	//判断编号是否存在
	private static Boolean isExist(String code) {
		Note record = queryNoteCode(code);
		return null != record;
		}
		
	//查询当前fix_code是否存在，存在返回>0，否则==0。
	public static Note queryNoteCode(String note_code) {
		   //连接数据库
		   Connection connection = DBManager.getConnection();
		   PreparedStatement preparedStatement = null;
		   ResultSet resultSet = null;
		  
		   //SQL查询语句
		   StringBuilder sqlStatement = new StringBuilder();
		   sqlStatement.append("select * from MaintenanceRecord where code=?");        //问号？的地方会被id替换
		      
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
		sql1.append("SELECT * FROM MaintenanceRecord a NATURAL JOIN (SELECT fix_code,MAX(mainstate) as state,MAX(time) as time FROM MaintenanceRecordState GROUP BY fix_code HAVING MAX(mainstate)='1' or MAX(mainstate) ='2') b");
		
		Connection connection = DBManager.getConnection();
		PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
        Map<String, String> message = new HashMap<>();

	    JSONObject jsonObject = new JSONObject();
	    
	    try {
			preparedStatement = connection.prepareStatement(sql1.toString());
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()){
				resultSet.previous();
				for(int i = 1;resultSet.next();i++) {
					message.put("fix_code", resultSet.getString("fix_code"));
					message.put("s_id", resultSet.getString("s_id"));
					message.put("maintenance", resultSet.getString("maintenance"));
					message.put("remark", resultSet.getString("remark"));
					message.put("contact", resultSet.getString("contact"));
					message.put("state", resultSet.getString("state"));
					message.put("time", resultSet.getString("time"));
					jsonObject.put(i, message);
					}
				}else{
					message.put("result", "none");
					jsonObject.put(1, message);
				}
        	
	    	} catch (SQLException ex) {
	    		Logger.getLogger(MainAppDAO.class.getName()).log(Level.SEVERE, null, ex);
	        	}
	    DBManager.closeAll(connection, preparedStatement, resultSet);
	    return jsonObject;
	    }
	
	
}
