package student;

public class ApplyRecord {
	String change_code, s_id, building, room_num, bed_num, contact, target_id, tbuilding, troom_num, tbed_num, tcontact;
	public ApplyRecord(String cc, String s, String b, String rn, String bn, String c, String tid, String tb, String tr, String tbn, String tc){
		change_code = cc;
		s_id = s;
		building = b; 
		room_num = rn;
		bed_num = bn;
		contact = c;
		target_id = tid;
		tbuilding = tb; 
		troom_num = tr;
		tbed_num = tbn;
		tcontact = tc;
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
	public final String getTs_id(){
		return target_id;
	}
	public final String getTBuilding(){
		return tbuilding;
	}
	public final String getTRoomNum(){
		return troom_num;
	}
	public final String getTBedNum(){
		return tbed_num;
	}
	public final String getTContact(){
		return tcontact;
	}
}
