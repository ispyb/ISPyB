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
package ispyb.server.mx.services.utils.bean;

import ispyb.server.mx.vos.autoproc.AutoProcStatus3VO;

import java.util.List;

/**
 * autoprocessing information displayed: details for 1 autoprocessing
 * @author BODIN
 *
 */
public class AutoProcessingDetail {

	protected Integer autoProcId;

	protected String overallResolution;

	protected String overallCompleteness;

	protected String overallIOverSigma;

	protected String overallRsymm;

	protected String overallMultiplicity;

	protected String outerResolution;

	protected String outerCompleteness;

	protected String outerIOverSigma;

	protected String outerRsymm;

	protected String outerMultiplicity;

	protected String unitCellA;

	protected String unitCellB;

	protected String unitCellC;

	protected String unitCellAlpha;

	protected String unitCellBeta;

	protected String unitCellGamma;

	protected List<AutoProcStatus3VO> autoProcEvents;

	protected List<AutoProcAttachmentWebBean> autoProcProgAttachmentsWebBeans;


	public AutoProcessingDetail() {
		super();
	}



	public Integer getAutoProcId() {
		return autoProcId;
	}

	public void setAutoProcId(Integer autoProcId) {
		this.autoProcId = autoProcId;
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

	public String getOverallMultiplicity() {
		return overallMultiplicity;
	}

	public void setOverallMultiplicity(String overallMultiplicity) {
		this.overallMultiplicity = overallMultiplicity;
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

	public String getOuterMultiplicity() {
		return outerMultiplicity;
	}

	public void setOuterMultiplicity(String outerMultiplicity) {
		this.outerMultiplicity = outerMultiplicity;
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

	public List<AutoProcStatus3VO> getAutoProcEvents() {
		return autoProcEvents;
	}

	public void setAutoProcEvents(List<AutoProcStatus3VO> autoProcEvents) {
		this.autoProcEvents = autoProcEvents;
	}

	public List<AutoProcAttachmentWebBean> getAutoProcProgAttachmentsWebBeans() {
		return autoProcProgAttachmentsWebBeans;
	}
	
	public void setAutoProcProgAttachmentsWebBeans(
			List<AutoProcAttachmentWebBean> autoProcProgAttachmentsWebBeans) {
		this.autoProcProgAttachmentsWebBeans = autoProcProgAttachmentsWebBeans;
	}

}
