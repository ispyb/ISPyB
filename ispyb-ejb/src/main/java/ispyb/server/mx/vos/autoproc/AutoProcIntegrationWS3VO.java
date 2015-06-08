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
 * AutoProcIntegration class for webservices
 * @author BODIN
 *
 */
public class AutoProcIntegrationWS3VO extends AutoProcIntegration3VO{
	private static final long serialVersionUID = 5942142080203412448L;

	private Integer dataCollectionId;
	
	private Integer autoProcProgramId;
	
	

	public AutoProcIntegrationWS3VO() {
		super();
	}

	public AutoProcIntegrationWS3VO(Integer dataCollectionId,
			Integer autoProcProgramId) {
		super();
		this.dataCollectionId = dataCollectionId;
		this.autoProcProgramId = autoProcProgramId;
	}
	
	public AutoProcIntegrationWS3VO(Integer autoProcIntegrationId,
			Integer dataCollectionId, Integer autoProcProgramId,
			Integer startImageNumber, Integer endImageNumber,
			Float refinedDetectorDistance, Float refinedXbeam,
			Float refinedYbeam, Float rotationAxisX, Float rotationAxisY,
			Float rotationAxisZ, Float beamVectorX, Float beamVectorY,
			Float beamVectorZ, Float cellA, Float cellB, Float cellC,
			Float cellAlpha, Float cellBeta, Float cellGamma,
			Date recordTimeStamp, Boolean anomalous) {
		super();
		this.autoProcIntegrationId = autoProcIntegrationId;
		this.dataCollectionId = dataCollectionId;
		this.autoProcProgramId = autoProcProgramId;
		this.startImageNumber = startImageNumber;
		this.endImageNumber = endImageNumber;
		this.refinedDetectorDistance = refinedDetectorDistance;
		this.refinedXbeam = refinedXbeam;
		this.refinedYbeam = refinedYbeam;
		this.rotationAxisX = rotationAxisX;
		this.rotationAxisY = rotationAxisY;
		this.rotationAxisZ = rotationAxisZ;
		this.beamVectorX = beamVectorX;
		this.beamVectorY = beamVectorY;
		this.beamVectorZ = beamVectorZ;
		this.cellA = cellA;
		this.cellB = cellB;
		this.cellC = cellC;
		this.cellAlpha = cellAlpha;
		this.cellBeta = cellBeta;
		this.cellGamma = cellGamma;
		this.recordTimeStamp = recordTimeStamp;
		this.anomalous = anomalous;
	}

	public Integer getDataCollectionId() {
		return dataCollectionId;
	}

	public void setDataCollectionId(Integer dataCollectionId) {
		this.dataCollectionId = dataCollectionId;
	}

	public Integer getAutoProcProgramId() {
		return autoProcProgramId;
	}

	public void setAutoProcProgramId(Integer autoProcProgramId) {
		this.autoProcProgramId = autoProcProgramId;
	}
	
	@Override
	public String toWSString(){
		String s= super.toWSString();
		s += ", dataCollectionId="+this.dataCollectionId+", "+
		"autoProcProgramId="+this.autoProcProgramId;
		return s;
	}
	
	
}
