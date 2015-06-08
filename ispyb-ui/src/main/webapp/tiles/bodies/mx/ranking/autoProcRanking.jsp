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
<%@ page isELIgnored="false" %>

<% 
    String targetResults = request.getContextPath() + "/user/viewResults.do?reqCode=display";
    String targetExportPDF 					= request.getContextPath() + "/user/exportDataCollection.do?reqCode=exportAutoProcRankAsPdf";
    String targetExportRTF 					= request.getContextPath() + "/user/exportDataCollection.do?reqCode=exportAutoProcRankAsRtf";
    String exportPDFTitle 					= "View PDF report";
    String exportRTFTitle 					= "View DOC report";
%>

<script language="javascript"> 

	function setValue(myfield,value)
	{
		myfield.value = value;
		return false;
	}
	
</script> 

<%-- BreadCrumbs bar --%>
<jsp:include page="/tiles/common/util/breadCrumbsBar.jsp" flush="true" />

<%-- To display messages --%>
<jsp:include page="/tiles/common/messages/info.jsp" flush="true" />

<layout:space/>

<%-- reports --%>
<layout:grid cols="1">
	<layout:row><%-- RTF report--%>
		<layout:link title="RTF report" href="<%= targetExportRTF %>" paramName="autoProcRankingForm" paramProperty="sessionId" styleClass="FIELD">
			<img src="<%=request.getContextPath()%>/images/Word.png" border="0" onmouseover="return overlib('Export this table as DOC');" onmouseout="return nd();"/>
				<%= exportRTFTitle %>
		</layout:link>
				
		<%-- PDF report--%>
		<layout:link title="PDF report" href="<%= targetExportPDF %>" paramName="autoProcRankingForm" paramProperty="sessionId" styleClass="FIELD">
			<img src="<%=request.getContextPath()%>/images/pdf.png" border="0" onmouseover="return overlib('Export this table as PDF');" onmouseout="return nd();"/>
				<%= exportPDFTitle %>
		</layout:link>
	</layout:row>
</layout:grid>

<%-- Empty table --%>
<logic:empty name="autoProcRankingForm" property="autoProcRankingValue">
           <h4>No&nbsp;Data Collection&nbsp;have&nbsp;been&nbsp;selected</h4>
</logic:empty>

