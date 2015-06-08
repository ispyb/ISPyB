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

<script type="text/javascript">
		Ext.onReady(run);
		Ext.Loader.setConfig({enabled: true});

		var mainWindow;
		function run() {
			IspybFillShipment.start("fillShipmentPanel");
		}
</script>


<%-- BreadCrumbs bar --%>
<jsp:include page="../../../common/util/breadCrumbsBar.jsp" flush="true">
  		<jsp:param name="isInFillShipment" value="1" />
</jsp:include>


<%-- Errors --%>
<jsp:include page="/tiles/common/messages/error.jsp" flush="true" />
<jsp:include page="/tiles/common/messages/info.jsp" flush="true" />

<div id="fillShipmentPanel"></div>