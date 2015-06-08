<%--------------------------------------------------------------------------------------------------
This file is part of ISPyB.

ISPyB is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

ISPyB is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with ISPyB.  If not, see <http://www.gnu.org/licenses/>.

Contributors : S. Delageniere, R. Leal, L. Launer, K. Levik, S. Veyrier, P. Brenchereau, M. Bodin, A. De Maria Antolinos
--------------------------------------------------------------------------------------------------%>
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

