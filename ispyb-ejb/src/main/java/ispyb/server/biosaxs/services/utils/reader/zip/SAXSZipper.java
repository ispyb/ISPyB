package ispyb.server.biosaxs.services.utils.reader.zip;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import ispyb.common.util.HashMapToZip;
import ispyb.common.util.PathUtils;
import ispyb.server.biosaxs.vos.advanced.FitStructureToExperimentalData3VO;
import ispyb.server.biosaxs.vos.advanced.RigidBodyModeling3VO;
import ispyb.server.biosaxs.vos.advanced.Superposition3VO;
import ispyb.server.biosaxs.vos.datacollection.Frametolist3VO;
import ispyb.server.biosaxs.vos.datacollection.Merge3VO;
import ispyb.server.biosaxs.vos.datacollection.ModelToList3VO;
import ispyb.server.biosaxs.vos.datacollection.Subtraction3VO;
import ispyb.server.biosaxs.vos.datacollection.SubtractiontoAbInitioModel3VO;

public class SAXSZipper {

	private static HashMap<String, String> parseAverages(List<Merge3VO> merges) {
		HashMap<String, String> files = new HashMap<String, String>();
		try {
			/**
			 * Adding the average including average file + all the frames
			 */
			for (Merge3VO merge3vo : merges) {
				/** Adding the average to the ZIP file **/
				String averageFilePath = PathUtils.getPath(merge3vo.getAverageFilePath());
				if (new File(averageFilePath).exists()) {
					files.put(new File(averageFilePath).getName(), averageFilePath);
				}
				/** Adding all the frames to the ZIP **/
				for (Frametolist3VO frametolist3VO : merge3vo.getFramelist3VO().getFrametolist3VOs()) {
					if (frametolist3VO.getFrame3VO() != null) {
						String filePath = PathUtils.getPath(frametolist3VO.getFrame3VO().getFilePath());
						if (new File(filePath).exists()) {
							files.put(new File(filePath).getName(), filePath);
						}
					}
				}
			}
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		return files;
	}

	private static void appendFile(HashMap<String, String> files, String filePath) {
		appendFile(files, filePath, null);
	}

	private static void appendFile(HashMap<String, String> files, String filePath, String folder) {
		if (filePath != null) {
			filePath = PathUtils.getPath(filePath);
			if (new File(filePath).exists()) {
				if (folder != null) {
					files.put(folder + "/" + new File(filePath).getName(), filePath);
				} else {
					files.put(new File(filePath).getName(), filePath);
				}
			}
		}
	}

	private static HashMap<String, String> parseSubtractions(List<Subtraction3VO> subtractions) {
		HashMap<String, String> files = new HashMap<String, String>();
		try {
			/**
			 * Adding the subtractions
			 */
			for (Subtraction3VO subtraction : subtractions) {
				SAXSZipper.appendFile(files, subtraction.getSampleAverageFilePath());
				SAXSZipper.appendFile(files, subtraction.getBufferAverageFilePath());
				SAXSZipper.appendFile(files, subtraction.getSubstractedFilePath());

				if (subtraction.getSampleOneDimensionalFiles() != null) {
					for (Frametolist3VO frameToList : subtraction.getSampleOneDimensionalFiles().getFrametolist3VOs()) {
						if (frameToList.getFrame3VO() != null) {
							SAXSZipper.appendFile(files, frameToList.getFrame3VO().getFilePath());
						}
					}
				}

				if (subtraction.getBufferOneDimensionalFiles() != null) {
					for (Frametolist3VO frameToList : subtraction.getBufferOneDimensionalFiles().getFrametolist3VOs()) {
						if (frameToList.getFrame3VO() != null) {
							SAXSZipper.appendFile(files, frameToList.getFrame3VO().getFilePath());
						}
					}
				}
				/** Fits **/
				if (subtraction.getFitStructureToExperimentalData3VOs() != null) {
					for (FitStructureToExperimentalData3VO fit : subtraction.getFitStructureToExperimentalData3VOs()) {
						SAXSZipper.appendFile(files, fit.getFitFilePath(), "fit");
						SAXSZipper.appendFile(files, fit.getLogFilePath(), "fit");
						SAXSZipper.appendFile(files, fit.getOutputFilePath(), "fit");
					}
				}

				/** Superposition **/
				if (subtraction.getSuperposition3VOs() != null) {
					for (Superposition3VO superposition : subtraction.getSuperposition3VOs()) {
						SAXSZipper.appendFile(files, superposition.getAbinitioModelPdbFilePath(), "superposition");
						SAXSZipper.appendFile(files, superposition.getAprioriPdbFilePath(), "superposition");
						SAXSZipper.appendFile(files, superposition.getAlignedPdbFilePath(), "superposition");
					}
				}

				/** Superposition **/
				if (subtraction.getRigidBodyModeling3VOs() != null) {
					for (RigidBodyModeling3VO rbm : subtraction.getRigidBodyModeling3VOs()) {
						SAXSZipper.appendFile(files, rbm.getContactDescriptionFilePath(), "rigidbody");
						SAXSZipper.appendFile(files, rbm.getCrossCorrConfigFilePath(), "rigidbody");
						SAXSZipper.appendFile(files, rbm.getCurveConfigFilePath(), "rigidbody");
						SAXSZipper.appendFile(files, rbm.getFitFilePath(), "rigidbody");
						SAXSZipper.appendFile(files, rbm.getLogFilePath(), "rigidbody");
						SAXSZipper.appendFile(files, rbm.getRigidBodyModelFilePath(), "rigidbody");
						SAXSZipper.appendFile(files, rbm.getSubUnitConfigFilePath(), "rigidbody");
					}
				}


			}
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		return files;

	}

	
	private static HashMap<String, String> parseAbinitio(List<SubtractiontoAbInitioModel3VO> abinitios) {
		HashMap<String, String> files = new HashMap<String, String>();
		/** Abinitio **/
		try {

			for (SubtractiontoAbInitioModel3VO subToAb : abinitios) {
					if (subToAb.getAbinitiomodel3VO() != null) {
						
						System.out.println("getAbinitiomodel3VO: ");
						
						if (subToAb.getAbinitiomodel3VO().getAveragedModel() != null) {
							System.out.println("getAveragedModel: ");
							SAXSZipper.appendFile(files, subToAb.getAbinitiomodel3VO().getAveragedModel().getFirFile(), "abinitio");
							SAXSZipper.appendFile(files, subToAb.getAbinitiomodel3VO().getAveragedModel().getFitFile(), "abinitio");
							SAXSZipper.appendFile(files, subToAb.getAbinitiomodel3VO().getAveragedModel().getPdbFile(), "abinitio");
						}
						
						if (subToAb.getAbinitiomodel3VO().getRapidShapeDeterminationModel() != null) {
							System.out.println("getRapidShapeDeterminationModel: ");
							SAXSZipper.appendFile(files, subToAb.getAbinitiomodel3VO().getRapidShapeDeterminationModel().getFirFile(), "abinitio");
							SAXSZipper.appendFile(files, subToAb.getAbinitiomodel3VO().getRapidShapeDeterminationModel().getFitFile(), "abinitio");
							SAXSZipper.appendFile(files, subToAb.getAbinitiomodel3VO().getRapidShapeDeterminationModel().getPdbFile(), "abinitio");
						}
						
						if (subToAb.getAbinitiomodel3VO().getShapeDeterminationModel() != null) {
							System.out.println("getShapeDeterminationModel: ");
							SAXSZipper.appendFile(files, subToAb.getAbinitiomodel3VO().getShapeDeterminationModel().getFirFile(), "abinitio");
							SAXSZipper.appendFile(files, subToAb.getAbinitiomodel3VO().getShapeDeterminationModel().getFitFile(), "abinitio");
							SAXSZipper.appendFile(files, subToAb.getAbinitiomodel3VO().getShapeDeterminationModel().getPdbFile(), "abinitio");
						}
						
						if (subToAb.getAbinitiomodel3VO().getModelList3VO() != null) {
							for (ModelToList3VO model : subToAb.getAbinitiomodel3VO().getModelList3VO().getModeltolist3VOs()) {
								System.out.println("models: ");
								if (model.getModel3VO() != null) {
									System.out.println("models: " + model.getModel3VO().getPdbFile());
									SAXSZipper.appendFile(files, model.getModel3VO().getFirFile(), "abinitio");
									SAXSZipper.appendFile(files, model.getModel3VO().getFitFile(), "abinitio");
									SAXSZipper.appendFile(files, model.getModel3VO().getLogFile(), "abinitio");
									SAXSZipper.appendFile(files, model.getModel3VO().getPdbFile(), "abinitio");
								}
							}
						}
				}
			}
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		return files;
	}
	
	public static byte[] zip(List<Merge3VO> merges, List<Subtraction3VO> subtractions) {
		HashMap<String, String> files = new HashMap<String, String>();
		files.putAll(SAXSZipper.parseAverages(merges));
		files.putAll(SAXSZipper.parseSubtractions(subtractions));
		return HashMapToZip.doZip(files);
	}

	public static byte[] zip(List<Merge3VO> merges, List<Subtraction3VO> subtractions, List<SubtractiontoAbInitioModel3VO> abinitios) {
		HashMap<String, String> files = new HashMap<String, String>();
		files.putAll(SAXSZipper.parseAverages(merges));
		files.putAll(SAXSZipper.parseSubtractions(subtractions));
		files.putAll(SAXSZipper.parseAbinitio(abinitios));
		return HashMapToZip.doZip(files);
	}

}
