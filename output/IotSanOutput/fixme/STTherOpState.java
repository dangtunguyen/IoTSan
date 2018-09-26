public class STTherOpState extends STDevice 
{
     public int currentThermostatOperatingState;
     public STState thermostatOperatingStateState;
     
     public STState currentState (int attributeName)
     {
          return this.thermostatOperatingStateState;
     }
     
     public int currentValue (int attributeName)
     {
          return this.currentThermostatOperatingState;
     }
     
     public STState latestState (int attributeName)
     {
          return this.thermostatOperatingStateState;
     }
     
     public int latestValue (int attributeName)
     {
          return this.currentThermostatOperatingState;
     }
}
