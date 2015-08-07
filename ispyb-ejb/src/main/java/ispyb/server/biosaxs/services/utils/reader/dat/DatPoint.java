package ispyb.server.biosaxs.services.utils.reader.dat;

public class DatPoint {
	private float x;
	private float y;
	private float error;

	public DatPoint(float x, float y, float error){
		this.x = x;
		this.y = y;
		this.error = error;
	}
	
//	public String getCSV(){
//		StringBuilder sb = new StringBuilder();
//		sb.append(this.x);
//		sb.append(Math.log(this.y)*this.y);
//		sb.append((this.error)+100);
//		return sb.toString();
//	}
	
	public Float getX(){
		return this.x;
	}

	public Float getY() {
		return this.y;
	}
	
	public Float getError() {
		return this.error;
	}
	
//	public String toString(){
//		return this.getCSV();
//	}

	public void setScale(Float scale) {
		this.y = this.y * scale;
	}
}
