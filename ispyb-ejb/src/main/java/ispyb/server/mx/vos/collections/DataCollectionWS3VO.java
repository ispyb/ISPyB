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
 * DataCollection class for webservices
 * @author 
 *
 */
public class DataCollectionWS3VO extends DataCollection3VO{
	
	private static final long serialVersionUID = -7565022988433720013L;

	private Integer dataCollectionGroupId;
	
	private Integer strategySubWedgeOrigId;
	
	private Integer detectorId;
	
	private Integer blSubSampleId;	
	
	public DataCollectionWS3VO() {
		super();
	}

	public DataCollectionWS3VO(DataCollection3VO vo) {
		super(vo);
	}


	public DataCollectionWS3VO(Integer sessionId, 
			Integer strategySubWedgeOrigId, Integer detectorId, Integer blSubSampleId) {
		super();
		this.dataCollectionGroupId = sessionId;
		this.strategySubWedgeOrigId = strategySubWedgeOrigId;
		this.detectorId = detectorId;
		this.blSubSampleId = blSubSampleId;
	}



	public Integer getDataCollectionGroupId() {
		return dataCollectionGroupId;
	}

	public void setDataCollectionGroupId(Integer dataCollectionGroupId) {
		this.dataCollectionGroupId = dataCollectionGroupId;
	}

	public Integer getDetectorId() {
		return detectorId;
	}

	public void setDetectorId(Integer detectorId) {
		this.detectorId = detectorId;
	}
	
	public Integer getBlSubSampleId() {
		return blSubSampleId;
	}

	public void setBlSubSampleId(Integer blSubSampleId) {
		this.blSubSampleId = blSubSampleId;
	}

	public Integer getStrategySubWedgeOrigId() {
		return strategySubWedgeOrigId;
	}

	public void setStrategySubWedgeOrigId(Integer strategySubWedgeOrigId) {
		this.strategySubWedgeOrigId = strategySubWedgeOrigId;
	}
	
	@Override
	public String toWSString(){
		String s= super.toWSString();
		s += ", dataCollectionGroupId="+this.dataCollectionGroupId+", "+
		"detectorId="+this.detectorId+", "+
		"blSubSampleId="+this.blSubSampleId+", "+
		"strategySubWedgeOrigId="+this.strategySubWedgeOrigId;
		return s;
	}

}
