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
// Result panel of the workflow 
function ResultPanel(args) {
	var _this = this;
	this.width = 1380;
	this.height = 700;

	if (args != null) {
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.height != null) {
			this.height = args.height;
		}
	}
}


// builds and returns the main panel
ResultPanel.prototype.getPanel = function(data) {
	var _this = this;
	var items = [];
	var activeTab = 0;
	if (data && data.workflowVO && data.workflowVO.resultFiles) {
		var nbResult = data.workflowVO.resultFiles.length;
		// Rank results in order to make tabs appear in the correct order
		for (var i = 0; i < nbResult; i++) {
			var title = data.workflowVO.resultFiles[i].directoryName;
		    if (title.indexOf("Snapshots") != -1) {
		        data.workflowVO.resultFiles[i]["rank"] = 10;
		    } else if (title.indexOf("Automesh") != -1) {
		        data.workflowVO.resultFiles[i]["rank"] = 20;
		    } else if (title.indexOf("Mesh_") != -1) {
		        data.workflowVO.resultFiles[i]["rank"] = 30;
		    } else if (title.indexOf("Line_") != -1) {
		        data.workflowVO.resultFiles[i]["rank"] = 40;
		    } else if (title.indexOf("html_") != -1) {
		        data.workflowVO.resultFiles[i]["rank"] = 50;
		    } else {
		        data.workflowVO.resultFiles[i]["rank"] = 60;
		    }
		}
		// Sort results
		data.workflowVO.resultFiles.sort(function(a,b) {
				var deltaRank = a.rank - b.rank;
				if (deltaRank == 0) {
					// If same rank then sort by directoryName
					listA = a.directoryName.split("_")
					listB = b.directoryName.split("_")
					if (listA.length==2 && listB.length==2) {
					 	valueA = parseInt(listA[1])
					 	valueB = parseInt(listB[1])
					 	deltaRank = valueA - valueB
					}		 
				}   
				return deltaRank; 
			} );
		for (var i = 0; i < nbResult; i++) {
			var webPanel = new WebPanel();
			var title = data.workflowVO.resultFiles[i].directoryName;
			if (title.indexOf("html_") != -1) {
				// Rename 'html' tab to 'Characterisation'
				title = "Characterisation_" + title.split("_")[1];
			}
			//var l = title.lastIndexOf("\\");
			//if (l != -1) {
			//	title = title.substring(0, l);
			//	l = title.lastIndexOf("\\");
			//	if (l != -1) {
			//		title = title.substring(l + 1);
			//	}
			//}
			items.push({
						tabConfig : {
							title : title
						},
						items : [webPanel.getPanel(data.workflowVO.resultFiles[i])]
					});
		}
		activeTab = items.length - 1;
	}

	var tabs = Ext.create('Ext.tab.Panel', {
				layout : 'fit',
				activeTab : activeTab,
				defaults : {
					bodyPadding : 0
				},
				items : items
			});

	_this.panel = Ext.create('Ext.Panel', {
				layout : 'fit',
				items : [tabs]
			});

	return _this.panel;
};