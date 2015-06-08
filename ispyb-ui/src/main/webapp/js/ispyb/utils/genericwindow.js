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
function GenericWindow(args) {
	this.title = "title";
	this.width = 700;
	this.height = 500;

	this.close = false;
	this.draggable = true;
	this.modal = true;

	if (args != null) {
		if (args.actions != null) {
			this.actions = args.actions;
		}
		if (args.form != null) {
			this.form = args.form;
		}
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.modal != null) {
			this.modal = args.modal;
		}

		if (args.height != null) {
			this.height = args.height;
		}
		if (args.title != null) {
			this.title = args.title;
		}
		if (args.form != null) {
			this.form = args.form;
		}
		if (args.close != null) {
			this.close = args.close;
		}
		if (args.draggable != null) {
			this.draggable = args.draggable;
		}

	}
	/** Events * */
	this.onSaved = new Event(this);
	this.onCancelled = new Event(this);
}

GenericWindow.prototype.getButtons = function() {
	var _this = this;

	if (this.close) {
		return [{
					text : 'Close',
					handler : function() {
						_this.panel.close();
					}
				}];
	} else {
		return [{
					text : 'Save',
					handler : function() {
						_this.save();

					}
				}, {
					text : 'Cancel',
					handler : function() {
						_this.cancel();
					}
				}];
	}
};

GenericWindow.prototype.save = function() {
	alert("Method save of GenerciWindow class is abstract");
};


GenericWindow.prototype.cancel = function() {
	_this.panel.close();
};


GenericWindow.prototype._postRender = function(data) {

};

GenericWindow.prototype.draw = function(data) {
	this._render(data);
};

GenericWindow.prototype.refresh = function(data) {
	this.data = data;
	this.form.refresh(data);
};

GenericWindow.prototype._render = function(data) {
	this.data = data;
	var _this = this;
	if (this.panel == null) {
		this.panel = Ext.create('Ext.Window', {
					id : this.id,
					title : this.title,
					resizable : true,
					constrain : true,
					border : 1,
					modal : this.modal,
					frame : false,
					draggable : this.draggable,
					closable : true,
					autoscroll : true,
					layout : {
						type : 'vbox',
						align : 'stretch'
					},
					items : this.form.getPanel(data),
					width : this.width,
					height : this.height,
					buttonAlign : 'right',
					buttons : this.getButtons(),
					listeners : {
						scope : this,
						minimize : function() {
							this.panel.hide();
						},
						destroy : function() {
							delete this.panel;
						}
					}
				});
		this.panel.setLoading();
	}

	this.panel.show();
	this._postRender();
};