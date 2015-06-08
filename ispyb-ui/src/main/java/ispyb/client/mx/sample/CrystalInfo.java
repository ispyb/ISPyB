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
 * CrystalInfo.java 
 * @author ludovic.launer@esrf.fr Feb 21, 2005
 * 
 */

package ispyb.client.mx.sample;

import ispyb.server.mx.vos.sample.Crystal3VO;

import java.util.ArrayList;
import java.util.List;

public class CrystalInfo extends Crystal3VO {
	static final long serialVersionUID = 0;

	private String geometryClassName = new String();

	private List listGeometrySpaceGroups = new ArrayList();

	public CrystalInfo(String geometryClassName) {
		this.geometryClassName = geometryClassName;
	}

	public void addGeometryLambda(String newGeometrySpaceGroup) {
		this.listGeometrySpaceGroups.add(newGeometrySpaceGroup);
	}

	// ______________________________________________________________________________________________________________________

	public String getGeometryClassName() {
		return geometryClassName;
	}

	public void setGeometryClassName(String geometryClass) {
		this.geometryClassName = geometryClass;
	}

	public List getListGeometrySpaceGroups() {
		return listGeometrySpaceGroups;
	}

	public void setListGeometrySpaceGroups(List listGeometryLambda) {
		this.listGeometrySpaceGroups = listGeometryLambda;
	}
}
