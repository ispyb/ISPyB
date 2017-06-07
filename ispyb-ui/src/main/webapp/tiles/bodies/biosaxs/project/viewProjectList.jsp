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

<%@ include file="biosaxs_css_include.jsp" %> 		


<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/bubble_tooltip.css" />
<layout:skin includeScript="true" />
<script src="<%=request.getContextPath()%>/js/external/overlib/overlib.js"></script>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>


<script type="text/javascript">
		Ext.onReady(function(){
			// Loading site favours 
			try{
				ISPYB_CONF.load(SITE_CONF);
			}
			catch(e){
				console.log("Error loading flavours");
				console.log(e);
			}
			BIOSAXS.start("mainPanel");
		});
		
		
</script>


<layout:skin includeScript="true" />
	<script src="<%=request.getContextPath()%>/js/external/overlib/overlib.js"></script>
	<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>

<div id="mainPanel"></div>
<div id="editor-grid"></div>


