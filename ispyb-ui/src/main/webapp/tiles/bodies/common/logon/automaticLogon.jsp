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

<layout:skin />
<SCRIPT LANGUAGE="JavaScript">
<!--
function getParameter ( queryString, parameterName ) {
   // Add "=" to the parameter name (i.e. parameterName=value)
   var parameterName = parameterName + "=";
   if ( queryString.length > 0 ) {
      // Find the beginning of the string
      begin = queryString.indexOf ( parameterName );
      // If the parameter name is not found, skip it, otherwise return the value
      if ( begin != -1 ) {
         // Add the length (integer) to the beginning
         begin += parameterName.length;
         // Multiple parameters are separated by the "&" sign
         end = queryString.indexOf ( "&" , begin );
      if ( end == -1 ) {
         end = queryString.length
      }
      // Return the string
      return unescape ( queryString.substring ( begin, end ) );
   }
   // Return "null" if no parameter has been found
   return "null";
   }
}
//-->
</SCRIPT>

<layout:panel key="Login" align="center" styleClass="PANEL">
	<form action="j_security_check" method="post" name="login">
    	<layout:grid cols="1" styleClass="SEARCH_GRID"  borderSpacing="10">	
			<layout:column>
				<layout:text key="label.login" property="j_username" styleClass="FIELD" mode="E,E,E" value=""/>
				<layout:password key="label.password" 	property="j_password" 	styleClass="FIELD" 	mode="E,E,E" value="" redisplay="true"/>				
			</layout:column>
			<layout:row>
				<layout:submit><layout:message key="button.login"/></layout:submit>
				<layout:reset />
			</layout:row>
		</layout:grid>
		<SCRIPT LANGUAGE="JavaScript">
		<!--
		document.login.j_username.focus();
		var queryString = window.top.location.search.substring(1);
		
		var username = getParameter(queryString, 'login');
		var password = getParameter(queryString, 'password');
		
		document.login.j_username.value = username
		document.login.j_password.value = password
		document.login.submit();
		//-->
		</SCRIPT>
	</form>
</layout:panel>


<center><h1>ISPYB Automatic Logon page</h1></center>
<p align="right"><a href="<%=request.getContextPath()%>/help/release_notes.txt">View release notes</a></p>
<p>
If you can see this page, then there's a problem ! You should have been redirected. Click the "Login" button ...
</p>


<DIV align=center>
<a href="http://www.esrf.fr"><img src="<%=request.getContextPath()%>/images/logo-esrf.gif" border=0></a>
<a href="http://www.spineurope.org"><img src="<%=request.getContextPath()%>/images/spine_icon.gif" border=0></a>
<a href="http://www.e-htpx.ac.uk"><img src="<%=request.getContextPath()%>/images/eHTPX_logo.gif" border=0></a>
<a href="http://www.bm14.ac.uk"><img src="<%=request.getContextPath()%>/images/bm14-logo.gif" border=0></a>
<a href="http://www.synchrotron-soleil.fr"><img src="<%=request.getContextPath()%>/images/soleil-logo.gif" border=0></a>
</DIV>
