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
 * ViewCrystalForm
 */

package ispyb.client.mx.sample;

import ispyb.common.util.DBTools;
import ispyb.common.util.Constants;
import ispyb.server.mx.vos.autoproc.GeometryClassname3VO;
import ispyb.server.mx.vos.autoproc.SpaceGroup3VO;
import ispyb.server.mx.vos.sample.Crystal3VO;
import ispyb.server.mx.vos.sample.DiffractionPlan3VO;
import ispyb.server.mx.vos.sample.Protein3VO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.apache.struts.action.ActionForm;

/**
 * @struts.form name="viewCrystalForm"
 */

public class ViewCrystalForm extends ActionForm implements Serializable {
	static final long serialVersionUID = 0;

	private Crystal3VO info = new Crystal3VO();

	private Protein3VO proteinInfo = new Protein3VO();

	private DiffractionPlan3VO difPlanInfo = new DiffractionPlan3VO();

	private List<String> listExperimentKind = new ArrayList<String>();

	private List listInfo = new ArrayList();

	private List listProtein = new ArrayList();

	private final Hashtable selectedProteins = new Hashtable();

	private Integer theProteinId = new Integer(0);

	private Integer defaultDiffractionPlanId = new Integer(Constants.DEFAULT_DIFFRACTION_PLAN_ID);

	private String acronym = new String();

	private List<CrystalInfo> listGeometryClass = new ArrayList<CrystalInfo>();

	private String geometryClass = new String();

	private String spaceGroup = new String();

	private boolean isAllowedTocreateProtein = false;

	// Export information to PocketSample
	private String exportedContent = null;

	private String exportedContentUrl = null;

	public ViewCrystalForm() {
		super();

		// Populate SpaceGroups information
		List<GeometryClassname3VO> listGeometryClassname = DBTools.getAllowedGeometryClassname();
		if (listGeometryClassname != null){
				for (Iterator<GeometryClassname3VO> iterator = listGeometryClassname.iterator(); iterator.hasNext();) {
					GeometryClassname3VO geometryClassname3VO = (GeometryClassname3VO) iterator.next();
					CrystalInfo ci = new CrystalInfo(geometryClassname3VO.getGeometryClassname());
					List<SpaceGroup3VO> listSpaceGroup = geometryClassname3VO.getSpaceGroupsList();
					for (Iterator<SpaceGroup3VO> iterator2 = listSpaceGroup.iterator(); iterator2.hasNext();) {
						SpaceGroup3VO spaceGroup3VO = (SpaceGroup3VO) iterator2.next();
						ci.addGeometryLambda(spaceGroup3VO.getSpaceGroupShortName());
					}
					this.listGeometryClass.add(ci);
				}
		}
		
		

		CrystalInfo ci = new CrystalInfo("Unknown");
		this.listGeometryClass.add(ci);

		listExperimentKind.add("");
		for (int i = 0; i < Constants.LIST_EXPERIMENT_KIND.length; i++) // Populate Experiment Kind
		{
			listExperimentKind.add(Constants.LIST_EXPERIMENT_KIND[i]);
		}

	}

	// ______________________________________________________________________________________________________________________
	public List getListInfo() {
		return listInfo;
	}

	public void setListInfo(List listInfo) {
		this.listInfo = listInfo;
	}

	public List getListProtein() {
		return listProtein;
	}

	public void setListProtein(List listProtein) {
		this.listProtein = listProtein;
	}

	public Crystal3VO getInfo() {
		return info;
	}

	public void setInfo(Crystal3VO info) {
		this.info = info;
	}

	public Integer getSelectedProteins(int i) {
		return (Integer) this.selectedProteins.get(new Integer(i));
	}

	public void setSelectedProteins(int i, Integer value) {
		this.selectedProteins.put(new Integer(i), value);
	}

	public Hashtable getSelectedProteins() {
		return this.selectedProteins;
	}

	public String getExportedContent() {
		return exportedContent;
	}

	public void setExportedContent(String exportedContent) {
		this.exportedContent = exportedContent;
	}

	public String getExportedContentUrl() {
		return exportedContentUrl;
	}

	public void setExportedContentUrl(String exportedContentUrl) {
		this.exportedContentUrl = exportedContentUrl;
	}

	public Protein3VO getProteinInfo() {
		return proteinInfo;
	}

	public void setProteinInfo(Protein3VO proteinInfo) {
		this.proteinInfo = proteinInfo;
	}

	public DiffractionPlan3VO getDifPlanInfo() {
		return difPlanInfo;
	}

	public void setDifPlanInfo(DiffractionPlan3VO difPlanInfo) {
		this.difPlanInfo = difPlanInfo;
	}

	public List getListExperimentKind() {
		return listExperimentKind;
	}

	public void setListExperimentKind(List listExperimentKind) {
		this.listExperimentKind = listExperimentKind;
	}

	public Integer getTheProteinId() {
		return theProteinId;
	}

	public void setTheProteinId(Integer theProteinId) {
		this.theProteinId = theProteinId;
	}

	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	public Integer getDefaultDiffractionPlanId() {
		return defaultDiffractionPlanId;
	}

	public void setDefaultDiffractionPlanId(Integer defaultDiffractionPlanId) {
		this.defaultDiffractionPlanId = defaultDiffractionPlanId;
	}

	public String getGeometryClass() {
		return geometryClass;
	}

	public void setGeometryClass(String geometryClass) {
		this.geometryClass = geometryClass;
	}

	public String getSpaceGroup() {
		return spaceGroup;
	}

	public void setSpaceGroup(String spaceGroup) {
		this.spaceGroup = spaceGroup;
	}

	public boolean isAllowedTocreateProtein() {
		return isAllowedTocreateProtein;
	}

	public void setAllowedTocreateProtein(boolean isAllowedTocreateProtein) {
		this.isAllowedTocreateProtein = isAllowedTocreateProtein;
	}

	public List getListGeometryClass() {
		return listGeometryClass;
	}

	public void setListGeometryClass(List listGeometryClass) {
		this.listGeometryClass = listGeometryClass;
	}

}
