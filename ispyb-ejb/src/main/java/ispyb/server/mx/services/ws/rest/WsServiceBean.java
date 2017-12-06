package ispyb.server.mx.services.ws.rest;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

public class WsServiceBean {
	
	protected List<Map<String, Object>> executeSQLQuery(SQLQuery query ){
		query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> aliasToValueMapList = query.list();
		return aliasToValueMapList;
	}

	protected String getQueryFromResourceFile(String resourcefilePath ){
		InputStream str = null;
	    try{
	        str = WsServiceBean.class.getResourceAsStream(resourcefilePath);
	        return IOUtils.toString(str).replace("\n", " ").replace("\r", " ").replaceAll("\\s{2,}", " ").trim();
	    }catch(Exception e){
	        throw new IllegalStateException("Failed to read SQL query in " + resourcefilePath, e);
	    }finally{
	        IOUtils.closeQuietly(str);
	    }
	}
	    
	    
}
