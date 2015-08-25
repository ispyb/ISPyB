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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>	
<%@page import="ispyb.common.util.Constants"%>


<%
	String ispybMail = "ispyb@esrf.fr";
	if (Constants.SITE_IS_MAXIV()){
		ispybMail = "ispyb@maxlab.lu.se";
	} else if (Constants.SITE_IS_SOLEIL()){
		ispybMail = "ispyb@synchrotron-soleil.fr";
	} else if (Constants.SITE_IS_EMBL()){
		ispybMail = "ispyb@embl-hamburg.de";
	}	   
%>

<h2>ISPyB OVERVIEW</h2>
<p>Information System for Protein crYstallography Beamline: ISPyB is a joint development between the ESRF Joint Structural Biology group (JSBG), BM14 (e-HTPX), the EU funded SPINE project.

<c:choose>
	<c:when test="${SITE_ATTRIBUTE == 'MAXIV'}">
		<br>It was adapted to work at the MAX IV Laboratory.
		<br>ISPyB provides you with a management environment to keep track of samples and experiments at protein diffraction stations such as I911-3 at the MAX IV Laboratory.
	</c:when>
	<c:when test="${SITE_ATTRIBUTE == 'SOLEIL'}">
		<br>It was adapted to work at the SOLEIL Beamlines.
		<br>ISPyB provides you with a management environment to keep track of samples and experiments at SOLEIL on SWING, PROXIMA1 and PROXIMA2 beamlines.
	</c:when>
	<c:when test="${SITE_ATTRIBUTE == 'EMBL'}">
		<br>It was adapted to work at the EMBL Hamburg Beamlines.
		<br>ISPyB provides you with a management environment to keep track of samples and experiments at EMBL Hamburg on P12, P13 and P14 beamlines.
	</c:when>
	<c:otherwise>
		<br>ISPyB provides you with a truly managed environment for keeping track of your experiments at the ESRF
	</c:otherwise>
</c:choose>

</p>


<h2>Overview of available features:</h2>
<ul>
<li>Submission of Sample shipments</li>
<li>Management of Sample, Crystal and Protein information</li>
<li>Real time monitoring of your data collections (Diffraction images and Crystal snapshots, harvesting of output from data reduction programs, ...) </li>
<li>View experiment related information</li>
<li>Create and edit experiments reports</li>
<li>Export your experiments meta-data back to your home lab</li>
</ul>

<c:choose>
	<c:when test="${Constants.SITE_IS_MAXIV()}">
		<p>
		Feedback, comments and suggestions on ISPyB at the MAX IV Laboratory are welcome: <a href="mailto:<%=ispybMail%>"><%=ispybMail%></a>
		</p>
	</c:when>
	<c:otherwise>
		<p>
		Feedback, comments and suggestions are welcome: <a href="mailto:<%=ispybMail%>"><%=ispybMail%></a>
		</p>
	</c:otherwise>
</c:choose>
