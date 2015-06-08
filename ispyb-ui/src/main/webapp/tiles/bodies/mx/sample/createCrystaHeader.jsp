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

<%@page import="java.util.Vector"%>

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
