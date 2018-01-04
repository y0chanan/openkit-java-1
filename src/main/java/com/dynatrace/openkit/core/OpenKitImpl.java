/**
 * Copyright 2018 Dynatrace LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dynatrace.openkit.core;

import com.dynatrace.openkit.api.Logger;
import com.dynatrace.openkit.api.OpenKit;
import com.dynatrace.openkit.api.Session;
import com.dynatrace.openkit.core.configuration.Configuration;
import com.dynatrace.openkit.protocol.Beacon;
import com.dynatrace.openkit.providers.*;

/**
 * Actual implementation of the {@link OpenKit} interface.
 */
public class OpenKitImpl implements OpenKit {

    // BeaconSender reference
    private final BeaconSender beaconSender;

    // AbstractConfiguration reference
    private final Configuration configuration;
    private final ThreadIDProvider threadIDProvider;
    private final TimingProvider timingProvider;

    //Logging context
    private final Logger logger;

    // *** constructors ***

    public OpenKitImpl(Logger logger, Configuration config) {
        this(logger, config, new DefaultHTTPClientProvider(logger), new DefaultTimingProvider(), new DefaultThreadIDProvider());
    }

    protected OpenKitImpl(Logger logger, Configuration config, HTTPClientProvider httpClientProvider, TimingProvider timingProvider, ThreadIDProvider threadIDProvider) {
        configuration = config;
        this.logger = logger;
        this.threadIDProvider = threadIDProvider;
        this.timingProvider = timingProvider;
        beaconSender = new BeaconSender(configuration, httpClientProvider, timingProvider);
    }

    /**
     * Initialize this OpenKit instance.
     * <p>
     * <p>
     * This method starts the {@link BeaconSender} and is called directly after
     * the instance has been created in {@link com.dynatrace.openkit.AbstractOpenKitBuilder}.
     * </p>
     */
    public void initialize() {
        beaconSender.initialize();
    }

    // *** OpenKit interface methods ***

    @Override
    public boolean waitForInitCompletion() {
        return beaconSender.waitForInit();
    }

    @Override
    public boolean waitForInitCompletion(long timeoutMillis) {
        return beaconSender.waitForInit(timeoutMillis);
    }

    @Override
    public boolean isInitialized() {
        return beaconSender.isInitialized();
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    @Override
    public Session createSession(String clientIPAddress) {
        // create beacon for session
        Beacon beacon = new Beacon(logger, configuration, clientIPAddress, threadIDProvider, timingProvider);
        // create session
        return new SessionImpl(beaconSender, beacon);
    }

    @Override
    public void shutdown() {
        beaconSender.shutdown();
    }
}
