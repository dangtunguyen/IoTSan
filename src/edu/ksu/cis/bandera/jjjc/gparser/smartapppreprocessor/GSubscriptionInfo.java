package edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor;

public class GSubscriptionInfo {
	public String inputName; /* variable name: GoodNight_motionSensors */
	public String subscribedAttribute;
	public String subscribedEvtType;
	public String evtHandler;
	
	public GSubscriptionInfo(String name, String attribute, String evtType, String evtHandler)
	{
		this.inputName = name;
		this.subscribedAttribute = attribute;
		this.subscribedEvtType = evtType.replaceAll("\\s", "");
		this.evtHandler = evtHandler;
	}
	
	public GSubscriptionInfo clone()
	{
		return new GSubscriptionInfo(this.inputName, this.subscribedAttribute, this.subscribedEvtType, this.evtHandler);
	}
}
