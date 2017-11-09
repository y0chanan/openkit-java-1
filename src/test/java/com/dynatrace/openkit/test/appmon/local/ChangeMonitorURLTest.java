/***************************************************
 * (c) 2016-2017 Dynatrace LLC
 *
 * @author: Christian Schwarzbauer
 */
package com.dynatrace.openkit.test.appmon.local;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import com.dynatrace.openkit.protocol.HTTPClient.RequestType;
import com.dynatrace.openkit.test.TestHTTPClient.Request;
import com.dynatrace.openkit.test.shared.ChangeMonitorURLTestShared;

public class ChangeMonitorURLTest extends AbstractLocalAppMonTest {

	public void setup() throws InterruptedException {
		ChangeMonitorURLTestShared.setup(testConfiguration);
		super.setup();
	}

	@Test
	public void test() {
		ChangeMonitorURLTestShared.test(openKit, TEST_IP);

		ArrayList<Request> sentRequests = openKitTestImpl.getSentRequests();
		String expectedBeacon = "vv=3&va=7.0.0000&ap=" + TEST_APPLICATION_ID + "&an=" + TEST_APPLICATION_NAME + "&pt=1&vi=" + testConfiguration.getVisitorID() + "&sn=1&ip=" + TEST_IP + "&tv=1005000&ts=1004000&tx=1009000&et=19&it=1&pa=0&s0=3&t0=4000&et=1&na=ChangeMonitorURLAction&it=1&ca=1&pa=0&s0=1&t0=2000&s1=2&t1=1000";
		Assert.assertEquals(3, sentRequests.size());
		validateRequest(sentRequests.get(0), RequestType.STATUS, "GET", getDefaultEndpoint(), null, "");
		validateRequest(sentRequests.get(1), RequestType.TIMESYNC, "GET", TEST_ENDPOINT + "changedMonitorURL?type=mts", null, "");
		validateRequest(sentRequests.get(2), RequestType.BEACON, "POST", TEST_ENDPOINT + "changedMonitorURL?type=m&srvid=1&app=" + TEST_APPLICATION_ID + "&va=7.0.0000", TEST_IP, expectedBeacon);
	}

}
