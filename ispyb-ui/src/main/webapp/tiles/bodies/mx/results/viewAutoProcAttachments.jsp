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

<%-- BreadCrumbs bar --%>
<jsp:include page="/tiles/common/util/breadCrumbsBar.jsp" flush="true" />

<bean:define id="dataCollectionId" name="viewResultsForm" property="dataCollectionId" type="java.lang.Integer"/>

<%
	String targetIndex = request.getContextPath() + "/user/viewResults.do?reqCode=display&dataCollectionId=" + dataCollectionId.toString();
%>


<H3><a href="<%=targetIndex%>">Back to the index result page</a><BR></H3>

<logic:notEmpty name="viewResultsForm" property="autoProcAttachmentContent" >
<H4><bean:write name="viewResultsForm" property="autoProcAttachmentName" /></H4>
<textarea readonly="true" cols="130" rows="25" style="font-family:courier;">
<bean:write name="viewResultsForm" property="autoProcAttachmentContent"/>
</textarea>
</logic:notEmpty>

<H3><a href="<%=targetIndex%>">Back to the index result page</a><BR></H3>


