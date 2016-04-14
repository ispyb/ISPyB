/*******************************************************************************
 * This file is part of ISPyB.
 * 
 * ISPyB is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ISPyB is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with ISPyB.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors : S. Delageniere, R. Leal, L. Launer, K. Levik, S. Veyrier, P. Brenchereau, M. Bodin, A. De Maria Antolinos
 ******************************************************************************/
/**
 * XLSParser.java
 * 
 * Created on 16 August 2006, 13:11
 * Updated 29/10/2009 - PBU - Code formatting
 */

package ispyb.common.util.upload;

import ispyb.server.common.vos.shipping.Dewar3VO;
import ispyb.server.mx.vos.sample.Crystal3VO;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;

/**
 * Base Class for the Excel Spreadsheet Library Defines the functions required for all Excel formats so that they
 * work....
 * 
 * @author IMB
 */
public abstract class XLSParser {

	public static List<java.lang.String> SPACE_GROUPS = new ArrayList();

	private List crystals = new ArrayList();

	private List diffractionPlans = new ArrayList();

	private List dewars = new ArrayList();

	private Integer shippingId = null;

	// private ObjectFactory objFactory = new ObjectFactory();

	private List validationErrors = new ArrayList(); // List containing XlsUploadExceptions

	private List validationWarnings = new ArrayList(); // List containing XlsUploadExceptions to be seen as information

	public XLSParser() {
		SPACE_GROUPS.add("P1");
		SPACE_GROUPS.add("P2");
		SPACE_GROUPS.add("P21");
		SPACE_GROUPS.add("C2");
		SPACE_GROUPS.add("P222");
		SPACE_GROUPS.add("P2221");
		SPACE_GROUPS.add("P21212");
		SPACE_GROUPS.add("P212121");
		SPACE_GROUPS.add("C222");
		SPACE_GROUPS.add("C2221");
		SPACE_GROUPS.add("F222");
		SPACE_GROUPS.add("I222");
		SPACE_GROUPS.add("I212121");
		SPACE_GROUPS.add("P4");
		SPACE_GROUPS.add("P41");
		SPACE_GROUPS.add("P42");
		SPACE_GROUPS.add("P43");
		SPACE_GROUPS.add("P422");
		SPACE_GROUPS.add("P4212");
		SPACE_GROUPS.add("P4122");
		SPACE_GROUPS.add("P41212");
		SPACE_GROUPS.add("P4222");
		SPACE_GROUPS.add("P42212");
		SPACE_GROUPS.add("P4322");
		SPACE_GROUPS.add("P43212");
		SPACE_GROUPS.add("I4");
		SPACE_GROUPS.add("I41");
		SPACE_GROUPS.add("I422");
		SPACE_GROUPS.add("I4122");
		SPACE_GROUPS.add("P3");
		SPACE_GROUPS.add("P31");
		SPACE_GROUPS.add("P32");
		SPACE_GROUPS.add("P31");
		SPACE_GROUPS.add("P321");
		SPACE_GROUPS.add("P3112");
		SPACE_GROUPS.add("P3121");
		SPACE_GROUPS.add("P3212");
		SPACE_GROUPS.add("P3221");
		SPACE_GROUPS.add("P6");
		SPACE_GROUPS.add("P61");
		SPACE_GROUPS.add("P65");
		SPACE_GROUPS.add("P62");
		SPACE_GROUPS.add("P64");
		SPACE_GROUPS.add("P63");
		SPACE_GROUPS.add("P622");
		SPACE_GROUPS.add("P6122");
		SPACE_GROUPS.add("P6522");
		SPACE_GROUPS.add("P6222");
		SPACE_GROUPS.add("P6422");
		SPACE_GROUPS.add("P6322");
		SPACE_GROUPS.add("R3");
		SPACE_GROUPS.add("R32");
		SPACE_GROUPS.add("P23");
		SPACE_GROUPS.add("P213");
		SPACE_GROUPS.add("P432");
		SPACE_GROUPS.add("P4232");
		SPACE_GROUPS.add("P4332");
		SPACE_GROUPS.add("P4132");
		SPACE_GROUPS.add("F23");
		SPACE_GROUPS.add("F432");
		SPACE_GROUPS.add("F4132");
		SPACE_GROUPS.add("I23");
		SPACE_GROUPS.add("I213");
		SPACE_GROUPS.add("I432");
		SPACE_GROUPS.add("I4132");
	}

