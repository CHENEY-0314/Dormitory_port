package student;

public class ApplyRecord {
	String change_code, s_id, building, room_num, bed_num, contact;
	public ApplyRecord(String cc, String s, String b, String rn, String bn, String c){
		change_code = cc;
		s_id = s;
		building = b; 
		room_num = rn;
		bed_num = bn;
		contact = c;
	}
	public final String getChangeCode(){
		return change_code;
	}
	public final String gets_id(){
		return s_id;
	}
	public final String getBuilding(){
		return building;
	}
	public final String getRoomNum(){
		return room_num;
	}
	public final String getBedNum(){
		return bed_num;
	}
	public final String getContact(){
		return contact;
	}
}
