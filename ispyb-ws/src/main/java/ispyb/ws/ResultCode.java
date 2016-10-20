package ispyb.ws;

import java.text.MessageFormat;

/**
 * A list of possible result codes that could be returned to the client application
 * 
 * @author durdav
 *
 */
public enum ResultCode {
	/**
	 * Parameters:
	 */
	OK("ISPYB-0000", "Ok"), 
	/**
	 * Parameters: Exception Message
	 */
	UNKNOWN_ERROR("ISPYB-0001", "Unknown Error: {0}"), 
	/**
	 * Parameters: Proposal Code, Proposal Number
	 */
	UNKNOWN_PROPOSAL("ISPYB-0002", "Unknown Proposal: {0}{1}"),
	/**
	 * Parameters: Experiment ID
	 */
	UNKNOWN_EXPERIMENT("ISPYB-0003", "Unknown Experiment: {0}"),
	/**
	 * Parameters: Exception Message
	 */
	RUNTIME_ERROR("ISPYB-0004", "{0}"),
	/**
	 * Parameters: Proposal Code, Proposal Number
	 */
	NO_EXPERIMENTS_FOUND("ISPYB-0004", "No experiments found for proposal: {0}{1}"),
	/**
	 * Parameters: 
	 */
	NOT_USED("ISPYB-0000", "ResultCode not used")
	;
	
	private String code;
	private String description;
	
	private ResultCode(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getDescription(Object... args) {
		return MessageFormat.format(description, args);
	}
}