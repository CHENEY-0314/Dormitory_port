package student;

class MaintenanceRecord{
	String fix_code, s_id, maintenance, remark, contact;
	//构造函数
	public MaintenanceRecord(String fc, String si, String mt, String rm, String ct){
		fix_code = fc;
		s_id = si;
		maintenance = mt;
		remark = rm;
		contact = ct;
	}
	//get函数
	public final String getFixCode(){
		return fix_code;
	}
	public final String gets_id(){
		return s_id;
	}
	public final String getMaintenance(){
		return maintenance;
	}
	public final String getRemark(){
		return remark;
	}
	public final String getContact(){
		return contact;
	}
}
