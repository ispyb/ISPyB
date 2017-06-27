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
<%--                   BreadCrumbs bar                                                               --%>
<%-- ###################################################### --%>

<jsp:include page="/tiles/common/util/breadCrumbsBar.jsp" flush="true" />

<%-- ###################################################### --%>
<%--                 Session and information                                                      --%>
<%-- ###################################################### --%>
	
<html:form action="/user/viewDataCollection">
		<bean:define name="viewDataCollectionForm" 	scope="request" id="viewDataCollectionForm" 	type="ispyb.client.mx.collection.ViewDataCollectionForm"/>			
		<input id="mode" value="<%= viewDataCollectionForm.getMode() %>" type="hidden"/>	
	</html:form>	
<script type="text/javascript">
		Ext.onReady(run);
		Ext.Loader.setConfig({enabled: true}); 

		var mainWindow;
		function run() {
			var  mode = document.getElementById('mode').value;
			IspybCollection.start("collectionPanel", mode);
		}
</script>

<div id="collectionPanel"></div>
