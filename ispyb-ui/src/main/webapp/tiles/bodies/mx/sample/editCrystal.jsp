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

<bean:define name="viewCrystalForm" property="theProteinId" id="spid" type="java.lang.Integer"/>

<layout:grid cols="1"  borderSpacing="5">

	<layout:panel key="Edit Crystal Form & Diffraction plan " align="left" styleClass="PANEL">

		<layout:form action="/user/viewProteinAndCrystal.do" reqCode="update">	
		<%-- Crystal --%>

			<layout:row>	
				<layout:text key="Space Group" property="info.spaceGroup" styleClass="FIELD" mode="I,I,I" size="12"/>
				<layout:text key="id" property="info.crystalId" styleClass="FIELD" mode="H,H,H" size="2"/>
				
				<layout:grid cols="3" styleClass="SEARCH_GRID">
						<layout:column>
							<layout:text 		key="a" 			property="info.cellA"	 		styleClass="FIELD"	mode="E,E,E"	size="4"/>																																																									
							<layout:text 		key="Alpha" 		property="info.cellAlpha"	 	styleClass="FIELD"	mode="E,E,E"	size="4"/>																																																			
						</layout:column>
						<layout:column>
							<layout:text 		key="b" 			property="info.cellB"	 		styleClass="FIELD"	mode="E,E,E"	size="4" />																																																		
							<layout:text 		key="Beta" 			property="info.cellBeta"	 	styleClass="FIELD"	mode="E,E,E"	size="4"/>
						</layout:column>
						<layout:column>
							<layout:text 		key="c" 			property="info.cellC"	 		styleClass="FIELD"	mode="E,E,E"	size="4" />
							<layout:text 		key="gamma" 		property="info.cellGamma"	 	styleClass="FIELD"	mode="E,E,E"	size="4"/>
						</layout:column>				
					</layout:grid>			
			</layout:row>
																																																			
			<layout:space/>
			
			<layout:grid cols="2" styleClass="SEARCH_GRID">
				<layout:column>

				</layout:column>
				<layout:column>
					<layout:textarea	key="Comments" 		property="info.comments"	 	styleClass="FIELD"	mode="E,E,E"	size="30" rows="4"/>
				</layout:column>
				
			</layout:grid>
																																																
			<layout:space/>
			<layout:space/>

		<%-- Diffraction Plan --%>
			<layout:row>	
				<layout:column>
					<layout:text key="Id" 					property="difPlanInfo.diffractionPlanId" 	styleClass="FIELD"	mode="H,H,H"/>
					<layout:select 	key="Experiment Kind" 	property="difPlanInfo.experimentKind" size="1" styleClass="FIELD"	mode="E,E,E"> 
									<layout:options property="listExperimentKind"/>
					</layout:select>					
					<layout:text key="Already Observed Resolution" 	property="difPlanInfo.observedResolution" 	    styleClass="FIELD"	mode="E,E,E"/>
					<layout:text key="Minimal Resolution" 	property="difPlanInfo.minimalResolution" 	styleClass="FIELD"	mode="E,E,E"/>
					<layout:text key="Required Resolution" 	property="difPlanInfo.requiredResolution" 	styleClass="FIELD"	mode="E,E,E"/>
					<layout:text key="Exposure Time" 		property="difPlanInfo.exposureTime" 		styleClass="FIELD"	mode="E,E,E"/>
					<layout:text key="Oscillation Range" 	property="difPlanInfo.oscillationRange" 	styleClass="FIELD"	mode="E,E,E"/>
				</layout:column>
		
			</layout:row>
			
			<layout:space/>
						
			<layout:row>
				<layout:reset/>
				<layout:submit reqCode="update"><layout:message key="Save"/></layout:submit>
			</layout:row>
      
		</layout:form>	
	</layout:panel>
</layout:grid>

<%-- Acknowledge Action --%>
<jsp:include page="/tiles/common/messages/info.jsp" flush="true" />

<%-- Errors --%>
<jsp:include page="/tiles/common/messages/error.jsp" flush="true" />
