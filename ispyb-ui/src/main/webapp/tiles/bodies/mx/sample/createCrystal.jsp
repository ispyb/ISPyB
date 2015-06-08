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

<layout:skin includeScript="true" />
<script src="<%=request.getContextPath()%>/js/external/overlib/overlib.js"></script>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>

<script>
function FillRestrictions(latticeChanged) {
	var className 	= document.getElementsByName("geometryClass")[0].value;
	var a			= document.getElementsByName("info.cellA")[0];
	var b			= document.getElementsByName("info.cellB")[0];
	var c			= document.getElementsByName("info.cellC")[0];
	var alpha		= document.getElementsByName("info.cellAlpha")[0];
	var beta		= document.getElementsByName("info.cellBeta")[0];
	var gamma		= document.getElementsByName("info.cellGamma")[0];
	
	if (latticeChanged=="")
		{
		a.value			="";
		b.value			="";
		c.value			="";
		alpha.value		="";
		beta.value		="";
		gamma.value		="";
		}
	
	
	switch (className)
		{
		case "Primitive triclinic":
									break;
		case "Primitive monoclinic":
									alpha.value = 90;	
									gamma.value = 90;
									break;
		case "Centred monoclinic":
									alpha.value = 90;
									gamma.value = 90;
									break;
		case "Primitive orthohombic":
									alpha.value = 90;
									beta.value	= 90;
									gamma.value = 90;
									break;
		case "C-centred orthohombic":
									alpha.value = 90;
									beta.value	= 90;
									gamma.value = 90;
									break;
		case "I-centred orthohombic":
									alpha.value = 90;
									beta.value	= 90;
									gamma.value = 90;
									break;
		case "F-centred orthohombic":
									alpha.value = 90;
									beta.value	= 90;
									gamma.value = 90;
									break;	
		case "Primitive tetragonal":
									alpha.value = 90;
									beta.value	= 90;
									gamma.value = 90;
									if (latticeChanged=="a") b.value=a.value;
									if (latticeChanged=="b") a.value=b.value;
									break;
		case "I-centred tetragonal":
									alpha.value = 90;
									beta.value	= 90;
									gamma.value = 90;
									if (latticeChanged=="a") b.value=a.value;
									if (latticeChanged=="b") a.value=b.value;
									break;
		case "Primitive trigonal":
									alpha.value = 90;
									beta.value	= 90;
									gamma.value	= 120;
									if (latticeChanged=="a") b.value=a.value;
									if (latticeChanged=="b") a.value=b.value;
									break;
		case "Primitive hexagonal":
									alpha.value = 90;
									beta.value	= 90;
									gamma.value	= 120;
									if (latticeChanged=="a") b.value=a.value;
									if (latticeChanged=="b") a.value=b.value;
									break;
		case "Rhombohedral":
									alpha.value = 90;
									beta.value	= 90;
									gamma.value	= 120;
									if (latticeChanged=="a") b.value=a.value;
									if (latticeChanged=="b") a.value=b.value;
									break;
		case "Primitive cubic":
									alpha.value = 90;
									beta.value	= 90;
									gamma.value = 90;
									if (latticeChanged=="a") {b.value=a.value;c.value=a.value;}
									if (latticeChanged=="b") {a.value=b.value;c.value=b.value;}
									if (latticeChanged=="c") {a.value=c.value;b.value=c.value;}
									break;
		case "I-centred cubic":
									alpha.value = 90;
									beta.value	= 90;
									gamma.value = 90;
									break;
		case "F-centred cubic":
									alpha.value = 90;
									beta.value	= 90;
									gamma.value = 90;
									break;																
		}
}
</script>

<bean:define name="viewCrystalForm" property="theProteinId" id="spid" type="java.lang.Integer"/>

