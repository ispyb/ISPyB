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

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>	

<%@page import="ispyb.common.util.Constants"%>

<c:if test="${SITE_ATTRIBUTE eq 'ESRF' or SITE_ATTRIBUTE eq 'EMBL' or SITE_ATTRIBUTE eq 'ALBA'}">
<!-- first bar:  Logon / Logoff   |   Documentation   -->
<TR>
	<TD class=headerblue vAlign=center align=right colspan=2>
		<TABLE  cellpadding="0" cellspacing="0">
			<TR>
				<%-- If the user is not logged in --%>
				<logic:notPresent name="<%=Constants.ROLES%>">
					<TD class=headerblue vAlign=center align=right>				
						&nbsp;<A class=navtab href="<%=request.getContextPath()%>/menuSelected.do?<%=Constants.TOP_MENU_ID%>=-1&amp;<%=Constants.LEFT_MENU_ID%>=-1&amp;<%=Constants.TARGET_URL%>=/security/logon.do"><bean:message key="header.link.logon"/></A> &nbsp;
					</TD>
				</logic:notPresent>

				<%-- If the user is logged in --%>
				<logic:present name="<%=Constants.PROPOSAL_TYPE%>">
					<bean:define id="proposalType" name="<%=Constants.PROPOSAL_TYPE%>" scope="session" type="java.lang.String"/>
					<logic:equal name="proposalType" value="MB" >
						<TD class=headerblue vAlign=center align=right>
							<html:form action="/userProposalTypeChoose.do" styleClass="searchform">
								<html:select property="value" onchange="userProposalTypeChooseForm.submit()" styleClass="searchbox">					
									<html:option value="0"> -- Select Technique -- </html:option>
									<html:option value="MX">MX</html:option>
									<html:option value="BX">BioSAXS</html:option>	
								</html:select>
							</html:form>
						</TD> 
					</logic:equal>
				</logic:present>
				<logic:present name="<%=Constants.ROLES%>">
					<bean:define id="roles" name="<%=Constants.ROLES%>" scope="session" type="java.util.ArrayList"/>
					<bean:define id="numberOfRoles" value="<%=(new Integer(roles.size())).toString()%>" />
					<logic:greaterThan name="numberOfRoles" value="1" >
						<TD class=headerblue vAlign=center align=right>
							<html:form action="/rolesChoose.do" styleClass="searchform">
								<html:select property="value" onchange="rolesChooseForm.submit()" styleClass="searchbox">					
									<html:option value="0"> -- Select a new Role -- </html:option>
									<html:options collection="roles" property="id" labelProperty="name"/>
								</html:select>
							</html:form>
						</TD> 
					</logic:greaterThan>
					<TD class=headerblue vAlign=center align=right>
						&nbsp;<A class=navtab href="<%=request.getContextPath()%>/menuSelected.do?<%=Constants.TOP_MENU_ID%>=-1&amp;<%=Constants.LEFT_MENU_ID%>=-1&amp;<%=Constants.TARGET_URL%>=/logoff.do"><bean:message key="header.link.logoff"/> <%=request.getUserPrincipal().getName()%></A> &nbsp;<%-- request.getUserPrincipal().toString()%></A> &nbsp; --%>
					</TD>
					<TD class=headerblue vAlign=center align=right>
						<%-- If the user is logged in --%>
						<logic:present name="<%=Constants.FULLNAME%>">
							(<%=request.getSession().getAttribute(Constants.FULLNAME)%>) 
						</logic:present>
					</TD>					
				</logic:present>
			</TR>
		</TABLE>
	</TD>
</TR>
</c:if>

<c:if test="${SITE_ATTRIBUTE eq 'DLS'}">
<!-- first bar:  Logon / Logoff   |   Documentation   -->
<tr>
	<td class="headerblue" vAlign="center" align="right">
		<table  cellpadding="0" cellspacing="0">
			<tr>
				
				<td align="right" style="color: #ffffff">
				&nbsp;
				<%-- If the user is logged in --%>
				<logic:present name="<%=Constants.FULLNAME%>">
					<%-- User: <%=request.getSession().getAttribute(Constants.FULLNAME)%> (<%=request.getUserPrincipal().toString()%>) --%>
					User: <%=request.getSession().getAttribute(Constants.FULLNAME)%> (<%=request.getRemoteUser().toLowerCase()%>)
					
				</logic:present>
				</td>
				</tr>
		</table>
	</td>
</tr>
</c:if>

