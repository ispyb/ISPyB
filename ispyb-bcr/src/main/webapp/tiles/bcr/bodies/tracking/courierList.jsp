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
	if ( !document.forms[0].currentDewarNumber.hasFocus && !document.forms[0].currentTrackingNumber.hasFocus )
    	document.forms[0].currentDewarNumber.focus();
	return true;
}

function submitDewar(myfield,e)
{
  var keycode;
  if (window.event) keycode = window.event.keyCode;
  else if (e) keycode = e.which;
  else return true;
  
  if (keycode == 13){
     myfield.form.currentTrackingNumber.focus();
     return false;
    }
  else
     return true;
}

function submitTracking(myfield,e)
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

<FORM action="/bcr/bcr/user/courierList.do" method="post" name="courierForm">
<INPUT name="reqCode" value="addDewar" type="hidden">

<INPUT name="courierName" value="<layout:write name="courierForm" property="courierName"/>" type="hidden">
<INPUT name="nbDewars" value="<layout:write name="courierForm" property="nbDewars"/>" type="hidden">

<TABLE class="tableForm1" id="table0" cellSpacing=8px cellPadding=0>
	<TR>
		<TD align="center" colspan=2 nowrap><FONT class="fieldName1"><layout:write name="courierForm" property="courierName"/>&nbsp;:&nbsp;<layout:write name="courierForm" property="nbDewars"/>&nbsp;dewars</FONT></TD>
	</TR>
	<TR>
		<TD>
		  	<TABLE id="table1" cellpadding="0" cellspacing="0" width="100%"">
			<TR>
				<TD align="left" nowrap><FONT class="fieldName1">DN&nbsp;:&nbsp;</FONT></TD>
				<TD align="right"><input class=inputType3 type="text" name="currentDewarNumber" value="<layout:write name="courierForm" property="currentDewarNumber"/>" onfocus="this.hasFocus=true" onblur="this.hasFocus=false" onKeyPress="return submitDewar(this,event)"></TD>
			</TR>
			<TR>
				<TD align="left" nowrap><FONT class="fieldName1">TN&nbsp;:&nbsp;</FONT></TD>
				<TD align="right"><input class=inputType3 type="text" name="currentTrackingNumber" value="<layout:write name="courierForm" property="currentTrackingNumber"/>" onfocus="this.hasFocus=true" onblur="this.hasFocus=false" onKeyPress="return submitTracking(this,event)"></TD>
			</TR>
			</TABLE>
		</TD>
	</TR>
	<TR>
		<TD align="center" colspan=2><textarea readonly class=fieldList1 name="dewarList"><layout:write name="courierForm" property="dewarList"/></textarea></TD>
	</TR>
	<TR>
		<TD align="center" colspan=2>
		<input class="buttonType1" type="submit" value="Ok" onclick="this.form.elements['reqCode'].value='sendList';loading('Traitement en cours...');">
		<input class="buttonType1" type="submit" value="Reset" onclick="this.form.elements['reqCode'].value='resetList';loading('Traitement en cours...');">
		<input class="buttonType1" type="button" value="Menu" onClick="parent.location='/bcr/bcr/user/chooseappli.do';loading('Traitement en cours...');">
		</TD>
	</TR>
	<TR>
		<TD align="center" colspan=2><FONT class=normText1_RED><layout:write name="courierForm" property="message"/></FONT></TD>
	</TR>
</TABLE>

</FORM>

<!-- EMBED SRC="<%=request.getContextPath()%>/images/ok.wav" hidden="true" autostart="true" --> 




