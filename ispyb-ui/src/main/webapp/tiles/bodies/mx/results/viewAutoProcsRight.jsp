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
<%@page import="ispyb.common.util.Constants"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.application-servers.com/layout" prefix="layout" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false"%>

<bean:define id="autoprocId" name="getAutoProcsForm" property="autoProc" />
<% 
	
	String targetExportPDFAutoProc 			= request.getContextPath() + "/user/exportAutoProc.do?reqCode=exportAutoProcAsPdf&autoProcId="+autoprocId;
    String targetExportRTFAutoProc 			= request.getContextPath() + "/user/exportAutoProc.do?reqCode=exportAutoProcAsRtf&autoProcId="+autoprocId;
    String exportPDFTitle 					= "View PDF report";
    String exportRTFTitle 					= "View DOC report";
    
%>

<%-- Auto Proc report Issue 1187--%>
	<layout:grid cols="1">
		<layout:row>
			<%-- RTF Report--%>
			<layout:link title="RTF auto processing report" href="<%= targetExportRTFAutoProc %>" paramName="getAutoProcsForm" paramId="dataCollectionId" paramProperty="dataCollectionId" styleClass="FIELD">
				<img src="<%=request.getContextPath()%>/images/Word.png" border=0 onmouseover="return overlib('Export Auto Processing into DOC');" onmouseout="return nd();"/>
						<%= exportRTFTitle %>
			</layout:link>
			<%-- PDF Report--%>
			<layout:link title="PDF auto processing report" href="<%= targetExportPDFAutoProc %>" paramName="getAutoProcsForm" paramId="dataCollectionId" paramProperty="dataCollectionId" styleClass="FIELD">
					<img src="<%=request.getContextPath()%>/images/pdf.png" border=0 onmouseover="return overlib('Export AutoProcessing into PDF');" onmouseout="return nd();"/>
						<%= exportPDFTitle %>
			</layout:link>
		</layout:row>
	</layout:grid>

<div style="height: 50px">
	<h2>Output Files:</h2>
</div>

<!-- output files - all -->
<logic:notEmpty name="getAutoProcsForm" property="outputAutoProcProgAttachmentsWebBeans">
	<bean:define id="dataCollectionIdTest" name="getAutoProcsForm" property="dataCollectionId" />
	<logic:iterate id="currAttachment" name="getAutoProcsForm" property="outputAutoProcProgAttachmentsWebBeans">
		<bean:define id="autoprocId" name="currAttachment" property="autoProcProgramAttachmentId" />
		
	<%
		String actionString = "/user/viewResults.do?reqCode=displayAutoProcProgAttachment&autoProcProgramAttachmentId="
				+ autoprocId
				+ "&dataCollectionId=" + dataCollectionIdTest;
		
	%>
	<logic:notEmpty name="currAttachment" property="ispybAutoProcAttachment" >	
	<div style="height: 50px;" >
	
	<logic:notEmpty name="currAttachment" property="ispybAutoProcAttachment.description" >	
		<bean:define id="desc" name="currAttachment" property="ispybAutoProcAttachment.description" type = "java.lang.String"/>
		<html:form action="<%= actionString %>">
			<html:submit onmouseover="return overlib('${desc}');" onmouseout="return nd();">
				<bean:write name="currAttachment" property="fileName" />
			</html:submit>
		</html:form>	
	</logic:notEmpty>
	</logic:notEmpty>
	
	<logic:notEmpty name="currAttachment" property="ispybAutoProcAttachment" >	
	<logic:empty name="currAttachment" property="ispybAutoProcAttachment.description" >	
		<html:form action="<%= actionString %>">
			<html:submit>
				<bean:write name="currAttachment" property="fileName" />
			</html:submit>
		</html:form>	
	</logic:empty>
	</logic:notEmpty>
		
	</div>
	</logic:iterate>	
</logic:notEmpty>

<!-- No output Files  -->
<logic:empty name="getAutoProcsForm" property="outputAutoProcProgAttachmentsWebBeans">
	<p class="FIELD_BIG">No attachments</p>
</logic:empty>


<div style="height: 50px">
	<h2>Input Files:</h2>
</div>

