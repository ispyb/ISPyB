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
	String trackingCourier = "Courier tracking Number TO "+Constants.SITE_NAME+" ";
%>


<script TYPE="text/javascript">

function selectDewarStatus(stOpened, stReadyToGo)
{
	var st = document.getElementsByName('info.dewarStatus')[0].value;
	if(st == stOpened || st == stReadyToGo){
		document.getElementById('courierTrackingF').style.display="none";
		document.getElementById('courierTrackingL').style.display="none";
	}else{
		document.getElementById('courierTrackingF').style.display="";
		document.getElementById('courierTrackingL').style.display="";
	}
	return false;
}

function setDewarStatus(){
	var st = document.getElementsByName('defaultDewarStatus')[0].value;
	document.getElementsByName('info.dewarStatus')[0].value=st;
	selectDewarStatus('opened', 'ready to go');
	return false;
}

window.onload=setDewarStatus

</script> 


<layout:skin includeScript="true" />
<script src="<%=request.getContextPath()%>/js/external/overlib/overlib.js"></script>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>

<%-- Related Information bar --%>
<jsp:include page="/tiles/common/util/breadCrumbsBar.jsp" flush="true" />
<%-------------------------%>

<%-- Errors --%>
<jsp:include page="/tiles/common/messages/error.jsp" flush="true" />
<jsp:include page="/tiles/common/messages/info.jsp" flush="true" />

