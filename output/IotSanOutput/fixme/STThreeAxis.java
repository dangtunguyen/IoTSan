public class STThreeAxis extends STDevice 
{
     public STState threeAxisState;
     public STxyz currentThreeAxis;
     
     public STState currentState (int attributeName)
     {
          return this.threeAxisState;
     }
     
     public int currentValue (int attributeName)
     {
          return this.currentThreeAxis;
     }
     
     public STState latestState (int attributeName)
     {
          return this.threeAxisState;
     }
     
     public int latestValue (int attributeName)
     {
          return this.currentThreeAxis;
     }
}
