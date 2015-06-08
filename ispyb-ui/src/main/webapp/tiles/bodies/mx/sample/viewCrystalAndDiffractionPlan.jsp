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

<layout:skin includeScript="true" />
<script src="<%=request.getContextPath()%>/js/external/overlib/overlib.js"></script>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>

<%-- BreadCrumbs bar --%>
<jsp:include page="/tiles/common/util/breadCrumbsBar.jsp" flush="true" />

<%-- Acknowledge Action --%>
<jsp:include page="/tiles/common/messages/info.jsp" flush="true" />

<%-- Form to view/edit diffraction plan--%>
<%-- <jsp:include page="diffractionPlan.jsp" flush="true" /> --%>

<%-- crystal form parameters --%>

<layout:row>

<layout:panel key="Crystal form" align="left" styleClass="PANEL">
	<layout:grid cols="1" >		
	      <layout:column>
			  <layout:text key="SpaceGroup" name="viewDiffractionPlanForm" property="crystalInfo.spaceGroup" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
	        <layout:row>
			    <layout:text key="size X" 		name="viewDiffractionPlanForm"	property="crystalInfo.sizeX" 	styleClass="FIELD_DISPLAY" mode="I,I,I"	/>
			    <layout:text key="size Y" 	name="viewDiffractionPlanForm"		property="crystalInfo.sizeY" 	styleClass="FIELD_DISPLAY" mode="I,I,I"	/>
			    <layout:text key="size Z" 	name="viewDiffractionPlanForm"		property="crystalInfo.sizeZ" 	styleClass="FIELD_DISPLAY" mode="I,I,I"	/>
		</layout:row>
		<layout:row>
			    <layout:text key="cell A" 	name="viewDiffractionPlanForm"		property="crystalInfo.cellA" 	styleClass="FIELD_DISPLAY" mode="I,I,I"	/>
			    <layout:text key="cell B" 		name="viewDiffractionPlanForm"	property="crystalInfo.cellB" 	styleClass="FIELD_DISPLAY" mode="I,I,I"/>
			    <layout:text key="cell C" 		name="viewDiffractionPlanForm"	property="crystalInfo.cellC" 	styleClass="FIELD_DISPLAY" mode="I,I,I"/>
		</layout:row>
		<layout:row>
			    <layout:text key="alpha" 	name="viewDiffractionPlanForm"	property="crystalInfo.cellAlpha" 	styleClass="FIELD_DISPLAY" mode="I,I,I"/>
			    <layout:text key="beta" 	name="viewDiffractionPlanForm"	property="crystalInfo.cellBeta" 	styleClass="FIELD_DISPLAY" mode="I,I,I"/>
			    <layout:text key="gama" 	name="viewDiffractionPlanForm"  property="crystalInfo.cellGamma" 	styleClass="FIELD_DISPLAY" mode="I,I,I"/>
		</layout:row>
	</layout:column>
		
     </layout:grid>
</layout:panel>

</layout:row>
