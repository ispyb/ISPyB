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
 * Brenchereau, M. Bodin,  A. De Maria Antolinos
 ******************************************************************************/

function ErrorPanel(args) {
	var _this = this;
	this.width = 600;
	this.height = 200;

	this.targetId = null;
	this.errors = null;

	if (args != null) {
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.height != null) {
			this.height = args.height;
		}
		if (args.targetId != null) {
			this.targetId = args.targetId;
		}
		if (args.errors != null) {
			this.errors = args.errors;
		}
	}

	this.getPanel();
}

ErrorPanel.prototype.getPanel = function() {
	var _this = this;
	var items = [];
	if (_this.errors) {
		_this.height = 0;
		for (var i = 0; i < _this.errors.length; i++) {
			items.push({
						xtype : 'displayfield',
						name : 'error' + i,
						fieldLabel : 'Error',
						value : _this.errors[i]
					});
			_this.height += 50;
		}
	}

	_this.panel = Ext.widget({
				xtype : 'form',
				layout : {
					type : 'fit'
					// align: 'stretch' // Child items are stretched to full
					// width
				},
				collapsible : false,
				frame : true,
				renderTo : _this.targetId,
				border : 0,
				//width : _this.width,
				//height : _this.height,
				fieldDefaults : {
					msgTarget : 'side',
					labelWidth : 90
				},
				defaultType : 'textfield',
				items : items

			});
	return _this.panel;
};