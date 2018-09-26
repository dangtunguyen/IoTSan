public class STPresSensor extends STDevice 
{
     public int currentPresence;
     public STState presenceState;
     
     public STState currentState (int attributeName)
     {
          return this.presenceState;
     }
     
     public int currentValue (int attributeName)
     {
          return this.currentPresence;
     }
     
     public STState latestState (int attributeName)
     {
          return this.presenceState;
     }
     
     public int latestValue (int attributeName)
     {
          return this.currentPresence;
     }
}
