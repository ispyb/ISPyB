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
<%@page import="ispyb.common.util.Constants"%>

<h2> What you can do with ISPyB now</h2>
<i>This application is under development, so if you have any remarks or comments about it, please send an <a href="<%=request.getContextPath()%>/user/SendMailAction.do?reqCode=display">email.</a></i>
<br>
Currently, the ISPyB application provides an interface that allows users to:<ul>
<li> enter information about the proteins samples : 
this information is stored in the <%=Constants.DB_NAME%> database so that the data collection using <%=Constants.BCM_NAME%> can be linked to the samples information ;</li> 
<li> view/edit reports about the collected data : these reports can be selected by date or by sample name</li>
<li> display of current data collection with access to images</li>
<li> search for data collected previously</li>
</ul>
ISPyB also allows secure access for the Operation Managers and Local Contacts to: <ul>
<li> view information about the beamline scheduling : in this case, information is coming directly from User Office database</li> 
<li> view/edit reports about beamlines and users</li>
<li> create statistics regarding beamtime usage : only data stored in the Experiment database is used for these statistics.</li>
</ul>
ISPyB is currently being used at the <%=Constants.SITE_NAME%> to generate reports for industrial activity
