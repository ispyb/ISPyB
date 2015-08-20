package ispyb.ws.rest.security.login;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class IgnoringHostnameVerifier implements HostnameVerifier {
  public boolean verify(String hostname, SSLSession session) {
    return true;
  }
}