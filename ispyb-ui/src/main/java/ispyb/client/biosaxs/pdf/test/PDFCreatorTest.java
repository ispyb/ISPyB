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

package ispyb.client.biosaxs.pdf.test;


import ispyb.server.biosaxs.services.core.ExperimentScope;
import ispyb.server.biosaxs.services.core.experiment.Experiment3Service;
import ispyb.server.biosaxs.services.core.proposal.SaxsProposal3Service;
import ispyb.server.biosaxs.vos.dataAcquisition.Experiment3VO;
import ispyb.server.common.services.proposals.Proposal3Service;
import ispyb.server.common.test.SaxsEJB3Test;

import java.text.DecimalFormat;

import org.junit.Before;



public class PDFCreatorTest extends SaxsEJB3Test {

	
	static DecimalFormat df=new DecimalFormat("0.00");
	
	/** PARAMETERS **/
	private Experiment3Service experiment3Service;
	private SaxsProposal3Service proposal3Service;
	private Proposal3Service MXproposal3Service;
	
	
//	String[] COLUMNS = {"ID", "MOLECULE","BUFFER", "CON. (mg/ml)", "Frames Avg.", "Rg", "Points", "Quality", "IO", "Aggregated", "Rg", "Total", "Dmax", "Volume", "MM(kD) Vol. Est." };
//	String[] MEASUREMENT_COLUMNS = {"ID", "MACROMOLECULE","BUFFER", "CONCENTRATION", "VOLUME", "EXP. TEMP.", "TRANS.", "VISCOSITY",  "WAIT TIME", "FLOW", "ENERGY", "FRAMES", "TIME"};
//	
	@Before
	public void setUp() throws Exception {
		experiment3Service = (Experiment3Service) serviceLocator.getRemoteService(Experiment3Service.class, properties);
		proposal3Service = (SaxsProposal3Service) serviceLocator.getRemoteService(SaxsProposal3Service.class, properties);
		MXproposal3Service = (Proposal3Service) serviceLocator.getRemoteService(Proposal3Service.class, properties);
		
	}
	
//	@Test
	public void FactoryAcquisitionReport() throws Exception{
		int proposalId = 3282;
		int experimentId = 246;
		try{
		Experiment3VO experiment =  this.experiment3Service.findById(experimentId, ExperimentScope.MEDIUM, proposalId);
//		List<Buffer3VO> buffers = proposal3Service.findBuffersByProposalId(experiment.getProposalId());
//		Proposal3VO proposal = MXproposal3Service.findByPk(proposalId);
		
		/** PDF **/
//		SaxsReportFactory factory = new SaxsReportFactory();
//		IPDFReport report = factory.getPDFReport(PDFReportType.DATA_ACQUISITION);
//		report.setUp(experiment, buffers, proposal);
//		
//		OutputStream outputStream = new FileOutputStream ("c:/SAXS_DataAcquisitionReport.pdf");
//		report.run().writeTo(outputStream);
//		
//		
//		/** CSV **/
//		DataAcquisitionCSVReport da = new DataAcquisitionCSVReport(); 
//		da.setUp(experiment, buffers, proposal);
		
//			FileWriter fw = new FileWriter("c:/SAXS_DataAcquisitionReport2.txt");
//			BufferedWriter bw = new BufferedWriter(fw);
//			bw.write(da.run());
//			bw.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	
}