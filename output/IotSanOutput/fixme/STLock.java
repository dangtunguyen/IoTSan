public class STLock extends STDevice 
{
     public boolean STCommand_STLock_usercodechange;
     public int currentLock;
     public boolean STCommand_STLock_lock;
     public STState lockState;
     public boolean STCommand_STLock_unlock;
     
     public STState currentState (int attributeName)
     {
          return this.lockState;
     }
     
     public int currentValue (int attributeName)
     {
          return this.currentLock;
     }
     
     public STState latestState (int attributeName)
     {
          return this.lockState;
     }
     
     public int latestValue (int attributeName)
     {
          return this.currentLock;
     }
     
     public void lock ()
     {
          this.STCommand_STLock_lock = true;
     }
     
     public void lock (int delay)
     {
          this.STCommand_STLock_lock = true;
     }
     
     public void unlock ()
     {
          this.STCommand_STLock_unlock = true;
     }
     
     public void unlock (int delay)
     {
          this.STCommand_STLock_unlock = true;
     }
     
     public void usercodechange (double user, double code, double status)
     {
          this.STCommand_STLock_usercodechange = true;
     }
}
