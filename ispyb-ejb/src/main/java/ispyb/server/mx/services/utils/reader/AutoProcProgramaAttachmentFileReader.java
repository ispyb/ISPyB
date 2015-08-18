package ispyb.server.mx.services.utils.reader;

import ispyb.common.util.PathUtils;
import ispyb.server.mx.vos.autoproc.AutoProcProgramAttachment3VO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AutoProcProgramaAttachmentFileReader {
	
	
	@SuppressWarnings("unchecked")
	public static List<AutoProcessingData> getAutoProcessingDataFromAttachemt(AutoProcProgramAttachment3VO attachment) throws Exception {
		return (List<AutoProcessingData>) AutoProcProgramaAttachmentFileReader.readAttachment(attachment).get("autoProcessingData");
	}
	
	
	public static HashMap<String, Object> readAttachment(AutoProcProgramAttachment3VO attachment) throws Exception {
		boolean xscaleFile = false;
		boolean truncateLog = false;
		List<AutoProcessingData> listAutoProcessingData = new ArrayList<AutoProcessingData>();
		
//		System.out.println(attachment);
		if (attachment != null) {
			String fileName = attachment.getFileName();
			xscaleFile = (fileName != null && fileName.toLowerCase().endsWith("xscale.lp"));
			truncateLog = (fileName != null && fileName.toLowerCase().endsWith(".log") && fileName.toLowerCase().contains("truncate"));

//			System.out.println(xscaleFile);
//			System.out.println(truncateLog);
			if (xscaleFile || truncateLog) {
				// parse the file
				String sourceFileName = PathUtils.FitPathToOS(attachment.getFilePath() + "/" + fileName);
				BufferedReader inFile = null;
				String output = new String();// = null;

				try {
					// 1. Reading input by lines:
					boolean startToRead = false;
					boolean startToRead2 = false;
					System.out.println(sourceFileName);
					inFile = new BufferedReader(new FileReader(sourceFileName));
					String s = new String();
					while ((s = inFile.readLine()) != null) {
						String line = s;
//						System.out.println(line);
						output += line + "\n";
						if (xscaleFile) {
							if (line.contains("SUBSET OF INTENSITY DATA WITH SIGNAL/NOISE")) {
								startToRead = true;
							} else if (startToRead) {
								if (!line.contains("RESOLUTION") && !line.isEmpty() && !line.contains("LIMIT")) {
									String[] values = line.split(" ");
									String[] val = new String[14];
									int i = 0;
									for (int k = 0; k < values.length; k++) {
										if (i <= 13 && !values[k].isEmpty()) {
											val[i] = values[k];
											if (values[k].endsWith("%") || values[k].endsWith("*"))
												val[i] = values[k].substring(0, values[k].length() - 1);
											i++;
										}
									}
									try {
										AutoProcessingData d = new AutoProcessingData(attachment.getAutoProcProgramAttachmentId(),
												Double.parseDouble(val[0]), Double.parseDouble(val[4]), Double.parseDouble(val[5]),
												Double.parseDouble(val[8]), Double.parseDouble(val[10]), Double.parseDouble(val[12]),
												Integer.parseInt(val[11]), fileName, attachment.getAutoProcProgramVO().getAutoProcProgramId());
										// int index=0;
										// for (int j=0; j<listAutoProcessingData.size(); j++){
										// if (d.getResolutionLimit() >=
										// listAutoProcessingData.get(j).getResolutionLimit() ){
										// index = j;
										// break;
										// }
										// }
										//
										// listAutoProcessingData.add(index, d);
										listAutoProcessingData.add(d);
									} catch (Exception e) {

									}
								}
							}
							if (line.contains("STATISTICS OF INPUT DATA SET")) {
								startToRead = false;
							}
						} else if (truncateLog) {
							if (line.contains("$TABLE: Wilson Plot")) {
								startToRead = true;
							} else if (startToRead) {
								try {
									String[] values = line.split(" ");
									String[] val = new String[10];
									int i = 0;
									for (int k = 0; k < values.length; k++) {
										if (i <= 9 && !values[k].isEmpty()) {
											val[i] = values[k];
											if (values[k].endsWith("."))
												val[i] = values[k].substring(0, values[k].length() - 1);
											i++;
										}
									}

									AutoProcessingData d = new AutoProcessingData(attachment.getAutoProcProgramAttachmentId(),
											Double.parseDouble(val[5]), Double.parseDouble(val[7]), fileName, attachment.getAutoProcProgramVO().getAutoProcProgramId());
									listAutoProcessingData.add(d);
								} catch (Exception e) {

								}
							} else if (line.contains("$TABLE: Cumulative intensity distribution")) {
								startToRead2 = true;
							} else if (startToRead2) {
								try {
									String[] values = line.split(" ");
									String[] val = new String[6];
									int i = 0;
									for (int k = 0; k < values.length; k++) {
										if (i <= 9 && !values[k].isEmpty()) {
											val[i] = values[k];
											if (values[k].endsWith("."))
												val[i] = values[k].substring(0, values[k].length() - 1);
											i++;
										}
									}

									AutoProcessingData d = new AutoProcessingData(attachment.getAutoProcProgramAttachmentId(),
											Double.parseDouble(val[0]), Double.parseDouble(val[1]), Double.parseDouble(val[2]),
											Double.parseDouble(val[3]), Double.parseDouble(val[4]), Double.parseDouble(val[5]),
											fileName, attachment.getAutoProcProgramVO().getAutoProcProgramId());
									listAutoProcessingData.add(d);
								} catch (Exception e) {

								}
							}
							if (line.contains("<hr>")) {
								startToRead = false;
							}

						}
					}
					inFile.close();

				} catch (Exception e) {
					throw e;
				} finally {
					if (inFile != null) {
						try {
							inFile.close();
						} catch (IOException ioex) {
							// ignore
							output = "nofile";
						}
					}

				}
			}
		}
		// o[0] is a boolean xscaleFile
		// o[1] is a boolean truncateLog
		// o[2] is List<AutoProcessingData>
//		Object[] o = new Object[3];
//		o[0] = xscaleFile;
//		o[1] = truncateLog;
//		o[2] = listAutoProcessingData;
		
		HashMap<String, Object> o = new HashMap<String, Object>();
		o.put("xscaleFile", xscaleFile);
		o.put("truncateLog", truncateLog);
		o.put("autoProcessingData", listAutoProcessingData);
		return o;
	}
}
