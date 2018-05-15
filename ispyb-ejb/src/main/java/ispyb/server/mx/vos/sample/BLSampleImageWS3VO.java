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
 * BLSampleImageWS class used for webservices
 * 
 * @author BODIN
 * 
 */
public class BLSampleImageWS3VO extends BLSampleImage3VO{
	private static final long serialVersionUID = 1L;
	
	private Integer blSampleId;

	public BLSampleImageWS3VO() {
		super();
	}
	
	public BLSampleImageWS3VO(BLSampleImage3VO vo) {
		super(vo);
		this.blSampleId = vo.getBlsampleVO().getBlSampleId();
	}

	public BLSampleImageWS3VO(Integer blSampleId) {
		super();
		this.blSampleId = blSampleId;
	}

	public Integer getBlSampleId() {
		return blSampleId;
	}

	public void setBlSampleId(Integer blSampleId) {
		this.blSampleId = blSampleId;
	}

	public String toWSString() {
		String s = "blSampleImageId="+this.blSampleImageId +", "+
		"imgFilePath="+this.imageFullPath+", "+
		"comments="+this.comments+", ";
		
		return s;
	}
	
}
