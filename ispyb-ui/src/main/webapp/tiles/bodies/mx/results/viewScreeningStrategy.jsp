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

<layout:skin />

<%@page import="ispyb.common.util.Constants"%>

<% 
	int nbStrategyWedge = 0;
	String targetScreeningStrategy	= request.getContextPath() + "/user/viewScreeningStrategy.do?reqCode=display";
%>

<script TYPE="text/javascript">

	function clearStrategySubWedge(nbStrategyWedge, screeningStrategyWedgeId){
		for(var i=0; i<nbStrategyWedge; i++){
			document.getElementById('strategySubWedgeTable_'+i).style.display = 'none';
		}
		return;
	}
	
	function showStrategySubWedge(screeningStrategyWedgeId){
		strategyWedgeIndexSelected = screeningStrategyWedgeId;
		document.getElementById('strategySubWedgeTable_'+screeningStrategyWedgeId).style.display = '';
		
		return;
	}
</script> 



<%-- BreadCrumbs bar --%>
<jsp:include page="/tiles/common/util/breadCrumbsBar.jsp" flush="true" />

<%------------- DNA Strategies ---------------------%>
<layout:grid cols="1"  borderSpacing="10">
	<layout:column>
	<layout:tabs width="100%">
		<layout:tab key="Strategy" width="150">
			<layout:text key="Osc. start (&deg;)" 	name="viewScreeningStrategyForm"  property="screeningStrategyValueInfo.phiStart" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
			<layout:text key="Osc. end (&deg;)" 	name="viewScreeningStrategyForm"  property="screeningStrategyValueInfo.phiEnd" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
			<layout:text key="Rotation (&deg;)" 	name="viewScreeningStrategyForm"  property="screeningStrategyValueInfo.rotation" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
			<layout:text key="Transmission (&#37;)" 	name="viewScreeningStrategyForm"  property="screeningStrategyValueInfo.transmission" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
			<layout:text key="Exposure time (s)" 	name="viewScreeningStrategyForm"  property="screeningStrategyValueInfo.exposureTime" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
			<layout:text key="Strategy resolution (&Aring;)" 	name="viewScreeningStrategyForm"  property="screeningStrategyValueInfo.resolution" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
			<layout:text key="Total exposure time (s)" 	name="viewScreeningStrategyForm"  property="screeningStrategyValueInfo.totExposureTime" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
			<layout:text key="Total Nb images" 	name="viewScreeningStrategyForm"  property="screeningStrategyValueInfo.totNbImages" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
			<layout:text key="Program" 	name="viewScreeningStrategyForm"  property="screeningStrategyValueInfo.program" styleClass="FIELD_DISPLAY" mode="I,I,I"/>
		</layout:tab>
		<logic:equal name="viewScreeningStrategyForm" property="screeningStrategyValueInfo.programLogFileExists" value="true">
				<layout:tab key="Program Log File" width="150">
					<bean:define id="dNAContent" name="viewScreeningStrategyForm" property="DNAContent" type="java.lang.String"/>
					<bean:define id="dNASelectedFile" name="viewScreeningStrategyForm" property="DNASelectedFile" type="java.lang.String"/>
					<layout:space></layout:space>
					<h3><%=dNASelectedFile%></h3>
					<textarea CLASS="TEXT">
						<%=dNAContent%>
					</textarea>
				</layout:tab>
		</logic:equal>
	</layout:tabs>
	</layout:column>
</layout:grid>


