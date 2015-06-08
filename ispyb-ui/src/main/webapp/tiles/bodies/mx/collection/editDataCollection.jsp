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

<%@page import="ispyb.common.util.Constants"%>

<%-- BreadCrumbs bar --%>
<jsp:include page="/tiles/common/util/breadCrumbsBar.jsp" flush="true" />


<layout:skin includeScript="true"/>

<layout:grid cols="1"  borderSpacing="10">
	<layout:column>
		<layout:panel key="Data Collection Details" align="left" styleClass="PANEL">
		<layout:form action="/user/viewDataCollection.do" reqCode="saveDataCollection" method="POST">
			<layout:grid cols="1" styleClass="SEARCH_GRID">	
				
				<layout:column>
					<layout:text 		key="dataCollectionId"	name="viewDataCollectionForm"	
					                    property="selectedDataCollection.dataCollectionId"  mode="H,H,H"/>
					<layout:text 		key="Start Time" 		name="viewDataCollectionForm" 
					                    property="selectedDataCollection.startTime" styleClass="FIELD" mode="I,I,I" type="dateTime"/>
					<layout:textarea	key="Comments" 			name="viewDataCollectionForm"	
					                    property="selectedDataCollection.comments"	 	styleClass="FIELD"	mode="E,E,E"	size="40" rows="4"/>

		            <layout:checkbox 	key="keep for report" 		name="viewDataCollectionForm" value="1"
					                    property="selectedDataCollection.printableForReport" styleClass="FIELD" mode="E,E,E"/>

<%-- edit crystalClass only for IFX proposals (MXPress experiments)--%>
                    <logic:equal name="breadCrumbsForm" property="proposalCode" value="<%=Constants.PROPOSAL_CODE_FX%>" >						    
				            <layout:text 		key="Crystal class" 		name="viewDataCollectionForm" 
							                    property="selectedDataCollection.crystalClass" styleClass="FIELD" mode="E,E,E"/>
                    </logic:equal>

				</layout:column>		
				<layout:row>
					<layout:submit>
						<layout:message key="Save"/>
					</layout:submit>
					<layout:cancel reqCode="display" >
						<layout:message key="Cancel"/>
					</layout:cancel>
				</layout:row>
			</layout:grid>
		</layout:form>
		</layout:panel>
	</layout:column>

</layout:grid>


