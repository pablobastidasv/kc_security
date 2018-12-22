package co.pablob.security.commons.entity;

import java.io.Serializable;
import java.util.Map;

import javax.enterprise.inject.Alternative;
import javax.security.enterprise.CallerPrincipal;

import static co.pablob.security.kc.control.KeycloakAuthMechanism.ANONYMOUS;

@Alternative
public class JwtPrincipal extends CallerPrincipal implements Serializable {

    /**
     * JWT token value of: preferred_username
     */
    private String userName;
    /**
     * JWT token value of: name
     */
    private String fullName;
    /**
     * JWT token value of: given_name
     */
    private String givenName;
    /**
     * JWT token value of: family_name
     */
    private String familyName;
    /**
     * JWT token value of: email
     */
    private String email;
    /**
     * JWT token value of: picture
     */
    private String picture;
    /**
     * The JWT token
     */
    private String token;
    /**
     * The realm where the user was validated.
     */
    private String realm;

    /**
     * Map of any other claims and data that might be in the IDToken. Could be custom claims set up by the auth server
     */
    private Map<String, Object> claims;

    public JwtPrincipal() {
        super(ANONYMOUS);
    }

    public JwtPrincipal(String name) {
        super(name);
    }

    public String getUserName() {
        return userName;
    }

    public String getFullName() {
        return fullName;
    }

    public String getGivenName() {
        return givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getEmail() {
        return email;
    }

    public String getPicture() {
        return picture;
    }

    public Map<String, Object> getClaims() {
        return claims;
    }

    public String getToken() {
        return token;
    }

    public String getRealm() {
        return realm;
    }

    public static JwtPrincipalBuilder Builder(String name) {
        return new JwtPrincipalBuilder(name);
    }

    public static class JwtPrincipalBuilder {

        private JwtPrincipal principal;

        private JwtPrincipalBuilder(String name) {
            principal = new JwtPrincipal(name);
        }

        public JwtPrincipalBuilder setUserName(String userName) {
            principal.userName = userName;
            return this;
        }

        public JwtPrincipalBuilder setFullName(String fullName) {
            principal.fullName = fullName;
            return this;
        }

        public JwtPrincipalBuilder setGivenName(String givenName) {
            principal.givenName = givenName;
            return this;
        }

        public JwtPrincipalBuilder setFamilyName(String familyName) {
            principal.familyName = familyName;
            return this;
        }

        public JwtPrincipalBuilder setEmail(String email) {
            principal.email = email;
            return this;
        }

        public JwtPrincipalBuilder setPicture(String picture) {
            principal.picture = picture;
            return this;
        }

        public JwtPrincipalBuilder setClaims(Map<String, Object> claims) {
            principal.claims = claims;
            return this;
        }

        public JwtPrincipalBuilder withToken(String token){
            principal.token = token;
            return this;
        }

        public JwtPrincipalBuilder withRealm(String realm){
            principal.realm = realm;
            return this;
        }

        public JwtPrincipal build() {
            return principal;
        }
    }
}
