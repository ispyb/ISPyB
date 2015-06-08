<%--------------------------------------------------------------------------------------------------
This file is part of ISPyB.

ISPyB is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

ISPyB is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with ISPyB.  If not, see <http://www.gnu.org/licenses/>.

Contributors : S. Delageniere, R. Leal, L. Launer, K. Levik, S. Veyrier, P. Brenchereau, M. Bodin, A. De Maria Antolinos
--------------------------------------------------------------------------------------------------%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.application-servers.com/layout" prefix="layout" %>

<layout:skin />

<%@page import="ispyb.common.util.Constants"%>

<%-- BreadCrumbs bar --%>
<jsp:include page="/tiles/common/util/breadCrumbsBar.jsp" flush="true" />

<%-- Screening Parameters lists --%>

<logic:notEmpty name="viewResultsForm"  property="screening" > 

<layout:grid cols="1"  borderSpacing="10">

<layout:column >
<layout:tabs width="800">

	<layout:tab key="Screening rank" width="400">

		<layout:text key="Rank value" 	name="viewResultsForm"  property="screeningRank.rankValue" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
		<layout:text key="Rank information" 	name="viewResultsForm"  property="screeningRank.rankInformation" styleClass="FIELD_DISPLAY" mode="I,I,I"/>

	</layout:tab>
		
	<layout:tab key="Screening Input" width="400">

		<layout:text key="BeamX" 	name="viewResultsForm"  property="screeningInput.beamX" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
		<layout:text key="BeamY" 	name="viewResultsForm"  property="screeningInput.beamY" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
		<layout:text key="RMS error" 	name="viewResultsForm"  property="screeningInput.rmsErrorLimits" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
		<layout:text key="Min Fraction indexed" 	name="viewResultsForm"  property="screeningInput.minimumFractionIndexed" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
		<layout:text key="Max Fraction rejected" 	name="viewResultsForm"  property="screeningInput.maximumFractionRejected" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
		<layout:text key="Min SignalToNoise" 	name="viewResultsForm"  property="screeningInput.minimumSignalToNoise" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
	
	</layout:tab>


	<layout:tab key="Screening output" width="400">

		<layout:text key="Status" 	name="viewResultsForm"  property="screeningOutput.statusDescription" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
		<layout:text key="Rejected reflections" 	name="viewResultsForm"  property="screeningOutput.rejectedReflections" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
		<layout:text key="Obtained Resol." 	name="viewResultsForm"  property="screeningOutput.resolutionObtained" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
		<layout:text key="Spot devR" 	name="viewResultsForm"  property="screeningOutput.spotDeviationR" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
		<layout:text key="Spot devT" 	name="viewResultsForm"  property="screeningOutput.spotDeviationTheta" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
		<layout:text key="BeamShiftX" 	name="viewResultsForm"  property="screeningOutput.beamShiftX" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
		<layout:text key="BeamShiftY" 	name="viewResultsForm"  property="screeningOutput.beamShiftY" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
		<layout:text key="NumSpots" 	name="viewResultsForm"  property="screeningOutput.numSpotsFound" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
		<layout:text key="Spots rejected" 	name="viewResultsForm"  property="screeningOutput.numSpotsUsed" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
		<layout:text key="Spots used" 	name="viewResultsForm"  property="screeningOutput.numSpotsRejected" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
		<layout:text key="Mosaicity" 	name="viewResultsForm"  property="screeningOutput.mosaicity" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
		<layout:text key="Dif rings" 	name="viewResultsForm"  property="screeningOutput.diffractionRings" styleClass="FIELD_DISPLAY" mode="I,I,I"/>

	</layout:tab>

	<layout:tab key="Screening output lattice" width="400">

		<layout:text key="Space group" 	name="viewResultsForm"  property="screeningOutputLattice.spaceGroup" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
		<layout:text key="Point group" 	name="viewResultsForm"  property="screeningOutputLattice.pointGroup" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
		<layout:text key="Bravais lattice" 	name="viewResultsForm"  property="screeningOutputLattice.bravaisLattice" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
		<layout:text key="Raw orientation matrix a x" 	name="viewResultsForm"  property="screeningOutputLattice.rawOrientationMatrix_a_x" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
		<layout:text key="Raw orientation matrix a y" 	name="viewResultsForm"  property="screeningOutputLattice.rawOrientationMatrix_a_y" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
		<layout:text key="Raw orientation matrix a z" 	name="viewResultsForm"  property="screeningOutputLattice.rawOrientationMatrix_a_z" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
		<layout:text key="Raw orientation matrix b x" 	name="viewResultsForm"  property="screeningOutputLattice.rawOrientationMatrix_b_x" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
		<layout:text key="Raw orientation matrix b y" 	name="viewResultsForm"  property="screeningOutputLattice.rawOrientationMatrix_b_y" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
		<layout:text key="Raw orientation matrix b z" 	name="viewResultsForm"  property="screeningOutputLattice.rawOrientationMatrix_b_z" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
		<layout:text key="Raw orientation matrix c x" 	name="viewResultsForm"  property="screeningOutputLattice.rawOrientationMatrix_c_x" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
		<layout:text key="Raw orientation matrix c y" 	name="viewResultsForm"  property="screeningOutputLattice.rawOrientationMatrix_c_y" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
		<layout:text key="Raw orientation matrix c z" 	name="viewResultsForm"  property="screeningOutputLattice.rawOrientationMatrix_c_z" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
		<layout:text key="Unit Cell a" 	name="viewResultsForm"  property="screeningOutputLattice.unitCell_a" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
		<layout:text key="Unit Cell b" 	name="viewResultsForm"  property="screeningOutputLattice.unitCell_b" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
		<layout:text key="Unit Cell c" 	name="viewResultsForm"  property="screeningOutputLattice.unitCell_c" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
		<layout:text key="Unit Cell alpha" 	name="viewResultsForm"  property="screeningOutputLattice.unitCell_alpha" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
		<layout:text key="Unit Cell beta" 	name="viewResultsForm"  property="screeningOutputLattice.unitCell_beta" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
		<layout:text key="Unit Cell gamma" 	name="viewResultsForm"  property="screeningOutputLattice.unitCell_gamma" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
		<layout:text key="Time" 	name="viewResultsForm"  property="screeningOutputLattice.bltimeStamp" styleClass="FIELD_DISPLAY" mode="I,I,I"/>

	</layout:tab>		


	<layout:tab key="Screening strategies" width="400">

	<logic:notEmpty name="viewResultsForm"  property="screeningStrategyList">
		<layout:collection name="viewResultsForm" property="screeningStrategyList" styleClass="LIST" >						
			<layout:collectionItem title="Phi start" 	property="phiStart" sortable="false" />
			<layout:collectionItem title="Phi end" 	property="phiEnd" sortable="false" />
			<layout:collectionItem title="Rotation" 	property="rotation" sortable="false" />
			<layout:collectionItem title="Exp. time" 	property="exposureTime" sortable="false" />
			<layout:collectionItem title="Resolution" 	property="resolution" sortable="false" />
			<layout:collectionItem title="Program" 	property="program" sortable="false" />
		</layout:collection>
	</logic:notEmpty>

	</layout:tab>

</layout:tabs>
</layout:column >


</layout:grid>

</logic:notEmpty>