	// /**
	// * Open an Excel file with the given filename
	// *
	// * @param filename
	// * The filename of the Excel file to open and parse
	// * @throws java.lang.Exception
	// * if there is a problem
	// */
	// public void open(String filename) throws Exception {
	// open(new FileInputStream(filename));
	// }
	//
	// /**
	// * Uses the input stream to extract the information from the spreadsheet
	// *
	// * @param file
	// * The input stream to use
	// * @throws java.lang.Exception
	// * when there is a problem
	// */
	// public abstract void open(InputStream file) throws Exception;

	/**
	 * Retrieves the shippingId from the xls file Populates shippingId attribute
	 */
	public void retrieveShippingId(String filename) throws Exception {
		retrieveShippingId(new FileInputStream(filename));
	}

	public abstract void retrieveShippingId(InputStream file) throws Exception;

	/**
	 * Used when creating a spreadsheet from known data
	 * 
	 * @param filename
	 *            The filename to write the data to in Excel format
	 * @throws java.lang.Exception
	 *             if there is a problem
	 */
	public abstract void write(String filename) throws Exception;

	/**
	 * Gets the list of crystals that have been defined by the Excel spreadsheet
	 * 
	 * @return The list of crystals
	 */

	/**
	 * Validates
	 * 
	 * @return
	 * @throws FileNotFoundException
	 *             XlsUploadException
	 */
	public List validate(String filename, Hashtable listProteinAcronym_SampleName, Integer proposalId)
			throws XlsUploadException, FileNotFoundException, Exception {
		return validate(new FileInputStream(filename), listProteinAcronym_SampleName, proposalId);
	}

	public abstract List validate(InputStream file, Hashtable listProteinAcronym_SampleName, Integer proposalId)
			throws XlsUploadException, Exception;

	public List getCrystals() {
		return crystals;
	}

	/**
	 * Sets the list of crystals
	 * 
	 * @param crystals
	 *            a list of crystals
	 */
	public void setCrystals(List crystals) {
		this.crystals = crystals;
	}

	/**
	 * Gets the list of Diffraction Plans defined by the spreadsheet
	 * 
	 * @return A list of diffraction plans
	 */
	public List getDiffractionPlans() {
		return diffractionPlans;
	}

	/**
	 * Sets a list of Diffraction Plans
	 * 
	 * @param diffractionPlans
	 *            The list of Diffraction Plans
	 */
	public void setDiffractionPlans(List diffractionPlans) {
		this.diffractionPlans = diffractionPlans;
	}

	/**
	 * Gets the list of Dewars defined by the spreadsheet
	 * 
	 * @return The list of Dewars
	 */
	public List getDewars() {
		return dewars;
	}

	/**
	 * Sets a list of Dewars
	 * 
	 * @param dewars
	 *            A list of Dewars
	 */
	public void setDewars(List dewars) {
		this.dewars = dewars;
	}

	/**
	 * Get the dewar information based on its barcode or null if it is not defined
	 * 
	 * @param barcode
	 *            The barcode of the Dewar to find
	 * @return The Dewar information for the given barcode or null if it is not defined
	 */
	public Dewar3VO getDewar(String barcode) {
		if ((null == barcode) || (barcode.equals(""))) {
			return null;
		}
		Iterator it = this.getDewars().iterator();

		while (it.hasNext()) {
			Dewar3VO dewar = (Dewar3VO) it.next();
			if (dewar.getBarCode().equals(barcode)) {
				return dewar;
			}
		}

		return null;
	}

