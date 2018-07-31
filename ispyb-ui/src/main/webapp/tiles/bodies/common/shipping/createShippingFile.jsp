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
<%@ page import="ispyb.common.util.Constants"%>
<%@ page isELIgnored="false" %>

<%
	String targetViewShippings					= request.getContextPath() + "/reader/genericShippingAction.do?reqCode=display";
	String targetDownloadCSVShipmentTemplate	= request.getContextPath() + "/tmp/shipment-template.csv";
%>

<layout:skin includeScript="true" />
<script src="<%=request.getContextPath()%>/js/external/overlib/overlib.js"></script>
<script type='text/javascript' src="<%=request.getContextPath()%>/js/external/toggleDiv.js"></script>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>



<%-- BreadCrumbs bar --%>
<jsp:include page="/tiles/common/util/breadCrumbsBar.jsp" flush="true" >
	<jsp:param name="isInSubmitPocketSampleInformation" value="0" />
</jsp:include>
<%-------------------------%>

<%-- Help toggle --%>	
<a class="noUnderline" onmouseover="return overlib('Expand/collapse Help');" onmouseout="return nd();" href="javascript:;" onclick="toggleDiv('helpDiv');">
  <p class="toggleText">&nbsp;&nbsp;Help&nbsp;<img src="<%=request.getContextPath()%>/images/doubleArrowDesc.png" border=0></p>
</a>
<div id="helpDiv" style="display:none">
	<layout:grid cols="1" borderSpacing="10">
	
		<%-- Help --%>
		<layout:column >
		<h2>Description</h2>
		<p>This feature allows you to upload sample information from a CSV file.</p>
		<h2>Format of the file</h2>
		<p>The different fields (required ones are highlighted in red) of the file are:<p/>
		<ul>
			<li><strong><font color="red">proposalCode</font></strong>: required</li>
			<li><strong><font color="red">proposalNumber</font></strong>: required</li>
            <c:if test="${SITE_ATTRIBUTE eq 'MAXIV'}">
                <li><strong>visitNumber</strong>: not required for Max IV Laboratory</li>
            </c:if>
            <c:if test="${SITE_ATTRIBUTE ne 'MAXIV'}">
                <li><strong>visitNumber</strong>: optional, information not integrated in ISPyB at the ESRF</li>
            </c:if>
			<li><strong><font color="red">shippingName</font></strong>: required, &lt; 45 characters.</li>
			<li><strong><font color="red">dewarCode</font></strong>: required, &lt; 45 characters.</li>
			<li><strong><font color="red">containerCode</font></strong>: required, &lt; 45 characters.</li>
			<li><strong>preObsResolution</strong>: number, optional but highly recommended</li>
			<li><strong>neededResolution</strong>: number, optional but highly recommended</li>
			<li><strong>oscillationRange</strong>: number, optional but highly recommended</li>
            <c:if test="${SITE_ATTRIBUTE eq 'MAXIV'}">
                <li><strong><font color="red">proteinAcronym</font></strong>: required, &lt; 45 characters</li>
                <li><strong><font color="red">proteinName</font></strong>: required, &lt; 255 characters</li>
            </c:if>
            <c:if test="${SITE_ATTRIBUTE ne 'MAXIV'}">
                <li><strong><font color="red">proteinAcronym</font></strong>: required, &lt; 8 characters, has to correspond to an acronym already existing in your account (coming from your sample sheets described in the DUO for that proposal)</li>
                <li><strong><font color="red">proteinName</font></strong>: required, &lt; 255 characters, has to correspond to a protein name already existing in your account associated with the proteinAcronym above (coming from your sample sheets described in the SMIS for that proposal)</li>
            </c:if>
			
			<li><strong>spaceGroup</strong>: optional but highly recommended, &lt; 20 characters</li>
			<li><strong>sampleBarcode</strong>: optional , &lt; 45 charac.</li>
			<li><strong><font color="red">sampleName</font></strong>: required, &lt; 8 characters, unique name for the protein acronym, must contain only a-z, A-Z or 0-9 or - or _ characters.</li>
			<li><strong><font color="red">samplePosition</font></strong>: required, number between 1 and 10</li>
			<li><strong>sampleComments</strong>: optional , &lt; 1024 characters</li>
			<li><strong>cell_a</strong>: number, optional but highly recommended</li>
			<li><strong>cell_b</strong>: number, optional but highly recommended</li>
			<li><strong>cell_c</strong>: number, optional but highly recommended</li>
			<li><strong>cell_alpha</strong>: number, optional but highly recommended</li>
			<li><strong>cell_beta</strong>: number, optional but highly recommended</li>
			<li><strong>cell_gamma</strong>: number, optional but highly recommended</li>
		</ul>
		<h2>Creation of data</h2>
		<p>A new shipment with its content is created upon each upload.</p>
		<p>If you need to modify your CSV and re-upload it, a new shipment with the same name will be created. We encourage you to then remove the previously created shipment (if obsolete) manually by clicking on the <img src="<%=request.getContextPath()%>/images/cancel.png" border=0 /> sign in the last column of your shipments table summary.</p>
		</layout:column>
	
	</layout:grid>
