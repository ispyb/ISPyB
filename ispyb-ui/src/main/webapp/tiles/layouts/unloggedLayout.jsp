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
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ page isELIgnored="false" %>	
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="ispyb.common.util.Constants"%>

<TITLE><tiles:getAsString name="title"/></TITLE>
<META http-equiv=Content-Type content="text/html; charset=iso-8859-1">
<LINK href="<%=request.getContextPath()%>/css/styles.css" type=text/css rel=STYLESHEET />
<LINK REL="SHORTCUT ICON" HREF="<%=request.getContextPath()%>/images/ispyb_icon.ico">
</HEAD>
<BODY bgColor="#ffffff" marginwidth="0" marginheight="0" text="#000000">
<TABLE cellSpacing="0" cellPadding="0" width="100%" border="0">
  <TBODY>
    <!-- first bar:  Logon / Logoff   |   Documentation   -->
    <tiles:insert attribute="header_top_bar" />
    <!-- bar with image -->
    <tiles:insert attribute="header_logo_bar" />
	<!-- black line over the navigation bar -->
    <TR bgColor="#000000">
      <TD colspan=2></TD>
    </TR>
    <!-- context bar -->
    <tiles:insert attribute="header_context_bar" />
    <!-- black line over the menu -->
    <TR bgColor="#000000">
      <TD colspan=2></TD>
    </TR>
  </TBODY>
</TABLE>
<!-- ************************* begin header tabs ****************************** -->
<TABLE cellSpacing="0" cellPadding="0" width="100%" border="0">
  <TBODY>
    <TR>
	  <tiles:insert attribute="header_tab_bar" />
    </TR>
  </TBODY>
</TABLE>
<TABLE cellSpacing="0" cellPadding="0" width="100%" border="0">
    <TR>
	  <c:if test="${SITE_ATTRIBUTE ne 'MAXIV'}">
      <TD vAlign="bottom" align="middle" bgColor="#336699"></TD>
      </c:if>
	  <c:if test="${SITE_ATTRIBUTE eq 'MAXIV'}">
      <TD vAlign="bottom" align="middle" bgColor="#8DC73F"></TD>
      </c:if>

    </TR>
  </TBODY>
</TABLE>

<c:if test="${SITE_ATTRIBUTE eq 'ESRF' or SITE_ATTRIBUTE eq 'ALBA'}">
	<!-- ****************** begin Body of the page: menu left + content + footer *********************** -->
	<TABLE cellSpacing="0" cellPadding="0" width="100%" border="0">
	  <TBODY>
	    <TR>
	      <!-- MENU LEFT (navigation + adicional links)-->
	      <TD class=navbartable vAlign=top width=150>
	      	<!-- MENU LEFT:  navigation -->
	      	<tiles:insert attribute="left_menu" />
	        <!-- end of Nav -->
	 
	        </TD>
	      <TD><IMG height=1 alt="" src="<%=request.getContextPath()%>/images/pixel_clear.gif" width=10 border=0></TD>
			<TD class=margintop vAlign=top>
		      	<!-- ******** BEGIN CONTENT AREA ******** -->
		        
		        <tiles:insert attribute="body" />
		
		        <!-- ******** END CONTENT AREA ******** -->
			</TD>
	    </TR>
	    <!-- Footer -->
	  </TBODY>
	</TABLE>
</c:if>

<c:if test="${SITE_ATTRIBUTE eq 'DLS'}">
	<!-- ****************** begin Body of the page: menu left + content + footer *********************** -->
	<TABLE cellSpacing="0" cellPadding="0" width="100%" border="0">
	  <TBODY>
	    <TR>
			<TD class="margintop" vAlign="top">
		      	<!-- ******** BEGIN CONTENT AREA ******** -->
		        
		        <tiles:insert attribute="body" />
		
		        <!-- ******** END CONTENT AREA ******** -->
			</TD>
	    </TR>
	    <!-- Footer -->
	    <TR>
	    	<TD>
				<IMG height="10" alt="" src="<%=request.getContextPath()%>/images/pixel_clear.gif" width="650" border="0" />
				<BR>
	      		<tiles:insert attribute="footer" />
				<BR clear="all" />
				<IMG height=10 alt="" src="<%=request.getContextPath()%>/images/pixel_clear.gif" width="1" border="0" /> 
			</TD>
	    </TR>
	  </TBODY>
	</TABLE>
