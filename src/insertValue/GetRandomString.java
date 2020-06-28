package insertValue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import student.ExchangeApplyDAO;
import student.student;
import DBManagerr.DBManager;

public class GetRandomString {

	// 随机生成中文名， 参数len代表生成几个字的名字
	public static String getRandomName(int len) {
//        String name = "";
//        for (int i = 0; i < len; i++) {
//            String str = null;
//            int hightPos, lowPos; // 定义高低位
//            Random random = new Random();
//            hightPos = (176 + Math.abs(random.nextInt(39))); // 获取高位值
//            lowPos = (161 + Math.abs(random.nextInt(93))); // 获取低位值
//            byte[] b = new byte[2];
//            b[0] = (new Integer(hightPos).byteValue());
//            b[1] = (new Integer(lowPos).byteValue());
//            try {
//                str = new String(b, "GBK"); // 转成中文
//            } catch (UnsupportedEncodingException ex) {
//                ex.printStackTrace();
//            }
//            name += str;
//        }
		String name = "";
        for (int i = 0; i < len; i++) {
            String str = null;
            int start = Integer.parseInt("4e00", 16);
            int end = Integer.parseInt("9fa5", 16);
         // 随机值
            int code = (new Random()).nextInt(end - start + 1) + start;
            // 转字符
            str = new String(new char[] { (char) code });
            name += str;
        }
        return name;
    }
	
	// 随机生成12位学号
	public static String getRandomID(){
		String id = "";
		Random r = new Random();
		int random;//生成随机数
		   do{
			random = (int) (Math.random()*100000000);  //生成8位数的项目id
			if(random % 4 == 3) id = "2016"+random;
			else if(random % 4 == 2) id = "2017"+random;
			else if(random % 4 == 1) id = "2018"+random;
			else if(random % 4 == 0) id = "2019"+random;
			if(id.length() < 12) id += r.nextInt(10);
		   }while(isIDExist(id));
		
		return id;
	}
	
	// 随机生成11位的联系方式
	public static String getRandomContact(){
		return (int)(Math.random()*100000)+""+(int)(Math.random()*1000000);
	}

	// 随机生成数字和字母组成的密码
    public static String getRandomPassword(int length) {  
        String password = "";  
        Random random = new Random();  
          
        //参数length，表示生成几位随机数  
        for(int i = 0; i < length; i++) {  
              
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";  
            //输出字母还是数字  
            if( "char".equalsIgnoreCase(charOrNum) ) {  
                //输出是大写字母还是小写字母  
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;  
                password += (char)(random.nextInt(26) + temp);  
            } else if( "num".equalsIgnoreCase(charOrNum) ) {  
            	password += String.valueOf(random.nextInt(10));  
            }  
        }  
        return password;  
    }
    
 // 查找学生ID是否存在
 	private static boolean isIDExist(String id) {
 		student stu = queryStudentID(id);
 		return null != stu;
 	}
    
 // 查找学生ID
 	public static student queryStudentID(String s_id) {
 	   // 与数据库连接
 	   Connection connection = DBManager.getConnection();
 	   PreparedStatement preparedStatement = null;
 	   ResultSet resultSet = null;
 	  
 	   // 创建SQL语句
 	   StringBuilder sqlStatement = new StringBuilder();
 	   sqlStatement.append("select * from Student where s_id=?");        
 	      
 	   // 执行SQL 语句 ֵ
 	   try {
 	    preparedStatement = connection.prepareStatement(sqlStatement.toString());
 	    preparedStatement.setString(1, s_id);
 	  
 	    resultSet = preparedStatement.executeQuery();                  
 	    
 	    student stu = null;
 	    if (resultSet.next()) {                                        
 	     stu = new student(
 	    		 resultSet.getString("s_id"),
 	    		 resultSet.getString("s_name")); 
 	     return stu;
 	     } else {
 	      return null;
 	      }
 	    } catch (SQLException ex) {
 	     Logger.getLogger(ExchangeApplyDAO.class.getName()).log(Level.SEVERE, null, ex);
 	     return null;
 	     } finally {
 	      DBManager.closeAll(connection, preparedStatement, resultSet);               //�ر�����
 	      }
 	   }
}
