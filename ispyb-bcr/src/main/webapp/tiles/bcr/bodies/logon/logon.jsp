<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.application-servers.com/layout" prefix="layout" %>

<layout:skin />

<SCRIPT TYPE="text/javascript">

function onloadScript()
{
	document.forms[0].j_password.focus();
	return true;
}

function onclickScript()
{
	if ( !document.forms[0].j_username.hasFocus && !document.forms[0].j_password.hasFocus )
    	document.forms[0].j_username.focus();
	return true;
}

function submitLogin(myfield,e)
{
  var keycode;
  if (window.event) keycode = window.event.keyCode;
  else if (e) keycode = e.which;
  else return true;
  
  if (keycode == 13){
     myfield.form.j_password.focus();
     //alert("submitLogin");
     return false;
    }
  else
     return true;
}

function submitPassword(myfield,e)
{
  var keycode;
  if (window.event) keycode = window.event.keyCode;
  else if (e) keycode = e.which;
  else return true;
  
  if (keycode == 13){
     myfield.value = myfield.value.toLowerCase();
     myfield.form.submit();
     //alert("submitPassword");
     return false;
    }
  else
     return true;
}

</SCRIPT>

<FORM action="j_security_check" method="post" name="login">

<TABLE class="tableForm1" id="table0" cellSpacing=10px cellPadding=0>
	<TR>
		<TD align="center" colspan=2><FONT class=fieldName1>Authentification</FONT></TD>
	</TR>
	<TR>
		<TD align="right"><FONT class=fieldName1>Utilisateur</FONT></TD>
		<TD align="left"><input class=inputType0 value="STORES" type="text" name="j_username" onfocus="this.hasFocus=true" onblur="this.hasFocus=false" onKeyPress="return submitLogin(this,event)"></TD>
	</TR>
	<TR>
		<TD align="right"><FONT class=fieldName1>Mot de passe</FONT></TD>
		<TD align="left"><input class=inputType0 value="" type="password" name="j_password" onfocus="this.hasFocus=true" onblur="this.hasFocus=false" onKeyPress="return submitPassword(this,event)"></TD>
	</TR>
	<TR>
		<TD align="center" colspan=2>
		<input class="buttonType1" type="submit" value="Ok" onClick="loading('Traitement en cours...');">
		<input class="buttonType1" type="reset" value="Reset">
		</TD>
	</TR>
</TABLE>

</FORM>	



