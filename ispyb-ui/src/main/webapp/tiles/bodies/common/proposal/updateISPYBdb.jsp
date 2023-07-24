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

<layout:skin includeScript="true" />
<script src="<%=request.getContextPath()%>/js/external/overlib/overlib.js"></script>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>

<h3> Update of Ispyb database</h3>

<logic:equal name="proposalAndSessionAndProteinForm" property="userIsManager" value="true" >

<layout:row>

<layout:panel key="Date for update" align="left" styleClass="PANEL">
<layout:form action="/updateDB.do" reqCode="update">
	
	<layout:grid cols="1" styleClass="SEARCH_GRID">	
		<layout:column>
					<layout:text key="Start date (dd/mm/yyyy)" 	property="startDate" 	styleClass="FIELD" 	mode="E,E,E"/>
					<layout:text key="End date (dd/mm/yyyy)" 	property="endDate" 	styleClass="FIELD" 	mode="E,E,E"/>
		</layout:column>		
			
		<layout:row>
			<layout:reset/>
			<layout:submit><layout:message key="update"/></layout:submit>
		</layout:row>
	</layout:grid>
	
</layout:form>	
</layout:panel>

</layout:row>
<layout:space/>
<layout:row>

<layout:panel key="Proposal to update" align="left" styleClass="PANEL">
<layout:form action="/updateDB.do" reqCode="updateProposal">
	
	<layout:grid cols="1" styleClass="SEARCH_GRID">	
		<layout:column>
					<layout:select key="Proposal code" 	property="proposalCode" 	styleClass="FIELD" 	>						 
                               <layout:option value="FX"/>   
                               <layout:option value="MX"/>
                               <layout:option value="BX"/>
                               <layout:option value="IX"/>   
                               <layout:option value="IN"/>
                               <layout:option value="IM"/>
                               <layout:option value="IH-LS"/>
                               <layout:option value="IH-MX"/>
                               <layout:option value="IH-SC"/>
                               <layout:option value="BLC"/>
                               <layout:option value="BM161"/>
                               <layout:option value="SC"/>
                               <layout:option value="TC"/>
                               <layout:option value="A07-1"/>
                	</layout:select>
					<layout:text key="Proposal number" 	property="proposalNumberSt" 	styleClass="FIELD" mode="E,E,E"/>

		</layout:column>		
			
		<layout:row>
			<layout:reset/>
			<layout:submit><layout:message key="update"/></layout:submit>
		</layout:row>
	</layout:grid>
	
</layout:form>
	
</layout:panel>

</layout:row>
<layout:space/>

<layout:row>

<layout:panel key="Launch batch update" align="left" styleClass="PANEL">
<layout:form action="/updateDB.do" reqCode="launchBatchUpdate">
	<layout:grid cols="1" styleClass="SEARCH_GRID">	
		<layout:column>	
			<layout:submit><layout:message key="launchBatchUpdate"/></layout:submit>
		</layout:column>
	</layout:grid>	
</layout:form>	
</layout:panel>

</layout:row>

</logic:equal>

<%-- Acknowledge Action --%>
<jsp:include page="/tiles/common/messages/info.jsp" flush="true" />

<%-- Errors --%>
<jsp:include page="/tiles/common/messages/error.jsp" flush="true" />
