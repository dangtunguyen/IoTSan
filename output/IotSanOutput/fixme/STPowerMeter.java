public class STPowerMeter extends STDevice 
{
     public int currentPower;
     public STState powerState;
     
     public STState currentState (int attributeName)
     {
          return this.powerState;
     }
     
     public int currentValue (int attributeName)
     {
          return this.currentPower;
     }
     
     public STState latestState (int attributeName)
     {
          return this.powerState;
     }
     
     public int latestValue (int attributeName)
     {
          return this.currentPower;
     }
}
