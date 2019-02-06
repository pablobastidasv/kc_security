package co.pablob.security.kc.control;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

public interface MultiTenantProducer {

    /**
     *  Based on the request will create an {@code InputStream} with the keycloak realm configuration information.
     *
     * @param request The incoming request
     * @return An input stream with the Realm information, normally a json file provided by Keycloak
     * @throws IOException In case the {@code InputStream} generation present some IO error.
     * @deprecated due to performance issues, now this method will be not used, this will make a passthrough
     *      to {@link MultiTenantProducer::obtainRealmNameKey} which receives a {@code String} passing value
     *      from {@link HttpServletRequest::getServerName}.
     */
    @Deprecated
    default InputStream adapterConfigFromRequest(HttpServletRequest request) throws IOException {
        return adapterConfigFromRequest(request.getServerName());
    };


    /**
     *  Based on the a String to identify the key of the realm, create an {@code InputStream} with the
     *  keycloak realm configuration information.
     *
     * @param realmKeyName a {@code String} which represents the information of the realm to build.
     * @return An input stream with the Realm information, normally a json file provided by Keycloak
     * @throws IOException In case the {@code InputStream} generation present some IO error.
     */
    InputStream adapterConfigFromRequest(String realmKeyName) throws IOException;


    default String obtainRealmNameKey(HttpServletRequest request){
        return request.getServerName();
    }
}
