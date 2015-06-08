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
package ispyb.client.mx.results;

import ispyb.server.mx.vos.autoproc.AutoProcStatus3VO;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.apache.struts.action.ActionForm;

/**
 * @struts.form name="getAutoProcsForm"
 */

public class GetAutoProcsForm extends ActionForm implements Serializable {

	static final long serialVersionUID = 0;

	private String autoProc;

	private String overallResolution;

	private String overallCompleteness;

	private String overallIOverSigma;

	private String overallRsymm;

	private String overallMultiplicity;

	private String outerResolution;

	private String outerCompleteness;

	private String outerIOverSigma;

	private String outerRsymm;

	private String outerMultiplicity;

	private String unitCellA;

	private String unitCellB;

	private String unitCellC;

	private String unitCellAlpha;

	private String unitCellBeta;

	private String unitCellGamma;

	private Integer dataCollectionId;

	private Collection autoProcProgAttachments;

	private List<AutoProcAttachmentWebBean> outputAutoProcProgAttachmentsWebBeans;
	private List<AutoProcAttachmentWebBean> inputAutoProcProgAttachmentsWebBeans;
	private List<AutoProcAttachmentWebBean> correctionFilesAttachmentsWebBeans;
	
	private int nbAutoProcAttachmentsWebBeans;
	
	private List<AutoProcStatus3VO> autoProcEvents;

	public GetAutoProcsForm() {
		super();
	}

	public String getAutoProc() {
		return autoProc;
	}

	public void setAutoProc(String autoProc) {
		this.autoProc = autoProc;
	}

	/**
	 * @return Returns the serialVersionUID.
	 */
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getOverallResolution() {
		return overallResolution;
	}

	public void setOverallResolution(String overallResolution) {
		this.overallResolution = overallResolution;
	}

	public String getOverallCompleteness() {
		return overallCompleteness;
	}

	public void setOverallCompleteness(String overallCompleteness) {
		this.overallCompleteness = overallCompleteness;
	}

	public String getOverallIOverSigma() {
		return overallIOverSigma;
	}

	public void setOverallIOverSigma(String overallIOverSigma) {
		this.overallIOverSigma = overallIOverSigma;
	}

	public String getOverallRsymm() {
		return overallRsymm;
	}

	public void setOverallRsymm(String overallRsymm) {
		this.overallRsymm = overallRsymm;
	}

	public String getOuterResolution() {
		return outerResolution;
	}

	public void setOuterResolution(String outerResolution) {
		this.outerResolution = outerResolution;
	}

	public String getOuterCompleteness() {
		return outerCompleteness;
	}

	public void setOuterCompleteness(String outerCompleteness) {
		this.outerCompleteness = outerCompleteness;
	}

	public String getOuterIOverSigma() {
		return outerIOverSigma;
	}

	public void setOuterIOverSigma(String outerIOverSigma) {
		this.outerIOverSigma = outerIOverSigma;
	}

	public String getOuterRsymm() {
		return outerRsymm;
	}

	public void setOuterRsymm(String outerRsymm) {
		this.outerRsymm = outerRsymm;
	}

	public String getUnitCellA() {
		return unitCellA;
	}

	public void setUnitCellA(String unitCellA) {
		this.unitCellA = unitCellA;
	}

	public String getUnitCellB() {
		return unitCellB;
	}

	public void setUnitCellB(String unitCellB) {
		this.unitCellB = unitCellB;
	}

	public String getUnitCellC() {
		return unitCellC;
	}

	public void setUnitCellC(String unitCellC) {
		this.unitCellC = unitCellC;
	}

	public String getUnitCellAlpha() {
		return unitCellAlpha;
	}

	public void setUnitCellAlpha(String unitCellAlpha) {
		this.unitCellAlpha = unitCellAlpha;
	}

	public String getUnitCellBeta() {
		return unitCellBeta;
	}

	public void setUnitCellBeta(String unitCellBeta) {
		this.unitCellBeta = unitCellBeta;
	}

	public String getUnitCellGamma() {
		return unitCellGamma;
	}

	public void setUnitCellGamma(String unitCellGamma) {
		this.unitCellGamma = unitCellGamma;
	}

	public String getOverallMultiplicity() {
		return overallMultiplicity;
	}

	public void setOverallMultiplicity(String overallMultiplicity) {
		this.overallMultiplicity = overallMultiplicity;
	}

	public String getOuterMultiplicity() {
		return outerMultiplicity;
	}

	public void setOuterMultiplicity(String outerMultiplicity) {
		this.outerMultiplicity = outerMultiplicity;
	}

	public Collection getAutoProcProgAttachments() {
		return autoProcProgAttachments;
	}

	public void setAutoProcProgAttachments(Collection autoProcProgAttachments) {
		this.autoProcProgAttachments = autoProcProgAttachments;
	}

	public Integer getDataCollectionId() {
		return dataCollectionId;
	}

	public void setDataCollectionId(Integer dataCollectionId) {
		this.dataCollectionId = dataCollectionId;
	}

	public List<AutoProcAttachmentWebBean> getOutputAutoProcProgAttachmentsWebBeans() {
		return outputAutoProcProgAttachmentsWebBeans;
	}

	public void setOutputAutoProcProgAttachmentsWebBeans(List<AutoProcAttachmentWebBean> outputAutoProcProgAttachmentsWebBeans) {
		this.outputAutoProcProgAttachmentsWebBeans = outputAutoProcProgAttachmentsWebBeans;
		//sortAutoProcProgAttachmentsWebBeans(this.outputAutoProcProgAttachmentsWebBeans);
	}
	
	public List<AutoProcAttachmentWebBean> getInputAutoProcProgAttachmentsWebBeans() {
		return inputAutoProcProgAttachmentsWebBeans;
	}

	public void setInputAutoProcProgAttachmentsWebBeans(List<AutoProcAttachmentWebBean> inputAutoProcProgAttachmentsWebBeans) {
		this.inputAutoProcProgAttachmentsWebBeans = inputAutoProcProgAttachmentsWebBeans;
		//sortAutoProcProgAttachmentsWebBeans(this.inputAutoProcProgAttachmentsWebBeans);
	}
	
	public List<AutoProcAttachmentWebBean> getCorrectionFilesAttachmentsWebBeans() {
		return correctionFilesAttachmentsWebBeans;
	}

	public void setCorrectionFilesAttachmentsWebBeans(
			List<AutoProcAttachmentWebBean> correctionFilesAttachmentsWebBeans) {
		this.correctionFilesAttachmentsWebBeans = correctionFilesAttachmentsWebBeans;
	}

	public int getNbAutoProcAttachmentsWebBeans(){
		return this.nbAutoProcAttachmentsWebBeans;
	}
	
	public void setNbAutoProcAttachmentsWebBeans(int nbAutoProcAttachmentsWebBeans){
		this.nbAutoProcAttachmentsWebBeans = nbAutoProcAttachmentsWebBeans;
	}

	public List<AutoProcStatus3VO> getAutoProcEvents() {
		return autoProcEvents;
	}

	public void setAutoProcEvents(List<AutoProcStatus3VO> autoProcEvents) {
		this.autoProcEvents = autoProcEvents;
	}
}