<%-- Not empty table --%>
<logic:notEmpty name="autoProcRankingForm" property="autoProcRankingValue">
<html:form action="/user/autoProcRanking">
	<html:hidden property="reqCode" value="update"/>
	<html:hidden property="actionName" value="none"/>
	<html:hidden name="autoProcRankingForm" property="sortOrder" />

	<layout:grid cols="1" borderSpacing="10">
		
		
		<%-- Ranking table --%>
		<layout:column >
		<layout:form action="/user/autoProcRanking.do" reqCode="rank">
		<layout:panel key="Auto Proc Ranking (Rank - Value)" align="center" styleClass="PANEL">
		<TR><TD>
		
		
		<table cellspacing="1" cellpadding="1" border="0" width="99%" align="CENTER" class="LIST">
			
			<%-- Headers --%>
			<tr>
			
				<%-- Select sample checkbox --%>
				<th class="LIST">Select<br>Sample</th>
				
			
				<%-- Image Prefix --%>
				<th class="LIST" ><table width="100%" border="0"><tbody><tr>
					<td><input name="Action" src="../images/sort.gif" onclick="setValue(this.form.actionName,'sortOnImagePrefix');" onmouseover="return overlib('Sort column');" onmouseout="return nd();" type="image"></td>
					<td>Image Prefix</td>
				</tr></tbody></table></th>
				
				<%-- Run number --%>
				<th class="LIST">Run No</th>
				
				<%-- Start Time --%>
				<th class="LIST" ><table width="100%" border="0"><tbody><tr>
					<td><input name="Action" src="../images/sort.gif" onclick="setValue(this.form.actionName,'sortOnStartTime');" onmouseover="return overlib('Sort column');" onmouseout="return nd();" type="image"></td>
					<td>Start<br>time</td>
				</tr></tbody></table></th>
				
				<%-- Space Group --%>
				<th class="LIST" ><table width="100%" border="0"><tbody><tr>
					<td><input name="Action" src="../images/sort.gif" onclick="setValue(this.form.actionName,'sortOnSpaceGroup');" onmouseover="return overlib('Sort column');" onmouseout="return nd();" type="image"></td>
					<td>Space<br>Group</td>
				</tr></tbody></table></th>
				
				<%-- Unit Cells --%>
				<th class="LIST">UC<br>a</th>
				<th class="LIST">UC<br>b</th>
				<th class="LIST">UC<br>c</th>
				
				<%-- Separator --%>
				<th class="LIST"></th>
				
				<%-- Overall R factor --%>
				<th class="LIST" ><table width="100%" border="0"><tbody><tr>
					<td><input name="Action" src="../images/sort.gif" onclick="setValue(this.form.actionName,'sortOnOverallRFactorRank');" onmouseover="return overlib('Sort column');" onmouseout="return nd();" type="image"></td>
					<td>R-factor</td>
				</tr></tbody></table></th>
				
				<%-- highest resolution --%>
				<th class="LIST" ><table width="100%" border="0"><tbody><tr>
					<td><input name="Action" src="../images/sort.gif" onclick="setValue(this.form.actionName,'sortOnHighestResolutionRank');" onmouseover="return overlib('Sort column');" onmouseout="return nd();" type="image"></td>
					<td>Highest<br>Resolution</td>
				</tr></tbody></table></th>
				
				<%-- Completeness --%>	
				<th class="LIST" ><table width="100%" border="0"><tbody><tr>
					<td><input name="Action" src="../images/sort.gif" onclick="setValue(this.form.actionName,'sortOnCompletenessRank');" onmouseover="return overlib('Sort column');" onmouseout="return nd();" type="image"></td>
					<td>Completeness<br>&nbsp;&#176;</td>
				</tr></tbody></table></th>
				
				
				
				<%-- Total --%>
				<th class="LIST" ><table width="100%" border="0"><tbody><tr>
					<td><input name="Action" src="../images/sort.gif" onclick="setValue(this.form.actionName,'sortOnTotalRank');" onmouseover="return overlib('Sort on column');" onmouseout="return nd();" type="image"></td>
					<td>Total</td>
				</tr></tbody></table></th>
			</tr>
			
			<%-- Weight and Action --%>
			<tr>
			<td class="LIST" colspan=8>
			
			<th class="LIST"></th>

			<td class="LIST" nowrap onmouseover="return overlib('Set weight for this criterion<br>(0: ignored 10: highest),<br>then click on Rank button');" onmouseout="return nd();">
        		Wt:&nbsp;
        		<html:select size="1" name="autoProcRankingForm" property="weightOverallRFactor" styleClass="FIELD_COMBO">
					<html:options collection="weightValueList" property="value" labelProperty="label" />
				</html:select>
			</td>
			<td class="LIST" nowrap onmouseover="return overlib('Set weight for this criterion<br>(0: ignored 10: highest),<br>then click on Rank button');" onmouseout="return nd();">
        		Wt:&nbsp;
        		<html:select size="1" name="autoProcRankingForm" property="weightHighestResolution" styleClass="FIELD_COMBO">
					<html:options collection="weightValueList" property="value" labelProperty="label" />
				</html:select>
			</td>
			<td class="LIST" nowrap onmouseover="return overlib('Set weight for this criterion<br>(0: ignored 10: highest),<br>then click on Rank button');" onmouseout="return nd();">
        		Wt:&nbsp;
        		<html:select size="1" name="autoProcRankingForm" property="weightCompleteness" styleClass="FIELD_COMBO">
					<html:options collection="weightValueList" property="value" labelProperty="label" />
				</html:select>
			</td>
						
			
			<td class="LIST">
  			<div align=center>
    			<input class="FIELD_BUTTON" name="Action" type="submit" value="Rank" onclick="setValue(this.form.actionName,'Rank');" onmouseover="return overlib('Rank samples based on weighted criteria');" onmouseout="return nd();">
    		</div>
			</td>
			
			</tr>
	
			<%-- Rows --%>
			  <logic:iterate name="autoProcRankingForm" property="autoProcRankingValue" id="currInfo" indexId="index" type="ispyb.client.mx.ranking.autoProcRanking.AutoProcRankingVO">
						
	            <html:hidden name="autoProcRankingForm" property="<%= \"idList[\" + index + \"]\" %>" value="<%=currInfo.getDataCollectionId().toString()%>" />         	
				
				<%-- Swap row color on imagePrefix change --%>
				<logic:notEqual name="currInfo" property="imagePrefix" value="${previousImagePrefix}" >
					<bean:define id="styleTDtmp" value="${styleTD1}"/>
					<bean:define id="styleTD1" value="${styleTD2}"/>
					<bean:define id="styleTD2" value="${styleTDtmp}"/>
				</logic:notEqual>	
				
				<tr class="LIST" onmouseover="this.className='LIST_OVER'" onmouseout="this.className='LIST'">

					<%-- Selection checkbox --%>
					<td> <html:checkbox property="<%=\"selectedItems[\" + currInfo.getDataCollectionId() + \"]\" %>" value="on"/> </td>
					
					
					<%-- Image Prefix 
					<td nowrap> --%>
					<td nowrap> 
					<div align=left>
					<html:link href="<%=targetResults + \"&dataCollectionId=\" + currInfo.getDataCollectionId()+\"&tabKeyResult=Characterisation results\"%>" styleClass="LIST">
						<bean:write name="currInfo" property="imagePrefix" />
					</html:link>
					</div>
					</td>
					
					<%-- Run number --%>
					<td><bean:write name="currInfo" property="dataCollectionNumber"/></td>
					
					<%-- Start Time --%>
					<td>
						<logic:empty name="breadCrumbsForm" property="selectedSession">
							<bean:write name="currInfo" property="startTime" format="dd-MM-yyyy HH:mm:ss"/>
						</logic:empty>
						<logic:notEmpty name="breadCrumbsForm" property="selectedSession">
							<bean:write name="currInfo" property="startTime" format="HH:mm:ss"/>
						</logic:notEmpty>
					</td>
					
					<%-- Space Group --%>
					<td><bean:write name="currInfo" property="spaceGroup"/></td>
					
					<%-- Unit Cells --%>
					<td><bean:write name="currInfo" property="unitCell_a" format="0"/></td>
					<td><bean:write name="currInfo" property="unitCell_b" format="0"/></td>
					<td><bean:write name="currInfo" property="unitCell_c" format="0"/></td>
					
					<%-- Separator --%>
					<th class="LIST"></th>
					
					<%-- Overall R-factor --%>
					<logic:notEqual name="currInfo" property="overallRFactorRank" value="1" ><bean:define id="cellStyle" value=""/></logic:notEqual>	
					<logic:equal name="currInfo" property="overallRFactorRank" value="1" ><bean:define id="cellStyle" value="TOP_RANK"/></logic:equal>	
					<logic:empty name="currInfo" property="overallRFactorValue"><bean:define id="cellStyle" value="NULL_RANK"/></logic:empty>	
					<td class="${cellStyle}">
			      		<table width="100%" border="0"><tbody><tr>
							<td class="SPLIT">#<bean:write name="currInfo" property="overallRFactorRank" format="0"/></td>
	            			<td class="SPLIT"><i><bean:write name="currInfo" property="overallRFactorValue" format="0.00"/></i></td>
            			</tr></tbody></table>
					</td>
			      	
			      	<%-- Highest resolution --%>	
			      	<logic:notEqual name="currInfo" property="highestResolutionRank" value="1" ><bean:define id="cellStyle" value=""/></logic:notEqual>	
					<logic:equal name="currInfo" property="highestResolutionRank" value="1" ><bean:define id="cellStyle" value="TOP_RANK"/></logic:equal>	
					<logic:empty name="currInfo" property="highestResolutionValue"><bean:define id="cellStyle" value="NULL_RANK"/></logic:empty>	
					<td class="${cellStyle}">
						<table width="100%" border="0"><tbody><tr>
							<td class="SPLIT">#<bean:write name="currInfo" property="highestResolutionRank" format="0"/></td>
	            			<td class="SPLIT"><i><bean:write name="currInfo" property="highestResolutionValue" format="0.00"/></i></td>
            			</tr></tbody></table>
					</td>
					
					<%-- Completeness --%>	
					<logic:notEqual name="currInfo" property="completenessRank" value="1" ><bean:define id="cellStyle" value=""/></logic:notEqual>	
					<logic:equal name="currInfo" property="completenessRank" value="1" ><bean:define id="cellStyle" value="TOP_RANK"/></logic:equal>	
					<logic:empty name="currInfo" property="completenessValue"><bean:define id="cellStyle" value="NULL_RANK"/></logic:empty>	
					<td class="${cellStyle}">
						<table width="100%" border="0"><tbody><tr>
							<td class="SPLIT">#<bean:write name="currInfo" property="completenessRank" format="0"/></td>
	            			<td class="SPLIT"><i><bean:write name="currInfo" property="completenessValue" format="0.00"/></i></td>
            			</tr></tbody></table>
					</td>
					
					
		
					<%-- Total --%>
					<logic:notEqual name="currInfo" property="totalRank" value="1" ><bean:define id="cellStyle" value=""/></logic:notEqual>	
					<logic:equal name="currInfo" property="totalRank" value="1" ><bean:define id="cellStyle" value="TOP_RANK"/></logic:equal>	
					<logic:empty name="currInfo" property="totalScore"><bean:define id="cellStyle" value="NULL_RANK"/></logic:empty>	
					<td class="${cellStyle}">
						<table width="100%" border="0"><tbody><tr>
							<td class="SPLIT">#<bean:write name="currInfo" property="totalRank" format="0"/></td>
	            			<td class="SPLIT"><i><bean:write name="currInfo" property="totalScore" format="0"/>&nbsp;%</i></td>
            			</tr></tbody></table>
					</td>
	
				</tr>
				
				<bean:define id="previousImagePrefix" value="<%=currInfo.getImagePrefix()%>"/>
			</logic:iterate>
		
		</table>
		
		<%-- Action buttons --%>
		<br>
		<div align=center>
	  		<input name="Action" type="submit" value="Chart" onclick="setValue(this.form.actionName,'ComparisonFlexChart');" onmouseover="return overlib('Draw comparison chart for selected samples');" onmouseout="return nd();"> 
     		<input name="Action" type="submit" value="Rank" onclick="setValue(this.form.actionName,'Rank');" onmouseover="return overlib('Rank samples based on weighted criteria');" onmouseout="return nd();">
			
		</div>
		</TD></TR>
		</layout:panel>
		</layout:form>
		</layout:column>
	</layout:grid>
</html:form>
</logic:notEmpty>