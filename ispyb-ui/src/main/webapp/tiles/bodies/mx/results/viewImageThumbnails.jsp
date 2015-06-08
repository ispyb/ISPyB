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

<%@ page import="ispyb.common.util.Constants"%>


<%
	String target = request.getContextPath() + "/user/viewResults.do?reqCode=viewJpegImage";
	String targetImageDownload = request.getContextPath() + "/user/imageDownload.do?reqCode=getImageJpgThumb";
%>

<layout:skin includeScript="true" />

<%-- BreadCrumbs bar --%>
<jsp:include page="/tiles/common/util/breadCrumbsBar.jsp" flush="true" />

<layout:skin />

<%-- Images list --%>

<layout:grid cols="1"  borderSpacing="10">
	<layout:column >

	<layout:panel key="Images collected" align="center" styleClass="PANEL">
	<layout:pager maxPageItems="<%=ispyb.common.util.Constants.MAX_PAG_ITEMS%>">
	
	<layout:collection name="viewResultsForm" property="listInfo" styleClass="LIST" id="currInfo">

			<layout:collectionItem title="Image" href="<%=target%>" paramId="imageId" paramProperty="imageId">
					<bean:define id="imageValue" name="currInfo" type="ispyb.server.mx.vos.collections.Image3VO"/>
			 		<img src="<%= targetImageDownload + "&amp;" + Constants.IMAGE_ID + "=" + imageValue.getImageId() %>" border="0" alt="Click to zoom the image" >
			 </layout:collectionItem>		

			<layout:collectionItem title="Image name" 	property="fileName" sortable="true" />
			<layout:collectionItem title="Image location" 	property="fileLocation" sortable="false" />

	</layout:collection>
	
	</layout:pager>
	</layout:panel>
	
	</layout:column >
</layout:grid>
