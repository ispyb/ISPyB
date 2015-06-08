<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.application-servers.com/layout" prefix="layout" %>

<layout:skin />

<FORM action="/bcr/user/XXX.do" method="post" name="chooseAppliForm">
<INPUT name="reqCode" value="XXX" type="hidden">

<TABLE class="tableForm1" id="table0" cellSpacing=8px cellPadding=0>
	<TR>
		<TD align="center" colspan=2><FONT class=fieldName1>Menu</FONT></TD>
	</TR>
	<TR>
		<TD align="center"><input class="buttonType1_large" type="button" value="Localisation des Dewars" onClick="parent.location='<%=request.getContextPath()%>/user/localization.do?reqCode=display';loading('Traitement en cours...');"></TD>
	</TR>
	<TR>
		<TD align="center"><input class="buttonType1_large" type="button" value="Expédition des Dewars" onClick="parent.location='<%=request.getContextPath()%>/user/courier.do?reqCode=display';loading('Traitement en cours...');"></TD>
	</TR>
	<TR>
		<TD align="center"><input class="buttonType1_large" type="button" value="Déconnexion" onClick="parent.location='/bcr/bcr/logoff.do'"></TD>
	</TR>
</TABLE>

</FORM>

<!-- EMBED SRC="<%=request.getContextPath()%>/images/ok.wav" hidden="true" autostart="true" --> 



