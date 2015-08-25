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
<%@page import="ispyb.common.util.Constants"%>

<%@ page isELIgnored="false" %>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/alert.css" />

<layout:skin includeScript="true"/>
<script src="<%=request.getContextPath()%>/js/external/overlib/overlib.js"></script>
<script type='text/javascript' src="<%=request.getContextPath()%>/js/external/toggleDiv.js"></script>


<script TYPE="text/javascript">
	function changeRowStyle( tag , newStyle) {
	  var child = tag.firstChild;
	  while (child != null) {
	    child.className = newStyle;
	    child = child.nextSibling;
	  }
	}

</script>

<%-- the action is defined according to user role  --%>

<%
	String targetViewSession    		= request.getContextPath() + "/menuSelected.do?leftMenuId=-1&topMenuId=16&targetUrl=/user/viewDataCollectionGroup.do?reqCode=display";
	String targetViewSessionSummary    		= request.getContextPath() + "/menuSelected.do?leftMenuId=-1&topMenuId=16&targetUrl=/user/viewSessionSummary.do?reqCode=display";
	String targetViewSessionBX    		= request.getContextPath() + "/menuSelected.do?leftMenuId=-1&topMenuId=16&targetUrl=/user/viewProjectList.do?reqCode=display";

	String targetViewSessionCol    		= request.getContextPath() + "/menuSelected.do?leftMenuId=-1&topMenuId=16&targetUrl=/user/viewDataCollection.do?reqCode=displayForSession";
	String targetDeleteSession 		= request.getContextPath() + "/menuSelected.do?leftMenuId=-1&topMenuId=16&targetUrl=/user/viewSession.do?reqCode=deleteSession";
	String targetExportSession 		= request.getContextPath() + "/menuSelected.do?leftMenuId=-1&topMenuId=16&targetUrl=/user/viewExportSession.do?reqCode=exportSession";
	String targetEditSession 		= request.getContextPath() + "/menuSelected.do?leftMenuId=-1&topMenuId=16&targetUrl=/user/editSession.do?reqCode=display";	
	String targetSubmitReport 		= request.getContextPath() + "/menuSelected.do?leftMenuId=-1&topMenuId=16&targetUrl=/user/SendReportAction.do?reqCode=display";
	String targetExportToGoogleCalendar	= request.getContextPath() + "/menuSelected.do?leftMenuId=-1&topMenuId=16&targetUrl=/user/viewSession.do?reqCode=exportToGoogleCalendar";
	String targetExportToGoogleCalendar2	= request.getContextPath() + "/menuSelected.do?leftMenuId=-1&topMenuId=16&targetUrl=/user/viewSession.do";
	String targetMail = "mailto:";
	String targetSMIS = "https://wwws.esrf.fr/misapps/SMISWebClient/protected/aform/manageAForm.do?action=view&currentTab=howtoTab";
	if (Constants.SITE_IS_EMBL()){ 
	    targetSMIS = "https://smis.embl-hamburg.de/misapps/SMISWebClient/protected/aform/manageAForm.do?action=view&currentTab=howtoTab";
	}    
%>

<jsp:include page="/tiles/common/util/breadCrumbsBar.jsp" flush="true" />

<%-- Search sessions toggle --%>	
<a class="noUnderline" onmouseover="return overlib('Expand/collapse Search Form');" onmouseout="return nd();" href="javascript:;" onclick="toggleDiv('searchDiv');">
  <p class="toggleText">&nbsp;&nbsp;Search <%=Constants.SESSION_VISIT_CAP%>s&nbsp;<img src="<%=request.getContextPath()%>/images/doubleArrowDesc.png" border=0></p>
</a>
<div id="searchDiv" style="display:none">
	<layout:grid cols="1" borderSpacing="10">
	
		<%-- Search Form --%>
		<layout:column >
		<jsp:include page="session.jsp" flush="true" />
		</layout:column>
	
	</layout:grid>
</div>


<%-- To not show a empty table when no session exists --%>	
<logic:empty name="viewSessionForm" property="listInfo">
           <h4>No&nbsp;Sessions&nbsp;have&nbsp;been&nbsp;found</h4>
</logic:empty>

<logic:notEmpty name="viewSessionForm" property="listInfo">
<bean:define name="viewSessionForm" property="sessionsTitle" id="titleForSessions" type="java.lang.String" toScope="page"/>



