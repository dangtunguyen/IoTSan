public class STColorCtrl extends STDevice 
{
     public STState saturationState;
     public int currentSaturation;
     public boolean STCommand_STColorCtrl_setColor;
     public boolean STCommand_STColorCtrl_setSaturation;
     public int currentLevel;
     public boolean STCommand_STColorCtrl_off;
     public boolean STCommand_STColorCtrl_on;
     public STState colorState;
     public int currentSwitch;
     public boolean STCommand_STColorCtrl_setHue;
     public int currentHue;
     public int currentColor;
     public STState hueState;
     public STState switchState;
     public STState levelState;
     public boolean STCommand_STColorCtrl_setLevel;
     
     public int currentValue (int attributeName)
     {
          switch (attributeName)
          {
               case 14:
               return this.currentSwitch;
               case 74:
               return this.currentLevel;
               case 83:
               return this.currentColor;
               case 84:
               return this.currentHue;
               case 85:
               return this.currentSaturation;
               default:
               return this.currentSwitch;
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
               case 83:
               return this.currentColor;
               case 84:
               return this.currentHue;
               case 85:
               return this.currentSaturation;
               default:
               return this.currentSwitch;
          }
     }
     
     public STState currentState (int attributeName)
     {
          switch (attributeName)
          {
               case 14:
               return this.switchState;
               case 74:
               return this.levelState;
               case 83:
               return this.colorState;
               case 84:
               return this.hueState;
               case 85:
               return this.saturationState;
               default:
               return this.switchState;
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
               case 83:
               return this.colorState;
               case 84:
               return this.hueState;
               case 85:
               return this.saturationState;
               default:
               return this.switchState;
          }
     }
     
     public void on ()
     {
          this.STCommand_STColorCtrl_on = true;
     }
     
     public void on (int delay)
     {
          this.STCommand_STColorCtrl_on = true;
     }
     
     public void off ()
     {
          this.STCommand_STColorCtrl_off = true;
     }
     
     public void off (int delay)
     {
          this.STCommand_STColorCtrl_off = true;
     }
     
     public void setLevel (int level)
     {
          this.currentLevel = level;
          this.STCommand_STColorCtrl_setLevel = true;
     }
     
     public void setLevel (int level, int delay)
     {
          this.currentLevel = level;
          this.STCommand_STColorCtrl_setLevel = true;
     }
     
     public void setHue (int hue)
     {
          this.currentHue = hue;
          this.STCommand_STColorCtrl_setHue = true;
     }
     
     public void setSaturation (int saturation)
     {
          this.currentSaturation = saturation;
          this.STCommand_STColorCtrl_setSaturation = true;
     }
     
     public void setColor (int color)
     {
          this.currentSwitch = (color >> 24);
          this.currentLevel = (color >> 16);
          this.currentHue = (color >> 8);
          this.currentSaturation = (color & 15);
          this.currentColor = color;
          this.STCommand_STColorCtrl_setColor = true;
     }
}
