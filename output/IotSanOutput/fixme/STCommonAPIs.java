public class STCommonAPIs 
{
     static STLocation location;
     static int STCurrentSystemTime;
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
}
