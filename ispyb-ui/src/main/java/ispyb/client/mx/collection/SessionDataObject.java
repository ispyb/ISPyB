/*******************************************************************************
 * This file is part of ISPyB.
 * 
 * ISPyB is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ISPyB is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with ISPyB.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors : S. Delageniere, R. Leal, L. Launer, K. Levik, S. Veyrier, P. Brenchereau, M. Bodin, A. De Maria Antolinos
 ******************************************************************************/

package ispyb.client.mx.collection;

import java.util.Date;
import java.util.List;

import ispyb.server.mx.vos.collections.DataCollection3VO;
import ispyb.server.mx.vos.collections.DataCollectionGroup3VO;
import ispyb.server.mx.vos.collections.EnergyScan3VO;
import ispyb.server.mx.vos.collections.Session3VO;
import ispyb.server.mx.vos.collections.Workflow3VO;
import ispyb.server.mx.vos.collections.XFEFluorescenceSpectrum3VO;

/**
 * A session data object represents data linked to a session: it could be a EnergyScan, a XRFSpectra, a Workflow, 
 * a DataDollectionGroup or a DataCollection
 * @author BODIN
 *
 */
public class SessionDataObject {
	protected Session3VO session;
	
	protected Date dataTime;
	
	// energy scan
	protected EnergyScan3VO energyScan;
	
	// xrf spectra
	protected XFEFluorescenceSpectrum3VO xrfSpectra;
	
	// dataCollectionGroup
	protected DataCollectionGroup3VO dataCollectionGroup;
	
	// workflow
	protected Workflow3VO workflow;
	
	// collect
	protected DataCollection3VO dataCollection;
	
	// for Workflow => list of collects
	protected List<DataCollection3VO> listDataCollection;

	
	
	public SessionDataObject() {
		super();
	}
	
	public SessionDataObject(SessionDataObject o) {
		super();
		this.dataTime = o.getDataTime();
		this.energyScan = o.getEnergyScan();
		this.xrfSpectra = o.getXrfSpectra();
		this.dataCollectionGroup = o.getDataCollectionGroup();
		this.workflow = o.getWorkflow();
		this.dataCollection = o.getDataCollection();
		this.listDataCollection = o.getListDataCollection();
	}


	// constructor for EnergyScan
	public SessionDataObject(EnergyScan3VO energyScan) {
		super();
		this.energyScan = energyScan;
		this.xrfSpectra = null;
		this.dataCollectionGroup = null;
		this.workflow = null;
		this.dataCollection = null;
		this.listDataCollection = null;
		
		if (energyScan != null){
			this.dataTime = energyScan.getStartTime();
		}
	} 
	
	// constructor for XRFSpectra
	public SessionDataObject(XFEFluorescenceSpectrum3VO xrfSpectra) {
		super();
		this.energyScan = null;
		this.xrfSpectra = xrfSpectra;
		this.dataCollectionGroup = null;
		this.workflow = null;
		this.dataCollection = null;
		this.listDataCollection = null;
			
		if (xrfSpectra != null){
			this.dataTime = xrfSpectra.getStartTime();
		}
	} 
	
	// constructor for DataCollectionGroup
	public SessionDataObject(DataCollectionGroup3VO dataCollectionGroup) {
		super();
		this.energyScan = null;
		this.xrfSpectra = null;
		this.dataCollectionGroup = dataCollectionGroup;
		this.workflow = null;
		this.dataCollection = null;
		this.listDataCollection = null;
				
		if (dataCollectionGroup != null){
			this.dataTime = dataCollectionGroup.getStartTime();
		}
	} 
	
	// constructor for Workflow
	public SessionDataObject(Workflow3VO workflow, List<DataCollection3VO> listDataCollection) {
		super();
		this.energyScan = null;
		this.xrfSpectra = null;
		this.dataCollectionGroup = null;
		this.workflow = workflow;
		this.dataCollection = null;
		this.listDataCollection = listDataCollection;
					
		if (listDataCollection != null && listDataCollection.size() > 0){
			//this.dataTime = listDataCollection.get(0).getStartTime();
			this.dataTime = listDataCollection.get(listDataCollection.size()-1).getStartTime();
		}
	} 
	
	// constructor for DataCollection
	public SessionDataObject(DataCollection3VO dataCollection) {
		super();
		this.energyScan = null;
		this.xrfSpectra = null;
		this.dataCollectionGroup = null;
		this.workflow = null;
		this.dataCollection = dataCollection;
		this.listDataCollection = null;
						
		if (dataCollection != null ){
			this.dataTime = dataCollection.getStartTime();
		}
	} 

	public Session3VO getSession() {
		return session;
	}

	public void setSession(Session3VO session) {
		this.session = session;
	}

	public Date getDataTime() {
		return dataTime;
	}

	public void setDataTime(Date dataTime) {
		this.dataTime = dataTime;
	}

	public EnergyScan3VO getEnergyScan() {
		return energyScan;
	}

	public void setEnergyScan(EnergyScan3VO energyScan) {
		this.energyScan = energyScan;
	}

	public XFEFluorescenceSpectrum3VO getXrfSpectra() {
		return xrfSpectra;
	}

	public void setXrfSpectra(XFEFluorescenceSpectrum3VO xrfSpectra) {
		this.xrfSpectra = xrfSpectra;
	}

	public DataCollectionGroup3VO getDataCollectionGroup() {
		return dataCollectionGroup;
	}

	public void setDataCollectionGroup(DataCollectionGroup3VO dataCollectionGroup) {
		this.dataCollectionGroup = dataCollectionGroup;
	}

	public Workflow3VO getWorkflow() {
		return workflow;
	}

	public void setWorkflow(Workflow3VO workflow) {
		this.workflow = workflow;
	}

	public DataCollection3VO getDataCollection() {
		return dataCollection;
	}

	public void setDataCollection(DataCollection3VO dataCollection) {
		this.dataCollection = dataCollection;
	}

	public List<DataCollection3VO> getListDataCollection() {
		return listDataCollection;
	}

	public void setListDataCollection(List<DataCollection3VO> listDataCollection) {
		this.listDataCollection = listDataCollection;
	}
	
	// kind of objects
	public boolean isEnergyScan(){
		return this.energyScan != null;
	}
	
	public boolean isXRFSpectra(){
		return this.xrfSpectra != null;
	}
	
	public boolean isDataCollectionGroup(){
		return this.dataCollectionGroup != null;
	}
	
	public boolean isWorkflow(){
		return this.workflow != null;
	}
	
	public boolean isDataCollection(){
		return this.dataCollection != null;
	}
	
}
