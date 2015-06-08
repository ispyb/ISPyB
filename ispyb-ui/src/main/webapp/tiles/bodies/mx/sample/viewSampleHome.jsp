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

<br/>

<h1>  What you can do in the samples pages</h1>


  <h2>Create</h2> 
  <h4>New sample</h4>
Note that the <b> most recommended </b> way of creating new samples is to create first 1 shipment, then dewars, then containers and add samples to them.
<br>
<br>This sample will be attached to a protein and to a crystal form: information is retrieved from the samplesheets you submited to safety.

  <h4>New Crystal Form</h4>
To create new crystal forms you can use later when creating your samples.

<h2>View </h2>

 <h4> Proteins and crystal forms </h4>
To display all the proteins you have entered through samplesheets and the crystal forms associated. 
<br> Click on a protein acronym to retrieve the list of all samples coming from this protein.
<br> Click on crystalForm space group to retrieve the list of all samples coming from this crystal form.


<h4>All samples  </h4>
To display the list of all your samples
<br>Click on edit to add/update sample parameters after experiment
<br>Click on Diffraction plan to edit the diffraction plan parameters you want
(By default the parameters you submitted through samplesheets are kept).
<br>click on the sample name to have all the characteristics of this sample.



  <h2>Search</h2>  
Forms to search for a particular protein  or a particular sample.
   
  <h2>Update Database</h2>
If some protein acronyms are missing in the list when creating a sample, you can get them by updating ISPyB database.
