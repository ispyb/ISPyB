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
<%@ page isELIgnored="false" %>	
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="ispyb.common.util.Constants"%>


<c:if test="${SITE_ATTRIBUTE eq 'MAXIV'}">
	<c:set var="bgcolor" value="#8DC73F"/>
	<c:set var="bgcolor2" value="#fea901"/>
	<c:set var="tab_divider" value="tab_divider1_maxlab.png"/>
</c:if>
<c:if test="${SITE_ATTRIBUTE ne 'MAXIV'}">
	<c:set var="bgcolor" value="#336699"/>
	<c:set var="bgcolor2" value="#6699cc"/>
	<c:set var="tab_divider" value="tab_divider1.gif"/>
</c:if>


<!-- first separator -->
<td vAlign="bottom" align="left" bgColor="${bgcolor}">
	<img height="16" alt="" src="<%=request.getContextPath()%>/images/${tab_divider}" width="2" border="0" />
</td>

<%-- If the user has a menu defined --%>
<logic:present name="<%=Constants.MENU%>" property="menuTop">
	<bean:define id="selectedMenu" name="<%=Constants.MENU%>" property="activeTopMenu"/>
	<logic:iterate id="menuItem" name="<%=Constants.MENU%>" property="menuTop">		
		<c:choose>
			<%-- selected menu --%>
			<c:when test="${menuItem.id == selectedMenu}">
			<!-- begin selected tab -->
				<td align="right" vAlign="top" bgColor="${bgcolor2}"> 
					<IMG height=16 alt="" hspace=0 src="<%=request.getContextPath()%>/images/pixel_clear.gif" width=11 align=left border=0> 
				</td>
				<TD vAlign="top" bgColor="${bgcolor2}" nowrap>      		
		      		<DIV style="VERTICAL-ALIGN: middle; TEXT-ALIGN: center">
		        		<A class="navtab" href="<%=request.getContextPath()%>/menuSelected.do?<%=Constants.TOP_MENU_ID%>=<bean:write name="menuItem" property="id"/>&amp;<%=Constants.TARGET_URL%>=<bean:write name="menuItem" property="url"/>">
		        			<bean:write name="menuItem" property="name"/>
		        		</A>
		        	</DIV>
		        </TD>
		        <td align="right" vAlign="top" bgColor="${bgcolor2}"> 
					<IMG height="16" alt="" hspace="0" src="<%=request.getContextPath()%>/images/pixel_clear.gif" width=11 align=left border=0> 
				</td>
			</c:when>
			<c:otherwise>
				<!-- begin tab -->
				<td align="right" vAlign="top" bgColor="${bgcolor}"> 
					<IMG height=16 alt="" hspace=0 src="<%=request.getContextPath()%>/images/pixel_clear.gif" width=11 align=left border=0> 
				</td>
				<TD vAlign="top" bgColor="${bgcolor}" nowrap>
		        	<DIV style="VERTICAL-ALIGN: middle; TEXT-ALIGN: center">
		        		<A class="navtab" href="<%=request.getContextPath()%>/menuSelected.do?<%=Constants.TOP_MENU_ID%>=<bean:write name="menuItem" property="id"/>&amp;<%=Constants.TARGET_URL%>=<bean:write name="menuItem" property="url"/>"><bean:write name="menuItem" property="name"/>&nbsp;</A>
		        	</DIV>
		        </TD>
		        <td align="right" vAlign="top" bgColor="${bgcolor}"> 
					<IMG height=16 alt="" hspace=0 src="<%=request.getContextPath()%>/images/pixel_clear.gif" width=11 align=left border=0> 
				</td>
			</c:otherwise>
		</c:choose>

	 <!-- tab divider -->
      <TD vAlign="bottom" align="middle" bgColor="${bgcolor}"><IMG height="16" alt="" src="<%=request.getContextPath()%>/images/${tab_divider}" width="2" border="0"></TD>
     

     </logic:iterate>
	 
</logic:present>

<td align="right" width="100%" vAlign="top" bgColor="${bgcolor}"> <img height="16" alt="" hspace="0" src="<%=request.getContextPath()%>/images/pixel_clear.gif" width="11" align="left" border="0" /> </td>


<%-- If the user is not logged in --%>
<logic:notPresent name="<%=Constants.ROLES%>">
<td bgColor="${bgcolor}" vAlign="center" align="right">				
	<a class="navtab" href="<%=request.getContextPath()%>/menuSelected.do?<%=Constants.TOP_MENU_ID%>=-1&amp;<%=Constants.LEFT_MENU_ID%>=-1&amp;<%=Constants.TARGET_URL%>=/security/logon.do"><bean:message key="header.link.logon"/></a>
</td>
</logic:notPresent>

<logic:present name="<%=Constants.ROLES%>">
<td align="right" bgColor="${bgcolor}">
	<a class="navtab" href="<%=request.getContextPath()%>/menuSelected.do?<%=Constants.TOP_MENU_ID%>=-1&amp;<%=Constants.LEFT_MENU_ID%>=-1&amp;<%=Constants.TARGET_URL%>=/logoff.do"><bean:message key="header.link.logoff"/></a>
</td>
</logic:present>