	/**
	 * Gets the Crystal information based on the CrystalUUID or returns null if its not defined
	 * 
	 * @param crystalUUID
	 *            The CrystalUUID of the crystal
	 * @return The Crystal information or null if not defined
	 */
	public Crystal3VO getCrystal(String crystalUUID) {
		if ((null == crystalUUID) || (crystalUUID.equals(""))) {
			return null;
		}
		Iterator it = this.getCrystals().iterator();

		while (it.hasNext()) {
			Crystal3VO crystal = (Crystal3VO) it.next();
			if (crystal.getCrystalUUID().equals(crystalUUID)) {
				return crystal;
			}
		}

		return null;
	}

	// /**
	// * Compares Collect Plans
	// *
	// * @param cPlan1
	// * The first Collect Plan to compare
	// * @param cPlan2
	// * The second CollectPlan to compare
	// * @return true if the Collect Plans are identical, false otherwise
	// */
	// public boolean compareCollectPlans(CollectPlan cPlan1, CollectPlan cPlan2) {
	// if (cPlan1 == cPlan2) {
	// return true;
	// }
	// if (cPlan1.getAnomalousScattererElement() != cPlan2.getAnomalousScattererElement()) {
	// return false;
	// }
	// if (cPlan1.getExperimentType() != cPlan2.getExperimentType()) {
	// return false;
	// }
	// if (cPlan1.getNotesComments() != cPlan2.getNotesComments()) {
	// return false;
	// }
	// if (cPlan1.getOscillationAnglePerImage() != cPlan2.getOscillationAnglePerImage()) {
	// return false;
	// }
	// if (cPlan1.getProcessingLevel() != cPlan2.getProcessingLevel()) {
	// return false;
	// }
	// if (cPlan1.getTemperature() != cPlan2.getTemperature()) {
	// return false;
	// }
	// if (cPlan1.isCollectTwinnedData() != cPlan2.isCollectTwinnedData()) {
	// return false;
	// }
	// if (cPlan1.getSweepInformation().size() != cPlan2.getSweepInformation().size()) {
	// return false;
	// }
	//
	// return true;
	// }

	/**
	 * Compares two Crystals
	 * 
	 * @param crystal1
	 *            The first Crystal to compare
	 * @param crystal2
	 *            The second Crystal to compare
	 * @return true if these Crystals are identical, false otherwise
	 */
	public boolean compareCrystals(Crystal3VO crystal1, Crystal3VO crystal2) {
		if (crystal1 == crystal2) {
			return true;
		}
		if ((crystal1 == null) || (crystal2 == null)) {
			return false;
		}
		if (crystal1.getCrystalUUID() == crystal2.getCrystalUUID()) {
			return true;
		}
		if (crystal1.getCellA() != crystal2.getCellA()) {
			return false;
		}
		if (crystal1.getCellAlpha() != crystal2.getCellAlpha()) {
			return false;
		}
		if (crystal1.getCellB() != crystal2.getCellB()) {
			return false;
		}
		if (crystal1.getCellBeta() != crystal2.getCellBeta()) {
			return false;
		}
		if (crystal1.getCellC() != crystal2.getCellC()) {
			return false;
		}
		if (crystal1.getColor() != crystal2.getColor()) {
			return false;
		}
		if (crystal1.getCellGamma() != crystal2.getCellGamma()) {
			return false;
		}

		if (crystal1.getMorphology() != crystal2.getMorphology()) {
			return false;
		}
		if (crystal1.getName() != crystal2.getName()) {
			return false;
		}
		// if (crystal1.getResolution() != crystal2.getResolution()) {
		// return false;
		// }
		if (crystal1.getSpaceGroup() != crystal2.getSpaceGroup()) {
			return false;
		}
		if (crystal1.getSizeX() != crystal2.getSizeX()) {
			return false;
		}
		if (crystal1.getSizeY() != crystal2.getSizeY()) {
			return false;
		}
		if (crystal1.getSizeZ() != crystal2.getSizeZ()) {
			return false;
		}

		return true;
	}

