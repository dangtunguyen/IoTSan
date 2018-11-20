public class STRelHumMeas extends STDevice 
{
     public int currentHumidity;
     public STState humidityState;
     
     public int currentValue (java.lang.String attributeName)
     {
          return this.currentHumidity;
     }
     
     public int latestValue (java.lang.String attributeName)
     {
          return this.currentHumidity;
     }
     
     public STState currentState (int attributeName)
     {
          return this.humidityState;
     }
     
     public STState latestState (int attributeName)
     {
          return this.humidityState;
     }
}
