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
/*
 * AdminChart.java
 * @author patrice.brenchereau@esrf.fr
 * August 21, 2008
 */
package ispyb.client.common.admin;

import ispyb.common.util.Constants;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.naming.InitialContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

public class AdminChart {

	private final Logger LOG = Logger.getLogger(AdminChart.class);

	/**
	 * Build xml data file for chart viewing
	 * 
	 * @param request
	 * @param jndiName
	 *            : jndi name of the database
	 * @param viewName
	 *            : name of the view in the database that gives the chart data
	 * @param chartTypes
	 *            : 'column', 'bar', 'area', 'line', 'stacked column', 'stacked area', 'pie',... and combinations: 'column,line',...
	 * @param chartTitle
	 *            : chart title to be displayed on the chart
	 * @param chartUnit
	 *            : chart unit to be displayed on cursor
	 * @param tmpDir
	 *            : relative temporary directory (from web root)
	 * @return Url of xml data file
	 * @throws Exception
	 */

	public String getReport(HttpServletRequest request, String viewName, String[] chartTypes, String chartTitle, String chartUnit,
			String tmpDir) throws Exception {

		String connectionString = Constants.getProperty("ISPyB.dbJndiName.direct");

		InitialContext ic = new InitialContext();
		DataSource ds = (DataSource) ic.lookup(connectionString);
		Connection conn = ds.getConnection();
		Statement stmt = conn.createStatement();

		String query = "SELECT * FROM " + viewName;

		// Issue query
		ResultSet rs = stmt.executeQuery(query);
		ResultSetMetaData rsMetaData = rs.getMetaData();

		// Get Metadata
		int numberOfColumns = rsMetaData.getColumnCount();
		List colLabels = new ArrayList();
		for (int i = 1; i <= numberOfColumns; i++) {
			colLabels.add(new String(rsMetaData.getColumnName(i)));
		}

		// Get values
		int numberOfRows = 0;
		List rowValues = new ArrayList();
		while (rs.next()) {
			List colValues = new ArrayList();
			for (int i = 1; i <= numberOfColumns; i++) {
				// System.out.println("Value = "+rs.getString(i));
				colValues.add(new String("" + rs.getString(i)));
			}
			rowValues.add(colValues);
			numberOfRows++;
		}

		// Close connection
		stmt.close();
		conn.close();

		// Start chart
		String xmlData = "";
		xmlData += "<chart>\n";

		// Chart data
		xmlData += "\t<chart_data>\n";
		for (int col = 0; col < numberOfColumns; col++) {
			xmlData += "\t\t<row>\n";
			if (col == 0)
				xmlData += "\t\t\t<null/>\n";
			else
				xmlData += "\t\t\t<string>" + colLabels.get(col) + "</string>\n";
			for (int row = 0; row < numberOfRows; row++) {
				// if ( col == 0) xmlData += "\t\t\t<string>"+((List)rowValues.get(row)).get(col)+"</string>\n";
				// else xmlData += "\t\t\t<number  bevel='data'>"+((List)rowValues.get(row)).get(col)+"</number>\n";
				String value = "" + ((List) rowValues.get(row)).get(col);
				if (value.equals("null")) {
					if (col == 0)
						xmlData += "\t\t\t<null/>\n";
					else
						xmlData += "\t\t\t<null/>\n";
					;
				} else {
					if (col == 0)
						xmlData += "\t\t\t<string>" + value + "</string>\n";
					else
						xmlData += "\t\t\t<number  bevel='data'>" + value + "</number>\n";
				}
			}
			xmlData += "\t\t</row>\n";
		}
		xmlData += "\t</chart_data>\n";

		// Chart types
		if (chartTypes.length == 1) {
			xmlData += "\t<chart_type>" + chartTypes[0] + "</chart_type>\n";
		} else {
			xmlData += "\t<chart_type>\n";
			for (int i = 0; i < chartTypes.length; i++) {
				xmlData += "\t<string>" + chartTypes[i] + "</string>\n";
			}
			xmlData += "\t</chart_type>\n";
		}

		// Pie
		if (chartTypes.length == 1 && (chartTypes[0].equals("pie") || chartTypes[0].equals("3d pie"))) {

			xmlData += "\t<chart_label shadow='low' color='ffffff' alpha='95' size='10' position='inside' as_percentage='true' />\n";
			xmlData += "\t<chart_rect bevel='bg' positive_color='EEEEEE' positive_alpha='40' />\n";
			xmlData += "\t<chart_transition type='scale' delay='0' duration='.5' order='series' />\n";

			xmlData += "\t<filter>\n";
			xmlData += "\t<shadow id='low' distance='2' angle='45' color='0' alpha='50' blurX='5' blurY='5' />\n";
			xmlData += "\t<bevel id='data' angle='45' blurX='10' blurY='10' distance='3' highlightAlpha='5' highlightColor='ffffff' shadowColor='000000' shadowAlpha='50' type='full' />\n";
			xmlData += "\t</filter>\n";

			xmlData += "\t<draw>\n";
			xmlData += "\t<text shadow='low' transition='dissolve' delay='.5' duration='0.5' color='333333' alpha='75' size='14' x='490' y='50' width='296' height='300' h_align='right' v_align='top'>"
					+ chartTitle + "</text>\n";
			xmlData += "\t</draw>\n";

			xmlData += "\t<legend shadow='low' fill_color='0' fill_alpha='5' line_alpha='0' line_thickness='0' bullet='circle' size='12' color='000000' alpha='75' margin='10' />\n";

		}
		// Column, area, line,...
		else {

			xmlData += "\t<axis_category shadow='low' size='8' color='000000' alpha='50' skip='0' />\n";
			xmlData += "\t<axis_ticks value_ticks='false' category_ticks='true' major_thickness='2' minor_thickness='1' minor_count='3' major_color='000000' minor_color='888888' position='outside' />\n";
			xmlData += "\t<axis_value shadow='low' size='8' color='000000' alpha='50' steps='4' prefix='' suffix='' decimals='0' separator='' show_min='true' />\n";

			xmlData += "\t<chart_border color='000000' top_thickness='1' bottom_thickness='2' left_thickness='0' right_thickness='0' />\n";
			// xmlData +=
			// "\t<chart_guide horizontal='true' vertical='true' thickness='1' color='000000' alpha='35' type='dashed' radius='3' fill_alpha='75' line_alpha='0' background_color='DDDDDD' text_h_alpha='90' text_v_alpha='90' prefix_h='' suffix_h='' suffix_v='' />\n";
			xmlData += "\t<chart_guide horizontal='true' vertical='true' thickness='1' color='000000' alpha='35' type='dashed' radius='6' line_alpha='80' line_thickness='2'  background_color='DDDDDD' text_h_alpha='90' text_v_alpha='90' prefix_h='' suffix_h='' suffix_v='' />\n";
			xmlData += "\t<chart_rect x='20' y='50' width='770' height='320' positive_color='FFFFFF' positive_alpha='40' />\n";
			xmlData += "\t<chart_pref line_thickness='2' point_shape='none' fill_shape='false' />\n";
			// xmlData +=
			// "\t<chart_label shadow='low' color='ffffff' alpha='95' size='10' position='inside' as_percentage='true' />\n";
			// xmlData +=
			// "\t<chart_label color='ffffcc' background_color='444488' alpha='100' size='12' position='cursor' />\n";
			// xmlData +=
			// "\t<chart_label color='ff6600' alpha='90' size='12' position='cursor' prefix='' suffix=' "+chartUnit+"' background_color='000000' />\n";
			xmlData += "\t<chart_label color='000000' alpha='90' size='12' position='cursor' prefix='' suffix=' " + chartUnit
					+ "' background_color='AAAAAA' />\n";

			xmlData += "\t<chart_grid_h alpha='10' color='0066FF' thickness='1' />\n";
			xmlData += "\t<chart_grid_v alpha='10' color='0066FF' thickness='1' />\n";
			// xmlData += "\t<chart_transition type='scale' delay='0' duration='.5' order='series' />\n";

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

		}

		// Colors
		xmlData += "\t<series_color>\n";
		xmlData += "\t<color>dd6b66</color>\n";
		xmlData += "\t<color>ddaa41</color>\n";
		xmlData += "\t<color>FFCC66</color>\n";
		xmlData += "\t<color>336699</color>\n";
		xmlData += "\t<color>FFFFCC</color>\n";
		xmlData += "\t<color>7700ee</color>\n";
		xmlData += "\t<color>4e62dd</color>\n";
		xmlData += "\t<color>88dd11</color>\n";
		xmlData += "\t<color>ff8811</color>\n";
		xmlData += "\t<color>4d4d4d</color>\n";
		xmlData += "\t<color>5a4b6e</color>\n";
		xmlData += "\t</series_color>\n";
		// xmlData += "\t<series set_gap='45' bar_gap='-80' />\n";
		xmlData += "\t<series bar_gap='0' set_gap='10' />\n";

		// End chart
		xmlData += "</chart>\n";

		// Write result to temporary file (supposed to be unique)
		Random random = new Random();
		int v = random.nextInt(1000);
		String sv = "000" + Integer.toString(v);
		sv = sv.substring(sv.length() - 3);
		String fileName = "Chart_" + System.currentTimeMillis() + "_" + sv + ".xml";
		
		ServletContext context = request.getSession().getServletContext();
		String relativeFilePath = "/" + tmpDir + "/" + fileName;		
		String tmpFilePath = "\\" + tmpDir +  "\\";		
		
		String filePathOld = request.getRealPath(tmpFilePath) + "\\" + fileName;
		//String filePath = request.getRealPath("/") + "\\" + tmpDir + "\\" + fileName;	
		String filePath = request.getRealPath("/") + "/" + tmpDir + "/" + fileName;	
		
		try {
			File tempFile = new File(filePath);
			//TODO use fileInputStream because on prod and valid the request.getRealPath(tmpFilePath) returns null.
			//InputStream fileInputStream = context.getResourceAsStream(relativeFilePath);
			//File tempFile2 = File.createTempFile(prefix, suffix, directory)	
							
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
