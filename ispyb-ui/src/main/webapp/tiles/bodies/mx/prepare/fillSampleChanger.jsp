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
<%@ page isELIgnored="false" %>
<%@page import="ispyb.common.util.Constants"%>

<%@ page isELIgnored="false" %>

<% 
    String buttonLabel 					= "Next step: Link Samples in " + Constants.BCM_NAME;
%>

<layout:skin includeScript="true" />
<script src="<%=request.getContextPath()%>/js/external/overlib/overlib.js"></script>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>

<%-------------------------------------------%>
<%-- Prepare your experiment 			   --%>
<%-------------------------------------------%>

<h4>Prepare your experiment:</h4>  
<p align="justify">

	<%-- 1) Select shipment --%>
	<span class="greyText">
		1- Select the dewars you want for processing.<br>
	</span>

	<%-- 2) Fill Sample changer --%>
	<b>
	<c:if test="${SITE_ATTRIBUTE eq 'ESRF' or SITE_ATTRIBUTE eq 'EMBL'}"> 
		2- Fill the sample changer: assign a location for your containers (only required if not using Damatrix codes)<br>
	</c:if>
	
	<c:if test="${SITE_ATTRIBUTE eq 'DLS'}">
		2- Fill the sample changer: assign a location for your containers<br>
	</c:if>
	
	<c:if test="${SITE_ATTRIBUTE eq 'MAXIV'}">
		2- Fill the sample changer: assign a location for your containers<br>
	</c:if>  

	<c:if test="${SITE_ATTRIBUTE eq 'SOLEIL'}">
		2- Fill the sample changer: assign a location for your containers<br>
	</c:if>
	</b>

	<%-- 3) Link crystals --%>
	<span class="greyText">
	<c:if test="${SITE_ATTRIBUTE ne 'ESRF' and SITE_ATTRIBUTE ne 'EMBL'}">
		3- Associate data collections to samples in <%=Constants.BCM_NAME%>.<br>
	</c:if> 
	<c:if test="${SITE_ATTRIBUTE eq 'ESRF' or SITE_ATTRIBUTE eq 'EMBL'}"> 
		3- In MxCuBe, link the samples in the container to the crystals in the Sample Changer.<br>
	</c:if>
	<c:if test="${SITE_ATTRIBUTE eq 'SOLEIL'}"> 
		3- In MxCuBe, link the samples in the container to the crystals in the Sample Changer.<br>
	</c:if>	
	</span>
	
	<%-- Steps --%>
	<br>
	<a class="noUnderline" href="<%=request.getContextPath()%>/user/prepareExp.do?reqCode=selectDewar" title="Select the dewars">
	<img src="<%=request.getContextPath()%>/images/Previous-16x16.png" border=0>
	</a>
	<a href="<%=request.getContextPath()%>/helpMxcubeGuestPage.do" title="Link Samples in MxCuBe">
	<img src="<%=request.getContextPath()%>/images/Next-16x16.png" border=0></a>
	<input type=button value='<%=buttonLabel%>' onclick="parent.location='<%=request.getContextPath()%>/helpMxcubeGuestPage.do'"">	

</p>

<%-------------------------------------------%>
<%-- Fill Sample Changer                   --%>
<%-------------------------------------------%>

<c:if test="${SITE_ATTRIBUTE eq 'ESRF'}">
	<h4> This step is not required if you are using Datamatrix code on your samples pins.</h4>
</c:if>

<%-- To not show a empty page when no processing shipment exists --%>	
<logic:empty name="viewContainerForm" property="containersInfo">
           <h4>No&nbsp;containers&nbsp;have&nbsp;been&nbsp;found.</h4>
           <h4>Have&nbsp;you&nbsp;selected&nbsp;a&nbsp;dewar&nbsp;to&nbsp;process?</h4>
</logic:empty>

<logic:notEmpty name="viewContainerForm" property="containersInfo">

<layout:grid cols="1"  borderSpacing="1">
<layout:column>

<layout:panel key="Containers to load in sample changer" align="center" styleClass="PANEL">
<table cellspacing="1" cellpadding="3" border="0" width="100%" align="CENTER" class="LIST">
	<tr>
		<th class="LIST">Shipment<br>name</th>
		<th class="LIST">Shipment<br>creation date</th>
		<th class="LIST">Dewar<br>label</th>
		<th class="LIST">Dewar<br>barcode</th>
		<th class="LIST">Container<br>code</th>
		<th class="LIST">Container<br>type</th>
		<th class="LIST">Container<br>capacity</th>
		<th class="LIST">#<br>samples</th>
		<th class="LIST">Beamline<br>Location</th>
		<th class="LIST">Location in<br>Sample Changer</th>
	</tr>
	
	<html:form action="/user/fillSampleChanger">
		<html:hidden property="reqCode" value="updateAll"/>
			
		<logic:iterate name="viewContainerForm" property="containersInfo" id="item" indexId="index" type="ispyb.server.common.vos.shipping.Container3VO">
		
			<html:hidden name="viewDataCollectionForm" property="<%= \"containerIdList[\" + index + \"]\" %>" value="<%=item.getContainerId().toString()%>" />  
			<tr class="LIST" onmouseover="this.className='LIST_OVER'" onmouseout="this.className='LIST'">
				<td class="LEFT"><bean:write name="viewContainerForm" property="<%=\"shipmentNameList[\" + index + \"]\" %>" /></td>		
				<td><bean:write name="viewContainerForm" property="<%=\"creationDateList[\" + index + \"]\" %>" format="dd-MM-yyyy" /></td>
				<td><bean:write name="item" property="dewarVO.code"/></td>
				<td><bean:write name="item" property="dewarVO.barCode"/></td>
				<td><bean:write name="item" property="code"/></td>
				<td><bean:write name="item" property="containerType"/></td>
				<td><bean:write name="item" property="capacity"/></td>
				<td><bean:write name="viewContainerForm" property="<%=\"nbSampleList[\" + index + \"]\" %>" /></td>
				<td>
					<html:select styleClass="FIELD_COMBO" size="1" name="viewContainerForm" property="<%=\"beamlineLocationList[\" + index + \"]\" %>">
                        <c:if test="${SITE_ATTRIBUTE eq 'MAXIV'}">
                            <html:options collection="beamlineList" property="value" labelProperty="label" />
                            <html:option value=""></html:option>
                        </c:if>
                        <c:if test="${SITE_ATTRIBUTE ne 'MAXIV'}">
                            <html:option value=""></html:option>
                            <html:options collection="beamlineList" property="value" labelProperty="label" />
                        </c:if>
					</html:select>
				</td>
				<td>
					<html:select styleClass="FIELD_COMBO" size="1" name="viewContainerForm" property="<%=\"sampleChangerLocationList[\" + index + \"]\" %>">
						<html:option value=""></html:option>
						<html:options collection="scList" property="value" labelProperty="label" />
					</html:select>
				</td>			
				
			</tr>
		
		</logic:iterate>
		<tr><td class="LIST" colspan="10"><html:submit value="Save"/></td></tr>
		
	</html:form>
</table>
</layout:panel>

</layout:column>
</layout:grid>

</logic:notEmpty>
