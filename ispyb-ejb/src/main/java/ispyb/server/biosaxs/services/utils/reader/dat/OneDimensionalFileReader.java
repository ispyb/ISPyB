package ispyb.server.biosaxs.services.utils.reader.dat;

import ispyb.common.util.PathUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class OneDimensionalFileReader{
	private String filePath;
	private Integer id;

	public OneDimensionalFileReader(Integer id, String filePath) {
		this.filePath = filePath;
		this.id = id;
	}

	public HashMap<String, Object> readFile() throws IOException {
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("id", id);
		
		List<List<Float>> data = new ArrayList<List<Float>>();
		
		/** If path doesn't exist we check that we are not in development mode **/
		if (filePath != null){
			if (!new File(filePath.trim()).exists()){
				filePath = PathUtils.getPath(filePath.trim());
			}
		}
		else{
			/** FilePath is null**/
			result.put("fileName", null);
			result.put("size", null);
			result.put("dataPoints", null);
			result.put("data", null);
			return result;
		}
		
		/** Updated file path **/
		File file = new File(filePath.trim());
		if (file.exists()){
			BufferedReader br = new BufferedReader(new FileReader(filePath.trim()));
			String line;
			while ((line = br.readLine()) != null) {
				/** Skipping blank lines **/
				if (line.toString().trim().length() != 0){
					if (!line.startsWith("#")){
								List<String> splitted = Arrays.asList(line.trim().split(" +"));
								boolean isValidColumn = true;
								List<Float> row = new ArrayList<Float>();
								for (String column : splitted) {
									if (!column.matches("[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?")){
										isValidColumn = false;
									}
									else{
										row.add(Float.parseFloat(column));
									}
								}
								if (isValidColumn){
									/** Truncating **/
									List<Float> truncate = new ArrayList<Float>();
									for (Float float1 : row) {
//										DecimalFormat df = new DecimalFormat("###.###");
//										truncate.add(Float.parseFloat(df.format(float1)));
										truncate.add(float1);
									}
									data.add(truncate);
								}
					}
				}
			}
			br.close();
			
		}
			
		/** Adding meta data **/
		result.put("fileName", file.getName());
		result.put("size", file.length());
		result.put("dataPoints", data.size());
		result.put("data", data);
		return result;
	}

}
