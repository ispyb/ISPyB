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

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="ispyb.common.util.Constants"%>

<%@ taglib uri="http://struts.application-servers.com/layout" prefix="layout" %>

<%
	String targetUpdateDB 	= request.getContextPath() + "/updateDB.do?reqCode=updateProposal"; 
	String selectedProposal = request.getContextPath() + "/user/welcomePerson.do?reqCode=displayProposals";
	
	if (request.getSession().getAttribute(Constants.TECHNIQUE) == null){
		request.getSession().setAttribute(Constants.TECHNIQUE, "NA");
	}
	
	String latestISPyBNews = "Latest ISPyB News & Information";
	if (Constants.SITE_IS_MAXIV()){
		latestISPyBNews += " (@esrf)";
	}
%>

<c:if test="${PROPOSAL_LIST_DISPLAY_ATTRIBUTE eq 'true'}">

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
</c:if>

<c:if test="${PROPOSAL_LIST_DISPLAY_ATTRIBUTE ne 'true'}">


<h1> Welcome to User : <bean:write name="<%=Constants.PROPOSAL_CODE%>"/><bean:write name="<%=Constants.PROPOSAL_NUMBER%>"/> </h1>

<c:if test="${sessionScope.TECHNIQUE eq 'MX' || sessionScope.PROPOSAL_TYPE eq 'MX'}">
		<p align="center">
		<a href="<%=Constants.getProperty("ISPyB.news.url")%>" target=_blank><img src="<%=request.getContextPath()%>/images/information.gif" border=0><%=latestISPyBNews%></a>
		</p>
		
		<h2>In case of problems when creating shipments/samples, <a href="<%=targetUpdateDB%>" >update ISPyB database</a> (this may take a few minutes).
		</h2>
		<br>
		<h2> "Shipment" tab</h2>
		<p>
		Click on this tab to deal with the samples you are planning to send by courier.
		<br> You will be able to define an electronic shipment, containing electronic dewars and containers
		<br> You will be able enter the samples description based on the protein you have submitted through "samplesheets".
		<br> You will be able to retrieve information about the shipments, dewars and containers already submitted.
		</p>
		<h2>"Samples" tab</h2>
		<p>
		Click on this tab to deal with data concerning your proteins, crystals and samples.
		<br> You will be able to create new samples for experiment: samples description will be based on the protein you have submitted through "samplesheets".
		<br> You will be able to add/edit a new crystal form for your protein
		<br> You will be able to view the lists of your proteins, crystal forms, samples
		<br> You will be able to edit the diffraction plans linked to your samples
		</p>
		
		<h2>"Prepare experiment" tab</h2>
		<p>
		Click on this tab to prepare the list of samples you want to see during experiment.
		<br>You will also need to fill the sample changer manually if you are not using pins with DM codes
		</p>
		
		<h2>"Data collection" tab</h2>
		<p>
		Click on this tab to deal with the data collection you perform on your samples.
		<br> You will be able to retrieve information about a particular session
		<br> You will be able to retrieve information about a particular data collection
		<br> You will be able to retrieve information about a particular protein
		<br> You will be able to retrieve information about a particular sample
		</p>
</c:if>

<c:if test="${sessionScope.TECHNIQUE eq 'BX' || sessionScope.PROPOSAL_TYPE eq 'BX'}">
 <%@ include file="/tiles/bodies/biosaxs/welcome.jsp" %>
 
 
</c:if>
<%-- for later
<h2>"Reports" tab</h2>
<p>
Click on this tab to create/edit all types of reports you need.
<br> pdf/html reports
<br> xml files
</p>
--%>
</c:if>

