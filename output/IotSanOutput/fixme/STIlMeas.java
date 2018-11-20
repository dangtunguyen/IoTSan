public class STIlMeas extends STDevice 
{
     public STState illuminanceState;
     public int currentIlluminance;
     
     public STState currentState (int attributeName)
     {
          return this.illuminanceState;
     }
     
     public int currentValue (int attributeName)
     {
          return this.currentIlluminance;
     }
     
     public STState latestState (int attributeName)
     {
          return this.illuminanceState;
     }
     
     public int latestValue (int attributeName)
     {
          return this.currentIlluminance;
     }
}
