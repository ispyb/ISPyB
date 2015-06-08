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

<layout:skin includeScript="true" />

<layout:grid cols="1"  borderSpacing="10" align="center">

	<layout:column >
	<layout:panel key="Who's online" align="center" styleClass="PANEL" width="550px">
		<layout:collection 	name="adminSiteForm" property="listInfo1" id="currInfo1" styleClass="LIST" styleClass2="LIST2" width="100%">
				<layout:collectionItem title="Name" 			property="username" sortable="true"/>
				<layout:collectionItem title="Last action date" property="dateTime" sortable="true"/>
				<layout:collectionItem title="Last action page" property="comments" sortable="true"/>
		</layout:collection>
	</layout:panel>
	<layout:space/>  
	<layout:panel key="Last actions of the day" align="center" styleClass="PANEL" width="550px">
		<layout:collection 	name="adminSiteForm" property="listInfo2" id="currInfo2" styleClass="LIST" styleClass2="LIST2" width="100%">
				<layout:collectionItem title="Name" 			property="username" sortable="true"/>
				<layout:collectionItem title="Last action date" property="dateTime" sortable="true"/>
				<layout:collectionItem title="Status" 			property="action" sortable="true"/>
				<layout:collectionItem title="Last action page" property="comments" sortable="true"/>
		</layout:collection>
	</layout:panel>
	
	</layout:column>
	
</layout:grid>


