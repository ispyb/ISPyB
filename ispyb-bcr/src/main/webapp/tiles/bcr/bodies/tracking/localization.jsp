<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.application-servers.com/layout" prefix="layout" %>

<layout:skin />

<SCRIPT TYPE="text/javascript">

function onloadScript()
{
	document.forms[0].location.focus();
	return true;
}

function onclickScript()
{
	if ( !document.forms[0].location.hasFocus )
    	document.forms[0].location.focus();
	return true;
}

function submitMyForm(myfield,e)
{
  var keycode;
  if (window.event) keycode = window.event.keyCode;
  else if (e) keycode = e.which;
  else return true;
  
  if (keycode == 13){
     myfield.form.submit();
     return false;
    }
  else
     return true;
}

</SCRIPT>

<FORM action="/bcr/bcr/user/localization.do" method="post" name="localizationForm">
<INPUT name="reqCode" value="localizationList" type="hidden">

<TABLE class="tableForm1" id="table0" cellSpacing=8px cellPadding=0>
	<TR>
		<TD align="center" colspan=2><FONT class=fieldName1>Localisation</FONT></TD>
	</TR>
	<TR>
		<TD align="center"><input class=inputType0 type="text" name="location" value="<layout:write name="localizationForm" property="location"/>" onfocus="this.hasFocus=true" onblur="this.hasFocus=false"  onKeyPress="return submitMyForm(this,event)"></TD>
	</TR>
	<TR>
		<TD align="center" colspan=2>
		<input class="buttonType1" type="submit" value="Ok" onclick="this.form.elements['reqCode'].value='localizationList';loading('Traitement en cours...');">
		<input class="buttonType1" type="reset" value="Reset">
		<input class="buttonType1" type="button" value="Menu" onClick="parent.location='/bcr/bcr/user/chooseappli.do';loading('Traitement en cours...');">
		</TD>
	</TR>
	<TR>
		<TD align="center" colspan=2><FONT class=normText1_RED><layout:write name="localizationForm" property="message"/></FONT></TD>
	</TR>
</TABLE>

</FORM>

<!-- EMBED SRC="<%=request.getContextPath()%>/images/ok.wav" hidden="true" autostart="true" --> 



