public class STSwitchLevel extends STDevice 
{
     public boolean STCommand_STSwitchLevel_off;
     public STState levelState;
     public int currentSwitch;
     public STState switchState;
     public boolean STCommand_STSwitchLevel_setLevel;
     public int currentLevel;
     public boolean STCommand_STSwitchLevel_on;
     
     public STState currentState (int attributeName)
     {
          switch (attributeName)
          {
               case 14:
               return this.switchState;
               case 74:
               return this.levelState;
               default:
               return this.switchState;
          }
     }
     
     public int currentValue (int attributeName)
     {
          switch (attributeName)
          {
               case 14:
               return this.currentSwitch;
               case 74:
               return this.currentLevel;
               default:
               return this.currentSwitch;
          }
     }
     
     public STState latestState (int attributeName)
     {
          switch (attributeName)
          {
               case 14:
               return this.switchState;
               case 74:
               return this.levelState;
               default:
               return this.switchState;
          }
     }
     
     public int latestValue (int attributeName)
     {
          switch (attributeName)
          {
               case 14:
               return this.currentSwitch;
               case 74:
               return this.currentLevel;
               default:
               return this.currentSwitch;
          }
     }
     
     public void on ()
     {
          this.STCommand_STSwitchLevel_on = true;
     }
     
     public void on (int delay)
     {
          this.STCommand_STSwitchLevel_on = true;
     }
     
     public void off ()
     {
          this.STCommand_STSwitchLevel_off = true;
     }
     
     public void off (int delay)
     {
          this.STCommand_STSwitchLevel_off = true;
     }
     
     public void setLevel (int level)
     {
          this.currentLevel = level;
          this.STCommand_STSwitchLevel_setLevel = true;
     }
     
     public void setLevel (int level, int delay)
     {
          this.currentLevel = level;
          this.STCommand_STSwitchLevel_setLevel = true;
     }
}
