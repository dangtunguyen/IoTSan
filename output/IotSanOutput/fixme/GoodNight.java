public class GoodNight 
{
     static int STCurrentSystemTime;
     static int GoodNight_sendPushMessage;
     static int GoodNight_modeStartTime;
     static STNetworkManager _STNetworkManager;
     static int GoodNight_timeOfDay;
     static int GoodNight_newMode;
     static int GoodNight_minutes;
     static STApp GoodNight_app;
     static int GoodNight_recipients;
     static STMotionSensor[] GoodNight_motionSensors;
     static STLocation location;
     static STEvent evt;
     static STSwitch[] GoodNight_switches;
     static int GoodNight_phoneNumber;
     
     static int now ()
     {
          return (_static_STCurrentSystemTime * 3600000);
     }
     
     static void increaseSTSystemTime (int amount)
     {
          int h = ((amount / 3600) + 1);
          STCurrentSystemTime = (STCurrentSystemTime + h);
     }
     
     static int timeTodayAfter (int[] startTimeString, int timeString, int timeZone)
     {
          return (timeString * 3600000);
     }
     
     static int timeTodayAfter (int[] startTimeString, int[] timeString)
     {
          return (timeString * 3600000);
     }
     
     static int timeTodayAfter (int startTime, int timeVal, int timeZone)
     {
          return (timeVal * 3600000);
     }
     
     static int timeTodayAfter (int startTime, int timeVal)
     {
          return (timeVal * 3600000);
     }
     
     static int timeToday (int startTime, int timeZone)
     {
          return startTime;
     }
     
     static int timeToday (int startTime)
     {
          return startTime;
     }
     
     static void setLocationMode (int mode)
     {
          if (mode == 1400)
          {
               _static_location.STCommand_Location_Home = true;
          } else
          {
               if (mode == 1401)
               {
                    _static_location.STCommand_Location_Away = true;
               } else
               {
                    if (mode == 1402)
                    {
                         _static_location.STCommand_Location_Night = true;
                    }
               }
          }
     }
     
     static void setLocationMode (STMode mode)
     {
          if (mode.name == 1400)
          {
               _static_location.STCommand_Location_Home = true;
          } else
          {
               if (mode.name == 1401)
               {
                    _static_location.STCommand_Location_Away = true;
               } else
               {
                    if (mode.name == 1402)
                    {
                         _static_location.STCommand_Location_Night = true;
                    }
               }
          }
     }
     
     STSunriseSunset getSunriseAndSunset ()
     {
          return _static_location.sunriseSunset;
     }
     
     public STWeatherFeature getWeatherFeature (int feature)
     {
          return _static_location.weatherFeature;
     }
     
     public STWeatherFeature getWeatherFeature (int feature, int zipcode)
     {
          return _static_location.weatherFeature;
     }
     
     static void sendNotificationToContacts (int msg, int[] recipients)
     {
     }
     
     static void sendPush (int msg)
     {
     }
     
     static void sendSms (int phoneNumb, int msg)
     {
          _static__STNetworkManager.receivedPhoneNumber = phoneNumb;
          _static__STNetworkManager.STCommand_sendSms = true;
     }
     
     static void httpPost ()
     {
          _static__STNetworkManager.STCommand_httpPost = true;
     }
     
     static void unsubscribe ()
     {
          _static__STNetworkManager.STCommand_unsubscribe = true;
     }
     
     public void scheduleCheck ()
     {
          if ((this.correctMode()) && (this.correctTime()))
          {
               if ((this.allQuiet()) && (this.switchesOk()))
               {
                    this.takeActions();
               }
          }
     }
     
     private boolean correctMode ()
     {
          if (_static_location.mode != _static_GoodNight_newMode)
          {
               return true;
          } else
          {
               return false;
          }
     }
     
     private boolean correctTime ()
     {
          int t0 = GoodNight.now();
          int modeStartTime = GoodNight.now();
          int startTime = GoodNight.timeTodayAfter(modeStartTime,_static_GoodNight_timeOfDay,_static_location.timeZone);
          if (t0 >= startTime)
          {
               return true;
          } else
          {
               return false;
          }
     }
     
     private boolean allQuiet ()
     {
          int threshold = (((1000 * 60) * _static_GoodNight_minutes) - 1000);
          STState[] collectResult1;
          int index3 = 0;
          while (index3 < _static_GoodNight_motionSensors.length)
          {
               STMotionSensor it = _static_GoodNight_motionSensors[index3];
          }
          STState[] states = collectResult1;
          if (states != 0)
          {
               STState findResult1;
          } else
          {
               return true;
          }
     }
     
     private boolean switchesOk ()
     {
          boolean result = true;
          int index2 = 0;
          while (index2 < _static_GoodNight_switches.length)
          {
               STSwitch it = _static_GoodNight_switches[index2];
          }
          return result;
     }
     
     private void takeActions ()
     {
          GoodNight.setLocationMode(_static_GoodNight_newMode);
     }
     
     public void installedEvtHandler ()
     {
          this.createSubscriptions();
     }
     
     public void createSubscriptions ()
     {
          if (_static_GoodNight_modeStartTime == 0)
          {
               GoodNight_modeStartTime = 0;
          }
     }
     
     public void motionActiveEvtHandler ()
     {
     }
     
     public void switchOffEvtHandler ()
     {
          if ((this.correctMode()) && (this.correctTime()))
          {
               if ((this.allQuiet()) && (this.switchesOk()))
               {
                    this.takeActions();
               }
          }
     }
     
     public void motionInactiveEvtHandler ()
     {
          if (_static_GoodNight_modeStartTime == 0)
          {
               GoodNight_modeStartTime = 0;
          }
          if ((this.correctMode()) && (this.correctTime()))
          {
               GoodNight.increaseSTSystemTime((_static_GoodNight_minutes * 60));
          }
     }
     
     public void modeChangeEvtHandler ()
     {
          GoodNight_modeStartTime = GoodNight.now();
     }
     
     private void send (int msg)
     {
     }
}
