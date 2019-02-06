package co.pablob.security.kc.control;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.KeycloakDeploymentBuilder;
import org.keycloak.representations.adapters.config.AdapterConfig;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class DeploymentProducer {

    @Inject
    private AdapterConfigProducer adapterConfigProducer;

    @Inject
    @ConfigProperty(name = "security.kc.multiTenant.enabled", defaultValue = "false")
    boolean multiTenant;

    private final Map<String, KeycloakDeployment> deployments = new ConcurrentHashMap<>();

    public KeycloakDeployment getDeployment(HttpServletRequest request) {
        String realmName = adapterConfigProducer.obtainRealmNameKey(request);

        return deployments.computeIfAbsent(realmName, key -> KeycloakDeploymentBuilder.build(getAdapterConfig(key)));
    }

    private AdapterConfig getAdapterConfig(String realmName)  {
        try {
            return adapterConfigProducer.produceAdapterConfig(realmName);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
