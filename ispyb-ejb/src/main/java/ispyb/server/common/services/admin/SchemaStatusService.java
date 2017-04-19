package ispyb.server.common.services.admin;

import java.util.List;

import javax.ejb.Remote;

import ispyb.server.common.vos.admin.SchemaStatusVO;

@Remote
public interface SchemaStatusService {
	
	public List<SchemaStatusVO> findFiltered(final Integer schemaStatusId, final String scriptName, final String schemaStatus) throws Exception;

	public List<SchemaStatusVO> findAll() throws Exception;

}
