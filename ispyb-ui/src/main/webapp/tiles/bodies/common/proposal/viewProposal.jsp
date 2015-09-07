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
<%@ page isELIgnored="false" %>	

<script type='text/javascript' src="<%=request.getContextPath()%>/js/external/toggleDiv.js"></script>
<script src="<%=request.getContextPath()%>/js/external/overlib/overlib.js"></script>

<layout:skin includeScript="true" />

<script type='text/javascript'>

function onLoad(){
	var code = document.getElementsByName('code')[0];
	var number = document.getElementsByName('number')[0];
	var title = document.getElementsByName('title')[0];
	var login = document.getElementsByName('login')[0];
	var searchDiv = document.getElementById('searchDiv');
	
	if(code.value == '' && number.value == ''  && title.value == ''){
		searchDiv.style.display = 'none';
	}
}


</script>

<%-- the action is defined according to user role  --%>

<% 
	String target = request.getContextPath() + "/user/viewSession.do?reqCode=display";
	String targetViewAllSessions    = request.getContextPath() + "/menuSelected.do?leftMenuId=17&targetUrl=/user/viewSession.do?reqCode=display";
%>

<a class="noUnderline" onmouseover="return overlib('Expand/collapse Search Form');" onmouseout="return nd();" href="javascript:;" onclick="toggleDiv('searchDiv');">
  <p class="toggleText">&nbsp;&nbsp;Search Proposals&nbsp;<img src="<%=request.getContextPath()%>/images/doubleArrowDesc.png" border=0></p>
</a>
<div id="searchDiv">
	<layout:grid cols="1" borderSpacing="10">
	
		<%-- Search Form --%>
		<layout:column >
			<layout:panel key="Search Proposals" align="left" styleClass="PANEL">
			<layout:form action="/manager/viewProposal.do" reqCode="display">
				
				<layout:grid cols="1" styleClass="SEARCH_GRID">	
					<layout:column>
						<layout:text key="Code" 	property="code" 	styleClass="FIELD" 	mode="E,E,E"/>
						<layout:text key="Number" 	property="number" 	styleClass="FIELD" 	mode="E,E,E"/>
						<layout:text key="Title" 	property="title" 	styleClass="FIELD" 	mode="E,E,E"/>
						<layout:text key="reqCode"	property="reqCode"  mode="H,H,H" 		value="displayAll" />
					</layout:column>  
					<layout:space/>
					<layout:row styleClass="rowAlignCenter">
						<layout:submit><layout:message key="Search"/></layout:submit>		
						<layout:reset />	
					</layout:row>
		
				</layout:grid>
			</layout:form>
			
			</layout:panel>
		</layout:column>
	
	</layout:grid>
</div>

<logic:notEmpty name="viewProposalForm" property="listInfo">
<layout:grid cols="1"  borderSpacing="10">

	<layout:column >
	<layout:panel key="Proposals" align="center" styleClass="PANEL">
	
	<layout:collection 	name="viewProposalForm" property="listInfo" id="currInfo" styleClass="LIST">

			<layout:collectionItem title="Code" 	property="code" sortable="false"/>
			<layout:collectionItem title="Number" 	property="number" sortable="true"/>
			<layout:collectionItem title="Title" 	property="title" sortable="false" 
							        href="<%=target%>"
			                        paramId="proposalId" paramProperty="proposalId"/>
			                        
            <c:if test="${SITE_ATTRIBUTE eq 'EMBL'}">
				<layout:collectionItem title="User"     property="login" sortable="true"/>                                                      
            </c:if>
			                        
	</layout:collection>
	</layout:panel>
	</layout:column>
</layout:grid>
</logic:notEmpty>


