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


package ispyb.server.mx.vos.collections;

/**
 * Image class for webservices
 * @author 
 *
 */
public class ImageWS3VO extends Image3VO {
	private static final long serialVersionUID = -5923235540984966756L;
	
	private Integer dataCollectionId;
	
	private Integer motorPositionId;

	public ImageWS3VO() {
		super();
	}

	public ImageWS3VO(Integer dataCollectionId, Integer motorPositionId) {
		super();
		this.dataCollectionId = dataCollectionId;
		this.motorPositionId =  motorPositionId;
	}

	public Integer getDataCollectionId() {
		return dataCollectionId;
	}

	public void setDataCollectionId(Integer dataCollectionId) {
		this.dataCollectionId = dataCollectionId;
	}
	
	public Integer getMotorPositionId() {
		return motorPositionId;
	}

	public void setMotorPositionId(Integer motorPositionId) {
		this.motorPositionId = motorPositionId;
	}

	@Override
	public String toWSString(){
		String s= super.toWSString();
		s += ", dataCollectionId="+this.dataCollectionId;
		s += ", motorPositionId="+this.motorPositionId;
		return s;
	}
	
}
