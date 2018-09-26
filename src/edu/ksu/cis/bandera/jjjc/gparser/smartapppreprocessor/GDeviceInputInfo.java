package edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor;

public class GDeviceInputInfo extends GInputInfo{
	public String deviceType;
	
	public GDeviceInputInfo(String deviceName, String deviceType, boolean isMultiple)
	{
		String lowerCasedeviceType = deviceType.toLowerCase();
		this.inputName = deviceName;
		this.isMultiple = isMultiple;
		
		switch(lowerCasedeviceType)
		{
		case "accelerationsensor": this.deviceType = "STAccSensor"; break;
		case "actuator": this.deviceType = "STActuator"; break;
		case "aeonkeyfob": this.deviceType = "STAeonKeyFob"; break;
		case "alarm": this.deviceType = "STAlarm"; break;
		case "battery": this.deviceType = "STBattery"; break;
		case "beacon": this.deviceType = "STBeacon"; break;
		case "button": this.deviceType = "STButton"; break;
		case "carbonmonoxidedetector": this.deviceType = "STCarMoDetector"; break;
		case "colorcontrol": this.deviceType = "STColorCtrl"; break;
		case "colortemperature": this.deviceType = "STColorTemp"; break;
		case "configuration": this.deviceType = "STConfiguration"; break;
		case "contactsensor": this.deviceType = "STContactSensor"; break;
		case "doorcontrol": this.deviceType = "STDoorControl"; break;
		case "energymeter": this.deviceType = "STEnergyMeter"; break;
		case "estimatedtimeofarrival": this.deviceType = "STEstTimeOfArr"; break;
		case "garagedoorcontrol": this.deviceType = "STGarDoCtrl"; break;
		case "healthcheck": this.deviceType = "STHealthCheck"; break;
		case "illuminancemeasurement": this.deviceType = "STIlMeas"; break;
		case "imagecapture": this.deviceType = "STImageCapture"; break;
		case "indicator": this.deviceType = "STIndicator"; break;
		case "locationmode": this.deviceType = "STLocMode"; break;
		case "lock": this.deviceType = "STLock"; break;
		case "lockcodes": this.deviceType = "STLockCodes"; break;
		case "mediacontroller": this.deviceType = "STMediaCtrler"; break;
		case "momentary": this.deviceType = "STMomentary"; break;
		case "motionsensor": this.deviceType = "STMotionSensor"; break;
		case "musicplayer": this.deviceType = "STMusicPlayer"; break;
		case "notification": this.deviceType = "STNotification"; break;
		case "petfeedershield": this.deviceType = "STPetFeederShield"; break;
		case "polling": this.deviceType = "STPolling"; break;
		case "powermeter": this.deviceType = "STPowerMeter"; break;
		case "presencesensor": this.deviceType = "STPresSensor"; break;
		case "refresh": this.deviceType = "STRefresh"; break;
		case "relativehumiditymeasurement": this.deviceType = "STRelHumMeas"; break;
		case "relayswitch": this.deviceType = "STRelSwitch"; break;
		case "sensor": this.deviceType = "STSensor"; break;
		case "signalstrength": this.deviceType = "STSigStrength"; break;
		case "sleepsensor": this.deviceType = "STSleepSensor"; break;
		case "smartweatherstationtile": this.deviceType = "STSmartWethStationTile"; break;
		case "smokedetector": this.deviceType = "STSmokeDetector"; break;
		case "speechrecognition": this.deviceType = "STSpeechRecog"; break;
		case "speechsynthesis": this.deviceType = "STSpeechSyn"; break;
		case "stepsensor": this.deviceType = "STStepSensor"; break;
		case "switch": this.deviceType = "STSwitch"; break;
		case "switchlevel": this.deviceType = "STSwitchLevel"; break;
		case "temperaturemeasurement": this.deviceType = "STTempMeas"; break;
		case "testcapability": this.deviceType = "STTestCap"; break;
		case "thermostat": this.deviceType = "STThermostat"; break;
		case "thermostatcoolingsetpoint": this.deviceType = "STTherCoSetpoint"; break;
		case "thermostatfanmode": this.deviceType = "STTherFanMode"; break;
		case "thermostatheatingsetpoint": this.deviceType = "STTherHeatSetpoint"; break;
		case "thermostatmode": this.deviceType = "STTherMode"; break;
		case "thermostatoperatingstate": this.deviceType = "STTherOpState"; break;
		case "thermostatschedule": this.deviceType = "STTherSchedule"; break;
		case "thermostatsetpoint": this.deviceType = "STTherSetpoint"; break;
		case "threeaxis": this.deviceType = "STThreeAxis"; break;
		case "tone": this.deviceType = "STTone"; break;
		case "touchsensor": this.deviceType = "STTouchSensor"; break;
		case "tv": this.deviceType = "STTV"; break;
		case "ultravioletindex": this.deviceType = "STUltrIndex"; break;
		case "valve": this.deviceType = "STValve"; break;
		case "videocamera": this.deviceType = "STVideoCamera"; break;
		case "videocapture": this.deviceType = "STVideoCapture"; break;
		case "watersensor": this.deviceType = "STWaterSensor"; break;
		case "windowshade": this.deviceType = "STWindowShade"; break;
		case "zwmultichannel": this.deviceType = "STZwMultichan"; break;
		default: this.deviceType = deviceType; break;
		}
	}
	
	public GInputInfo clone()
	{
		return (new GDeviceInputInfo(this.inputName, this.deviceType, this.isMultiple));
	}
}
