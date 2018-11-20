public class STTherHeatSetpoint extends STDevice 
{
     public boolean STCommand_STTherHeatSetpoint_setHeatingSetpoint;
     public int currentHeatingSetpoint;
     public STState heatingSetpointState;
     
     public STState currentState (int attributeName)
     {
          return this.heatingSetpointState;
     }
     
     public int currentValue (int attributeName)
     {
          return this.currentHeatingSetpoint;
     }
     
     public STState latestState (int attributeName)
     {
          return this.heatingSetpointState;
     }
     
     public int latestValue (int attributeName)
     {
          return this.currentHeatingSetpoint;
     }
     
     public void setHeatingSetpoint (int setpoint)
     {
          this.currentHeatingSetpoint = setpoint;
          this.STCommand_STTherHeatSetpoint_setHeatingSetpoint = true;
     }
     
     public void setHeatingSetpoint (int setpoint, int delay)
     {
          this.currentHeatingSetpoint = setpoint;
          this.STCommand_STTherHeatSetpoint_setHeatingSetpoint = true;
     }
}
