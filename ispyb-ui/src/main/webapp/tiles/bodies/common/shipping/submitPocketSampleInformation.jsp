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

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="ispyb.common.util.Constants"%>

<%@ page isELIgnored="false" %>	

<%
	String targetUpload 					= request.getContextPath() + Constants.TEMPLATE_RELATIVE_PATH;
	String targetHelpDoc					= request.getContextPath() + "/tmp/upload-spreadsheet2ISPyB-guidelines.pdf";
	String targetHelpDemo					= request.getContextPath() + "/tmp/Excel_Upload_Demo.htm";
	String targetViewShippings				= request.getContextPath() + "/reader/genericShippingAction.do?reqCode=display";
	String targetDownloadTemplate				= request.getContextPath() + "/menuSelected.do?leftMenuId=36&targetUrl=/user/submitPocketSampleInformationAction.do?reqCode=downloadFile&fileType=template";
	String targetDownloadPopulatedTemplate			= request.getContextPath() + "/menuSelected.do?leftMenuId=36&targetUrl=/user/submitPocketSampleInformationAction.do?reqCode=downloadFile&fileType=populatedTemplate";
	String targetDownloadPopulatedTemplateAdvanced		= request.getContextPath() + "/menuSelected.do?leftMenuId=36&targetUrl=/user/submitPocketSampleInformationAction.do?reqCode=downloadFile&fileType=populatedTemplateAdvanced";
	String targetDownloadPopulatedTemplateFromShipment	= request.getContextPath() + "/menuSelected.do?leftMenuId=36&targetUrl=/user/submitPocketSampleInformationAction.do?reqCode=downloadFile&fileType=populatedTemplateFromShipment&shippingId=";
	String targetViewSamples 		= request.getContextPath() + "/menuSelected.do?leftMenuId=11&topMenuId=10&targetUrl=/user/viewSample.do?reqCode=displayForShipping";
	
%>


<layout:skin includeScript="true" />
<script src="<%=request.getContextPath()%>/js/external/overlib/overlib.js"></script>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>

<script language="javascript">
<!--

var state = 'none';

function showhide(layer_ref, state) {

//if (state == 'block') {
//state = 'none';
//}
//else {
//state = 'block';
//}

if (document.all) { //IS IE 4 or 5 (or 6 beta)
eval( "document.all." + layer_ref + ".style.display = state");
}
if (document.layers) { //IS NETSCAPE 4 or below
document.layers[layer_ref].display = state;
}
if (document.getElementById &&!document.all) {
hza = document.getElementById(layer_ref);
hza.style.display = state;
}
}



function uploadFile(){
	var selFile = document.getElementsByName("requestFile")[0];
	if (selFile.files.length == 0){
		var msg = "Please select first a .xls file with your shipment content prior to upload";
		return alert(msg);
	}
	var msgConfirm = "Are you sure you want to continue?";
	var rbDeleteAll = document.getElementsByName("deleteAllShipment")[0];
	if (rbDeleteAll.checked == true){
		msgConfirm = '"Overwrite current shipment" will delete the previously existing samples and pucks, as well as the data collections in ISPyB, if any. We strongly recommend you to create a new shipment. Are you sure you want to continue?';
	}else{
		msgConfirm = '"Add to current shipment" will add new pucks and samples to that shipment. Be aware that if a puck already exists with the same name or barcode it will be overwritten. Are you sure you want to continue?';
	}
	var result = confirm(msgConfirm);
	if (result==true) {
		switchView();
	}
	return result;
}
function switchView(){
	showhide('panelWait', 'block');
	showhide('panelSubmit', 'none');
	
}
//-->
</script> 

<%-- BreadCrumbs bar --%>
<jsp:include page="../../../common/util/breadCrumbsBar.jsp" flush="true">
  <jsp:param name="isInSubmitPocketSampleInformation" value="1" />
</jsp:include>


<%-- Warning message --%>

<logic:empty name="uploadForm" property="shippingId"> 
  <p align=justify><b><font color="#FF0000">
  Warning: By using "Create / Upload from file", you are going to create a new shipment without the tracking features.
  We recommend to use "Create / Shipment " and then "Download / Upload Shipment from Excel file". That way you will have a fully tracked shipment.
  </font></b></p>
</logic:empty>


<%---------------------%>

<div id="panelWait" style="display: none;">
	<layout:row>
		<layout:panel key="Action in progress. Please wait..." align="left" styleClass="PANEL">
			<img src="<%=request.getContextPath()%>/images/progress_bar.gif" border=0>
		</layout:panel>
	</layout:row>
</div> 
	
