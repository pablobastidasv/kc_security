package co.pablob.security.commons.boundary;

import co.pablob.security.commons.entity.JwtPrincipal;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;

public class JwtPrincipalProvider {

    @Inject
    private SecurityContext securityContext;

    @Produces
    @RequestScoped
    public JwtPrincipal produceJwtPrincipal(){
        return securityContext.getPrincipalsByType(JwtPrincipal.class)
                .toArray(new JwtPrincipal[0])[0];
    }

}
