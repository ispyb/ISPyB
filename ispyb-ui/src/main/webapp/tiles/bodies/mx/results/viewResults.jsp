<%--------------------------------------------------------------------------------------------------
This file is part of ISPyB.

ISPyB is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

ISPyB is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with ISPyB.  If not, see <http://www.gnu.org/licenses/>.

Contributors : S. Delageniere, R. Leal, L. Launer, K. Levik, S. Veyrier, P. Brenchereau, M. Bodin, A. De Maria Antolinos
--------------------------------------------------------------------------------------------------%>

<%@ include file="../../mx/mx_css_include.jsp" %> 		
<%@ include file="../../mx/mx_javascript_include.jsp" %> 

<script type="text/javascript">
	var state = 'none';

	function showhide(layer_ref, state) {
		hza = document.getElementById(layer_ref);
		hza.style.display = state;
	}
</script>

<% 
	String target 				= request.getContextPath() + "/user/viewResults.do?reqCode=viewJpegImage";
	String targetGetDataFromFile		= request.getContextPath() + "/user/viewResults.do?reqCode=getDataFromFile";
	//String targetScreening 			= request.getContextPath() + "/user/viewScreening.do?reqCode=display";
	String targetLogFile			= request.getContextPath() + "/user/viewResults.do?reqCode=displayProgramLogFiles";
	String targetImageDownload 		= request.getContextPath() + "/user/imageDownload.do?reqCode=getImageJpgThumb";
	String targetImageDownloadFile 	= request.getContextPath() + "/user/imageDownload.do?reqCode=getImageJpgFromFile";
	String targetImageDownloadFileFullSize 	= request.getContextPath() + "/user/viewResults.do?reqCode=viewJpegImageFromFile";
	int	nbSnapshotDisplayed			= 0;
	int	nbImageDisplayed			= 0;
	String targetViewImageWall		= request.getContextPath() + "/user/viewImageWall.do?reqCode=display";
%>




<%-- BreadCrumbs bar --%>
<jsp:include page="/tiles/common/util/breadCrumbsBar.jsp" flush="true" />

<%-- Include Experiments parameters, Beamline parameters, DNA, Denzo . --%>

<script type="text/javascript">
		Ext.onReady(run);
		Ext.Loader.setConfig({enabled: true});

		var mainWindow;
		function run() {
			IspybResult.start("resultTabPanel");
		}
</script>
<div id="resultTabPanel"></div>

<%----------------------- Include  Xtal snapshots -----------------------%>
<jsp:include page="./viewResults_top.jsp" flush="true" />


<%---------------------- Images collected -------------------------------%>
<layout:grid cols="1"  borderSpacing="10">

	<%-- Images list --%>
	<layout:column>
		<logic:present name="viewResultsForm"  property="listInfo">
		<logic:notEmpty name="viewResultsForm"  property="listInfo">
		<layout:tabs width="100%">
			<layout:tab key="Images collected" width="150">
			
				<%-- Location --%>
				<layout:text key="Location" name="viewResultsForm" property="listInfo[0].fileLocation" styleClass="FIELD_DISPLAY" mode="I,I,I">
				<div align="right">
				<layout:link href="<%=targetViewImageWall%>" paramName="viewResultsForm" paramId="dataCollectionId" paramProperty="dataCollectionId" styleClass="FIELD">
					View as Image wall <img src="<%=request.getContextPath()%>/images/imageWall.gif" border=0 align="middle">
				</layout:link>
				</div>
				<layout:space/>
				</layout:text>
				

				<div id="sliderContent" class="ui-corner-all">  
      
        <div class="viewer ui-corner-all">  
          <div class="content-conveyor ui-helper-clearfix">  
          <logic:present name="viewResultsForm"  property="listInfo">
			<logic:notEmpty name="viewResultsForm"  property="listInfo">
				<logic:iterate name="viewResultsForm" property="listInfo" id="item" indexId="index" type="ispyb.client.mx.results.ImageValueInfo">
					<div class="item">  
            			<h4><bean:write name="item" property="fileName"/></h4>  
            			<html:link href="<%=target+ \"&imageId=\" + item.getImageId()+ \"&previousImageId=\" + item.getPreviousImageId()+ \"&nextImageId=\" + item.getNextImageId()%>" >
						<img src="<%= targetImageDownload + "&" + Constants.IMAGE_ID + "=" %><bean:write name="item" property="imageId"/>" alt="Click to zoom the image"> 
						</html:link> 
						<bean:define id="imageValue" name="item" type="ispyb.server.mx.vos.collections.Image3VO"/>
						<logic:equal name="item" property="imageFileExists" value="true">
								<layout:link onclick="showhide(imageValue.getImageId(), 'block');" href="<%=targetGetDataFromFile%>" paramName="item" paramId="imageId" paramProperty="imageId" property="fileLocation" styleClass="LIST">
								<%=imageValue.getFileName()%>
								</layout:link>
								
								<div id="<%=imageValue.getImageId() %>" style="display: none;" onclick="showhide(imageValue.getImageId(), 'none'); ">
									<layout:panel key="Compression in progress.Please wait..." align="left" styleClass="PANEL">
										<img src="<%=request.getContextPath()%>/images/progress_bar.gif" border=0>
									</layout:panel>
								</div>
							</logic:equal>
            			<dl class="details ui-helper-clearfix">  
              				<dt>Machine message:</dt><dd><bean:write name="item" property="machineMessage"/></dd>  
              				<dt>Comments:</dt><dd><bean:write name="item" property="comments"/></dd> 
           			 	</dl>  
         			 </div>  
				</logic:iterate>
			</logic:notEmpty>
		</logic:present>
		
          
        </div>  
      </div>  
      <div id="slider"></div>  
    </div>  
	
<script type="text/javascript"> 
      $(function() {  
	   var $j = jQuery.noConflict();
        //vars  
        var conveyor = $j(".content-conveyor", $j("#sliderContent")),  
        item = $j(".item", $j("#sliderContent"));  
        //set length of conveyor  
        conveyor.css("width", item.length * parseInt(item.css("width")));  
        //config  
        var sliderOpts = {  
          max: (item.length * parseInt(item.css("width"))) - parseInt($j(".viewer", $j("#sliderContent")).css("width")),  
          slide: function(e, ui) {  
            conveyor.css("left", "-" + ui.value + "px");  
          }  
        };  
        //create slider  
        $j("#slider").slider(sliderOpts);  
      });  
   </script>  		
				
	
			</layout:tab>
		</layout:tabs>
		</logic:notEmpty>
		</logic:present>	
	</layout:column >

</layout:grid>

<%-- Errors --%>
<jsp:include page="/tiles/common/messages/error.jsp" flush="true" />
