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
 * ViewSampleForm.java
 * @author ludovic.launer@esrf.fr
 * Jan 3, 2005
 * Modified solange.delageniere@esrf.fr
 * Jan 11, 2005
 */

package ispyb.client.mx.sample;

import ispyb.common.util.Constants;
import ispyb.server.common.vos.proposals.Laboratory3VO;
import ispyb.server.common.vos.proposals.Person3VO;
import ispyb.server.common.vos.shipping.Container3VO;
import ispyb.server.common.vos.shipping.Dewar3VO;
import ispyb.server.common.vos.shipping.Shipping3VO;
import ispyb.server.mx.vos.sample.BLSample3VO;
import ispyb.server.mx.vos.sample.Crystal3VO;
import ispyb.server.mx.vos.sample.DiffractionPlan3VO;
import ispyb.server.mx.vos.sample.ExperimentKindDetails3VO;
import ispyb.server.mx.vos.sample.Protein3VO;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.struts.action.ActionForm;

/**
 * @struts.form name="viewSampleForm"
 */

public class ViewSampleForm extends ActionForm implements Serializable {
	static final long serialVersionUID = 0;

	private BLSample3VO info = new BLSample3VO(); // sample

	private Container3VO containerVO = new Container3VO();

	private Dewar3VO dewarVO = new Dewar3VO();

	private Shipping3VO shippingVO = new Shipping3VO();

	private Crystal3VO crystalVO = new Crystal3VO();

	private Protein3VO proteinVO = new Protein3VO();

	private Person3VO personLightValue = new Person3VO();

	private Laboratory3VO laboratoryLightValue = null;

	private Integer selectedSampleId = null;

	// lists
	private List<BLSample3VO> listInfo = new ArrayList<BLSample3VO>(); // Samples

	private List freeSampleList = new ArrayList(); // Free samples not linked to any container

	private List listCrystal = new ArrayList();

	private List listContainer = new ArrayList();

	private ArrayList<String> selectedSamplesList = new ArrayList<String>(); // Selected Samples

	private List listProtein = new ArrayList();

	private List listSpaceGroup = new ArrayList();

	private List listDMCodes = new ArrayList(); // DM codes detected by the SC

	private List listBeamlines = new ArrayList(); // Beamlines having DM codes detected by the SC

	private Integer theCrystalId = null;

	private Integer theProteinId;

	private Integer theContainerId = null;

	private String name;

	private String code;

	private String codeInScDate; // Date when the DM code was entered into the DB

	private String selectedBeamline; // Selected beamline to display DM codes

	private Integer editMode = new Integer(0); // By default the form is on create mode

	private String responsibleAddress;

	private String laboratoryAddress;

	private String acronym;

	private boolean[] hasDataCollection; // Sample has DataCollection

	private boolean[] hasDataCollectionGroup; // Sample has DataCollectionGroup, EnergyScan XRFSpectra
	
	private boolean[] hasSnapshot; // Sample has crystal snapshot

	// ------------------------------ Diffraction Plan -------------------------------------
	private DiffractionPlan3VO difPlanInfo = new DiffractionPlan3VO();

	private List listExperimentKind = new ArrayList();

	private List listDataCollectionType = new ArrayList();

	private List listDataCollectionKind = new ArrayList();

	private List<ExperimentKindDetails3VO> listExperimentKindDetails = new ArrayList<ExperimentKindDetails3VO>();

	private Integer defaultDiffractionPlanId = new Integer(Constants.DEFAULT_DIFFRACTION_PLAN_ID);

	private Integer numberOfWaveLength = new Integer(0);

	private ExperimentKindDetails3VO experimentKindDetail_1 = new ExperimentKindDetails3VO();

	private ExperimentKindDetails3VO experimentKindDetail_2 = new ExperimentKindDetails3VO();

	private ExperimentKindDetails3VO experimentKindDetail_3 = new ExperimentKindDetails3VO();

	private int nb_max_experiment_kind_detailsConstants = Constants.NB_MAX_EXPERIMENT_KIND_DETAILS;
	
	private boolean IsAllowedToCreateProtein  = false;

	// -------------------------------------------------------------------------------------

	public ViewSampleForm() {
		super();

		for (int i = 0; i < Constants.LIST_EXPERIMENT_KIND.length; i++) // Populate Experiment Kind
		{
			listExperimentKind.add(Constants.LIST_EXPERIMENT_KIND[i]);
		}
		for (int i = 0; i < Constants.DATA_COLLECTION_TYPE.length; i++) // Populate Datacollection Type
		{
			listDataCollectionType.add(Constants.DATA_COLLECTION_TYPE[i]);
		}
		for (int i = 0; i < Constants.DATA_COLLECTION_KIND.length; i++) // Populate Datacollection Kind
		{
			listDataCollectionKind.add(Constants.DATA_COLLECTION_KIND[i]);
		}
	}