	// /**
	// * Compares two Diffraction Plans
	// *
	// * @param dPlan1
	// * The first Diffraction Plan
	// * @param dPlan2
	// * The second Diffraction Plan
	// * @return true if the Diffraction Plans are identical, false otherwise
	// */
	// public boolean compareDiffractionPlans(DiffractionPlan3VO dPlan1, DiffractionPlan3VO dPlan2) {
	// if ((dPlan1 == null) || (dPlan2 == null)) {
	// return false;
	// }
	// if (dPlan1.getDiffractionPlanUUID() == dPlan2.getDiffractionPlanUUID()) {
	// // if UUIDs are the same, we can assume the rest is the same....
	// return true;
	// }
	// boolean same = true;
	// if (dPlan1.getProjectUUID() != dPlan2.getProjectUUID()) {
	// return false;
	// }
	// // if (!compareScreenPlans(dPlan1.getScreenPlan(), dPlan2.getScreenPlan())) {
	// // return false;
	// // }
	// if (!compareCollectPlans(dPlan1.getCollectPlan(), dPlan2.getCollectPlan())) {
	// return false;
	// }
	// // if its got this far then i think we can say that they are the same...
	// // just have to merge the crystal lists....
	//
	// return true;
	// }

	// /**
	// * Adds a Diffraction Plan to the list of Diffraction plans, checking whether it already exists. If it does, the
	// * existing one is returned
	// *
	// * @param diffractionPlan
	// * The Diffraction Plan to add
	// * @return Either the Diffraction plan that has been added or the existing Diffraction Plan if it had already been
	// * defined
	// */
	// public DiffractionPlan3VO addDiffractionPlan(DiffractionPlanElement diffractionPlan) {
	// // need to compare diffraction plans!
	// Iterator it = this.getDiffractionPlans().iterator();
	// while (it.hasNext()) {
	// DiffractionPlanElement existingDiffractionPlan = (DiffractionPlanElement) it.next();
	// if (compareDiffractionPlans(existingDiffractionPlan, diffractionPlan)) {
	// // need to merge crystals....
	// Iterator crysIt = diffractionPlan.getCrystalUUID().iterator();
	// while (crysIt.hasNext()) {
	// String crystalUUID = (String) crysIt.next();
	// if (!existingDiffractionPlan.getCrystalUUID().contains(crystalUUID)) {
	// existingDiffractionPlan.getCrystalUUID().add(crystalUUID);
	// }
	// }
	// return existingDiffractionPlan;
	// }
	// }
	//
	// this.getDiffractionPlans().add(diffractionPlan);
	// return diffractionPlan;
	// }

	/**
	 * Adds a Crystal to the list of Crystals, checking whether it already exists. If it does, the existing one is
	 * returned
	 * 
	 * @param crystal
	 *            The Crystal to add
	 * @return Either the Crystal that has been added or the existing Crystal if it had already been defined
	 */
	public Crystal3VO addCrystal(Crystal3VO crystal) {
		Crystal3VO existingCrystal = null;

		existingCrystal = this.getCrystal(crystal.getCrystalUUID());

		if (existingCrystal == null) {
			this.getCrystals().add(crystal);
			existingCrystal = crystal;
		}
		return existingCrystal;
	}

	/**
	 * Adds a Dewar to the list of Dewars, checking whether it already exists. If it does, the existing one is returned
	 * 
	 * @param dewar
	 *            The Dewar to add
	 * @return Either the Dewar that has been added or the existing Dewar if it had already been defined
	 */
	public Dewar3VO addDewar(Dewar3VO dewar) {
		// need to check whether this dewar already exists and return that....
		Dewar3VO existingDewar = this.getDewar(dewar.getBarCode());
		if (existingDewar == null) {
			getDewars().add(dewar);
			existingDewar = dewar;
		}

		return existingDewar;
	}

