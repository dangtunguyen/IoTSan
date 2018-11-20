public class STSwitch extends STDevice 
{
     public boolean STCommand_STSwitch_setLevel;
     public boolean STCommand_STSwitch_on;
     public STState levelState;
     public int currentSwitch;
     public boolean STCommand_STSwitch_off;
     public STState switchState;
     public int currentLevel;
     
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
          this.STCommand_STSwitch_on = true;
     }
     
     public void on (int delay)
     {
          this.STCommand_STSwitch_on = true;
     }
     
     public void off ()
     {
          this.STCommand_STSwitch_off = true;
     }
     
     public void off (int delay)
     {
          this.STCommand_STSwitch_off = true;
     }
     
     public void setLevel (int level)
     {
          this.currentLevel = level;
          this.STCommand_STSwitch_setLevel = true;
     }
     
     public void setLevel (int level, int delay)
     {
          this.currentLevel = level;
          this.STCommand_STSwitch_setLevel = true;
     }
}
