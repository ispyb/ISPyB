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
 * SampleRankinghart.java
 * @author patrice.brenchereau@esrf.fr
 * March 09, 2009
 */

package ispyb.client.mx.ranking;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class SampleRankingChart {

	private final static Logger LOG = Logger.getLogger(SampleRankingChart.class);

	/**
	 * Build radar/polar chart
	 * 
	 * @param request
	 * @param sampleRankingValues
	 * @param tmpDir
	 * @return
	 * @throws Exception
	 */
	public static String buildComparisonChart(HttpServletRequest request, List<SampleRankingVO> sampleRankingValues,
			SampleRankingCriteriaVO criteriaVO, String tmpDir) throws Exception {

		// Get best values
		Collections.sort(sampleRankingValues, SampleRankingComparator.ORDER_RANK_TheoreticalResolution);
		double rankingResolutionBest = 0;
		if (sampleRankingValues.get(0).getTheoreticalResolutionValue() != null)
			rankingResolutionBest = sampleRankingValues.get(0).getTheoreticalResolutionValue();

		Collections.sort(sampleRankingValues, SampleRankingComparator.ORDER_RANK_TotalExposureTime);
		double exposureTimeBest = 0;
		if (sampleRankingValues.get(0).getTotalExposureTimeValue() != null)
			exposureTimeBest = sampleRankingValues.get(0).getTotalExposureTimeValue();

		Collections.sort(sampleRankingValues, SampleRankingComparator.ORDER_RANK_Mosaicity);
		double mosaicityBest = 0;
		if (sampleRankingValues.get(0).getMosaicityValue() != null)
			mosaicityBest = sampleRankingValues.get(0).getMosaicityValue();

		Collections.sort(sampleRankingValues, SampleRankingComparator.ORDER_RANK_NumberOfSpotsIndexed);
		int numberOfSpotsBest = 0;
		if (sampleRankingValues.get(0).getNumberOfSpotsIndexedValue() != null)
			numberOfSpotsBest = sampleRankingValues.get(0).getNumberOfSpotsIndexedValue();

		Collections.sort(sampleRankingValues, SampleRankingComparator.ORDER_RANK_NumberOfImages);
		int numberOfImagesBest = 0;
		if (sampleRankingValues.get(0).getNumberOfImagesValue() != null)
			numberOfImagesBest = sampleRankingValues.get(0).getNumberOfImagesValue();

		Collections.sort(sampleRankingValues, SampleRankingComparator.ORDER_RANK_Total);

		// Calculate percentage
		String data = "";
		for (int i = 0; i < sampleRankingValues.size(); i++) {
			SampleRankingVO sampleRankingVO = sampleRankingValues.get(i);

			// Normalized chart
			int rankingResolutionPercent = 0;
			int exposureTimePercent = 0;
			int mosaicityPercent = 0;
			int numberOfSpotsPercent = 0;
			int numberOfImagesPercent = 0;
			if (sampleRankingVO.getTheoreticalResolutionValue() != null
					&& sampleRankingVO.getTheoreticalResolutionValue() != 0)
				rankingResolutionPercent = (int) Math.round((100 * rankingResolutionBest)
						/ sampleRankingVO.getTheoreticalResolutionValue());
			if (sampleRankingVO.getTotalExposureTimeValue() != null && sampleRankingVO.getTotalExposureTimeValue() != 0)
				exposureTimePercent = (int) Math.round((100 * exposureTimeBest)
						/ sampleRankingVO.getTotalExposureTimeValue());
			if (sampleRankingVO.getMosaicityValue() != null && sampleRankingVO.getMosaicityValue() != 0)
				mosaicityPercent = (int) Math.round((100 * mosaicityBest) / sampleRankingVO.getMosaicityValue());
			if (sampleRankingVO.getNumberOfSpotsIndexedValue() != null
					&& sampleRankingVO.getNumberOfSpotsIndexedValue() != 0)
				numberOfSpotsPercent = Math.round((100 * sampleRankingVO.getNumberOfSpotsIndexedValue())
						/ numberOfSpotsBest);
			if (sampleRankingVO.getNumberOfImagesValue() != null && sampleRankingVO.getNumberOfImagesValue() != 0)
				numberOfImagesPercent = Math.round((100 * numberOfImagesBest)
						/ sampleRankingVO.getNumberOfImagesValue());

			// // Relative chart
			// long rankingResolutionPercent = Math.round(sampleRankingVO.getTheoreticalResolutionScore());
			// long exposureTimePercent = Math.round(sampleRankingVO.getTotalExposureTimeScore());
			// long mosaicityPercent = Math.round(sampleRankingVO.getMosaicityScore()*scaleFactor);
			// long numberOfSpotsPercent = Math.round(sampleRankingVO.getNumberOfSpotsIndexedScore());
			// long numberOfImagesPercent = Math.round(sampleRankingVO.getNumberOfImagesScore());

			// Legend
			String legend = "";
			legend += "#" + sampleRankingVO.getTotalRank() + "-" + Math.round(sampleRankingVO.getTotalScore()) + "%";
			legend += " - " + sampleRankingVO.getImagePrefix();
			legend += " - #" + sampleRankingVO.getDataCollectionNumber();
			legend += " - " + sampleRankingVO.getSpaceGroup();

			// Data
			data += "\t<row>\n";
			data += "\t\t<string>" + legend + "</string>\n";
			data += "\t\t<number  bevel='data'>" + rankingResolutionPercent + "</number>\n";
			data += "\t\t<number  bevel='data'>" + exposureTimePercent + "</number>\n";
			data += "\t\t<number  bevel='data'>" + mosaicityPercent + "</number>\n";
			data += "\t\t<number  bevel='data'>" + numberOfSpotsPercent + "</number>\n";
			data += "\t\t<number  bevel='data'>" + numberOfImagesPercent + "</number>\n";
			data += "\t</row>\n";

		}

		// Start chart
		String xmlData = "";
		xmlData += "<chart>\n";

		// Chart data
		xmlData += "\t<chart_data>\n";
		xmlData += "\t<row>\n";
		xmlData += "\t\t<null/>\n";
		xmlData += "\t\t<string>Ranking Resolution (x" + criteriaVO.getWeightRankingResolution() + ")</string>\n";
		xmlData += "\t\t<string>Exposure Time (x" + criteriaVO.getWeightExposureTime() + ")</string>\n";
		xmlData += "\t\t<string>Mosaicity (x" + criteriaVO.getWeightMosaicity() + ")</string>\n";
		xmlData += "\t\t<string>Number of spots (x" + criteriaVO.getWeightNumberOfSpots() + ")</string>\n";
		xmlData += "\t\t<string>Number of images (x" + criteriaVO.getWeightNumberOfImages() + ")</string>\n";
		xmlData += "\t</row>\n";
		xmlData += data;
		xmlData += "\t</chart_data>\n";

		// Chart type
		xmlData += "\t<chart_type>polar</chart_type>\n";

		// Chart values
		xmlData += "\t<axis_category shadow='shadow2' size='12' color='000000' alpha='75' orientation='circular' />\n";
		xmlData += "\t<axis_ticks value_ticks='' category_ticks='' />\n";
		// TODO remove scale
		xmlData += "\t<axis_value alpha='0' min='0' />\n";

		xmlData += "\t<chart_border bottom_thickness='0' left_thickness='0' />\n";
		xmlData += "\t<chart_grid_h alpha='20' color='000000' thickness='1' type='solid' />\n";
		xmlData += "\t<chart_grid_v alpha='20' color='000000' thickness='2' type='dotted' />\n";
		xmlData += "\t<chart_pref line_thickness='2' point_size='0'  grid='circular' />\n";
		xmlData += "\t<chart_pref type='line' line_thickness='2' point_shape='circle' point_size='4' fill_shape='true' grid='circular' />\n";
		xmlData += "\t<chart_rect bevel='bevel1' x='75' y='50' width='300' height='300' positive_color='000000' positive_alpha='10' />\n";

		xmlData += "\t<filter>\n";
		xmlData += "\t\t<shadow id='shadow1' distance='2' angle='45' color='0' alpha='75' blurX='10' blurY='10' />\n";
		xmlData += "\t\t<shadow id='shadow2' distance='1' angle='45' color='0' alpha='50' blurX='3' blurY='3' />\n";
		xmlData += "\t\t<bevel id='bevel1' angle='45' blurX='15' blurY='15' distance='5' highlightAlpha='25' highlightColor='ffffff' shadowAlpha='50' type='outer' />\n";
		xmlData += "\t</filter>\n";

		xmlData += "\t<legend shadow='low' x='450' y='25' width='20' height='70' margin='5' fill_color='0' fill_alpha='5' line_alpha='0' line_thickness='0' bullet='line' size='12' color='000000' alpha='75' margin='10' />\n";

		xmlData += "\t<series_color>\n";
		xmlData += "\t\t<color>ff4400</color>\n";
		xmlData += "\t\t<color>008800</color>\n";
		xmlData += "\t\t<color>0000FF</color>\n";
		xmlData += "\t\t<color>222222</color>\n";
		xmlData += "\t\t<color>FFFF00</color>\n";
		xmlData += "\t</series_color>\n";

		// End chart
		xmlData += "</chart>\n";

		// Write result to temporary file (supposed to be unique)
		Random random = new Random();
		int v = random.nextInt(1000);
		String sv = "000" + Integer.toString(v);
		sv = sv.substring(sv.length() - 3);
		String fileName = "Chart_" + System.currentTimeMillis() + "_" + sv + ".xml";
		String relativeFilePath = "/" + tmpDir + "/" + fileName;
		String filePath = request.getRealPath(relativeFilePath);

		try {
			File tempFile = new File(filePath);
			Writer output = new BufferedWriter(new FileWriter(tempFile));
			output.write(xmlData);
			output.close();
		} catch (IOException ex) {
			LOG.error("ERROR Creating temporary file '" + filePath + "': " + ex.getMessage());
			ex.printStackTrace();
			return null;
		}

		String fileUrl = request.getContextPath() + relativeFilePath;
		LOG.debug("Created temporary file '" + filePath + "' for url: '" + fileUrl + "'");

		return fileUrl;
	}

	/**
	 * @param request
	 * @param sampleRankingValues
	 * @param criteriaVO
	 * @param tmpDir
	 * @return
	 * @throws Exception
	 */
	public static String buildComparisonFlexChart(HttpServletRequest request,
			List<SampleRankingVO> sampleRankingValues, SampleRankingCriteriaVO criteriaVO, Set<Integer> selectedItems,
			String tmpDir) throws Exception {

		// Get best values
		Collections.sort(sampleRankingValues, SampleRankingComparator.ORDER_RANK_TheoreticalResolution);
		double rankingResolutionBest = 0;
		if (sampleRankingValues.get(0).getTheoreticalResolutionValue() != null)
			rankingResolutionBest = sampleRankingValues.get(0).getTheoreticalResolutionValue();

		Collections.sort(sampleRankingValues, SampleRankingComparator.ORDER_RANK_TotalExposureTime);
		double exposureTimeBest = 0;
		if (sampleRankingValues.get(0).getTotalExposureTimeValue() != null)
			exposureTimeBest = sampleRankingValues.get(0).getTotalExposureTimeValue();

		Collections.sort(sampleRankingValues, SampleRankingComparator.ORDER_RANK_Mosaicity);
		double mosaicityBest = 0;
		if (sampleRankingValues.get(0).getMosaicityValue() != null)
			mosaicityBest = sampleRankingValues.get(0).getMosaicityValue();

		Collections.sort(sampleRankingValues, SampleRankingComparator.ORDER_RANK_NumberOfSpotsIndexed);
		int numberOfSpotsBest = 0;
		if (sampleRankingValues.get(0).getNumberOfSpotsIndexedValue() != null)
			numberOfSpotsBest = sampleRankingValues.get(0).getNumberOfSpotsIndexedValue();

		Collections.sort(sampleRankingValues, SampleRankingComparator.ORDER_RANK_NumberOfImages);
		int numberOfImagesBest = 0;
		if (sampleRankingValues.get(0).getNumberOfImagesValue() != null)
			numberOfImagesBest = sampleRankingValues.get(0).getNumberOfImagesValue();

		Collections.sort(sampleRankingValues, SampleRankingComparator.ORDER_RANK_Total);

		// Calculate percentage
		String data = "";
		for (int i = 0; i < sampleRankingValues.size(); i++) {
			SampleRankingVO sampleRankingVO = sampleRankingValues.get(i);

			// Normalized chart
			int rankingResolutionPercent = 0;
			int exposureTimePercent = 0;
			int mosaicityPercent = 0;
			int numberOfSpotsPercent = 0;
			int numberOfImagesPercent = 0;

			// // 100% rank
			// if ( sampleRankingVO.getTheoreticalResolutionValue() != null &&
			// sampleRankingVO.getTheoreticalResolutionValue() != 0 )
			// rankingResolutionPercent = (int) Math.round( ( 100 * rankingResolutionBest ) /
			// sampleRankingVO.getTheoreticalResolutionValue() );
			// if ( sampleRankingVO.getTotalExposureTimeValue() != null && sampleRankingVO.getTotalExposureTimeValue()
			// != 0 )
			// exposureTimePercent = (int) Math.round( ( 100 * exposureTimeBest ) /
			// sampleRankingVO.getTotalExposureTimeValue() );
			// if ( sampleRankingVO.getMosaicityValue() != null && sampleRankingVO.getMosaicityValue() != 0 )
			// mosaicityPercent = (int) Math.round( ( 100 * mosaicityBest ) / sampleRankingVO.getMosaicityValue() );
			// if ( sampleRankingVO.getNumberOfSpotsIndexedValue() != null &&
			// sampleRankingVO.getNumberOfSpotsIndexedValue() != 0 )
			// numberOfSpotsPercent = (int) Math.round( ( 100 * sampleRankingVO.getNumberOfSpotsIndexedValue() ) /
			// numberOfSpotsBest );
			// if ( sampleRankingVO.getNumberOfImagesValue() != null && sampleRankingVO.getNumberOfImagesValue() != 0 )
			// numberOfImagesPercent = (int) Math.round( ( 100 * numberOfImagesBest ) /
			// sampleRankingVO.getNumberOfImagesValue() );

			int selectedSample = 0;
			if (selectedItems.contains(sampleRankingVO.getDataCollectionId()))
				selectedSample = 1;

			// Relative chart
			rankingResolutionPercent = (int) Math.round(sampleRankingVO.getTheoreticalResolutionScore());
			exposureTimePercent = (int) Math.round(sampleRankingVO.getTotalExposureTimeScore());
			mosaicityPercent = (int) Math.round(sampleRankingVO.getMosaicityScore());
			numberOfSpotsPercent = (int) Math.round(sampleRankingVO.getNumberOfSpotsIndexedScore());
			numberOfImagesPercent = (int) Math.round(sampleRankingVO.getNumberOfImagesScore());

			// Data
			data += "\t<row>\n";

			// Row attribute
			data += "\t\t<row_attributes_values>\n";
			data += "\t\t\t<string>" + sampleRankingVO.getDataCollectionId() + "</string>\n";
			data += "\t\t\t<string>" + selectedSample + "</string>\n";
			data += "\t\t\t<string>" + sampleRankingVO.getTotalRank() + "</string>\n";
			data += "\t\t\t<string>" + Math.round(sampleRankingVO.getTotalScore()) + "</string>\n";
			data += "\t\t\t<string>" + sampleRankingVO.getImagePrefix() + "</string>\n";
			data += "\t\t\t<string>" + sampleRankingVO.getDataCollectionNumber() + "</string>\n";
			data += "\t\t\t<string>" + sampleRankingVO.getSpaceGroup() + "</string>\n";
			data += "\t\t</row_attributes_values>\n";

			// Chart axis values
			data += "\t\t<chart_axis_values>\n";
			data += "\t\t\t<number>" + rankingResolutionPercent + "</number>\n";
			data += "\t\t\t<number>" + exposureTimePercent + "</number>\n";
			data += "\t\t\t<number>" + mosaicityPercent + "</number>\n";
			data += "\t\t\t<number>" + numberOfSpotsPercent + "</number>\n";
			data += "\t\t\t<number>" + numberOfImagesPercent + "</number>\n";
			data += "\t\t</chart_axis_values>\n";

			data += "\t</row>\n";
		}

		// Start chart
		String xmlData = "";
		xmlData += "<chart>\n";

		// Chart Definition
		xmlData += "\t<chart_def>\n";

		// Chart Title
		xmlData += "\t\t<chart_title>ISPyB Ranking Chart</chart_title>\n";

		// Row attribute def
		xmlData += "\t\t<row_attributes_def>\n";
		xmlData += "\t\t\t<string type='hidden' key='true'>id</string>\n";
		xmlData += "\t\t\t<string type='checkbox'>Selected</string>\n";
		xmlData += "\t\t\t<string type='int'>Rank</string>\n";
		xmlData += "\t\t\t<string type='int'>Score</string>\n";
		xmlData += "\t\t\t<string type='string' width='50%'>Image prefix</string>\n";
		xmlData += "\t\t\t<string type='int'>Run</string>\n";
		xmlData += "\t\t\t<string type='string' width='10%'>Space Group</string>\n";
		xmlData += "\t\t</row_attributes_def>\n";

		// Chart axis def
		xmlData += "\t\t<chart_axis_def>\n";
		xmlData += "\t\t\t<string>Ranking Resolution (x" + criteriaVO.getWeightRankingResolution() + ")</string>\n";
		xmlData += "\t\t\t<string>Exposure Time (x" + criteriaVO.getWeightExposureTime() + ")</string>\n";
		xmlData += "\t\t\t<string>Mosaicity (x" + criteriaVO.getWeightMosaicity() + ")</string>\n";
		xmlData += "\t\t\t<string>Number of spots (x" + criteriaVO.getWeightNumberOfSpots() + ")</string>\n";
		xmlData += "\t\t\t<string>Number of images (x" + criteriaVO.getWeightNumberOfImages() + ")</string>\n";
		xmlData += "\t\t</chart_axis_def>\n";

		// Chart label def
		xmlData += "\t\t<chart_label_def>\n";
		xmlData += "\t\t\t<string>Image prefix</string>\n";
		xmlData += "\t\t\t<string>Run</string>\n";
		xmlData += "\t\t</chart_label_def>\n";

		xmlData += "\t</chart_def>\n";

		// Chart data
		xmlData += "\t<chart_data>\n";
		xmlData += data;
		xmlData += "\t</chart_data>\n";

		// End chart
		xmlData += "</chart>\n";

		// Write result to temporary file (supposed to be unique)
		Random random = new Random();
		int v = random.nextInt(1000);
		String sv = "000" + Integer.toString(v);
		sv = sv.substring(sv.length() - 3);
		String fileName = "Chart_" + System.currentTimeMillis() + "_" + sv + ".xml";
		String relativeFilePath = "/" + tmpDir + "/" + fileName;
		String filePath = request.getRealPath(relativeFilePath);

		try {
			File tempFile = new File(filePath);
			Writer output = new BufferedWriter(new FileWriter(tempFile));
			output.write(xmlData);
			output.close();
		} catch (IOException ex) {
			LOG.error("ERROR Creating temporary file '" + filePath + "': " + ex.getMessage());
			ex.printStackTrace();
			return null;
		}

		String fileUrl = request.getContextPath() + relativeFilePath;
		LOG.debug("Created temporary file '" + filePath + "' for url: '" + fileUrl + "'");

		return fileUrl;
	}

	/**
	 * Build distribution chart
	 * 
	 * @param request
	 * @param sampleRankingValues
	 * @param tmpDir
	 * @return
	 * @throws Exception
	 */
	public static String buildDistributionChart(HttpServletRequest request, String chartTitle, Integer[] distribution,
			String tmpDir) throws Exception {

		// Start chart
		String xmlData = "";
		xmlData += "<chart>\n";

		// Chart data
		xmlData += "\t<chart_data>\n";
		xmlData += "\t<row>\n";
		xmlData += "\t<null/>\n";
		for (int i = 0; i < distribution.length; i++) {
			xmlData += "\t\t<string>" + 100 / distribution.length * i + "</string>\n";
		}
		xmlData += "\t</row>\n";
		xmlData += "\t<row>\n";
		xmlData += "\t\t<string>Number of samples</string>\n";
		for (int i = 0; i < distribution.length; i++) {
			xmlData += "\t\t<number  bevel='data'>" + distribution[i] + "</number>\n";
		}
		xmlData += "\t</row>\n";
		xmlData += "\t</chart_data>\n";

		// Chart type
		String chartUnit = "sample(s)";
		// String chartType = "column";
		String chartType = "area";
		xmlData += "\t<chart_type>" + chartType + "</chart_type>\n";

		xmlData += "\t<axis_category shadow='low' size='8' color='000000' alpha='50' skip='0' />\n";
		xmlData += "\t<axis_ticks value_ticks='false' category_ticks='true' major_thickness='2' minor_thickness='1' minor_count='3' major_color='000000' minor_color='888888' position='outside' />\n";
		xmlData += "\t<axis_value shadow='low' size='8' color='000000' alpha='50' steps='4' prefix='' suffix='' decimals='0' separator='' show_min='true' />\n";

		xmlData += "\t<chart_border color='000000' top_thickness='1' bottom_thickness='2' left_thickness='0' right_thickness='0' />\n";
		xmlData += "\t<chart_guide horizontal='true' vertical='true' thickness='1' color='000000' alpha='35' type='dashed' radius='6' line_alpha='80' line_thickness='2'  background_color='DDDDDD' text_h_alpha='90' text_v_alpha='90' prefix_h='' suffix_h='' suffix_v='' />\n";
		xmlData += "\t<chart_rect x='20' y='50' width='770' height='320' positive_color='FFFFFF' positive_alpha='40' />\n";
		xmlData += "\t<chart_pref line_thickness='2' point_shape='none' fill_shape='false' />\n";
		xmlData += "\t<chart_label color='000000' alpha='90' size='12' position='cursor' prefix='' suffix=' "
				+ chartUnit + "' background_color='AAAAAA' />\n";

		xmlData += "\t<chart_grid_h alpha='10' color='0066FF' thickness='1' />\n";
		xmlData += "\t<chart_grid_v alpha='10' color='0066FF' thickness='1' />\n";

		xmlData += "\t<filter>\n";
		xmlData += "\t\t<shadow id='high' distance='3' angle='45' color='0' alpha='50' blurX='10' blurY='10' />\n";
		xmlData += "\t\t<shadow id='low' distance='2' angle='45' alpha='35' blurX='5' blurY='5' />\n";
		xmlData += "\t\t<shadow id='bg' inner='true' quality='1' distance='25' angle='135' color='000000' alpha='15' blurX='200' blurY='150' />\n";
		xmlData += "\t\t<glow id='glow1' color='ff88ff' alpha='75' blurX='30' blurY='30' inner='false' />\n";
		xmlData += "\t</filter>\n";

		xmlData += "\t<draw>\n";
		xmlData += "\t<text shadow='low' transition='dissolve' delay='.5' duration='0.5' color='333333' alpha='75' size='14' x='390' y='50' width='396' height='300' h_align='right' v_align='top'>"
				+ chartTitle + "</text>\n";
		xmlData += "\t</draw>\n";

		xmlData += "\t<legend toggle='true' shadow='low' x='0' y='10' width='800' height='20' margin='5' fill_color='000066' fill_alpha='8' line_alpha='0' line_thickness='0' size='12' color='333355' alpha='90' />\n";

		// Colors
		xmlData += "\t<series_color>\n";
		xmlData += "\t<color>dd6b66</color>\n";
		xmlData += "\t<color>ddaa41</color>\n";
		xmlData += "\t<color>FFCC66</color>\n";
		xmlData += "\t</series_color>\n";
		xmlData += "\t<series bar_gap='0' set_gap='1' />\n";

		// End chart
		xmlData += "</chart>\n";

		// Write result to temporary file (supposed to be unique)
		Random random = new Random();
		int v = random.nextInt(1000);
		String sv = "000" + Integer.toString(v);
		sv = sv.substring(sv.length() - 3);
		String fileName = "Chart_" + System.currentTimeMillis() + "_" + sv + ".xml";
		String relativeFilePath = "/" + tmpDir + "/" + fileName;
		String filePath = request.getRealPath(relativeFilePath);

		try {
			File tempFile = new File(filePath);
			Writer output = new BufferedWriter(new FileWriter(tempFile));
			output.write(xmlData);
			output.close();
		} catch (IOException ex) {
			LOG.error("ERROR Creating temporary file '" + filePath + "': " + ex.getMessage());
			ex.printStackTrace();
			return null;
		}

		String fileUrl = request.getContextPath() + relativeFilePath;
		LOG.debug("Created temporary file '" + filePath + "' for url: '" + fileUrl + "'");

		return fileUrl;
	}

}
