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
<UL>
<html:messages id="error">
<LI><bean:write name="error"/></LI>
</html:messages>
</UL>
</logic:messagesPresent>


<html:form action="/user/viewSession.do">
    		<table width="200" height="100%">
				
                <tr>
                    <td align="left">Proposal ID</td>
                    <td align="left"><html:text property="proposalId" styleClass="txt" size="12" maxlength="12"/></td>
                </tr>
                <tr>
                    <td colspan="2"></td>
                </tr>
                <tr>
                    <td align="left">
						<html:submit property="submit">                            
                            <bean:message key="button.search"/>
                        </html:submit></td>
					<td align="left">
                        <html:reset>
							<bean:message key="button.reset"/>
						</html:reset></td>
                </tr>
            </table>
        </html:form>
