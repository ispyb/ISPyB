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

<%@page import="ispyb.common.util.Constants"%>

<h1> Prepare your experiment</h1>
<br>
<h2> First select the <a href="<%=request.getContextPath()%>/user/prepareExp.do?reqCode=selectShipment" 
	title="select the shipment to process">shipments</a> you want to process with</h2>
<h3>
All the samples belonging to this shipment will be pre-selected for data collection.</h3>
<h3>
Note that only <b>closed</b> shipments can be selected for processing.</h3>

<br>
<br>
<h2> Then <a href="<%=request.getContextPath()%>/user/fillSampleChanger.do?reqCode=display" 
	title="select the shipment to process">fill the sample changer</a>: assign a location in the sample changer to your containers</h2>
	<h3> This is useless if you are using Datamatrix code on your samples pins.</h3>
