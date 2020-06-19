package administrator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import student.studentDAO;
import DBManagerr.DBManager;

public class ReleaseNoteDAO {
//	String code, head, content, time;
	
	public static int noteRelease(String code, String head, String content, String time){
		
		//连接数据库
		Connection connection = DBManager.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		//SQL查询语句
		StringBuilder sqlStatement = new StringBuilder();
		sqlStatement.append("insert into Note values(?, ?, ?, ?)");        //问号？的地方会被学号替换
		
		//设置字段值
				try {
					preparedStatement = connection.prepareStatement(sqlStatement.toString());
					preparedStatement.setString(1, code);
					preparedStatement.setString(2, head);
					preparedStatement.setString(3, content);
					preparedStatement.setString(4, time);
		 
					return preparedStatement.executeUpdate();                  //执行查找语句，获得返回信息
					
				}
				catch (SQLException ex) {
					Logger.getLogger(ReleaseNoteDAO.class.getName()).log(Level.SEVERE, null, ex);
						return 0;
					} finally {
						DBManager.closeAll(connection, preparedStatement, resultSet);               //关闭连接
					}
	}
}
