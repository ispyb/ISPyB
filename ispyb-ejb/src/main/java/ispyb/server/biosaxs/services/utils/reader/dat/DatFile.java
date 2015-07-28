package ispyb.server.biosaxs.services.utils.reader.dat;

import java.util.List;
import java.util.Stack;

public class DatFile {

	protected String id;
	protected String file;
	protected String type;
	protected List<List<Float>> data;
	private Stack<DatPoint> stack; 
	
	public DatFile(String id, String file, String type, List<List<Float>> data) {
		super();
		this.id = id;
		this.file = file;
		this.type = type;
		this.data = data;
		
		this.stack = this.init();
	}
	
	private Stack<DatPoint> init(){
		Stack<DatPoint> points = new Stack<DatPoint>();
		for (int i =  this.data.size() - 1; i > -1 ; i--) {
			points.add(new DatPoint(this.data.get(i).get(0),this.data.get(i).get(1), this.data.get(i).get(2)));
		}
		return points;
	}
	
	public Stack<DatPoint> getStack(){
		return this.stack;
	}
}
