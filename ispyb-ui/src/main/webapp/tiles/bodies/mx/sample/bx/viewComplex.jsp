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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.application-servers.com/layout" prefix="layout"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/bubble_tooltip.css" />
<layout:skin includeScript="true" />

<%
	//String targetEditComplex 			= request.getContextPath() + "/user/createComplexAction.do?reqCode=display";
	String targetEditComplex 			= request.getContextPath() + "/user/createComplexAction.do?reqCode=display";
	String targetRemoveComplex 			= request.getContextPath() + "/user/createComplexAction.do?reqCode=remove";
%>


<layout:grid cols="1" borderSpacing="10">

	<%-- Page --%>
	<layout:column>
		<layout:panel>
			<layout:row>
				<layout:collection name="ViewComplexForm" property="complexes" styleClass="LIST" styleClass2="LIST2" id="currComplexes">
					<layout:collectionItem title="Name" property="complex3VO.name" paramId="complex3VO.name" paramProperty="complex3VO.name" sortable="false"  />
					<layout:collectionItem title="Type" property="complex3VO.type" paramId="complex3VO.type" paramProperty="complex3VO.type" />
					<layout:collectionItem title="Protein" property="complex3VO.protein3VO.acronym" paramId="complex3VO.protein3VO.acronym" paramProperty="complex3VO.protein3VO.acronym" />
					<layout:collectionItem title="Comment" property="complex3VO.comment" paramId="complex3VO.comments" paramProperty="complex3VO.comment" />
					
					<layout:collectionItem title="Edit" width="60px">
											<html:link href="<%=targetEditComplex %>"
												paramName="currComplexes" paramId="complexId"
												paramProperty="complex3VO.complexId"
												onclick="return window.confirm('Do you really want to edit this complex?');">
												<img src="<%=request.getContextPath()%>/images/Edit_16x16_01.png" border=0 onmouseover="return overlib('Delete the domain');" onmouseout="return nd();">
											</html:link>
					</layout:collectionItem>
					<layout:collectionItem title="Remove" width="60px">
											<html:link href="<%=targetRemoveComplex %>"
												paramName="currComplexes" paramId="complexId"
												paramProperty="complex3VO.complexId"
												onclick="return window.confirm('Do you really want to remove this complex?');">
												<img src="<%=request.getContextPath()%>/images/cancel.png" border=0 onmouseover="return overlib('Delete the domain');" onmouseout="return nd();">
											</html:link>
					</layout:collectionItem>
				</layout:collection>
			</layout:row>
		</layout:panel>
	</layout:column>

</layout:grid>