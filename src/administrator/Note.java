package administrator;

public class Note {

		String code, head, content, time;
		//构造函数
		public Note(String mcode, String mhead, String mcontent, String mtime){
			code = mcode;
			head = mhead;
			content = mcontent;
			time = mtime;
		}
		//get函数
		public final String getCode(){
			return code;
		}
		public final String gethead(){
			return head;
		}
		public final String getcontent(){
			return content;
		}
		public final String gettime(){
			return time;
		}

}
