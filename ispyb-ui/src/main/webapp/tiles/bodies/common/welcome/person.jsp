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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.application-servers.com/layout" prefix="layout"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ include file="/tiles/bodies/biosaxs/project/biosaxs_css_include.jsp"%>

<layout:skin includeScript="true" />
<script
	src="<%=request.getContextPath()%>/js/external/overlib/overlib.js"></script>
<div id="overDiv"
	style="position: absolute; visibility: hidden; z-index: 1000;"></div>

<h1>Welcome</h1>
<h2>Select the proposal you want to work on : </h2>
<script type="text/javascript">
	Ext.onReady(function() {
		$.ajax({
			type : "POST",
			url : "/ispyb/user/userproposalchooseaction.do?reqCode=getProposalList",
			datatype : "text/json",
			success : function(json) {
				new ProposalGrid().show(json, "mainPanel");
			},
			error : function(xhr) {
				console.log(xhr.responseText);
			}
		});

	});
</script>


<layout:skin includeScript="true" />
<script
	src="<%=request.getContextPath()%>/js/external/overlib/overlib.js"></script>
<div id="overDiv"
	style="position: absolute; visibility: hidden; z-index: 1000;"></div>

<div id="mainPanel"></div>
<div id="editor-grid"></div>