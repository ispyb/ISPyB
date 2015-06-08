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
<%@page import="ispyb.common.util.StringUtils"%>
<%@page import="java.util.List"%>

<LINK href="<%=request.getContextPath()%>/css/progressbarstyle.css" type=text/css rel=STYLESHEET />

<script type='text/javascript' src="<%=request.getContextPath()%>/js/external/toggleDiv.js"></script>

<%
    String targetViewAllDC  = request.getContextPath() + "/menuSelected.do?leftMenuId=-1&topMenuId=16&targetUrl=/user/viewDataCollection.do?reqCode=displayForSession";
    String targetViewDataCollection = request.getContextPath() + "/user/viewDataCollection.do?reqCode=displayForDataCollectionGroup";
%>


<script TYPE="text/javascript">

	function statusPopup(icon, mess1,  mess2, mess3)
	{
		var popupMessage = "";
		popupMessage += "<TABLE cellSpacing=1 cellPadding=0 width=100% border=0>";
		popupMessage += "<TR><TD valign=middle><img src='"+icon+"' border=0\/><\/TD><TD class='smallText_black' nowrap>"+mess1+"<\/TD><\/TR>";
		popupMessage += "<TR><TD><\/TD><TD class='smallText_black' nowrap>"+mess2+"<\/TD><\/TR>";
		popupMessage += "<TR><TD><\/TD><TD class='smallText_black' nowrap>"+mess3+"<\/TD><\/TR>";
		popupMessage += "<\/TABLE>";
	    return overlib(popupMessage);
	}

	function viewDCG_setValue(myfield,value)
	{
		myfield.value = value;
		return false;
	}

</script> 


<table>
	<tr>
		<td>
			<layout:grid cols="1">
				<layout:row>
					<layout:link title="View dataCollections for all groups" href="<%=targetViewAllDC%>" paramName="viewDataCollectionGroupForm" paramId="sessionId" paramProperty="sessionId" styleClass="FIELD">
						<img src="<%=request.getContextPath()%>/images/magnif_16.png" border=0 onmouseover="return overlib('View dataCollections for all groups');" onmouseout="return nd();">
						View dataCollections for all groups
					</layout:link>	
				</layout:row>
			</layout:grid>
		</td>
	</tr>
</table>

