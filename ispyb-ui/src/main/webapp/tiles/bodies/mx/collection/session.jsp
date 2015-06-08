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

<%@page import="java.util.Calendar,java.util.GregorianCalendar"%>

<%
	String targetViewAllSessions    = request.getContextPath() + "/menuSelected.do?leftMenuId=17&targetUrl=/user/viewSession.do?reqCode=display";
%>
<layout:skin includeScript="true" />

<layout:panel key="<%=Constants.SEARCH_SESSIONS_VISITS%>" align="left" styleClass="PANEL">
<layout:form action="/user/viewSession.do" reqCode="display">
	
	<layout:grid cols="1" styleClass="SEARCH_GRID">	
		<layout:column>
			<layout:row >
			<layout:text key="Beamline" 	property="beamLineName" 	styleClass="FIELD" 	mode="E,E,E"/>
			<logic:notEmpty name="viewSessionForm" property="beamlineList">
				<layout:select key="or select a beamline" property="beamLineNameFromList" styleClass="FIELD"	mode="E,E,E" >
						<layout:option key="" 		 value="" />
						<layout:options name="viewSessionForm" property="beamlineList"></layout:options>
					</layout:select>
			</logic:notEmpty>
			</layout:row>
					<layout:date key="Start Date (DD-MM-YYYY)" 	property="startDatest" 	styleClass="FIELD"	mode="E,E,E" patternKey="<%=Constants.DATE_FORMAT%>" 
					startYear="<%=(new Integer((new GregorianCalendar()).get(Calendar.YEAR) - 2)).toString()%>" 
					endYear="<%=(new Integer((new GregorianCalendar()).get(Calendar.YEAR))).toString()%>"/>
					<layout:date key="End Date (DD-MM-YYYY)" property="endDatest" styleClass="FIELD"	mode="E,E,E" patternKey="<%=Constants.DATE_FORMAT%>" 
					startYear="<%=(new Integer((new GregorianCalendar()).get(Calendar.YEAR) - 2)).toString()%>" 
					endYear="<%=(new Integer((new GregorianCalendar()).get(Calendar.YEAR))).toString()%>"/>
		</layout:column>  
		<layout:space/>
		<layout:row styleClass="rowAlignCenter">
			<layout:reset />
			<layout:submit><layout:message key="Search"/></layout:submit>			
		</layout:row>

		<layout:row>
			<html:link href="<%=targetViewAllSessions%>"  >
			  <font class="FIELD">
				<img src="<%=request.getContextPath()%>/images/magnif_16.png" border=0 onmouseover="return overlib('View all sessions');" onmouseout="return nd();">
				View all <%=Constants.SESSIONS%>
				</font>
			</html:link>
		</layout:row>
	</layout:grid>
</layout:form>

</layout:panel>
