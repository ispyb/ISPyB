//
//Javascript Loading Control
//

	document.write('<style type="text/css">');
	document.write('.loadingStyle { FONT-WEIGHT: bold; FONT-SIZE: 14px; LINE-HEIGHT: normal; FONT-STYLE: normal; FONT-FAMILY: Verdana, Arial, Helvetica, sans-serif }')
	document.write('</style>')

	isNS4 = (document.layers) ? true : false;
	isIE4 = (document.all && !document.getElementById) ? true : false;
	isIE5 = (document.all && document.getElementById) ? true : false;
	isNS6 = (!document.all && document.getElementById) ? true : false;
	var allowLoading = (isIE4 || isIE5 || isNS6);
	var isIE = isIE4 || isIE5;
	
	function hide_elements(tagName, currentObject)
	{
		windowed_element_visibility(tagName, -1, currentObject)
	}
	
	function show_elements(tagName, currentObject)
	{
		windowed_element_visibility(tagName, +1, currentObject)
	}
	
	function windowed_element_visibility(tagName, change, currentObject)
	{
		var els = document.getElementsByTagName(tagName)
		var i
		var rect = new element_rect(document.getElementById(currentObject))
		//alert("["+rect.top+","+rect.left+","+rect.width+","+rect.height+"]");
		var elsLength = els.length;
		for (i=0; i < elsLength; i++)
		{
			var el = els.item(i)
			if (elements_overlap(el, rect))
			{
				//alert("Overlap")
				if (el.visLevel)
					el.visLevel += change
				else
					el.visLevel = change
				if (el.visLevel == -1 && change == -1)
				{
					el.visibilitySave = el.style.visibility;
					el.style.visibility = "hidden";
				}
				else if (el.visLevel == 0 && change == +1)
				{
					el.style.visibility = el.visibilitySave;
				}
			}
		}
	}
	
	function element_rect(el)
	{
		var left = 0
		var top = 0
		this.width = el.offsetWidth
		this.height = el.offsetHeight
		while (el)
		{
			left += el.offsetLeft
			top += el.offsetTop
			el = el.offsetParent
		}
		this.left = left;
		this.top = top;
	}
	
	function elements_overlap(el, rect)
	{
		var r = new element_rect(el);
		//alert("["+r.top+","+r.left+","+r.width+","+r.height+"]"+"["+rect.top+","+rect.left+","+rect.width+","+rect.height+"]");
		return ((r.left < rect.left + rect.width) && (r.left + r.width > rect.left) && (r.top < rect.top + rect.height) && (r.top + r.height > rect.top))
	}
	
	
	function noLoading() { 
	     if (allowLoading) {
	     	//loadingLayer.style.visibility = "hidden";
			document.getElementById("loadingLayer").style.visibility = "hidden";
	     } 
	}
	
	function loading(strMessage) {  
        if (allowLoading) {
				  //loadingLayer.style.visibility = "visible";
				  document.getElementById("loadingLayer").style.visibility = "visible";
               var oTD = document.getElementById("tdTableLoadingLayer");
               if ( strMessage == null) strMessage = "Loading...";
               if ( strMessage == "" ) strMessage = "Loading...";
               str = "";
               str += "<TABLE id=loadingTable class=loadingTable border=0><TBODY>";
               str += "<TR><TD align=center valign=middle width='200px' height='100px'>";
               str += "<FONT class=loadingText><B>"+strMessage+"</B></FONT>";
               str += "<BR><BR>";
               str += "<IMG src='../images/progressbar_2.gif' border=0>";
               str += "</TD></TR>";
               str += "</TBODY></TABLE>";
               oTD.innerHTML = str;
               
				  if (isIE) hide_elements("SELECT","loadingTable");

           }
   }	
 if (allowLoading) {
 	//loadingLayer.style.pixelHeight = document.body.clientHeight;
		if (isNS6)
			document.getElementById("loadingLayer").style.height = document.body.clientHeight;
		else 
			document.getElementById("loadingLayer").style.pixelHeight = document.body.clientHeight;
		
		
		//alert(document.getElementById("loadingLayer").style.pixelHeight);
		if (isNS6)
			document.getElementById("loadingLayer").style.width = document.body.clientWidth;
		else
			document.getElementById("loadingLayer").style.pixelWidth = document.body.clientWidth;
 }
	
	noLoading();