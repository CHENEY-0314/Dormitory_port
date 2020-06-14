package student;

public class student {
	
	//学号
    private String StudentNumber;
	//用户名称
    private String StudentName;
    //用户密码
    private String Password;
    
    public String getStudentNumber() {
        return StudentNumber;
    }

    public String getStudentName() {
        return StudentName;
    }

    public void setStudentName(String studentName) {
        this.StudentName = studentName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

	public void setStudentNumber(String string) {
		this.StudentNumber = string;
	}

}
