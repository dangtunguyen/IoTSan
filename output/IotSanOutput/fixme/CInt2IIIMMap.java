public class CInt2IIIMMap 
{
     public CInt2IIMMap valueToPut;
     private CInt2IIMMap[] valueArr;
     private int[] keyArr;
     private int gArrIndex;
     private int size;
     public boolean STCommand_CInt2IIIMMap_put;
     public int keyToPut;
     
     public CInt2IIMMap get (int key)
     {
          int index = 10;
          for (int i = 1; i <= this.size; i = (i + 1))
          {
               if (this.keyArr[i] == key)
               {
                    index = i;
                    break;
               }
          }
          if (index == 10)
          {
               index = 0;
          }
          return this.valueArr[index];
     }
     
     public void put (int key, CInt2IIMMap value)
     {
          this.keyToPut = key;
          this.valueToPut = value;
          this.STCommand_CInt2IIIMMap_put = true;
     }
}
