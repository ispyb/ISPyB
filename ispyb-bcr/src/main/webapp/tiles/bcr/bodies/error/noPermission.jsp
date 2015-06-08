<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>

<FORM action="/bcr/bcr/logoff.do" method="post" name="logoffForm">

<TABLE class="tableForm1" id="table0" cellSpacing=10px cellPadding=0>
	<TR>
		<TD align="center" nowrap><FONT class="normText1_RED">ERREUR</FONT></TD>
	</TR>
	<TR>
		<TD align="center" nowrap>
			<FONT class="normText1">
				Permission refusée<BR>
				<BR>
			</FONT>
		</TD>
	</TR>
	<TR>
		<TD align="center" colspan=2>
		<input class="buttonType1" type="submit" value="Ok">
		</TD>
	</TR>
</TABLE>

<!-- EMBED SRC="<%=request.getContextPath()%>/images/nok.wav" hidden="true" autostart="true" --> 

</FORM>