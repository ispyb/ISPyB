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


package ispyb.server.mx.vos.screening;

import java.util.Date;


/**
 * Screening class for webservices
 * @author BODIN
 *
 */
public class ScreeningWS3VO extends Screening3VO{
	private static final long serialVersionUID = 5342032809035398856L;
	
	private Integer dataCollectionGroupId;
	
	private Integer diffractionPlanId;

	
	public ScreeningWS3VO() {
		super();
	}
	
	
	
	public ScreeningWS3VO(Integer dataCollectionGroupId, Integer diffractionPlanId) {
		super();
		this.dataCollectionGroupId = dataCollectionGroupId;
		this.diffractionPlanId = diffractionPlanId;
	}



	public ScreeningWS3VO(Screening3VO vo) {
		super(vo);
	}
	public ScreeningWS3VO(Integer screeningId, Integer dataCollectionGroupId, Integer diffractionPlanId,
			Date timeStamp, String programVersion, String comments,
			String shortComments, String xmlSampleInformation) {
		super();
		this.screeningId = screeningId;
		this.dataCollectionGroupId = dataCollectionGroupId;
		this.diffractionPlanId = diffractionPlanId;
		this.timeStamp = timeStamp;
		this.programVersion = programVersion;
		this.comments = comments;
		this.shortComments = shortComments;
		this.xmlSampleInformation = xmlSampleInformation;
	}



	public Integer getDiffractionPlanId() {
		return diffractionPlanId;
	}

	public void setDiffractionPlanId(Integer diffractionPlanId) {
		this.diffractionPlanId = diffractionPlanId;
	}

	public Integer getDataCollectionGroupId() {
		return dataCollectionGroupId;
	}
	
	public void setDataCollectionGroupId(Integer dataCollectionGroupId) {
		this.dataCollectionGroupId = dataCollectionGroupId;
	}

	@Override
	public String toWSString(){
		String s= super.toWSString();
		s += ", diffractionPlanId="+this.diffractionPlanId+", "+
		"dataCollectionGroupId="+this.dataCollectionGroupId;
		return s;
	}
	
	
}
