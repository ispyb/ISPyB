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

Contributors : S. Delageniere, R. Leal, L. Launer, K. Levik, S. Veyrier, P. Brenchereau, M. Bodin, , A. De Maria Antolinos
--------------------------------------------------------------------------------------------------%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.application-servers.com/layout" prefix="layout" %>

<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="ispyb.common.util.Constants"%>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/alert.css" />
<LINK href="<%=request.getContextPath()%>/css/progressbarstyle.css" type=text/css rel=STYLESHEET />
	<%
		String cssExternalURL = request.getContextPath() + "/js/external/";
		String cssURL =request.getContextPath() + "/js/css/";		
	%>
	

<c:if test="${SITE_ATTRIBUTE eq 'MAXIV'}">
<link rel="stylesheet" type="text/css" href="<%=cssExternalURL%>extjs-4.1.1/resources/css/ext-all-gray.css" />
</c:if>
<c:if test="${SITE_ATTRIBUTE ne 'MAXIV'}">
<link rel="stylesheet" type="text/css" href="<%=cssExternalURL%>extjs-4.1.1/resources/css/ext-all-scoped.css" />
</c:if>
<link rel="stylesheet" type="text/css" href="<%=cssURL%>ispyb_js_custom.css" />


<link rel="stylesheet" type="text/css" href="<%=cssURL%>jquery/smoothness/jquery-ui-1.8.24.custom.css">
<link rel="stylesheet" type="text/css" href="<%=cssURL%>slider.css" >


 <script>
    Ext = {
        buildSettings:{
            "scopeResetCSS": true
        }
    };
</script>