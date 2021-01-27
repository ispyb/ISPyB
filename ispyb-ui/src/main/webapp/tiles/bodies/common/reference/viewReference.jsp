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

<script src="<%=request.getContextPath()%>/js/external/overlib/overlib.js"></script>
<%@page import="ispyb.common.util.Constants"%>

<%
	String targetDownloadReference = request.getContextPath() + "/viewReference.do?reqCode=downloadSimpleReference";
	String reportingText = "When reporting data from the ESRF MX beamlines please cite the appropriate references:";
	if (Constants.SITE_IS_MAXIV()){
		reportingText = "When reporting data from the MAX IV Laboratory MX beamlines please cite the appropriate references:";
	}
	if (Constants.SITE_IS_EMBL()){
		reportingText = "When reporting data from the EMBL Hamburg beamlines please cite the appropriate references:";
	}
	if (Constants.SITE_IS_ALBA()){
		reportingText = "When reporting data from the ALBA MX beamlines please cite the appropriate references:";
	}
%>	
	
<%-- Reference--%>
<h4> <%=reportingText %> </h4>
<layout:form action="/viewReference" reqCode="downloadReference">
	<layout:grid cols="1" styleClass="LINK_GRID">	
		<layout:column>			
			<logic:iterate name="viewReferenceForm" property="listReferences" id="item" indexId="index" type="ispyb.server.mx.vos.collections.IspybReference3VO">
				<layout:row>
					<html:link href="<%=targetDownloadReference%>" paramName="item" paramId="referenceId" paramProperty="referenceId" ><img src="<%=request.getContextPath()%>/images/tex.png" border="0" onmouseover="return overlib('download as bibtex');" onmouseout="return nd();"/></html:link>&nbsp;<html:link href="<%=item.getReferenceUrl() %>" target="_blank"><bean:write name="item" property="referenceName"/></html:link>
				</layout:row>
			</logic:iterate>
			<layout:row>
			<layout:submit><layout:message key="Download as BibTeX"/></layout:submit>
			
			</layout:row>
		</layout:column>
	</layout:grid>
</layout:form>