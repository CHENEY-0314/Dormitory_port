package student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import DBManagerr.DBManager;

public class MainAppDAO {
	public static boolean insertApp(String id, String b, String r, String m, String t, String c){
		
		//连接数据库
		Connection connection = DBManager.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int result = 0;
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
			preparedStatement.setString(5, t);
			preparedStatement.setString(6, c);
			preparedStatement.setString(7, "1");
 
			result = preparedStatement.executeUpdate();                  //执行查找语句，获得返回信息
			
			if(result != 0){
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
