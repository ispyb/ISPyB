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



<h4>
	<logic:notEmpty name="viewResultsForm" property="interruptedAutoProcEvents">
	<h4>Interrupted autoProc:</h4>
	<logic:iterate name="viewResultsForm" property="interruptedAutoProcEvents" id="item" indexId="index" >
		<logic:notEmpty name="viewResultsForm" property="<%=\"interruptedAutoProcEvents[\" + index + \"]\" %>" >
		<layout:collection 	name="viewResultsForm" property="<%=\"interruptedAutoProcEvents[\" + index + \"]\" %>" 
				id="autoProcEvent" styleClass="LIST" styleClass2="LIST2">
										
			<layout:collectionItem title="Date" sortable="false">
				<bean:write name="autoProcEvent" property="blTimeStamp" format="dd-MM-yyyy HH:mm"/>
			</layout:collectionItem>
			<layout:collectionItem title="Step"   property="step" sortable="false"/>
			<layout:collectionItem title="Status" property="status" sortable="false"/>
			<layout:collectionItem title="Comments" property="comments" sortable="false"/>
		</layout:collection>
		<br/>
		</logic:notEmpty>
		</logic:iterate>
	</logic:notEmpty>
</h4>












