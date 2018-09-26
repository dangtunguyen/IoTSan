public class STCarMoDetector extends STDevice 
{
     public STState carbonMonoxideState;
     public boolean STCommand_STCarMoDetector_COSmoke;
     public int currentCarbonMonoxide;
     
     public STState currentState (int attributeName)
     {
          return this.carbonMonoxideState;
     }
     
     public int currentValue (int attributeName)
     {
          return this.currentCarbonMonoxide;
     }
     
     public STState latestState (int attributeName)
     {
          return this.carbonMonoxideState;
     }
     
     public int latestValue (int attributeName)
     {
          return this.currentCarbonMonoxide;
     }
     
     public void COSmoke ()
     {
          this.STCommand_STCarMoDetector_COSmoke = true;
     }
     
     public void COSmoke (int delay)
     {
          this.STCommand_STCarMoDetector_COSmoke = true;
     }
}
