package co.pablob.security.kc.control;

import org.junit.Before;
import org.junit.Test;
import org.keycloak.representations.adapters.config.AdapterConfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.nio.file.Path;
import java.nio.file.Paths;

import static co.pablob.security.kc.control.AdapterConfigProducer.UNCONFIGURED_VALUE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

public class AdapterConfigProducerTest {

    private static final String EXPECTED_SERVICE_URL = "http://auth_service/auth";
    private static final String EXPECTED_CLIENT_ID = "services";
    private static final String EXPECTED_REALM = "dev";

    private HttpServletRequest request;

    @Before
    public void setUp() throws Exception {
        request = mock(HttpServletRequest.class);
    }

    @Test
    public void whenConfigurationIsDirect_shouldGenerateCorrectAdapter() throws Exception {
        // given
        AdapterConfigProducer adapterConfigProducer = new AdapterConfigProducer();

        adapterConfigProducer.filePath = UNCONFIGURED_VALUE;
        adapterConfigProducer.authServerUrl = EXPECTED_SERVICE_URL;
        adapterConfigProducer.clientId = EXPECTED_CLIENT_ID;
        adapterConfigProducer.realm = EXPECTED_REALM;

        // when
        final AdapterConfig adapterConfig = adapterConfigProducer.produceAdapterConfig(request);

        // then
        assertEquals(EXPECTED_SERVICE_URL, adapterConfig.getAuthServerUrl());
        assertEquals(EXPECTED_CLIENT_ID, adapterConfig.getResource());
        assertEquals(EXPECTED_REALM, adapterConfig.getRealm());
    }

    @Test
    public void whenConfigurationIsInFilePath_shouldGenerateCorrectAdapter() throws Exception {
        // given
        AdapterConfigProducer adapterConfigProducer = new AdapterConfigProducer();

        Path configFile = Paths.get(getClass().getResource("/keycloak.json").toURI());

        adapterConfigProducer.filePath = configFile.toRealPath().toString();
        adapterConfigProducer.authServerUrl = UNCONFIGURED_VALUE;
        adapterConfigProducer.clientId = UNCONFIGURED_VALUE;
        adapterConfigProducer.realm = UNCONFIGURED_VALUE;

        // when
        final AdapterConfig adapterConfig = adapterConfigProducer.produceAdapterConfig(request);

        // then
        assertEquals(EXPECTED_SERVICE_URL, adapterConfig.getAuthServerUrl());
        assertEquals(EXPECTED_CLIENT_ID, adapterConfig.getResource());
        assertEquals(EXPECTED_REALM, adapterConfig.getRealm());
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenConfigurationIsInFilePathAndDoesNotExist_shouldThrowException() throws Exception {
        // given
        AdapterConfigProducer adapterConfigProducer = new AdapterConfigProducer();

        adapterConfigProducer.filePath = "/opt/test/notfolder/keycloak.json";
        adapterConfigProducer.authServerUrl = UNCONFIGURED_VALUE;
        adapterConfigProducer.clientId = UNCONFIGURED_VALUE;
        adapterConfigProducer.realm = UNCONFIGURED_VALUE;

        // when
        adapterConfigProducer.produceAdapterConfig(request);
        fail("Exception should be throws");
    }

    @Test
    public void whenConfigurationIsInDefaultFile_shouldGenerateCorrectAdapter() throws Exception {
        // given
        AdapterConfigProducer adapterConfigProducer = new AdapterConfigProducer();

        adapterConfigProducer.filePath = UNCONFIGURED_VALUE;
        adapterConfigProducer.authServerUrl = UNCONFIGURED_VALUE;
        adapterConfigProducer.clientId = UNCONFIGURED_VALUE;
        adapterConfigProducer.realm = UNCONFIGURED_VALUE;

        // when
        final AdapterConfig adapterConfig = adapterConfigProducer.produceAdapterConfig(request);

        // then
        assertEquals(EXPECTED_SERVICE_URL, adapterConfig.getAuthServerUrl());
        assertEquals(EXPECTED_CLIENT_ID, adapterConfig.getResource());
        assertEquals(EXPECTED_REALM, adapterConfig.getRealm());
    }
}