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

package ispyb.server.common.vos.shipping;

import java.util.Date;

/**
 * Container class for webservices
 * @author 
 *
 */
public class ContainerWS3VO extends Container3VO {
	private static final long serialVersionUID = 6010853140528349178L;
	
	protected Integer containerId;

	protected String code;

	protected String containerType;

	protected Integer capacity;

	protected String beamlineLocation;

	protected String sampleChangerLocation;

	protected String containerStatus;

	protected Date timeStamp;
	
	protected String barcode;

	public ContainerWS3VO() {
		super();
	}
	
	public ContainerWS3VO(Container3VO vo){
		this.barcode = vo.getBarcode();
		this.containerId = vo.getContainerId();
		this.code = vo.getCode();
		this.containerType = vo.getContainerType();
		this.capacity = vo.getCapacity();
		this.beamlineLocation = vo.getBeamlineLocation();
		this.sampleChangerLocation = vo.getSampleChangerLocation();
		this.containerStatus = vo.getContainerStatus();

	}

	public Integer getContainerId() {
		return containerId;
	}

	public void setContainerId(Integer containerId) {
		this.containerId = containerId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getContainerType() {
		return containerType;
	}

	public void setContainerType(String containerType) {
		this.containerType = containerType;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	public String getBeamlineLocation() {
		return beamlineLocation;
	}

	public void setBeamlineLocation(String beamlineLocation) {
		this.beamlineLocation = beamlineLocation;
	}

	public String getSampleChangerLocation() {
		return sampleChangerLocation;
	}

	public void setSampleChangerLocation(String sampleChangerLocation) {
		this.sampleChangerLocation = sampleChangerLocation;
	}

	public String getContainerStatus() {
		return containerStatus;
	}

	public void setContainerStatus(String containerStatus) {
		this.containerStatus = containerStatus;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	
}
