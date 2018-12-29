package co.pablob.security.kc.control;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.KeycloakDeploymentBuilder;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URISyntaxException;

@ApplicationScoped
public class DeploymentProducer {

    @Inject
    private AdapterConfigProducer adapterConfigProducer;

    @Inject
    @ConfigProperty(name = "security.kc.multiTenant.enabled", defaultValue = "false")
    boolean multiTenant;

    private KeycloakDeployment deployment;

    public KeycloakDeployment getDeployment(HttpServletRequest request) throws IOException, URISyntaxException {
        if(multiTenant){
            return KeycloakDeploymentBuilder.build(adapterConfigProducer.produceAdapterConfig(request));
        }

        if(deployment == null){
            deployment = KeycloakDeploymentBuilder.build(adapterConfigProducer.produceAdapterConfig(request));
        }

        return deployment;
    }
}
