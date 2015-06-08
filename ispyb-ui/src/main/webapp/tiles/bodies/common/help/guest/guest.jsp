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
<META HTTP-EQUIV=REFRESH CONTENT="0; URL=<%=request.getContextPath()%>/menuSelected.do?topMenuId=-1&leftMenuId=-1&targetUrl=/security/logon.do">
<table  with="100%">
<tr>
<td width="80%">
<h1>Welcome to ISPyB environment.</h1>
</td>
<td width="20%"  BGCOLOR="#EEEEEE">
<%--
<p align="center">
	<a href="http://www.mozilla.org/products/firefox/" 
	title="Get Firefox"><img src="<%=request.getContextPath()%>/images/firefox.jpg" border=0><br>Best viewed with Firefox </a></p>
--%>
<p align="center">
	Click here to &nbsp;
	<a href="<%=request.getContextPath()%>/menuSelected.do?topMenuId=-1&leftMenuId=-1&targetUrl=/security/logon.do" 
	title="Log On"><b><br>LOG ON</a></B></p>

</td>
</tr>
</table>

<i>This application is under development, so if you have any <a href="mailto:ispyb@esrf.fr">remarks or comments about it...</a></i>
<br>
ISPyB is a web application developed by <a href="http://www.bm14.ac.uk">eHTPX / BM14 Grenoble</a> and <a href="http://www.esrf.fr/exp_facilities/jsbg/jsbg_beamlines.html"> JSBG </a>(Joint Structural Biology group).
<br>JSBG is a group of persons working for both european institutes <a href="http://www.esrf.fr"> ESRF </a>and <a href="http://www.embl-grenoble.fr"> EMBL </a> in protein crystallography in Grenoble.
<br>ISPyB replaces the former <a href="http://www.esrf.fr/exp_facilities/jsbg/pxweb.html"> PXWEB </a> application so that you can do all what you were used to in PXWEB through ISPyB, with other features.
<br>
<br>If you are a User, ISPyB will provide you with an interface:
<br>
<ul>
<li>to connect from outside ESRF and submit a new "electronic" shipment containing dewars, containers and samples, </li>
<li>to enter information about your proteins samples,</li>
<li>to view/edit reports about your collected data,</li>
<li>to view experiment parameters of collection. </li>
</ul>
<br>If you are an industrial user submitting dewars, ISPyB will provide you with an interface:
<ul>
<li>to connect from outside ESRF and submit a new "electronic" shipment containing dewars, containers and samples. </li>
<li>to view all the samples data of the shipments already submitted, </li>
<li>to view collection reports.</li>
</ul>
