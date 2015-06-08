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
	String targetReports 	= request.getContextPath() + "";
	String targetgetPDF		= request.getContextPath() + "/user/SendReportAction.do?reqCode=GetPDF";
%>

<layout:skin includeScript="true" />
<script src="<%=request.getContextPath()%>/js/external/overlib/overlib.js"></script>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>

<%-- BreadCrumbs bar --%>
	<jsp:include page="/tiles/common/util/breadCrumbsBar.jsp" flush="true" />


<%-- Page --%>
<layout:grid cols="1"  borderSpacing="5">
	<layout:column>
		<logic:present name="sendReportForm" property="fileFullPath">	
			<bean:define name="sendReportForm" property="fileFullPath" id="fileFullPath" 	type="java.lang.String"/>
			<layout:panel key="Uploaded Report" align="left" styleClass="PANEL">
					<layout:link href="<%=targetgetPDF%>" paramName="sendReportForm" paramId="fileFullPath" paramProperty="fileFullPath" styleClass="FIELD">
						<layout:write name="sendReportForm" property="fileFullPath"/>
					</layout:link>
			</layout:panel>
		</logic:present>
	</layout:column>
	
<%----------------------------------------------------------------------------%>
<layout:panel key="Submit your Report" align="left" styleClass="PANEL">
	<html:form action="/user/SendReportAction.do?reqCode=sendReport" method="post" enctype="multipart/form-data">
 		<html:file property="uploadedFile"/>
 		<br>
 		<html:submit>Upload File</html:submit>
  </html:form>
</layout:panel>	
	
	
<%-- Acknowledge Action --%>
<jsp:include page="/tiles/common/messages/info.jsp" flush="true" />	
<jsp:include page="/tiles/common/messages/error.jsp" flush="true" />	
</layout:grid>
