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
<%@page import="java.util.Calendar,java.util.GregorianCalendar,java.util.Arrays,java.lang.Integer"%>

<layout:skin includeScript="true" />
<script src="<%=request.getContextPath()%>/js/external/overlib/overlib.js"></script>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>

<%-- Related Information bar --%>
<jsp:include page="/tiles/common/util/breadCrumbsBar.jsp" flush="true" />
<%-------------------------%>




<script>
function changeCapacity(optionBox, capacity) {
	var capacities = new Array<bean:write name="viewContainerForm" property="capacitiesAsString"/>
	capacity.value = capacities[optionBox.options.selectedIndex];
}
</script>

<layout:space/>

<%-- Page --%>

<layout:tabs width="400" styleClass="PANEL">
	<%------------------------------------ create new container -------------------------------------------------------%>
	<layout:tab key="Edit / Create Container" width="250">
		<layout:form action="/user/createContainerAction.do" reqCode="save">
			<logic:equal name="isSelectingContainer" value="true">
			<input type="hidden" name="isSelectingContainer" value="true" />
			</logic:equal>
	
				<layout:grid cols="1">
						<layout:select 	key="Type" 	property="container3VO.containerType" styleClass="FIELD" 
						onchange="changeCapacity(this, document.getElementsByName('container3VO.capacity')[0])"
						>
						
							<bean:define id="listContainersDefinitions" name="viewContainerForm" property="containersInfo" type="java.util.ArrayList"/>
							<layout:options collection="listContainersDefinitions" property="type"  labelProperty="type"/>
						</layout:select>
						
						<layout:text key="Capacity" 	property="container3VO.capacity"	styleClass="OPTIONAL"	mode="E,E,E"
									onfocus="changeCapacity(document.getElementsByName('container3VO.containerType')[0], this)"
									size="5">
									<script>
									changeCapacity(document.getElementsByName('container3VO.containerType')[0], document.getElementsByName('container3VO.capacity')[0])
									</script>
						</layout:text>
						
						<layout:space/>
		
						<layout:text key="Label or Bar Code" 	property="container3VO.code" 	styleClass="OPTIONAL"	mode="E,E,E"/>
	
						<layout:space/>
						<layout:text key="Status" 				property="container3VO.containerStatus" 	styleClass="FIELD"	mode="I,I,I"
									value="<%=Constants.SHIPPING_STATUS_OPENED%>"
									/>
						<layout:space/>
				
				</layout:grid>
				<layout:row>
					<layout:reset/>
					<layout:submit reqCode="save"><layout:message key="Save"/></layout:submit>
				</layout:row>
			</layout:form>	
	</layout:tab>
	<layout:tab key="Memo: positions on Cane" width="250">
		<img src="<%=request.getContextPath()%>/images/sample_cane.jpg" border="0" />
	</layout:tab>
</layout:tabs>