<%-- Page --%>
<bean:define name="uploadForm" 	property="populatedTemplateURL" id="populatedTemplateURL" type="java.lang.String"/>
<layout:space/>

<div id="panelDownload" style="display: block;">
<layout:grid cols="1"  borderSpacing="1" >	
<layout:panel key="Download your template for the selected Shipment" align="left" styleClass="PANEL">
	<layout:column>
			<layout:line>
				<%-- <layout:link href="<%=targetDownloadPopulatedTemplate%>"  styleClass="FIELD"><img src="<%=request.getContextPath()%>/images/excel_template_populated.gif" border=0>The PRE-FILLED with your Protein Acronyms - template for submission via upload of XLS file can be downloaded here.</layout:link> --%>
				<bean:define name="uploadForm" 	property="shippingId" id="shippingId" type="java.lang.String"/>
				<c:if test="${SITE_ATTRIBUTE eq 'ESRF'}">
					<layout:link href="<%=targetDownloadPopulatedTemplateFromShipment + shippingId%>"  styleClass="FIELD"><img src="<%=request.getContextPath()%>/images/Excel_24x24_01.png" border=0>The PRE-FILLED with your Protein Acronyms - template for submission via upload of XLS file can be downloaded here.</layout:link>
				</c:if>
				<c:if test="${SITE_ATTRIBUTE eq 'EMBL'}">
					<layout:link href="<%=targetDownloadPopulatedTemplateFromShipment + shippingId%>"  styleClass="FIELD"><img src="<%=request.getContextPath()%>/images/Excel_24x24_01.png" border=0>The PRE-FILLED with your Protein Acronyms - template for submission via upload of XLS file can be downloaded here.</layout:link>
				</c:if>
				<c:if test="${SITE_ATTRIBUTE eq 'MAXIV'}">
					<layout:link href="<%=targetDownloadPopulatedTemplateFromShipment + shippingId%>"  styleClass="FIELD"><img src="<%=request.getContextPath()%>/images/Excel_24x24_01.png" border=0>The PRE-FILLED with your Protein Acronyms - template for submission via upload of XLS file can be downloaded here.</layout:link>
				</c:if>
				<c:if test="${SITE_ATTRIBUTE eq 'ALBA'}">
					<layout:link href="<%=targetDownloadPopulatedTemplateFromShipment + shippingId%>"  styleClass="FIELD"><img src="<%=request.getContextPath()%>/images/Excel_24x24_01.png" border=0>The PRE-FILLED with your Protein Acronyms - template for submission via upload of XLS file can be downloaded here.</layout:link>
				</c:if>
			</layout:line>
			<c:if test="${SITE_ATTRIBUTE eq 'DLS'}">
				<layout:line>
					<font size="-1">
					<layout:link href="<%=targetDownloadTemplate%>"  styleClass="FIELD"><img src="<%=request.getContextPath()%>/images/excel_template.gif" border=0 width="15" height="17">BLANK template can be downloaded here.</layout:link>
					</font>
				</layout:line>
			</c:if>
			<layout:space/>
			
	</layout:column>
</layout:panel>
</layout:grid>
</div>

<layout:space/>
<div id="panelSubmit" style="display: block;">
	<layout:grid cols="1"  borderSpacing="1" >	
		<layout:panel key="Submit your .xls file" align="left" styleClass="PANEL">
			<layout:form action="/user/submitPocketSampleInformationAction.do" reqCode="uploadFile" enctype="multipart/form-data">
				<layout:text key="shippingId" property="shippingId"  mode="H,H,H"/>     
				<p><b><font color="red">The file must be a xls file (not xlsx).</font></b></p>           
				<layout:file key="File to upload" fileKey="File to upload" property="requestFile" styleClass="FIELD_DISPLAY"/>
				<layout:radios layoutId="deleteAllShipment" key="Samples and Containers: "   name="uploadForm"  property="deleteAllShipment" styleClass="FIELD" > 
		
					<html:radio name="uploadForm"  property="deleteAllShipment"  value="false" styleClass="FIELD" onmouseover="return overlib('Samples and pucks for this shipment will be overwritten');" onmouseout="return nd();" >Overwrite current shipment</html:radio>
		
					<html:radio name="uploadForm"  property="deleteAllShipment" value="true" styleClass="FIELD" onmouseover="return overlib('Add pucks and samples to this shipment');" onmouseout="return nd();" >Add to current shipment</html:radio>
			
				</layout:radios>
				<layout:submit onclick="return uploadFile();"><layout:message key="Upload"/></layout:submit>
			</layout:form>
			<layout:message key="Warning : "  styleClass="FIELD_WARNING"/>
			<layout:message key='"Overwrite current shipment" will delete the previously existing samples and pucks, as well as the data collections in ISPyB, if any. We strongly recommend you to create a new shipment.'  styleClass="FIELD_WARNING"/>
			<layout:message key='"Add to current shipment" will add new pucks and samples to that shipment. Be aware that if a puck already exists with the same name or barcode it will be overwritten.'  styleClass="FIELD_WARNING"/>
		</layout:panel>
	</layout:grid>
