package ispyb.server.common.vos.login;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "Login")
public class Login3VO implements java.io.Serializable {

	
	private static final long serialVersionUID = 1L;
	
	protected Integer loginId;
	protected String token;
	protected String username;
	protected String roles;
	protected Date expirationTime;
	
	public Login3VO(){}

	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "loginId", unique = true, nullable = false)
	public Integer getLoginId() {
		return loginId;
	}

	public void setLoginId(Integer loginId) {
		this.loginId = loginId;
	}

	@Column(name = "token")
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Column(name = "username")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "roles")
	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "expirationTime", length = 0)
	public Date getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(Date expirationTime) {
		this.expirationTime = expirationTime;
	}
	
	@Transient
	public long getRemainingMilliseconds(){
		return this.expirationTime.getTime() - Calendar.getInstance().getTime().getTime();
	}
	
	@Transient
	public boolean isValid(){
	    return  this.expirationTime.getTime() > Calendar.getInstance().getTime().getTime();
	}
	
	@Transient
	public boolean isManager(){
	    return  this.getRoles().toUpperCase().contains("MANAGER");
	}
	
	@Transient
	public boolean isUser(){
	    return  this.getRoles().toUpperCase().contains("USER");
	}
	
	@Transient
	public boolean isLocalContact(){
	    return  this.getRoles().toUpperCase().contains("LOCALCONTACT");
	}

	/**
	 * This method checks if this login contains at least one role of the roles list
	 * @param rolesSet
	 * @return True if the role is in the login
	 */
	public boolean checkRoles(Set<String> rolesSet) {
		for (String role : rolesSet) {
			for (String loginRole : this.getRoles().split(",")) {
				System.out.println(loginRole.toUpperCase() + " " + role.toUpperCase() + " " + loginRole.toUpperCase().trim().equals(role.toUpperCase().trim()));
				if (loginRole.toUpperCase().trim().equals(role.toUpperCase().trim())){
					return true;
				}
			}
		}
		return false;
	}


	
}
