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

<layout:skin />

<%@page import="ispyb.common.util.Constants"%>

<% 
	String target 				= request.getContextPath() + "/user/viewResults.do?reqCode=viewJpegImage";
	String targetGetDataFromFile		= request.getContextPath() + "/user/viewResults.do?reqCode=getDataFromFile";
	//String targetScreening 			= request.getContextPath() + "/user/viewScreening.do?reqCode=display";
	String targetLogFile			= request.getContextPath() + "/user/viewResults.do?reqCode=displayProgramLogFiles";
	String targetImageDownload 		= request.getContextPath() + "/user/imageDownload.do?reqCode=getImageJpgThumb";
	String targetImageDownloadFile 		= request.getContextPath() + "/user/imageDownload.do?reqCode=getImageJpgFromFile";
	String targetImageDownloadFileFullSize 	= request.getContextPath() + "/user/viewResults.do?reqCode=viewJpegImageFromFile";
	int	nbSnapshotDisplayed		= 0;
	int	nbImageDisplayed		= 0;
	String targetViewImageList		= request.getContextPath() + "/user/viewResults.do?reqCode=display";
	String targetViewImageWall		= request.getContextPath() + "/user/viewImageWall.do?reqCode=display";
%>

<script language="javascript">
<!--
var state = 'none';

function showhide(layer_ref, state) {

hza = document.getElementById(layer_ref);
hza.style.display = state;
}
//-->
</script>


<%-- BreadCrumbs bar --%>
<jsp:include page="/tiles/common/util/breadCrumbsBar.jsp" flush="true" />

<%-- Include Experiments parameters, Beamline parameters, DNA, Denzo, Xtal snapshots, ... --%>
<jsp:include page="./viewResults_top.jsp" flush="true" />


<%---------------------- Images collected -------------------------------%>
<layout:grid cols="1"  borderSpacing="10">

	<%-- Images list --%>
	<layout:column>
		<logic:present name="viewResultsForm"  property="listInfo">
		<logic:notEmpty name="viewResultsForm"  property="listInfo">
		<layout:tabs width="100%">
			<layout:tab key="Images collected" width="150">
			
				<%-- Location --%>
				
					<layout:row>
					<layout:text key="Location" name="viewResultsForm" property="listInfo[0].fileLocation" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
					
					<div align="right">
						<layout:link href="<%=targetViewImageList%>" paramName="viewResultsForm" paramId="dataCollectionId" paramProperty="dataCollectionId" styleClass="FIELD">
							View as Image List <img src="<%=request.getContextPath()%>/images/imageList.gif" border=0 align="middle">
						</layout:link>
					</div>
					</layout:row>
				<layout:space></layout:space>
				
				<%-- List --%>
				<layout:form action="/user/viewImageWall.do?reqCode=display">
					<layout:row>
					<layout:message key="View Wall as (Horizontal X Vertical):<br>To use all available space, use: 0x0" styleClass="FIELD"/>
					<layout:text key=""  name="viewResultsForm" property="nbImagesHorizontal" 	styleClass="FIELD" size="2" mode="E,E,E"/>
					<layout:message key="X" styleClass="FIELD"/>
					<layout:text key=""  name="viewResultsForm" property="nbImagesVertical" 	styleClass="FIELD" size="2" mode="E,E,E"/>
					
					<layout:select key="Image Width" 	name="viewResultsForm" property="imageWidth" size="1" styleClass="FIELD" mode="E,E,E">				
						<layout:option key="256x256" 	value="256px"/>
						<layout:option key="192x192"	value="192px"/>
						<layout:option key="125x125"	value="125px"/>
			            <layout:option key="62x62" 		value="62px"/>              
			        </layout:select>
					
					<layout:submit><layout:message key="Refresh"/></layout:submit>
					</layout:row>
				</layout:form>
				
				<layout:column>
					<logic:iterate id="im" name="viewResultsForm"  property="listInfo">
						<bean:define id="imgVal" name="im"  type="ispyb.client.mx.results.ImageValueInfo"/>
						<bean:define id="imageWidth" name="viewResultsForm" property="imageWidth"  type="java.lang.String"/>
						<a href="<%= target  + "&" + Constants.PREVIOUS_IMAGE_ID +"=" + imgVal.getPreviousImageId() + "&" + Constants.IMAGE_ID + "=" + imgVal.getImageId() + "&" + Constants.NEXT_IMAGE_ID +"=" + imgVal.getNextImageId() %>">
						<img src="<%= targetImageDownload + "&" + Constants.IMAGE_ID + "=" + imgVal.getImageId() %>" border="0" width="<%=imageWidth%>">
						</a>
						<logic:equal name="imgVal" property="lastImageHorizontal" value="true">
							<br>
						</logic:equal>
						<logic:equal name="imgVal" property="lastImageVertical" value="true">
							<br><hr><br>
						</logic:equal>
					</logic:iterate>
				</layout:column>
				
			</layout:tab>
		</layout:tabs>
		</logic:notEmpty>
		</logic:present>	
	</layout:column >

</layout:grid>

<%-- Errors --%>
<jsp:include page="/tiles/common/messages/error.jsp" flush="true" />
