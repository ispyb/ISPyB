<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.application-servers.com/layout" prefix="layout" %>

<layout:skin />

<SCRIPT TYPE="text/javascript">

function onloadScript()
{
	document.forms[0].currentDewarNumber.focus();
	var objControl=document.forms[0].dewarList;
    objControl.scrollTop = objControl.scrollHeight;
	return true;
}

function onclickScript()
{
	if ( !document.forms[0].currentDewarNumber.hasFocus  )
    	document.forms[0].currentDewarNumber.focus();
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

<FORM action="/bcr/bcr/user/localizationList.do" method="post" name="localizationForm">
<INPUT name="reqCode" value="addDewar" type="hidden">

<INPUT name="location" value="<layout:write name="localizationForm" property="location"/>" type="hidden">
<INPUT name="nbDewars" value="<layout:write name="localizationForm" property="nbDewars"/>" type="hidden">

<TABLE class="tableForm1" id="table0" cellSpacing=8px cellPadding=0>
	<TR>
		<TD align="center" nowrap colspan=2><FONT class="fieldName1"><layout:write name="localizationForm" property="location"/>&nbsp;:&nbsp;<layout:write name="localizationForm" property="nbDewars"/>&nbsp;dewars</FONT></TD>
	</TR>
	<TR>
		<TD align="left" nowrap><FONT class="fieldName1">DN&nbsp;:&nbsp;</FONT></TD>
		<TD align="center"><input class=inputType3 type="text" name="currentDewarNumber" onfocus="this.hasFocus=true" onblur="this.hasFocus=false" onKeyPress="return submitMyForm(this,event)"></TD>
	</TR>
	<TR>
		<TD align="center"  colspan=2><textarea readonly class=fieldList1 name="dewarList" ><layout:write name="localizationForm" property="dewarList"/></textarea></TD>
	</TR>
	<TR>
		<TD align="center" colspan=2>
		<input class="buttonType1" type="submit" value="Ok" onclick="this.form.elements['reqCode'].value='sendList';loading('Traitement en cours...');">
		<input class="buttonType1" type="submit" value="Reset" onclick="this.form.elements['reqCode'].value='resetList';loading('Traitement en cours...');">
		<input class="buttonType1" type="button" value="Menu" onClick="parent.location='/bcr/bcr/user/chooseappli.do';loading('Traitement en cours...');">
		</TD>
	</TR>
	<TR>
		<TD align="center" colspan=2><FONT class=normText1_RED><layout:write name="localizationForm" property="message"/></FONT></TD>
	</TR>
</TABLE>

</FORM>

<!-- EMBED SRC="<%=request.getContextPath()%>/images/ok.wav" hidden="true" autostart="true" --> 