<!-- input files - all -->
<logic:notEmpty name="getAutoProcsForm" property="inputAutoProcProgAttachmentsWebBeans">
	<bean:define id="dataCollectionIdTest" name="getAutoProcsForm" property="dataCollectionId" />
	<logic:iterate id="currAttachment" name="getAutoProcsForm" property="inputAutoProcProgAttachmentsWebBeans">
		<bean:define id="autoprocId" name="currAttachment" property="autoProcProgramAttachmentId" />

	<%
		String actionString = "/user/viewResults.do?reqCode=displayAutoProcProgAttachment&autoProcProgramAttachmentId="
				+ autoprocId
				+ "&dataCollectionId=" + dataCollectionIdTest;
	%>
	
	<div style="height: 50px;" >
	
	<logic:notEmpty name="currAttachment" property="ispybAutoProcAttachment.description" >	
		<bean:define id="desc" name="currAttachment" property="ispybAutoProcAttachment.description" type = "java.lang.String"/>
		<html:form action="<%= actionString %>">
			<html:submit onmouseover="return overlib('${desc}');" onmouseout="return nd();">
				<bean:write name="currAttachment" property="fileName" />
			</html:submit>
		</html:form>	
	</logic:notEmpty>
	
	<logic:empty name="currAttachment" property="ispybAutoProcAttachment.description" >	
		<html:form action="<%= actionString %>">
			<html:submit>
				<bean:write name="currAttachment" property="fileName" />
			</html:submit>
		</html:form>	
	</logic:empty>
		
	</div>
	</logic:iterate>	
</logic:notEmpty>

<!-- No input Files  -->
<logic:empty name="getAutoProcsForm" property="inputAutoProcProgAttachmentsWebBeans">
	<p class="FIELD_BIG">No attachments</p>
</logic:empty>

<!-- Correction Files  -->
<logic:notEmpty name="getAutoProcsForm" property="correctionFilesAttachmentsWebBeans">
	<div style="height: 50px">
		<h2>Correction Files:</h2>
	</div>
	<bean:define id="dataCollectionIdTest" name="getAutoProcsForm" property="dataCollectionId" />
	<logic:iterate id="currAttachment" name="getAutoProcsForm" property="correctionFilesAttachmentsWebBeans">
		<bean:define id="fileName" name="currAttachment" property="fileName" />
			<%
				String actionString = "/user/viewResults.do?reqCode=displayCorrectionFile&fileName="
						+ fileName
						+ "&dataCollectionId=" + dataCollectionIdTest;
			%>
	
		<div style="height: 50px;" >
	
		<logic:notEmpty name="currAttachment" property="ispybAutoProcAttachment.description" >	
			<bean:define id="desc" name="currAttachment" property="ispybAutoProcAttachment.description" type = "java.lang.String"/>
			<html:form action="<%= actionString %>">
				<html:submit onmouseover="return overlib('${desc}');" onmouseout="return nd();">
					<bean:write name="currAttachment" property="fileName" />
				</html:submit>
			</html:form>	
		</logic:notEmpty>
	
		<logic:empty name="currAttachment" property="ispybAutoProcAttachment.description" >	
			<html:form action="<%= actionString %>">
				<html:submit>
					<bean:write name="currAttachment" property="fileName" />
				</html:submit>
			</html:form>	
		</logic:empty>
		
		</div>
	</logic:iterate>	
</logic:notEmpty>

<!-- Download files  -->
<logic:notEqual name="getAutoProcsForm" property="nbAutoProcAttachmentsWebBeans" value="0">
	<div style="height: 50px">
		<h2>Download Files:</h2>
	</div>
	<p class="FIELD_BIG">You can create a tar of all the files and download it.</p>
	<bean:define id="dataCollectionIdTest" name="getAutoProcsForm" property="dataCollectionId" />
	<bean:define id="autoprocId" name="getAutoProcsForm" property="autoProc" />
	<%
	String actionString = "/user/viewResults.do?reqCode=downloadAttachment&autoProcId="
		+ autoprocId
		+ "&dataCollectionId=" + dataCollectionIdTest;
	%>
	
	<html:form action="<%= actionString %>">
		<input class="FIELD_BUTTON" name="Action" type="submit" value="Download" onmouseover="return overlib('Download all attachments');" onmouseout="return nd();">
	</html:form>
</logic:notEqual>




