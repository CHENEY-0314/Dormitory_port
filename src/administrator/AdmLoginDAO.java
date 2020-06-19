package administrator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import DBManagerr.DBManager;

public class AdmLoginDAO {
	//查询账户是否存在，存在返回具体student对象，否则为空。
	public static administrator queryAdm(String a_id) {  //StudentID是学生学号
		//连接数据库
		Connection connection = DBManager.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		 
		//SQL查询语句
		StringBuilder sqlStatement = new StringBuilder();
		sqlStatement.append("select * from Administrator where a_id=?");        //问号？的地方会被学号替换
		    	
		//设置数据库的字段值
		try {
			preparedStatement = connection.prepareStatement(sqlStatement.toString());
			preparedStatement.setString(1, a_id);
		 
			resultSet = preparedStatement.executeQuery();                  //执行查找语句，获得返回信息
					
			if (resultSet.next()) {                                        //生成student对象并返回 设置账户、密码，根据返回的内容还可以设置其他信息
				return new administrator(resultSet.getString("a_id"),resultSet.getString("password"));
				} else {
					return null;
					}
			} catch (SQLException ex) {
				Logger.getLogger(AdmLoginDAO.class.getName()).log(Level.SEVERE, null, ex);
				return null;
				} finally {
					DBManager.closeAll(connection, preparedStatement, resultSet);               //关闭连接
				}
		}
}
