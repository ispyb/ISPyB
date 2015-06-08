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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page isELIgnored="false" %>

<%@page import="ispyb.common.util.Constants"%>
<%@page import="ispyb.common.util.StringUtils"%>


<%
	String targetExportPDF 					= request.getContextPath() + "/user/exportDataCollection.do?reqCode=exportAsPdf";
    String targetExportRTF 					= request.getContextPath() + "/user/exportDataCollection.do?reqCode=exportAsRtf";
    String targetExportCSV 					= request.getContextPath() + "/user/exportDataCollection.do?reqCode=exportAsCsv";
    String targetExportPDFScreening 		= request.getContextPath() + "/user/exportDataCollection.do?reqCode=exportScreeningAsPdf";
    String targetExportRtfScreening 		= request.getContextPath() + "/user/exportDataCollection.do?reqCode=exportScreeningAsRtf";
    String exportPDFTitle 					= "View PDF report";
    String exportRTFTitle 					= "View DOC report";
    String exportCSVTitle 					= "View CSV report";
    String exportPDFTitleScreening 			= "PDF Screenings";
    String exportRtfTitleScreening 			= "DOC Screenings";
    String targetExportAndSendPDF 			= request.getContextPath() + "/user/exportDataCollection.do?reqCode=exportAndSendAsPdf";
    String exportAndSendPDFTitle 			= "Send PDF report";
    String targetMail = "mailto:";
    String formName = request.getParameter("formName"); // viewDataCollectionGroup or viewDataCollection Form
    
