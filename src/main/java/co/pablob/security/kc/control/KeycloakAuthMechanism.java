package co.pablob.security.kc.control;

import org.keycloak.adapters.BearerTokenRequestAuthenticator;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.KeycloakDeploymentBuilder;
import org.keycloak.adapters.servlet.ServletHttpFacade;
import org.keycloak.adapters.spi.AuthOutcome;
import org.keycloak.adapters.spi.HttpFacade;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.adapters.config.AdapterConfig;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import javax.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.pablob.security.commons.entity.JwtPrincipal;

@ApplicationScoped
public class KeycloakAuthMechanism implements HttpAuthenticationMechanism {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER = "Bearer ";
    private static final String ANONYMOUS = "Anonymous";

    private KeycloakDeployment deployment;

    @Inject
    private AdapterConfig adapterConfig;

    @PostConstruct
    private void init() {
        deployment = KeycloakDeploymentBuilder.build(adapterConfig);
    }

    @Override
    public AuthenticationStatus validateRequest(
            HttpServletRequest request,
            HttpServletResponse response,
            HttpMessageContext httpMessageContext) {

        final String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER)) {
            BearerTokenRequestAuthenticator authenticator = new BearerTokenRequestAuthenticator(deployment);
            HttpFacade facade = new ServletHttpFacade(request, response);

            final AuthOutcome authResult = authenticator.authenticate(facade);

            if (AuthOutcome.AUTHENTICATED == authResult) {
                return authenticationStatus(authenticator.getToken(), httpMessageContext);
            }

            return httpMessageContext.responseUnauthorized();
        }

        return httpMessageContext.notifyContainerAboutLogin(new JwtPrincipal(ANONYMOUS), new HashSet<>());
    }

    private AuthenticationStatus authenticationStatus(AccessToken token, HttpMessageContext httpMessageContext) {
        final Set<String> roles = token.getRealmAccess().getRoles();
        final JwtPrincipal principal = JwtPrincipal.Builder(token.getSubject())
                .setEmail(token.getEmail())
                .setFamilyName(token.getFamilyName())
                .setFullName(token.getName())
                .setGivenName(token.getGivenName())
                .setUserName(token.getPreferredUsername())
                .setPicture(token.getPicture())
                .build();

        CredentialValidationResult credentials = new CredentialValidationResult(principal, roles);

        return httpMessageContext.notifyContainerAboutLogin(credentials);
    }
}
