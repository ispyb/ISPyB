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

<%
	String targetContainer 			= request.getContextPath() + "/menuSelected.do?leftMenuId=11&topMenuId=10&targetUrl=/user/viewSample.do?reqCode=displayForContainer";
	String targetDewar 				= request.getContextPath() + "/user/viewDewarAction.do?reqCode=displaySlave";
	String targetSample 			= request.getContextPath() + "/menuSelected.do?leftMenuId=11&topMenuId=10&targetUrl=/user/viewSample.do?reqCode=displayForDewar";
	String targetCreateDewar 		= request.getContextPath() + "/menuSelected.do?leftMenuId=6&topMenuId=5&targetUrl=/user/createDewarAction.do?reqCode=display";
	String targetCreateContainer	= request.getContextPath() + "/menuSelected.do?leftMenuId=6&topMenuId=5&targetUrl=/user/createContainerAction.do?reqCode=display";
	
	String targetUpdateContainer	= request.getContextPath() + "/menuSelected.do?leftMenuId=6&topMenuId=5&targetUrl=/user/createContainerAction.do?reqCode=updateDisplay";
	String targetCreateSample		= request.getContextPath() + "/menuSelected.do?leftMenuId=6&topMenuId=5&targetUrl=/user/createSampleAction.do?reqCode=display";
	String targetDeleteContainer	= request.getContextPath() + "/menuSelected.do?leftMenuId=6&topMenuId=5&targetUrl=/user/createContainerAction.do?reqCode=delete";
	String targetCloneContainer		= request.getContextPath() + "/menuSelected.do?leftMenuId=8&topMenuId=5&targetUrl=/user/createContainerAction.do?reqCode=clone";
	
	String targetUpdateDewar		= request.getContextPath() + "/menuSelected.do?leftMenuId=6&topMenuId=5&targetUrl=/user/createDewarAction.do?reqCode=updateDisplay";
	String targetDeleteDewar		= request.getContextPath() + "/menuSelected.do?leftMenuId=6&topMenuId=5&targetUrl=/user/createDewarAction.do?reqCode=delete";

	String targetViewShippings		= request.getContextPath() + "/menuSelected.do?leftMenuId=8&topMenuId=5&targetUrl=/user/viewShippingAction.do?reqCode=display";	
	String targetViewDewars			= request.getContextPath() + "/menuSelected.do?leftMenuId=8&topMenuId=5&targetUrl=/user/viewDewarAction.do?reqCode=display";
	String selectedDewarTitle		= 	"<a href=\"" + targetViewDewars + "\">" +
										"<img src=\"" + request.getContextPath() + "/images/up.gif\" border=0 onmouseover=\"return overlib('View Dewars');\" onmouseout=\"return nd();\">" +
										"</a>" +
										"Selected Dewar";
	String targetEditSamples		= request.getContextPath() + "/menuSelected.do?leftMenuId=6&topMenuId=5&targetUrl=/user/createPuckAction.do?reqCode=display";
	
	String selectProperty			= "";
	String selectName				= "";
	String selectType				= "";
%>

<layout:skin includeScript="true"/>
<script src="<%=request.getContextPath()%>/js/external/overlib/overlib.js"></script>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>

<%----------------------------------------------------- Containers -----------------------------------------------------------------------%>	
	<%-- To show a message when a dewar is selected and no container exists --%>
	<%-- View Dewar list --%>
	<logic:present name="viewDewarForm" scope="request">
	<bean:define name="viewDewarForm" 		id="myForm" 							type="ispyb.client.common.shipping.ViewDewarForm"			toScope="request"/>
	<bean:define name="viewDewarForm" 		property="listInfoSlave" id="myList" 	type="java.util.List"								toScope="request"/>
	</logic:present>
	
	<%-- Select Dewar from list--%>
	<logic:notPresent name="viewDewarForm" scope="request">
	<bean:define name="viewContainerForm" 	id="myForm" 							type="ispyb.client.mx.container.ViewContainerForm"		toScope="request"/>
	<bean:define name="viewContainerForm" property="freeContainerList" id="myList" 	type="java.util.List"								toScope="request"/>
		<%
		 selectProperty	= "containerId";
		 selectName		= "theContainerId";
		 selectType		= "radio";
		%>
	</logic:notPresent>
    
    <logic:present 	name="myForm">
    <logic:notEmpty name="myList">
		<%---------------------- Containers --------------------------------%>
			<layout:collection 	name="myList" 
								styleClass="SLAVE"
								id="container"
								selectProperty="<%=selectProperty%>"  selectName="<%=selectName%>" selectType="<%=selectType%>"
								>
		
		<%---------------%>						
				<layout:collectionItem title="Code" width="40" property="code" href="<%=targetContainer%>"	 paramId="containerId" paramProperty="containerId" />
				<layout:collectionItem title="Capacity" width="50" property="capacity"/>
				<layout:collectionItem title="Type" width="40" property="containerType"/>
				<layout:collectionItem title="View Samples"	width="80" sortable="false" href="<%=targetContainer%>" paramId="containerId" paramProperty="containerId">
					<center><img src="<%=request.getContextPath()%>/images/SampleHolderView_24x24_01.png" border=0 onmouseover="return overlib('View Samples in container');" onmouseout="return nd();"></center>
				</layout:collectionItem>
			
			
				<%-- Actions : edit/delete/add new not possible if shipment is in processing state   --%>	


				<logic:notEqual name="breadCrumbsForm" property="selectedShipping.shippingStatus" value="<%=Constants.SHIPPING_STATUS_CLOSED%>">
				<logic:notEqual name="breadCrumbsForm" property="selectedShipping.shippingStatus" value="<%=Constants.SHIPPING_STATUS_PROCESS%>">	
														
				<layout:collectionItem title="Actions" width="120">
					<div align="left">
					<logic:present name="breadCrumbsForm" property="selectedShipping" scope="session">

						<html:link href="<%=targetCreateSample%>" paramName="container" paramId="containerId" paramProperty="containerId">
							<img src="<%=request.getContextPath()%>/images/SampleHolderAdd_24x24_01.png" border=0 onmouseover="return overlib('Add Sample to Container');" onmouseout="return nd();">
						</html:link>
						
						<html:link href="<%=targetUpdateContainer%>" paramName="container" paramId="containerId" paramProperty="containerId">
							<img src="<%=request.getContextPath()%>/images/BasketEdit_24x24_01.png" border="0" onmouseover="return overlib('Edit Container');" onmouseout="return nd();"/>
						</html:link>						
						
						<html:link href="<%=targetDeleteContainer%>" paramName="container" paramId="containerId" paramProperty="containerId" onclick="return window.confirm('Do you realy want to delete this Container?');">
							<img src="<%=request.getContextPath()%>/images/BasketDelete_24x24_01.png" border=0 onmouseover="return overlib('Delete the Container');" onmouseout="return nd();">
						</html:link>
						
						<html:link href="<%=targetEditSamples%>" paramName="container" paramId="containerId" paramProperty="containerId">
							<img src="<%=request.getContextPath()%>/images/magnif.png" border="0" onmouseover="return overlib('Edit Samples');" onmouseout="return nd();"/>
						</html:link>	

					</logic:present>
					</div>
				</layout:collectionItem>	

				</logic:notEqual>
				</logic:notEqual>


			</layout:collection>
			
			
	</logic:notEmpty>
	</logic:present>
<%----------------------------------------------------------------------------------------------------------------------------------------%>	
