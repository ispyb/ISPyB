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
<title>Help page for MxCube</title>
<body>

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
	<b>
	<c:if test="${BCM ne 'mxcube'}">
		3- Associate data collections to samples in <%=Constants.BCM_NAME%>.<br>
	</c:if> 
	<c:if test="${BCM eq 'mxcube'}"> 
		3- In MxCuBe, link the samples in the container to the crystals in the Sample Changer.<br>
	</c:if>
	</b>
	
	<%-- Steps --%>
	<br>
	<a class="noUnderline" href="<%=request.getContextPath()%>/user/fillSampleChanger.do?reqCode=display" title="Fill the sample changer">
	<img src="<%=request.getContextPath()%>/images/Previous-16x16.png" border=0>
	</a>
	<img src="<%=request.getContextPath()%>/images/Next_disabled-16x16.png" border=0>
	</a>	
	
</p>

<%-- we should use BCM instead of BCM_ATTRIBUTE, but the text content for mxcube is so old that I prefer not to change it!  --%>
<c:if test="${BCM ne 'mxcube'}">
<h4>Now, go to <%=Constants.BCM_NAME%> and associate the data collections to their respective samples.</h4>
</c:if> 
<c:if test="${BCM eq 'mxcube'}">
	<c:if test="${SITE_ATTRIBUTE eq 'MAXIV'}">
		<p align=center><img src="<%=request.getContextPath()%>/images/help/mxcube3-samples.png"> </p>
	</c:if>
	<c:if test="${SITE_ATTRIBUTE ne 'MAXIV'}">
		<p align=center><img src="<%=request.getContextPath()%>/images/help/mxcube-scan.png"> </p>
	</c:if>
</c:if> 

</body>
</html>
