/*******************************************************************************
 * This file is part of ISPyB.
 * 
 * ISPyB is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * ISPyB is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with ISPyB. If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors : S. Delageniere, R. Leal, L. Launer, K. Levik, S. Veyrier, P.
 * Brenchereau, M. Bodin
 ******************************************************************************/
// Reference Panel (for a session or a list of collects)
function ReferencePanel(args) {
	var _this = this;
	this.width = 300;
	this.height = 250;
	if (args != null) {
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.height != null) {
			this.height = args.height;
		}
	}
	this.listOfRef = null;
}


// returns the list of references
ReferencePanel.prototype.getListOfReference = function() {
	return this.listOfRef;
};


// builds and returns the main panel
ReferencePanel.prototype.getPanel = function(data) {
	var _this = this;
	var links = "";
	var listIds = [];
	this.listOfRef = data.listOfReferences;
	if (data.referenceText) {
		links += "<h2>" + data.referenceText + "</h2>";
	}
	// build the links
	if (this.listOfRef) {
		for (var i = 0; i < this.listOfRef.length; i++) {
			var ref = this.listOfRef[i];
			links += "<a href=" + ref.referenceUrl + " target=_blank>" + ref.referenceName + "</a><br/> ";
			listIds.push(ref.referenceId);
		}
	}

	var url = data.contextPath+'/viewReference.do?reqCode=downloadListOfReference&listOfReference=' + listIds;
	_this.panel = Ext.widget({
				xtype : 'form',
				layout : 'fit',
				collapsible : true,
				frame : true,
				autoScroll : true,
				title : 'References',
				width : this.width,
				fieldDefaults : {
					msgTarget : 'side',
					labelWidth : 100
				},
				defaultType : 'textfield',
				style : {
					marginLeft : '10px', 
					marginTop : '10px'
				},
				items : [{
							xtype : 'panel',
							manageHeight : false,
							html : links
						}],
				buttons : [{
							xtype : 'button',
							text : 'Download as BibTeX',
							href : url,
							target : '_blank',
							hrefTarget : '_blank'
						}]

			});

	return _this.panel;
};