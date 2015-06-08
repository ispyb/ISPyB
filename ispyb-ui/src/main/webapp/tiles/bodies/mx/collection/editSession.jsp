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

<%
	String editSession = "Edit " + Constants.SESSION_VISIT;
	String targetMail = "mailto:";
%>

<%-- BreadCrumbs bar --%>
<jsp:include page="/tiles/common/util/breadCrumbsBar.jsp" flush="true" />


<layout:skin includeScript="true"/>

<layout:grid cols="1"  borderSpacing="10">
	<layout:column>
		<layout:panel key="<%=editSession%>" align="left" styleClass="PANEL">
		<layout:form action="/user/editSession.do" reqCode="updateSession" method="POST">
			<layout:grid cols="1" styleClass="SEARCH_GRID">	
				
				<layout:column>
					<layout:text 		key="theSessionId"	name="viewSessionForm"	
					                    property="theSessionId"  mode="H,H,H"/>
					<layout:text 		key="Local contact" 		name="viewSessionForm"	
					                    property="beamLineOperator" styleClass="FIELD" mode="E,E,E" />
					<layout:row>
						<logic:present name="viewSessionForm" property="beamLineOperatorEmail">
							<logic:notEqual name="viewSessionForm" property="beamLineOperatorEmail" value="">
								<bean:define id="email" name="viewSessionForm" property="beamLineOperatorEmail" type="java.lang.String"></bean:define>
								<html:link href="<%= targetMail + email  %>" onmouseover="<%= \"return overlib('mail to: \"+email+\"')\"%>" onmouseout="return nd();">
									<img src="<%=request.getContextPath()%>/images/mail_generic.png" border=0">
								</html:link>
							</logic:notEqual>
						</logic:present>
					</layout:row>
					<layout:textarea	key="Comments" 			name="viewSessionForm"		
					                    property="comments"	 	styleClass="FIELD"	mode="E,E,E"	size="40" rows="4"/>

					<%-- display only for FX accounts --%>
					<logic:present name="breadCrumbsForm"  property="proposalCode">
		  				<logic:equal name="breadCrumbsForm" property="proposalCode" value="<%=Constants.PROPOSAL_CODE_FX%>" >
							<layout:text key="Session Title" property="sessionTitle" styleClass="FIELD" mode="E,E,E" />
							<layout:text key="Structure Determinations" property="structureDeterminations" styleClass="FIELD" mode="E,E,E" />
							<layout:text key="Dewar Transport" property="dewarTransport" styleClass="FIELD" mode="E,E,E" />
							<layout:text key="Data backup &<br/>Express delivery France" property="dataBackupFrance" styleClass="FIELD" mode="E,E,E" />
							<layout:text key="Data backup &<br/>Express delivery Europe" property="dataBackupEurope" styleClass="FIELD" mode="E,E,E" />
						</logic:equal>
					</logic:present>
					<%-- display only for IX accounts --%>
					<logic:present name="breadCrumbsForm"  property="proposalCode">
		  				<logic:equal name="breadCrumbsForm" property="proposalCode" value="<%=Constants.PROPOSAL_CODE_IX%>" >
							<layout:text key="Dewar transport" property="dewarTransport" styleClass="FIELD" mode="E,E,E" />
							<layout:text key="Data backup &<br/>Express delivery France" property="dataBackupFrance" styleClass="FIELD" mode="E,E,E" />
							<layout:text key="Data backup &<br/>Express delivery Europe" property="dataBackupEurope" styleClass="FIELD" mode="E,E,E" />
						</logic:equal>
					</logic:present>
						
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


