public class STCarDioMeas extends STDevice 
{
     public int currentCarbonDioxide;
     public STState carbonDioxideState;
     
     public STState currentState (int attributeName)
     {
          return this.carbonDioxideState;
     }
     
     public int currentValue (int attributeName)
     {
          return this.currentCarbonDioxide;
     }
     
     public STState latestState (int attributeName)
     {
          return this.carbonDioxideState;
     }
     
     public int latestValue (int attributeName)
     {
          return this.currentCarbonDioxide;
     }
}
