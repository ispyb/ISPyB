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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="ispyb.common.util.Constants"%>

<%@ page isELIgnored="false" %>	

<jsp:useBean id="adminVar" class="ispyb.server.common.util.AdminUtils" scope="page" />

<layout:skin includeScript="true" />
<br/>

<bean:define scope="page" id="serverName" value="<%=request.getServerName()%>" 	type="java.lang.String"/>	



<!-- LOGIN FORM -->
<TABLE cellSpacing=0 cellPadding=0 width=700px border=0>
  <TR>
    
    <!-- LOGON FORM -->
    
    <TD valign=top>
      <layout:panel key="Login" align="center" styleClass="PANEL_LOGON" width="490px">
      	<form action="j_security_check" method="post" name="login">
      		  
      		  <layout:grid cols="1" styleClass="SEARCH_GRID"  borderSpacing="10">	
      			<layout:column>
      			   <layout:grid cols="1" styleClass="SEARCH_GRID"  borderSpacing="0">	
      			   
      			   	  <!-- Login -->
	          		  <layout:column styleClass="MESSAGE_GREY">
	          		    <bean:message key="label.login"/>
	          		  </layout:column>
	          		  <layout:column styleClass="FIELD">
	          		    <input name="j_username" value="" class="FIELD" type="text">
	          		  </layout:column>
	          		  <layout:column styleClass="FIELD">
	          		    &nbsp;
	          		  </layout:column>
	          		  
	          		  <!-- Password -->
	          		  <layout:column styleClass="MESSAGE_GREY">
<c:choose>
<c:when test="${SITE_ATTRIBUTE eq 'ESRF'|| SITE_ATTRIBUTE eq 'MAXIV'|| SITE_ATTRIBUTE eq 'SOLEIL' || SITE_ATTRIBUTE eq 'ALBA'}">
	          		    <bean:message key="label.password"/>&nbsp;(*)
</c:when>
<c:otherwise>
	          		    <bean:message key="label.password"/>
</c:otherwise>
</c:choose>
	          		  </layout:column>
	          		  <layout:column styleClass="FIELD">
	          		    <input name="j_password" value="" class="FIELD" type="password">
	          		  </layout:column>
	          		  
	          		  <layout:column styleClass="FIELD">&nbsp;</layout:column>
	          		  
	          		  <!-- Password warning message-->

<c:if test="${SITE_ATTRIBUTE eq 'ESRF'}">
	          		  <layout:column styleClass="MESSAGE_GREY">
	          		    <div align=justify>If you are a ESRF User, your login is the beamline proposal login.<br>
	          		    (*): Your beamline proposal password is now randomly generated. You can retrieve this password from your A-form, login to the <a href="https://wwws.esrf.fr/misapps/SMISWebClient/"><font color="#6888a8">ESRF User Portal webclient here</font></a>.</div>
	                  </layout:column>
</c:if>
<c:if test="${SITE_ATTRIBUTE eq 'MAXIV'}">
	          		  <layout:column styleClass="MESSAGE_GREY">
	          		    <div align=justify>(*): Your DUO password.
	          		    </div>
	                  </layout:column>
</c:if>	              	                
<c:if test="${SITE_ATTRIBUTE eq 'SOLEIL'}">
	          		  <layout:column styleClass="MESSAGE_GREY">
	          		    <div align=justify>(*): Your SUN set password.</div>
	                  </layout:column>
</c:if>	
                </layout:grid>
              
            </layout:column>
      			<layout:row styleClass="rowAlignCenter">
      				<layout:submit><layout:message key="button.login"/></layout:submit>
      				<layout:reset />
      			</layout:row>
      		  </layout:grid>  
      		  
      		<SCRIPT LANGUAGE="JavaScript">
      		  document.login.j_username.focus();
      		</SCRIPT>
      		
      	</form>
      </layout:panel>
    </TD>

    <TD>&nbsp;</TD>
    
    <!-- MESSAGES -->
    
  	<TD vAlign=top>
  		<layout:panel key="Messages" align="center" styleClass="PANEL_LOGON" width="270px">
	  		<layout:grid cols="1" styleClass="SEARCH_GRID"  borderSpacing="5">	
	      		<layout:column>
  					<%
  						String infoMessage = adminVar.getValue(Constants.MESSAGE_INFO);
  						if (infoMessage.equals("unknown var")) infoMessage = "";
  					%>
  					<%=infoMessage%>
  				</layout:column>
  			</layout:grid> 
		 </layout:panel>
  	</TD>
  </TR>
  
    
  <TR><TD align=center colspan=3>&nbsp;</TD></TR>

  <!-- LOGOS -->
  
  <TR><TD align=center colspan=3>
  
    <DIV align=center>
    <a href="http://www.esrf.fr"><img src="<%=request.getContextPath()%>/images/logo-esrf_02.gif" border=0></a>
    <a href="http://www.embl-hamburg.de/"><img src="<%=request.getContextPath()%>/images/embl_logo.png" border=0></a>
    <a href="http://www.spineurope.org"><img src="<%=request.getContextPath()%>/images/spine_icon_02.gif" border=0></a>
    <a href="http://www.bm14.ac.uk"><img src="<%=request.getContextPath()%>/images/bm14-logo_02.gif" border=0></a>
	<a href="http://www.diamond.ac.uk"><img src="<%=request.getContextPath()%>/images/diamond-logo.png" border=0></a>
	<a href="http://www.synchrotron-soleil.fr"><img src="<%=request.getContextPath()%>/images/soleil-logo.gif" border=0></a>
	<a href="http://www.maxiv.se"><img src="<%=request.getContextPath()%>/images/max_iv_logo.gif" border=0></a>
	<a href="http://www.cells.es"><img src="<%=request.getContextPath()%>/images/alba.png" border=0></a>

    </DIV>

  </TD></TR>

  <!-- FOOTER -->
  
  <TR><TD align=center colspan=3>
  
    <DIV align=center>
		  <P class=x-small>
          <A href="http://www.esrf.fr/">ESRF</A> | 
          <A href="http://www.embl-hamburg.de/">EMBL</A> | 
          <A href="http://www.bm14.ac.uk/">BM14</A> | 
          <A href="http://www.spineurope.org/">SPINE</A> | 
          <A href="http://www.ebi.ac.uk/msd/">MSD</A> | 
		  <A href="http://www.diamond.ac.uk">Diamond</A> | 
		  <A href="http://www.synchrotron-soleil.fr">SOLEIL</A> | 
		  <A href="http://www.embl-grenoble.fr/">EMBL</A> |
		  <A href="http://www.maxiv.se/">MAXIV</A> |
		  <A href="http://www.cells.es/">ALBA</A>
      </P>
      <P class=x-small>
      		Copyright &copy; 2004 ISPyB All rights reserved. <br/>
      </P>
    </DIV>

  </TD></TR>

</TABLE>

<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/js/external/awstats/awstats_misc_tracker.js" ></script>
<noscript><img src="<%=request.getContextPath()%>/js/external/awstats/awstats_misc_tracker.js?nojs=y" height="0" width="0" border="0" style="display: none"></noscript>
