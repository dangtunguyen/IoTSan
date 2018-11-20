public class STCapability 
{
     public STCommand[] commands;
     public STAttribute[] attributes;
     public int name;
     
     public STAttribute[] getAttributes ()
     {
          return this.attributes;
     }
     
     public STCommand[] getCommands ()
     {
          return this.commands;
     }
     
     public int getName ()
     {
          return this.name;
     }
}
