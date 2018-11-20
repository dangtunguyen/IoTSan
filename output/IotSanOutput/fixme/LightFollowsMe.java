public class LightFollowsMe 
{
     static STEvent evt;
     static STLocation location;
     static STSwitch[] LightFollowsMe_switches;
     static STApp LightFollowsMe_app;
     static int STCurrentSystemTime;
     static int LightFollowsMe_minutes1;
     static STMotionSensor LightFollowsMe_motion1;
     static STNetworkManager _STNetworkManager;
     
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
          STState motionState = _static_LightFollowsMe_motion1.currentState(11);
          if (motionState.value == 13)
          {
               int elapsed = (LightFollowsMe.now() - motionState.date);
          }
     }
     
     public void motionEvtHandler ()
     {
          if (_static_evt.value == 12)
          {
               int index6 = 0;
          } else
          {
               if (_static_evt.value == 13)
               {
                    LightFollowsMe.increaseSTSystemTime((_static_LightFollowsMe_minutes1 * 60));
               }
          }
     }
}
