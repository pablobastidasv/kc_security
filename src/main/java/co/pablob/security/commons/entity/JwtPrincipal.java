package co.pablob.security.commons.entity;

import java.io.Serializable;
import java.util.Map;

import javax.security.enterprise.CallerPrincipal;

public class JwtPrincipal extends CallerPrincipal implements Serializable {

    private String userName;
    private String fullName;
    private String givenName;
    private String familyName;
    private String email;
    private String picture;

    private Map<String, Object> claims;

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

        public JwtPrincipalBuilder setClaims(Map<String, Object> claims){
            principal.claims = claims;
            return this;
        }

        public JwtPrincipal build(){
            return principal;
        }
    }
}
