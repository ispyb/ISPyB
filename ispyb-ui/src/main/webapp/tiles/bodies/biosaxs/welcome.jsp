
<style>
.container 		{
						background-color	: white;
						width				: 400px;
						height				: 300px;
						position			: relative;
						left				: 10px;
}
.tab_container {
							border-bottom		: 2px #314e64 solid;
							background-color	: white;
							color				: #314e64;
							font-size			: 14px; 
							font-weight			: bold;
							width				: 450px;
							height				: 20px;
}
	
.right_container {
								top					: -20px;
								left				: 500px;
								position			: relative;
}				

.left_container {
								top					: 0px;
								left				: 10px;
								position			: relative;
}	
.text_container 	{
								width				: 450px;
								height				: 200px;
								position			: relative;
								top					: 25px;
								left				: 10px;
								color				: black;
								font-size			: 12px; 
								font-weight			: normal;
					}	
					
.item_square		{
								width				: 40px;
								height				: 40px;
								border				: 2px solid;
								-moz-border-radius	: 10px;
								border-radius		: 10px;
								text-align			: center;
								line-height			: 40px;
								font-size			: 20px;
								color				: white;
								font-weight			: bold;
}		

.currently			{
								font-family				: Arial, Helvetica, sans-serif;
								font-size				: 12px;
								font-weight				: bold;
								color					: black;
}	

.item_square_text			{
								font-family				: Arial, Helvetica, sans-serif;
								font-size				: 12px;
								color					: black;
								font-weight				: normal;
}

.item_red_square			{
								background-color		:#F78181;
								border-color			:#B40404;
}

.item_blue_square			{
								background-color		:#A9D0F5;
								border-color			:#013ADF;
}

.item_green_square			{
								background-color		:#00FF40;
								border-color			:#31B404;
}

.item_purple_square			{
								background-color		:#D0A9F5;
								border-color			:#5F04B4;
}
</style>

<% String targetExploreResults = request.getContextPath() + "/menuSelected.do?topMenuId=5000&targetUrl=/user/viewProjectList.do?reqCode=display&menu=results";  %>
<% String targetDataAcquisitions = request.getContextPath() + "/user/viewProjectList.do?reqCode=display";  %>
<% String targetListShipment = request.getContextPath() + "/user/viewProjectList.do?reqCode=display&menu=list_shipment";  %>
<% String targetPrepareExperiment = request.getContextPath() + "/user/viewProjectList.do?reqCode=display&menu=prepareexperiment";  %>
<% String toUpdateDB 	= request.getContextPath() + "/updateDB.do?reqCode=updateProposal"; %>

	
<h2>In case of problems when creating shipments/samples, <a href="<%=toUpdateDB%>" >update ISPyB database</a> (this may take a few minutes).
</h2>
<br>
<div class='container'>

	<div  class='tab_container left_container'><a href="<%=targetListShipment%>" >Shipping Tab</a>
		<div class='text_container'>
			Click on this tab to deal with the samples you are planning to send by courier.
		 	<br />You will be able to:
			 	<ol >
			 		<li class='welcome_biosaxs_text'>Define an shipment, containing stock solutions and cases</li>
			 		<li class='welcome_biosaxs_text'>You will be able to retrieve information about the shipments and cases</li>
			 	</ol>
			 <br />
			 <span class='currently'>Currently your proposal contains:</span>
			 <ol >
				<li id='stock-solution-text'>Stock Solutions</li>
				<li id='shipment-text'>Shipments</li>
			 </ol>
		 </div>
		 			
	 </div>
	 
	 <div class='tab_container right_container'><a href="<%=targetPrepareExperiment%>" >Prepare Experiment Tab</a>
	 	<div class='text_container'>Click on this tab to deal with data concerning your samples
		 	<br />You will be able to:
			<ol >
			 	<li class='welcome_biosaxs_text'>Create new samples for experiment: samples description will be based on the protein you have submitted through "samplesheets".</li>
			 	<li class='welcome_biosaxs_text'>Define the buffers you plan to use for data collection</li>
			 	<li class='welcome_biosaxs_text'>Program data collections with the sample changer</li>
			</ol>
			<br />
			<span class='currently'>Currently your proposal contains:</span>
			 <ol >
				<li id='macromolecule-text'>Macromolecules</li>
				<li id='buffer-text'>Buffers</li>
				<li id='template-text'>Exp. Templates</li>
			 </ol>
			
		 </div>
	 </div>
</div>

