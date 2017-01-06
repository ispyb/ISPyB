package ispyb.server.common.services.admin;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import ispyb.common.util.Constants;
import ispyb.common.util.StringUtils;
import ispyb.server.common.vos.admin.SchemaStatusVO;

@Stateless
public class SchemaStatusServiceBean implements SchemaStatusService, SchemaStatusServiceLocal {

	private final static Logger LOG = Logger.getLogger(SchemaStatusServiceBean.class);

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;
	
	public SchemaStatusServiceBean() {
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SchemaStatusVO> findFiltered(Integer schemaStatusId, String scriptName, String schemaStatus)
			throws Exception {
		Session session = (Session) this.entityManager.getDelegate();

		Criteria crit = session.createCriteria(SchemaStatusVO.class);

		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // DISTINCT RESULTS !

		if (!StringUtils.isEmpty(scriptName))
			crit.add(Restrictions.like("scriptName", scriptName));
		
		if (!StringUtils.isEmpty(schemaStatus))
			crit.add(Restrictions.like("schemaStatus", schemaStatus));
		
		if (schemaStatusId != null)
			crit.add(Restrictions.like("schemaStatusId", schemaStatusId));

		//crit.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
				
		return crit.list();
	};
	
	public List<SchemaStatusVO> findAll() throws Exception {
		return this.findFiltered(null, null, null);
	};	

	@Override
	public List<String> findScriptsDone() throws Exception {

			List<String> doneInDB = this.getScriptNames(this.findAll());
			return doneInDB;
	}

	@Override
	public List<String> findScriptsNotDone() throws Exception {

		List<String> results = new ArrayList<String>();
		List<String> doneInDB = this.getScriptNames(this.findAll());
		List<String> foundInFolder = this.findUpdateScripts();
		if (foundInFolder == null) {
			results.add("No script found ??? may be wrong folder path");
		}
		else {			
			foundInFolder.removeAll(doneInDB);
			results = foundInFolder;	
		}
			return results;
	}
	
	private List<String> findUpdateScripts() {
		
		LOG.info(" Path to scripts = " + Constants.PATH_TO_SCRIPTS );
		List<String> results = new ArrayList<String>();
		if (Constants.PATH_TO_SCRIPTS == null) 
			return null;
		File[] files = new File(Constants.PATH_TO_SCRIPTS).listFiles();
		//If this pathname does not denote a directory, then listFiles() returns null. 

		if (files != null) {
			for (File file : files) {
				if (file.isFile()) {
					results.add(file.getName().replaceAll(".sql", ""));
				}
			}	
		}
		return results;
	}
				
	private List<String> getScriptNames(List<SchemaStatusVO> listVOs){
		
		List<String> names = new ArrayList<String>();
		for (Iterator<SchemaStatusVO> iterator2 = listVOs.iterator(); iterator2.hasNext();) {
			SchemaStatusVO o = (SchemaStatusVO) iterator2.next();
			names.add(o.getScriptName().replaceAll(".sql", ""));
		}
		return names;
	}

}
