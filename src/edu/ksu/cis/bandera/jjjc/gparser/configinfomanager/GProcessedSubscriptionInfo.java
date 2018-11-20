package edu.ksu.cis.bandera.jjjc.gparser.configinfomanager;

import java.util.List;

import edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor.GSubscriptionInfo;

public class GProcessedSubscriptionInfo {
	public String inputName; /* variable name: GoodNight_motionSensors */
	public List<GSubscriptionInfo> subscriptionInfoList;

	public GProcessedSubscriptionInfo(String inputName, List<GSubscriptionInfo> subscriptionInfoList) {
		this.inputName = inputName;
		this.subscriptionInfoList = subscriptionInfoList;
	}
}