<layout:grid cols="1"  borderSpacing="5">

	<%-- Crystal --%>
	<layout:panel key="Create a new Crystal Form" align="left" styleClass="PANEL">

		<layout:form action="/user/createCrystal.do" reqCode="save">	

			<layout:row>
			<layout:select 	key="Please select a Protein Acronym<br>this field is required" 	property="theProteinId" 	styleClass="FIELD"   size="1" isRequired="true" value="<%=spid.toString()%>" >
						<%-- <layout:option value="Please select an acronym" /> --%>
						<layout:optionsCollection property="listProtein" label="acronym" value="proteinId"/>
			</layout:select>
			
			<logic:equal name="viewCrystalForm" property="allowedTocreateProtein" value="true">
				<layout:message key="or" styleClass="FIELD"/>
				<layout:submit reqCode="newProtein"><layout:message key="or create a new protein"/></layout:submit>
			</logic:equal>
			
			</layout:row>
			<layout:space/>


			<layout:row>
				<%-- Geometry Class and SpaceGroup --%>
				<layout:select key="Geometry Class" property="geometryClass" styleClass="OPTIONAL"  onclick="javascript:FillRestrictions('')" size="17">				
					<layout:option value=""/>
					<layout:options collection="listGeometryClass" property="geometryClassName" sourceOf="spaceGroup"/>
				</layout:select>
				
				<layout:select key="Space Group" property="spaceGroup" styleClass="OPTIONAL" size="12">
					<layout:optionsDependent collection="listGeometrySpaceGroups" dependsFrom="geometryClass"/>
				</layout:select>
				
				<layout:grid cols="3" styleClass="SEARCH_GRID">
						<layout:column>
							<layout:text 		key="a" 			property="info.cellA"	 		styleClass="OPTIONAL"	mode="E,E,E"	size="4" onchange="javascript:FillRestrictions('a')"/>																																																									
							<layout:text 		key="Alpha" 		property="info.cellAlpha"	 	styleClass="OPTIONAL"	mode="E,E,E"	size="4"/>																																																			
						</layout:column>
						<layout:column>
							<layout:text 		key="b" 			property="info.cellB"	 		styleClass="OPTIONAL"	mode="E,E,E"	size="4" onchange="javascript:FillRestrictions('b')"/>																																																		
							<layout:text 		key="Beta" 			property="info.cellBeta"	 	styleClass="OPTIONAL"	mode="E,E,E"	size="4"/>
						</layout:column>
						<layout:column>
							<layout:text 		key="c" 			property="info.cellC"	 		styleClass="OPTIONAL"	mode="E,E,E"	size="4" onchange="javascript:FillRestrictions('c')"/>
							<layout:text 		key="gamma" 		property="info.cellGamma"	 	styleClass="OPTIONAL"	mode="E,E,E"	size="4"/>
						</layout:column>				
					</layout:grid>			
			</layout:row>
			
			<layout:space/>

			<layout:row>
				<layout:text 		key="Morphology" 	property="info.morphology" 		styleClass="OPTIONAL"	mode="E,E,E"/>																										
				<layout:text 		key="Color" 		property="info.color"	 		styleClass="OPTIONAL"	mode="E,E,E"/>
				<layout:space/>
			</layout:row>	
			
			<layout:row>
				<layout:grid cols="3" styleClass="SEARCH_GRID">
					<layout:column>																																																			
						<layout:text 		key="Crystal size X" 			property="info.sizeX"	 		styleClass="OPTIONAL"	mode="E,E,E"	size="4"/>																																																				
					</layout:column>
					<layout:column>																																																			
						<layout:text 		key="Crystal size Y" 			property="info.sizeY"	 		styleClass="OPTIONAL"	mode="E,E,E"	size="4"/>																																																			
					</layout:column>
					<layout:column>
						<layout:text 		key="Crystal size Z" 			property="info.sizeZ"	 		styleClass="OPTIONAL"	mode="E,E,E"	size="4"/>
					</layout:column>
			   </layout:grid>
			</layout:row>
																																														
					
			<layout:space/>
			
			<layout:grid cols="2" styleClass="SEARCH_GRID">
				<layout:column>

				</layout:column>
				<layout:column>
					<layout:textarea	key="Comments" 		property="info.comments"	 	styleClass="OPTIONAL"	mode="E,E,E"	size="30" rows="4"/>
				</layout:column>
				
			</layout:grid>
																																																
			<layout:space/>
						
			<layout:row>
				<layout:reset/>
				<layout:submit reqCode="save"><layout:message key="Save"/></layout:submit>
			</layout:row>
      
		</layout:form>	
	</layout:panel>
</layout:grid>

<%-- Acknowledge Action --%>
<jsp:include page="/tiles/common/messages/info.jsp" flush="true" />

<%-- Errors --%>
<jsp:include page="/tiles/common/messages/error.jsp" flush="true" />