<div class='container'>
	<div class='tab_container left_container'><a href="<%=targetDataAcquisitions%>" >Data Acquisition Tab</a>
		<div class='text_container'>
			Click on this tab to deal with the data collection you perform on your samples
		 	<br />You will be able to:
			 	<ol >
				 	<li class='welcome_biosaxs_text'>Retrieve information about a particular session</li>
				 	<li class='welcome_biosaxs_text'>Retrieve information about a particular data collection</li>
				 	<li class='welcome_biosaxs_text'>Retrieve information about a particular session</li>
				 	<li class='welcome_biosaxs_text'>Retrieve information on the results from the data analysis pipeline</li>
			 	</ol>
			 	<br />
				<span class='currently'>Currently your proposal contains:</span>
				 <ol >
					<li id='sessions-text'>Sessions</li>
					<li id='calibration-text'>Calibration</li>
					<li id='static-text'>Static. Exp.</li>
					<li id='hplc-text'>HPLC Exp.</li>
				 </ol>
			 	
		 </div>
	 </div>

	 <div class='tab_container right_container'><a href="<%=targetExploreResults%>" >Explore your results</a>
	 	<div class='text_container'>Click on this tab to deal with the data collected for your macromolecules
		 	<br />You will be able to:
			<ol >
			 	<li class='welcome_biosaxs_text'>Find all data collections you did for a macromolecule and an estimate of their quality</li>
			 	<li class='welcome_biosaxs_text'>Compare results from different collections and sessions</li>
			</ol>
			<br />
				<span class='currently'>Currently your proposal contains:</span>
				 <ol >
					<li id='datacollections-text'>Data Collections</li>
				 </ol>
			<div >
			
			</div>
	
	
		 </div>
	 </div>
</div>

<%@ include file="project/biosaxs_css_include.jsp" %>

<script type="text/javascript">

Ext.onReady(function(){
	BIOSAXS.proposal.onInitialized.attach(function(){
		if (BIOSAXS.proposal != null){
			
			if (BIOSAXS.proposal.getMacromolecules() != null){
				document.getElementById("macromolecule-text").innerHTML = BIOSAXS.proposal.getMacromolecules().length + " " + document.getElementById("macromolecule-text").innerHTML;
			}
			if (BIOSAXS.proposal.getBuffers() != null){
				document.getElementById("buffer-text").innerHTML = BIOSAXS.proposal.getBuffers().length + " " + document.getElementById("buffer-text").innerHTML;
			}

			if (BIOSAXS.proposal.getStockSolutions() != null){
				document.getElementById("stock-solution-text").innerHTML = BIOSAXS.proposal.getStockSolutions().length + " " + document.getElementById("stock-solution-text").innerHTML;
			}

			if (BIOSAXS.proposal.getShipments() != null){
				document.getElementById("shipment-text").innerHTML = BIOSAXS.proposal.getShipments().length + " " + document.getElementById("shipment-text").innerHTML;
				
			}

			if (BIOSAXS.proposal.getSessions() != null){
				document.getElementById("sessions-text").innerHTML = BIOSAXS.proposal.getSessions().length + " " + document.getElementById("sessions-text").innerHTML;
			}
		}
	});
	BIOSAXS.proposal.init();
	
	var adapter2 = new BiosaxsDataAdapter();
	adapter2.onSuccess.attach(function(sender, data){
		var templateCount = 0 ;
		var staticCount = 0 ;
		var calibCount = 0 ;
		var hplcCount = 0 ;
		if (data != null){
			for (var i = 0; i < data.length; i++){
				if (data[i].experimentType != null){
					if (data[i].experimentType == 'TEMPLATE'){
						templateCount ++;
					}
					else if (data[i].experimentType == 'STATIC'){
						staticCount ++;
					}
					else if (data[i].experimentType == 'HPLC'){
						hplcCount ++;
					}
					else if (data[i].experimentType == 'CALIBRATION'){
						calibCount ++;
					}
				}
			}
		}
		
		
		document.getElementById("template-text").innerHTML = templateCount + " " + document.getElementById("template-text").innerHTML;
		document.getElementById("static-text").innerHTML = staticCount + " " + document.getElementById("static-text").innerHTML;
		document.getElementById("calibration-text").innerHTML = calibCount + " " + document.getElementById("calibration-text").innerHTML;
		document.getElementById("hplc-text").innerHTML = hplcCount + " " + document.getElementById("hplc-text").innerHTML;
	});
	adapter2.getExperimentInformationByProposalId();


	var adapter3 = new BiosaxsDataAdapter();
	adapter3.onSuccess.attach(function(sender, data3){
		var goodCount = 0 ;
		
		if (data3 != null){
			for (var i = 0; i < data3.length; i++){
				if (data3[i].quality != null){
					if (data3[i].quality > 0.8){
						goodCount ++;
					}
				}
			}
			document.getElementById("datacollections-text").innerHTML = data3.length + " " + document.getElementById("datacollections-text").innerHTML;
		}	
	});
	adapter3.getAnalysisByProposalId();

});

</script>
