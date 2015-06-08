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
<%@ page import="ispyb.common.util.Constants"%>

<%@ page isELIgnored="false" %>

<html>
<title>Help page for Shipping</title>
<body>

<p align=justify><img src="<%=request.getContextPath()%>/images/help/fedexPlane.gif">
  A <B>Shipment</B> consists of a set of Dewars which is sent from your home lab to the synchrotron via a courier company. 
  Each dry shipping <B>Dewar</B> within the shipment is identified by a label (barcode or sticker). The dewars(s) contains a set of Containers (Pucks or  canes).
  <B>Containers</B> (typically Pucks), contain Samples. A <B>Sample</B> (Sample Holder) contains the <B>Crystal</B>.
</p>

<p align=center><img src="<%=request.getContextPath()%>/images/help/ShippingObjects_02.png"></p>


<p align=justify><img src="<%=request.getContextPath()%>/images/help/barcode_01.png">
<B>Tracking</B> your shipment & contents (Dewars, toolboxes etc) allows you to follow the progress of your shipment from your home Lab to <%=Constants.ESRF_DLS%>. This feature also provides for:
<UL>
<LI>Generation of Dewar and shipment labels for sending your shipment by courier</LI>
<LI>Notification by email of your shipment arrival and departure from <%=Constants.ESRF_DLS%>. Returned shipment notifications also include the courier tracking number to allow you to track the shipment while in transit back to your home laboratory</LI>
<LI>Location of your shipment at <%=Constants.ESRF_DLS%></LI>
</UL>
</p>
<c:choose>
	<c:when test="${SITE_ATTRIBUTE eq 'ESRF'}">
		<p align=center><img src="<%=request.getContextPath()%>/images/help/dewarTrackingWF_01.png"> </p>
	</c:when>
	<c:when test="${SITE_ATTRIBUTE eq 'MAXIV'}">
		<p align=center><img src="<%=request.getContextPath()%>/images/help/dewarTrackingWF_MAXIV.png"> </p>
	</c:when>
	<c:when test="${SITE_ATTRIBUTE eq 'SOLEIL'}">
		<p align=center><img src="<%=request.getContextPath()%>/images/help/dewarTrackingWF_01-base.png"> </p>
	</c:when>
	<c:otherwise>
		<p align=center><img src="<%=request.getContextPath()%>/images/help/dewarTrackingWF_01-base.png"> </p>
	</c:otherwise>
</c:choose>
</body>
</html>
