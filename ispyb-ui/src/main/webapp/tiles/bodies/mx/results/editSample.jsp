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
<%--Edit a sample already existing --%>

<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.application-servers.com/layout" prefix="layout" %>

<layout:skin includeScript="true" />
<script src="<%=request.getContextPath()%>/js/external/overlib/overlib.js"></script>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>

<%-- BreadCrumbs bar --%>
<jsp:include page="/tiles/common/util/breadCrumbsBar.jsp" flush="true" />
<%-------------------------%>

<%-- page --%>
<layout:grid cols="1"  borderSpacing="10">

<%-- form to edit sample --%>
	<layout:panel key="Edit Sample" align="left" styleClass="PANEL">
		<layout:form action="/user/analyseSample.do" reqCode="updateSample">	
			<layout:grid cols="2" styleClass="SEARCH_GRID">	
				<layout:column>
					<layout:text key="Sample Name:" 		property="info.name" 			styleClass="FIELD"	mode="I,I,I"/>
                    <layout:text key="Id" 				property="info.blSampleId" 	styleClass="FIELD"	mode="H,H,H"/>
				</layout:column>
				<layout:column>
					<layout:text key="Data Matrix Code:" property="info.code"			styleClass="FIELD"	mode="I,I,I"/>
				</layout:column>
			</layout:grid>
			<layout:space/>
			<layout:row>
					<layout:text key="Sample status (T-date, C-date, Trash?, Trash, None)" 	property="info.blSampleStatus"		styleClass="FIELD"	mode="E,E,E"	size="10"/>
			</layout:row>
			<layout:space/>

			<%-- TODO : add data collection details to help filling following fields ??? --%>
			
			<layout:grid cols="2" styleClass="SEARCH_GRID">	
				<layout:column>
					<layout:text key="Completion Stage" 	property="info.completionStage"		styleClass="FIELD"	mode="E,E,E"	size="10"/>
					<layout:text key="Structure Stage" 		property="info.structureStage"		styleClass="FIELD"	mode="E,E,E"	size="10"/>
					<layout:text key="Publication Stage" 	property="info.publicationStage"	styleClass="FIELD"	mode="E,E,E"	size="10"/>
				</layout:column>
				<layout:column>
				<layout:textarea key="Publication<br>Details" property="info.publicationComments" styleClass="FIELD"	mode="E,E,E"	size="25" rows="4"/>
				</layout:column>
			</layout:grid>
					
			<layout:row>
				<layout:reset/>
				<layout:submit reqCode="updateSample"><layout:message key="Save"/></layout:submit>
			</layout:row>
			
		</layout:form>	
	</layout:panel>
</layout:grid>

<br>

<%-- Acknowledge Action --%>
<jsp:include page="/tiles/common/messages/info.jsp" flush="true" />
