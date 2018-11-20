public class STDevice 
{
     public long currentBattery;
     public STState[] states;
     public int gArrIndex;
     public STEvent[] events;
     public STState batteryState;
     public int id;
     
     public STEvent[] events (int N)
     {
          return this.events;
     }
     
     public STEvent[] events ()
     {
          return this.events;
     }
     
     public STEvent[] eventsBetween (int startDate, int endDate)
     {
          return this.events;
     }
     
     public STEvent[] eventsSince (int startDate)
     {
          return this.events;
     }
     
     public int getId ()
     {
          return this.id;
     }
     
     public STState[] statesBetween (int attributeName, int startDate, int endDate)
     {
          return this.states;
     }
     
     public STState[] statesSince (int attributeName, int startDate)
     {
          return this.states;
     }
     
     public STState currentState (int attributeName)
     {
     }
     
     public int currentValue (int attributeName)
     {
     }
     
     public STState latestState (int attributeName)
     {
     }
     
     public int latestValue (int attributeName)
     {
     }
}
