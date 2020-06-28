package insertValue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import DBManagerr.DBManager;

public class DBinsertValueDAO {

	static String[] College = {"数学学院","艺术学院","物理学院","电子信息学院学院","物理与光电学院","机械与汽车工程学院","自动化科学与工程学院","食品科学与工程学院","建筑学院","软件学院","经济与贸易学院","计算机科学与工程学院"};
	
	public static void insertValue() throws SQLException{
		Connection connection = DBManager.getConnection();
		ResultSet resultSet = null;
//		connection.setAutoCommit(false);
		// 创建语句
		StringBuilder dormitory = new StringBuilder();
		StringBuilder student = new StringBuilder();
		StringBuilder liveRecord = new StringBuilder();
		StringBuilder stuLogin = new StringBuilder();
		StringBuilder intention = new StringBuilder();
		
		dormitory.append("insert into Dormitory values(?,?,?)");
		student.append("insert into Student values(?,?,?,?,?)");
		liveRecord.append("insert into LiveRecord values(?,?,?,?)");
		stuLogin.append("insert into StudentLogin values(?,?)");
		intention.append("insert into Intention values(?,'0')");
		
		PreparedStatement Dormitory = null;
		PreparedStatement Student = null;
		PreparedStatement LiveRecord = null;
		PreparedStatement StuLogin = null;
		PreparedStatement Intention = null;
//		PreparedStatement sql = null;
		
		try {
//		   Dormitory = connection.prepareStatement(dormitory.toString());
//		   Student = connection.prepareStatement(student.toString());
//		   LiveRecord = connection.prepareStatement(liveRecord.toString());
//		   StuLogin = connection.prepareStatement(stuLogin.toString());
//		   Intention = connection.prepareStatement(intention.toString());
		   Random r = new Random();
		   // 插入数据
		   for(int b = 1; b < 13; b++){
			   String building = "C"+b;// 生成楼栋信息
			   for(int f = 1; f < 5; f++){
				   for(int n = 0; n < 6; n++){
					   for(int r1 = 1; r1 < 10; r1++){
						   String room_num = f+""+n+""+r1;// 生成房间信息
						   String sex = "男";
						   if(f%2 == 0) sex = "女";// 生成性别信息
						   for(int bn = 1; bn < 5; bn++){
//							   Dormitory = connection.prepareStatement(dormitory.toString());
//							   Student = connection.prepareStatement(student.toString());
//							   LiveRecord = connection.prepareStatement(liveRecord.toString());
//							   StuLogin = connection.prepareStatement(stuLogin.toString());
//							   Intention = connection.prepareStatement(intention.toString());
							   String bed_num = ""+bn;// 生成床号信息
							   String s_id = GetRandomString.getRandomID();// 生成学号信息
							   String contact = GetRandomString.getRandomContact();// 生成联系方式
							   
							   Dormitory = connection.prepareStatement(dormitory.toString());
							   Dormitory.setString(1, building);
							   Dormitory.setString(2, room_num);
							   Dormitory.setString(3, bed_num);
							   Dormitory.executeUpdate();
							   Dormitory.close();
//							   Dormitory.addBatch();// 插入宿舍信息
							   Student = connection.prepareStatement(student.toString());
							   Student.setString(1, s_id);
							   Student.setString(2, GetRandomString.getRandomName(r.nextInt(2)+2));
							   Student.setString(3, sex);
							   Student.setString(4, College[b-1]);
							   Student.setString(5, contact);
							   Student.executeUpdate();
							   Student.close();
//							   Student.addBatch();// 插入学生信息
							   StuLogin = connection.prepareStatement(stuLogin.toString());
							   StuLogin.setString(1, s_id);
							   StuLogin.setString(2, GetRandomString.getRandomPassword(r.nextInt(11)+9));
							   StuLogin.executeUpdate();
							   StuLogin.close();
//							   StuLogin.addBatch();// 插入学生账号信息
							   LiveRecord = connection.prepareStatement(liveRecord.toString());
							   LiveRecord.setString(1,s_id);
							   LiveRecord.setString(2,building);
							   LiveRecord.setString(3,room_num);
							   LiveRecord.setString(4,bed_num);
							   LiveRecord.executeUpdate();
							   LiveRecord.close();
//							   LiveRecord.addBatch();// 插入居住信息
							   if(r.nextInt(33)>24){
								   Intention = connection.prepareStatement(intention.toString());
								   Intention.setString(1, s_id);
//								   Intention.addBatch();// 插入换宿意向
								   Intention.executeUpdate();
								   Intention.close();
							   }
							   
//							   Dormitory.executeUpdate();
//							   Student.executeUpdate();
//							   LiveRecord.executeUpdate();
//							   StuLogin.executeUpdate();
//							   
//							   Dormitory.close();
//							   Student.close();
//							   LiveRecord.close();
//							   StuLogin.close();
//							   Intention.close();
						   }
					   }
					   
				   }
			   }
		   }
//		   connection.commit();
		   // 批量执行语句
//		   Dormitory.executeBatch();
//		   Student.executeBatch();
//		   LiveRecord.executeBatch();
//		   StuLogin.executeBatch();
//		   Intention.executeBatch();
//		   // 关闭连接
		   Dormitory.close();
		   Student.close();
		   LiveRecord.close();
		   StuLogin.close();
		   Intention.close();
		   } catch (SQLException ex) {
		   } finally {
			   Student.close();
			   LiveRecord.close();
			   StuLogin.close();
			   Intention.close();
		    DBManager.closeAll(connection, Dormitory, resultSet);
		    }
		  }
}
