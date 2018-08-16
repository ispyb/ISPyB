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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<%@page import="ispyb.common.util.Constants"%>

<%@ page isELIgnored="false" %>

<html>
<title>Help page for Prepare Experiment</title>
<body>

<%-- Prepare your experiment --%>
<h4>Prepare your experiment:</h4>  
<p align="justify">

	<%-- 1) Select shipment --%>
	<span class="greyText">
		1- Select the dewars you want for processing.<br>
	</span>

	<%-- 2) Fill Sample changer --%>
	<span class="greyText">
	<c:if test="${SITE_ATTRIBUTE eq 'ESRF'}"> 
		2- Fill the sample changer: assign a location for your containers (only required if not using Damatrix codes)<br>
	</c:if>
	<c:if test="${SITE_ATTRIBUTE eq 'DLS'}">
		2- Fill the sample changer: assign a location for your containers<br>
	</c:if> 
	<c:if test="${SITE_ATTRIBUTE eq 'EMBL'}">
		2- Fill the sample changer: assign a location for your containers<br>
	</c:if>
	<c:if test="${SITE_ATTRIBUTE eq 'MAXIV'}">
		2- Fill the sample changer: assign a location for your containers<br>
	</c:if> 
	<c:if test="${SITE_ATTRIBUTE eq 'SOLEIL'}">
		2- Fill the sample changer: assign a location for your containers<br>
	</c:if>
	</span>

	<%-- 3) Link crystals --%>
	<span class="greyText">
	<c:if test="${BCM_ATTRIBUTE ne 'mxcube'}">
		3- Associate data collections to samples in <%=Constants.BCM_NAME%>.<br>
	</c:if> 
	<c:if test="${BCM_ATTRIBUTE eq 'mxcube'}"> 
		3- In MxCuBe, link the samples in the container to the crystals in the Sample Changer.<br>
	</c:if>
	</span>
	
	<%-- Steps --%>
	<br>
	<img src="<%=request.getContextPath()%>/images/Previous_disabled-16x16.png" border=0>
	<a href="<%=request.getContextPath()%>/user/prepareExp.do?reqCode=selectDewar" title="Select the dewars">
	<img src="<%=request.getContextPath()%>/images/Next-16x16.png" border=0></a>
	<input type=button value='First step: Select the dewars' onclick="parent.location='<%=request.getContextPath()%>/user/prepareExp.do?reqCode=selectDewar'">	

</p>

<p align=center><img src="<%=request.getContextPath()%>/images/help/sampleLinkingWF_01.png"></p>

<h4>Linking Samples to Data Collection: how does it help my experiment?</h4>
<p align="justify">
Linking Samples of your Shipment (sample descriptions) with Crystals in the Sample changer (physical samples) will allow you to associate 
the Data Collections made on these crystals to their Sample description. Then, you will 
be able to make Sample Ranking or to make reports that can be processed by your LIMS. 
</p>

<p align=center><img src="<%=request.getContextPath()%>/images/help/sampleLinking_01.png"> </p>

</body>
</html>
