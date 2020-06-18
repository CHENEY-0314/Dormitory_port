package student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import DBManagerr.DBManager;

public class MainAppDAO {
	public static boolean insertApp(String id, String b, String r, String m, String c){
		
		//连接数据库
		Connection connection = DBManager.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		//SQL查询语句
		StringBuilder sqlStatement = new StringBuilder();
		sqlStatement.append("insert into MaintenanceRecord values (?,?,?,?,?,?,?)");        //问号？的地方会被学号替换
		
		//设置数据库的字段值
		try {
			preparedStatement = connection.prepareStatement(sqlStatement.toString());
			preparedStatement.setString(1, id);
			preparedStatement.setString(2, b);
			preparedStatement.setString(3, r);
			preparedStatement.setString(4, m);
			preparedStatement.setString(5, "2018-01-01 12:00:00");
			preparedStatement.setString(1, c);
			preparedStatement.setString(1, "1");
 
			resultSet = preparedStatement.executeQuery();                  //执行查找语句，获得返回信息
			
			if(resultSet != null){
				return true;
			} else return false;
		}
		catch (SQLException ex) {
			Logger.getLogger(studentDAO.class.getName()).log(Level.SEVERE, null, ex);
				return false;
			} finally {
				DBManager.closeAll(connection, preparedStatement, resultSet);               //关闭连接
			}
	}
}
