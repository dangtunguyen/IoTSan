public class STTempMeas extends STDevice 
{
     public int currentTemperature;
     public STState temperatureState;
     
     public STState currentState (int attributeName)
     {
          return this.temperatureState;
     }
     
     public int currentValue (int attributeName)
     {
          return this.currentTemperature;
     }
     
     public STState latestState (int attributeName)
     {
          return this.temperatureState;
     }
     
     public int latestValue (int attributeName)
     {
          return this.currentTemperature;
     }
}
