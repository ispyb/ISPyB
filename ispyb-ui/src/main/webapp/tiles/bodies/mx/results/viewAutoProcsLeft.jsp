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
<%@ taglib uri="http://struts.application-servers.com/layout" prefix="layout" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h2>Main Output Parameters:</h2>

<div>
	<p class="FIELD_BIG"><strong>Overall</strong></p>
</div>

<table>
<tr>
	<td>
	<p class="FIELD_BIG">Overall Resolution: </p>
	</td>
	<td>
	<p class="FIELD_BIG"><bean:write name="getAutoProcsForm" property="overallResolution" filter="false"/></p>
	</td>
</tr>
<tr>
	<td>
	<p class="FIELD_BIG">Overall Completeness: </p>
	</td>
	<td>
	<p class="FIELD_BIG"><bean:write name="getAutoProcsForm" property="overallCompleteness" filter="false"/></p>
	</td>
</tr>
<tr>
	<td>
	<p class="FIELD_BIG">Overall I over Sigma: </p>
	</td>
	<td>
	<p class="FIELD_BIG"><bean:write name="getAutoProcsForm" property="overallIOverSigma" filter="false"/></p>
	</td>
</tr>
<tr>
	<td>
	<p class="FIELD_BIG">Overall Rsymm: </p>
	</td>
	<td>
	<p class="FIELD_BIG"><bean:write name="getAutoProcsForm" property="overallRsymm" filter="false"/></p>
	</td>
</tr>
<tr>
	<td>
	<p class="FIELD_BIG">Overall Multiplicity: </p>
	</td>
	<td>
	<p class="FIELD_BIG"><bean:write name="getAutoProcsForm" property="overallMultiplicity" filter="false"/></p>
	</td>
</tr>
</table>

<div>
	<p class="FIELD_BIG"><strong>Outer Shell</strong></p>
</div>

<table>
<tr>
	<td>
	<p class="FIELD_BIG">Outer Shell Resolution: </p>
	</td>
	<td>
	<p class="FIELD_BIG"><bean:write name="getAutoProcsForm" property="outerResolution" filter="false"/></p>
	</td>
</tr>
<tr>
	<td>
	<p class="FIELD_BIG">Outer Shell Completeness: </p>
	</td>
	<td>
	<p class="FIELD_BIG"><bean:write name="getAutoProcsForm" property="outerCompleteness" filter="false"/></p>
	</td>
</tr>
<tr>
	<td>
	<p class="FIELD_BIG">Outer Shell I over Sigma: </p>
	</td>
	<td>
	<p class="FIELD_BIG"><bean:write name="getAutoProcsForm" property="outerIOverSigma" filter="false"/></p>
	</td>
</tr>
<tr>
	<td>
	<p class="FIELD_BIG">Outer Shell Rsymm: </p>
	</td>
	<td>
	<p class="FIELD_BIG"><bean:write name="getAutoProcsForm" property="outerRsymm" filter="false"/></p>
	</td>
</tr>
<tr>
	<td>
	<p class="FIELD_BIG">Outer Shell Multiplicity: </p>
	</td>
	<td>
	<p class="FIELD_BIG"><bean:write name="getAutoProcsForm" property="outerMultiplicity" filter="false"/></p>
	</td>
</tr>
</table>

<div>
	<p class="FIELD_BIG"><strong>Unit Cell</strong></p>
</div>

<div>
<table>
<tr>
	<td>
	<p class="FIELD_BIG">Unit Cell A:</p>
	</td>
	<td>
	<p class="FIELD_BIG"><bean:write name="getAutoProcsForm" property="unitCellA" filter="false"/></p>
	</td>
</tr>
<tr>
	<td>
	<p class="FIELD_BIG">Unit Cell B:</p>
	</td>
	<td>
	<p class="FIELD_BIG"><bean:write name="getAutoProcsForm" property="unitCellB" filter="false"/></p>
	</td>
</tr>
<tr>
	<td>
	<p class="FIELD_BIG">Unit Cell C:</p>
	</td>
	<td>
	<p class="FIELD_BIG"><bean:write name="getAutoProcsForm" property="unitCellC" filter="false"/></p>
	</td>
</tr>
<tr>
	<td>
	<p class="FIELD_BIG">Unit Cell Alpha:</p>
	</td>
	<td>
	<p class="FIELD_BIG"><bean:write name="getAutoProcsForm" property="unitCellAlpha" filter="false"/></p>
	</td>
</tr>
<tr>
	<td>
	<p class="FIELD_BIG">Unit Cell Beta:</p>
	</td>
	<td>
	<p class="FIELD_BIG"><bean:write name="getAutoProcsForm" property="unitCellBeta" filter="false"/></p>
	</td>
</tr>
<tr>
	<td>
	<p class="FIELD_BIG">Unit Cell Gamma:</p>
	</td>
	<td>
	<p class="FIELD_BIG"><bean:write name="getAutoProcsForm" property="unitCellGamma" filter="false"/></p>
	</td>
</tr>
</table>
</div>
<%-- ** Status ** --%>
<h2>Status history:</h2>
	<logic:empty name="getAutoProcsForm" property="autoProcEvents">
		<h4>No status found for this autoProc.</h4>
	</logic:empty>
	<logic:notEmpty name="getAutoProcsForm" property="autoProcEvents">
		<layout:collection 	name="getAutoProcsForm" property="autoProcEvents" 
				id="autoProcEvent" styleClass="LIST" styleClass2="LIST2">
										
			<layout:collectionItem title="Date" sortable="false">
				<bean:write name="autoProcEvent" property="blTimeStamp" format="dd-MM-yyyy HH:mm"/>
			</layout:collectionItem>
			<layout:collectionItem title="Step"   property="step" sortable="false"/>
			<layout:collectionItem title="Status" property="status" sortable="false"/>
			<layout:collectionItem title="Comments" property="comments" sortable="false"/>
		</layout:collection>
	</logic:notEmpty>
<%-- ** End of Status ** --%>
	