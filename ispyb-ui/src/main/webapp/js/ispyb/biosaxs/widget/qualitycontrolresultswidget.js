/**
 * It shows an slider for qualityThreshold and radiationDamageThreshold
 * 
 * @qualityThreshold between 0 and 1
 * @radiationDamageThreshold between 0 and 1
 * 
 * #onQualityChanged
 * #onRadiationDatamageChanged
 */

function QualityControlResultsWidget(args) {
	this.id = BUI.id();

	this.qualityThreshold = 0.25;
	this.radiationDamageThreshold = 0.25;

	if (args != null) {
		if (args.qualityThreshold != null) {
			this.qualityThreshold = args.qualityThreshold;
		}
		if (args.radiationDamageThreshold != null) {
			this.radiationDamageThreshold = args.radiationDamageThreshold;
		}
	}

	this.onQualityChanged = new Event(this);
	this.onRadiationDatamageChanged = new Event(this);
}

QualityControlResultsWidget.prototype.getPanel = function() {
	var _this = this;
	var sliderQuality = Ext.create('Ext.slider.Single', {
		width : 400,
		value : _this.qualityThreshold * 100,
		increment : 1,
		fieldLabel : "Quality (%)",
		labelWidth : 220,
		minValue : 0,
		maxValue : 100,
		listeners : {
			change : function(grid, value) {
				Ext.getCmp(_this.id + "_qualityField").setText('> ' + value + " %");
				_this.onQualityChanged.notify(value / 100);
			}
		}
	});

	var sliderRadiationDamage = Ext.create('Ext.slider.Single', {
		width : 400,
		value : _this.radiationDamageThreshold * 100,
		increment : 1,
		fieldLabel : "Radiation Damage/Inhomogenety (%)",
		labelWidth : 220,
		minValue : 0,
		maxValue : 100,
		listeners : {
			change : function(grid, value) {
				Ext.getCmp(_this.id + "_rdField").setText('> ' + value + " %");
				_this.onRadiationDatamageChanged.notify(value / 100);
			}
		}
	});

	return {
		xtype : 'container',
		flex : 1,
		border : false,
		layout : 'anchor',
		defaultType : 'textfield',
		items : [ {
			xtype : 'container',
			layout : 'hbox',
			items : [ sliderQuality, {
				xtype : 'label',
				id : _this.id + "_qualityField",
				labelWidth : 50,
				margin : '0 0 0 20',
				readOnly : true,
				text : '> ' + _this.qualityThreshold * 100 + ' %'
			}, {
				html : "<span style='font-style:italic;'> Guinier Quality <span style='font-weight:bold;'>Recommended value is > 70.</span></span>",
				margin : '0 0 0 20',
				border : 0,
				padding : "0 100 0 0 "
			} ]
		}, {
			xtype : 'container',
			layout : 'hbox',
			items : [ sliderRadiationDamage, {
				xtype : 'label',
				id : _this.id + "_rdField",
				labelWidth : 50,
				margin : '0 0 0 20',
				readOnly : true,
				text : '> ' + _this.radiationDamageThreshold * 100 + ' %'
			}, {
				html : "<span style='font-style:italic;'> Percentage of frames merged <span style='font-weight:bold;'>Recommended value is > 70.</span></span>",
				margin : '0 0 0 20',
				border : 0,
				padding : "0 100 0 0 "
			} ]
		} ]
	};
};

QualityControlResultsWidget.prototype.input = function() {

};

QualityControlResultsWidget.prototype.test = function(targetId) {
	var qualityControlResultsWidget = new QualityControlResultsWidget();
	var panel = qualityControlResultsWidget.getPanel();
	Ext.create('Ext.panel.Panel', {
		width : 1000,
		items : [ panel ],
		renderTo : targetId
	});
};