</div>


<%-- Errors --%>
<jsp:include page="/tiles/common/messages/error.jsp" flush="true" />

<%-- Page --%>
<c:if test="${SITE_ATTRIBUTE eq 'MAXIV'}">
	<div id="panelDownload" style="display: block;">
		<layout:grid cols="1"  borderSpacing="1" >	
			<layout:panel key="Download your template" align="left" styleClass="PANEL">
				<layout:column>
						<layout:line>
							<p><b><font color="red">Please, the first line in the template is to help you to make the CSV, when uploading you will need to remove this first line</font></b></p>
							<p><b><font color="red">You will need to have created protein acronyms before you can submit these through a CSV</font></b></p>
							<layout:link href="<%=targetDownloadCSVShipmentTemplate%>"  styleClass="FIELD"><img src="<%=request.getContextPath()%>/images/Excel_24x24_01.png" border=0>The template for submission via upload of CSV file can be downloaded here.</layout:link>
						</layout:line>
						<layout:space/>
				</layout:column>
			</layout:panel>
		</layout:grid>
	</div>
</c:if>

<div id="panelSubmit" style="display: block;">
	<layout:grid cols="1"  borderSpacing="1" >	
		<layout:panel key="Submit your .csv file" align="left" styleClass="PANEL">
			<layout:form action="/user/createShippingFileAction.do" reqCode="uploadCsvFile" enctype="multipart/form-data">
				<p><font color="orange">The file must be a csv file.</font></p>           
				<layout:file key="File to upload" fileKey="File to upload" property="requestFile" styleClass="FIELD_DISPLAY"/>
				<layout:select   key="Field separator" 	name="createShippingFileForm" property="fieldSeparator" styleClass="COMBO"	mode="E,E,I" isRequired="true">
						<bean:define id="listFieldSeparator" name="createShippingFileForm" property="listFieldSeparator" type="java.util.ArrayList"/>
						<layout:options collection="listFieldSeparator" />
				</layout:select>
				<layout:select   key="Text separator" 	name="createShippingFileForm" property="textSeparator" styleClass="COMBO"	mode="E,E,I" isRequired="true">
						<bean:define id="listTextSeparator" name="createShippingFileForm" property="listTextSeparator" type="java.util.ArrayList"/>
						<layout:options collection="listTextSeparator"   />
				</layout:select>
				<layout:submit onclick="javascript:switchView();"><layout:message key="Upload"/></layout:submit>
			</layout:form>
		</layout:panel>
	</layout:grid>
</div>

<logic:messagesPresent>
	<h3><font color="red">Please correct your file and re-submit it.</font></h3>
</logic:messagesPresent>
	
	
<logic:messagesNotPresent>
	<logic:messagesPresent message="true">
		<h3><font color="green">Information uploaded successfully</font></h3>
		<layout:link href="<%=targetViewShippings%>" styleClass="PANEL_BREAD_CRUMBS">
			<img src="<%=request.getContextPath()%>/images/up.gif" border=0 onmouseover="return overlib('View All Shipments');" onmouseout="return nd();">View&nbsp;all&nbsp;Shipments
		</layout:link>
	</logic:messagesPresent>
</logic:messagesNotPresent>


<%-- Called from Action --%>
<logic:present name="createShippingFileForm" scope="request">
	<%-- Acknowledge Action --%>
	<jsp:include page="/tiles/common/messages/error.jsp" flush="true" />
	<jsp:include page="/tiles/common/messages/info.jsp" flush="true" />
</logic:present>