</div>




	<logic:messagesPresent>
		<h3><font color="red">Please correct your file and re-submit it.</font></h3>
		<layout:link href="<%=targetViewSamples%>" styleClass="PANEL_BREAD_CRUMBS">
				<img src="<%=request.getContextPath()%>/images/SampleHolderView_24x24_01.png" border=0 onmouseover="return overlib('View Samples');" onmouseout="return nd();">View&nbsp;Samples&nbsp;in&nbsp;Shipping
			</layout:link>
	</logic:messagesPresent>
	
	
	<logic:messagesNotPresent>
		<logic:messagesPresent message="true">
			<h3><font color="green">Information uploaded successfully</font></h3>
			<layout:link href="<%=targetViewShippings%>" styleClass="PANEL_BREAD_CRUMBS">
				<img src="<%=request.getContextPath()%>/images/up.gif" border=0 onmouseover="return overlib('View All Shipments');" onmouseout="return nd();">View&nbsp;all&nbsp;Shipments
			</layout:link>
			<layout:link href="<%=targetViewSamples%>" styleClass="PANEL_BREAD_CRUMBS">
				<img src="<%=request.getContextPath()%>/images/SampleHolderView_24x24_01.png" border=0 onmouseover="return overlib('View Samples');" onmouseout="return nd();">View&nbsp;Samples&nbsp;in&nbsp;Shipping
			</layout:link>
			
		</logic:messagesPresent>
	</logic:messagesNotPresent>
	
	
	<%-- Acknowledge Action --%>
	<jsp:include page="/tiles/common/messages/error.jsp" flush="true" />
	<jsp:include page="/tiles/common/messages/info.jsp" flush="true" />

<layout:space/>

<%--------------------------------------------------------- GUIDELINES ------------------------------------------------------------------%>
<layout:row>
	<layout:panel key="GUIDELINES on how to fill in the XLS template" align="left" styleClass="PANEL">
		<layout:grid cols="1" styleClass="PANEL_BREAD_CRUMBS">
			<ul type="circle">
			    <%--
				<li>The namimg of the template follows this rule: <i><font color='grey'>proposalName.xls</font></i><br>
				Once uploaded, the files will create a Shipment called: <i><font color='grey'>proposalName_dd-mm-yyyy</font></i> where dd-mm-yyy is the date the file was uploaded<br>
				</li>
				<br>
				--%>
				<li><a href="<%=targetHelpDoc%>"  target=_blank>Please read guidelines on use before trying out spreadsheet upload</a></li>
				<br/>
				<li> Use <a href="http://www.microsoft.com/excel" target=_blank>Microsoft Excel</a> or
				<a href="http://www.openoffice.org" target=_blank>OpenOffice Calc 2.1 or above <img src="http://marketing.openoffice.org/art/galleries/marketing/web_buttons/nicu/80x15_3.png" border="0" alt=" Use OpenOffice.org" title="Use OpenOffice.org"></a>
				</li>
				<br/>
				<li>Make sure you provide data on all compulsory fields for each sample</li>
				<br/>
				<li>Make sure the sample name contains only a-z , A-Z or 0-9 or - or _ characters.</li>
				<br/>
				<li>The spreadsheet contails pull-down menus to aid data completion (e.g. space-group pull-down list). Please ensure these pull-down lists are not activated when you save the file before upload as file upload will fail in this case due to a bug in the Excel conversion package which is out of our control</li>
				<br/>
				<li>For now, only xls files (and not xlsx) can be uploaded. With the last version of Microsoft Excel, to save your file, click on 'Save As' and choose the *.xls format.
				</li>
				<br/>
				<li><a href="<%=targetHelpDemo%>"  target=_blank >View the animated online demo</li>
				<br/>
				<img src="<%=request.getContextPath()%>/images/help/uploadDemo.gif" border=0 onmouseover="return overlib('View demo');" onmouseout="return nd();"></a>				
				<li>Should there be any problems, please email us at <a href="mailto:<%=Constants.MAIL_TO_SITE%>"><%=Constants.MAIL_TO_SITE%></a>.</li>
			</ul>
		</layout:grid>
	</layout:panel>
</layout:row>

