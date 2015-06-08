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
 * ScreeningOutputLattice class for webservices
 * @author BODIN
 *
 */
public class ScreeningOutputLatticeWS3VO extends ScreeningOutputLattice3VO{
	private static final long serialVersionUID = 3167572408092786300L;
	
	private Integer screeningOutputId;

	public ScreeningOutputLatticeWS3VO() {
		super();
	}
	
	public ScreeningOutputLatticeWS3VO(ScreeningOutputLattice3VO vo) {
		super(vo);
	}
	
	public ScreeningOutputLatticeWS3VO(Integer screeningOutputLatticeId,
			Integer screeningOutputId, String spaceGroup, String pointGroup,
			String bravaisLattice, Double rawOrientationMatrix_a_x,
			Double rawOrientationMatrix_a_y, Double rawOrientationMatrix_a_z,
			Double rawOrientationMatrix_b_x, Double rawOrientationMatrix_b_y,
			Double rawOrientationMatrix_b_z, Double rawOrientationMatrix_c_x,
			Double rawOrientationMatrix_c_y, Double rawOrientationMatrix_c_z,
			Double unitCell_a, Double unitCell_b, Double unitCell_c,
			Double unitCell_alpha, Double unitCell_beta, Double unitCell_gamma,
			Date timeStamp, Boolean labelitIndexing) {
		super();
		this.screeningOutputLatticeId = screeningOutputLatticeId;
		this.screeningOutputId = screeningOutputId;
		this.spaceGroup = spaceGroup;
		this.pointGroup = pointGroup;
		this.bravaisLattice = bravaisLattice;
		this.rawOrientationMatrix_a_x = rawOrientationMatrix_a_x;
		this.rawOrientationMatrix_a_y = rawOrientationMatrix_a_y;
		this.rawOrientationMatrix_a_z = rawOrientationMatrix_a_z;
		this.rawOrientationMatrix_b_x = rawOrientationMatrix_b_x;
		this.rawOrientationMatrix_b_y = rawOrientationMatrix_b_y;
		this.rawOrientationMatrix_b_z = rawOrientationMatrix_b_z;
		this.rawOrientationMatrix_c_x = rawOrientationMatrix_c_x;
		this.rawOrientationMatrix_c_y = rawOrientationMatrix_c_y;
		this.rawOrientationMatrix_c_z = rawOrientationMatrix_c_z;
		this.unitCell_a = unitCell_a;
		this.unitCell_b = unitCell_b;
		this.unitCell_c = unitCell_c;
		this.unitCell_alpha = unitCell_alpha;
		this.unitCell_beta = unitCell_beta;
		this.unitCell_gamma = unitCell_gamma;
		this.timeStamp = timeStamp;
		this.labelitIndexing = labelitIndexing ;
	}

	public Integer getScreeningOutputId() {
		return screeningOutputId;
	}

	public void setScreeningOutputId(Integer screeningOutputId) {
		this.screeningOutputId = screeningOutputId;
	}
	
	@Override
	public String toWSString(){
		String s= super.toWSString();
		s += ", screeningOutputId="+this.screeningOutputId;
		return s;
	}
	
	
	
}
