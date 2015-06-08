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
package ispyb.server.mx.vos.autoproc;

import java.util.List;


/**
 * data used to fill phasing data
 * @author BODIN
 *
 */
public class PhasingData {
	// STEP cf CST.PHASING_STEP: preparePhasingData, SubStructureDetermination, Phasing, ModelBuilding
	protected Integer step;
	protected Integer autoProcScalingId;
	protected Integer datasetNumber;
	protected String spaceGroup;
	protected Double lowRes;
	protected Double highRes;
	protected PhasingProgramRun3VO phasingProgramRun; 
	protected List<PhasingProgramAttachmentWS3VO> listPhasingProgramAttachment;
	protected List<PhasingStatisticsWS3VO> listPhasingStatistics;
	protected String method;
	protected Double solventContent;
	protected Boolean enantiomorph;
	
	public PhasingData() {
		super();
	}

	public PhasingData(Integer step, Integer autoProcScalingId, Integer datasetNumber,
			String spaceGroup, Double lowRes, Double highRes,
			PhasingProgramRun3VO phasingProgramRun,
			List<PhasingProgramAttachmentWS3VO> listPhasingProgramAttachment,
			List<PhasingStatisticsWS3VO> listPhasingStatistics, String method,
			Double solventContent, Boolean enantiomorph) {
		super();
		this.step = step;
		this.autoProcScalingId = autoProcScalingId;
		this.datasetNumber = datasetNumber;
		this.spaceGroup = spaceGroup;
		this.lowRes = lowRes;
		this.highRes = highRes;
		this.phasingProgramRun = phasingProgramRun;
		this.listPhasingProgramAttachment = listPhasingProgramAttachment;
		this.listPhasingStatistics = listPhasingStatistics;
		this.method = method;
		this.solventContent = solventContent;
		this.enantiomorph = enantiomorph;
	}

	public Integer getStep() {
		return step;
	}

	public void setStep(Integer step) {
		this.step = step;
	}

	public Integer getAutoProcScalingId() {
		return autoProcScalingId;
	}

	public void setAutoProcScalingId(Integer autoProcScalingId) {
		this.autoProcScalingId = autoProcScalingId;
	}

	public Integer getDatasetNumber() {
		return datasetNumber;
	}

	public void setDatasetNumber(Integer datasetNumber) {
		this.datasetNumber = datasetNumber;
	}

	public String getSpaceGroup() {
		return spaceGroup;
	}

	public void setSpaceGroup(String spaceGroup) {
		this.spaceGroup = spaceGroup;
	}

	public Double getLowRes() {
		return lowRes;
	}

	public void setLowRes(Double lowRes) {
		this.lowRes = lowRes;
	}

	public Double getHighRes() {
		return highRes;
	}

	public void setHighRes(Double highRes) {
		this.highRes = highRes;
	}

	public PhasingProgramRun3VO getPhasingProgramRun() {
		return phasingProgramRun;
	}

	public void setPhasingProgramRun(PhasingProgramRun3VO phasingProgramRun) {
		this.phasingProgramRun = phasingProgramRun;
	}

	public List<PhasingProgramAttachmentWS3VO> getListPhasingProgramAttachment() {
		return listPhasingProgramAttachment;
	}

	public void setListPhasingProgramAttachment(
			List<PhasingProgramAttachmentWS3VO> listPhasingProgramAttachment) {
		this.listPhasingProgramAttachment = listPhasingProgramAttachment;
	}

	public List<PhasingStatisticsWS3VO> getListPhasingStatistics() {
		return listPhasingStatistics;
	}

	public void setListPhasingStatistics(
			List<PhasingStatisticsWS3VO> listPhasingStatistics) {
		this.listPhasingStatistics = listPhasingStatistics;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Double getSolventContent() {
		return solventContent;
	}

	public void setSolventContent(Double solventContent) {
		this.solventContent = solventContent;
	}

	public Boolean getEnantiomorph() {
		return enantiomorph;
	}

	public void setEnantiomorph(Boolean enantiomorph) {
		this.enantiomorph = enantiomorph;
	}
	
	public String toWSString(){
		return "step="+this.step+
		", autoProcScalingId="+this.autoProcScalingId+
		", datasetNumber="+this.datasetNumber+
		", spaceGroup="+this.spaceGroup+
		", lowRes="+this.lowRes+
		", highRes="+this.highRes+
		", method="+this.method+
		", solventContent="+this.solventContent+
		", enantiomorph="+this.enantiomorph+
		", phasingProgramRun="+(this.phasingProgramRun == null?"null":this.phasingProgramRun.toWSString())+
		", listPhasingProgramAttachment="+(this.listPhasingProgramAttachment==null?"null":this.listPhasingProgramAttachment.size())+
		", listPhasingStatistics="+(this.listPhasingStatistics==null?"null":this.listPhasingStatistics.size());
	}
	
	
	
}
