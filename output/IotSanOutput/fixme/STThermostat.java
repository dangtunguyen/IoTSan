public class STThermostat extends STDevice 
{
     public boolean STCommand_STThermostat_emergencyHeat;
     public boolean STCommand_STThermostat_setCoolingSetpoint;
     public int currentHeatingSetpoint;
     public int currentThermostatSetpoint;
     public boolean STCommand_STThermostat_setHeatingSetpoint;
     public boolean STCommand_STThermostat_auto;
     public boolean STCommand_STThermostat_fanAuto;
     public int currentThermostatFanMode;
     public STState thermostatFanModeState;
     public int currentCoolingSetpoint;
     public STState thermostatModeState;
     public int currentThermostatOperatingState;
     public boolean STCommand_STThermostat_cool;
     public STState thermostatOperatingStateState;
     public STState temperatureState;
     public STState heatingSetpointState;
     public boolean STCommand_STThermostat_heat;
     public boolean STCommand_STThermostat_fanOn;
     public int currentTemperature;
     public boolean STCommand_STThermostat_off;
     public STState coolingSetpointState;
     public int currentThermostatMode;
     public boolean STCommand_STThermostat_fanCirculate;
     public STState thermostatSetpointState;
     
     public STState currentState (int attributeName)
     {
          if (attributeName == 20)
          {
               return this.temperatureState;
          } else
          {
               if (attributeName == 30)
               {
                    return this.thermostatModeState;
               } else
               {
                    if (attributeName == 21)
                    {
                         return this.coolingSetpointState;
                    } else
                    {
                         if (attributeName == 27)
                         {
                              return this.heatingSetpointState;
                         } else
                         {
                              if (attributeName == 42)
                              {
                                   return this.thermostatSetpointState;
                              } else
                              {
                                   if (attributeName == 24)
                                   {
                                        return this.thermostatFanModeState;
                                   } else
                                   {
                                        if (attributeName == 34)
                                        {
                                             return this.thermostatOperatingStateState;
                                        }
                                   }
                              }
                         }
                    }
               }
          }
          return this.coolingSetpointState;
     }
     
     public int currentValue (int attributeName)
     {
          if (attributeName == 20)
          {
               return this.currentTemperature;
          } else
          {
               if (attributeName == 30)
               {
                    return this.currentThermostatMode;
               } else
               {
                    if (attributeName == 21)
                    {
                         return this.currentCoolingSetpoint;
                    } else
                    {
                         if (attributeName == 27)
                         {
                              return this.currentHeatingSetpoint;
                         } else
                         {
                              if (attributeName == 42)
                              {
                                   return this.currentThermostatSetpoint;
                              } else
                              {
                                   if (attributeName == 24)
                                   {
                                        return this.currentThermostatFanMode;
                                   } else
                                   {
                                        if (attributeName == 34)
                                        {
                                             return this.currentThermostatOperatingState;
                                        }
                                   }
                              }
                         }
                    }
               }
          }
          return this.currentThermostatMode;
     }
     
     public STState latestState (int attributeName)
     {
          if (attributeName == 20)
          {
               return this.temperatureState;
          } else
          {
               if (attributeName == 30)
               {
                    return this.thermostatModeState;
               } else
               {
                    if (attributeName == 21)
                    {
                         return this.coolingSetpointState;
                    } else
                    {
                         if (attributeName == 27)
                         {
                              return this.heatingSetpointState;
                         } else
                         {
                              if (attributeName == 42)
                              {
                                   return this.thermostatSetpointState;
                              } else
                              {
                                   if (attributeName == 24)
                                   {
                                        return this.thermostatFanModeState;
                                   } else
                                   {
                                        if (attributeName == 34)
                                        {
                                             return this.thermostatOperatingStateState;
                                        }
                                   }
                              }
                         }
                    }
               }
          }
          return this.coolingSetpointState;
     }
     
     public int latestValue (int attributeName)
     {
          if (attributeName == 20)
          {
               return this.currentTemperature;
          } else
          {
               if (attributeName == 30)
               {
                    return this.currentThermostatMode;
               } else
               {
                    if (attributeName == 21)
                    {
                         return this.currentCoolingSetpoint;
                    } else
                    {
                         if (attributeName == 27)
                         {
                              return this.currentHeatingSetpoint;
                         } else
                         {
                              if (attributeName == 42)
                              {
                                   return this.currentThermostatSetpoint;
                              } else
                              {
                                   if (attributeName == 24)
                                   {
                                        return this.currentThermostatFanMode;
                                   } else
                                   {
                                        if (attributeName == 34)
                                        {
                                             return this.currentThermostatOperatingState;
                                        }
                                   }
                              }
                         }
                    }
               }
          }
          return this.currentThermostatMode;
     }
     
     public void auto ()
     {
          this.STCommand_STThermostat_auto = true;
     }
     
     public void auto (int delay)
     {
          this.STCommand_STThermostat_auto = true;
     }
     
     public void cool ()
     {
          this.STCommand_STThermostat_cool = true;
     }
     
     public void cool (int delay)
     {
          this.STCommand_STThermostat_cool = true;
     }
     
     public void emergencyHeat ()
     {
          this.STCommand_STThermostat_emergencyHeat = true;
     }
     
     public void emergencyHeat (int delay)
     {
          this.STCommand_STThermostat_emergencyHeat = true;
     }
     
     public void heat ()
     {
          this.STCommand_STThermostat_heat = true;
     }
     
     public void heat (int delay)
     {
          this.STCommand_STThermostat_heat = true;
     }
     
     public void off ()
     {
          this.STCommand_STThermostat_off = true;
     }
     
     public void off (int delay)
     {
          this.STCommand_STThermostat_off = true;
     }
     
     public void setThermostatMode (int mode)
     {
          if (mode == 25)
          {
               this.STCommand_STThermostat_auto = true;
          } else
          {
               if (mode == 31)
               {
                    this.STCommand_STThermostat_cool = true;
               } else
               {
                    if (mode == 32)
                    {
                         this.STCommand_STThermostat_emergencyHeat = true;
                    } else
                    {
                         if (mode == 33)
                         {
                              this.STCommand_STThermostat_heat = true;
                         } else
                         {
                              if (mode == 16)
                              {
                                   this.STCommand_STThermostat_off = true;
                              }
                         }
                    }
               }
          }
     }
     
     public void fanAuto ()
     {
          this.STCommand_STThermostat_fanAuto = true;
     }
     
     public void fanAuto (int delay)
     {
          this.STCommand_STThermostat_fanAuto = true;
     }
     
     public void fanCirculate ()
     {
          this.STCommand_STThermostat_fanCirculate = true;
     }
     
     public void fanCirculate (int delay)
     {
          this.STCommand_STThermostat_fanCirculate = true;
     }
     
     public void fanOn ()
     {
          this.STCommand_STThermostat_fanOn = true;
     }
     
     public void fanOn (int delay)
     {
          this.STCommand_STThermostat_fanOn = true;
     }
     
     public void setHeatingSetpoint (int setpoint)
     {
          this.currentHeatingSetpoint = setpoint;
          this.STCommand_STThermostat_setHeatingSetpoint = true;
     }
     
     public void setHeatingSetpoint (int setpoint, int delay)
     {
          this.currentHeatingSetpoint = setpoint;
          this.STCommand_STThermostat_setHeatingSetpoint = true;
     }
     
     public void setCoolingSetpoint (int setpoint)
     {
          this.currentCoolingSetpoint = setpoint;
          this.STCommand_STThermostat_setCoolingSetpoint = true;
     }
     
     public void setCoolingSetpoint (int setpoint, int delay)
     {
          this.currentCoolingSetpoint = setpoint;
          this.STCommand_STThermostat_setCoolingSetpoint = true;
     }
     
     public void setThermostatFanMode (int mode)
     {
          if (mode == 25)
          {
               this.STCommand_STThermostat_fanAuto = true;
          } else
          {
               if (mode == 26)
               {
                    this.STCommand_STThermostat_fanCirculate = true;
               } else
               {
                    if (mode == 15)
                    {
                         this.STCommand_STThermostat_fanOn = true;
                    }
               }
          }
     }
}
