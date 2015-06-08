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


<logic:notEqual name="getAutoProcListForm" property="nbAutoProc" value="0">
	
	<bean:define id="dataCollectionIdTest" name="getAutoProcListForm" property="dataCollectionId" />
	<bean:define id="rmerge" name="getAutoProcListForm" property="rmerge" />
	<bean:define id="isigma" name="getAutoProcListForm" property="isigma" />
	<%
		
		String targetResults = request.getContextPath() + "/user/viewResults.do?reqCode=display"+
			"&dataCollectionId=" + dataCollectionIdTest + 
			"&rmerge=" + rmerge + 
			"&isigma=" + isigma;
		
	%>
	<input id="dataCollectionIdText" value="<%= dataCollectionIdTest %>" type="hidden"/>	
	<bean:define name="getAutoProcListForm" 	scope="request" id="getAutoProcListForm" 	type="ispyb.client.mx.results.GetAutoProcListForm"/>	
	
	<layout:radios layoutId="anomalous" key="View autoprocessings:"   name="getAutoProcListForm"  property="anomalous" styleClass="FIELD" > 
		
		<html:radio name="getAutoProcListForm"  property="anomalous"  value="true" styleClass="FIELD"  onclick="<%= \"return setAnomalous('\"+targetResults+\"', 'true')\"%>">
			<html:link href="<%=targetResults +  \"&anomalous=true\" %>" style="text-decoration:none;color: #6888a8;font-size: x-small" > 
				Anomalous
		</html:link></html:radio>
		
		
		<html:radio name="getAutoProcListForm"  property="anomalous" value="false" styleClass="FIELD" onclick="<%= \"return setAnomalous('\"+targetResults+\"', 'false')\"%>">
			<html:link href="<%=targetResults +  \"&anomalous=false\" %>" style="text-decoration:none;color: #6888a8;font-size: x-small"> 
				No anomalous
		</html:link></html:radio>
			
	</layout:radios>
	<BR>
</logic:notEqual>

<logic:notEmpty name="getAutoProcListForm" property="autoProcs">
	<layout:select layoutId="autoProcSelect" key="AutoProcessings:" styleClass="SELECT" name="getAutoProcListForm" property="selectedAutoProcLabel">
		<layout:options name="getAutoProcListForm" property="autoProcs" labelName="getAutoProcListForm" labelProperty="autoProcLabels" />
	</layout:select>
	<html:select styleId="autoProcSelectIds" style="visibility: hidden; height: 0px;" name="getAutoProcListForm" property="selectedAutoProc">
		<html:options name="getAutoProcListForm" property="autoProcs" labelName="getAutoProcListForm" labelProperty="autoProcIdLabels" />
	</html:select>
</logic:notEmpty>

<logic:empty name="getAutoProcListForm" property="autoProcs">
	<p class="FIELD_BIG">No autoprocessings to display</p>
</logic:empty>
