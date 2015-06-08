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
 * ViewContainerForm.java
 * @author ludovic.launer@esrf.fr
 * Dec 17, 2004
 */

package ispyb.client.mx.container;

import ispyb.common.util.StringUtils;
import ispyb.server.common.vos.shipping.Container3VO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import org.apache.struts.action.ActionForm;

/**
 * 
 * @struts.form name="viewContainerForm"
 */
public class ViewContainerForm extends ActionForm implements Serializable {
	static final long serialVersionUID = 0;

	private ArrayList containersInfo = new ArrayList();

	private ArrayList freeContainerList = new ArrayList();

	private Container3VO Container3VO = new Container3VO();

	private String beamlineLocation;

	private String sampleChangerLocation;

	private Integer theContainerId;

	private String[] containerIdList;

	private String[] shipmentNameList;

	private Date[] creationDateList;

	private Date[] experimentDateList;

	private Integer[] nbSampleList;

	private String[] beamlineLocationList;

	private String[] sampleChangerLocationList;

	/**
	 * Constructor
	 */
	public ViewContainerForm() {

	}

	/**
	 * @return Returns the serialVersionUID.
	 */
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	/**
	 * @return Returns the containersInfo.
	 */
	public ArrayList getContainersInfo() {
		return containersInfo;
	}

	/**
	 * @param containersInfo
	 *            The containersInfo to set.
	 */
	public void setContainersInfo(ArrayList containersInfo) {
		this.containersInfo = containersInfo;
	}

	/**
	 * @return Returns the Container3VO.
	 */
	public Container3VO getContainer3VO() {
		return Container3VO;
	}

	/**
	 * @param Container3VO
	 *            The Container3VO to set.
	 */
	public void setContainer3VO(Container3VO Container3VO) {
		this.Container3VO = Container3VO;
	}

	/**
	 * Auxiliar to pass arrays in java to javaScript
	 * 
	 * @return
	 */
	public String getCapacitiesAsString() {
		String result = "(";

		for (int i = 0; i < containersInfo.size(); i++) {
			ContainerDO cont = (ContainerDO) containersInfo.get(i);
			result = result + cont.getCapacity() + ",";
		}
		return result.substring(0, result.lastIndexOf(',')) + ")";

	}

	/**
	 * @return Returns the freeContainerList.
	 */
	public ArrayList getFreeContainerList() {
		return freeContainerList;
	}

	/**
	 * @param freeContainerList
	 *            The freeContainerList to set.
	 */
	public void setFreeContainerList(ArrayList freeContainerList) {
		this.freeContainerList = freeContainerList;
	}

	/**
	 * @return Returns the theContainerId.
	 */
	public Integer getTheContainerId() {
		return theContainerId;
	}

	/**
	 * @param theContainerId
	 *            The theContainerId to set.
	 */
	public void setTheContainerId(Integer theContainerId) {
		this.theContainerId = theContainerId;
	}

	/**
	 * @return Returns the sampleChangerLocation.
	 */
	public String getSampleChangerLocation() {
		return sampleChangerLocation;
	}

	/**
	 * @param sampleChangerLocation
	 *            The sampleChangerLocation to set.
	 */
	public void setSampleChangerLocation(String sampleChangerLocation) {
		this.sampleChangerLocation = sampleChangerLocation;
	}

	public String getBeamlineLocation() {
		return beamlineLocation;
	}

	public void setBeamlineLocation(String beamlineLocation) {
		this.beamlineLocation = beamlineLocation;
	}

	public String[] getContainerIdList() {
		return containerIdList;
	}

	public void setContainerIdList(String[] containerIdList) {
		this.containerIdList = containerIdList;
	}

	public String getContainerIdList(int index) {
		return StringUtils.getArrayElement(index, containerIdList);
	}

	public void setContainerIdList(int index, String value) {
		String[] array = StringUtils.setArrayElement(value, index, containerIdList);
		containerIdList = array;
	}

	public String[] getBeamlineLocationList() {
		return beamlineLocationList;
	}

	public void setBeamlineLocationList(String[] beamlineLocationList) {
		this.beamlineLocationList = beamlineLocationList;
	}

	public String getBeamlineLocationList(int index) {
		return StringUtils.getArrayElement(index, beamlineLocationList);
	}

	public void setBeamlineLocationList(int index, String value) {
		String[] array = StringUtils.setArrayElement(value, index, beamlineLocationList);
		beamlineLocationList = array;
	}

	public String[] getSampleChangerLocationList() {
		return sampleChangerLocationList;
	}

	public void setSampleChangerLocationList(String[] sampleChangerLocationList) {
		this.sampleChangerLocationList = sampleChangerLocationList;
	}

	public String getSampleChangerLocationList(int index) {
		return StringUtils.getArrayElement(index, sampleChangerLocationList);
	}

	public void setSampleChangerLocationList(int index, String value) {
		String[] array = StringUtils.setArrayElement(value, index, sampleChangerLocationList);
		sampleChangerLocationList = array;
	}

	public String[] getShipmentNameList() {
		return shipmentNameList;
	}

	public void setShipmentNameList(String[] shipmentNameList) {
		this.shipmentNameList = shipmentNameList;
	}

	public Date[] getCreationDateList() {
		return creationDateList;
	}

	public void setCreationDateList(Date[] creationDateList) {
		this.creationDateList = creationDateList;
	}

	public Date[] getExperimentDateList() {
		return experimentDateList;
	}

	public void setExperimentDateList(Date[] experimentDateList) {
		this.experimentDateList = experimentDateList;
	}

	public Integer[] getNbSampleList() {
		return nbSampleList;
	}

	public void setNbSampleList(Integer[] nbSampleList) {
		this.nbSampleList = nbSampleList;
	}
}
