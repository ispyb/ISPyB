package ispyb.server.biosaxs.services.utils.reader.dat;

import java.util.ArrayList;
import java.util.List;

public class DatFilePlotter {
	public enum Operation {
		LOG,
		LINEAL
	}
	protected List<DatFile> files;
	protected Operation operation;
	
	public DatFilePlotter(List<DatFile> files) {
		this.files = files;
		this.operation = Operation.LOG;
	}
	
	public DatFilePlotter(List<DatFile> files, Operation op) {
		this.files = files;
		this.operation = op;
	}
	

	protected float getMin(List<DatPoint> points) {
		/** Validates input **/
		if (points == null) {
			throw new IllegalArgumentException("The Array must not be null");
		} else if (points.size() == 0) {
			throw new IllegalArgumentException("Array cannot be empty.");
		}

		/** Finds and returns min **/
		float min = points.get(0).getX();
		for (int i = 1; i < points.size(); i++) {
			if (Float.isNaN(points.get(i).getX())) {
				return Float.NaN;
			}
			if (points.get(i).getX() < min) {
				min = points.get(i).getX();
			}
		}

		return min;
	}

	protected ArrayList<DatPoint> getCurrentXPointList() {
		ArrayList<DatPoint> xList = new ArrayList<DatPoint>(this.files.size());
		for (DatFile file : this.files) {
			if (file.getStack().size() > 0) {
				xList.add(file.getStack().peek());
			}
		}
		return xList;
	}
	
	protected Float doOperation(Float x) {
		switch (this.operation) {
		case LINEAL:
			return x;
		case LOG:
			return (float) Math.log(x);
		}
		return x;
	}

	protected String parseCurrent(float min) {
		StringBuilder sb = new StringBuilder();
		sb.append(min);
		for (DatFile file: this.files) {
			if (!file.getStack().isEmpty()){
				if (file.getStack().peek().getX().equals(min)){
					DatPoint point = file.getStack().pop();
					sb.append("," + doOperation(point.getY()) + "," + (doOperation(Math.abs(point.getY() - point.getError())) - doOperation(point.getY())));
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

	public String getCSV() {
		StringBuilder sb = new StringBuilder();

		sb.append("I");
		for (DatFile file: this.files) {
			sb.append("," + file.file);
		}
		sb.append("\n");
		ArrayList<DatPoint> current = this.getCurrentXPointList();
		int security = 10000;
		while ((current.size() > 0)&&(security > 0)){
			float min = this.getMin(current);
			sb.append(this.parseCurrent(min)).append("\n");
			security = security - 1;
			current = this.getCurrentXPointList();
		}
		return sb.toString();
	}
}
