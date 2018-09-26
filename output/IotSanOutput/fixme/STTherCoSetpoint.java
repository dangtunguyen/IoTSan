public class STTherCoSetpoint extends STDevice 
{
     public boolean STCommand_STTherCoSetpoint_setCoolingSetpoint;
     public int currentCoolingSetpoint;
     public STState coolingSetpointState;
     
     public STState currentState (int attributeName)
     {
          return this.coolingSetpointState;
     }
     
     public int currentValue (int attributeName)
     {
          return this.currentCoolingSetpoint;
     }
     
     public STState latestState (int attributeName)
     {
          return this.coolingSetpointState;
     }
     
     public int latestValue (int attributeName)
     {
          return this.currentCoolingSetpoint;
     }
     
     public void setCoolingSetpoint (int setpoint)
     {
          this.currentCoolingSetpoint = setpoint;
          this.STCommand_STTherCoSetpoint_setCoolingSetpoint = true;
     }
     
     public void setCoolingSetpoint (int setpoint, int delay)
     {
          this.currentCoolingSetpoint = setpoint;
          this.STCommand_STTherCoSetpoint_setCoolingSetpoint = true;
     }
}
