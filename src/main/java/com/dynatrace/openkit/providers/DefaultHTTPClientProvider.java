/***************************************************
 * (c) 2016-2017 Dynatrace LLC
 *
 * @author: Christian Schwarzbauer
 */
package com.dynatrace.openkit.providers;

import com.dynatrace.openkit.api.Logger;
import com.dynatrace.openkit.core.configuration.HTTPClientConfiguration;
import com.dynatrace.openkit.protocol.HTTPClient;

/**
 * Implementation of an HTTPClientProvider which creates a HTTP client for executing status check, beacon send and time sync requests.
 */
public class DefaultHTTPClientProvider implements HTTPClientProvider {

	private final Logger logger;

	public DefaultHTTPClientProvider(Logger logger) {
		this.logger = logger;
	}

	@Override
	public HTTPClient createClient(HTTPClientConfiguration configuration) {
		return new HTTPClient(logger, configuration);
	}

}
