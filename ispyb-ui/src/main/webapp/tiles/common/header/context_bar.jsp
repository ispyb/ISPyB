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

<%@page import="ispyb.client.common.menu.*"%>


	<%
	BarContext bar = new BarContext(request);
	%>

<c:if test="${SITE_ATTRIBUTE eq 'ESRF' or SITE_ATTRIBUTE eq 'SOLEIL'}">
	<TR bgColor=#ffffff>
		<TD vAlign=center colspan=2>
			<TABLE  cellpadding="0" cellspacing="0">
				<TR>
				    <TD>&nbsp;</TD>
					<logic:iterate id="contextItem" collection="<%=bar.getContext()%>" type="ispyb.server.common.vos.config.Menu3VO">
						<TD>
							<IMG src="<%=request.getContextPath()%>/images/blue-arrow.gif">
							<A class=navbarchild 
							<logic:present name="contextItem" property="action">href="<bean:write name="contextItem" property="action"/>"</logic:present>>
							<bean:write name="contextItem" property="name"/></A>
							&nbsp;
			     		</TD>
		     		</logic:iterate>
	 				<!--   
					<TD>
						&nbsp;<IMG src="<%=request.getContextPath()%>/images/blue-arrow.gif">
						<A class=navbarchild><%=  request.getRequestURL().toString().substring(request.getRequestURL().toString().lastIndexOf('/'))  %></A>
					</TD>
					-->
				</TR>
			</TABLE>
  		</TD>
    </TR>
</c:if>

<c:if test="${SITE_ATTRIBUTE eq 'DLS'}">
	<tr bgColor="#ffffff">
	<td>
		<table cellpadding="0" cellspacing="0" width="100%"> 
			<tr bgColor="#ffffff">
			<td>
				<table cellpadding="0" cellspacing="0" align="left"> 
					<tr>
						<logic:iterate id="contextItem" collection="<%=bar.getContext()%>" type="ispyb.server.common.vos.config.Menu3VO">
							<td align="left">
								&nbsp;<img src="<%=request.getContextPath()%>/images/blue-arrow.gif" />
								<logic:present name="contextItem" property="action">
									<a class="navbarchild" href='<bean:write name="contextItem" property="action"/>'>
									<bean:write name="contextItem" property="name"/></a>
								</logic:present>
								<logic:notPresent name="contextItem" property="action">
									<a class="navbarchild"><bean:write name="contextItem" property="name"/></a>
								</logic:notPresent>
				     		</td>
			     		</logic:iterate>
		 
						<td align="left">
							&nbsp;<img src="<%=request.getContextPath()%>/images/blue-arrow.gif"/>
							<a class="navbarchild"><%=request.getRequestURL().toString().substring(request.getRequestURL().toString().lastIndexOf('/'))  %></a>
						</td>
					</tr>
				</table>	
			</td>
			<td>			
				<logic:present name="<%=Constants.PROPOSAL_NUMBER%>">
				<table cellpadding="0" cellspacing="0" width="100%" align="right"> 
					<tr>
						<td align="right">
							<table align="right">
							<tr>
								<td style="font-weight:normal; color: #406088;">Proposal:&nbsp;</td>
								<td style="font-weight:normal; color: #406088;">
									<%=request.getSession().getAttribute(Constants.PROPOSAL_CODE)%><%=request.getSession().getAttribute(Constants.PROPOSAL_NUMBER)%>
								</td>
							</tr>
							</table>
						</td>
					</tr>
				</table>
				</logic:present>
			</td>
			</tr>
		</table>
	</td>
	</tr>
</c:if>

<c:if test="${SITE_ATTRIBUTE eq 'EMBL'}">
	<TR bgColor=#ffffff>
		<TD vAlign=center colspan=2>
			<TABLE  cellpadding="0" cellspacing="0">
				<TR>
				    <TD>&nbsp;</TD>
					<logic:iterate id="contextItem" collection="<%=bar.getContext()%>" type="ispyb.server.common.vos.config.Menu3VO">
						<TD>
							<IMG src="<%=request.getContextPath()%>/images/blue-arrow.gif">
							<A class=navbarchild 
							<logic:present name="contextItem" property="action">href="<bean:write name="contextItem" property="action"/>"</logic:present>>
							<bean:write name="contextItem" property="name"/></A>
							&nbsp;
			     		</TD>
		     		</logic:iterate>
	 				<!--   
					<TD>
						&nbsp;<IMG src="<%=request.getContextPath()%>/images/blue-arrow.gif">
						<A class=navbarchild><%=  request.getRequestURL().toString().substring(request.getRequestURL().toString().lastIndexOf('/'))  %></A>
					</TD>
					-->
				</TR>
			</TABLE>
  		</TD>
    </TR>
</c:if>

<c:if test="${SITE_ATTRIBUTE eq 'MAXIV'}">
	<TR bgColor=#ffffff>
		<TD vAlign=center colspan=2>
			<TABLE  cellpadding="0" cellspacing="0">
				<TR>
				    <TD>&nbsp;</TD>
					<logic:iterate id="contextItem" collection="<%=bar.getContext()%>" type="ispyb.server.common.vos.config.Menu3VO">
						<TD>
							<IMG src="<%=request.getContextPath()%>/images/blue-arrow.gif">
							<A class=navbarchild 
							<logic:present name="contextItem" property="action">href="<bean:write name="contextItem" property="action"/>"</logic:present>>
							<bean:write name="contextItem" property="name"/></A>
							&nbsp;
			     		</TD>
		     		</logic:iterate>
	 				<!--   
					<TD>
						&nbsp;<IMG src="<%=request.getContextPath()%>/images/blue-arrow.gif">
						<A class=navbarchild><%=  request.getRequestURL().toString().substring(request.getRequestURL().toString().lastIndexOf('/'))  %></A>
					</TD>
					-->
				</TR>
			</TABLE>
  		</TD>
    </TR>
</c:if>

<c:if test="${SITE_ATTRIBUTE eq 'ALBA'}">
	<TR bgColor=#ffffff>
		<TD vAlign=center colspan=2>
			<TABLE  cellpadding="0" cellspacing="0">
				<TR>
				    <TD>&nbsp;</TD>
					<logic:iterate id="contextItem" collection="<%=bar.getContext()%>" type="ispyb.server.common.vos.config.Menu3VO">
						<TD>
							<IMG src="<%=request.getContextPath()%>/images/blue-arrow.gif">
							<A class=navbarchild 
							<logic:present name="contextItem" property="action">href="<bean:write name="contextItem" property="action"/>"</logic:present>>
							<bean:write name="contextItem" property="name"/></A>
							&nbsp;
			     		</TD>
		     		</logic:iterate>
	 				<!--   
					<TD>
						&nbsp;<IMG src="<%=request.getContextPath()%>/images/blue-arrow.gif">
						<A class=navbarchild><%=  request.getRequestURL().toString().substring(request.getRequestURL().toString().lastIndexOf('/'))  %></A>
					</TD>
					-->
				</TR>
			</TABLE>
  		</TD>
    </TR>
</c:if>
