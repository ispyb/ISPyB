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

<h1>About ISPyB</h1>
<h2> ISPyB project </h2>
<i>This application is under development, so if you have any remarks or comments about it, please send us an <a href="mailto:<%=Constants.MAIL_TO%>">email.</a></i>
The ISPyB project is concerned with developing web pages 
(it is based on Java technology : J2EE) in order to allow users to view the experiment status, 
visualise and access the data and control the data processing itself.
<br>As the interface is based around web formatted pages, this beamline management could be carried out remotely from the site.
<br>
A key part of the software structure is a database (<%=Constants.DB_NAME%>) called “Experiment database”, which records the parameters set during an experiment and is also planned to record the associated data processing statistics. It could be possible to export part of the database specific to a certain user and make that available for home laboratory use as a data management tool.
Note that to be able to store experiment parameters and data in this database, the user has to log with his name in the ProDC software, if not, the records will not be stored with the corresponding user information.
<h2> What you can do with ISPyB now</h2>
Currently, ISPyB application provides an interface that allows users to:<ul>
<li> enter information about the crystal samples : 
this information is stored in the <%=Constants.DB_NAME%> database so that the data collection using <%=Constants.BCM_NAME%> can be linked to the samples information ;</li> 
<li> view/edit reports about the collected data : theses reports can be selected by date or by sample name</li>
<li> display of current data collection with access to images</li>
<li> search for data collected previously</li>
<li> display results and files produced by DNA software </li> 
</ul>
ISPyB also allows secure access for the Operation Managers, Local Contacts and Stores to: <ul>
<li> view information about the dewars and their location at <%=Constants.SITE_NAME%></li> 
</ul>
ISPyB is currently being used at the <%=Constants.SITE_NAME%> to generate reports for industrial activity
