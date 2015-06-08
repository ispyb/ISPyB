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


<logic:equal name="viewDataCollectionForm" property="archived" value="<%=Constants.PYARCH_ARCHIVED%>" >
	<table>
		<tr><td class="FIELD">
			<font color="orange">Data have been archived. Snapshots and some files are not available for this session. </font>
		</td></tr>
		<tr><td class="FIELD">
			<font color="orange">You can restore them. This may take time.</font>
		</td></tr>
		<tr><td>
			<layout:form action="/user/viewDataCollection" reqCode="restoreArchivedData">
				<layout:submit onclick="return window.confirm('Do you really want to restore data?');" onmouseover="return overlib('Restore snapshots and files');" onmouseout="return nd();">
					<layout:message key="Restore"/>
				</layout:submit>
			</layout:form>
		</td></tr>
	</table>
</logic:equal>
<logic:equal name="viewDataCollectionForm" property="archived" value="<%=Constants.PYARCH_ARCHIVED_INPROGRESS%>" >
	<table>
		<tr><td class="FIELD">
			<font color="orange">Data have been archived and restoration is in progress.</font>
		</td></tr>
	</table>
	<div id="panelWait">
	<layout:row>
		<layout:panel key="Action in progress. Please wait..." align="left" styleClass="PANEL">
			<img src="<%=request.getContextPath()%>/images/progress_bar.gif" border=0>
		</layout:panel>
	</layout:row>
</div> 
</logic:equal>