<html:form style="\" autocomplete=\"off" action="/user/viewDataCollectionGroup"> 
	<html:hidden property="reqCode" value="update"/>
	<html:hidden property="actionName" value="none"/>
	<bean:define name="viewDataCollectionGroupForm" property="listDataCollectionGroups" id="currentList" type="java.util.List" toScope="request"/>
	<table cellspacing="1" cellpadding="1" border="0" width="99%" align="CENTER" class="LIST" id="dcgTable">
		<%-- ##### Headers ##### --%>
		<tr>
			<th class="LIST">Workflow</th>
			<%--experiment type--%>
			<th class="LIST">Experiment Type</th>
			<th class="LIST">Protein Acronym<br/>(Image prefix)</th>
			<th class="LIST">Sample name</th>
			<th class="LIST">Sample position</th>
			<th class="LIST">Start Time</th>
			<%-- display crystalClass only for IFX proposals (MXPress experiments) --%>
		    <logic:equal name="breadCrumbsForm" property="proposalCode" value="<%=Constants.PROPOSAL_CODE_FX%>" >
				<th class="LIST">Crystal Class</th>
			</logic:equal>
			<th class="LIST">Comments</th>
			<th class="LIST">Nb Data<br/>Collections<br/>(Nb images)</th>
			<th class="LIST">View collections</th>
		</tr>
		<%-- ##### Action buttons ##### --%>
		<logic:equal name="viewDataCollectionGroupForm" property="editSkipAndComments" value="true" >
			<tr>
				<td colspan="6" class="LIST">&nbsp;</td>
				<logic:equal name="breadCrumbsForm" property="proposalCode" value="<%=Constants.PROPOSAL_CODE_FX%>" >
					<td colspan="2" class="LIST">
			  			<input class="FIELD_BUTTON" name="Action" type="submit" value="Save" onclick="viewDC_setValue(this.form.actionName,'SaveDataCollectionGroup');" onmouseover="return overlib('Save crystal class and comments');" onmouseout="return nd();">
			  		</td>
			  	</logic:equal>
			  	<logic:notEqual name="breadCrumbsForm" property="proposalCode" value="<%=Constants.PROPOSAL_CODE_FX%>" >
			  		<td colspan="1" class="LIST">
			  			<input class="FIELD_BUTTON" name="Action" type="submit" value="Save" onclick="viewDC_setValue(this.form.actionName,'SaveDataCollectionGroup');" onmouseover="return overlib('Save comments');" onmouseout="return nd();">
			  		</td>
			  	</logic:notEqual>
			  	
			  	<td colspan="2" class="LIST">&nbsp;</td>
			</tr>
		</logic:equal>	
		<%-- ##### Rows ##### --%>
		<logic:iterate name="viewDataCollectionGroupForm" property="listDataCollectionGroups" id="item" indexId="index" type="ispyb.server.mx.vos.collections.DataCollectionGroup3VO">
			<html:hidden name="viewDataCollectionGroupForm" property="<%= \"idList[\" + index + \"]\" %>" value="<%=item.getDataCollectionGroupId().toString()%>" />         	
			<tr class="LIST" onmouseover="this.className='LIST_OVER'" onmouseout="this.className='LIST'">
				<%-- Workflow --%>
				<logic:empty name="item" property="workflowVO">
					<td></td>
				</logic:empty>
				<logic:notEmpty name="item" property="workflowVO">
					<%-- workflow status --%>
					<logic:empty name="item" property="workflowVO.status">
						<bean:define id="icon" value="../images/Sphere_White_12.png" />
						<bean:define id="mess1" value="" />
						<logic:empty name="item" property="workflowVO.workflowTitle">
							<bean:define id="mess2" value="" />
						</logic:empty>
						<logic:notEmpty name="item" property="workflowVO.workflowTitle">
							<bean:define id="title" name="item" property="workflowVO.workflowTitle"/>
							<bean:define id="mess2" value="${title}" />
						</logic:notEmpty> 
						<logic:empty name="item" property="workflowVO.comments">
							<bean:define id="mess3" value="" />
						</logic:empty>
						<logic:notEmpty name="item" property="workflowVO.comments">
							<bean:define id="comments" name="item" property="workflowVO.comments"/>
							<bean:define id="mess3" value="${comments}" />
						</logic:notEmpty>
					</logic:empty>
					<logic:notEmpty name="item" property="workflowVO.status">
						<bean:define id="status" name="item" property="workflowVO.status"/>
						<bean:define id="mess1" value="${status}" />
						<logic:empty name="item" property="workflowVO.workflowTitle">
							<bean:define id="mess2" value="" />
						</logic:empty>
						<logic:notEmpty name="item" property="workflowVO.workflowTitle">
							<bean:define id="title" name="item" property="workflowVO.workflowTitle"/>
							<bean:define id="mess2" value="${title}" />
						</logic:notEmpty> 
						<logic:empty name="item" property="workflowVO.comments">
							<bean:define id="mess3" value="" />
						</logic:empty>
						<logic:notEmpty name="item" property="workflowVO.comments">
							<bean:define id="comments" name="item" property="workflowVO.comments"/>
							<bean:define id="mess3" value="${comments}" />
						</logic:notEmpty>
						<logic:equal name="item" property="workflowVO.status" value="Success">
							<bean:define id="icon" value="../images/Sphere_Green_12.png" />
						</logic:equal> 
						<logic:equal name="item" property="workflowVO.status" value="Started">
							<bean:define id="icon" value="../images/Sphere_Orange_12.png" />
						</logic:equal>
						<logic:equal name="item" property="workflowVO.status" value="Failure">
							<bean:define id="icon" value="../images/Sphere_Red_12.png" />
						</logic:equal>
					</logic:notEmpty> 
				
					<%-- Display status icons 	--%>
					<td nowrap onmouseover="return statusPopup('${icon}','${mess1}','${mess2}','${mess3}');" onmouseout="return nd();">
						<img src="${icon}" border=0> <bean:write name="item" property="workflowVO.workflowType"/>
					</td>
				</logic:notEmpty>
				<%-- Experiment type --%>
				<td><bean:write name="item" property="experimentType"/></td>
				<%-- Protein Acronym --%>
				<td>
				<logic:notEmpty name="item"  property="blSampleVO">
					<logic:notEmpty name="item"  property="blSampleVO.crystalVO">
						<logic:notEmpty name="item"  property="blSampleVO.crystalVO.proteinVO">
							<bean:write name="item" property="blSampleVO.crystalVO.proteinVO.acronym"/>
						</logic:notEmpty>
					</logic:notEmpty>
				</logic:notEmpty>
				<logic:notEmpty name="item" property="dataCollectionVOs">
					<logic:iterate name="item" property="dataCollectionVOs"  id="dc"  >
						<br>(<bean:write name="dc" property="imagePrefix"/>)
					</logic:iterate>
					</logic:notEmpty>
				</td>
				<%-- Sample name --%>
				<td>
				<logic:notEmpty name="item"  property="blSampleVO">
					<bean:write name="item" property="blSampleVO.name"/>
				</logic:notEmpty>
				</td>
				<%-- Sample position --%>
				<td nowrap>
					<bean:write name="item" property="actualSampleSlotInContainer"/>
					<logic:notEqual name="item" property="actualContainerSlotInSC" value="">
						&nbsp;(<bean:write name="item" property="actualContainerSlotInSC"/>)
					</logic:notEqual>
					<br />
					<bean:write name="item" property="actualSampleBarcode"/>
					<logic:notEqual name="item" property="actualContainerBarcode" value="">
						&nbsp;(<bean:write name="item" property="actualContainerBarcode"/>)
					</logic:notEqual>
				</td>
				<%-- Start Time --%>
				<td>
					<bean:write name="item" property="startTime" format="dd-MM-yyyy HH:mm:ss"/>
				</td>
				<%-- display crystalClass only for IFX proposals (MXPress experiments) --%>
		    	<bean:define id="dataCollectionVOs" name="item" property="dataCollectionVOs" type="java.util.Set"/>
				<logic:equal name="breadCrumbsForm" property="proposalCode" value="<%=Constants.PROPOSAL_CODE_FX%>" >
		    		<td>
		    			<logic:notEmpty name="dataCollectionVOs">
						<html:select property="<%=\"crystalClassList[\" + index + \"]\" %>" styleClass= "FIELD_COMBO" value="<%=item.getCrystalClass() == null ? \"\" : item.getCrystalClass().trim().toUpperCase()%>">
							<OPTION></OPTION>
							<html:optionsCollection name="viewDataCollectionGroupForm" property="listOfCrystalClass"
								label="crystalClassName" value="crystalClassCode" />
						</html:select>
						</logic:notEmpty>
					</td>
		    	</logic:equal>
		    	<%-- Comments --%>
				<td>
					<logic:equal name="viewDataCollectionGroupForm" property="editSkipAndComments" value="true" >
             			<html:textarea styleClass="normalText_black" name="viewDataCollectionGroupForm" property="<%=\"commentsList[\" + index + \"]\" %>" value="<%=item.getComments()%>" cols="50" rows="2" />
             		</logic:equal>
             	</td>	
				<%-- Nb dataCollections --%>
				<td>
					<%=Integer.toString(dataCollectionVOs.size())%>
					<logic:notEmpty name="item" property="dataCollectionVOs">
					<logic:iterate name="item" property="dataCollectionVOs"  id="dc"  >
						<br>(<bean:write name="dc" property="numberOfImages"/>)
					</logic:iterate>
					</logic:notEmpty>
				</td>
				<%-- View dataCollections --%>
				<td>
					<logic:notEmpty name="item"  property="dataCollectionVOs">										
						<html:link href="<%=targetViewDataCollection%>" paramName="item" paramId="dataCollectionGroupId" paramProperty="dataCollectionGroupId" >
							<img src="<%=request.getContextPath()%>/images/magnif_16.png" border=0 onmouseover="return overlib('View');" onmouseout="return nd();">
						</html:link>
					</logic:notEmpty>
				</td>
			</tr>
		</logic:iterate>
	</table>
	<br>
