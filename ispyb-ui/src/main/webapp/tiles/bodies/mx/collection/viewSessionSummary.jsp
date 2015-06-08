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

<%@ include file="../../mx/mx_css_include.jsp" %> 
<%@ include file="../../mx/mx_javascript_include.jsp" %>

<%-- ###################################################### --%>
<%--                   BreadCrumbs bar                      --%>
<%-- ###################################################### --%>

<jsp:include page="/tiles/common/util/breadCrumbsBar.jsp" flush="true" />

<%-- ###################################################### --%>
<%--                 Session and information                --%>
<%-- ###################################################### --%>

<html:form action="/user/viewSessionSummary">
		<bean:define name="viewSessionSummaryForm" 	scope="request" id="viewSessionSummaryForm" 	type="ispyb.client.mx.collection.ViewSessionSummaryForm"/>			
		<input id="sessionId" value="<%= viewSessionSummaryForm.getSessionId() %>" type="hidden"/>	
		<input id="nbOfItems" value="<%= viewSessionSummaryForm.getNbOfItems() %>" type="hidden"/>	
	</html:form>
		
<script type="text/javascript">
		Ext.onReady(run);
		Ext.Loader.setConfig({enabled: true});

		var mainWindow;
		function run() {
			var  sessionId = document.getElementById('sessionId').value;
			var  nbOfItems = document.getElementById('nbOfItems').value;
			IspybSessionSummary.start("sessionSummaryPanel", sessionId, nbOfItems);
		}
</script>

<div id="sessionSummaryPanel"></div>