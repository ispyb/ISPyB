package ispyb.server.biosaxs.services.utils.reader.dat;

import java.util.ArrayList;
import java.util.List;

public class MergeDatFilePlotter extends DatFilePlotter{

	private List<Float> from;
	private List<Float> to;
	private List<Float> scale;

	public MergeDatFilePlotter(List<DatFile> files, List<Float> from, List<Float> to, List<Float> scale) {
		super(files);
		this.from = from;
		this.to = to;
		this.scale = scale;
	}

	private boolean checkFrom(int fileIndex, DatPoint point) {
		if (this.from.get(fileIndex) != null){
			if (point.getX() > this.from.get(fileIndex)){
				return true;
			}
			else{
				return false;
			}
		}
		return true;
	}
	
	private boolean checkTo(int fileIndex, DatPoint point) {
		if (this.to.get(fileIndex) != null){
			if (point.getX() < this.to.get(fileIndex)){
				return true;
			}
			else{
				return false;
			}
		}
		return true;
	}
	
	protected boolean isWithinRange(int fileIndex, DatPoint point) {
		return this.checkFrom(fileIndex, point) && this.checkTo(fileIndex, point);
		
	}
	@Override
	protected String parseCurrent(float min) {
		StringBuilder sb = new StringBuilder();
		sb.append(min);
		for (int i = 0; i < this.files.size(); i++) {
			DatFile file = this.files.get(i);
			if (!file.getStack().isEmpty()){
				if (file.getStack().peek().getX().equals(min)){
					DatPoint point = file.getStack().pop();
					if (this.isWithinRange(i, point)){
						if (this.scale.get(i) != null){
							point.setScale(this.scale.get(i));
						}
						sb.append("," + point.getY() + "," + point.getError());
					}
					else{
						sb.append(",,");
					}
				}
				else{
					sb.append(",,");
				}
			}
			else{
				sb.append(",,");
			}
		} 
		return sb.toString();
	}
	
	
	protected String parseMerge(float min) {
		StringBuilder sb = new StringBuilder();
		sb.append(min);
		for (int i = 0; i < this.files.size(); i++) {
			DatFile file = this.files.get(i);
			if (!file.getStack().isEmpty()){
				if (file.getStack().peek().getX().equals(min)){
					DatPoint point = file.getStack().pop();
					if (this.isWithinRange(i, point)){
						if (this.scale.get(i) != null){
							point.setScale(this.scale.get(i));
						}
						sb.append("\t" + point.getY() + "\t" + point.getError());
						return sb.toString();
					}
				}
			}
		}
		return new String();
	}
	

	public String merge() {
		StringBuilder sb = new StringBuilder();

//		sb.append("I");
//		for (DatFile file: this.files) {
//			sb.append("," + file.file);
//		}
//		sb.append("\n");
		ArrayList<DatPoint> current = this.getCurrentXPointList();
		int security = 10000;
		while ((current.size() > 0)&&(security > 0)){
			float min = this.getMin(current);
			String newLine = this.parseMerge(min);
			if (!newLine.isEmpty()){
				sb.append(newLine).append("\n");
			}
			security = security - 1;
			current = this.getCurrentXPointList();
		}
		return sb.toString();
	}
}
