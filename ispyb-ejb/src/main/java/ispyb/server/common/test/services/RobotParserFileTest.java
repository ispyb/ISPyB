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

package ispyb.server.common.test.services;


import ispyb.server.biosaxs.services.core.ExperimentScope;
import ispyb.server.biosaxs.services.core.experiment.Experiment3Service;
import ispyb.server.biosaxs.services.core.proposal.SaxsProposal3Service;
import ispyb.server.biosaxs.vos.dataAcquisition.Experiment3VO;
import ispyb.server.biosaxs.vos.utils.comparator.SaxsDataCollectionComparator;
import ispyb.server.biosaxs.vos.utils.parser.RobotXMLParser;
import ispyb.server.common.test.SaxsEJB3Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.HashSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class RobotParserFileTest extends SaxsEJB3Test {



	private Experiment3Service experiment3Service;
	private SaxsProposal3Service saxsProposal3Service;

	

	/** The now. */
	protected static Calendar NOW;
	
	@Before
	public void setUp() throws Exception {
		experiment3Service = (Experiment3Service) serviceLocator.getRemoteService(Experiment3Service.class, properties);
		saxsProposal3Service = (SaxsProposal3Service) serviceLocator.getRemoteService(SaxsProposal3Service.class, properties);
	}

	@Test
	public void test(){
//		for (int i = 2018; i < 2030; i++) {
//			System.out.println("--------------" + i);
			try{
				this.getFile(2198, 10);
			}
			catch(Exception e){
				e.printStackTrace();
//				System.out.println("ERROR:" + i);
			}
//		}
	}
	
	public void getFile(int experimentId, int proposalId){
			try {
				String filePath = "c:\\robot2.xml";
				PrintWriter out = new PrintWriter(filePath);
				
				Experiment3VO experiment = null;
				try{
					experiment = experiment3Service.findById(experimentId, ExperimentScope.MEDIUM, proposalId);
				}
				catch(Exception e){
					System.out.println("ERROR:");
					return;
				}
				experiment3Service.setPriorities(experiment.getExperimentId(), proposalId, SaxsDataCollectionComparator.defaultComparator);
				
				out.println(RobotXMLParser.toRobotXML(experiment, experiment.getSamplePlate3VOs(), saxsProposal3Service.findBuffersByProposalId(proposalId)));
				out.close();
				
				
				PrintWriter outtxt = new PrintWriter("c:\\robot.txt");
				outtxt.println(readFile(filePath));
				outtxt.close();
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public String readFile(String filePath) throws ParserConfigurationException, SAXException, IOException{
		File fXmlFile = new File(filePath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
	 
		int spaces = 15;
		doc.getDocumentElement().normalize();
	 
		StringBuilder sb = new StringBuilder();
		NodeList nList = doc.getElementsByTagName("Buffer");
		
		HashSet<String> ignore = new HashSet<String>();
		ignore.add("enable");
		ignore.add("comments");
		ignore.add("priority");
		ignore.add("code");
		ignore.add("recuperate");
		ignore.add("plate");
		ignore.add("plate");
		
		for (int temp = 0; temp < 1; temp++) {
			Node nNode = nList.item(temp);
			NodeList bufferNode = nNode.getChildNodes();
			
			/** Titles **/
			for (int i = 0; i < bufferNode.getLength(); i++) {
				for (int j = 0; j < bufferNode.item(i).getChildNodes().getLength(); j++) {
					Node node =  bufferNode.item(i).getChildNodes().item(j);
					if (!ignore.contains(node.getNodeName())){
						if (node.getNodeType() == Node.ELEMENT_NODE){
							sb.append("\t" +  String.format("%-" + spaces + "s", node.getNodeName().trim())); 
						}
					}
				}
			}
		}
		
	
		
		nList = doc.getElementsByTagName("Buffer");
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			NodeList bufferNode = nNode.getChildNodes();
			sb.append("\nBuffer:");
			for (int i = 0; i < bufferNode.getLength(); i++) {
				for (int j = 0; j < bufferNode.item(i).getChildNodes().getLength(); j++) {
					Node node =  bufferNode.item(i).getChildNodes().item(j);
					if (node.getNodeType() == Node.ELEMENT_NODE){
//						if (!ignore.contains(node.getNodeName())){
//							if (node.getTextContent().trim().length() != 0){
//								sb.append("\t" +  String.format("%-" + spaces + "s", node.getTextContent().trim()));
//							}
//							else{
//								String s = "\t--";
//								sb.append(String.format("%-"+ spaces +"s", s));
//							}
//						}
					}
					else{
					}
				}
			}
		}
		sb.append("\n");
		nList = doc.getElementsByTagName("Sample");
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			NodeList bufferNode = nNode.getChildNodes();
			sb.append("\nSample:");
			for (int i = 0; i < bufferNode.getLength(); i++) {
				for (int j = 0; j < bufferNode.item(i).getChildNodes().getLength(); j++) {
					Node node =  bufferNode.item(i).getChildNodes().item(j);
					if (!ignore.contains(node.getNodeName())){
						if (node.getNodeType() == Node.ELEMENT_NODE){
							
//							if (node.getTextContent().trim().length() != 0){
//								sb.append("\t" +  String.format("%-" + spaces + "s", node.getTextContent().trim()));
//							}
//							else{
//								String s = "\t--";
//								sb.append(String.format("%-"+ spaces +"s", s));
//							}
						}
					}
				}
			}
		}
		
		return sb.toString();
	}
	
	
}