public class STContactSensor extends STDevice 
{
     public STState contactState;
     public int currentContact;
     
     public STState currentState (int attributeName)
     {
          return this.contactState;
     }
     
     public int currentValue (int attributeName)
     {
          return this.currentContact;
     }
     
     public STState latestState (int attributeName)
     {
          return this.contactState;
     }
     
     public int latestValue (int attributeName)
     {
          return this.currentContact;
     }
}
