public class STPetFeederShield extends STDevice 
{
     public STState petFeederShieldState;
     public int currentPetFeederShield;
     
     public STState currentState (int attributeName)
     {
          return this.petFeederShieldState;
     }
     
     public int currentValue (int attributeName)
     {
          return this.currentPetFeederShield;
     }
     
     public STState latestState (int attributeName)
     {
          return this.petFeederShieldState;
     }
     
     public int latestValue (int attributeName)
     {
          return this.currentPetFeederShield;
     }
     
     public void feed ()
     {
     }
     
     public void feed (java.util.Map map)
     {
     }
}
