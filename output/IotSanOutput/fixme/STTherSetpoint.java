public class STTherSetpoint extends STDevice 
{
     public int currentThermostatSetpoint;
     public STState thermostatSetpointState;
     
     public STState currentState (int attributeName)
     {
          return this.thermostatSetpointState;
     }
     
     public int currentValue (int attributeName)
     {
          return this.currentThermostatSetpoint;
     }
     
     public STState latestState (int attributeName)
     {
          return this.thermostatSetpointState;
     }
     
     public int latestValue (int attributeName)
     {
          return this.currentThermostatSetpoint;
     }
}
