public class CInt2IntMap 
{
     public int valueToPut;
     private int[] valueArr;
     private int[] keyArr;
     private int gArrIndex;
     private int size;
     public boolean STCommand_CInt2IntMap_put;
     public int keyToPut;
     
     public int get (int key)
     {
          int index = 10;
          for (int i = 0; i < this.size; i = (i + 1))
          {
               if (this.keyArr[i] == key)
               {
                    index = i;
                    break;
               }
          }
          if (index == 10)
          {
               return 0;
          }
          return this.valueArr[index];
     }
     
     public void put (int key, int value)
     {
          this.keyToPut = key;
          this.valueToPut = value;
          this.STCommand_CInt2IntMap_put = true;
     }
     
     public void put (int key, boolean value)
     {
          this.keyToPut = key;
          this.valueToPut = value;
          this.STCommand_CInt2IntMap_put = true;
     }
}