	/**
	 * Remove a Dewar from the list of Dewars, checking whether it already exists.
	 * 
	 * @param dewar
	 *            The Dewar to remove
	 * @return True if the Dewar existed and was removed, False otherwise
	 */
	public boolean removeDewarIfEmpty(Dewar3VO dewar) {
		boolean removedOK = true;
		Dewar3VO existingDewar = this.getDewar(dewar.getBarCode());
		if (existingDewar == null) { // Dewar did not exist
			removedOK = false;
		} else // Dewar was there, deleting it ...
		{
			if (dewar.getContainerVOs().size() == 0) {
				getDewars().remove(dewar);
			}
		}

		return removedOK;
	}

	// /**
	// * Gets the JAXB Object Factory from which the data model elements can be generated
	// *
	// * @return The Object Factory
	// */
	// public ObjectFactory getObjFactory() {
	// return objFactory;
	// }

	// /**
	// * Sets the ObjectFactory for the JAXB Objects
	// *
	// * @param objFactory
	// * The Object Factory to use
	// */
	// public void setObjFactory(ObjectFactory objFactory) {
	// this.objFactory = objFactory;
	// }

	/**
	 * Converts from Excel Cell contents to a String
	 * 
	 * @param cell
	 *            The Cell to convert
	 * @return A String value of the contents of the cell
	 */
	public String cellToString(HSSFCell cell) {
		String retVal = "";
		if (cell == null) {
			return retVal;
		}
		if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
			retVal = cell.getStringCellValue();
		} else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			retVal = String.valueOf(new Double(cell.getNumericCellValue()).intValue());
		} else if (cell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN) {
			if (new Boolean(cell.getBooleanCellValue()) == Boolean.TRUE) {
				retVal = "true";
			} else {
				retVal = "false";
			}
		}
		return retVal;
	}

	/**
	 * Converts from Excel Cell contents to a double
	 * 
	 * @param cell
	 *            The Cell to convert
	 * @return The double value contained within the Cell or 0.0 if the Cell is not the correct type or is undefined
	 */
	public double cellToDouble(HSSFCell cell) {
		Double retVal = new Double(0.0);
		if (cell == null) {
			return retVal.doubleValue();
		}
		if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			retVal = new Double(cell.getNumericCellValue());
		}
		return retVal.doubleValue();
	}

	/**
	 * Converts from Excel Cell contents to a int
	 * 
	 * @param cell
	 *            The Cell to convert
	 * @return The int value contained within the Cell or 0 if the Cell is not the correct type or is undefined
	 */
	public int cellToInteger(HSSFCell cell) {
		Integer retVal = new Integer(0);
		if (cell == null) {
			return retVal.intValue();
		}

		if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			Double value = new Double(cell.getNumericCellValue());
			retVal = new Integer(value.intValue());
		}
		return retVal.intValue();
	}

	/**
	 * Converts from Excel Cell contents to a boolean
	 * 
	 * @param cell
	 *            The Cell to convert
	 * @return The boolean value contained within the Cell or false if the Cell is not the correct type or is undefined
	 */
	public boolean cellToBoolean(HSSFCell cell) {
		Boolean retVal = new Boolean(false);
		if (cell == null) {
			return retVal.booleanValue();
		}

		if (cell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN) {
			retVal = new Boolean(cell.getBooleanCellValue());
		}
		return retVal.booleanValue();
	}

	public List<XlsUploadException> getValidationErrors() {
		return validationErrors;
	}

	public void setValidationErrors(List validationErrors) {
		this.validationErrors = validationErrors;
	}

	public List<XlsUploadException> getValidationWarnings() {
		return validationWarnings;
	}

	public void setValidationWarnings(List validationWarnings) {
		this.validationWarnings = validationWarnings;
	}

	public Integer getShippingId() {
		return shippingId;
	}

	public void setShippingId(Integer shippingId) {
		this.shippingId = shippingId;
	}
}
