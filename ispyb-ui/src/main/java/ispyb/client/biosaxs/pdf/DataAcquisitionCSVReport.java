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
 ******************************************************************************************************************************/

package ispyb.client.biosaxs.pdf;

import ispyb.server.biosaxs.vos.dataAcquisition.Buffer3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Experiment3VO;
import ispyb.server.common.vos.proposals.Proposal3VO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * @author DEMARIAA
 *
 */
public class DataAcquisitionCSVReport extends DataAcquisitionReport implements ICSVReport {
	
	

	public String exportToASCII(Experiment3VO experiment, List<Buffer3VO> buffers, Proposal3VO proposal) throws Exception{
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		
		StringBuilder content = new StringBuilder();
		content.append("# ISPyB Report generated on " + dateFormat.format(cal.getTime())).append("\n");
		content.append("# Title: " + proposal.getTitle()).append("\n");
		content.append("# Proposal: " + proposal.getCode() + proposal.getNumber()).append("\n");
		content.append("# Data Acquisition: " + experiment.getName()).append("\n");
		content.append("# Type: " + experiment.getType()).append("\n");
		content.append("# Date: " + experiment.getCreationDate()).append("\n");
		
		content.append("#");
		for (String column : this.COLUMNS) {
			content.append(column).append("\t");
		}
		
		List<List<String>> data = this.getExperimentAnalysisData(experiment, buffers);
		content.append("\n");
		for (int i = 0; i < data.size(); i++) {
			List<String> row = data.get(i);
			for (String column : row) {
				content.append(column).append("\t");
			}
			content.append("\n");
		}
		return content.toString();
	}

	
	@Override
	public String run() throws Exception {
		if (this.checkParameters()){
			return this.exportToASCII(this.experiment, this.buffers, this.proposal);
		}
		else{
			throw new Exception("Parameters are not correct");
		}
	}

	
}
