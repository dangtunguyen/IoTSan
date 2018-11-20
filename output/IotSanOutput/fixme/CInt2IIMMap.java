public class CInt2IIMMap 
{
     public CInt2IntMap valueToPut;
     public boolean STCommand_CInt2IIMMap_put;
     private CInt2IntMap[] valueArr;
     private int[] keyArr;
     private int gArrIndex;
     private int size;
     public int keyToPut;
     
     public CInt2IntMap get (int key)
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
     
     public void put (int key, CInt2IntMap value)
     {
          this.keyToPut = key;
          this.valueToPut = value;
          this.STCommand_CInt2IIMMap_put = true;
     }
}
