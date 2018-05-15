package co.pablob.security.commons.boundary;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;

import co.pablob.security.commons.entity.JwtPrincipal;

@ApplicationScoped
public class JwtPrincipalProvider {

    @Inject
    @SuppressWarnings("CdiInjectionPointsInspection")
    private SecurityContext securityContext;

    public JwtPrincipal produceJwtPrincipal(){
        return (JwtPrincipal) securityContext.getCallerPrincipal();
    }

}
