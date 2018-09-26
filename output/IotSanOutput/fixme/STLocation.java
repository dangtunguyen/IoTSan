public class STLocation 
{
     public boolean contactBookEnabled;
     public boolean STCommand_Location_Night;
     public STMode[] modes;
     public boolean STCommand_Location_Away;
     public boolean STCommand_Location_Home;
     public STSunriseSunset sunriseSunset;
     public int mode;
     public STWeatherFeature weatherFeature;
     public int timeZone;
     public STEvent latestEvt;
     
     public boolean getContactBookEnabled ()
     {
          return this.contactBookEnabled;
     }
     
     public int getMode ()
     {
          return this.mode;
     }
     
     public STMode[] getModes ()
     {
          return this.modes;
     }
     
     public void setMode (int mode)
     {
          if (mode == 1400)
          {
               this.STCommand_Location_Home = true;
          } else
          {
               if (mode == 1401)
               {
                    this.STCommand_Location_Away = true;
               } else
               {
                    if (mode == 1401)
                    {
                         this.STCommand_Location_Night = true;
                    }
               }
          }
     }
     
     public void setMode (STMode mode)
     {
          if (mode.name == 1400)
          {
               this.STCommand_Location_Home = true;
          } else
          {
               if (mode.name == 1401)
               {
                    this.STCommand_Location_Away = true;
               } else
               {
                    if (mode.name == 1401)
                    {
                         this.STCommand_Location_Night = true;
                    }
               }
          }
     }
     
     public int getTimeZone ()
     {
          return this.timeZone;
     }
     
     STSunriseSunset getSunriseAndSunset ()
     {
          return this.sunriseSunset;
     }
     
     public STWeatherFeature getWeatherFeature (int feature)
     {
          return this.weatherFeature;
     }
     
     public STWeatherFeature getWeatherFeature (int feature, int zipcode)
     {
          return this.weatherFeature;
     }
}
