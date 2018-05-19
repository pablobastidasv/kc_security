package co.pablob.security.kc.control;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.keycloak.adapters.KeycloakDeploymentBuilder;
import org.keycloak.representations.adapters.config.AdapterConfig;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import static org.eclipse.microprofile.config.inject.ConfigProperty.UNCONFIGURED_VALUE;

@SuppressWarnings("CdiInjectionPointsInspection")
public class AdapterConfigProducer {

    private static final String FILE_PATH_PROPERTY = "security.kc.file-path";

    @Inject
    @ConfigProperty(name = FILE_PATH_PROPERTY)
    String filePath;
    @Inject
    @ConfigProperty(name = "security.kc.realm")
    String realm;
    @Inject
    @ConfigProperty(name = "security.kc.authServerUrl")
    String authServerUrl;
    @Inject
    @ConfigProperty(name = "security.kc.clientId")
    String clientId;

    @Produces
    public AdapterConfig produceAdapterConfig() throws IOException {

        if(UNCONFIGURED_VALUE.equals(filePath)) {
            return adapterConfigFromProperties();
        } else {
            return adapterConfigFromFile();
        }
    }

    private AdapterConfig adapterConfigFromFile() throws IOException {
        final Path path = Paths.get(filePath);

        if(Files.exists(path)){
            final InputStream is = Files.newInputStream(path);
            return KeycloakDeploymentBuilder.loadAdapterConfig(is);
        } else {
            final String message = String.format("Configuration file '%s' does not exist.", path.toString());
            throw new IllegalArgumentException(message);
        }
    }

    private AdapterConfig adapterConfigFromProperties() {
        final AdapterConfig adapterConfig = new AdapterConfig();

        adapterConfig.setRealm(realm);
        adapterConfig.setAuthServerUrl(authServerUrl);
        adapterConfig.setResource(clientId);
        adapterConfig.setSslRequired("external");
        adapterConfig.setBearerOnly(true);
        adapterConfig.setConfidentialPort(0);

        return adapterConfig;
    }

}
