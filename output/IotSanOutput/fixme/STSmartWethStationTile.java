public class STSmartWethStationTile extends STDevice 
{
     public STState smartweatherStationTileState;
     public int currentSmartweatherStationTile;
     
     public STState currentState (int attributeName)
     {
          return this.smartweatherStationTileState;
     }
     
     public int currentValue (int attributeName)
     {
          return this.currentSmartweatherStationTile;
     }
     
     public STState latestState (int attributeName)
     {
          return this.smartweatherStationTileState;
     }
     
     public int latestValue (int attributeName)
     {
          return this.currentSmartweatherStationTile;
     }
}
