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
	String targetDisplayPDB		= request.getContextPath() + "/user/uploadPdbFile.do?reqCode=displayPdbFile";
	String targetDeletePdb 		= request.getContextPath() + "/user/uploadPdbFile.do?reqCode=deletePdbFile";
%>

<layout:skin includeScript="true" />
<script src="<%=request.getContextPath()%>/js/external/overlib/overlib.js"></script>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>

<%-- BreadCrumbs bar --%>
<jsp:include page="/tiles/common/util/breadCrumbsBar.jsp" flush="true" />


<%-- Page --%>
<layout:grid cols="1"  borderSpacing="5">
	<layout:column>
		<logic:present name="uploadPdbFileForm" property="fileFullPath">	
			<bean:define name="uploadPdbFileForm" property="fileFullPath" id="fileFullPath" 	type="java.lang.String"/>
			<layout:panel key="Uploaded Pdb" align="left" styleClass="PANEL">
					<layout:grid cols="1" styleClass="SEARCH_GRID">	
						<layout:row>
							<layout:column>
								<layout:link href="<%=targetDisplayPDB%>" paramName="uploadPdbFileForm" paramId="fileFullPath" paramProperty="fileFullPath" styleClass="FIELD">
									<layout:write name="uploadPdbFileForm" property="fileFullPath"/>
								</layout:link>
							</layout:column>	
							<layout:column>
								<html:link href="<%= targetDeletePdb %>"  paramName="uploadPdbFileForm" paramId="crystalId" paramProperty="crystalId" onmouseover="<%= \"return overlib('delete the file')\"%>" onmouseout="return nd();">
									<img src="<%=request.getContextPath()%>/images/cancel.png" border=0">
								</html:link>
							</layout:column>	
						</layout:row>
					</layout:grid>
			</layout:panel>
		</logic:present>
	</layout:column>
	
<%----------------------------------------------------------------------------%>
<layout:panel key="Submit your pdb file" align="left" styleClass="PANEL">
	<html:form action="/user/uploadPdbFile.do?reqCode=uploadPdbFile" method="post" enctype="multipart/form-data">
 		<html:hidden name="uploadPdbFileForm" property="crystalId" />
 		<html:hidden name="uploadPdbFileForm" property="proteinAcronym" />
 		<layout:grid cols="1" styleClass="SEARCH_GRID">	
			<layout:row>
				<html:file property="requestFile"/>
			</layout:row>
			<logic:present name="uploadPdbFileForm" property="crystalId">
				<layout:row>
					<layout:checkbox styleId="checkboxUpdate"  key="Update the unit cell parameters"  name="uploadPdbFileForm" property="updateCellValues" value="1" 	 styleClass="FIELD" mode="E,E,I"/><br/>
				</layout:row>
			</logic:present>
			<layout:row>			
				<html:submit>Upload File</html:submit>
			</layout:row>
		</layout:grid>
  </html:form>
</layout:panel>	
	
	
<%-- Acknowledge Action --%>
<jsp:include page="/tiles/common/messages/info.jsp" flush="true" />	
<jsp:include page="/tiles/common/messages/error.jsp" flush="true" />	
</layout:grid>
