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

<%@page import="ispyb.common.util.Constants,ispyb.common.util.StringUtils,java.io.File"%>



<%-- BreadCrumbs bar --%>
<jsp:include page="/tiles/common/util/breadCrumbsBar.jsp" flush="true" />

<H1> DNA Images </H1>

<%-- DNA html files links --%>
<bean:define name="viewResultsForm" 	scope="request" id="viewResultsForm" 	type="ispyb.client.mx.results.ViewResultsForm"/>
<layout:form action="/user/viewResults.do" reqCode="displayDNAHtmlFiles">

		<layout:text key="dataCollectionId"	name="viewResultsForm" property="dataCollectionId"  mode="H,H,H"/>
        <html:submit disabled="<%=!viewResultsForm.isDataProcessingContentPresent()%>" property="DNASelectedFile" value="<%=Constants.DNA_FILES_DATA_PROC%>"></html:submit>
		<html:submit disabled="<%=!viewResultsForm.isIntegrationContentPresent()%>" property="DNASelectedFile"	value="<%=Constants.DNA_FILES_INTEGRATION%>" />
        <html:submit disabled="<%=!viewResultsForm.isStrategyContentPresent()%>" property="DNASelectedFile"	value="<%=Constants.DNA_FILES_STRATEGY%>" />

</layout:form>	

<bean:define id="dNAContent" name="viewResultsForm" property="DNAContent" type="java.lang.String"/>
<%=dNAContent%>












