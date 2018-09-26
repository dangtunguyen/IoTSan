public class STAeonKeyFob extends STDevice 
{
     public STState buttonState;
     public int currentButton;
     
     public STState currentState (int attributeName)
     {
          return this.buttonState;
     }
     
     public int currentValue (int attributeName)
     {
          return this.currentButton;
     }
     
     public STState latestState (int attributeName)
     {
          return this.buttonState;
     }
     
     public int latestValue (int attributeName)
     {
          return this.currentButton;
     }
}
