public class STSmokeDetector extends STCarMoDetector 
{
     public int currentSmoke;
     public STState smokeState;
     
     public STState currentState (int attributeName)
     {
          if (attributeName == 50)
          {
               return this.smokeState;
          } else
          {
               if (attributeName == 75)
               {
                    return this.carbonMonoxideState;
               }
          }
          return this.batteryState;
     }
     
     public int currentValue (int attributeName)
     {
          if (attributeName == 50)
          {
               return this.currentSmoke;
          } else
          {
               if (attributeName == 75)
               {
                    return this.currentCarbonMonoxide;
               }
          }
          return (int) this.currentBattery;
     }
     
     public STState latestState (int attributeName)
     {
          if (attributeName == 50)
          {
               return this.smokeState;
          } else
          {
               if (attributeName == 75)
               {
                    return this.carbonMonoxideState;
               }
          }
          return this.batteryState;
     }
     
     public int latestValue (int attributeName)
     {
          if (attributeName == 50)
          {
               return this.currentSmoke;
          } else
          {
               if (attributeName == 75)
               {
                    return this.currentCarbonMonoxide;
               }
          }
          return (int) this.currentBattery;
     }
}
