public class STImageCapture extends STDevice 
{
     public STState imageState;
     public int currentImage;
     
     public int currentValue (int attributeName)
     {
          return this.currentImage;
     }
     
     public int latestValue (int attributeName)
     {
          return this.currentImage;
     }
     
     public STState currentState (int attributeName)
     {
          return this.imageState;
     }
     
     public STState latestState (int attributeName)
     {
          return this.imageState;
     }
     
     public void take ()
     {
     }
     
     public void take (int delay)
     {
     }
     
     public void alarmOn ()
     {
     }
     
     public void alarmOn (int delay)
     {
     }
     
     public void alarmOff ()
     {
     }
     
     public void alarmOff (int delay)
     {
     }
     
     public void ledOn ()
     {
     }
     
     public void ledOn (int delay)
     {
     }
     
     public void ledOff ()
     {
     }
     
     public void ledOff (int delay)
     {
     }
     
     public void ledAuto ()
     {
     }
     
     public void ledAuto (int delay)
     {
     }
}
