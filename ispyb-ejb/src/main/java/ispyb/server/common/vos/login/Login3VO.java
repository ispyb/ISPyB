package ispyb.server.common.vos.login;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Calendar;
import java.util.Date;

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
//		long loginDuration = 3*(1000*60*60);
//		System.out.println("login duration: " + loginDuration + " ms");
//		System.out.println("now: " + Calendar.getInstance().getTime().getTime() + " ms");
//		System.out.println("expiration: " + this.expirationTime.getTime() + " ms");
//		System.out.println("subs: " + (loginDuration - (Calendar.getInstance().getTime().getTime() - this.expirationTime.getTime()))  + " ms");
//	    return (loginDuration - (Calendar.getInstance().getTime().getTime() - this.expirationTime.getTime()));
	}
	
	@Transient
	public boolean isValid(){
	    return  this.expirationTime.getTime() > Calendar.getInstance().getTime().getTime();
	}
	
}