</c:if>

<c:if test="${SITE_ATTRIBUTE eq 'EMBL'}">
	<!-- ****************** begin Body of the page: menu left + content + footer *********************** -->
	<TABLE cellSpacing="0" cellPadding="0" width="100%" border="0">
	  <TBODY>
	    <TR>
	      <!-- MENU LEFT (navigation + adicional links)-->
	      <TD class=navbartable vAlign=top width=150>
	      	<!-- MENU LEFT:  navigation -->
	      	<tiles:insert attribute="left_menu" />
	        <!-- end of Nav -->
	 
	        </TD>
	      <TD><IMG height=1 alt="" src="<%=request.getContextPath()%>/images/pixel_clear.gif" width=10 border=0></TD>
			<TD class=margintop vAlign=top>
		      	<!-- ******** BEGIN CONTENT AREA ******** -->
		        
		        <tiles:insert attribute="body" />
		
		        <!-- ******** END CONTENT AREA ******** -->
			</TD>
	    </TR>
	    <!-- Footer -->
	  </TBODY>
	</TABLE>
</c:if>

<c:if test="${SITE_ATTRIBUTE eq 'MAXIV'}">
	<!-- ****************** begin Body of the page: menu left + content + footer *********************** -->
	<TABLE cellSpacing="0" cellPadding="0" width="100%" border="0">
	  <TBODY>
	    <TR>
	      <!-- MENU LEFT (navigation + adicional links)-->
	      <TD class=navbartable vAlign=top width=150>
	      	<!-- MENU LEFT:  navigation -->
	      	<tiles:insert attribute="left_menu" />
	        <!-- end of Nav -->
	 
	        </TD>
	      <TD><IMG height=1 alt="" src="<%=request.getContextPath()%>/images/pixel_clear.gif" width=10 border=0></TD>
			<TD class=margintop vAlign=top>
		      	<!-- ******** BEGIN CONTENT AREA ******** -->
		        
		        <tiles:insert attribute="body" />
		
		        <!-- ******** END CONTENT AREA ******** -->
			</TD>
	    </TR>
	    <!-- Footer -->
	  </TBODY>
	</TABLE>
</c:if>

<c:if test="${SITE_ATTRIBUTE eq 'SOLEIL'}">
	<!-- ****************** begin Body of the page: menu left + content + footer *********************** -->
	<TABLE cellSpacing="0" cellPadding="0" width="100%" border="0">
	  <TBODY>
	    <TR>
	      <!-- MENU LEFT (navigation + adicional links)-->
	      <TD class=navbartable vAlign=top width=150>
	      	<!-- MENU LEFT:  navigation -->
	      	<tiles:insert attribute="left_menu" />
	        <!-- end of Nav -->
	 
	        </TD>
	      <TD><IMG height=1 alt="" src="<%=request.getContextPath()%>/images/pixel_clear.gif" width=10 border=0></TD>
			<TD class=margintop vAlign=top>
		      	<!-- ******** BEGIN CONTENT AREA ******** -->
		        
		        <tiles:insert attribute="body" />
		
		        <!-- ******** END CONTENT AREA ******** -->
			</TD>
	    </TR>
	    <!-- Footer -->
	  </TBODY>
	</TABLE>
</c:if>

<c:if test="${SITE_ATTRIBUTE eq 'GENERIC'}">
	<!-- ****************** begin Body of the page: menu left + content + footer *********************** -->
	<TABLE cellSpacing="0" cellPadding="0" width="100%" border="0">
	  <TBODY>
	    <TR>
	      <!-- MENU LEFT (navigation + additional links) -->
	      <TD class=navbartable vAlign=top width=150>
	      <!-- MENU LEFT: navigation -->
	        <tiles:insert attribute="left_menu" />
	      <!-- end of Nav -->
	      </TD>
	      <TD><IMG height=1 alt="" src="<%=request.getContextPath()%>/images/pixel_clear.gif" width=10 border=0></TD>
	      <TD class=margintop vAlign=top>
	        <!-- ******** BEGIN CONTENT AREA ******** -->
	        <tiles:insert attribute="body" />
	        <!-- ******** END CONTENT AREA ******** -->
	      </TD>
	    </TR>
	    <!-- Footer -->
	  </TBODY>
	</TABLE>
</c:if>

</BODY>
</HTML>
