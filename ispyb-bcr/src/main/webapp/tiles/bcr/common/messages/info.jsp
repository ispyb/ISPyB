<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>


<logic:messagesPresent message="true">
	<UL>
		<html:messages id="message" message="true">
			<LI class="info"><bean:write name="message"/></LI>
		</html:messages>
	</UL>
</logic:messagesPresent>
