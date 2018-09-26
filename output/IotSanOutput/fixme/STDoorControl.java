public class STDoorControl extends STDevice 
{
     public STState doorState;
     public boolean STCommand_STDoorControl_open;
     public boolean STCommand_STDoorControl_close;
     public int currentDoor;
     
     public STState currentState (int attributeName)
     {
          return this.doorState;
     }
     
     public int currentValue (int attributeName)
     {
          return this.currentDoor;
     }
     
     public STState latestState (int attributeName)
     {
          return this.doorState;
     }
     
     public int latestValue (int attributeName)
     {
          return this.currentDoor;
     }
     
     public void close ()
     {
          this.STCommand_STDoorControl_close = true;
     }
     
     public void close (int delay)
     {
          this.STCommand_STDoorControl_close = true;
     }
     
     public void open ()
     {
          this.STCommand_STDoorControl_open = true;
     }
     
     public void open (int delay)
     {
          this.STCommand_STDoorControl_open = true;
     }
}
