/** This file is part of ISPyB.
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

package ispyb.ws.soap.em;

import java.util.HashMap;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import org.jboss.ejb3.annotation.SecurityDomain;
import org.jboss.ws.api.annotation.WebContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
	

@WebService(name = "ToolsForEMWebService", serviceName = "ispybWS", targetNamespace = "http://ispyb.ejb3.webservices.em")
@SOAPBinding(style = Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
@Stateless
@RolesAllowed({ "WebService", "User", "Industrial" })
@SecurityDomain("ispyb")
@WebContext(authMethod = "BASIC", secureWSDLAccess = false, transportGuarantee = "NONE")
public class ToolsForEMDataCollection extends EMDataCollection{

	protected Logger log = LoggerFactory.getLogger(ToolsForEMDataCollection.class);

	@WebMethod(operationName = "addMovie")
	public void addMovie(
			@WebParam(name = "proposal") String proposal,
			@WebParam(name = "sampleAcronym") String sampleAcronym,
			@WebParam(name = "imageDirectory") String imageDirectory,
			@WebParam(name = "jpeg") String jpeg,
			@WebParam(name = "mrc") String mrc,
			@WebParam(name = "xml") String xml,
			@WebParam(name = "microscopeVoltage") String microscopeVoltage,
			@WebParam(name = "sphericalAberration") String sphericalAberration,
			@WebParam(name = "amplitudeContrast") String amplitudeContrast,
			@WebParam(name = "magnificationRate") String magnificationRate,
			@WebParam(name = "pixelSize") String pixelSize,
			@WebParam(name = "noImages") String noImages,
			@WebParam(name = "dosePerImage") String dosePerImage
			)	
	{
		try {
			log.info("addMovie. technique=EM proposal={} sampleAcronym={} imageDirectory={} jpeg={} mrc={} xml={} microscopeVoltage={} sphericalAberration={} amplitudeContrast={} magnificationRate={} pixelSize={} noImages={} dosePerImage={}", proposal, sampleAcronym, imageDirectory, jpeg, mrc, xml, microscopeVoltage, sphericalAberration,amplitudeContrast,magnificationRate,pixelSize,noImages,dosePerImage);

		} catch (Exception exp) {
			log.error("addMovie. technique=EM proposal={} sampleAcronym={} imageDirectory={} jpeg={} mrc={} xml={} microscopeVoltage={} sphericalAberration={} amplitudeContrast={} magnificationRate={} pixelSize={} noImages={} dosePerImage={}", proposal, sampleAcronym, imageDirectory, jpeg, mrc, xml, microscopeVoltage, sphericalAberration,amplitudeContrast,magnificationRate,pixelSize,noImages,dosePerImage);
		}
	}
	
	
	@WebMethod(operationName = "addMotionCorrection")
	public void addMotionCorrection(
			@WebParam(name = "proposal") String proposal,
			@WebParam(name = "imageDirectory") String imageDirectory,
			@WebParam(name = "jpeg") String jpeg,
			@WebParam(name = "png") String png,
			@WebParam(name = "mrc") String mrc,
			@WebParam(name = "logFilePath") String logFilePath
			)	
	{
		try {
			log.info("addMotionCorrection. technique=EM proposal={} imageDirectory={} jpeg={} png={} mrc={} log={}", proposal, imageDirectory, jpeg, png, mrc, logFilePath);
		} catch (Exception exp) {
			log.error("addMotionCorrection. technique=EM proposal={} imageDirectory={} jpeg={} png={} mrc={} log={}", proposal, imageDirectory, jpeg, png, mrc, logFilePath);
		}
	}
	
	@WebMethod(operationName = "addCTF")
	public void addCTF(
			@WebParam(name = "proposal") String proposal,
			@WebParam(name = "imageDirectory") String imageDirectory,
			@WebParam(name = "jpeg") String jpeg,
			@WebParam(name = "mrc") String mrc,
			@WebParam(name = "outputOne") String outputOne,
			@WebParam(name = "outputTwo") String outputTwo,
			@WebParam(name = "logFilePath") String logFilePath
			)	
	{
		try {
			log.info("addCTF. technique=EM proposal={} imageDirectory={} jpeg={} mrc={} outputOne={} outputTwo={} log={}", proposal, imageDirectory, jpeg, mrc, outputOne, outputTwo, logFilePath);
		} catch (Exception exp) {
			log.error("addCTF. technique=EM proposal={} imageDirectory={} jpeg={} mrc={} outputOne={} outputTwo={} log={}", proposal, imageDirectory, jpeg, mrc, outputOne, outputTwo, logFilePath);
		}
	}

	
}