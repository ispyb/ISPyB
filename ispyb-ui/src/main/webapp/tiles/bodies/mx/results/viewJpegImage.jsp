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
	String target 				= request.getContextPath() + "/user/viewResults.do?reqCode=viewImageApplet";
	String targetImageDownload 		= request.getContextPath() + "/user/imageDownload.do?reqCode=getImageJpg";
	String targetImageDownloadFile 		= request.getContextPath() + "/user/imageDownload.do?reqCode=getImageJpgFromFile";
	String targetViewJpegImage 		= request.getContextPath() + "/user/viewResults.do?reqCode=viewJpegImage";
%>

<%-- BreadCrumbs bar --%>
<jsp:include page="/tiles/common/util/breadCrumbsBar.jsp" flush="true" />

<layout:skin />

<layout:grid cols="1"  borderSpacing="5">
<layout:column>
		<layout:panel key="Full size Image" align="center" styleClass="PANEL">
		
			<%------------- Datacollection Image ---------------------%>
			<logic:present name="viewResultsForm"  property="image">
				<bean:define name="viewResultsForm" property="image" id="image" type="ispyb.server.mx.vos.collections.Image3VO"/>
				<layout:form action="/user/viewResults.do" reqCode="viewJpegImage">	
				<layout:text name="viewResultsForm"  property="currentImageId" value="<%=image.getImageId().toString()%>" mode="H,H,H" />
					<layout:row>
						<layout:grid cols="2"  borderSpacing="5" styleClass="">
								<layout:row>
									<logic:equal name="image" property="first" value="true">
										<layout:link href="<%=targetViewJpegImage%>" paramName="image" paramId="<%=Constants.IMAGE_ID%>" paramProperty="previousImageId" styleClass="LIST">
											<html:img src="<%=request.getContextPath() + \"/images/previousRecord_disabled.gif\" %>" border="0" onmouseover="return overlib('No previous Image available');" onmouseout="return nd();"/>
										</layout:link>
									</logic:equal>
									<logic:notEqual name="image" property="first" value="true">
										<layout:link href="<%=targetViewJpegImage%>" paramName="image" paramId="<%=Constants.IMAGE_ID%>" paramProperty="previousImageId" styleClass="LIST">
											<html:img src="<%=request.getContextPath() + \"/images/previousRecord.gif\" %>" border="0" onmouseover="return overlib('View Previous Image');" onmouseout="return nd();"/>
										</layout:link>
									</logic:notEqual>
									
									
									<layout:text key="" property="targetImageNumber" styleClass="FIELD" >/ <bean:write name="viewResultsForm" property="totalImageNumber"/></layout:text>
									
									
									<logic:equal name="image" property="last" value="true">
										<layout:link href="<%=targetViewJpegImage%>" paramName="image" paramId="<%=Constants.IMAGE_ID%>" paramProperty="nextImageId" styleClass="LIST">
											<html:img src="<%=request.getContextPath() + \"/images/nextRecord_disabled.gif\" %>" border="0" onmouseover="return overlib('No next Image available');" onmouseout="return nd();"/>
										</layout:link>
									</logic:equal>
									<logic:notEqual name="image" property="last" value="true">
										<layout:link href="<%=targetViewJpegImage%>" paramName="image" paramId="<%=Constants.IMAGE_ID%>" paramProperty="nextImageId" styleClass="LIST">
											<html:img src="<%=request.getContextPath() + \"/images/nextRecord.gif\" %>" border="0" onmouseover="return overlib('View Next Image');" onmouseout="return nd();"/>
										</layout:link>
									</logic:notEqual>
								</layout:row>
						</layout:grid>
					</layout:row>
				</layout:form>
				<layout:row>
					<html:img src="<%= targetImageDownload + \"&amp;\" + Constants.IMAGE_ID + \"=\" + image.getImageId() %>" border="0" alt="Click to zoom the image" />
				</layout:row>
			</logic:present>
			<%------------- Crystal Snapshots ---------------------%>
			<logic:present name="viewResultsForm"  property="snapshotInfo">
				<bean:define id="imgSrc" name="viewResultsForm" property="snapshotInfo.fileLocation" type="java.lang.String"/>
				<img src="<%= targetImageDownloadFile + "&" + Constants.IMG_SNAPSHOT_URL_PARAM + "=" + imgSrc %>" border="0" alt="Crystal Snapshot" >
			</logic:present>
		
		</layout:panel>
	</layout:column>
</layout:grid>

