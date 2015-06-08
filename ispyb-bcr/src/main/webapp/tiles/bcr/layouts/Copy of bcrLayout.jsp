<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
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
<TABLE cellSpacing=0 cellPadding=0 border=0 width=223px>
  <TBODY>
    <tiles:insert attribute="header_top_bar" />
    <tiles:insert attribute="header_logo_bar" />
    <tiles:insert attribute="header_context_bar" />
    <tiles:insert attribute="header_tab_bar" />
  </TBODY>
</TABLE>


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
