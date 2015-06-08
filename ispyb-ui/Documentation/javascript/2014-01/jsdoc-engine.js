function JSDoc(json){
	this.setJson(json);
	
	
};


JSDoc.prototype.setJson = function(json){
	this.json = json;
	this.classes = this.getClasses(json);
	
};



JSDoc.prototype.getByClassName = function(className){    
    for (myClass in this.classes){
    	if (className == myClass){
    		/** Sorting methods **/
    		this.classes[myClass].methods.sort(function(a,b){	if (a.name < b.name )
													return -1;
										    	if (a.name > b.name )
										    			return 1;
										    	return 0;
											}
    		);
    		return this.classes[myClass];
    	}
    }
    return null;
};


JSDoc.prototype.isUpperCase = function(aCharacter){    
    return (aCharacter >= 'A') && (aCharacter <= 'Z');
};

JSDoc.prototype.getClassUnit = function(className){
	var index = 0;
	for ( var i = 0; i < className.length; i++) {
		if (this.isUpperCase(className[i])){
			index = i;
		}
	}
	var unit = className.substring(index, className.length);;
	if (this.units[unit] == null){
		this.units[unit] = new Array();
	} 
	return unit;
};

JSDoc.prototype.getClasses = function(){
	var classes = new Object();
	this.units = new Object();
	for ( var i = 0; i < this.json.length; i++) {
		var record = this.json[i];
		
		if (classes[record.className] == null){
			classes[record.className] = new Object();
			classes[record.className].file = record.file;
			classes[record.className].name = record.className;
			classes[record.className].methods = new Array();
			classes[record.className].constructor = new Array();
			classes[record.className].unit = this.getClassUnit(record.className);
			this.units[classes[record.className].unit].push(classes[record.className]);
		}
		if (record.type != null){
			if (record.type == "method"){
				classes[record.className].methods.push(record);
			}
		}
		else{
			classes[record.className].constructor.push(record);
		}
			
	}
	return classes;
};


function JSViewer(jsDoc){
	this.jsDoc = jsDoc;
	this.height = 700;
	this.id = BUI.id()
};

JSViewer.prototype._formatClassCommentsInHTMLByFeatures = function(comments, params, events, methods){
	var html = new String();
	html = html +  "<p class='doc_description'>" + comments + "</p>";
	if (params.length > 0){
		html = html +  "<p class='doc_params_header'>Constructor Parameters</p><table>";
		for ( var i = 0; i < params.length; i++) {
			var paramName = params[i].substr(0,params[i].indexOf(' '));
			var paramDesc = params[i].substr(params[i].indexOf(' ')+1);
			if (paramName == ""){
				paramName = paramDesc;
				paramDesc = "";
			}
			html = html + "<tr><td >" + BUI.getRectangleColorDIV('#FA58D0', 8, 8) + "</td><td><span class='doc_params'>" +paramName + "  </span><span class='params_desc'>" + paramDesc +"</span></td></tr>";
		}
		html = html +  "</table>";
	}
	

	if (methods.length > 0){
		html = html +  "<p class='doc_params_header'>Methods</p><table>";
		for ( var i = 0; i < methods.length; i++) {
			
			var square;
			if (methods[i].name.indexOf("_") == 0){
				square = BUI.getRectangleColorDIV('#FA58D0', 8, 8);
			}
			else{
				square = BUI.getRectangleColorDIV('#81BEF7', 8, 8);
			}
			
			html = html + "<tr><td >" + square + "</td><td><span class='doc_params' style='width:200px'>" +methods[i].name + " </span></td><td class='params_desc'>("+ methods[i].parameters + ")</td>";
			if (methods[i].comments != null){
				html = html + "<td class='params_desc' style='color:gray'>" + methods[i].comments + "</td>";
			}
			html = html +"</tr>";
		}
		html = html +  "</table>";
	}
	
	if (events.length > 0){
		html = html +  "<p class='doc_params_header'>Events</p><table>";
		for ( var i = 0; i < events.length; i++) {
			html = html + "<tr><td ><img src='http://i.msdn.microsoft.com/dynimg/IC279819.gif' /></td><td><span class='doc_params'>" +events[i] + " </span></tr>";
		}
		html = html +  "</table>";
	}
	
	html = html +  "<br /><br />";
	html = html +  "<p class='doc_params_header'>Testing</p><table>";
	return html;
};


