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

<layout:skin includeScript="true" />
<script src="<%=request.getContextPath()%>/js/external/overlib/overlib.js"></script>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>

<%-- Protein --%>
<h3>Please note that protein should be created through "safety samplesheets" which should be submitted to safety prior to experiment preparation.
</h3>
<h3>Authorized characters for acronym are a-z, A-Z, 0-9 and dash.
</h3>

<layout:grid cols="1"  borderSpacing="5">
	<layout:column>
		<layout:panel key="New Protein" align="left" styleClass="PANEL">
			<layout:form action="/user/createProtein.do" reqCode="save">	
				<layout:grid cols="1" styleClass="SEARCH_GRID">	
					<layout:column>
						<layout:text key="Name" 	property="proteinInfo.name" 		styleClass="OPTIONAL"	mode="E,E,E"/>
						<layout:text key="Acronym" 	property="proteinInfo.acronym"		styleClass="FIELD"	mode="E,E,E" isRequired="true"/>

					</layout:column>		
					<layout:row>
						<layout:reset/>
						<layout:submit reqCode="save"><layout:message key="Save"/></layout:submit>
					</layout:row>
				</layout:grid>
			</layout:form>	
		</layout:panel>
	
		
	</layout:column>
</layout:grid>
<br>

<%-- Acknowledge Action --%>
<jsp:include page="/tiles/common/messages/info.jsp" flush="true" />

