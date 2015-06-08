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

<layout:skin  includeScript="true"/>

	
<layout:grid cols="1" styleClass="SEARCH_GRID">	

	<layout:panel key="Search Sample" align="left" styleClass="PANEL">
	<layout:form action="/user/viewSample.do" reqCode="displayForName">

		<layout:column>
					<layout:text key="Name" 				property="name" 	styleClass="FIELD"	mode="E,E,E"/>
					<layout:text key="Datamatrix Code" 		property="code" 	styleClass="FIELD"	mode="E,E,E"/>
		</layout:column>		

		<layout:space/>	

		<layout:row>
			<layout:reset/>
			<layout:submit><layout:message key="Search"/></layout:submit>
		</layout:row>

	</layout:form>	
	</layout:panel>
</layout:grid>
		
		<h4>You may use the * character to do a search with an incomplete Name.</h4>


