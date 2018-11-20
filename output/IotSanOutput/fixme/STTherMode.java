public class STTherMode extends STDevice 
{
     public boolean STCommand_STTherMode_heat;
     public boolean STCommand_STTherMode_cool;
     public boolean STCommand_STTherMode_auto;
     public int currentThermostatMode;
     public boolean STCommand_STTherMode_emergencyHeat;
     public boolean STCommand_STTherMode_off;
     public STState thermostatModeState;
     
     public STState currentState (int attributeName)
     {
          return this.thermostatModeState;
     }
     
     public int currentValue (int attributeName)
     {
          return this.currentThermostatMode;
     }
     
     public STState latestState (int attributeName)
     {
          return this.thermostatModeState;
     }
     
     public int latestValue (int attributeName)
     {
          return this.currentThermostatMode;
     }
     
     public void auto ()
     {
          this.STCommand_STTherMode_auto = true;
     }
     
     public void auto (int delay)
     {
          this.STCommand_STTherMode_auto = true;
     }
     
     public void cool ()
     {
          this.STCommand_STTherMode_cool = true;
     }
     
     public void cool (int delay)
     {
          this.STCommand_STTherMode_cool = true;
     }
     
     public void emergencyHeat ()
     {
          this.STCommand_STTherMode_emergencyHeat = true;
     }
     
     public void emergencyHeat (int delay)
     {
          this.STCommand_STTherMode_emergencyHeat = true;
     }
     
     public void heat ()
     {
          this.STCommand_STTherMode_heat = true;
     }
     
     public void heat (int delay)
     {
          this.STCommand_STTherMode_heat = true;
     }
     
     public void off ()
     {
          this.STCommand_STTherMode_off = true;
     }
     
     public void off (int delay)
     {
          this.STCommand_STTherMode_off = true;
     }
     
     public void setThermostatMode (int mode)
     {
          if (mode == 25)
          {
               this.STCommand_STTherMode_auto = true;
          } else
          {
               if (mode == 31)
               {
                    this.STCommand_STTherMode_cool = true;
               } else
               {
                    if (mode == 32)
                    {
                         this.STCommand_STTherMode_emergencyHeat = true;
                    } else
                    {
                         if (mode == 33)
                         {
                              this.STCommand_STTherMode_heat = true;
                         } else
                         {
                              if (mode == 16)
                              {
                                   this.STCommand_STTherMode_off = true;
                              }
                         }
                    }
               }
          }
     }
}
