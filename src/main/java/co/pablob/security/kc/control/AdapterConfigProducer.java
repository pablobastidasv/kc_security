package co.pablob.security.kc.control;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.keycloak.representations.adapters.config.AdapterConfig;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

@SuppressWarnings("CdiInjectionPointsInspection")
public class AdapterConfigProducer {

    @Inject
    @ConfigProperty(name = "security.kc.realm")
    private String realm;
    @Inject
    @ConfigProperty(name = "security.kc.authServerUrl")
    private String authServerUrl;
    @Inject
    @ConfigProperty(name = "security.kc.clientId")
    private String clientId;

    @Produces
    public AdapterConfig produceAdapterConfig(){
        final AdapterConfig adapterConfig = new AdapterConfig();

        adapterConfig.setRealm(realm);
        adapterConfig.setBearerOnly(true);
        adapterConfig.setAuthServerUrl(authServerUrl);
        adapterConfig.setSslRequired("external");
        adapterConfig.setResource(clientId);
        adapterConfig.setConfidentialPort(0);

        return adapterConfig;
    }

}