<%-- Page --%>
<layout:grid cols="1"  borderSpacing="10">
	<%-- Dewar --%>
	<layout:panel key="New/Edit component" align="left" styleClass="PANEL">
		<layout:form action="/user/createDewarAction.do" reqCode="save">	

			<layout:grid cols="1"  borderSpacing="10">
				<layout:column>
					<layout:text key="Component 1D barcode" property="info.barCode" styleClass="FIELD"	mode="I,I,I"/>
					<layout:space/>
					
					<layout:text key="Component label"      property="info.code"    styleClass="FIELD"	mode="E,E,I" isRequired="true"/>
					<layout:space/>
					
					<logic:notEmpty name="viewDewarForm" property="listSessions">
            			<layout:select  key="Beamline for delivery" property="firstExperimentId" styleClass="FIELD"	mode="E,E,I" isRequired="true">
  							<bean:define id="listSessions" name="viewDewarForm" property="listSessions" type="java.util.ArrayList"/>
  							<layout:options collection="listSessions" property="sessionId"  labelProperty="sessionDescription"/>
  						</layout:select>
					</logic:notEmpty>
					<logic:empty name="viewDewarForm" property="listSessions">
						<tr><th class="FIELD">Beamline for delivery</th><td class="FIELD"><font color="red">No <%=Constants.SESSION_VISIT%> available</font></td></tr>
					</logic:empty>
					<layout:space/>
					
					<layout:textarea key="Comments"      property="info.comments" styleClass="OPTIONAL"	mode="E,E,I"	size="25" rows="4"/>
					<layout:space/>
					
					<layout:text key="Transport value (Euros)"
											 	         property="info.transportValue" styleClass="FIELD"	mode="E,E,I"/>
					<layout:text key="Customs value (Euros)"
											 	         property="info.customsValue"   styleClass="FIELD"	mode="E,E,I"/>
					<layout:space/>
					
					<layout:radios property="info.type" key="Type" styleClass="FIELD"	mode="E,E,I">
						<layout:radio property="info.type" key="" value="Dewar" styleClass="FIELD"	mode="E,E,I">
							<layout:img alt="Dewar" src="<%=request.getContextPath()+\"/images/Dewar_24x24_01.png\" %>" border="0"/>
						</layout:radio>
						<layout:radio property="info.type" key="" value="Toolbox" styleClass="FIELD"	mode="E,E,I">
							<layout:img alt="Toolbox" src="<%=request.getContextPath()+\"/images/toolbox.png\" %>" border="0"/>
						</layout:radio>
					</layout:radios>
					<layout:space/>
					
					<!-- Issue 1076: if role in {manager, localContact, blom} then the user can change the status -->
					<logic:empty name="viewDewarForm" property="info.dewarStatus"> 
						<html:text property="defaultDewarStatus" value="opened" style="display:none"></html:text>
					</logic:empty>					
					<logic:notEmpty name="viewDewarForm" property="info.dewarStatus"> 
						<bean:define id="defaultDewarStatus"  name="viewDewarForm" property="info.dewarStatus" type="java.lang.String"></bean:define>
						<html:text property="defaultDewarStatus" value="<%=defaultDewarStatus %>" style="display:none"></html:text>
					</logic:notEmpty>
														
					<logic:notEmpty name="viewDewarForm" property="info.code">
						<logic:equal name="viewDewarForm" property="role" value="<%=Constants.ROLE_BLOM%>">
							<layout:select layoutId="dewarStatus" key="Status" property="info.dewarStatus" styleClass= "FIELD" value="info.dewarStatus" mode="E,E,I" onchange="<%= \"selectDewarStatus('\"+Constants.SHIPPING_STATUS_OPENED+\"', '\"+Constants.SHIPPING_STATUS_READY_TO_GO+\"');\" %>">
								<layout:options name="viewDewarForm" property="listDewarStatus"/>
							</layout:select>
						</logic:equal>
						<logic:equal name="viewDewarForm" property="role" value="<%=Constants.ROLE_MANAGER%>">
							<layout:select layoutId="dewarStatus" key="Status" property="info.dewarStatus" styleClass= "FIELD" value="info.dewarStatus" mode="E,E,I" onchange="<%= \"selectDewarStatus('\"+Constants.SHIPPING_STATUS_OPENED+\"', '\"+Constants.SHIPPING_STATUS_READY_TO_GO+\"');\" %>">
								<layout:options name="viewDewarForm" property="listDewarStatus"/>
							</layout:select>
						</logic:equal>
						<logic:equal name="viewDewarForm" property="role" value="<%=Constants.ROLE_LOCALCONTACT%>">
							<layout:select layoutId="dewarStatus" key="Status" property="info.dewarStatus" styleClass= "FIELD" value="info.dewarStatus" mode="E,E,I" onchange="<%= \"selectDewarStatus('\"+Constants.SHIPPING_STATUS_OPENED+\"', '\"+Constants.SHIPPING_STATUS_READY_TO_GO+\"');\" %>">
								<layout:options name="viewDewarForm" property="listDewarStatus"/>
							</layout:select>
						</logic:equal>
					</logic:notEmpty>
					
					<logic:notEqual name="viewDewarForm" property="role" value="<%=Constants.ROLE_LOCALCONTACT%>">
						<logic:notEqual name="viewDewarForm" property="role" value="<%=Constants.ROLE_MANAGER%>">
							<logic:notEqual name="viewDewarForm" property="role" value="<%=Constants.ROLE_BLOM%>">
								<layout:text key="Status" property="info.dewarStatus" styleClass="FIELD" mode="I,I,I"/>
							</logic:notEqual>
						</logic:notEqual>
					</logic:notEqual>
					
					
					
					<logic:empty name="viewDewarForm" property="info.code">
						<layout:text key="Status" property="info.dewarStatus" styleClass="FIELD" mode="I,I,I"/>
					</logic:empty>
					
					<layout:space/>
						<layout:text layoutId="courierTracking" key="<%=trackingCourier%>" property="info.trackingNumberToSynchrotron" styleClass="OPTIONAL"	mode="E,E,I"/>
					
				</layout:column> 
			</layout:grid>
				
					<layout:submit reqCode="save"><layout:message key="Save"/></layout:submit>
			
		</layout:form>	
	</layout:panel>
	<layout:space/>
</layout:grid>
<%-- Errors --%>
<jsp:include page="/tiles/common/messages/error.jsp" flush="true" />


