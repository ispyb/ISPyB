
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>



<logic:messagesPresent>
<h1>Error</h1>
<p> This is the list of errors: </p>
<UL>
<html:messages id="error">
<LI><bean:write name="error"/></LI>
</html:messages>
</UL>
</logic:messagesPresent>


<logic:messagesPresent message="true">
<h1>Information Page</h1>
<p> This is the list of messages: </p>
<UL>
<html:messages id="message" message="true">
<LI><bean:write name="message"/></LI>
</html:messages>
</UL>
</logic:messagesPresent>


<p> Click <a href="<%=request.getContextPath()%>">here</a> to return to homepage.</p>

