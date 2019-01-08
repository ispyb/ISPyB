package ispyb.ws.rest.security.login;

import ispyb.common.util.Constants;
import ispyb.server.common.util.ISPyBException;
import ispyb.server.common.util.ISPyBRuntimeException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * A login module whose credentials store is an ISPyB property.
 * <p>
 * The credentials store for this module is the value of the
 * {@code ispyb.ws.rest.auth.property.credentials} ISPyB property. The value is
 * a semicolon-separated list of credentials where a credential has the form:
 * <p>
 * <pre>
 * {@code <username>,<password>[,<role-list>]}</pre>
 * <p>
 * and {@code <role-list>} has the form:
 * <p>
 * <pre>
 * {@code <role>[,...]}</pre>
 * <p>
 * Whitespace is taken literally, not skipped. It is illegal for a
 * {@code <username>} or {@code <role>} (if specified) to be empty. Note that
 * {@code <password>} is plaintext, not ciphertext. The {@code <role-list>} is
 * optional. And because commas and semicolons are used as delimiters, these
 * two characters are illegal in {@code <username>}, {@code <password>}, and
 * {@code <role>}.
 * <p>
 * Here's an example credentials list of two users:
 * <p>
 * <pre>
 * {@code alice,dragon,User,Manager;bob,batman,User}</pre>
 * <p>
 * The first user is "alice" with password "dragon" and the roles "User" and
 * "Manager". The second is the user "bob" with password "batman" and the role
 * "User".
 * <p>
 * NOTE: Because the passwords are stored in the property as plaintext, this
 * module is not suitable for production use, but it can be useful for
 * development and testing since it's easy to define users.
 *
 */
public class PropertyLoginModule {
  private static final Map<String, Credential> CREDENTIALS = readCredentials();
  private static final Credential NONEXISTENT_USER_CREDENTIAL = createNonexistentUserCredential();

  /**
   * Returns the roles of the user if authentication succeeds.
   *
   * @param username the username of the user to authenticate
   * @param password the password of the user to authenticate
   *
   * @return the roles of the authenticated user
   *
   * @throws ISPyBException if the user could not be authenticated
   */
  public static List<String> authenticate(String username, String password) throws ISPyBException {
    Credential existingCredential = CREDENTIALS.get(username);
    boolean userExists = (existingCredential != null);
    Credential c = userExists ? existingCredential : NONEXISTENT_USER_CREDENTIAL;
    boolean passwordEquals = constantTimeEquals(c.getPasswordHash(), hash(password));
    if (userExists && passwordEquals) return c.getRoles();
    throw new ISPyBException("Unauthorized");
  }

  private static final Map<String, Credential> readCredentials() throws ISPyBRuntimeException {
    try {
      return Collections
          .unmodifiableMap(
              parseCredentials(Constants.getProperty("ispyb.ws.rest.auth.property.credentials")));
    } catch (ParseException e) {
      ISPyBRuntimeException ex = new ISPyBRuntimeException("Invalid credentials store");
      ex.initCause(e);
      throw ex;
    }
  }

  private static final Map<String, Credential> parseCredentials(String credentialsList)
      throws ParseException {
    final Pattern semicolonPat = Pattern.compile(";");
    final Pattern commaPat = Pattern.compile(",");
    Map<String, Credential> result = new HashMap<>();
    if (credentialsList == null) return result;
    if (credentialsList.isEmpty()) return result;
    int offset = 0;
    String[] credentials = semicolonPat.split(credentialsList, -1);
    for (int credentialIndex = 0; credentialIndex < credentials.length; credentialIndex++) {
      if (credentialIndex > 0) offset += ";".length();
      String credential = credentials[credentialIndex];
      String[] parts = commaPat.split(credential, -1);
      if (parts.length < 2) {
        throw new ParseException("Credential \"" + credential + "\" not list of 2 or more", offset);
      }
      String username = parts[0];
      if (username.isEmpty()) {
        throw new ParseException("Username is empty", offset);
      }
      offset += username.length() + ",".length();
      String password = parts[1];
      offset += password.length();
      List<String> roles = Arrays.asList(parts).subList(2, parts.length);
      if (!roles.isEmpty()) offset += ",".length();
      for (ListIterator<String> rolesIterator = roles.listIterator(); rolesIterator.hasNext();) {
        if (rolesIterator.hasPrevious()) offset += ",".length();
        String role = rolesIterator.next();
        if (role.isEmpty()) {
          throw new ParseException("Role is empty", offset);
        }
        offset += role.length();
      }
      result.put(username, new Credential(username, hash(password), roles));
    }
    return result;
  }

  /*
   * Hashes the plaintext password into a fixed-size byte array.  The
   * algorithm is SHA-256, a one-way hash function.  The only requirement
   * for the one-way hash function used here is that it doesn't have a high
   * probability of collision such that many different passwords would all
   * have the same hash value thus making a brute-force attack easier.  A
   * proper key derivation function is not used here because the passwords for
   * this login module are stored in plaintext, so if they're ever obtained,
   * it's game over anyway.
   */
  private static final byte[] hash(String password) {
    MessageDigest md;
    try {
      md = MessageDigest.getInstance("SHA-256");
    } catch (NoSuchAlgorithmException e) {
      throw new AssertionError(e);
    }
    md.update(password.getBytes(StandardCharsets.UTF_8));
    return md.digest();
  }

  private static final boolean constantTimeEquals(byte[] a, byte[] b) {
    if (a.length != b.length) throw new IllegalArgumentException("a.length != b.length");
    byte result = 0;
    for (int i = 0; i < a.length; i++) {
      result |= a[i] ^ b[i];
    }
    return result == 0;
  }

  private static final Credential createNonexistentUserCredential() {
    return new Credential("", hash(""), Collections.<String>emptyList());
  }

  private static final class Credential {
    private final String username;
    private final byte[] passwordHash;
    private final List<String> roles;

    /**
     * The caller must not modify {@code passwordHash} nor {@code roles} after
     * calling this constructor.
     */
    public Credential(String username, byte[] passwordHash, List<String> roles) {
      this.username = username;
      this.passwordHash = passwordHash;
      this.roles = Collections.unmodifiableList(roles);
    }

    @SuppressWarnings("unused")
    public String getUsername() {
      return username;
    }

    /**
     * The caller must not modify the returned byte array.
     */
    public byte[] getPasswordHash() {
      return passwordHash;
    }

    public List<String> getRoles() {
      return roles;
    }
  }
}
