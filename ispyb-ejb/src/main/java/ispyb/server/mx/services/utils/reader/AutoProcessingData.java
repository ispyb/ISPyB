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
package ispyb.server.mx.services.utils.reader;

/**
 * data to be plotted - auto processing graphs
 * 3 different graphes are available: 
 * from XSCALE.LP file: resolution vs completeness /or R-Factor or I/Sigl or CC/2 or SigAno
 * wilson plot
 * cumultive intensity distribution
 * @author BODIN
 *
 */
public class AutoProcessingData {
	
	protected String autoProcProgramAttachmentId;

	protected Double resolutionLimit;
	
	protected Double completeness;
	
	protected Double rFactorObserved;
	
	protected Double iSigma;
	
	protected Double cc2;
	
	protected Double sigAno;
	
	protected Integer anomalCorr;
	
	/*data for wilson plot*/
	protected Double resolution;
	
	protected Double wilsonPlot;
	
	/*data for cumulative intensity distribution */
	protected Double z;
	protected Double acent_theor;
	protected Double acent_twin;
	protected Double acent_obser;
	protected Double centric_theor;
	protected Double centric_obser;

	private String fileName;

	private int autoProcProgramId;



	public int getAutoProcProgramId() {
		return autoProcProgramId;
	}

	public String getFileName() {
		return fileName;
	}

	public AutoProcessingData(String integer, double d, double e, double f, double g, double h, double i, int j, Integer autoProcProgramId) {
		super();
	}

	public AutoProcessingData(String autoProcProgramAttachmentId, Double resolutionLimit, Double completeness,
			Double rFactorObserved, Double iSigma, Double cc2, Double sigAno, Integer anomalCorr, String fileName, Integer autoProcProgramId) {
		super();
		this.autoProcProgramAttachmentId = autoProcProgramAttachmentId;
		this.resolutionLimit = resolutionLimit;
		this.completeness = completeness;
		this.rFactorObserved = rFactorObserved;
		this.iSigma = iSigma;
		this.cc2 = cc2;
		this.sigAno = sigAno;
		this.anomalCorr = anomalCorr;
		this.resolution = null;
		this.wilsonPlot = null;
		this.z = null;
		this.acent_theor = null;
		this.acent_twin = null;
		this.acent_obser = null;
		this.centric_theor = null;
		this.centric_obser = null;
		this.fileName = fileName;
	}
	
	
	public AutoProcessingData(String autoProcProgramAttachmentId, Double z, Double acent_theor, Double acent_twin,
			Double acent_obser, Double centric_theor, Double centric_obser, String fileName, Integer autoProcProgramId) {
		super();
		this.autoProcProgramAttachmentId = autoProcProgramAttachmentId;
		this.resolutionLimit = null;
		this.completeness = null;
		this.rFactorObserved = null;
		this.iSigma = null;
		this.cc2 = null;
		this.sigAno = null;
		this.anomalCorr = null;
		this.resolution = null;
		this.wilsonPlot = null;
		this.z = z;
		this.acent_theor = acent_theor;
		this.acent_twin = acent_twin;
		this.acent_obser = acent_obser;
		this.centric_theor = centric_theor;
		this.centric_obser = centric_obser;
		this.fileName = fileName;
	}

	/**
	 * Constructor  for Wilson Chart
	 * @param resolution
	 * @param wilsonPlot
	 * @param  autoProcProgramId 
	 */
	public AutoProcessingData(String autoProcProgramAttachmentId, Double resolution, Double wilsonPlot, String fileName, Integer autoProcProgramId) {
		super();
		this.autoProcProgramAttachmentId = autoProcProgramAttachmentId ;
		this.resolutionLimit = null;
		this.completeness = null;
		this.rFactorObserved = null;
		this.iSigma = null;
		this.cc2 = null;
		this.sigAno = null;
		this.anomalCorr = null;
		this.resolution = resolution;
		this.wilsonPlot = wilsonPlot;
		this.z = null;
		this.acent_theor = null;
		this.acent_twin = null;
		this.acent_obser = null;
		this.centric_theor = null;
		this.centric_obser = null;
		this.fileName = fileName;
		this.autoProcProgramId = autoProcProgramId;
	}

	public Double getResolutionLimit() {
		return resolutionLimit;
	}

	public void setResolutionLimit(Double resolutionLimit) {
		this.resolutionLimit = resolutionLimit;
	}

	public Double getCompleteness() {
		return completeness;
	}

	public void setCompleteness(Double completeness) {
		this.completeness = completeness;
	}

	public Double getrFactorObserved() {
		return rFactorObserved;
	}

	public void setrFactorObserved(Double rFactorObserved) {
		this.rFactorObserved = rFactorObserved;
	}

	public Double getiSigma() {
		return iSigma;
	}

	public void setiSigma(Double iSigma) {
		this.iSigma = iSigma;
	}

	public Double getCc2() {
		return cc2;
	}

	public void setCc2(Double cc2) {
		this.cc2 = cc2;
	}

	public Double getSigAno() {
		return sigAno;
	}

	public void setSigAno(Double sigAno) {
		this.sigAno = sigAno;
	}

	public Integer getAnomalCorr() {
		return anomalCorr;
	}

	public void setAnomalCorr(Integer anomalCorr) {
		this.anomalCorr = anomalCorr;
	}

	public Double getResolution() {
		return resolution;
	}

	public void setResolution(Double resolution) {
		this.resolution = resolution;
	}

	public Double getWilsonPlot() {
		return wilsonPlot;
	}

	public void setWilsonPlot(Double wilsonPlot) {
		this.wilsonPlot = wilsonPlot;
	}

	public Double getZ() {
		return z;
	}

	public void setZ(Double z) {
		this.z = z;
	}

	public Double getAcent_theor() {
		return acent_theor;
	}

	public void setAcent_theor(Double acent_theor) {
		this.acent_theor = acent_theor;
	}

	public Double getAcent_twin() {
		return acent_twin;
	}

	public void setAcent_twin(Double acent_twin) {
		this.acent_twin = acent_twin;
	}

	public Double getAcent_obser() {
		return acent_obser;
	}

	public void setAcent_obser(Double acent_obser) {
		this.acent_obser = acent_obser;
	}

	public Double getCentric_theor() {
		return centric_theor;
	}

	public void setCentric_theor(Double centric_theor) {
		this.centric_theor = centric_theor;
	}

	public Double getCentric_obser() {
		return centric_obser;
	}

	public void setCentric_obser(Double centric_obser) {
		this.centric_obser = centric_obser;
	}

	public String getAutoProcProgramAttachmentId() {
		return autoProcProgramAttachmentId;
	}

	public void setAutoProcProgramAttachmentId(String autoProcProgramAttachmentId) {
		this.autoProcProgramAttachmentId = autoProcProgramAttachmentId;
	}
	
	
}
