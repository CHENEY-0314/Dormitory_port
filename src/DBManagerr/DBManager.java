package DBManagerr;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DBManager extends HttpServlet {
	private static final long serialVersionUID = -2692489887367146591L;
	ServletConfig config;//定义一个ServletConfig对象
	private static String username;//定义数据库用户名
	private static String password;//定义数据库连接密码
	private static String url;//定义数据库连接URL
	private static Connection connection;//定义连接
	@Override
	public void init(ServletConfig config) throws ServletException {
		this.config = config; //获取配置信息
		username = config.getInitParameter("DBUsername"); //获取数据库用户名
		password = config.getInitParameter("DBPassword"); //获取数据库连接密码
		url = config.getInitParameter("ConnectionURL");//获取数据库连接URL
		}
	//连接数据库
	public static Connection getConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(url, username, password);
			} catch (ClassNotFoundException | InstantiationException| IllegalAccessException | SQLException ex) {
				Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
				}
		return connection;
		}
//关闭连接，ResultSet是返回数据库的查询内容。
	public static void closeAll(Connection connection, Statement statement,
			ResultSet resultSet) {
		try {
			if (resultSet != null) {
				resultSet.close();
				}
			if (statement != null) {
				statement.close();
				}
			if (connection != null) {
				connection.close();
				}
			} catch (SQLException ex) {
				Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
				}
		}
//没有ResultSet的是关闭修改、增加数据操作（因为不用返回结果）
	public static void closeAll(Connection connection, Statement statement) {
		try {
			if (statement != null) {
				statement.close();
				}
			if (connection != null) {
				connection.close();
				}
			} catch (SQLException ex) {
				Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
				}
		}
}
