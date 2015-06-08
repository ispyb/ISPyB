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

<% 
	String targetJpeg = request.getContextPath() + "/user/viewResults.do?reqCode=viewJpegImage";
	String targetThumbs = request.getContextPath() + "/user/viewResults.do?reqCode=display";
%>

<%-- Selected  Image --%>
<logic:present name="breadCrumbsForm" property="selectedImage" scope="session">
	<bean:define name="breadCrumbsForm" property="selectedImage" 	scope="session" id="selectedImage" 	type="ispyb.server.mx.vos.collections.Image3VO"/>
	<%-- Selected Image Info --%>
	<layout:column styleClass="PANEL_BREAD_CRUMBS">
		<layout:panel key="Selected Image" align="left" styleClass="PANEL_BREAD_CRUMBS_ITEM">
			
			<layout:text key="Image name" 	name="selectedImage" 	property="fileName" styleClass="PANEL_BREAD_CRUMBS_FIELD" mode="I,I,I" />
			<layout:text key="Filename" 	name="selectedImage" 	property="fileName" styleClass="PANEL_BREAD_CRUMBS_FIELD" mode="I,I,I" />
			<logic:present name="breadCrumbsForm" property="selectedDataCollection" scope="session">
				<bean:define name="breadCrumbsForm" property="selectedDataCollection" 	scope="session" id="selectedDataCollection" 	type="ispyb.server.mx.vos.collections.DataCollection3VO"/>
			<tr><th class="PANEL_BREAD_CRUMBS_FIELD" valign="top"></th><td class="PANEL_BREAD_CRUMBS_FIELD" valign="top" style="">
	            <html:link href="<%=targetThumbs%>" paramId="dataCollectionId" paramName="selectedDataCollection" paramProperty="dataCollectionId" styleClass="PANEL_BREAD_CRUMBS_FIELD">
					Back to image thumbnails
				</html:link>
			</td></tr>
			</logic:present>
			<tr><th class="PANEL_BREAD_CRUMBS_FIELD" valign="top"></th><td class="PANEL_BREAD_CRUMBS_FIELD" valign="top" style="">
	            <html:link href="<%=targetJpeg%>" paramId="imageId" paramName="selectedImage" paramProperty="imageId" styleClass="PANEL_BREAD_CRUMBS_FIELD">
					View this image as Jpeg
				</html:link>
			</td></tr>
		</layout:panel>
	</layout:column>
</logic:present>
