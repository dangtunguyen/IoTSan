public class STValve extends STDevice 
{
     public boolean STCommand_STValve_open;
     public STState valveState;
     public boolean STCommand_STValve_close;
     public int currentValve;
     
     public STState currentState (int attributeName)
     {
          return this.valveState;
     }
     
     public int currentValue (int attributeName)
     {
          return this.currentValve;
     }
     
     public STState latestState (int attributeName)
     {
          return this.valveState;
     }
     
     public int latestValue (int attributeName)
     {
          return this.currentValve;
     }
     
     public void close ()
     {
          this.STCommand_STValve_close = true;
     }
     
     public void close (int delay)
     {
          this.STCommand_STValve_close = true;
     }
     
     public void open ()
     {
          this.STCommand_STValve_open = true;
     }
     
     public void open (int delay)
     {
          this.STCommand_STValve_open = true;
     }
}