</html:form>

<%-- #### Session summary (User) #### --%>
<logic:equal name="viewDataCollectionGroupForm" property="isUser" value="true" >
	<logic:equal name="breadCrumbsForm" property="proposalCode" value="<%=Constants.PROPOSAL_CODE_FX%>" >
		<logic:present name="viewDataCollectionGroupForm" property="sessionId" >
			<table cellspacing="1" cellpadding="1" border="0" align="center" class="LIST">
				<tr>
					<th class="LIST">Crystal class name</th>
					<th class="LIST">Crystal class code</th>
					<th class="LIST">Number of crystals</th>
					<th class="LIST" width="60%">&nbsp;</th>
				</tr>
				<logic:iterate name="viewDataCollectionGroupForm" property="<%=\"listOfCrystalClass\" %>" id="aCrystalClass" indexId="idCC" type="ispyb.server.mx.vos.collections.IspybCrystalClass3VO">
					<logic:notEqual name="viewDataCollectionGroupForm" property="<%=\"listOfNbCrystalPerClass[\" + idCC + \"]\" %>" value="0">
				 	<tr>
						<td class="LIST"><bean:write name="aCrystalClass" property="crystalClassName" /></td>
						<td class="LIST"><bean:write name="aCrystalClass" property="crystalClassCode" /></td>
						<td class="LIST"><bean:write name="viewDataCollectionGroupForm" property="<%=\"listOfNbCrystalPerClass[\" + idCC + \"]\" %>" /></td>
						<td class="LIST" width="60%">&nbsp;</td>
					 </tr>
					</logic:notEqual>
				</logic:iterate>
			</table>
		<br/>
		</logic:present>
	</logic:equal>
</logic:equal>