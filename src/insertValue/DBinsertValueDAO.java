package insertValue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

import DBManagerr.DBManager;

public class DBinsertValueDAO {

	static String[] College = {"数学学院","艺术学院","物理学院","电子信息学院学院","物理与光电学院","机械与汽车工程学院","自动化科学与工程学院","食品科学与工程学院","建筑学院","软件学院","经济与贸易学院","计算机科学与工程学院"};
	
	public static void insertValue(){
		Connection connection = DBManager.getConnection();
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
		intention.append("insert into Intention values(?,?,'0')");
		
		PreparedStatement Dormitory = null;
		PreparedStatement Student = null;
		PreparedStatement LiveRecord = null;
		PreparedStatement StuLogin = null;
		PreparedStatement Intention = null;
		
		try {
//		   Dormitory = connection.prepareStatement(dormitory.toString());
//		   Student = connection.prepareStatement(student.toString());
//		   LiveRecord = connection.prepareStatement(liveRecord.toString());
//		   StuLogin = connection.prepareStatement(stuLogin.toString());
//		   Intention = connection.prepareStatement(intention.toString());
		   
		   Random r = new Random();
		   String building, room_num, bed_num, sex, s_id, contact;
		   // 插入数据
		   for(int b = 1; b < 13; b++){
			   building = "C"+b;// 生成楼栋信息
			   for(int rn = 101; rn < 751; rn++){
				   room_num = ""+rn;// 生成房间信息
				   sex = "男";
				   if(Integer.parseInt(room_num.substring(0, 1))%2==0) sex = "女";// 生成性别信息
				   for(int bn = 1; bn < 5; bn++){
					   Dormitory = connection.prepareStatement(dormitory.toString());
					   Student = connection.prepareStatement(student.toString());
					   LiveRecord = connection.prepareStatement(liveRecord.toString());
					   StuLogin = connection.prepareStatement(stuLogin.toString());
					   Intention = connection.prepareStatement(intention.toString());
					   
					   bed_num = ""+bn;// 生成床号信息
					   s_id = GetRandomString.getRandomID();// 生成学号信息
					   contact = GetRandomString.getRandomContact();// 生成联系方式
					   Dormitory.setString(1, building);
					   Dormitory.setString(2, room_num);
					   Dormitory.setString(3, bed_num);
//					   Dormitory.addBatch();// 插入宿舍信息
					   Student.setString(1, s_id);
					   Student.setString(2, GetRandomString.getRandomName(r.nextInt(2)+2));
					   Student.setString(3, sex);
					   Student.setString(4, College[b]);
					   Student.setString(5, contact);
//					   Student.addBatch();// 插入学生信息
					   StuLogin.setString(1, s_id);
					   StuLogin.setString(1, GetRandomString.getRandomPassword(r.nextInt(7))+6);
//					   StuLogin.addBatch();// 插入学生账号信息
					   LiveRecord.setString(1,s_id);
					   LiveRecord.setString(2,building);
					   LiveRecord.setString(3,room_num);
					   LiveRecord.setString(4,bed_num);
//					   LiveRecord.addBatch();// 插入居住信息
					   if(r.nextInt(32)>24){
						   Intention.setString(1, s_id);
						   Intention.setString(2, contact);
//						   Intention.addBatch();
						   Intention.executeUpdate();
						   Intention.close();
					   }
					   
					   Dormitory.executeUpdate();
					   Student.executeUpdate();
					   LiveRecord.executeUpdate();
					   StuLogin.executeUpdate();
					   
					   Dormitory.close();
					   Student.close();
					   LiveRecord.close();
					   StuLogin.close();
				   }
			   }
		   }
		   
		   // 批量执行语句
//		   Dormitory.executeBatch();
//		   Student.executeBatch();
//		   LiveRecord.executeBatch();
//		   StuLogin.executeBatch();
//		   Intention.executeBatch();
		   // 关闭连接
		   Dormitory.close();
		   Student.close();
		   LiveRecord.close();
		   StuLogin.close();
		   Intention.close();
		   } catch (SQLException ex) {
		   } finally {
		    DBManager.closeAll(connection, Dormitory);
		    }
		  }
}
