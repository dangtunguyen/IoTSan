public class STTherFanMode extends STDevice 
{
     public STState thermostatFanModeState;
     public boolean STCommand_STTherFanMode_fanOn;
     public boolean STCommand_STTherFanMode_fanCirculate;
     public int currentThermostatFanMode;
     public boolean STCommand_STTherFanMode_fanAuto;
     
     public STState currentState (int attributeName)
     {
          return this.thermostatFanModeState;
     }
     
     public int currentValue (int attributeName)
     {
          return this.currentThermostatFanMode;
     }
     
     public STState latestState (int attributeName)
     {
          return this.thermostatFanModeState;
     }
     
     public int latestValue (int attributeName)
     {
          return this.currentThermostatFanMode;
     }
     
     public void fanAuto ()
     {
          this.STCommand_STTherFanMode_fanAuto = true;
     }
     
     public void fanAuto (int delay)
     {
          this.STCommand_STTherFanMode_fanAuto = true;
     }
     
     public void fanCirculate ()
     {
          this.STCommand_STTherFanMode_fanCirculate = true;
     }
     
     public void fanCirculate (int delay)
     {
          this.STCommand_STTherFanMode_fanCirculate = true;
     }
     
     public void fanOn ()
     {
          this.STCommand_STTherFanMode_fanOn = true;
     }
     
     public void fanOn (int delay)
     {
          this.STCommand_STTherFanMode_fanOn = true;
     }
     
     public void setThermostatFanMode (int mode)
     {
          if (mode == 25)
          {
               this.STCommand_STTherFanMode_fanAuto = true;
          } else
          {
               if (mode == 26)
               {
                    this.STCommand_STTherFanMode_fanCirculate = true;
               } else
               {
                    if (mode == 15)
                    {
                         this.STCommand_STTherFanMode_fanOn = true;
                    }
               }
          }
     }
}
