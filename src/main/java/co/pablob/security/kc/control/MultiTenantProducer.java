package co.pablob.security.kc.control;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

public interface MultiTenantProducer {
    InputStream adapterConfigFromRequest(HttpServletRequest request) throws IOException;
}
