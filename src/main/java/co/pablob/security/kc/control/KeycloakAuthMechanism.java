package co.pablob.security.kc.control;

import co.pablob.security.commons.entity.JwtPrincipal;
import org.keycloak.adapters.BearerTokenRequestAuthenticator;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.KeycloakDeploymentBuilder;
import org.keycloak.adapters.servlet.ServletHttpFacade;
import org.keycloak.adapters.spi.AuthOutcome;
import org.keycloak.adapters.spi.HttpFacade;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.adapters.config.AdapterConfig;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import javax.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@ApplicationScoped
public class KeycloakAuthMechanism implements HttpAuthenticationMechanism {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER = "Bearer ";
    public static final String ANONYMOUS = "Anonymous";

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
                return authenticationStatus(deployment.getResourceName(), authenticator, httpMessageContext);
            }

            return httpMessageContext.responseUnauthorized();
        }

        return httpMessageContext.notifyContainerAboutLogin(new JwtPrincipal(), new HashSet<>());
    }

    private AuthenticationStatus authenticationStatus(String resourceName, BearerTokenRequestAuthenticator authenticator, HttpMessageContext httpMessageContext) {
        final AccessToken token = authenticator.getToken();
        final Set<String> roles = Optional.ofNullable(token.getRealmAccess())
                .map(AccessToken.Access::getRoles)
                .orElse(new HashSet<>());

        Set<String> resourceRoles = Optional.ofNullable(token.getResourceAccess(resourceName))
                .map(AccessToken.Access::getRoles)
                .orElse(Collections.emptySet());
        roles.addAll(resourceRoles);

        final JwtPrincipal principal = JwtPrincipal.Builder(token.getSubject())
                .setEmail(token.getEmail())
                .setFamilyName(token.getFamilyName())
                .setFullName(token.getName())
                .setGivenName(token.getGivenName())
                .setUserName(token.getPreferredUsername())
                .setPicture(token.getPicture())
                .setClaims(token.getOtherClaims())
                .withToken(authenticator.getTokenString())
                .build();

        CredentialValidationResult credentials = new CredentialValidationResult(principal, roles);

        return httpMessageContext.notifyContainerAboutLogin(credentials);
    }
}
