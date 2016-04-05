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

import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.mx.services.autoproc.AutoProcProgram3Service;
import ispyb.server.mx.vos.autoproc.AutoProc3VO;
import ispyb.server.mx.vos.autoproc.AutoProcProgram3VO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.CreateException;

import org.apache.struts.action.ActionForm;

/**
 * @struts.form name="getAutoProcListForm"
 */

public class GetAutoProcListForm extends ActionForm implements Serializable {

	static final long serialVersionUID = 0;

	private String rmerge;

	private String isigma;

	private String dataCollectionId;

	private Collection<AutoProc3VO> autoProcs;

	private Collection<String> autoProcLabels = null;

	private Collection<String> autoProcIdLabels = null;

	private String selectedAutoProcLabel;

	private String selectedAutoProc;

	private boolean anomalous = false;

	private int nbAutoProc = 0;
	
	private int nbRemoved = 0;

	public String getRmerge() {
		return rmerge;
	}

	public void setRmerge(String merge) {
		rmerge = merge;
	}

	public String getIsigma() {
		return isigma;
	}

	public void setIsigma(String sigma) {
		isigma = sigma;
	}

	public String getDataCollectionId() {
		return dataCollectionId;
	}

	public void setDataCollectionId(String dataCollectionId) {
		this.dataCollectionId = dataCollectionId;
	}

	public Collection<AutoProc3VO> getAutoProcs() {
		return autoProcs;
	}

	public void setAutoProcs(Collection<AutoProc3VO> autoProcs) {
		this.autoProcs = autoProcs;
		updateAutoProcLabels();
		updateAutoProcIdLabels();
	}

	// TODO remove this from the form and put it in action
	private void updateAutoProcLabels() {
		AutoProcProgram3Service program_facade = null;
		try {
			program_facade = (AutoProcProgram3Service) Ejb3ServiceLocator.getInstance().getLocalService(
					AutoProcProgram3Service.class);

			this.autoProcLabels = new ArrayList<String>();

			if (autoProcs == null)
				return;

			for (java.util.Iterator<AutoProc3VO> i = this.autoProcs.iterator(); i.hasNext();) {
				AutoProc3VO apv = i.next();
				if (apv == null)
					continue;
				double refinedCellA = ((double) ((int) (apv.getRefinedCellA() * 10))) / 10; // round to 1 dp
				double refinedCellB = ((double) ((int) (apv.getRefinedCellB() * 10))) / 10; // round to 1 dp
				double refinedCellC = ((double) ((int) (apv.getRefinedCellC() * 10))) / 10; // round to 1 dp

				String label = apv.getSpaceGroup() + ": " + refinedCellA + ", " + refinedCellB + ", " + refinedCellC
						+ " (" + apv.getRefinedCellAlpha() + ", " + apv.getRefinedCellBeta() + ", "
						+ apv.getRefinedCellGamma() + ")";
				String cmdLine = "";

				AutoProcProgram3VO autoProcProgram = program_facade
						.findByPk(apv.getAutoProcProgramVOId(), false);
				if (autoProcProgram != null && autoProcProgram.getProcessingCommandLine() != null) {
					String cmdLine2 = autoProcProgram.getProcessingCommandLine();
					if (cmdLine2.length() > 20)
						cmdLine2 = cmdLine2.substring(0, 20);
					cmdLine = cmdLine + cmdLine2 + "...: ";
				}

				autoProcLabels.add(cmdLine + label);
			}
		} catch (CreateException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public java.util.Collection<String> getAutoProcIdLabels() {
		return autoProcIdLabels;
	}

	public void setAutoProcIdLabels(java.util.Collection<String> autoProcIdLabels) {
		this.autoProcIdLabels = autoProcIdLabels;
	}

	private void updateAutoProcIdLabels() {
		this.autoProcIdLabels = new ArrayList<String>();

		if (autoProcs == null)
			return;

		for (java.util.Iterator<AutoProc3VO> i = this.autoProcs.iterator(); i.hasNext();) {

			AutoProc3VO apv = i.next();
			if (apv == null)
				continue;
			autoProcIdLabels.add("" + apv.getAutoProcId());
		}

	}

	public String getSelectedAutoProcLabel() {
		return selectedAutoProcLabel;
	}

	public void setSelectedAutoProcLabel(String selectedAutoProcLabel) {
		this.selectedAutoProcLabel = selectedAutoProcLabel;
	}

	public String getSelectedAutoProc() {
		return selectedAutoProc;
	}

	public void setSelectedAutoProc(String selectedAutoProc) {
		this.selectedAutoProc = selectedAutoProc;
	}

	public Collection<String> getAutoProcLabels() {
		return autoProcLabels;
	}

	public void setAutoProcLabels(Collection<String> autoProcLabels) {
		this.autoProcLabels = autoProcLabels;
	}

	public boolean getAnomalous() {
		return this.anomalous;
	}

	public void setAnomalous(boolean anomalous) {
		this.anomalous = anomalous;
	}

	public int getNbAutoProc() {
		return this.nbAutoProc;
	}

	public void setNbAutoProc(int nbAutoProc) {
		this.nbAutoProc = nbAutoProc;
	}

	public int getNbRemoved() {
		return nbRemoved;
	}

	public void setNbRemoved(int nbRemoved) {
		this.nbRemoved = nbRemoved;
	}

}
