function CalendarWidget(args) {
	this.height = 740;

	if (args != null) {
		if (args.height != null) {
			this.height = args.height;
		}
	}

	this.onClick = new Event();
}

CalendarWidget.prototype.loadData = function(data) {
	this.events = [];

	for ( var i = 0; i < data.length; i++) {
		var date = moment(data[i].creationDate);
		var textColor = 'black';

		if (data[i].status == "FINISHED") {
			textColor = 'green';
		}
		if (data[i].status == "ABORTED") {
			textColor = 'red';
		}
		this.events.push({
			title : data[i].name,
			start : date.format("YYYY-MM-DD HH:mm:ss"),
			end : date.format("YYYY-MM-DD HH:mm:ss"),
			date : date,
			allDay : false,
			color : textColor,
			className : date.format("YYYY-MM-DD")
		});
	}

};

CalendarWidget.prototype.draw = function(targetId) {
	var _this = this;
	$('#' + targetId).fullCalendar({
		eventClick : function(calEvent, jsEvent, view) {
			_this.onClick.notify(calEvent.className[0]);

		},
		contentHeight : _this.height,
		header : {
			left : 'prev,next today',
			center : 'title',
			right : 'month,basicWeek,basicDay'
		},
		editable : false,
		events : this.events
	});

};
