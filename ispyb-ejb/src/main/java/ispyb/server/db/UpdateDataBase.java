/*************************************************************************************************
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
 ****************************************************************************************************/

package ispyb.server.db;

import ispyb.common.util.Constants;
import ispyb.common.util.PathUtils;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.mx.services.collections.Workflow3Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

/**
 * Scripts to update the ispyb database and to generate some statistics (exported in xls or csv files). It is also used ot populate the
 * database with test data. This class is not used in the ISPyB application and only designed for the ESRF
 * 
 * @author BODIN
 * 
 */
public class UpdateDataBase {

	private final static Logger LOG = Logger.getLogger(UpdateDataBase.class);

	/**
	 * update the DataCollection table with the detectorId
	 */
	public static void setDetectorInDataCollection() {
		System.out.println("********* setDetectorInDataCollection *************");
		String query = "SELECT d.dataCollectionId, s.sessionId, s.beamLineSetupId FROM DataCollection d, DataCollectionGroup g, BLSession s "
				+ "WHERE d.dataCollectionGroupId = g.dataCollectionGroupId AND g.sessionId = s.sessionId AND d.detectorId is NULL  ORDER BY dataCollectionId";
		try {
			Connection con = getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				Integer dataCollectionId = rs.getInt("dataCollectionId");
				Integer sessionId = rs.getInt("sessionId");
				Integer beamLineSetupId = rs.getInt("beamLineSetupId");
				if (beamLineSetupId != null) {
					String queryBL = "SELECT detectorType, detectorManufacturer,detectorModel, detectorPixelSizeHorizontal, detectorPixelSizeVertical "
							+ "FROM BeamLineSetup WHERE beamLineSetupId = " + beamLineSetupId + " ;";
					Statement stmt2 = con.createStatement();
					ResultSet rs2 = stmt2.executeQuery(queryBL);
					while (rs2.next()) {
						String detectorType = rs2.getString("detectorType");
						String detectorManufacturer = rs2.getString("detectorManufacturer");
						String detectorModel = rs2.getString("detectorModel");
						String detectorPixelSizeHorizontal = rs2.getString("detectorPixelSizeHorizontal");
						String detectorPixelSizeVertical = rs2.getString("detectorPixelSizeVertical");
						String s1 = "(detectorPixelSizeHorizontal like '" + detectorPixelSizeHorizontal
								+ "' OR detectorPixelSizeHorizontal = '" + detectorPixelSizeHorizontal + "') ";
						if (detectorPixelSizeHorizontal == null) {
							s1 = "detectorPixelSizeHorizontal is NULL ";
						}
						String s2 = "(detectorPixelSizeVertical like '" + detectorPixelSizeVertical
								+ "' OR detectorPixelSizeVertical = '" + detectorPixelSizeVertical + "' ) ";
						if (detectorPixelSizeHorizontal == null) {
							s2 = "detectorPixelSizeVertical is NULL ";
						}
						String s3 = " detectorManufacturer like '" + detectorManufacturer + "' ";
						if (detectorManufacturer == null)
							s3 = " detectorManufacturer is NULL ";
						Integer detectorId = null;
						String queryDetector = "SELECT detectorId FROM Detector WHERE " + "detectorType like '" + detectorType
								+ "' AND " + s3 + " AND detectorModel like '" + detectorModel + "' AND " + s1 + " AND " + s2 + " ;";
						Statement stmt3 = con.createStatement();
						ResultSet rs3 = stmt3.executeQuery(queryDetector);
						while (rs3.next()) {
							detectorId = rs3.getInt("detectorId");
						}
						rs3.close();
						stmt3.close();
						if (detectorId != null) {
							String queryUpdate = "UPDATE DataCollection set detectorId = " + detectorId + " "
									+ "WHERE dataCollectionId = " + dataCollectionId + ";";
							Statement stmtU = con.createStatement();
							stmtU.executeUpdate(queryUpdate);
							stmtU.close();
						} else {
							if (detectorType != null)
								System.out.println("WARNING the Detector is null for dataCollectionId=" + dataCollectionId
										+ " -- with detectorType " + detectorType + ", detectorManufacturer " + detectorManufacturer
										+ ", detectorModel " + detectorModel + ", detectorPixelSizeHorizontal "
										+ detectorPixelSizeHorizontal + ", detectorPixelSizeVertical " + detectorPixelSizeVertical);
						}
					}
					rs2.close();
					stmt2.close();
				}
			}
			rs.close();
			stmt.close();
			con.close();
			System.out.println("** END setDetectorInDataCollection **");
		} catch (SQLException e) {
			LOG.error(e);
			System.out.println(e);
		} catch (Exception e) {
			LOG.error(e);
			System.out.println(e);
		}
	}

	/**
	 * update the ScreeningOutput.ranskingResolution with the ScreeningStrategy.rankingResolution
	 */
	public static void setRankingResolutionInScreeningOutput() {
		System.out.println("********* setRankingResolutionInScreeningOutput *************");
		String query = "SELECT s.rankingResolution, o.screeningOutputId, s.screeningStrategyId FROM ScreeningStrategy s, ScreeningOutput o WHERE s.screeningoutputId = o.screeningOutputId;";
		try {
			Connection con = getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				Integer screeningOutputId = rs.getInt("screeningOutputId");
				Integer screeningStrategyId = rs.getInt("screeningStrategyId");
				Double rankingResolution = rs.getDouble("rankingResolution");
				String queryUpdate = "UPDATE ScreeningOutput set rankingResolution = " + rankingResolution
						+ " WHERE screeningOutputId = " + screeningOutputId + " ;";
				Statement stmtU = con.createStatement();
				stmtU.executeUpdate(queryUpdate);
				stmtU.close();
			}
			rs.close();
			stmt.close();
			con.close();
			System.out.println("** END setRankingResolutionInScreeningOutput **");
		} catch (SQLException e) {
			LOG.error(e);
			System.out.println(e);
		} catch (Exception e) {
			LOG.error(e);
			System.out.println(e);
		}
	}

	/**
	 * update the diffractionPlanId and xmlSampleInformation from ScreeningInput table
	 */
	public static void setScreeningInputInScreening() {
		System.out.println("********* setScreeningInputInScreening *************");
		String query = "SELECT screeningInputId, screeningId, diffractionPlanId, xmlSampleInformation FROM ScreeningInput ORDER BY screeningInputId;";
		try {
			Connection con = getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				Integer screeningInputId = rs.getInt("screeningInputId");
				Integer screeningId = rs.getInt("screeningId");
				Integer diffractionPlanId = rs.getInt("diffractionPlanId");
				String xmlSampleInformation = rs.getString("xmlSampleInformation");
				String s = "";
				if (xmlSampleInformation != null && xmlSampleInformation.length() > 0) {
					s = ", xmlSampleInformation= '" + xmlSampleInformation + "'";
				}
				if (diffractionPlanId == 0)
					diffractionPlanId = null;
				String queryUpdate = "UPDATE Screening set diffractionPlanId = " + diffractionPlanId + s + " WHERE screeningId = "
						+ screeningId + " ;";
				Statement stmtU = con.createStatement();
				stmtU.executeUpdate(queryUpdate);
				stmtU.close();
			}
			rs.close();
			stmt.close();
			con.close();
			System.out.println("** END setScreeningInputInScreening **");
		} catch (SQLException e) {
			LOG.error(e);
			System.out.println(e);
		} catch (Exception e) {
			LOG.error(e);
			System.out.println(e);
		}
	}

	/**
	 * copy the data from ScreeningStrategy in screeningStrategyWedge & ScreeningStrategySubWedge
	 */
	public static void copyScreeningStrategyInWedge() {
		System.out.println("********* copyScreeningStrategyInWedge *************");
		String query = "SELECT  * FROM ScreeningStrategy " + "WHERE `screeningStrategyId` NOT IN "
				+ "(SELECT DISTINCT `screeningStrategyId` FROM `ScreeningStrategyWedge` " + "WHERE `screeningStrategyId` IS NOT NULL)";

		try {
			Connection con = getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				Integer screeningStrategyId = rs.getInt("screeningStrategyId");
				String phiStart = rs.getString("phiStart");
				String phiEnd = rs.getString("phiEnd");
				String rotation = rs.getString("rotation");
				String exposureTime = rs.getString("exposureTime");
				String resolution = rs.getString("resolution");
				String completeness = rs.getString("completeness");
				String multiplicity = rs.getString("multiplicity");
				Byte anomalous = rs.getByte("anomalous");
				String program = rs.getString("program");
				String rankingResolution = rs.getString("rankingResolution");
				String transmission = rs.getString("transmission");
				String comments = program == "null" ? "" : "'" + program + "'";

				String queryWedge = "INSERT INTO ScreeningStrategyWedge"
						+ "(screeningStrategyWedgeId, screeningStrategyId, wedgeNumber, resolution, "
						+ "completeness, multiplicity, doseTotal, numberOfImages, phi, kappa, " + "comments, wavelength) "
						+ " VALUES (NULL," + screeningStrategyId + ", 1, " + resolution + ", " + completeness + "" + ", "
						+ multiplicity + ",   null, null, null, null, " + comments + ", null )";
				Statement stmtW = con.createStatement();
				stmtW.executeUpdate(queryWedge);
				Integer screeningStrategyWedgeId = null;
				ResultSet rs2 = stmtW.getGeneratedKeys();
				if (rs2.next()) {
					screeningStrategyWedgeId = rs2.getInt(1);
				}
				rs2.close();
				stmtW.close();
				if (screeningStrategyWedgeId != null) {
					String querySubWedge = "INSERT INTO ScreeningStrategySubWedge"
							+ "(screeningStrategySubWedgeId, screeningStrategyWedgeId, subWedgeNumber,"
							+ "rotationAxis, axisStart, axisEnd, exposureTime, transmission, oscillationRange, "
							+ "completeness, multiplicity, doseTotal, numberOfImages, comments ) " + " VALUES (NULL,"
							+ screeningStrategyWedgeId + ", 1, " + rotation + ", " + phiStart + ", " + "" + phiEnd + ", "
							+ exposureTime + ", " + transmission + ", null," + completeness + "" + ", " + multiplicity
							+ ", null, null, " + comments + " )";
					Statement stmtSW = con.createStatement();
					stmtSW.executeUpdate(querySubWedge);
					stmtSW.close();
				}
			}
			rs.close();
			stmt.close();
			con.close();
			System.out.println("** END copyScreeningStrategyInWedge **");
		} catch (SQLException e) {
			LOG.error(e);
			System.out.println(e);
		} catch (Exception e) {
			LOG.error(e);
			System.out.println(e);
		}
	}

	/**
	 * switch actualSampleSlotInContainer with actualContainerSlotInSC
	 */
	public static void switchSlot() {
		System.out.println("********* switchSlot *************");
		String query = "SELECT  dataCollectionGroupId, actualSampleSlotInContainer, actualContainerSlotInSC "
				+ "FROM DataCollectionGroup where startTime < '30/09/2012 00:00:00' AND (actualSampleSlotInContainer is not null or actualContainerSlotInSC is not null); ";

		try {
			Connection con = getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				Integer dataCollectionGroupId = rs.getInt("dataCollectionGroupId");
				String actualSampleSlotInContainer = rs.getString("actualSampleSlotInContainer");
				String actualContainerSlotInSC = rs.getString("actualContainerSlotInSC");

				String newActualSampleSlotInContainer = "'" + actualContainerSlotInSC + "'";
				String newActualContainerSlotInSC = "'" + actualSampleSlotInContainer + "'";
				if (actualContainerSlotInSC == null) {
					newActualSampleSlotInContainer = "null";
				}
				if (actualSampleSlotInContainer == null) {
					newActualContainerSlotInSC = "null";
				}
				String updateQuery = "UPDATE DataCollectionGroup set actualSampleSlotInContainer = " + newActualSampleSlotInContainer
						+ "  , actualContainerSlotInSC =  " + newActualContainerSlotInSC + " WHERE dataCollectionGroupId = "
						+ dataCollectionGroupId + ";";
				Statement stmtU = con.createStatement();
				stmtU.executeUpdate(updateQuery);
				stmtU.close();
			}
			rs.close();
			stmt.close();
			con.close();
			System.out.println("** END switchSlot **");
		} catch (SQLException e) {
			LOG.error(e);
			System.out.println(e);
		} catch (Exception e) {
			LOG.error(e);
			System.out.println(e);
		}
	}

	/**
	 * update Detector value
	 */
	public static void updateDetector() {
		System.out.println("********* updateDetector *************");
		String query = "SELECT  d.dataCollectionId , g.detectorMode" + " FROM DataCollection d, DataCollectionGroup g"
				+ " where d.detectorId=9 AND d.dataCollectionGroupId = g.dataCollectionGroupId; ";

		try {
			Connection con = getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				Integer dataCollectionId = rs.getInt("dataCollectionId");
				String detectorMode = rs.getString("detectorMode");
				Integer newDetectorId = 21;
				if (detectorMode != null
						&& (detectorMode.equalsIgnoreCase("Hardware binned") || detectorMode.equalsIgnoreCase("Software binned")))
					newDetectorId = 30;
				String updateQuery = "UPDATE DataCollection set detectorId = " + newDetectorId + " WHERE dataCollectionId = "
						+ dataCollectionId + ";";
				Statement stmtU = con.createStatement();
				stmtU.executeUpdate(updateQuery);
				stmtU.close();
			}
			rs.close();
			stmt.close();
			con.close();
			System.out.println("** END updateDetector **");
		} catch (SQLException e) {
			LOG.error(e);
			System.out.println(e);
		} catch (Exception e) {
			LOG.error(e);
			System.out.println(e);
		}
	}

	private static HashMap<String, String> getCountries() {
		HashMap<String, String> countries = new HashMap<String, String>();
		try {
			String filePath = "C:/Users/bodin/Documents/ISPyB/statMax/countries.csv";
			BufferedReader inFile = null;
			inFile = new BufferedReader(new FileReader(filePath));
			String s = new String();
			while ((s = inFile.readLine()) != null) {
				String line = s;
				String[] values = line.split(";");
				countries.put(values[0], values[1]);
			}
		} catch (IOException e) {
			LOG.error(e);
			System.out.println(e);
		} catch (Exception e) {
			LOG.error(e);
			System.out.println(e);
		}
		return countries;
	}

	/**
	 * retrieves dataCollection Information from dataCollectionIds read from a txt file create a csv file with localContact name,
	 * country of main proposer, hour of day, day of week
	 */
	public static void getDataCollectionInformation() {
		System.out.println("********* getDataCollectionInformation *************");
		String csvSeparator = ";";
		try {
			Connection con = getConnection();
			Statement stmt = con.createStatement();
			String result = "";
			// getCountries
			HashMap<String, String> countries = getCountries();
			// read the txt file and get the dataCollectionId
			String filePath = "C:/Users/bodin/Documents/ISPyB/statMax/visitor_data_stats.txt";
			BufferedReader inFile = null;
			inFile = new BufferedReader(new FileReader(filePath));
			String s = new String();
			while ((s = inFile.readLine()) != null) {
				String line = s;
				String[] values = line.split("\t");
				int n = values.length;
				Integer dataCollectionId = null;
				try {
					dataCollectionId = Integer.parseInt(values[0]);
				} catch (NumberFormatException ef) {
				}
				if (dataCollectionId != null) {
					String query = "SELECT  s.beamLineOperator, l.country,  HOUR(c.startTime) as hour, DAYNAME(c.startTime) as dayname "
							+ "FROM BLSession s, DataCollectionGroup g, DataCollection c, Proposal pro, Person p, Laboratory l "
							+ "WHERE s.sessionId = g.sessionId "
							+ "AND g.dataCollectionGroupId = c.dataCollectionGroupId "
							+ "AND s.proposalId = pro.proposalId "
							+ "AND pro.personId = p.personId "
							+ "AND p.laboratoryId = l.laboratoryId " + "AND c.dataCollectionId = " + dataCollectionId;
					ResultSet rs = stmt.executeQuery(query);
					String localContact = "";
					String country = "";
					String hour = "";
					String dayName = "";
					while (rs.next()) {
						localContact = rs.getString("beamLineOperator");
						country = rs.getString("country");
						hour = rs.getString("hour");
						dayName = rs.getString("dayname");
					}
					if (localContact == null || localContact.equals("NULL")) {
						localContact = "";
					}
					if (country == null || country.equals("NULL")) {
						country = "";
					}
					if (hour == null || hour.equals("NULL")) {
						hour = "";
					}
					if (dayName == null || dayName.equals("NULL")) {
						dayName = "";
					}
					String c = countries.get(country);
					if (c != null) {
						country = c;
					}
					rs.close();
					result += dataCollectionId + csvSeparator + localContact + csvSeparator + country + csvSeparator + hour
							+ csvSeparator + dayName + csvSeparator;
					// old values
					for (int i = 1; i < n; i++) {
						result += values[i] + csvSeparator;
					}
					result += "\n";
				}
			}
			inFile.close();
			stmt.close();
			con.close();
			// export the result in a file
			String resultFile = "C:/ispyb_visitor_data_stats.csv";
			PrintWriter outWriter = new PrintWriter(new FileWriter(new File(resultFile)));
			outWriter.print(result);
			outWriter.flush();
			outWriter.close();
			System.out.println("** END getDataCollectionInformation **");
		} catch (SQLException e) {
			LOG.error(e);
			System.out.println(e);
		} catch (IOException e) {
			LOG.error(e);
			System.out.println(e);
		} catch (Exception e) {
			LOG.error(e);
			System.out.println(e);
		}
	}

	/**
	 * retrieves dataCollection Information from dataCollectionIds .dataCOllectionIds are retrieved from search criteria /
	 * AutoProcessing create a csv file with localContact name, country of main proposer, hour of day, day of week
	 */
	public static void getDataCollectionInformation2() {
		System.out.println("********* getDataCollectionInformation2 *************");
		long startTime = System.currentTimeMillis();
		String csvSeparator = ",";
		try {
			Connection con = getConnection();
			Statement stmt = con.createStatement();
			Statement stmt3 = con.createStatement();
			String result = "";

			result = "dataCollectionId" + csvSeparator + "Local Contact" + csvSeparator + "BeamLineName" + csvSeparator + "Country"
					+ csvSeparator + "Hour" + csvSeparator + "Day" + csvSeparator;
			result += "NumberOfImages" + csvSeparator + "Wavelength" + csvSeparator + "Resolution" + csvSeparator + "Transmission"
					+ csvSeparator + "Exposure Time" + csvSeparator + "Axis range" + csvSeparator;
			result += "meanIOverSigI_overall" + csvSeparator + "completeness_overall" + csvSeparator + "multiplicity_overall"
					+ csvSeparator + "rMerge_overall" + csvSeparator + "resolutionLimitLow_overall" + csvSeparator
					+ "resolutionLimitHigh_overall" + csvSeparator;
			result += "meanIOverSigI_outer" + csvSeparator + "completeness_outer" + csvSeparator + "multiplicity_outer" + csvSeparator
					+ "rMerge_outer" + csvSeparator + "resolutionLimitLow_outer" + csvSeparator + "resolutionLimitHigh_outer"
					+ csvSeparator;
			result += "filePath" + csvSeparator;
			result += "\n";

			// getCountries
			HashMap<String, String> countries = getCountries();
			// get the dataCollectionIds
			String queryCollect = "SELECT DISTINCT c.dataCollectionId, appa.filePath, apss.meanIOverSigI, apss.completeness, apss.multiplicity, apss.rMerge, apss.resolutionLimitLow, apss.resolutionLimitHigh "
					+ "FROM DataCollection c, AutoProcScalingStatistics apss, AutoProcProgramAttachment appa, AutoProcProgram app, AutoProcScaling aps, AutoProcScaling_has_Int apshi, AutoProcIntegration api  "
					+ "WHERE c.dataCollectionId = api.dataCollectionId AND "
					+ "api.autoProcProgramId = app.autoProcProgramId AND "
					+ "app.autoProcProgramId = appa.autoProcProgramId AND "
					+ "appa.filePath like '%pointless_%' AND "
					+ "apshi.autoProcIntegrationId = api.autoProcIntegrationId AND "
					+ "apshi.autoProcScalingId = aps.autoProcScalingId AND "
					+ "aps.autoProcScalingId = apss.autoProcScalingId AND "
					+ "apss.scalingStatisticsType = 'overall' AND "
					+ "c.startTime  >= '2011-01-01' AND "
					+ "c.endTime <= '2013-03-19' ;";

			Statement stmt2 = con.createStatement();
			ResultSet rs2 = stmt2.executeQuery(queryCollect);
			Integer dataCollectionId = null;
			String filePath = null;
			Float meanIOverSigI = null;
			Float completeness = null;
			Float multiplicity = null;
			Float rMerge = null;
			Float resolutionLimitLow = null;
			Float resolutionLimitHigh = null;
			while (rs2.next()) {
				dataCollectionId = rs2.getInt("dataCollectionId");
				filePath = rs2.getString("filePath");
				meanIOverSigI = rs2.getFloat("meanIOverSigI");
				completeness = rs2.getFloat("completeness");
				multiplicity = rs2.getFloat("multiplicity");
				rMerge = rs2.getFloat("rMerge");
				resolutionLimitLow = rs2.getFloat("resolutionLimitLow");
				resolutionLimitHigh = rs2.getFloat("resolutionLimitHigh");

				filePath = getStringvalue(filePath);
				String meanIOverSigIOverall = getFormat(meanIOverSigI);
				String completenessOverall = getFormat(completeness);
				String multiplicityOverall = getFormat(multiplicity);
				String rMergeOverall = getFormat(rMerge);
				String resolutionLimitLowOverall = getFormat(resolutionLimitLow);
				String resolutionLimitHighOverall = getFormat(resolutionLimitHigh);

				if (dataCollectionId != null) {

					// get the corresponding outer
					String queryCollectOuter = "SELECT DISTINCT c.dataCollectionId, appa.filePath, apss.meanIOverSigI, apss.completeness, apss.multiplicity, apss.rMerge, apss.resolutionLimitLow, apss.resolutionLimitHigh "
							+ "FROM DataCollection c, AutoProcScalingStatistics apss, AutoProcProgramAttachment appa, AutoProcProgram app, AutoProcScaling aps, AutoProcScaling_has_Int apshi, AutoProcIntegration api  "
							+ "WHERE c.dataCollectionId = api.dataCollectionId AND "
							+ "api.autoProcProgramId = app.autoProcProgramId AND "
							+ "app.autoProcProgramId = appa.autoProcProgramId AND "
							+ "appa.filePath like '%pointless_%' AND "
							+ "apshi.autoProcIntegrationId = api.autoProcIntegrationId AND "
							+ "apshi.autoProcScalingId = aps.autoProcScalingId AND "
							+ "aps.autoProcScalingId = apss.autoProcScalingId AND "
							+ "apss.scalingStatisticsType = 'outerShell' AND "
							+ "c.dataCollectionId = " + dataCollectionId + " ;";

					ResultSet rs3 = stmt3.executeQuery(queryCollectOuter);
					Float meanIOverSigI2 = null;
					Float completeness2 = null;
					Float multiplicity2 = null;
					Float rMerge2 = null;
					Float resolutionLimitLow2 = null;
					Float resolutionLimitHigh2 = null;
					while (rs3.next()) {
						meanIOverSigI2 = rs3.getFloat("meanIOverSigI");
						completeness2 = rs3.getFloat("completeness");
						multiplicity2 = rs3.getFloat("multiplicity");
						rMerge2 = rs3.getFloat("rMerge");
						resolutionLimitLow2 = rs3.getFloat("resolutionLimitLow");
						resolutionLimitHigh2 = rs3.getFloat("resolutionLimitHigh");
					}
					rs3.close();

					String meanIOverSigIOuter = getFormat(meanIOverSigI2);
					String completenessOuter = getFormat(completeness2);
					String multiplicityOuter = getFormat(multiplicity2);
					String rMergeOuter = getFormat(rMerge2);
					String resolutionLimitLowOuter = getFormat(resolutionLimitLow2);
					String resolutionLimitHighOuter = getFormat(resolutionLimitHigh2);

					String query = "SELECT  s.beamLineOperator, s.beamLineName, l.country,  HOUR(c.startTime) as hour, DAYNAME(c.startTime) as dayname, "
							+ "c.numberOfImages, c.wavelength, c.resolution, c.transmission, c.exposureTime, c.axisRange "
							+ "FROM BLSession s, DataCollectionGroup g, DataCollection c, Proposal pro, Person p, Laboratory l "
							+ "WHERE s.sessionId = g.sessionId "
							+ "AND g.dataCollectionGroupId = c.dataCollectionGroupId "
							+ "AND s.proposalId = pro.proposalId "
							+ "AND pro.personId = p.personId "
							+ "AND p.laboratoryId = l.laboratoryId " + "AND c.dataCollectionId = " + dataCollectionId;
					ResultSet rs = stmt.executeQuery(query);
					String localContact = "";
					String beamlineName = "";
					String country = "";
					String hour = "";
					String dayName = "";
					Integer numberOfImages = null;
					Float wavelength = null;
					Float resolution = null;
					Float transmission = null;
					Float exposureTime = null;
					Float axisRange = null;
					while (rs.next()) {
						localContact = rs.getString("beamLineOperator");
						beamlineName = rs.getString("beamLineName");
						country = rs.getString("country");
						hour = rs.getString("hour");
						dayName = rs.getString("dayname");
						numberOfImages = rs.getInt("numberOfImages");
						wavelength = rs.getFloat("wavelength");
						resolution = rs.getFloat("resolution");
						transmission = rs.getFloat("transmission");
						exposureTime = rs.getFloat("exposureTime");
						axisRange = rs.getFloat("axisRange");
					}
					localContact = getStringvalue(localContact);
					beamlineName = getStringvalue(beamlineName);
					country = getStringvalue(country);
					hour = getStringvalue(hour);
					dayName = getStringvalue(dayName);
					String c = countries.get(country);
					if (c != null) {
						country = c;
					}
					String numberOfImagesS = "";
					if (numberOfImages != null)
						numberOfImagesS = Integer.toString(numberOfImages);
					String wavelengthS = getFormat(wavelength);
					String resolutionS = getFormat(resolution);
					String transmissionS = getFormat(transmission);
					String exposureTimeS = getFormat(exposureTime);
					String axisRangeS = getFormat(axisRange);
					rs.close();
					result += dataCollectionId + csvSeparator + localContact + csvSeparator + beamlineName + csvSeparator + country
							+ csvSeparator + hour + csvSeparator + dayName + csvSeparator;
					result += numberOfImagesS + csvSeparator + wavelengthS + csvSeparator + resolutionS + csvSeparator + transmissionS
							+ csvSeparator + exposureTimeS + csvSeparator + axisRangeS + csvSeparator;
					result += meanIOverSigIOverall + csvSeparator + completenessOverall + csvSeparator + multiplicityOverall
							+ csvSeparator + rMergeOverall + csvSeparator + resolutionLimitLowOverall + csvSeparator
							+ resolutionLimitHighOverall + csvSeparator;
					result += meanIOverSigIOuter + csvSeparator + completenessOuter + csvSeparator + multiplicityOuter + csvSeparator
							+ rMergeOuter + csvSeparator + resolutionLimitLowOuter + csvSeparator + resolutionLimitHighOuter
							+ csvSeparator;
					result += filePath + csvSeparator;
					result += "\n";
				}
			}
			rs2.close();
			stmt2.close();
			stmt3.close();
			stmt.close();
			con.close();
			// export the result in a file
			String resultFile = "C:/ispyb_visitor_data_stats.csv";
			PrintWriter outWriter = new PrintWriter(new FileWriter(new File(resultFile)));
			outWriter.print(result);
			outWriter.flush();
			outWriter.close();
		} catch (SQLException e) {
			LOG.error(e);
			System.out.println(e);
		} catch (IOException e) {
			LOG.error(e);
			System.out.println(e);
		} catch (Exception e) {
			LOG.error(e);
			System.out.println(e);
		}
	}

	private static String getStringvalue(String s) {
		if (s == null || s.equals("NULL"))
			return "";
		return s;
	}

	private static String getFormat(Float f) {
		if (f == null)
			return "";
		return Float.toString(f);
	}

	public static void createFakeMeshData() {
		System.out.println("********* createFakeMeshData *************");
		String query = "SELECT c.dataCollectionId, g.dataCollectionGroupId, g.comments "
				+ "FROM DataCollection c, DataCollectionGroup g " + "WHERE c.dataCollectionGroupId = g.dataCollectionGroupId AND "
				+ "g.sessionId = 34019 AND " + "g.experimentType = 'Mesh' ; ";

		try {
			Connection con = getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				Integer dataCollectionId = rs.getInt("dataCollectionId");
				Integer dataCollectionGroupId = rs.getInt("dataCollectionGroupId");
				String comments = rs.getString("comments");

				Double[] pos = getPositions(comments);
				Double posY = pos[0];
				Double posZ = pos[1];
				// position
				//String queryInsert = "INSERT INTO MotorPosition (phiZ, phiY) VALUES (" + posZ + ", " + posY + ") ;";
				//Statement stmtW = con.createStatement();
				//stmtW.executeUpdate(queryInsert);
				//Integer positionId = null;
				//ResultSet rs2 = stmtW.getGeneratedKeys();
				//if (rs2.next()) {
				//	positionId = rs2.getInt(1);
				//}
				//rs2.close();
				//stmtW.close();

				String newCom = "old dcgId = " + dataCollectionGroupId;
				String queryUpdate = "UPDATE DataCollection SET dataCollectionGroupId = 1098614, " + "comments = '" + newCom
						 + "WHERE dataCollectionId = " + dataCollectionId + " ; ";
				Statement stmtU = con.createStatement();
				stmtU.executeUpdate(queryUpdate);
				stmtU.close();

			}
			rs.close();
			stmt.close();
			con.close();
			System.out.println("** END createFakeMeshData **");
		} catch (SQLException e) {
			LOG.error(e);
			System.out.println(e);
		} catch (Exception e) {
			LOG.error(e);
			System.out.println(e);
		}
	}

	public static void getDataCollectionStatistics() {
		System.out.println("********* getDataCollectionStatistics *************");
		String csvSeparator = ";";
		try {
			Connection con = getConnection();
			Statement stmt = con.createStatement();
			String result = "";
			String[] beamlines = { "ID14-1", "ID14-2", "ID14-3", "ID14-4", "ID29", "ID23-1", "ID23-2" };
			String[] years = { "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013" };
			int nbYears = years.length;
			int nbBL = beamlines.length;
			// first line : years
			result += csvSeparator;
			for (int y = 0; y < nbYears; y++) {
				result += years[y] + csvSeparator + csvSeparator;
			}
			result += "\n";
			// second line: titles
			result += csvSeparator;
			for (int y = 0; y < nbYears; y++) {
				result += "Sample Evaluations" + csvSeparator + "Data Sets" + csvSeparator;
			}
			result += "\n";
			for (int b = 0; b < nbBL; b++) {
				result += beamlines[b] + csvSeparator;
				for (int y = 0; y < nbYears; y++) {
					String query = "SELECT SUM(if(c.numberOfImages<5,1,0)) as screening, SUM(if(c.numberOfImages>30,1,0)) as datacollection "
							+ "FROM DataCollection c, DataCollectionGroup g, BLSession s "
							+ "WHERE c.dataCollectionGroupId = g.dataCollectionGroupId and "
							+ "      g.sessionId = s.sessionId and "
							+ "    YEAR(c.startTime) = '" + years[y] + "' AND " + "     s.beamlineName like '%" + beamlines[b] + "%'";
					ResultSet rs = stmt.executeQuery(query);
					while (rs.next()) {
						Integer screening = rs.getInt("screening");
						Integer datacollection = rs.getInt("datacollection");
						result += screening + csvSeparator + datacollection + csvSeparator;
					}
				}
				result += "\n";
			}
			// export the result in a file
			String resultFile = "C:/ispyb_stats.csv";
			PrintWriter outWriter = new PrintWriter(new FileWriter(new File(resultFile)));
			outWriter.print(result);
			outWriter.flush();
			outWriter.close();
			System.out.println("** END getDataCollectionStatistics **");
		} catch (SQLException e) {
			LOG.error(e);
			System.out.println(e);
		} catch (IOException e) {
			LOG.error(e);
			System.out.println(e);
		} catch (Exception e) {
			LOG.error(e);
			System.out.println(e);
		}
	}

	public static void getWorkflowStatistics() {
		System.out.println("********* getWorkflowStatistics *************");
		String csvSeparator = ";";
		try {
			Connection con = getConnection();
			Statement stmt = con.createStatement();
			String result = "";
			String[] beamlines = { "BM14U", "ID14-4", "ID29", "ID23-1", "ID23-2" };
			String[] years = { "2014" };
			String[] workflowTypes = { "Undefined", "EnhancedCharacterisation", "LineScan", "MeshScan", "Dehydration",
					"KappaReorientation", "BurnStrategy", "XrayCentering", "DiffractionTomography", "TroubleShooting",
					"VisualReorientation", "HelicalCharacterisation", "GroupedProcessing", "MXPressE", "MXPressO", "MXPressL",
					"MXScore" };
			String[] status = { "Success", "Started", "Failure", "Failure with no diffraction signal detected" };
			int nbYears = years.length;
			int nbBL = beamlines.length;
			int nbW = workflowTypes.length;
			int nbSt = status.length;
			// first line : years
			result += csvSeparator;
			for (int y = 0; y < nbYears; y++) {
				result += years[y];
				for (int w = 0; w < nbW; w++) {
					for (int s = 0; s < nbSt; s++) {
						result += csvSeparator;
					}
				}
			}
			result += "\n";
			// second line: wf titles
			result += csvSeparator;
			for (int y = 0; y < nbYears; y++) {
				for (int w = 0; w < nbW; w++) {
					result += workflowTypes[w];
					for (int s = 0; s < nbSt; s++) {
						result += csvSeparator;
					}
				}
			}
			result += "\n";
			// third line: status titles
			result += csvSeparator;
			for (int y = 0; y < nbYears; y++) {
				for (int w = 0; w < nbW; w++) {
					for (int s = 0; s < nbSt; s++) {
						result += status[s] + csvSeparator;
					}
				}
			}
			result += "\n";
			for (int b = 0; b < nbBL; b++) {
				result += beamlines[b] + csvSeparator;
				for (int y = 0; y < nbYears; y++) {
					for (int w = 0; w < nbW; w++) {
						for (int s = 0; s < nbSt - 1; s++) {
							String query = "SELECT count(w.workflowId) as nbW "
									+ "FROM Workflow w, DataCollectionGroup g, BLSession s, Proposal p "
									+ "WHERE w.workflowId = g.workflowId  and " + "      g.sessionId = s.sessionId and "
									+ "      s.proposalId = p.proposalId and " + "      s.proposalId = p.proposalId and "
									+ "      p.proposalCode != 'opid' and " + "      p.proposalId != 1170 and "
									+ "     YEAR(g.startTime) = '" + years[y] + "' AND " + "     s.beamlineName like '%"
									+ beamlines[b] + "%' AND " + "     w.workflowType = '" + workflowTypes[w] + "' AND "
									+ "     w.status = '" + status[s] + "' ";
							ResultSet rs = stmt.executeQuery(query);
							while (rs.next()) {
								Integer nbWk = rs.getInt("nbW");
								result += nbWk + csvSeparator;
							}
							rs.close();
						}
						// failure with no diffraction signal detected
						int nbFailWithNoSig = 0;
						String query = "SELECT w.workflowId, w.logFilePath  "
								+ "FROM Workflow w, DataCollectionGroup g, BLSession s, Proposal p "
								+ "WHERE w.workflowId = g.workflowId  and " + "      g.sessionId = s.sessionId and "
								+ "      s.proposalId = p.proposalId and " + "      s.proposalId = p.proposalId and "
								+ "      p.proposalCode != 'opid' and " + "      p.proposalId != 1170 and "
								+ "     YEAR(g.startTime) = '" + years[y] + "' AND " + "     s.beamlineName like '%" + beamlines[b]
								+ "%' AND " + "     w.workflowType = '" + workflowTypes[w] + "' AND " + "     w.status = 'Failure' ";
						ResultSet rs = stmt.executeQuery(query);
						while (rs.next()) {
							String logFilePath = rs.getString("w.logFilePath");
							boolean logFileExists = false;
							try {
								String targetPath = PathUtils.FitPathToOS(logFilePath);
								File targetFile = new File(targetPath);
								logFileExists = (targetFile.exists() && targetFile.isFile());
							} catch (Exception e) {
							}
							String logFileFullPath = PathUtils.FitPathToOS(logFilePath);
							String fullLogFileContent = "";
							if (logFileExists) {
								// fullLogFileContent = FileUtil.fileToString(logFileFullPath);
								BufferedReader inFile = null;
								fullLogFileContent = new String();// = null;

								try {

									// 1. Reading input by lines:
									inFile = new BufferedReader(new FileReader(logFileFullPath));
									String s = new String();
									while ((s = inFile.readLine()) != null)
										fullLogFileContent += s + "\n";
									inFile.close();

								} catch (Exception e) {
									e.printStackTrace();
								} finally {
									if (inFile != null) {
										try {
											inFile.close();
										} catch (IOException ioex) {
											// ignore
											fullLogFileContent = "nofile";
										}
									}

								}
							}
							if (fullLogFileContent.indexOf("No diffraction signal detected") != -1) {
								nbFailWithNoSig++;
							}
						}
						rs.close();
						result += nbFailWithNoSig + csvSeparator;
					}
				}
				result += "\n";
			}
			System.out.println(result);
			stmt.close();
			con.close();
			// export the result in a file
			String resultFile = "C:/ispyb_stats.csv";
			PrintWriter outWriter = new PrintWriter(new FileWriter(new File(resultFile)));
			outWriter.print(result);
			outWriter.flush();
			outWriter.close();
			System.out.println("** END getWorkflowStatistics **");
		} catch (SQLException e) {
			LOG.error(e);
			System.out.println(e);
		} catch (IOException e) {
			LOG.error(e);
			System.out.println(e);
		} catch (Exception e) {
			LOG.error(e);
			System.out.println(e);
		}
	}

	private static Double[] getPositions(String comments) {
		Double[] pos = new Double[2];
		if (comments == null)
			return pos;
		int id1 = comments.indexOf("phiz");
		int id2 = comments.indexOf("phiy");
		Double phiZ = null;
		Double phiY = null;
		phiZ = Double.parseDouble(comments.substring(id1 + 5, id2 - 2));
		phiY = Double.parseDouble(comments.substring(id2 + 4));

		System.out.println(phiY + " /  " + phiZ);
		pos[0] = phiY;
		pos[1] = phiZ;
		return pos;
	}

	public static void getDataCollectionStatistics2() {
		System.out.println("********* getDataCollectionStatistics2 *************");
		String csvSeparator = ";";
		try {
			Connection con = getConnection();
			Statement stmt = con.createStatement();
			String result = "";
			String[] beamlines = { "ID23-1", "ID23-2", "ID29", "BM14" };
			String[] years = { "2014" };
			String[] collectTypes = { "Nb OSC Group", "Nb OSC", "Nb Characterization Group", "Nb Characterization",
					"Nb Collect <= 4 images", "Nb Collect > 4 images" };
			String[] proposalType = { "FX", "IX", "Other" };
			int nbYears = years.length;
			int nbBL = beamlines.length;
			int nbCollectTypes = collectTypes.length;
			int nbProposalCode = proposalType.length;

			int nb = nbProposalCode * nbCollectTypes;
			// first line : years
			result += csvSeparator;
			for (int y = 0; y < nbYears; y++) {
				result += years[y];
				for (int s = 0; s < nb; s++) {
					result += csvSeparator;
				}
			}
			result += "\n";
			// second line: titles collect
			result += csvSeparator;
			for (int y = 0; y < nbYears; y++) {
				for (int c = 0; c < nbCollectTypes; c++) {
					result += collectTypes[c];
					for (int p = 0; p < nbProposalCode; p++) {
						result += csvSeparator;
					}
				}
			}
			result += "\n";
			// third line: proposal Code
			result += csvSeparator;
			for (int y = 0; y < nbYears; y++) {
				for (int c = 0; c < nbCollectTypes; c++) {
					for (int p = 0; p < nbProposalCode; p++) {
						result += proposalType[p] + csvSeparator;
					}
				}
			}
			result += "\n";
			// queries
			for (int b = 0; b < nbBL; b++) {
				result += beamlines[b] + csvSeparator;
				for (int y = 0; y < nbYears; y++) {
					// types of collect
					// osc group
					for (int p = 0; p < proposalType.length; p++) {
						String proposalCond = " p.proposalCode = '" + proposalType[p] + "' and ";
						if (p == proposalType.length - 1) {
							proposalCond = " p.proposalCode  NOT in ('IX', 'FX') and ";
						}
						String queryGroup1 = "SELECT  count(distinct g.dataCollectionGroupId) as osc "
								+ "FROM DataCollectionGroup g, BLSession s, Proposal p , DataCollection c  "
								+ "WHERE   c.dataCollectionGroupId = g.dataCollectionGroupId and "
								+ "      g.sessionId = s.sessionId and " + "      s.proposalId = p.proposalId and " + proposalCond
								+ "g.experimentType = 'OSC' and " + "    YEAR(g.startTime) = '" + years[y] + "' AND "
								+ "     s.beamlineName like '%" + beamlines[b] + "%'";
						// System.out.println("2: "+queryGroup1);
						ResultSet rs2 = stmt.executeQuery(queryGroup1);
						while (rs2.next()) {
							Integer oscGroup = rs2.getInt("osc");
							result += oscGroup + csvSeparator;

						}
						rs2.close();
					}
					// osc
					for (int p = 0; p < proposalType.length; p++) {
						String proposalCond = " p.proposalCode = '" + proposalType[p] + "' and ";
						if (p == proposalType.length - 1) {
							proposalCond = " p.proposalCode  NOT in ('IX', 'FX') and ";
						}
						String query1 = "SELECT  count(c.dataCollectionId) as osc "
								+ "FROM DataCollectionGroup g, BLSession s, Proposal p, DataCollection c  "
								+ "WHERE  c.dataCollectionGroupId = g.dataCollectionGroupId and "
								+ "      g.sessionId = s.sessionId and " + "      s.proposalId = p.proposalId and " + proposalCond
								+ "g.experimentType = 'OSC' and " + "    YEAR(g.startTime) = '" + years[y] + "' AND "
								+ "     s.beamlineName like '%" + beamlines[b] + "%'";
						// System.out.println("4: "+query1);
						ResultSet rs2 = stmt.executeQuery(query1);
						while (rs2.next()) {
							Integer osc = rs2.getInt("osc");
							result += osc + csvSeparator;
						}
						rs2.close();
					}
					// nb Charac group
					for (int p = 0; p < proposalType.length; p++) {
						String proposalCond = " p.proposalCode = '" + proposalType[p] + "' and ";
						if (p == proposalType.length - 1) {
							proposalCond = " p.proposalCode  NOT in ('IX', 'FX') and ";
						}
						String queryGroup2 = "SELECT  count(distinct g.dataCollectionGroupId) as charac "
								+ "FROM DataCollectionGroup g, BLSession s, Proposal p , DataCollection c  "
								+ "WHERE   c.dataCollectionGroupId = g.dataCollectionGroupId and "
								+ "      g.sessionId = s.sessionId and " + "      s.proposalId = p.proposalId and " + proposalCond
								+ "g.experimentType = 'Characterization' and " + "    YEAR(g.startTime) = '" + years[y] + "' AND "
								+ "     s.beamlineName like '%" + beamlines[b] + "%'";
						// System.out.println("3: "+queryGroup2);
						ResultSet rs2 = stmt.executeQuery(queryGroup2);
						while (rs2.next()) {
							Integer characGroup = rs2.getInt("charac");
							result += characGroup + csvSeparator;
						}
						rs2.close();
					}
					// nb Charac
					for (int p = 0; p < proposalType.length; p++) {
						String proposalCond = " p.proposalCode = '" + proposalType[p] + "' and ";
						if (p == proposalType.length - 1) {
							proposalCond = " p.proposalCode  NOT in ('IX', 'FX') and ";
						}
						String query2 = "SELECT  count(c.dataCollectionId) as charac "
								+ "FROM DataCollectionGroup g, BLSession s, Proposal p, DataCollection c  "
								+ "WHERE  c.dataCollectionGroupId = g.dataCollectionGroupId and "
								+ "      g.sessionId = s.sessionId and " + "      s.proposalId = p.proposalId and " + proposalCond
								+ "g.experimentType = 'Characterization' and " + "    YEAR(g.startTime) = '" + years[y] + "' AND "
								+ "     s.beamlineName like '%" + beamlines[b] + "%'";
						// System.out.println("5: "+query2);
						ResultSet rs2 = stmt.executeQuery(query2);
						while (rs2.next()) {
							Integer charac = rs2.getInt("charac");
							result += charac + csvSeparator;
						}
						rs2.close();
					}

					// nb screening
					for (int p = 0; p < proposalType.length; p++) {
						String proposalCond = " p.proposalCode = '" + proposalType[p] + "' and ";
						if (p == proposalType.length - 1) {
							proposalCond = " p.proposalCode  NOT in ('IX', 'FX') and ";
						}
						String query = "SELECT  SUM(if(c.numberOfImages<5,1,0)) as screening, SUM(if(c.numberOfImages>4,1,0)) as datacollection "
								+ "FROM DataCollection c, DataCollectionGroup g, BLSession s, Proposal p  "
								+ "WHERE c.dataCollectionGroupId = g.dataCollectionGroupId and "
								+ "      g.sessionId = s.sessionId and "
								+ "      s.proposalId = p.proposalId and "
								+ proposalCond
								+ "    YEAR(c.startTime) = '"
								+ years[y]
								+ "' AND "
								+ "     s.beamlineName like '%"
								+ beamlines[b]
								+ "%'";
						// System.out.println("1: "+query);
						ResultSet rs = stmt.executeQuery(query);
						while (rs.next()) {
							Integer screening = rs.getInt("screening");
							result += screening + csvSeparator;
						}
						rs.close();
					}
					// nb collect
					for (int p = 0; p < proposalType.length; p++) {
						String proposalCond = " p.proposalCode = '" + proposalType[p] + "' and ";
						if (p == proposalType.length - 1) {
							proposalCond = " p.proposalCode  NOT in ('IX', 'FX') and ";
						}
						String query = "SELECT  SUM(if(c.numberOfImages>4,1,0)) as datacollection "
								+ "FROM DataCollection c, DataCollectionGroup g, BLSession s, Proposal p  "
								+ "WHERE c.dataCollectionGroupId = g.dataCollectionGroupId and "
								+ "      g.sessionId = s.sessionId and " + "      s.proposalId = p.proposalId and " + proposalCond
								+ "    YEAR(c.startTime) = '" + years[y] + "' AND " + "     s.beamlineName like '%" + beamlines[b]
								+ "%'";
						// System.out.println("1: "+query);
						ResultSet rs = stmt.executeQuery(query);
						while (rs.next()) {
							Integer datacollection = rs.getInt("datacollection");
							result += datacollection + csvSeparator;
						}
						rs.close();
					}
				}
				result += "\n";
			}
			stmt.close();
			con.close();
			// export the result in a file
			String resultFile = "C:/ispyb_stats.csv";
			PrintWriter outWriter = new PrintWriter(new FileWriter(new File(resultFile)));
			outWriter.print(result);
			outWriter.flush();
			outWriter.close();
			System.out.println("** END getDataCollectionStatistics2 **");
		} catch (SQLException e) {
			LOG.error(e);
			e.printStackTrace();
			System.out.println(e);
		} catch (IOException e) {
			LOG.error(e);
			e.printStackTrace();
			System.out.println(e);
		} catch (Exception e) {
			LOG.error(e);
			e.printStackTrace();
			System.out.println(e);
		}
	}

	public static void getSessionStatistics() {
		System.out.println("********* getSessionStatistics *************");
		String csvSeparator = ";";
		try {
			Connection con = getConnection();
			Statement stmt = con.createStatement();
			String result = "";
			String[] beamlines = { "ID23-1", "ID23-2", "ID29", "BM14" };
			String[] years = { "2014" };
			String[] sessionTypes = { "Nb Session", "Nb Shifts" };
			String[] proposalType = { "FX", "IX", "Other" };
			int nbYears = years.length;
			int nbBL = beamlines.length;
			int nbSessionTypes = sessionTypes.length;
			int nbProposalCode = proposalType.length;

			int nb = nbProposalCode * nbSessionTypes;
			// first line : years
			result += csvSeparator;
			for (int y = 0; y < nbYears; y++) {
				result += years[y];
				for (int s = 0; s < nb; s++) {
					result += csvSeparator;
				}
			}
			result += "\n";
			// second line: titles collect
			result += csvSeparator;
			for (int y = 0; y < nbYears; y++) {
				for (int c = 0; c < nbSessionTypes; c++) {
					result += sessionTypes[c];
					for (int p = 0; p < nbProposalCode; p++) {
						result += csvSeparator;
					}
				}
			}
			result += "\n";
			// third line: proposal Code
			result += csvSeparator;
			for (int y = 0; y < nbYears; y++) {
				for (int c = 0; c < nbSessionTypes; c++) {
					for (int p = 0; p < nbProposalCode; p++) {
						result += proposalType[p] + csvSeparator;
					}
				}
			}
			result += "\n";
			// queries
			for (int b = 0; b < nbBL; b++) {
				result += beamlines[b] + csvSeparator;
				for (int y = 0; y < nbYears; y++) {
					// types of ses info
					// nb Session
					for (int p = 0; p < proposalType.length; p++) {
						String proposalCond = " p.proposalCode = '" + proposalType[p] + "' and ";
						if (p == proposalType.length - 1) {
							proposalCond = " p.proposalCode  NOT in ('IX', 'FX') and ";
						}
						String querySes1 = "SELECT  count(distinct s.sessionId) as ses " + "FROM BLSession s, Proposal p  "
								+ "WHERE    " + "      s.proposalId = p.proposalId and " + "s.usedFlag = 1 and " + proposalCond
								+ "    YEAR(s.startDate) = '" + years[y] + "' AND " + "     s.beamlineName like '%" + beamlines[b]
								+ "%'";
						// System.out.println("2: "+queryGroup1);
						ResultSet rs = stmt.executeQuery(querySes1);
						while (rs.next()) {
							Integer nbSes = rs.getInt("ses");
							result += nbSes + csvSeparator;

						}
						rs.close();
					}
					// nbShifts
					for (int p = 0; p < proposalType.length; p++) {
						String proposalCond = " p.proposalCode = '" + proposalType[p] + "' and ";
						if (p == proposalType.length - 1) {
							proposalCond = " p.proposalCode  NOT in ('IX', 'FX') and ";
						}
						String querySes2 = "SELECT  s.nbShifts as nbShifts " + "FROM BLSession s, Proposal p  " + "WHERE    "
								+ "      s.proposalId = p.proposalId and " + "s.usedFlag = 1 and " + proposalCond
								+ "    YEAR(s.startDate) = '" + years[y] + "' AND " + "     s.beamlineName like '%" + beamlines[b]
								+ "%'";
						// System.out.println("2: "+queryGroup1);
						ResultSet rs = stmt.executeQuery(querySes2);
						Integer nbShifts = 0;
						while (rs.next()) {
							Integer nbS = rs.getInt("nbShifts");
							nbShifts += nbS;

						}
						result += nbShifts + csvSeparator;
						rs.close();
					}

				}
				result += "\n";
			}
			stmt.close();
			con.close();
			// export the result in a file
			String resultFile = "C:/ispyb_stats.csv";
			PrintWriter outWriter = new PrintWriter(new FileWriter(new File(resultFile)));
			outWriter.print(result);
			outWriter.flush();
			outWriter.close();
			System.out.println("** END getSessionStatistics **");
		} catch (SQLException e) {
			LOG.error(e);
			e.printStackTrace();
			System.out.println(e);
		} catch (IOException e) {
			LOG.error(e);
			e.printStackTrace();
			System.out.println(e);
		} catch (Exception e) {
			LOG.error(e);
			e.printStackTrace();
			System.out.println(e);
		}
	}

	private static Connection getConnection() throws NamingException, SQLException {
		String connectionString = Constants.getProperty("ISPyB.dbJndiName.direct");

		Properties properties = new Properties();
		properties.put("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
		properties.put("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
		properties.put("java.naming.provider.url", "jnp://localhost:1099");
		InitialContext ic = new InitialContext(properties);
		DataSource ds = (DataSource) ic.lookup(connectionString);
		Connection conn = ds.getConnection();
		return conn;
	}

	public static void main(String args[]) {
		int n = JOptionPane.showConfirmDialog(null, "Are you sure to continue?", "Update of the database", JOptionPane.YES_NO_OPTION);
		if (n == JOptionPane.YES_OPTION) {
			System.out.println("** UPDATE OF THE DATABASE **");
			// setScreeningInputInScreening();
			// setRankingResolutionInScreeningOutput();
			// setDetectorInDataCollection();
			// copyScreeningStrategyInWedge();
			// switchSlot();
			// updateDetector();
			// getDataCollectionInformation();
			// getDataCollectionInformation2();
			// createFakeMeshData();
			// getDataCollectionStatistics();
			// getDataCollectionStatistics2();
			// getWorkflowStatistics();
			// getSessionStatistics();

		}
		System.out.println("quit the database update");
	}

	public static void getWorkflowStats() {
		System.out.println("********* getWorkflowStats *************");
		String csvSeparator = ";";

		try {
			Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
			Workflow3Service workflowService = (Workflow3Service) ejb3ServiceLocator.getLocalService(Workflow3Service.class);
			String result = "";
			String[] beamlines = { "BM14U", "ID29", "ID23-1", "ID23-2" };
			String[] years = { "2014" };
			String[] workflowTypes = { "Undefined", "EnhancedCharacterisation", "LineScan", "MeshScan", "Dehydration",
					"KappaReorientation", "BurnStrategy", "XrayCentering", "DiffractionTomography", "TroubleShooting",
					"VisualReorientation", "HelicalCharacterisation", "GroupedProcessing", "MXPressE", "MXPressO", "MXPressL",
					"MXScore" };
			String[] status = { "Success", "Started", "Failure", "Failure with no diffraction signal detected" };
			int nbYears = years.length;
			int nbBL = beamlines.length;
			int nbW = workflowTypes.length;
			int nbSt = status.length;
			// first line : years
			result += csvSeparator;
			for (int y = 0; y < nbYears; y++) {
				result += years[y];
				for (int w = 0; w < nbW; w++) {
					for (int s = 0; s < nbSt; s++) {
						result += csvSeparator;
					}
				}
			}
			result += "\n";
			// second line: wf titles
			result += csvSeparator;
			for (int y = 0; y < nbYears; y++) {
				for (int w = 0; w < nbW; w++) {
					result += workflowTypes[w];
					for (int s = 0; s < nbSt; s++) {
						result += csvSeparator;
					}
				}
			}
			result += "\n";
			// third line: status titles
			result += csvSeparator;
			for (int y = 0; y < nbYears; y++) {
				for (int w = 0; w < nbW; w++) {
					for (int s = 0; s < nbSt; s++) {
						result += status[s] + csvSeparator;
					}
				}
			}
			result += "\n";
			for (int b = 0; b < nbBL; b++) {
				result += beamlines[b] + csvSeparator;
				for (int y = 0; y < nbYears; y++) {
					for (int w = 0; w < nbW; w++) {
						for (int s = 0; s < nbSt - 1; s++) {
							Integer nbWk = workflowService.countWF(years[y], beamlines[b], workflowTypes[w], status[s]);
							result += nbWk + csvSeparator;

						}
						// failure with no diffraction signal detected
						int nbFailWithNoSig = 0;
						List results = workflowService.getWorkflowResult(years[y], beamlines[b], workflowTypes[w]);
						for (Iterator iterator = results.iterator(); iterator.hasNext();) {
							Object object = iterator.next();
							// while ( rs.next() ) {
							String logFilePath = (String) object;
							boolean logFileExists = false;
							try {
								String targetPath = PathUtils.FitPathToOS(logFilePath);
								File targetFile = new File(targetPath);
								logFileExists = (targetFile.exists() && targetFile.isFile());
							} catch (Exception e) {
							}
							String logFileFullPath = PathUtils.FitPathToOS(logFilePath);
							String fullLogFileContent = "";
							if (logFileExists) {
								// fullLogFileContent = FileUtil.fileToString(logFileFullPath);
								BufferedReader inFile = null;
								fullLogFileContent = new String();// = null;

								try {

									// 1. Reading input by lines:
									inFile = new BufferedReader(new FileReader(logFileFullPath));
									String s = new String();
									while ((s = inFile.readLine()) != null)
										fullLogFileContent += s + "\n";
									inFile.close();

								} catch (Exception e) {
									e.printStackTrace();
								} finally {
									if (inFile != null) {
										try {
											inFile.close();
										} catch (IOException ioex) {
											// ignore
											fullLogFileContent = "nofile";
										}
									}

								}
							}
							if (fullLogFileContent.indexOf("No diffraction signal detected") != -1) {
								nbFailWithNoSig++;
							}
						}
						result += nbFailWithNoSig + csvSeparator;
					}
				}
				result += "\n";
			}
			System.out.println(result);
			// export the result in a file
			String resultFile = "C:/ispyb_stats.csv";
			PrintWriter outWriter = new PrintWriter(new FileWriter(new File(resultFile)));
			outWriter.print(result);
			outWriter.flush();
			outWriter.close();
			System.out.println("** END getWorkflowStats **");
		} catch (SQLException e) {
			LOG.error(e);
			System.out.println(e);
		} catch (IOException e) {
			LOG.error(e);
			System.out.println(e);
		} catch (Exception e) {
			LOG.error(e);
			System.out.println(e);
		}
	}

}