<layout:grid cols="1" borderSpacing="10">	
	<%-- Export to Google Calendar --%>
	<c:if test="${SITE_ATTRIBUTE eq 'DLS'}">
		<layout:column >
			<form name="exportToGoogleCalendar_form" method="POST" action="<%=targetExportToGoogleCalendar%>">
				<a href="#" onclick="javascript:getGoogleInfo();" class="PANEL_BREAD_CRUMBS">Export to <img src="<%=request.getContextPath()%>/images/google_calendar.gif" border="0" align="middle"></a>
		       	</form>
		       	
		       	<%-- Acknowledge Action --%>
			<jsp:include page="/tiles/common/messages/error.jsp" flush="true" />
			<logic:messagesNotPresent>
				<jsp:include page="/tiles/common/messages/info.jsp" flush="true" />
			</logic:messagesNotPresent>
			<logic:messagesPresent message="true">
				<a href="http://www.google.com/calendar" class="PANEL_BREAD_CRUMBS" target="_new">Go to <img src="<%=request.getContextPath()%>/images/google_calendar2.gif" border="0" align="middle"></a>
			</logic:messagesPresent>
		</layout:column>
   	</c:if> 
	<%-------------------------------%>
	
	<%-- Sessions list --%>
	<layout:column >
	<layout:panel key="<%=titleForSessions%>" align="center" styleClass="PANEL">
	<layout:pager maxPageItems="20" styleClass="LIST">
	<layout:collection 	name="viewSessionForm" property="listSessionInformation" id="currInfo"  indexId="currInfoIndex" styleClass="LIST" onRowMouseOver="changeRowStyle(this,'LIST_OVER')" onRowMouseOut="changeRowStyle

(this,'LIST')" onRowClick="changeRowStyle(this,'LIST')">
<c:if test="${SITE_ATTRIBUTE eq 'DLS'}">		
		<layout:collectionItem title="Visit Number" property="visit_number" sortable="true" />
		<layout:collectionItem title="Start Date" property="startDate" sortable="true">
			<logic:equal name="currInfo"  property="hasDataCollectionGroup" value="false">
				<bean:write name="currInfo" property="startDate" format="dd-MM-yyyy"/>
			</logic:equal>
			<logic:equal name="currInfo"  property="hasDataCollectionGroup" value="true">
				<html:link href="<%=targetViewSession%>" paramName="currInfo" paramId="sessionId" paramProperty="sessionId" styleClass="LIST">
					<bean:write name="currInfo" property="startDate" format="dd-MM-yyyy"/>	
				</html:link>			
			</logic:equal>
		</layout:collectionItem>
</c:if>	

<c:if test="${SITE_ATTRIBUTE eq 'ESRF'}">
			<layout:collectionItem title="Start Date" property="startDate" sortable="true">
				<logic:equal name="currInfo"  property="beamlineName" value="BM29">
					<html:link href="<%=targetViewSessionBX%>" paramName="currInfo" paramId="sessionId" paramProperty="sessionId" styleClass="LIST">
						<bean:write name="currInfo" property="startDate" format="dd-MM-yyyy"/>	
						
					</html:link>	
				</logic:equal>
				
				<logic:notEqual name="currInfo"  property="beamlineName" value="BM29">
					<html:link href="<%=targetViewSessionSummary%>" paramName="currInfo" paramId="sessionId" paramProperty="sessionId" styleClass="LIST">
						<bean:write name="currInfo" property="startDate" format="dd-MM-yyyy"/>	
					</html:link>	
				</logic:notEqual>
			</layout:collectionItem>
</c:if>

<c:if test="${SITE_ATTRIBUTE eq 'EMBL'}">
			<layout:collectionItem title="Start Date" property="startDate" sortable="true">
				<logic:equal name="currInfo"  property="beamlineName" value="P12">
					<html:link href="<%=targetViewSessionBX%>" paramName="currInfo" paramId="sessionId" paramProperty="sessionId" styleClass="LIST">
						<bean:write name="currInfo" property="startDate" format="dd-MM-yyyy"/>	
						
					</html:link>	
				</logic:equal>
				
				<logic:notEqual name="currInfo"  property="beamlineName" value="P12">
					<html:link href="<%=targetViewSessionSummary%>" paramName="currInfo" paramId="sessionId" paramProperty="sessionId" styleClass="LIST">
						<bean:write name="currInfo" property="startDate" format="dd-MM-yyyy"/>	
					</html:link>	
				</logic:notEqual>
			</layout:collectionItem>