<c:if test="${SITE_ATTRIBUTE eq 'MAXIV'}">
<!-- first bar:  Logon / Logoff   |   Documentation   -->
<TR>
	<TD class=headerblue vAlign=center align=right colspan=2>
		<TABLE  cellpadding="0" cellspacing="0">
			<TR>
				<%-- If the user is not logged in --%>
				<logic:notPresent name="<%=Constants.ROLES%>">
					<TD class=headerblue vAlign=center align=right>				
						&nbsp;<A class=navtab href="<%=request.getContextPath()%>/menuSelected.do?<%=Constants.TOP_MENU_ID%>=-1&amp;<%=Constants.LEFT_MENU_ID%>=-1&amp;<%=Constants.TARGET_URL%>=/security/logon.do"><bean:message key="header.link.logon"/></A> &nbsp;
					</TD>
				</logic:notPresent>

				<%-- If the user is logged in --%>
				<logic:present name="<%=Constants.PROPOSAL_TYPE%>">
					<bean:define id="proposalType" name="<%=Constants.PROPOSAL_TYPE%>" scope="session" type="java.lang.String"/>
					<logic:equal name="proposalType" value="MB" >
						<TD class=headerblue vAlign=center align=right>
							<html:form action="/userProposalTypeChoose.do" styleClass="searchform">
								<html:select property="value" onchange="userProposalTypeChooseForm.submit()" styleClass="searchbox">					
									<html:option value="0"> -- Select Technique -- </html:option>
									<html:option value="MX">MX</html:option>
									<html:option value="BX">BioSAXS</html:option>	
								</html:select>
							</html:form>
						</TD> 
					</logic:equal>
				</logic:present>
				<logic:present name="<%=Constants.ROLES%>">
					<bean:define id="roles" name="<%=Constants.ROLES%>" scope="session" type="java.util.ArrayList"/>
					<bean:define id="numberOfRoles" value="<%=(new Integer(roles.size())).toString()%>" />
					<logic:greaterThan name="numberOfRoles" value="1" >
						<TD class=headerblue vAlign=center align=right>
							<html:form action="/rolesChoose.do" styleClass="searchform">
								<html:select property="value" onchange="rolesChooseForm.submit()" styleClass="searchbox">					
									<html:option value="0"> -- Select a new Role -- </html:option>
									<html:options collection="roles" property="id" labelProperty="name"/>
								</html:select>
							</html:form>
						</TD> 
					</logic:greaterThan>
					<TD class=headerblue vAlign=center align=right>
						&nbsp;<A class=navtab href="<%=request.getContextPath()%>/menuSelected.do?<%=Constants.TOP_MENU_ID%>=-1&amp;<%=Constants.LEFT_MENU_ID%>=-1&amp;<%=Constants.TARGET_URL%>=/logoff.do"><bean:message key="header.link.logoff"/> <%=request.getUserPrincipal().getName()%></A> &nbsp;<%-- request.getUserPrincipal().toString()%></A> &nbsp; --%>
					</TD>
					<TD class=headerblue vAlign=center align=right>
						<%-- If the user is logged in --%>
						<logic:present name="<%=Constants.FULLNAME%>">
							(<%=request.getSession().getAttribute(Constants.FULLNAME)%>) 
						</logic:present>
					</TD>					
				</logic:present>
			</TR>
		</TABLE>
	</TD>
</TR>
</c:if>

<c:if test="${SITE_ATTRIBUTE eq 'SOLEIL'}">
<!-- first bar:  Logon / Logoff   |   Documentation   -->
<TR>
	<TD class=headerblue vAlign=center align=right colspan=2>
		<TABLE  cellpadding="0" cellspacing="0">
			<TR>
				<%-- If the user is not logged in --%>
				<logic:notPresent name="<%=Constants.ROLES%>">
					<TD class=headerblue vAlign=center align=right>				
						&nbsp;<A class=navtab href="<%=request.getContextPath()%>/menuSelected.do?<%=Constants.TOP_MENU_ID%>=-1&amp;<%=Constants.LEFT_MENU_ID%>=-1&amp;<%=Constants.TARGET_URL%>=/security/logon.do"><bean:message key="header.link.logon"/></A> &nbsp;
					</TD>
				</logic:notPresent>

				<%-- If the user is logged in --%>
				<logic:present name="<%=Constants.PROPOSAL_TYPE%>">
					<bean:define id="proposalType" name="<%=Constants.PROPOSAL_TYPE%>" scope="session" type="java.lang.String"/>
					<logic:equal name="proposalType" value="MB" >
						<TD class=headerblue vAlign=center align=right>
							<html:form action="/userProposalTypeChoose.do" styleClass="searchform">
								<html:select property="value" onchange="userProposalTypeChooseForm.submit()" styleClass="searchbox">					
									<html:option value="0"> -- Select Technique -- </html:option>
									<html:option value="MX">MX</html:option>
									<html:option value="BX">BioSAXS</html:option>	
								</html:select>
							</html:form>
						</TD> 
					</logic:equal>
				</logic:present>
				<logic:present name="<%=Constants.ROLES%>">
					<bean:define id="roles" name="<%=Constants.ROLES%>" scope="session" type="java.util.ArrayList"/>
					<bean:define id="numberOfRoles" value="<%=(new Integer(roles.size())).toString()%>" />
					<logic:greaterThan name="numberOfRoles" value="1" >
						<TD class=headerblue vAlign=center align=right>
							<html:form action="/rolesChoose.do" styleClass="searchform">
								<html:select property="value" onchange="rolesChooseForm.submit()" styleClass="searchbox">					
									<html:option value="0"> -- Select a new Role -- </html:option>
									<html:options collection="roles" property="id" labelProperty="name"/>
								</html:select>
							</html:form>
						</TD> 
					</logic:greaterThan>
					<TD class=headerblue vAlign=center align=right>
						&nbsp;<A class=navtab href="<%=request.getContextPath()%>/menuSelected.do?<%=Constants.TOP_MENU_ID%>=-1&amp;<%=Constants.LEFT_MENU_ID%>=-1&amp;<%=Constants.TARGET_URL%>=/logoff.do"><bean:message key="header.link.logoff"/> <%=request.getUserPrincipal().getName()%></A> &nbsp;<%-- request.getUserPrincipal().toString()%></A> &nbsp; --%>
					</TD>
					<TD class=headerblue vAlign=center align=right>
						<%-- If the user is logged in --%>
						<logic:present name="<%=Constants.FULLNAME%>">
							(<%=request.getSession().getAttribute(Constants.FULLNAME)%>) 
						</logic:present>
					</TD>					
				</logic:present>
			</TR>
		</TABLE>
	</TD>
</TR>
</c:if>