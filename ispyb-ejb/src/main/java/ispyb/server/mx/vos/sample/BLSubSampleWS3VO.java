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

package ispyb.server.mx.vos.sample;

/**
 * BLSubSample class used for webservices
 * 
 * @author BODIN
 * 
 */
public class BLSubSampleWS3VO extends BLSubSample3VO{
	private static final long serialVersionUID = 1L;
	
	private Integer blSampleId;

	private Integer positionId;
	
	private Integer position2Id;
	
	private Integer diffractionPlanId;

	public BLSubSampleWS3VO() {
		super();
	}
	
	public BLSubSampleWS3VO(BLSubSample3VO vo) {
		super(vo);
		this.blSampleId = vo.getBlSampleVO().getBlSampleId();
		this.positionId = (vo.getPositionVO() == null ? null: vo.getPositionVO().getPositionId());
		this.position2Id = (vo.getPosition2VO() == null ? null: vo.getPosition2VO().getPositionId());
		this.diffractionPlanId = (vo.getDiffractionPlanVO() == null ? null: vo.getDiffractionPlanVO().getDiffractionPlanId());
	}

	public BLSubSampleWS3VO(Integer blSampleId, Integer positionId, Integer position2Id, 
			Integer diffractionPlanId) {
		super();
		this.blSampleId = blSampleId;
		this.positionId = positionId;
		this.position2Id = position2Id;
		this.diffractionPlanId = diffractionPlanId;
	}

	public Integer getBlSampleId() {
		return blSampleId;
	}

	public void setBlSampleId(Integer blSampleId) {
		this.blSampleId = blSampleId;
	}

	public Integer getPositionId() {
		return positionId;
	}

	public void setPositionId(Integer positionId) {
		this.positionId = positionId;
	}

	public Integer getPosition2Id() {
		return position2Id;
	}

	public void setPosition2Id(Integer position2Id) {
		this.position2Id = position2Id;
	}

	public Integer getDiffractionPlanId() {
		return diffractionPlanId;
	}

	public void setDiffractionPlanId(Integer diffractionPlanId) {
		this.diffractionPlanId = diffractionPlanId;
	}
	
}
