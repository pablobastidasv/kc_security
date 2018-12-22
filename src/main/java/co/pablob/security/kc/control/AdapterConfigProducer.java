package co.pablob.security.kc.control;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.keycloak.adapters.KeycloakDeploymentBuilder;
import org.keycloak.representations.adapters.config.AdapterConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

@SuppressWarnings("CdiInjectionPointsInspection")
public class AdapterConfigProducer {

    private static final String FILE_PATH_PROPERTY = "security.kc.file-path";
    private static final String DEFAULT_CONFIG_LOCATION = "/keycloak.json";

    static final String UNCONFIGURED_VALUE = "NA";

    private static final String ERROR_CONFIG_FILE_NOT_EXISTS = "Configuration file '%s' does not exist.";
    private static final String ERROR_KEYCLOAK_NOT_CONFIGURED = "Any Keycloak configuration provided, please check " +
            "https://goo.gl/oKpiiU for more information.";

    @Inject
    private Instance<MultiTenantProducer> multiTenantProducer;

    @Inject
    @ConfigProperty(name = FILE_PATH_PROPERTY, defaultValue = UNCONFIGURED_VALUE)
    String filePath;
    @Inject
    @ConfigProperty(name = "security.kc.realm", defaultValue = UNCONFIGURED_VALUE)
    String realm;
    @Inject
    @ConfigProperty(name = "security.kc.authServerUrl", defaultValue = UNCONFIGURED_VALUE)
    String authServerUrl;
    @Inject
    @ConfigProperty(name = "security.kc.clientId", defaultValue = UNCONFIGURED_VALUE)
    String clientId;
    @Inject
    @ConfigProperty(name = "security.kc.multiTenant.enabled", defaultValue = "false")
    boolean multiTenant;

    public AdapterConfig produceAdapterConfig(HttpServletRequest request) throws IOException, URISyntaxException {
        if(multiTenant && !(multiTenantProducer.isAmbiguous() || multiTenantProducer.isUnsatisfied())) {
            // When configuration must be provided by request because of multi-tenant support.
            InputStream is = multiTenantProducer.get().adapterConfigFromRequest(request);
            return KeycloakDeploymentBuilder.loadAdapterConfig(is);
        } else if(!UNCONFIGURED_VALUE.equals(filePath)) {
            // When filepath is defined
            return adapterConfigFromFile();
        } else if(Objects.nonNull(getClass().getResource(DEFAULT_CONFIG_LOCATION))) {
            // When keycloak.json is present in project's /
            return adapterConfigFromDefaultLocation();
        } else if(isConfigParamsProvided()){
            // When configuration is defined in env var
            return adapterConfigFromProperties();
        } else {
            throw new IllegalStateException(ERROR_KEYCLOAK_NOT_CONFIGURED);
        }
    }

    private boolean isConfigParamsProvided() {
        return !(UNCONFIGURED_VALUE.equals(realm) ||
                UNCONFIGURED_VALUE.equals(authServerUrl) ||
                UNCONFIGURED_VALUE.equals(clientId));
    }

    private AdapterConfig adapterConfigFromDefaultLocation() throws IOException, URISyntaxException {
        Path is = Paths.get(getClass().getResource(DEFAULT_CONFIG_LOCATION).toURI());
        return getAdapterConfigFromPath(is);
    }

    private AdapterConfig adapterConfigFromFile() throws IOException {
        final Path path = Paths.get(filePath);

        return getAdapterConfigFromPath(path);
    }

    private AdapterConfig getAdapterConfigFromPath(Path path) throws IOException {
        if(Files.exists(path)){
            final InputStream is = Files.newInputStream(path);
            return KeycloakDeploymentBuilder.loadAdapterConfig(is);
        } else {
            final String message = String.format(ERROR_CONFIG_FILE_NOT_EXISTS, path.toString());
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
