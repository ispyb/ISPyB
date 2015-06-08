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

<%
	String targetUpdateDB 	= request.getContextPath() + "/updateDB.do?reqCode=updateProposal"; 
	String latestISPyBNews = "Latest ISPyB News & Information";
	if (Constants.SITE_IS_MAXIV()){
		latestISPyBNews += " (@esrf)";
	}
%>

<h1> Welcome to industrial user :  <bean:write name="<%=Constants.PROPOSAL_CODE%>"/><bean:write name="<%=Constants.PROPOSAL_NUMBER%>"/> </h1>

<p align="center">
<a href="<%=Constants.getProperty("ISPyB.news.url")%>"><img src="<%=request.getContextPath()%>/images/information.gif" border=0><%=latestISPyBNews%></a>
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

<h2>"Data collection" tab</h2>
<p>
Click on this tab to deal with the data collection you perform on your samples.
<br> You will be able to retrieve information about a particular session
<br> You will be able to retrieve information about a particular data collection
<br> You will be able to retrieve information about a particular protein
<br> You will be able to retrieve information about a particular sample
</p>

<%-- for later
<h2>"Reports" tab</h2>
<p>
Click on this tab to create/edit all types of reports you need.
<br> pdf/html reports
<br> xml files
</p>
--%>
