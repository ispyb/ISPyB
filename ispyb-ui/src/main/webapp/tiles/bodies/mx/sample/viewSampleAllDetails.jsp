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

<%
    String targetDataCollectionForSample = request.getContextPath() + "/menuSelected.do?leftMenuId=27&topMenuId=16&targetUrl=/user/viewDataCollection.do?reqCode=displayForSampleById";
    String targetDataCollectionGroupForSample = request.getContextPath() + "/menuSelected.do?leftMenuId=27&topMenuId=16&targetUrl=/user/viewDataCollectionGroup.do?reqCode=displayForSample";
%>


<layout:skin includeScript="true"/>
<script src="<%=request.getContextPath()%>/js/external/overlib/overlib.js"></script>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>

<%-- BreadCrumbs bar --%>
<jsp:include page="/tiles/common/util/breadCrumbsBar.jsp" flush="true" />

<%-- To not show an empty table when no samples exists --%>	
<logic:empty name="viewSampleForm" property="info.name">
           <h4>No&nbsp;samples&nbsp;have&nbsp;been&nbsp;found</h4>
</logic:empty>


<logic:notEmpty name="viewSampleForm" property="info.name">

<H1>All Sample Details</H1>

<%-------------------------%>
<layout:tabs width="600">

	<layout:tab key="Sample" width="200">
            <layout:space />
            <layout:message key="PROTEIN" styleClass="FIELD" />   
			<layout:text key="Protein name"		name="viewSampleForm" property="proteinVO.name" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
			<layout:text key="Protein acronym"	name="viewSampleForm" property="proteinVO.acronym" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
			<layout:text key="Protein sequence"	name="viewSampleForm" property="proteinVO.sequence" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
			<layout:text key="Protein Responsible"	name="viewSampleForm" property="responsibleAddress" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
            <layout:space />
            <layout:message key="CRYSTAL FORM" styleClass="FIELD" />   
			<layout:text key="Space group"	name="viewSampleForm" property="crystalVO.spaceGroup" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
			<layout:text key="a"			name="viewSampleForm" property="crystalVO.cellA" styleClass="FIELD_DISPLAY" mode="I,I,I"><logic:present name="viewSampleForm" property="crystalVO.cellA"> &#197;</logic:present></layout:text>
			<layout:text key="b"			name="viewSampleForm" property="crystalVO.cellB" styleClass="FIELD_DISPLAY" mode="I,I,I"><logic:present name="viewSampleForm" property="crystalVO.cellB"> &#197;</logic:present></layout:text>
			<layout:text key="c"			name="viewSampleForm" property="crystalVO.cellC" styleClass="FIELD_DISPLAY" mode="I,I,I"><logic:present name="viewSampleForm" property="crystalVO.cellC"> &#197;</logic:present></layout:text>
			<layout:text key="alpha"		name="viewSampleForm" property="crystalVO.cellAlpha" styleClass="FIELD_DISPLAY" mode="I,I,I"><logic:present name="viewSampleForm" property="crystalVO.cellAlpha">&#176;</logic:present></layout:text>
			<layout:text key="beta"			name="viewSampleForm" property="crystalVO.cellBeta" styleClass="FIELD_DISPLAY" mode="I,I,I"><logic:present name="viewSampleForm" property="crystalVO.cellBeta">&#176;</logic:present></layout:text>
			<layout:text key="gamma"		name="viewSampleForm" property="crystalVO.cellGamma" styleClass="FIELD_DISPLAY" mode="I,I,I"><logic:present name="viewSampleForm" property="crystalVO.cellGamma">&#176;</logic:present></layout:text>

            <layout:space />
            <layout:message key="SAMPLE" styleClass="FIELD" />   
			<layout:text key="Sample name"		name="viewSampleForm" property="info.name" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
			<layout:text key="Sample barcode"	name="viewSampleForm" property="info.code" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
			<layout:text key="Holder length"	name="viewSampleForm" property="info.holderLength" styleClass="FIELD_DISPLAY" mode="I,I,I"><logic:present name="viewSampleForm" property="info.holderLength"> mm</logic:present></layout:text>
			<layout:text key="Loop type"		name="viewSampleForm" property="info.loopType" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
			<layout:text key="Sample comments"	name="viewSampleForm" property="info.comments" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
			<layout:text key="Sample status"	name="viewSampleForm" property="info.blSampleStatus" styleClass="FIELD_DISPLAY" mode="I,I,I"/>

            <layout:space />
            <layout:message key="DIFFRACTION PLAN" styleClass="FIELD" />   
			<layout:text key="Pre-observed resolution"	name="viewSampleForm" property="difPlanInfo.observedResolution" styleClass="FIELD_DISPLAY" mode="I,I,I"><logic:present name="viewSampleForm" property="difPlanInfo.observedResolution"> &#197;</logic:present></layout:text>
			<layout:text key="Required resolution"		name="viewSampleForm" property="difPlanInfo.requiredResolution" styleClass="FIELD_DISPLAY" mode="I,I,I"><logic:present name="viewSampleForm" property="difPlanInfo.requiredResolution"> &#197;</logic:present></layout:text>
			<layout:text key="Minimal resolution"		name="viewSampleForm" property="difPlanInfo.minimalResolution" styleClass="FIELD_DISPLAY" mode="I,I,I"><logic:present name="viewSampleForm" property="difPlanInfo.minimalResolution"> &#197;</logic:present></layout:text>
			<layout:text key="Oscillation range"		name="viewSampleForm" property="difPlanInfo.oscillationRange" styleClass="FIELD_DISPLAY" mode="I,I,I"><logic:present name="viewSampleForm" property="difPlanInfo.oscillationRange">&#176;</logic:present></layout:text>
			<layout:text key="Experiment type"			name="viewSampleForm" property="difPlanInfo.experimentKind" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
  		    <layout:text key="Anomalous scatterer"		name="viewSampleForm" property="difPlanInfo.anomalousScatterer" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
			<layout:text key="Preferred beam diameter"		name="viewSampleForm" property="difPlanInfo.preferredBeamDiameter" styleClass="FIELD_DISPLAY" mode="I,I,I"><logic:present name="viewSampleForm" property="difPlanInfo.preferredBeamDiameter"> &#181;m</logic:present></layout:text>
			<layout:text key="Number of positions"		name="viewSampleForm" property="difPlanInfo.numberOfPositions" styleClass="FIELD_DISPLAY" mode="I,I,I"><logic:present name="viewSampleForm" property="difPlanInfo.numberOfPositions"></logic:present></layout:text>
			<layout:text key="Radiation sensitivity"		name="viewSampleForm" property="difPlanInfo.radiationSensitivity" styleClass="FIELD_DISPLAY" mode="I,I,I"><logic:present name="viewSampleForm" property="difPlanInfo.radiationSensitivity"></logic:present></layout:text>
			<layout:text key="Aimed completeness"		name="viewSampleForm" property="difPlanInfo.aimedCompleteness" styleClass="FIELD_DISPLAY" mode="I,I,I"><logic:present name="viewSampleForm" property="difPlanInfo.requiredCompleteness"></logic:present></layout:text>
			<layout:text key="Aimed multiplicity"		name="viewSampleForm" property="difPlanInfo.aimedMultiplicity" styleClass="FIELD_DISPLAY" mode="I,I,I"><logic:present name="viewSampleForm" property="difPlanInfo.requiredMultiplicity"></logic:present></layout:text>
			<layout:text key="Comments"					name="viewSampleForm" property="difPlanInfo.comments" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
	</layout:tab>

	<layout:tab key="Sample location" width="200">
			<layout:space />
			<layout:message key="SHIPMENT" styleClass="FIELD" />   
			<layout:text key="Shipment name"		name="viewSampleForm" property="shippingVO.shippingName" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
			<layout:text key="Laboratory address"		name="viewSampleForm" property="laboratoryAddress" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
			<%--<logic:present name="viewSampleForm" property="laboratoryVO">
			<logic:notEmpty name="viewSampleForm" property="laboratoryVO">		
			    <bean:define id="address" 			name="viewSampleForm" property="laboratoryVO.address"/>
			    <bean:define id="city" 				name="viewSampleForm" property="laboratoryVO.city"/>
			    <bean:define id="country" 			name="viewSampleForm" property="laboratoryVO.country"/>
				<layout:text key="Laboratory address"	name="viewSampleForm" property="laboratoryVO.name" styleClass="FIELD_DISPLAY" mode="I,I,I"><BR><%=address%> <%=city%> <%=country%></layout:text>
			</logic:notEmpty>
			</logic:present>--%>
			<layout:text key="Courrier name"	name="viewSampleForm" property="shippingVO.deliveryAgentAgentName" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
			<%-- TODO : implement courrier account number, MXFeature
				<layout:text key="Courrier account number"	name="viewSampleForm" property="shippingVO.deliveryAgentAgentName" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
			--%>
			<layout:text key="Tracking number"	name="viewSampleForm" property="shippingVO.deliveryAgentAgentCode" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
			<layout:text key="Shipping date"	name="viewSampleForm" property="shippingVO.deliveryAgentShippingDate" styleClass="FIELD_DISPLAY" mode="I,I,I" type="date"/>
			<layout:text key="Delivery date"	name="viewSampleForm" property="shippingVO.deliveryAgentDeliveryDate" styleClass="FIELD_DISPLAY" mode="I,I,I" type="date"/>
			<layout:text key="Status"	name="viewSampleForm" property="shippingVO.shippingStatus" styleClass="FIELD_DISPLAY" mode="I,I,I"/>

			<layout:space />
			<layout:message key="DEWAR" styleClass="FIELD" />   
			<layout:text key="Dewar barcode"	name="viewSampleForm" property="dewarVO.code" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
			<layout:text key="Dewar comments"	name="viewSampleForm" property="dewarVO.comments" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
			<layout:text key="Storage location"	name="viewSampleForm" property="dewarVO.storageLocation" styleClass="FIELD_DISPLAY" mode="I,I,I"/>

			<layout:space />
			<layout:message key="CONTAINER" styleClass="FIELD" />   
			<layout:text key="Type"				name="viewSampleForm" property="containerVO.containerType" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
			<layout:text key="Container code"	name="viewSampleForm" property="containerVO.code" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
			<layout:text key="Capacity"			name="viewSampleForm" property="containerVO.capacity" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
			<layout:text key="Sample location"	name="viewSampleForm" property="containerVO.sampleChangerLocation" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
	</layout:tab>

	<layout:tab key="Data collection" width="200">
			<layout:space />
			<layout:link href="<%=targetDataCollectionForSample%>" paramName="viewSampleForm" paramId="id" paramProperty="info.blSampleId" styleClass="FIELD">
				View data collections for this sample
			</layout:link>
			<layout:space />
			<layout:link href="<%=targetDataCollectionGroupForSample%>" paramName="viewSampleForm" paramId="blSampleId" paramProperty="info.blSampleId" styleClass="FIELD">
				View data collectionGroups, Energy Scans or XRFSpectra for this sample
			</layout:link>
			<layout:space />
			<layout:space />
			
	</layout:tab>

</layout:tabs>

</logic:notEmpty>


