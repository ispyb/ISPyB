/* WebAppers Progress Bar, version 0.2
* (c) 2007 Ray Cheung
*
* WebAppers Progress Bar is freely distributable under the terms of an Creative Commons license.
* For details, see the WebAppers web site: http://www.Webappers.com/
*
/*--------------------------------------------------------------------------*/


/* Deleted unused functions - Sam*/


var initial = -61;
var imageWidth= 122;
var eachPercent = (imageWidth/2)/100;

function progressbar ( id, percentage1, percentage2, percentage3)
{	
		
    var percentageWidth1 = eachPercent * percentage1;
    var actualWidth1 = initial + percentageWidth1 ;
    var colour1 = percentage1 < 85 ? 4 : 1;
    
    var percentageWidth2 = eachPercent * percentage2;
    var actualWidth2 = initial + percentageWidth2 ;
    var colour2 = percentage2 < 85 ? 4 : 1;
    
    var percentageWidth3 = eachPercent * percentage3;
    var actualWidth3 = initial + percentageWidth3 ;
    var colour3 = percentage3 < 85 ? 4 : 1;
    
    if(!(percentage1 == 0 && percentage2 == 0 && percentage3 == 0)){
    	document.write('<img id="'+id+'" src="../images/percentImage_small.png" title="'+percentage1+'% (inner)" alt="'+percentage1+'% (inner)" class="percentImage'+ colour1 + '" style="background-position: '+actualWidth1+'px 0pt; "/><br/>');
    	document.write('<img id="'+id+'" src="../images/percentImage_small.png" title="'+percentage2+'% (outer)" alt="'+percentage2+'% (outer)" class="percentImage'+ colour2 + '" style="background-position: '+actualWidth2+'px 0pt; "/><br/>');
    	document.write('<img id="'+id+'" src="../images/percentImage_small.png" title="'+percentage3+'% (overall)" alt="'+percentage3+'% (overall)" class="percentImage'+ colour3 + '" style="background-position: '+actualWidth3+'px 0pt; "/><br/>');
    }

}