<%--Strategy Wedge--%>
<logic:present name="viewScreeningStrategyForm"  property="listStrategiesWedgeInfo"> 
<logic:notEmpty name="viewScreeningStrategyForm"  property="listStrategiesWedgeInfo">
	<h4>Strategies Wedge</h4>
		<table cellspacing="1" cellpadding="1" border="0" width="99%" align="CENTER" class="LIST" >
			<%-- ----- Headers ----- --%>
			<tr>
				<th class="LIST">View<br>details</th>
				<th class="LIST">Wedge number</th>
				<th class="LIST">Resolution<br>&Aring;</th>
				<th class="LIST">Completeness</th>
				<th class="LIST">Multiplicity</th>
				<th class="LIST">Total dose</th>
				<th class="LIST">Number of<br>images</th>
				<th class="LIST">Phi<br>&deg;</th>
				<th class="LIST">Kappa<br>&deg;</th>
			</tr>
			<%-- ----- Rows ----- --%>
			<logic:iterate name="viewScreeningStrategyForm" property="listStrategiesWedgeInfo" id="strategyWedgeInfo" indexId="strategyWedgeIndex" type="ispyb.server.mx.vos.screening.ScreeningStrategyWedge3VO">
			<%nbStrategyWedge++;%>
			</logic:iterate>
			<logic:iterate name="viewScreeningStrategyForm" property="<%= \"listStrategiesWedgeInfo\" %>" id="strategyWedgeInfo" indexId="strategyWedgeIndex" type="ispyb.server.mx.vos.screening.ScreeningStrategyWedge3VO">
				 <tr class="LIST" id="strategyWedgeRow_<%=strategyWedgeIndex%>" onmouseover="this.className='LIST_OVER';showStrategySubWedge(<%=strategyWedgeIndex%>);" onmouseout="this.className='LIST';clearStrategySubWedge(<%=nbStrategyWedge%>, <%=strategyWedgeIndex%>);"> 
				<%--	<tr class="LIST" id="strategyWedgeRow_<%=strategyWedgeIndex%>" >--%>
					<bean:define name="viewScreeningStrategyForm" 	scope="request" id="viewScreeningStrategyForm" 	type="ispyb.client.mx.results.ViewScreeningStrategyForm"/>
						<td class="LEFT" nowrap> 
						&nbsp;
							<logic:present name="viewScreeningStrategyForm"  property="<%= \"listStrategiesSubWedgeInfoAll[\"+strategyWedgeIndex+\"]\" %>">
							<logic:notEmpty name="viewScreeningStrategyForm"  property="<%= \"listStrategiesSubWedgeInfoAll[\"+strategyWedgeIndex+\"]\" %>">
								+
							</logic:notEmpty>
							</logic:present>
						</td>
					<td><bean:write name="strategyWedgeInfo" property="wedgeNumber"/></td>
					<td><bean:write name="strategyWedgeInfo" property="resolution"/></td>
					<td><bean:write name="strategyWedgeInfo" property="completeness"/></td>
					<td><bean:write name="strategyWedgeInfo" property="multiplicity"/></td>
					<td><bean:write name="strategyWedgeInfo" property="doseTotal"/></td>
					<td><bean:write name="strategyWedgeInfo" property="numberOfImages"/></td>
					<td><bean:write name="strategyWedgeInfo" property="phi"/></td>
					<td><bean:write name="strategyWedgeInfo" property="kappa"/></td>
				</tr>
			</logic:iterate>
		</table>
		<%--Strategy SubWedge--%>
			<logic:present name="viewScreeningStrategyForm"  property="listStrategiesSubWedgeInfoAll">
			<logic:notEmpty name="viewScreeningStrategyForm"  property="listStrategiesSubWedgeInfoAll">
			<%--<logic:present name="viewScreeningStrategyForm"  property="listStrategiesSubWedgeInfo">--%>
			<%--<logic:notEmpty name="viewScreeningStrategyForm"  property="listStrategiesSubWedgeInfo">--%>
				<h4>Strategies SubWedge</h4>
				<logic:iterate name="viewScreeningStrategyForm" property="<%= \"listStrategiesWedgeInfo\" %>" id="strategyWedgeInfo" indexId="strategyWedgeIndex" type="ispyb.server.mx.vos.screening.ScreeningStrategyWedge3VO">
				<logic:notEmpty name="viewScreeningStrategyForm"  property="<%= \"listStrategiesSubWedgeInfoAll[\"+strategyWedgeIndex+\"]\" %>">
				<%--<logic:notEmpty name="viewScreeningStrategyForm"  property="<%= \"listStrategiesSubWedgeInfo\" %>">--%>
				<table cellspacing="1" cellpadding="1" border="0" width="99%" align="CENTER" class="LIST" id="strategySubWedgeTable_<%=strategyWedgeIndex%>" style="display:none" >
				<%-- ----- Headers ----- --%>
				<tr>
					<th class="LIST">Sub Wedge number</th>
					<th class="LIST">Rotation axis</th>
					<th class="LIST">Axis start</th>
					<th class="LIST">Axis end</th>
					<th class="LIST">Exposure time</th>
					<th class="LIST">Transmission</th>
					<th class="LIST">Oscillation range</th>
					<th class="LIST">Completeness</th>
					<th class="LIST">Multiplicity</th>
					<th class="LIST">Resolution<br>&Aring;</th>
					<th class="LIST">Total dose</th>
					<th class="LIST">Number of<br>images</th>
				</tr>
				<%-- ----- Rows ----- --%>
				 <logic:iterate name="viewScreeningStrategyForm" property="<%= \"listStrategiesWedgeInfo\" %>" id="strategyWedgeInfo2" indexId="strategyWedgeIndex2" type="ispyb.server.mx.vos.screening.ScreeningStrategyWedge3VO"> 
				 <logic:iterate name="viewScreeningStrategyForm" property="<%= \"listStrategiesSubWedgeInfoAll[\"+strategyWedgeIndex+\"]\" %>" id="strategySubWedgeInfo" indexId="strategySubWedgeIndex" type="ispyb.server.mx.vos.screening.ScreeningStrategySubWedge3VO">
					<tr class="LIST"  >
						<td><bean:write name="strategySubWedgeInfo" property="subWedgeNumber"/></td>
						<td><bean:write name="strategySubWedgeInfo" property="rotationAxis"/></td>
						<td><bean:write name="strategySubWedgeInfo" property="axisStart"/></td>
						<td><bean:write name="strategySubWedgeInfo" property="axisEnd"/></td>
						<td><bean:write name="strategySubWedgeInfo" property="exposureTime"/></td>
						<td><bean:write name="strategySubWedgeInfo" property="transmission"/></td>
						<td><bean:write name="strategySubWedgeInfo" property="oscillationRange"/></td>
						<td><bean:write name="strategySubWedgeInfo" property="completeness"/></td>
						<td><bean:write name="strategySubWedgeInfo" property="multiplicity"/></td>
						<td><bean:write name="strategySubWedgeInfo" property="resolution"/></td>
						<td><bean:write name="strategySubWedgeInfo" property="doseTotal"/></td>
						<td><bean:write name="strategySubWedgeInfo" property="numberOfImages"/></td>
					</tr>
				</logic:iterate>
				</logic:iterate>
				</table>
				</logic:notEmpty>
				 </logic:iterate> 
			</logic:notEmpty>
			</logic:present>
		<%--/Strategy SubWedge--%>
</logic:notEmpty>
</logic:present>
<%--/Strategy Wedge--%>
