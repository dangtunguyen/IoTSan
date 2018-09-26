package edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor;

public class GOtherInputInfo extends GInputInfo{
	public String infoType;
	
	public GOtherInputInfo(String name, String type, boolean isMultiple)
	{
		this.inputName = name;
		this.isMultiple = isMultiple;
		
		switch(type)
		{
		case "bool":
		case "boolean": this.infoType = "BOOL"; break;
		case "contact": this.infoType = "CONTACT"; break;
		case "decimal": this.infoType = "DECIMAL"; break;
		case "email": this.infoType = "EMAIL"; break;
		case "enum": this.infoType = "ENUM"; break;
		case "hub": this.infoType = "HUB"; break;
		case "icon": this.infoType = "ICON"; break;
		case "number": this.infoType = "NUMBER"; break;
		case "password": this.infoType = "PASSWORD"; break;
		case "phone": this.infoType = "PHONE"; break;
		case "time": this.infoType = "TIME"; break;
		case "text": this.infoType = "TEXT"; break;
		case "mode": this.infoType = "MODE"; break;
		default: this.infoType = "UNKNOWN"; break;
		}
	}
	
	public GInputInfo clone()
	{
		return (new GOtherInputInfo(this.inputName, this.infoType, this.isMultiple));
	}
}
