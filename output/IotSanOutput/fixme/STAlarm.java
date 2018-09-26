public class STAlarm extends STDevice 
{
     public boolean STCommand_STAlarm_siren;
     public boolean STCommand_STAlarm_off;
     public boolean STCommand_STAlarm_both;
     public boolean STCommand_STAlarm_strobe;
     public int currentAlarm;
     public STState alarmState;
     
     public STState currentState (int attributeName)
     {
          return this.alarmState;
     }
     
     public int currentValue (int attributeName)
     {
          return this.currentAlarm;
     }
     
     public STState latestState (int attributeName)
     {
          return this.alarmState;
     }
     
     public int latestValue (int attributeName)
     {
          return this.currentAlarm;
     }
     
     public void both ()
     {
          this.STCommand_STAlarm_both = true;
     }
     
     public void both (int delay)
     {
          this.STCommand_STAlarm_both = true;
     }
     
     public void off ()
     {
          this.STCommand_STAlarm_off = true;
     }
     
     public void off (int delay)
     {
          this.STCommand_STAlarm_off = true;
     }
     
     public void siren ()
     {
          this.STCommand_STAlarm_siren = true;
     }
     
     public void siren (int delay)
     {
          this.STCommand_STAlarm_siren = true;
     }
     
     public void strobe ()
     {
          this.STCommand_STAlarm_strobe = true;
     }
     
     public void strobe (int delay)
     {
          this.STCommand_STAlarm_strobe = true;
     }
}
