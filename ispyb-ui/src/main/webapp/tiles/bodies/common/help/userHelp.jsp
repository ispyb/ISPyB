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

<%@page import="ispyb.common.util.Constants"%>

<%
	String targetUpdateDB 	= request.getContextPath() + "/updateDB.do?reqCode=updateProposal"; 
	String latestISPyBNews = "Latest ISPyB News & Information";
	if (Constants.SITE_IS_MAXIV()){
		latestISPyBNews += " (@esrf)";
	}
%>

<h2> News web pages </h2>
<h4>
<a href="<%=Constants.getProperty("ISPyB.news.url")%>" target=_blank><img src="<%=request.getContextPath()%>/images/information.gif" border=0><%=latestISPyBNews%></a>
</h4>
<br>
<h2> Help web pages </h2>
<h4>
You may find some help on our <a href="<%=Constants.getProperty("ISPyB.help.url")%>" target=_blank>web pages</a>.</h4>
<br>
<h2>Database update</h2>
<h4>
If you do not find your latest proteins/samplesheets, or if your do not find your latest sessions <a href="<%=targetUpdateDB%>" >update ISPyB database</a> (this may take a few minutes).
<br>
-> Note that the new/duplicated samplesheets need to be <font color= '#ff6622'>approved by Safety</font> in the User Portal prior to be imported in ISPyB.

</h4>
