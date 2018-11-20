public class STWaterSensor extends STDevice 
{
     public int currentWater;
     public STState waterState;
     
     public STState currentState (int attributeName)
     {
          return this.waterState;
     }
     
     public int currentValue (int attributeName)
     {
          return this.currentWater;
     }
     
     public STState latestState (int attributeName)
     {
          return this.waterState;
     }
     
     public int latestValue (int attributeName)
     {
          return this.currentWater;
     }
}
