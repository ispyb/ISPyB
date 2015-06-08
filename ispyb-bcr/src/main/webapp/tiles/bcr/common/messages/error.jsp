<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>

<logic:messagesPresent>
	<UL>
		<html:messages id="error">
			<LI class="error"><bean:write name="error"/></LI>
		</html:messages>
	</UL>
</logic:messagesPresent>