</c:if>

<c:if test="${SITE_ATTRIBUTE eq 'MAXIV'}">
			<layout:collectionItem title="Start Date" property="startDate" sortable="true">
				<logic:equal name="currInfo"  property="beamlineName" value="MX">
					<html:link href="<%=targetViewSessionBX%>" paramName="currInfo" paramId="sessionId" paramProperty="sessionId" styleClass="LIST">
						<bean:write name="currInfo" property="startDate" format="dd-MM-yyyy"/>	
						
					</html:link>	
				</logic:equal>
				
				<logic:notEqual name="currInfo"  property="beamlineName" value="MX">
					<html:link href="<%=targetViewSessionSummary%>" paramName="currInfo" paramId="sessionId" paramProperty="sessionId" styleClass="LIST">
						<bean:write name="currInfo" property="startDate" format="dd-MM-yyyy"/>	
					</html:link>	
				</logic:notEqual>
			</layout:collectionItem>
</c:if>
		
<c:if test="${SITE_ATTRIBUTE eq 'SOLEIL'}">
			<layout:collectionItem title="Start Date" property="startDate" sortable="true">
				<logic:equal name="currInfo"  property="beamlineName" value="BM29">
					<html:link href="<%=targetViewSessionBX%>" paramName="currInfo" paramId="sessionId" paramProperty="sessionId" styleClass="LIST">
						<bean:write name="currInfo" property="startDate" format="dd-MM-yyyy"/>	
						
					</html:link>	
				</logic:equal>
				
				<logic:notEqual name="currInfo"  property="beamlineName" value="BM29">
					<html:link href="<%=targetViewSessionSummary%>" paramName="currInfo" paramId="sessionId" paramProperty="sessionId" styleClass="LIST">
						<bean:write name="currInfo" property="startDate" format="dd-MM-yyyy"/>	
					</html:link>	
				</logic:notEqual>
			</layout:collectionItem>
