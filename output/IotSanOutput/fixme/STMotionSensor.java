public class STMotionSensor extends STDevice 
{
     public STState motionState;
     public int currentMotion;
     
     public STState currentState (int attributeName)
     {
          return this.motionState;
     }
     
     public int currentValue (int attributeName)
     {
          return this.currentMotion;
     }
     
     public STState latestState (int attributeName)
     {
          return this.motionState;
     }
     
     public int latestValue (int attributeName)
     {
          return this.currentMotion;
     }
}
