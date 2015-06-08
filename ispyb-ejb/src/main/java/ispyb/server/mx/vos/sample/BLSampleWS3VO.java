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
 * BLSample class used for webservices
 * 
 * @author BODIN
 * 
 */
public class BLSampleWS3VO extends BLSample3VO {

	private static final long serialVersionUID = 1L;

	private Integer crystalId;

	private Integer containerId;

	private Integer diffractionPlanId;

	public BLSampleWS3VO() {
		super();
	}

	public BLSampleWS3VO(BLSample3VO vo) {
		super(vo);
	}

	public Integer getCrystalId() {
		return crystalId;
	}

	public void setCrystalId(Integer crystalId) {
		this.crystalId = crystalId;
	}

	public Integer getContainerId() {
		return containerId;
	}

	public void setContainerId(Integer containerId) {
		this.containerId = containerId;
	}

	public Integer getDiffractionPlanId() {
		return diffractionPlanId;
	}

	public void setDiffractionPlanId(Integer diffractionPlanId) {
		this.diffractionPlanId = diffractionPlanId;
	}
	
	@Override
	public String toWSString(){
		String s= super.toWSString();
		s += ", crystalId="+this.crystalId+", "+
		"containerId="+this.containerId+", "+
		"diffractionPlanId="+this.diffractionPlanId;
		return s;
	}
	

}
