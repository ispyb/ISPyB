/*******************************************************************************
 * This file is part of ISPyB.
 * 
 * ISPyB is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ISPyB is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with ISPyB.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors : S. Delageniere, R. Leal, L. Launer, K. Levik, S. Veyrier, P. Brenchereau, M. Bodin, A. De Maria Antolinos
 ******************************************************************************/

// add functions to be execute on the same event, onload here
function addEvent(obj, event, fct) {
    if (obj.attachEvent) //IE?
        obj.attachEvent("on" + event, fct);
    else
        obj.addEventListener(event, fct, true);
}


function onLoadPage(){
	// check archived status every 59s - stop after 5h
	var startTime = new Date().getTime();
	var interval = setInterval(
		function(){
			if(new Date().getTime() - startTime > 18000000){
				clearInterval(interval);
				return;
		}
		archivedRefresh();
	}, 59000);   
}



// ajax request to update the archiving status
function archivedRefresh(){
	var url = "viewDataCollection.do?reqCode=updateArchiving";
	$.ajax( {
		type: 'get',
		url: url,
		success: function( r ) {
			var pyarchArchivedDiv = document.getElementById('pyarchArchivedDiv');
			pyarchArchivedDiv.innerHTML = r;
		}
	} );
}