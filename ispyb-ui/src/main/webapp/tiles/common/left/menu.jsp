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

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@page import="ispyb.common.util.Constants"%>

<!-- MENU LEFT:  navigation -->
<TABLE cellSpacing=0 cellPadding=2 width=150 border=0>
	<!-- setup row to define the layout of menu-->
    <TBODY>
		<TR>
		  <TD width=10><IMG height=1 alt="" 
		src="<%=request.getContextPath()%>/images/pixel_clear.gif" width=11 border=0></TD>
		  <TD width=10><IMG height=1 alt="" 
		src="<%=request.getContextPath()%>/images/pixel_clear.gif" width=6 border=0></TD>
		  <TD><IMG height=1 alt="" 
		src="<%=request.getContextPath()%>/images/pixel_clear.gif" width=1 border=0></TD>
		</TR>


<logic:present name="<%=Constants.MENU%>" property="menuLeft">
	<bean:define id="selectedMenu" name="<%=Constants.MENU%>" property="activeLeftMenu"/>
	<%-- Iterate first Level List --%>
	<logic:iterate id="menuItem1stLevel" name="<%=Constants.MENU%>" property="menuLeft">
		<c:choose>
			<%-- parent: selected menu --%>
			<c:when test="${menuItem1stLevel.id == selectedMenu}">
				<TR>
              		<TD vAlign=top align=right>
              			<IMG height=9 alt="" src="<%=request.getContextPath()%>/images/navbar_arrow2.gif" width=6 border=0>
              		</TD>
              		<TD vAlign=top align=left colSpan=2>
              			<A class=navbarparentselected
              			href="<%=request.getContextPath()%>/menuSelected.do?<%=Constants.LEFT_MENU_ID%>=<bean:write name="menuItem1stLevel" property="id"/>&amp;<%=Constants.TARGET_URL%>=<bean:write name="menuItem1stLevel" property="url"/>">
              			<bean:write name="menuItem1stLevel" property="name"/></A>
              		</TD>
            	</TR>
			</c:when>
			<%-- parent: menu without URL --%>
			<c:when test="${menuItem1stLevel.url == null}">
				<TR>
              		<TD vAlign=top align=right>
              			<IMG height=9 alt="" src="<%=request.getContextPath()%>/images/navbar_arrow2.gif" width=6 border=0>
              		</TD>
              		<TD vAlign=top align=left colSpan=2>
              			<A class=navbarparent><bean:write name="menuItem1stLevel" property="name"/></A>
              		</TD>
            	</TR>
			</c:when>
			<%-- parent: menu link --%>
			<c:otherwise>
			 	<TR>
              		<TD vAlign=top align=right>
              			<IMG height=9 alt="" src="<%=request.getContextPath()%>/images/navbar_arrow2.gif" width=6 border=0>
              		</TD>
              		<TD vAlign=top align=left colSpan=2>
              			<A class=navbarparent 
              			href="<%=request.getContextPath()%>/menuSelected.do?<%=Constants.LEFT_MENU_ID%>=<bean:write name="menuItem1stLevel" property="id"/>&amp;<%=Constants.TARGET_URL%>=<bean:write name="menuItem1stLevel" property="url"/>"><bean:write name="menuItem1stLevel" property="name"/></A>
              		</TD>
            	</TR>
			</c:otherwise>
		</c:choose>
		<%-- Iterate second Level List --%>
		<logic:iterate id="menuItem2stLevel" name="menuItem1stLevel" property="subMenus">
			<c:choose>
				<%-- child: selected menu --%>
				<c:when test="${menuItem2stLevel.id == selectedMenu}">
					<TR>
              			<TD vAlign=top align=right colSpan=2>
              				<IMG height=9 alt="" src="<%=request.getContextPath()%>/images/navbar_box.gif" width=6 border=0>
              			</TD>
              			<TD vAlign=top align=left>
              				<A class=navbarchildselected
              				href="<%=request.getContextPath()%>/menuSelected.do?<%=Constants.LEFT_MENU_ID%>=<bean:write name="menuItem2stLevel" property="id"/>&amp;<%=Constants.TARGET_URL%>=<bean:write name="menuItem2stLevel" property="url"/>">
              				<bean:write name="menuItem2stLevel" property="name"/></A>
              			</TD>
            		</TR>
				</c:when>
				<%-- child: menu without URL --%>
				<c:when test="${menuItem2stLevel.url == null}">
					<TR>
              			<TD vAlign=top align=right colSpan=2>
              				<IMG height=9 alt="" src="<%=request.getContextPath()%>/images/navbar_box.gif" width=6 border=0>
              			</TD>
              			<TD vAlign=top align=left>
              				<A class=navbarchild><bean:write name="menuItem2stLevel" property="name"/></A>
              			</TD>
            		</TR>
				</c:when>
				<%-- child: menu link --%>
				<c:otherwise>
				 	<TR>
              			<TD vAlign=top align=right colSpan=2>
              				<IMG height=9 alt="" src="<%=request.getContextPath()%>/images/navbar_box.gif" width=6 border=0>
              			</TD>
              			<TD vAlign=top align=left>
              				<A class=navbarchild
              				href="<%=request.getContextPath()%>/menuSelected.do?<%=Constants.LEFT_MENU_ID%>=<bean:write name="menuItem2stLevel" property="id"/>&amp;<%=Constants.TARGET_URL%>=<bean:write name="menuItem2stLevel" property="url"/>"><bean:write name="menuItem2stLevel" property="name"/></A>
              			</TD>
            		</TR>
				</c:otherwise>
			</c:choose>
		</logic:iterate>	 
	</logic:iterate>
</logic:present>


  </TBODY>
</TABLE>
