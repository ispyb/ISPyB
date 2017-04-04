package ispyb.server.common.services.admin;

import ispyb.common.util.StringUtils;
import ispyb.server.common.vos.admin.SchemaStatusVO;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

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

		return crit.list();
	};
	

	@Override
	public List<SchemaStatusVO> findAll() throws Exception {
			return this.findFiltered(null, null, null);
	}


}