	public ArrayList<String> getSelectedSamplesList() {
		return this.selectedSamplesList;
	}

	public String getSelectedSamplesList(int index) {
		if (selectedSamplesList.size() - 1 < index)
			return null;
		return selectedSamplesList.get(index);
	}

	public void setSelectedSamplesList(int index, String value) {
		if (selectedSamplesList.size() - 1 < index) {
			for (int i = selectedSamplesList.size(); i <= index; i++) {
				selectedSamplesList.add(i, new String());
			}
		}
		selectedSamplesList.set(index, value);
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCodeInScDate() {
		return codeInScDate;
	}

	public void setCodeInScDate(String codeInScDate) {
		this.codeInScDate = codeInScDate;
	}

	public String getSelectedBeamline() {
		return selectedBeamline;
	}

	public void setSelectedBeamline(String selectedBeamline) {
		this.selectedBeamline = selectedBeamline;
	}

	public Container3VO getContainerVO() {
		return containerVO;
	}

	public void setContainerVO(Container3VO containerVO) {
		this.containerVO = containerVO;
	}

	public Crystal3VO getCrystalVO() {
		return crystalVO;
	}

	public void setCrystalVO(Crystal3VO crystalVO) {

		DecimalFormat df1 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		df1.applyPattern("#####0.0");
		DecimalFormat df2 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		df2.applyPattern("#####0.00");

		if (crystalVO.getCellA() != null) {
			Double cellA = new Double(df2.format(crystalVO.getCellA()));
			crystalVO.setCellA(cellA);
		}

		if (crystalVO.getCellB() != null) {
			Double cellB = new Double(df2.format(crystalVO.getCellB()));
			crystalVO.setCellB(cellB);
		}

		if (crystalVO.getCellC() != null) {
			Double cellC = new Double(df2.format(crystalVO.getCellC()));
			crystalVO.setCellC(cellC);
		}

		if (crystalVO.getCellAlpha() != null) {
			Double cellAlpha = new Double(df1.format(crystalVO.getCellAlpha()));
			crystalVO.setCellAlpha(cellAlpha);
		}

		if (crystalVO.getCellBeta() != null) {
			Double cellBeta = new Double(df1.format(crystalVO.getCellBeta()));
			crystalVO.setCellBeta(cellBeta);
		}

		if (crystalVO.getCellGamma() != null) {
			Double cellGamma = new Double(df1.format(crystalVO.getCellGamma()));
			crystalVO.setCellGamma(cellGamma);
		}

		this.crystalVO = crystalVO;
	}

	public Dewar3VO getDewarVO() {
		return dewarVO;
	}

	public void setDewarVO(Dewar3VO dewarVO) {
		this.dewarVO = dewarVO;
	}

	
	public Protein3VO getProteinVO() {
		return proteinVO;
	}

	public void setProteinVO(Protein3VO proteinVO) {
		this.proteinVO = proteinVO;
	}

	public Person3VO getPersonLightValue() {
		return personLightValue;
	}

	public void setPersonLightValue(Person3VO personLightValue) {
		this.personLightValue = personLightValue;
	}

	public Laboratory3VO getLaboratoryLightValue() {
		return laboratoryLightValue;
	}

	public void setLaboratoryLightValue(Laboratory3VO laboratoryLightValue) {
		this.laboratoryLightValue = laboratoryLightValue;
	}

	public Shipping3VO getShippingVO() {
		return shippingVO;
	}

	public void setShippingVO(Shipping3VO shippingVO) {
		this.shippingVO = shippingVO;
	}

	public BLSample3VO getInfo() {
		return info;
	}

	public void setInfo(BLSample3VO info) {

		DecimalFormat df1 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		df1.applyPattern("#####0.0");

		if (info.getHolderLength() != null) {
			Double holderLength = new Double(df1.format(info.getHolderLength()));
			info.setHolderLength(holderLength);
		}

		this.info = info;
	}

	public List getListCrystal() {
		return listCrystal;
	}

	public void setListCrystal(List listCrystal) {
		this.listCrystal = listCrystal;
	}

	public List<BLSample3VO> getListInfo() {
		return listInfo;
	}

	public void setListInfo(List<BLSample3VO> listInfo) {
		this.listInfo = listInfo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSelectedSampleId() {
		return selectedSampleId;
	}

	public void setSelectedSampleId(Integer selectedSampleId) {
		this.selectedSampleId = selectedSampleId;
	}

	public Integer getEditMode() {
		return editMode;
	}

	public void setEditMode(Integer editMode) {
		this.editMode = editMode;
	}

	public String getResponsibleAddress() {
		return responsibleAddress;
	}

	public void setResponsibleAddress(String givenName, String familyName, String emailAddress, String phoneNumber) {

		StringBuffer buf = new StringBuffer();
		if (givenName != null) {
			buf.append(givenName).append(" ");
		}
		if (familyName != null) {
			buf.append(familyName).append(" ");
		}
		if (emailAddress != null) {
			buf.append(emailAddress).append(" ");
		}
		if (phoneNumber != null) {
			buf.append(phoneNumber);
		}

		this.responsibleAddress = buf.toString();
	}

	public String getLaboratoryAddress() {
		return laboratoryAddress;
	}

	public void setLaboratoryAddress(String name, String address, String city, String country) {

		StringBuffer buf = new StringBuffer();
		if (name != null) {
			buf.append(name).append("<BR>");
		}
		if (address != null) {
			buf.append(address).append("&nbsp;");
		}
		if (city != null) {
			buf.append(city).append("&nbsp;");
		}
		if (country != null) {
			buf.append(country);
		}

		this.laboratoryAddress = buf.toString();
	}

	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	public List getListProtein() {
		return listProtein;
	}

	public void setListProtein(List listProtein) {
		this.listProtein = listProtein;
	}

	public List getListContainer() {
		return listContainer;
	}

	public void setListContainer(List listContainer) {
		this.listContainer = listContainer;
	}

	public List getListSpaceGroup() {
		return listSpaceGroup;
	}

	public void setListSpaceGroup(List listSpaceGroup) {
		this.listSpaceGroup = listSpaceGroup;
	}

	public List getListDMCodes() {
		return listDMCodes;
	}

	public void setListDMCodes(List listDMCodes) {
		this.listDMCodes = listDMCodes;
	}

	public List getListBeamlines() {
		return listBeamlines;
	}

	public void setListBeamlines(List listBeamlines) {
		this.listBeamlines = listBeamlines;
	}

	public List getListExperimentKind() {
		return listExperimentKind;
	}

	public void setListExperimentKind(List listExperimentKind) {
		this.listExperimentKind = listExperimentKind;
	}

	public Integer getDefaultDiffractionPlanId() {
		return defaultDiffractionPlanId;
	}

	public void setDefaultDiffractionPlanId(Integer defaultDiffractionPlanId) {
		this.defaultDiffractionPlanId = defaultDiffractionPlanId;
	}

	public Integer getTheCrystalId() {
		return theCrystalId;
	}

	public void setTheCrystalId(Integer theCrystalId) {
		this.theCrystalId = theCrystalId;
	}

	public Integer getTheProteinId() {
		return theProteinId;
	}

	public void setTheProteinId(Integer theProteinId) {
		this.theProteinId = theProteinId;
	}

	public Integer getTheContainerId() {
		return theContainerId;
	}

	public void setTheContainerId(Integer theContainerId) {
		this.theContainerId = theContainerId;
	}

	public List getFreeSampleList() {
		return freeSampleList;
	}

	public void setFreeSampleList(List freeSampleList) {
		this.freeSampleList = freeSampleList;
	}

	// ------------------------------ Diffraction Plan -------------------------------------
	public DiffractionPlan3VO getDifPlanInfo() {
		return difPlanInfo;
	}

	public void setDifPlanInfo(DiffractionPlan3VO difPlanInfo2) {

		DecimalFormat df0 = (DecimalFormat) DecimalFormat.getIntegerInstance();
		df0.applyPattern("#####0");
		DecimalFormat df1 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		df1.applyPattern("#####0.0");

		if (difPlanInfo2.getObservedResolution() != null) {
			Double observedResolution = new Double(df1.format(difPlanInfo2.getObservedResolution()));
			difPlanInfo2.setObservedResolution(observedResolution);
		}

		if (difPlanInfo2.getMinimalResolution() != null) {
			Double minimalResolution = new Double(df1.format(difPlanInfo2.getMinimalResolution()));
			difPlanInfo2.setMinimalResolution(minimalResolution);
		}
		
		if (difPlanInfo2.getRequiredResolution() != null) {
			Double requiredResolution = new Double(df1.format(difPlanInfo2.getRequiredResolution()));
			difPlanInfo2.setRequiredResolution(requiredResolution);
		}

		if (difPlanInfo2.getOscillationRange() != null) {
			Double oscillationRange = new Double(df1.format(difPlanInfo2.getOscillationRange()));
			difPlanInfo2.setOscillationRange(oscillationRange);
		}

		if (difPlanInfo2.getPreferredBeamSizeX() != null) {
			Double preferredBeamSizeX = new Double(df0.format(difPlanInfo2.getPreferredBeamSizeX()));
			difPlanInfo2.setPreferredBeamSizeX(preferredBeamSizeX);
		}

		if (difPlanInfo2.getPreferredBeamSizeY() != null) {
			Double preferredBeamSizeY = new Double(df0.format(difPlanInfo2.getPreferredBeamSizeY()));
			difPlanInfo2.setPreferredBeamSizeY(preferredBeamSizeY);
		}

		this.difPlanInfo = difPlanInfo2;
	}

	public void setSelectedSamplesList(ArrayList<String> selectedSamplesList) {
		this.selectedSamplesList = selectedSamplesList;
	}

	public List getListDataCollectionKind() {
		return listDataCollectionKind;
	}

	public void setListDataCollectionKind(List listDataCollectionKind) {
		this.listDataCollectionKind = listDataCollectionKind;
	}

	public List getListDataCollectionType() {
		return listDataCollectionType;
	}

	public void setListDataCollectionType(List listDataCollectionType) {
		this.listDataCollectionType = listDataCollectionType;
	}

	public Integer getNumberOfWaveLength() {
		return numberOfWaveLength;
	}

	public void setNumberOfWaveLength(Integer numberOfWaveLength) {
		this.numberOfWaveLength = numberOfWaveLength;
	}

	public List<ExperimentKindDetails3VO> getListExperimentKindDetails() {
		return listExperimentKindDetails;
	}

	public void setListExperimentKindDetails(List<ExperimentKindDetails3VO> listExperimentKindDetails) {
		this.listExperimentKindDetails = listExperimentKindDetails;
	}

	public ExperimentKindDetails3VO getExperimentKindDetail_1() {
		return experimentKindDetail_1;
	}

	public void setExperimentKindDetail_1(ExperimentKindDetails3VO experimentKindDetail_1) {
		this.experimentKindDetail_1 = experimentKindDetail_1;
	}

	public ExperimentKindDetails3VO getExperimentKindDetail_2() {
		return experimentKindDetail_2;
	}

	public void setExperimentKindDetail_2(ExperimentKindDetails3VO experimentKindDetail_2) {
		this.experimentKindDetail_2 = experimentKindDetail_2;
	}

	public ExperimentKindDetails3VO getExperimentKindDetail_3() {
		return experimentKindDetail_3;
	}

	public void setExperimentKindDetail_3(ExperimentKindDetails3VO experimentKindDetail_3) {
		this.experimentKindDetail_3 = experimentKindDetail_3;
	}

	public int getNb_max_experiment_kind_detailsConstants() {
		return nb_max_experiment_kind_detailsConstants;
	}

	public void setNb_max_experiment_kind_detailsConstants(int nb_max_experiment_kind_detailsConstants) {
		this.nb_max_experiment_kind_detailsConstants = nb_max_experiment_kind_detailsConstants;
	}

	/**
	 * @return Returns the nbSamples.
	 */
	public Integer getNbSamples() {
		return this.listInfo.size();
	}

	public boolean[] getHasSnapshot() {
		return hasSnapshot;
	}

	public void setHasSnapshot(boolean[] hasSnapshot) {
		this.hasSnapshot = hasSnapshot;
	}

	public boolean[] getHasDataCollection() {
		return hasDataCollection;
	}

	public void setHasDataCollection(boolean[] hasDataCollection) {
		this.hasDataCollection = hasDataCollection;
	}

	public boolean[] getHasDataCollectionGroup() {
		return hasDataCollectionGroup;
	}

	public void setHasDataCollectionGroup(boolean[] hasDataCollectionGroup) {
		this.hasDataCollectionGroup = hasDataCollectionGroup;
	}

	public boolean isIsAllowedToCreateProtein() {
		return IsAllowedToCreateProtein;
	}

	public void setIsAllowedToCreateProtein(boolean isAllowedToCreateProtein) {
		IsAllowedToCreateProtein = isAllowedToCreateProtein;
	}

}
