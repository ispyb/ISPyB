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

package ispyb.server.mx.vos.autoproc;

import java.util.Date;

/**
 * ImageQualityIndicators class for webservices
 * @author BODIN
 *
 */
public class ImageQualityIndicatorsWS3VO extends ImageQualityIndicators3VO{
	private static final long serialVersionUID = -4879088163539039433L;
	
	private Integer imageId;
	private Integer autoProcProgramId;
	
	private String fileName;
	
	private String fileLocation;
	
	public ImageQualityIndicatorsWS3VO() {
		super();
	}

	public ImageQualityIndicatorsWS3VO(ImageQualityIndicators3VO vo) {
		super(vo);
	}

	
	public ImageQualityIndicatorsWS3VO(Integer imageQualityIndicatorsId,
			Integer imageId, Integer autoProcProgramId, Integer spotTotal,
			Integer inResTotal, Integer goodBraggCandidates, Integer iceRings,
			Float method1Res, Float method2Res, Float maxUnitCell,
			Float pctSaturationTop50Peaks, Integer inResolutionOvrlSpots, Float binPopCutOffMethod2Res,
			Date recordTimeStamp, Double totalIntegratedSignal, Double dozor_score) {
		super();
		this.imageQualityIndicatorsId = imageQualityIndicatorsId;
		this.imageId = imageId;
		this.autoProcProgramId = autoProcProgramId;
		this.spotTotal = spotTotal;
		this.inResTotal = inResTotal;
		this.goodBraggCandidates = goodBraggCandidates;
		this.iceRings = iceRings;
		this.method1Res = method1Res;
		this.method2Res = method2Res;
		this.maxUnitCell = maxUnitCell;
		this.pctSaturationTop50Peaks = pctSaturationTop50Peaks;
		this.inResolutionOvrlSpots = inResolutionOvrlSpots;
		this.binPopCutOffMethod2Res = binPopCutOffMethod2Res;
		this.recordTimeStamp = recordTimeStamp;
		this.totalIntegratedSignal = totalIntegratedSignal;
		this.dozor_score = dozor_score;
	}



	public Integer getImageId() {
		return imageId;
	}


	public void setImageId(Integer imageId) {
		this.imageId = imageId;
	}


	public Integer getAutoProcProgramId() {
		return autoProcProgramId;
	}


	public void setAutoProcProgramId(Integer autoProcProgramId) {
		this.autoProcProgramId = autoProcProgramId;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileLocation() {
		return fileLocation;
	}

	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	@Override
	public String toWSString(){
		String s= super.toWSString();
		s += ", imageId="+this.imageId+", "+
		"autoProcProgramId="+this.autoProcProgramId;
		return s;
	}
	
	
}