</c:if>

			<layout:collectionItem title="End Date" 	property="endDate" sortable="true" type="date"/>
			<layout:collectionItem title="Beamline" 	property="beamlineName" sortable="true"/>
			<layout:collectionItem title="Local contact" 	property="beamlineOperator" sortable="false">
				<logic:present name="currInfo" property="beamLineOperatorEmail">
					<logic:equal name="currInfo" property="beamLineOperatorEmail" value="">
						<bean:write name="currInfo"	property="beamlineOperator"/>
					</logic:equal>
					<logic:notEqual name="currInfo" property="beamLineOperatorEmail" value="">
						<bean:define id="email" name="currInfo" property="beamLineOperatorEmail" type="java.lang.String"></bean:define>
						<html:link href="<%= targetMail + email  %>" paramName="currInfo" styleClass="LIST" onmouseover="<%= \"return overlib('mail to: \"+email+\"')\"%>" onmouseout="return nd();">
							<bean:write name="currInfo"	property="beamlineOperator"/>
						</html:link>
					</logic:notEqual>
				</logic:present>
				<logic:notPresent name="currInfo" property="beamLineOperatorEmail">
					<bean:write name="currInfo"	property="beamlineOperator"/>
				</logic:notPresent>
			</layout:collectionItem>
			<%-- proposal only for manager or localContact --%>
			<logic:present name="breadCrumbsForm" property="proposalNumber">
			        <logic:equal name="breadCrumbsForm" property="proposalNumber" value="<%=Constants.PROPOSAL_NUMBER_MANAGER%>" >
			        	<layout:collectionItem title="Proposal" name="currInfo"	 property="proposalCodeNumber" sortable="false"/>
			        </logic:equal>
			        <logic:equal name="breadCrumbsForm" property="proposalNumber" value="<%=Constants.PROPOSAL_NUMBER_LOCAL_CONTACT%>" >
			        	<layout:collectionItem title="Proposal" name="currInfo"	 property="proposalCodeNumber" sortable="false"/>
			        </logic:equal>
			</logic:present>
			
			<layout:collectionItem title="# Shifts" 	property="nbShifts" sortable="false"/>
			<%-- display sessionTitle only for FX accounts --%>
			<logic:present name="breadCrumbsForm"  property="proposalCode">
		  		<logic:equal name="breadCrumbsForm" property="proposalCode" value="<%=Constants.PROPOSAL_CODE_FX%>" >
					<layout:collectionItem title="Title" 	property="sessionTitle" sortable="false"/>
				</logic:equal>
			</logic:present>
			<layout:collectionItem title="Comments" 	width="400" sortable="false">
			 	<div align="left"><bean:write name="currInfo"	property="comments"/></div>
			</layout:collectionItem>

			<%-- Edit session --%>
			<logic:present name="breadCrumbsForm"  property="userRole">
	        	<logic:notEqual name="breadCrumbsForm"  property="userRole" value="<%=Constants.ROLE_INDUSTRIAL%>" >
					<layout:collectionItem title="Edit" styleClass="LIST" width="50">	
						<html:link href="<%=targetEditSession%>" paramName="currInfo" paramId="sessionId" paramProperty="sessionId" >
							<img src="<%=request.getContextPath()%>/images/Edit_16x16_01.png" border=0 onmouseover="return overlib('Edit');" onmouseout="return nd();">
						</html:link>
					</layout:collectionItem>
				</logic:notEqual>
			</logic:present>

			<%-- View session: summary --%>
			<layout:collectionItem title="View session report" styleClass="LIST" width="50">	
				<logic:equal name="currInfo"  property="hasDataCollectionGroup" value="true">										
						<html:link href="<%=targetViewSessionSummary%>" paramName="currInfo" paramId="sessionId" paramProperty="sessionId" >
							<img src="<%=request.getContextPath()%>/images/magnif_16.png" border=0 onmouseover="return overlib('View session report');" onmouseout="return nd();">
						</html:link>
				</logic:equal>
			</layout:collectionItem>
			
			<%-- View session: groups --%>
			<layout:collectionItem title="View collections groups" styleClass="LIST" width="50">	
				<logic:equal name="currInfo"  property="hasDataCollectionGroup" value="true">										
						<html:link href="<%=targetViewSession%>" paramName="currInfo" paramId="sessionId" paramProperty="sessionId" >
							<img src="<%=request.getContextPath()%>/images/magnif_16.png" border=0 onmouseover="return overlib('View all grouped collects for this session');" onmouseout="return nd();">
						</html:link>
				</logic:equal>
			</layout:collectionItem>
			
			<%-- View session: collect --%>
			<layout:collectionItem title="View collections" styleClass="LIST" width="50">	
				<logic:equal name="currInfo"  property="hasDataCollectionGroup" value="true">										
						<html:link href="<%=targetViewSessionCol%>" paramName="currInfo" paramId="sessionId" paramProperty="sessionId" >
							<img src="<%=request.getContextPath()%>/images/magnif_16.png" border=0 onmouseover="return overlib('View alls collects for this session');" onmouseout="return nd();">
						</html:link>
				</logic:equal>
			</layout:collectionItem>
						
			<%-- Export session --%>
			<logic:present name="breadCrumbsForm" property="proposalCode">
				<logic:equal name="breadCrumbsForm" property="proposalCode" value="<%=Constants.PROPOSAL_CODE_BM14%>" >
					<layout:collectionItem title="Export" styleClass="LIST" width="50">	
						<html:link href="<%=targetExportSession%>" paramName="currInfo" paramId="sessionId" paramProperty="sessionId" styleClass="LIST">
							<img src="<%=request.getContextPath()%>/images/export.gif" border=0 onmouseover="return overlib('Export information.');" onmouseout="return nd();">
							Export
						</html:link>
					</layout:collectionItem>
				</logic:equal>
			</logic:present>

			<%-- Submit Report --%>
			<logic:equal name="viewSessionForm"  property="allowedToSubmitReport" value="true" >
				<layout:collectionItem title="Submit Report" styleClass="LIST" width="50">	
					<html:link href="<%=targetSubmitReport%>" paramName="currInfo" paramId="sessionId" paramProperty="sessionId" styleClass="LIST">
						<img src="<%=request.getContextPath()%>/images/submitReport_pdf.gif" border=0 onmouseover="return overlib('Submit Report.');" onmouseout="return nd();">
						Submit Report
					</html:link>
				</layout:collectionItem>
			</logic:equal>

			<%-- delete only for manager --%>
			<logic:present name="breadCrumbsForm" property="proposalNumber">
			        <logic:equal name="breadCrumbsForm" property="proposalNumber" value="<%=Constants.PROPOSAL_NUMBER_MANAGER%>" >
						<layout:collectionItem title="Delete" styleClass="LIST" width="50">	
							<html:link href="<%=targetDeleteSession%>" paramName="currInfo" paramId="sessionId" paramProperty="sessionId" onclick="return window.confirm('Do you really want to delete this 

