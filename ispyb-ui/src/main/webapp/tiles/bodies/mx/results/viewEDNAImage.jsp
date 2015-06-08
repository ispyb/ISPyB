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
	String targetImageDownload 		= request.getContextPath() + "/user/imageDownload.do?reqCode=getEDNAImage";
%>
<bean:define id="imagePath" name="viewResultsForm" property="imagePath" type="java.lang.String"/>

<%-- BreadCrumbs bar --%>
<jsp:include page="/tiles/common/util/breadCrumbsBar.jsp" flush="true" />

<layout:skin />

<layout:grid cols="1"  borderSpacing="5">
<layout:column>
	<layout:panel key="Full size Image" align="center" styleClass="PANEL">
				
	<html:img src="<%= targetImageDownload + \"&amp;\" + Constants.EDNA_IMAGE_PATH + \"=\" + imagePath %>" border="0" alt="Click to zoom the image" />
		
	</layout:panel>
</layout:column>
</layout:grid>