JSViewer.prototype._parseCommentsHeader = function(comments){
	if (comments != null){
			var splited = comments.split(/([@|#])/g);
			
			var params = new Array();
			var events = new Array();
			var comment = "";
			var character = null;
			for ( var i = 0; i < splited.length; i++) {
				var line = splited[i].trim().replace(/\*/g, '');
				
				
				if (line.indexOf("@") != -1){
					character= "@";
				}
				if (line.indexOf("#") != -1){
					character= "#";
				}
				if (character == null){
					comment = comment + line;
				}
				else{
					if (character == "#"){
						if (line != "#"){
							events.push(line);
						}
					}
					if (character == "@"){
						if (line != "@"){
							params.push(line);
						}
					}
				}
			}
			return {
				comments	: comment,
				params		: params,
				events		: events
				
			}
	}
	
	return {
		comments	: "No comments",
		params		: [],
		events		: []
		
	}
	
};

JSViewer.prototype._formatClassCommentsInHTML = function(comments, methods){
	var parsed = this._parseCommentsHeader(comments);
	return this._formatClassCommentsInHTMLByFeatures(parsed.comments, parsed.params, parsed.events, methods);
};

JSViewer.prototype._getHTMCommentsPanel = function(comments, methods){
	var div = document.createElement("DIV");
	div.innerHTML = this._formatClassCommentsInHTML(comments, methods);
	return div.innerHTML;
};

JSViewer.prototype._getHTMClassHeader = function(jsClass){
	var html = "";
	html = html + "<span class='classHeaderName'>Class " + jsClass.name + "</span><br />";
	html = html + "<span class='unitHeaderName'>Package " + jsClass.unit + "</span><br />";
	html = html + "<span class='unitHeaderName'>Defined in " + jsClass.file + "</span><br />";
	return html;
};

JSViewer.prototype._getHTMMethodPanel = function(name, comments,targetId, jsClass){
	var html = this._getHTMClassHeader(jsClass);
	html = html + this._getHTMCommentsPanel(comments, jsClass.methods);
	html = html + "<br /><div>";
	html = html + "<div id='" + targetId + "code' class='vizCode' ></div><br/>";
	html = html + "<div id='" + targetId + "' class='vizTest'></div>";
	html = html + "</div>";
	return html;
};


function execute(codeTextAreaId, targetId){
	document.getElementById(targetId).innerHTML = "";
	eval(document.getElementById(codeTextAreaId).value);
};


JSViewer.prototype.formatTestClassFunction = function(text, targetId){
	var splitted = text.split("\n");
	var formatted = new String();
	for ( var i = 1; i < splitted.length - 1; i++) {
		formatted = formatted + splitted[i].replace("\t", "");
	}
	return formatted.replace("targetId", "'" + targetId + "'"); 
};

JSViewer.prototype._createCodeTextArea = function(name, targetId){
	var codeTextAreaId = this.id + name +  "_code_ex" + BUI.id();
	var title = document.createElement("span");
	title.setAttribute("class", "tryitbtn");
	title.setAttribute("onClick", "execute('" +  codeTextAreaId + "', '" + targetId +"')");
	title.appendChild(document.createTextNode("Try it yourself!"));
	
	var textArea = document.createElement("textarea");
	textArea.setAttribute("id",  codeTextAreaId);
	textArea.setAttribute("style", "font-size:11");
	textArea.setAttribute("rows", 10);
	textArea.setAttribute("cols", 180);
	
	if (this.object.test){
		var text = document.createTextNode(this.formatTestClassFunction(this.object.test.toString(), targetId));
		textArea.appendChild(text);
	}
	else{
		textArea.appendChild( document.createTextNode("Previsualization not available"));
	}
	
	return {
		title		: title,
		textArea	: textArea
		
	};
};

JSViewer.prototype.onClassTreeClick = function(record){
	var _this = this;
	var targetId = BUI.id() + '_viz_' + record.raw.text;
	var comments = "";
    if (record.raw.leaf){
    	var jsClass = this.jsDoc.getByClassName(record.raw.text);
    	if (jsClass["constructor"] != null){
    		if (jsClass["constructor"][0] != null){
    			if (jsClass["constructor"][0].comments != null){
    				comments = jsClass["constructor"][0].comments.replace(/\*/g, '');
    			}
    		}
    	}
    	Ext.getCmp('mainTabPanel').removeAll();
    	Ext.getCmp('mainTabPanel').insert({
			title			: jsClass.name,
			html			: _this._getHTMMethodPanel(jsClass.name, comments, targetId, jsClass),
			height			: 1000,
			autoScroll		: true,
			closable		: true,
			padding			: 10,
			listeners		: {
								afterrender : function(){
									/** getting input data if it exists **/
									try{
										eval("_this.object = new " + jsClass.name + "();")
										if (_this.object.input != null){
											if (_this.object.test){
												if (_this.object.width != null){
													document.getElementById(targetId).setAttribute("width", _this.object.width);
												}
												/** it renders the object **/
												_this.object.test(targetId);
												var codeElement = _this._createCodeTextArea(jsClass.name, targetId);
												document.getElementById(targetId + "code").appendChild(codeElement.title);
												document.getElementById(targetId + "code").appendChild(document.createElement("br"));
												document.getElementById(targetId + "code").appendChild(codeElement.textArea);
											}
										}
										else{
											var codeElement = _this._createCodeTextArea(jsClass.name, targetId);
											document.getElementById(targetId + "code").appendChild(codeElement.title);
											document.getElementById(targetId + "code").appendChild(document.createElement("br"));
											document.getElementById(targetId + "code").appendChild(codeElement.textArea);
										}
									}
									catch(e){
										alert("It has been not possible to dynamically make an instance of this object");
										
									}
								}
			}
    	});
    	
    	if (jsClass.constructor != null){
    		if (jsClass.constructor[0] != null){
    			var events = this._parseCommentsHeader(jsClass.constructor[0].comments).events;
    			var data = new Array();
    			for ( var i = 0; i < events.length; i++){
    				data.push({
    					name		: events[i]
    				});
				}
    			this.eventsStore.loadData(data);
    		}
    	}
      	this.attributesStore.loadData(jsClass.methods);
      	
      	
      	Ext.getCmp('mainTabPanel').setActiveTab(Ext.getCmp('mainTabPanel').items.length - 1);
    }
    else{
    	/** it is a packache **/
    	var interfaceMethod = new Object();
    	for ( var i = 0; i < record.childNodes.length; i++) {
    		var methods = this.jsDoc.getByClassName(record.childNodes[i].data.text).methods;
    		for ( var j = 0; j < methods.length; j++) {
    			if (interfaceMethod[methods[j].name] == null){
    				interfaceMethod[methods[j].name] = new Array();
    			}
    			interfaceMethod[methods[j].name].push(record.childNodes[i].data.text);
    		}
		}
    	
    
    	var html = "<h1>Interface</h1>";
    	html = html + "<table style='width:600px;'>" ;
    	html = html + "<tr ><th class='methodTableHeader'>Common methods</th>" ;
    	for ( var name in interfaceMethod) {
			var method = interfaceMethod[name];
			if (interfaceMethod[name].length == record.childNodes.length){
				html = html + "<tr class='interface'><td>" +name + "</td></tr>" ;
			}
		}
    	html = html + "</table><br />";
    	
    	
    	html = html + "<h1>Methods</h1>";
    	for ( var i = 0; i < record.childNodes.length; i++) {
    		var methods = this.jsDoc.getByClassName(record.childNodes[i].data.text).methods;
    		html = html + "<li class='classHeaderName'> " + record.childNodes[i].data.text + "</li>" ;
    		html = html + "<table style='width:600px;'>" ;
    		html = html + "<tr ><th class='methodTableHeader'>Method</th><th class='methodTableHeader'>Parameters</th><th class='methodTableHeader'>Comments</th>" ;
    		for ( var j = 0; j < methods.length; j++) {
    			var className = 'small';
    			if (interfaceMethod[ methods[j].name].length == record.childNodes.length){
    				className = 'interface';
    				html = html + "<tr class='"+className+"'><td>" + methods[j].name + "</td><td>"+ methods[j].parameters +"</td><td>INTERFACE</td></tr>" ;
    			}
    			else{
    				if (interfaceMethod[ methods[j].name].length/record.childNodes.length >= 0.5){
    					html = html + "<tr class='candidate'><td>" + methods[j].name + "</td><td>"+ methods[j].parameters +"</td><td>CANDIDATE</td></tr>" ;
    				}
    				else{
    					html = html + "<tr class='"+className+"'><td>" + methods[j].name + "</td><td>"+ methods[j].parameters +"</td><td></td></tr>" ;
    				}
    			}
    		}
    		html = html + "</table><br />";
		}
    	
    	
    	Ext.getCmp('mainTabPanel').insert({
			title			: record.data.text,
			html			: html,
			height			: 1000,
			autoScroll		: true,
			closable		: true,
			padding			: 10
    	});
    	
    }
};


JSViewer.prototype.parseFunctionComments = function(comments, parameter){
	comments = comments.replace(/\*/g, '');
	var splitted = comments.split("@");
	if (splitted.length > 0){
		for ( var i = 0; i < splitted.length; i++) {
			if (splitted[i].trim().indexOf(parameter) == 0){
				return splitted[i].trim();
			}
		}
	}
	return "";
};

JSViewer.prototype.onMethodGridClick = function(record){
		var data = new Array();
		for ( var i = 0; i < record.raw.parameters.length; i++) {
			if (record.raw.parameters[i].trim() != ""){
				var comments = "";
				if (record.raw.comments != null){
					comments = this.parseFunctionComments(record.raw.comments, record.raw.parameters[i].trim());
					
				} 
				data.push({name	: 	record.raw.parameters[i], comments : comments});
			}
		}
		this.propertiesStore.loadData(data);
};



JSViewer.prototype.sortStringArray = function(a, b){
	if (a.text < b.text )
				return -1;
	if (a.text > b.text )
				return 1;
	return 0;
}


JSViewer.prototype.getPropertiesMethodPanel = function(){
	this.propertiesStore = Ext.create('Ext.data.Store', {
	    fields	:['name', 'comments'],
	    data	:[]
	});
	var grid = Ext.create('Ext.grid.Panel', {
	    store			: this.propertiesStore,
	    border			: 1,
	    flex			: 1,
	    height			: this.height/4,
	    columns: [
	        { 	text			: 'Parameters',  
	        	dataIndex		: 'name', 
	        	flex			: 1, 
	        	renderer : function(row, opts ,record){
	        				var name = "<span class='paramName'>" + record.raw.name +"</span>";
	        				return name;
	        	} 
	        },
	        { 	text			: 'comments',  
	        	dataIndex		: 'comments', 
	        	flex			: 2, 
	        	renderer : function(row, opts ,record){
	        		
	        				var name = "<span class='paramName'>" + record.data.comments +"</span>";
	        				return name;
	        	} 
	        }
	    ]
	    
	});
	return grid;
};


JSViewer.prototype.getGridEventPanel = function(){
	var _this = this;
	
	
	this.eventsStore = Ext.create('Ext.data.Store', {
	    fields	:['name'],
	    data	:[]
	});	
	var grid = Ext.create('Ext.grid.Panel', {
	    store		: this.eventsStore,
	    height		: this.height*1/4,
	    border		: 1,
	    resizable	: true,
	    flex		: 1,
	    columns		: [
	           		   	{
			           		 	text			: '',  
					        	dataIndex		: 'name', 
					        	width			: 20, 
					        	renderer : function(row, opts ,record){
					        				return "<img src='http://i.msdn.microsoft.com/dynimg/IC279819.gif' />";
					        	} 
	           		   	
	           		   	},
				        { 	text			: 'Events',  
				        	dataIndex		: 'name', 
				        	flex			: 1, 
				        	renderer : function(row, opts ,record){
				        				return record.raw.name;
				        	} 
				        }
	    ],
	    viewConfig: {
	    	listeners : {
							'itemclick' :  function(grid, record, item, index, e, eOpts ){
											_this.onMethodGridClick(record);
														
							}
	    			}
	    }
	});
	return grid;
};


JSViewer.prototype.getGridMethodPanel = function(){
	var _this = this;
	this.attributesStore = Ext.create('Ext.data.Store', {
	    fields	:['name', 'type'],
	    data	:[]
	});	
	var grid = Ext.create('Ext.grid.Panel', {
	    store		: this.attributesStore,
	    height		: this.height*2/4,
	    border		: 1,
	    resizable	: true,
	    flex		: 1,
	    columns		: [
				        { 	text			: 'Methods',  
				        	dataIndex		: 'name', 
				        	flex			: 1, 
				        	renderer : function(row, opts ,record){
				        				var name = "<span class='methodName'>" + record.raw.name +"</span>";
				        				var params = "(<span class='paramName'>";
				        				for (var i = 0; i < record.raw.parameters.length - 1; i++){
				        					if (record.raw.parameters[i].trim() != ""){
				        						params = params + (record.raw.parameters[i]) + ", ";
				        					}
			        					}
				        				for (var i = record.raw.parameters.length - 1; i < record.raw.parameters.length; i++){
				        					if (record.raw.parameters[i].trim() != ""){
				        						params = params + (record.raw.parameters[i]);
				        					}
				        					
			        					}
				        				
				        				if (record.raw.name.indexOf("_") == 0){
				        					square = BUI.getRectangleColorDIV('#FA58D0', 8, 8);
				        				}
				        				else{
				        					square = BUI.getRectangleColorDIV('#81BEF7', 8, 8);
				        				}
				        				
				        				params = params + "</span>)";
				        				var name =  "<table class='parameters' ><tr ><td>" + square + "</td><td>"+name+"</td><td>" + params +"</td></tr></table>";
				        				return name;
				        	} 
				        }
	    ],
	    viewConfig: {
	    	listeners : {
							'itemclick' :  function(grid, record, item, index, e, eOpts ){
											_this.onMethodGridClick(record);
														
							}
	    			}
	    }
	});
	return grid;
};

JSViewer.prototype.getTreeClassPanel = function(){
	var data = new Array();
	for (unit in this.jsDoc.units){
		var items = new Array();
		for ( var i = 0; i < this.jsDoc.units[unit].length; i++) {
			var item = this.jsDoc.units[unit][i];
			items.push({ text: item.name, leaf: true });
		}
		items.sort(this.sortStringArray);
		data.push( { text: unit, leaf: false, children : items });
	}

	data.sort(this.sortStringArray);
	
	var store = Ext.create('Ext.data.TreeStore', {
	    root: {
	        expanded		:	true,
	        children		:	data
	    },
	    sort				: 'text'
	});
	
	var _this = this;
	
	var tree = Ext.create('Ext.tree.Panel', {
		folferSort		: true,
		height			: this.height,
	    store			: store,
	    rootVisible		: false,
	    viewConfig: {
				    	listeners : {
										'itemclick' :  function(grid, record, item, index, e, eOpts ){
															_this.onClassTreeClick(record);
																	
										}
				    			}
	    }
	});
	return tree;
};

