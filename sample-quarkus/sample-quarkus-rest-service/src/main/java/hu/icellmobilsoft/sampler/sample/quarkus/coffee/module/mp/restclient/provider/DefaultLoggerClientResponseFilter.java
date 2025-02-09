/*-
 * #%L
 * Coffee
 * %%
 * Copyright (C) 2020 i-Cell Mobilsoft Zrt.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package hu.icellmobilsoft.sampler.sample.quarkus.coffee.module.mp.restclient.provider;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.Priority;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.core.NewCookie;

import org.apache.commons.io.IOUtils;

import hu.icellmobilsoft.coffee.cdi.logger.AppLogger;
import hu.icellmobilsoft.coffee.cdi.logger.ThisLogger;
import hu.icellmobilsoft.coffee.tool.utils.string.StringHelper;
import hu.icellmobilsoft.sampler.sample.quarkus.coffee.filter.RequestResponseLogger;
import hu.icellmobilsoft.sampler.sample.quarkus.coffee.module.mp.restclient.RestClientPriority;

/**
 * Default REST client response log filter. This should be the first among the response filters in order to log untouched data.<br>
 * <br>
 * The priority should be higher than the io.smallrye.restclient.ExceptionMapping binding, currently set to "1"
 *
 * @author imre.scheffer
 * @since 1.0.0
 */
@Priority(value = RestClientPriority.RESPONSE_LOG)
@Dependent
public class DefaultLoggerClientResponseFilter implements ClientResponseFilter {

    @Inject
    @ThisLogger
    AppLogger log;

    @Inject
    RequestResponseLogger requestResponseLogger;

    /** {@inheritDoc} */
    @Override
    public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
        // TODO log check
        // if (RestLoggerUtil.logDisabled(requestContext, LogSpecifierTarget.CLIENT_RESPONSE)) {
        // return;
        // }
        StringBuilder msg = new StringBuilder();
        msg.append("<< ").append(getClass().getName()).append(" response from [").append(requestContext.getUri()).append("] ->\n");
        msg.append(logStatus(requestContext, responseContext));
        msg.append(logHeader(requestContext, responseContext));
        msg.append(logEntity(requestContext, responseContext));
        log.info(msg.toString());
    }

    /**
     * Logs HTTP status data.
     *
     * @param requestContext
     *            request context
     * @param responseContext
     *            response context
     * @return status {@link String}
     * @throws IOException
     *             exception
     */
    protected String logStatus(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
        StringBuilder msg = new StringBuilder();
        msg.append("< status: [");
        if (responseContext.getStatusInfo() != null) {
            msg.append(responseContext.getStatusInfo().getStatusCode());
            msg.append("]; family: [").append(responseContext.getStatusInfo().getFamily());
            msg.append("]; reasonPhrase: [").append(responseContext.getStatusInfo().getReasonPhrase()).append("]");
        }
        msg.append("]\n");
        return msg.toString();
    }

    /**
     * Logs HTTP header data.
     *
     * @param requestContext
     *            request context
     * @param responseContext
     *            response context
     * @return header {@link String}
     * @throws IOException
     *             exception
     */
    protected String logHeader(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
        StringBuilder msg = new StringBuilder();
        msg.append("< headers: [");
        for (Entry<String, List<String>> header : responseContext.getHeaders().entrySet()) {
            msg.append("\n<    ").append(header.getKey()).append(":").append(StringHelper.maskPropertyValue(header.getKey(), header.getValue()));
        }
        msg.append("]\n");
        msg.append("< cookies: [");
        for (Entry<String, NewCookie> cookie : responseContext.getCookies().entrySet()) {
            msg.append("\n<    ").append(cookie.getKey()).append(":").append(StringHelper.maskPropertyValue(cookie.getKey(), cookie.getValue()));
        }
        msg.append("]\n");
        msg.append("< locale: [").append(responseContext.getLanguage()).append("]\n");
        msg.append("< location: [").append(responseContext.getLocation()).append("]\n");
        return msg.toString();
    }

    /**
     * Logs entity.
     *
     * @param requestContext
     *            request context
     * @param responseContext
     *            response context
     * @return entity {@link String}
     * @throws IOException
     *             exception
     * @see RequestResponseLogger#printEntity(byte[], Integer, String)
     */
    protected String logEntity(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
        if (requestContext == null || responseContext == null) {
            return null;
        }
        InputStream in = responseContext.getEntityStream();
        try {
            int maxResponseEntityLogSize = getMaxResponseEntityLogSize(requestContext, responseContext);

            if (in == null) {
                return requestResponseLogger.printEntity(null, maxResponseEntityLogSize, RequestResponseLogger.RESPONSE_PREFIX);
            }

            byte[] responseEntity = IOUtils.toByteArray(in);
            // We write back the read stream
            responseContext.setEntityStream(new ByteArrayInputStream(responseEntity));

            return requestResponseLogger.printEntity(responseEntity, maxResponseEntityLogSize, RequestResponseLogger.RESPONSE_PREFIX);
        } catch (IOException e) {
            log.error("Error in logging response entity: " + e.getLocalizedMessage(), e);
            return null;
        }
    }

    private int getMaxResponseEntityLogSize(ClientRequestContext requestContext, ClientResponseContext responseContext) {
        // TODO calc log size
        // int maxResponseEntityLogSize = RestLoggerUtil.getMaxEntityLogSize(requestContext, LogSpecifierTarget.CLIENT_RESPONSE);
        // if (maxResponseEntityLogSize != LogSpecifier.NO_LOG &&
        // // byte-code betoltesi fajlokat ne loggoljuk ki egeszben
        // Objects.equals(responseContext.getMediaType(), MediaType.APPLICATION_OCTET_STREAM_TYPE)) {
        // maxResponseEntityLogSize = RequestResponseLogger.BYTECODE_MAX_LOG;
        //
        // }
        // return maxResponseEntityLogSize;

        return -1;
    }

}
