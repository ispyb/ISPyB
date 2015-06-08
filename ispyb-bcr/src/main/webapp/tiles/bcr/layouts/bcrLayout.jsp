<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE><tiles:getAsString name="title"/></TITLE>
<META http-equiv=Content-Type content="text/html; charset=iso-8859-1">
<LINK href="<%=request.getContextPath()%>/css/styles.css" type=text/css rel=STYLESHEET />
<LINK href="<%=request.getContextPath()%>/css/<tiles:getAsString name="selectedStyles"/>" type=text/css rel=STYLESHEET />
<LINK REL="SHORTCUT ICON" HREF="<%=request.getContextPath()%>/images/ispyb_icon.ico">
</HEAD>
<BODY bgColor=#ffffff marginwidth="0" marginheight="0" text="#000000" onload="onloadScript();"  onclick="onclickScript();">


<!-- =======================================================  -->
<!-- HEADER Page -->
<!-- =======================================================  -->

<c:choose>
<c:when test="${param.login == 'bcr'}">

<TABLE cellSpacing=0 cellPadding=0 border=0 width=223px>
  <TBODY>
    <TR><TD>BCR</TD></TR>
  </TBODY>
</TABLE>

</c:when>
<c:otherwise>
	
<TABLE cellSpacing=0 cellPadding=0 border=0 width=223px>
  <TBODY>
    <tiles:insert attribute="header_top_bar" />
    <tiles:insert attribute="header_logo_bar" />
    <tiles:insert attribute="header_context_bar" />
    <tiles:insert attribute="header_tab_bar" />
  </TBODY>
</TABLE>

</c:otherwise>
</c:choose>

<DIV id="loadingLayer" style="position:absolute; top=0px; left=0px; z-index=10" >
	<TABLE cellSpacing=0 cellPadding=0 border=0 height=100% width=100%>
		<TR height>
			<TD id="tdTableLoadingLayer" height=1 width=100% align=center valign=middle>
				<IMG src="../images/progressbar_2.gif" border=0 width=0 height=0> 
			</TD>
		</TR>
	</TABLE>
</DIV>
<SCRIPT SRC="../config/loading.js"></SCRIPT>

<TABLE cellSpacing=0 cellPadding=0 border=0 width=223px>
  <TBODY>
  
  	<!-- =======================================================  -->
	<!-- BODY Page -->
	<!-- =======================================================  -->
    <TR>
		<TD vAlign=top align=center>
	        <tiles:insert attribute="body" />
		</TD>
    </TR>
    
	<!-- =======================================================  -->
	<!-- FOOTER Page -->
	<!-- =======================================================  -->
    <TR>
    	<TD>
      		<tiles:insert attribute="footer" />
		</TD>
    </TR>
  </TBODY>
</TABLE>

</BODY>
</HTML>