%>



	<table>
	<tr>
		<%-- Session comments (User and only for session) --%>
		<logic:equal name="<%=formName%>" property="isUser" value="true" >
		<logic:equal name="<%=formName%>" property="editSkipAndComments" value="true" >
		<logic:equal name="<%=formName%>" property="isDataCollectionGroupView" value="true" >
		<td  valign = 'top'>
			<layout:panel key="Session Informations" align="left" styleClass="PANEL_100">
			<layout:form action="/user/viewDataCollectionGroup.do" reqCode="saveSessionComment">
				<layout:grid cols="2" styleClass="SEARCH_GRID">	
					<layout:column >
						<layout:text key="Local contact" property="sessionBeamLineOperator" styleClass="FIELD" mode="E,E,E" />
						<layout:row>
						<logic:present name="<%=formName%>" property="sessionBeamLineOperatorEmail">
							<logic:notEqual name="<%=formName%>" property="sessionBeamLineOperatorEmail" value="">
								<bean:define id="email" name="<%=formName%>" property="sessionBeamLineOperatorEmail" type="java.lang.String"></bean:define>
								<html:link href="<%= targetMail + email  %>" onmouseover="<%= \"return overlib('mail to: \"+email+\"')\"%>" onmouseout="return nd();">
									<img src="<%=request.getContextPath()%>/images/mail_generic.png" border="0" />
								</html:link>
							</logic:notEqual>
						</logic:present>
						</layout:row>
				 		<layout:row>
						<layout:textarea key="Session Comments" property="sessionComments"	styleClass="FIELD" mode="E,E,E" size="40" rows="5"/>
						</layout:row>
						<layout:text key="sessionId" property = "sessionId"  mode="H,H,H"/>
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
						<layout:submit><layout:message key="Save"/></layout:submit>
					</layout:column>		
				</layout:grid>
			</layout:form>
			</layout:panel>
		</td>
		</logic:equal>
		</logic:equal>
		</logic:equal>
		
		<%-- Export links --%>
		<logic:equal name="<%=formName%>" property="isDataCollectionGroupView" value="false" >
	<td  valign = 'top'>
		<layout:grid cols="1">
			<layout:panel key="Reports" align="left" styleClass="PANEL_100">
			<layout:row>
			
				<%-- RTF report--%>
				<layout:link title="RTF report" href="<%= targetExportRTF %>" paramName="<%=formName%>" paramId="sessionId" paramProperty="sessionId" styleClass="FIELD">
					<img src="<%=request.getContextPath()%>/images/Word.png" border="0" onmouseover="return overlib('Export this table as DOC');" onmouseout="return nd();"/>
						<%= exportRTFTitle %>
				</layout:link>
				
				<%-- PDF report--%>
				<layout:link title="PDF report" href="<%= targetExportPDF %>" paramName="<%=formName%>" paramId="sessionId" paramProperty="sessionId" styleClass="FIELD">
					<img src="<%=request.getContextPath()%>/images/pdf.png" border="0" onmouseover="return overlib('Export this table as PDF');" onmouseout="return nd();"/>
						<%= exportPDFTitle %>
				</layout:link>
				
				<%-- Email PDF Report --%>
				<%-- 22/06/12: remove the access for fx --%>
				<logic:equal name="<%=formName%>" property="isUser" value="true" >
				 <logic:equal name="breadCrumbsForm" property="proposalCode" value="<%=Constants.PROPOSAL_CODE_FX%>" >			
					<layout:link title="PDF report/send" href="<%= targetExportAndSendPDF %>" paramName="<%=formName%>" paramId="sessionId" paramProperty="sessionId" styleClass="FIELD">
						<img src="<%=request.getContextPath()%>/images/pdf.png" border="0" onmouseover="return overlib('Export and send as PDF');" onmouseout="return nd();"/>
							<%= exportAndSendPDFTitle %>
					</layout:link>	
				</logic:equal>
				</logic:equal>
			</layout:row>
			<layout:row>
				
				<%-- CSV Report--%>
				<layout:link title="CSV report" href="<%= targetExportCSV %>" paramName="<%=formName%>" paramId="sessionId" paramProperty="sessionId" styleClass="FIELD">
					<img src="<%=request.getContextPath()%>/images/icon_excel07.jpg" border=0 onmouseover="return overlib('Export this table as CSV');" onmouseout="return nd();"/>
						<%= exportCSVTitle %>
				</layout:link>	
				
			</layout:row>
			<layout:row>
				<%-- RTF Screening--%>
				<layout:link title="RTF report Screening" href="<%= targetExportRtfScreening %>" paramName="<%=formName%>" paramId="sessionId" paramProperty="sessionId" styleClass="FIELD">
					<img src="<%=request.getContextPath()%>/images/Word.png" border=0 onmouseover="return overlib('Export Screenings into DOC');" onmouseout="return nd();"/>
						<%= exportRtfTitleScreening %>
				</layout:link>
				<%-- PDF Screening--%>
				<layout:link title="PDF report Screening" href="<%= targetExportPDFScreening %>" paramName="<%=formName%>" paramId="sessionId" paramProperty="sessionId" styleClass="FIELD">
					<img src="<%=request.getContextPath()%>/images/pdf.png" border=0 onmouseover="return overlib('Export Screenings into PDF');" onmouseout="return nd();"/>
						<%= exportPDFTitleScreening %>
				</layout:link>
				
			
			</layout:row>
		</layout:panel>
		</layout:grid>
		<div style="height: 51px"></div>
		</td>	 
		</logic:equal>	

		<logic:equal name="<%=formName%>" property="isDataCollectionGroupView" value="false" >
		<td  valign = 'top'>
			<layout:form action="/user/viewDataCollection" reqCode="display">
				<html:hidden name="<%=formName%>" property="sampleName" />
				<html:hidden name="<%=formName%>" property="proteinAcronym" />
				<html:hidden name="<%=formName%>" property="beamlineName" />
				<html:hidden name="<%=formName%>" property="experimentDateStart" />
				<html:hidden name="<%=formName%>" property="experimentDateEnd" />
				<html:hidden name="<%=formName%>" property="minNumberOfImages" />
				<html:hidden name="<%=formName%>" property="maxNumberOfImages" />
				<html:hidden name="<%=formName%>" property="maxRecords" />
			
				<layout:grid cols="1" styleClass="SEARCH_GRID">	
					<layout:panel key="Parameters" align="left" styleClass="PANEL_100">
					<layout:column>			
						<layout:text key="Ignore RSymm in the<br/>low resolution shell over:" property="RMergeCutoff"	styleClass="FIELD" mode="E,E,E"/>
						<layout:text key="Ignore I / Sigma in the<br/> low resolution shell under:" property="ISigmaCutoff"	styleClass="FIELD" mode="E,E,E"/>
						<layout:submit><layout:message key="Update"/></layout:submit>
					</layout:column>
					</layout:panel>
				</layout:grid>
			</layout:form>
			<div style="height: 77px" ></div>
		</td>
		
		</logic:equal>
		<logic:notEmpty name="<%=formName%>" property="listReferences">
		<td valign = 'top' >
			<%-- Reference--%>
			<layout:form action="/viewReference" reqCode="downloadReference">
				<layout:grid cols="1" styleClass="LINK_GRID">	
					<layout:panel key="References" align="left" styleClass="PANEL_LINK">
					<p class="FIELD_BIG"><bean:write name="<%=formName%>" property="referenceText" filter="false"/></p>
					<layout:column>			
						<logic:iterate name="<%=formName%>" property="listReferences" id="item" indexId="index" type="ispyb.server.mx.vos.collections.IspybReference3VO">
							<layout:row>
								<html:link href="<%=item.getReferenceUrl() %>" target="_blank"><bean:write name="item" property="referenceName"/></html:link>
							</layout:row>
						</logic:iterate>
						<layout:submit><layout:message key="Download as BibTeX"/></layout:submit>
					</layout:column>
					</layout:panel>
				</layout:grid>
			</layout:form>
		</td>
		</logic:notEmpty>
		</tr>	
			
	</table>	