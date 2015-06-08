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
 * BLSampleHasEnergyScan for webservices
 * @author BODIN
 *
 */
public class BLSampleHasEnergyScanWS3VO extends BLSampleHasEnergyScan3VO{
	private static final long serialVersionUID = 3419065284576374455L;
	
	private Integer blSampleId;
	private Integer energyScanId;
	
	public BLSampleHasEnergyScanWS3VO() {
		super();
	}
	
	public BLSampleHasEnergyScanWS3VO(BLSampleHasEnergyScan3VO vo) {
		super(vo);
	}
	
	public BLSampleHasEnergyScanWS3VO(Integer blSampleId, Integer energyScanId) {
		super();
		this.blSampleId = blSampleId;
		this.energyScanId = energyScanId;
	}
	public Integer getBlSampleId() {
		return blSampleId;
	}
	public void setBlSampleId(Integer blSampleId) {
		this.blSampleId = blSampleId;
	}
	public Integer getEnergyScanId() {
		return energyScanId;
	}
	public void setEnergyScanId(Integer energyScanId) {
		this.energyScanId = energyScanId;
	}
	
}
