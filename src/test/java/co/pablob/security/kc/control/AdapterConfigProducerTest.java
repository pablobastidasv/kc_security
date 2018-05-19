package co.pablob.security.kc.control;

import org.junit.Test;
import org.keycloak.representations.adapters.config.AdapterConfig;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.eclipse.microprofile.config.inject.ConfigProperty.UNCONFIGURED_VALUE;
import static org.junit.Assert.assertEquals;

public class AdapterConfigProducerTest {

    private static final String EXPECTED_SERVICE_URL = "http://auth_service/auth";
    private static final String EXPECTED_CLIENT_ID = "services";
    private static final String EXPECTED_REALM = "dev";

    @Test
    public void whenConfigurationIsDirect_shouldGenerateCorrectAdapter() throws Exception {
        // given
        AdapterConfigProducer adapterConfigProducer = new AdapterConfigProducer();

        adapterConfigProducer.filePath = UNCONFIGURED_VALUE;
        adapterConfigProducer.authServerUrl = EXPECTED_SERVICE_URL;
        adapterConfigProducer.clientId = EXPECTED_CLIENT_ID;
        adapterConfigProducer.realm = EXPECTED_REALM;

        // when
        final AdapterConfig adapterConfig = adapterConfigProducer.produceAdapterConfig();

        // then
        assertEquals(EXPECTED_SERVICE_URL, adapterConfig.getAuthServerUrl());
        assertEquals(EXPECTED_CLIENT_ID, adapterConfig.getResource());
        assertEquals(EXPECTED_REALM, adapterConfig.getRealm());
    }

    @Test
    public void whenConfigurationIsInFile_shouldGenerateCorrectAdapter() throws Exception {
        // given
        AdapterConfigProducer adapterConfigProducer = new AdapterConfigProducer();

        Path configFile = Paths.get(getClass().getResource("/keycloak.json").toURI());

        adapterConfigProducer.filePath = configFile.toRealPath().toString();
        adapterConfigProducer.authServerUrl = UNCONFIGURED_VALUE;
        adapterConfigProducer.clientId = UNCONFIGURED_VALUE;
        adapterConfigProducer.realm = UNCONFIGURED_VALUE;

        // when
        final AdapterConfig adapterConfig = adapterConfigProducer.produceAdapterConfig();

        // then
        assertEquals(EXPECTED_SERVICE_URL, adapterConfig.getAuthServerUrl());
        assertEquals(EXPECTED_CLIENT_ID, adapterConfig.getResource());
        assertEquals(EXPECTED_REALM, adapterConfig.getRealm());
    }
}