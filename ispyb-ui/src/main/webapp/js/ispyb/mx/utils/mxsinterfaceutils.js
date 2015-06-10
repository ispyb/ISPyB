var MXUI = {
	//interval : 60000,
		interval : 40000,

	getShippingURL : function(shippingId) {
		return '/ispyb/user/viewProjectList.do?reqCode=display&menu=shipment&shippingId=' + shippingId;
	},
	
	getJpegByImageIdandPath : function(imageId, path) {
		return "<div style='white-space:normal !important;word-wrap: break-word;'>" + 
		"<a  target='_blank' href='viewResults.do?reqCode=viewJpegImage&imageId=" + 
		imageId + 
		"'  styleClass='LIST' >" + 
		"<img width='110' height='110' src='imageDownload.do?reqCode=getImageJpgFromFile&file=" + 
		path + 
		"' border='0' alt='Click to zoom the image'>" + 
		"</a></div>";
	},
	
	getSnapshotByPath : function(path) {
		return "<div style='white-space:normal !important;word-wrap: break-word;'>" + 
		"<a  target='_blank' href='viewResults.do?reqCode=getJpegImageFromFile&file=" + 
		path + 
		"'  styleClass='LIST'>" + 
		"<img width='110' height='83' src='imageDownload.do?reqCode=getImageJpgFromFile&file=" + 
		path + 
		"' border='0' alt='Click to zoom the image' >" + 
		"</a></div>";
		
	//	var url = "viewResults.do?reqCode=getJpegImageFromFile&file=" + path;
	//	var event = "OnClick= window.open('" + url + "')";
	//	return '<img src=' + url + '   height="83" width="110" border="0" alt="Click to zoom the image'  + event + '>';
	},
	
	getGraphByPath : function(path) {

		return "<div style='white-space:normal !important;word-wrap: break-word;'>" + 
		"<a  target='_blank' href='viewResults.do?reqCode=getJpegImageFromFile&file=" + 
		path + "'  styleClass='LIST'>" + 
		"<img width='120' height='100' src='imageDownload.do?reqCode=getImageJpgFromFile&file=" + 
		path + "' border='0' alt='Click to zoom the image' >" + "</a></div>";
	},

	getURL : function() {
		return this.getBaseURL() + '?reqCode=getImage';

	},

};
