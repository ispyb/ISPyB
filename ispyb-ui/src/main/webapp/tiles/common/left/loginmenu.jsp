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
<%@ page isELIgnored="false" %>	
<%@page import="ispyb.common.util.Constants"%>

<%
	String latestISPyBNews = "Latest ISPyB News";
    boolean showReleaseNotes = true;
	if (Constants.SITE_IS_MAXIV()){
		latestISPyBNews += " (@esrf)";
		showReleaseNotes = false;
	}
%>

<!-- MENU LEFT:  navigation -->
<TABLE cellSpacing=0 cellPadding=2 width=190 border=0>
	<!-- setup row to define the layout of menu-->
    <TBODY>
		<TR>
		  <TD width=10><IMG height=1 alt="" src="<%=request.getContextPath()%>/images/pixel_clear.gif" width=11 border=0></TD>
		  <TD width=10><IMG height=1 alt="" src="<%=request.getContextPath()%>/images/pixel_clear.gif" width=6 border=0></TD>
		  <TD><IMG height=1 alt="" src="<%=request.getContextPath()%>/images/pixel_clear.gif" width=1 border=0></TD>
		</TR>

		<TR>
      		<TD vAlign=top align=right><IMG height=9 alt="" src="<%=request.getContextPath()%>/images/navbar_arrow2.gif" width=6 border=0></TD>
   			<TD vAlign=top align=left colSpan=2>
   				<A class=navbarparentselected href="<%=request.getContextPath()%>/overviewPage.do">
   					ISPyB Overview
   				</A>
   			</TD>
     	</TR>
     	
     	<TR>
      		<TD vAlign=top align=right><IMG height=9 alt="" src="<%=request.getContextPath()%>/images/navbar_arrow2.gif" width=6 border=0></TD>
   			<TD vAlign=top align=left colSpan=2>
   				<A class=navbarparentselected href="<%=Constants.getProperty("ISPyB.news.url")%>">
   					<%=latestISPyBNews%>
   				</A>
   			</TD>
     	</TR>
		<c:if test="<%=showReleaseNotes%>">
	     	<TR>
	      		<TD vAlign=top align=right><IMG height=9 alt="" src="<%=request.getContextPath()%>/images/navbar_arrow2.gif" width=6 border=0></TD>
	   			<TD vAlign=top align=left colSpan=2>
	   				<A class=navbarparentselected href="<%=request.getContextPath()%>/help/release_notes.txt">
	   					Release notes
	   				</A>
	   			</TD>
	     	</TR>
	     </c:if>
     	 <TR>
      		<TD vAlign=top align=right><IMG height=9 alt="" src="<%=request.getContextPath()%>/images/navbar_arrow2.gif" width=6 border=0></TD>
   			<TD vAlign=top align=left colSpan=2>
   				<A class=navbarparentselected href="<%=request.getContextPath()%>/viewReference.do?reqCode=display">
   					References
   				</A>
   			</TD>
     	</TR>
     	<TR>
      		<TD vAlign=top align=right><IMG height=9 alt="" src="<%=request.getContextPath()%>/images/navbar_arrow2.gif" width=6 border=0></TD>
   			<TD vAlign=top align=left colSpan=2>
   				<A class=navbarparentselected href="http://www.mozilla.org/products/firefox/">
   					Get Firefox
   				</A>
   			</TD>
     	</TR>
     	<TR>
      		<TD vAlign=top align=right><IMG height=9 alt="" src="<%=request.getContextPath()%>/images/navbar_arrow2.gif" width=6 border=0></TD>
   			<TD vAlign=top align=left colSpan=2>
   				<A class=navbarparentselected href="mailto:<%= Constants.MAIL_TO_SITE %>">
   					Need help
   				</A>
   			</TD>
     	</TR>    
     	<c:if test="${SITE_ATTRIBUTE == 'EMBL'}">
		    <TR>
   				<TD vAlign=top align=right><IMG height=9 alt="" src="<%=request.getContextPath()%>/images/navbar_arrow2.gif" width=6 border=0></TD>
				<TD vAlign=top align=left colSpan=2>
					<A class=navbarparentselected href="http://photon-science.desy.de/e58/e176720/e179421/popup">Petra III status</A>
   				</TD>
     		</TR>
     	</c:if>	
	</TBODY>
</TABLE>
