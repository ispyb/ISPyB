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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
--------------------------------------------------------------------------------------------------%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<!DOCTYPE HTML>
<HTML>
<HEAD>
<TITLE><tiles:getAsString name="title"/></TITLE>
<META http-equiv=Content-Type content="text/html; charset=iso-8859-1">
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7; IE=EmulateIE9;">

<!-- main stylesheet -->
<LINK href="<%=request.getContextPath()%>/css/styles.css" type=text/css rel=STYLESHEET />
<!-- other complementary stylesheet -->
<LINK href="<%=request.getContextPath()%>/css/<tiles:getAsString name="selectedStyles"/>" type=text/css rel=STYLESHEET />
<LINK REL="SHORTCUT ICON" HREF="<%=request.getContextPath()%>/images/ispyb_icon.ico">
</HEAD>

<BODY bgColor="#ffffff" marginwidth="0" marginheight="0" text="#000000">

<!-- ****************** begin Body of the page: menu left + content + footer *********************** -->
<TABLE cellSpacing="0" cellPadding="0" width="100%" border="0">
  <TBODY>
    <TR>
      <!-- MENU LEFT (navigation + adicional links)-->
      
      
      <!-- The Body will start here! -->
      <TD class="margintop" vAlign="top">
      	<!-- ******** BEGIN CONTENT AREA ******** -->
        
        <tiles:insert attribute="body" />

        <!-- ******** END CONTENT AREA ******** --></TD>
    </TR>
</TBODY>
</TABLE>
</BODY>
</HTML>