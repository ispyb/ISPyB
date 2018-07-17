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
public class DiffractionPlanWS3VO extends DiffractionPlan3VO {

	private static final long serialVersionUID = 1L;

	public DiffractionPlanWS3VO() {
		super();
	}

	public DiffractionPlanWS3VO(DiffractionPlan3VO vo) {
		super(vo);
	}
	
	public DiffractionPlanWS3VO(Integer diffractionPlanId, String experimentKind,
			Double observedResolution, Double minimalResolution, Double exposureTime, Double oscillationRange,
			Double maximalResolution, Double screeningResolution, Double radiationSensitivity,
			String anomalousScatterer, Double preferredBeamSizeX, Double preferredBeamSizeY, Double preferredBeamDiameter,String comments,
			Double aimedCompleteness, Double aimedIOverSigmaAtHighestRes, Double aimedMultiplicity,
			Double aimedResolution, Boolean anomalousData, String complexity, Boolean estimateRadiationDamage,
			String forcedSpaceGroup, Double requiredCompleteness, Double requiredMultiplicity,
			Double requiredResolution, String strategyOption, String kappaStrategyOption, Integer numberOfPositions, 
			Double minDimAccrossSpindleAxis, Double maxDimAccrossSpindleAxis, Double radiationSensitivityBeta, 
			Double radiationSensitivityGamma, Double minOscWidth, Double axisRange) {
		super(diffractionPlanId, experimentKind,
				observedResolution, minimalResolution, exposureTime, oscillationRange,
				maximalResolution, screeningResolution, radiationSensitivity,
				anomalousScatterer, preferredBeamSizeX, preferredBeamSizeY, preferredBeamDiameter,comments,
				aimedCompleteness, aimedIOverSigmaAtHighestRes, aimedMultiplicity,
				aimedResolution, anomalousData, complexity, estimateRadiationDamage,
				forcedSpaceGroup, requiredCompleteness, requiredMultiplicity,
				requiredResolution, strategyOption, kappaStrategyOption, numberOfPositions, 
				minDimAccrossSpindleAxis, maxDimAccrossSpindleAxis, radiationSensitivityBeta, radiationSensitivityGamma, minOscWidth, axisRange);
	}


	@Override
	public String toWSString(){
		String s= super.toWSString();
		return s;
	}
	
}