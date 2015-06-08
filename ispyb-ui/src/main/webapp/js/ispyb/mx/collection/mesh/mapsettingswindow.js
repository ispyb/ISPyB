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

MapSettingsWindow.prototype.getButtons = GenericWindow.prototype.getButtons;
MapSettingsWindow.prototype._render = GenericWindow.prototype._render;
MapSettingsWindow.prototype._postRender = GenericWindow.prototype._postRender;

// parameters of the mesh map that can be set by the user. Parent is a Generic Window
function MapSettingsWindow(args) {
	this.title = "Map Settings";
	this.height = 200;

	if (args != null) {
		if (args.actions != null) {
			this.actions = args.actions;
		}
	}
	this.onSavedButtonClicked = new Event(this);
	this.form = new MapSettingsPanel();
	GenericWindow.prototype.constructor.call(this, {
				form : this.form,
				title : this.title,
				height : this.height
			});

}

MapSettingsWindow.prototype.getButtons = function() {
	return [];
};

// cancel click: close panel
MapSettingsWindow.prototype.cancel = function() {
	this.panel.close();
};

// save click: save parameters
MapSettingsWindow.prototype.save = function() {
	var params = {
		mapTitle : '',
		maxValue : 0,
		minValue : 0
	};
	params.mapTitle = this.form.getMapTitle();
	params.maxValue = this.form.getMaxValue();
	//params.minValue = this.form.getMinValue();
	params.minValue = 0;
	this.onSaved.notify(params);
	this.panel.close();
};

// display the window
MapSettingsWindow.prototype.draw = function(title, maxValue, minValue) {
	var _this = this;
	var data = {
			mapTitle : title,
			maxValue : maxValue,
			minValue : minValue
		};
	this._render(data);
};

// return the buttons
MapSettingsWindow.prototype.getButtons = function() {
	var _this = this;
	return [{
				text : 'Save',
				handler : function() {
					_this.save();

				}
			}, {
				text : 'Cancel',
				handler : function() {
					_this.panel.close();
				}
			}];
};