session?');">
								<img src="<%=request.getContextPath()%>/images/cancel.png" border=0 onmouseover="return overlib('Delete');" onmouseout="return nd();">
							</html:link>
						</layout:collectionItem>
					</logic:equal>
			</logic:present>
			<%-- Issue 1440: link to the A-form --%>
			<c:if test="${SITE_ATTRIBUTE eq 'ESRF'}">
			<layout:collectionItem title="SMIS A-Form" styleClass="LIST" width="50">
				<logic:present name="currInfo" property="expSessionPk">
						<html:link target="_blank" href="<%=targetSMIS%>" paramName="currInfo" paramId="expSessionVO.pk" paramProperty="expSessionPk" styleClass="LIST">
							A-Form
						</html:link>
						</logic:present>
			</layout:collectionItem>
			</c:if>
			<c:if test="${SITE_ATTRIBUTE eq 'EMBL'}">
			<layout:collectionItem title="SMIS A-Form" styleClass="LIST" width="50">
				<logic:present name="currInfo" property="expSessionPk">
						<html:link target="_blank" href="<%=targetSMIS%>" paramName="currInfo" paramId="expSessionVO.pk" paramProperty="expSessionPk" styleClass="LIST">
							A-Form
						</html:link>
						</logic:present>
			</layout:collectionItem>
			</c:if>
			<c:if test="${SITE_ATTRIBUTE eq 'SOLEIL'}">
			<layout:collectionItem title="SMIS A-Form" styleClass="LIST" width="50">
				<logic:present name="currInfo" property="expSessionPk">
						<html:link target="_blank" href="<%=targetSMIS%>" paramName="currInfo" paramId="expSessionVO.pk" paramProperty="expSessionPk" styleClass="LIST">
							A-Form
						</html:link>
						</logic:present>
			</layout:collectionItem>
			</c:if>
							       			                       	           
	</layout:collection>
	</layout:pager>
	</layout:panel>
	
	</layout:column>
</layout:grid>




</logic:notEmpty>

<%-- Export to Google Calendar --%>
<c:if test="${SITE_ATTRIBUTE eq 'DLS'}">
	<div id='alert' style='display : none'>
	    <div id='horizon'>
	        <div id='alert_cadre'>
	            <div id='alert_text'>
	                <table width='100%' height='100%' border=0 cellpadding=0 cellspacing=0>
	                     <tr>
	                     
	                     <td valign='middle' align='center' width='100%' height='100%' id='alert_cell'>
	                     
	                <font style='text-decoration:underline'>Enter Google account login details</font><br /><br />
				<table width='90%' border='0' cellspacing='0' cellpadding='0'>
				
					<tr>
						<td align='middle'>
						<layout:form action="/user/exportToGoogle.do" reqCode="exportToGoogleCalendar" >
						<layout:row>
						<layout:select key="Export future and past" 	property="googleExportPastMonth">						 
			                               <layout:option value="0"/>   
			                               <layout:option value="1"/>
			                               <layout:option value="2"/>   
			                               <layout:option value="3"/>      
			                               <layout:option value="4"/>
			                               <layout:option value="5"/>
			                               <layout:option value="6"/>                      
			                	</layout:select><layout:message key="month(s)"/>
			                	</layout:row>
						       	<layout:text key="Username :" property="googleUsername"  mode="E,E,E"/>
						       	<layout:password key="Password :" property="googlePassword"  mode="E,E,E"/>
						       	<layout:space/>
						       	<layout:row>
								<layout:submit>
									<layout:message key="Export"/>
								</layout:submit>
							<layout:button onclick="javascript:Effect.BlindUp('alert', {duration: 0.3});">
								<layout:message key="Cancel"/>
							</layout:button>
							</layout:row>
					    	</layout:form>
						</td>  
					</tr>
					<tr>
					<td></td>
					</tr>     	
				</table>
	                     </td>
	                     </tr></table>
	            </div>
	        </div>
	    </div>
	</div>
</c:if> 
<%-------------------------------%>
