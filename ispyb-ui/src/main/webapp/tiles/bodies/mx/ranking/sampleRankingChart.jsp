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

<%@page import="ispyb.common.util.Constants"%>

<layout:skin includeScript="true" />

<script language="javascript">AC_FL_RunContent = 0;</script>
<script language="javascript"> DetectFlashVer = 0; </script>
<script src="<%=request.getContextPath()%>/config/flashcharts/AC_RunActiveContent.js" language="javascript"></script>
<script language="JavaScript" type="text/javascript">
<!--
var requiredMajorVersion = 9;
var requiredMinorVersion = 0;
var requiredRevision = 45;
-->
</script>

<%-- BreadCrumbs bar --%>
<jsp:include page="/tiles/common/util/breadCrumbsBar.jsp" flush="true" />

<%-- To display messages --%>
<jsp:include page="/tiles/common/messages/info.jsp" flush="true" />

<CENTER><H2><layout:write name="sampleRankingForm" property="chartTitle"/></H2></CENTER>

<TABLE border="1" cellspacing="0" cellpadding="0" bordercolor=000000 align="center">
<TR><TD align=center>

<script language="JavaScript" type="text/javascript">
<!--
if (AC_FL_RunContent == 0 || DetectFlashVer == 0) {
	alert("This page requires AC_RunActiveContent.js.");
} else {
	var hasRightVersion = DetectFlashVer(requiredMajorVersion, requiredMinorVersion, requiredRevision);
	if(hasRightVersion) {  // if we've detected an acceptable version
		// embed the flash movie
		AC_FL_RunContent(
			'codebase', 'http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=9,0,45,0',
			'width', '800',
			'height', '400',
			'bgcolor', '#EEEEEE',
			'movie', '<%=request.getContextPath()%>/config/flashcharts/charts',
			'src', '<%=request.getContextPath()%>/config/flashcharts/charts',
			'FlashVars', 'library_path=<%=request.getContextPath()%>/config/flashcharts/charts_library&xml_source=<layout:write name="sampleRankingForm" property="reportDataUrl"/>', 
			'wmode', 'opaque',
			'scale', 'noScale',
			'id', 'charts',
			'name', 'charts',
			'menu', 'true',
			'allowFullScreen', 'true',
			'allowScriptAccess','sameDomain',
			'quality', 'high',
			'pluginspage', 'http://www.macromedia.com/go/getflashplayer',
			'align', 'middle',
			'play', 'true',
			'devicefont', 'false',
			'salign', 'TL'
			); //end AC code
	} else {  // flash is too old or we can't detect the plugin
		var alternateContent = 'This content requires the Adobe Flash Player. '
			+ '<u><a href=http://www.macromedia.com/go/getflash/>Get Flash</a></u>.';
		document.write(alternateContent);  // insert non-flash content
	}
}
// -->
</script>
<noscript>
	<P>This content requires JavaScript.</P>
</noscript>


</TD></TR></TABLE>
