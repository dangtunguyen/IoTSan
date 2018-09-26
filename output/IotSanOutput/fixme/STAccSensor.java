public class STAccSensor extends STDevice 
{
     public STState accelerationState;
     public int currentAcceleration;
     
     public STState currentState (int attributeName)
     {
          return this.accelerationState;
     }
     
     public int currentValue (int attributeName)
     {
          return this.currentAcceleration;
     }
     
     public STState latestState (int attributeName)
     {
          return this.accelerationState;
     }
     
     public int latestValue (int attributeName)
     {
          return this.currentAcceleration;
     }
}
