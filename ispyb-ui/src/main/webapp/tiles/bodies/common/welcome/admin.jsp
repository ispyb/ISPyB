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

<%@page import="ispyb.common.util.Constants"%>

<%@ taglib uri="http://struts.application-servers.com/layout" prefix="layout" %>

<%
	String selectedProposal = request.getContextPath() + 
	  "/admin/welcomeAdmin.do?reqCode=displayProposals";
%>

<layout:skin includeScript="true" />
<script src="<%=request.getContextPath()%>/js/external/overlib/overlib.js"></script>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>

<layout:space/>

<div align="center">

<%-- Page --%>

<layout:grid cols="1"  borderSpacing="10">
	<%-- Proposals list --%>
	<layout:column >
	<layout:panel key="Please click on any row to use the corresponding proposal" align="center" styleClass="PANEL">
	<layout:pager maxPageItems="30" styleClass="LIST">
	<layout:collection 	name="viewProposalForm" property="listInfo" id="currInfo" styleClass="LIST">
				
		<%-- Select proposal --%>

 		<layout:collectionItem title="Title" 	property="title" sortable="true" 
		href="<%=selectedProposal%>" paramName="currInfo" paramId="proposalId" paramProperty="proposalId"/>
		<layout:collectionItem title="Code" 	property="code" sortable="true"
		href="<%=selectedProposal%>" paramName="currInfo" paramId="proposalId" paramProperty="proposalId"/>
		<layout:collectionItem title="Number" 	property="number" sortable="true"
		href="<%=selectedProposal%>" paramName="currInfo" paramId="proposalId" paramProperty="proposalId"/>

	</layout:collection>
	</layout:pager>
	</layout:panel>
	</layout:column>
	
</layout:grid>

</div>

