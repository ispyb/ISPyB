<%--------------------------------------------------------------------------------------------------
This file is part of ISPyB BCR module.

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

Contributors : S. Delageniere, R. Leal, L. Launer, P. Brenchereau, M. Bodin
--------------------------------------------------------------------------------------------------%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<%--
    Redirect default requests to Welcome global ActionForward.
    By using a redirect, the user-agent will change address to match the path of our Welcome ActionForward. 
--%>
<logic:redirect forward="welcomeBCR"/>

