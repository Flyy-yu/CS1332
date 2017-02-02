import java.util.Comparator;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
public class Try{
	public static void main(String[] args) {
		
		int[] array = new int[5];
		array[0]=-111;
		array[1]=-888;
		array[2]=-9;
		array[3]=-3;
		array[4]=111;
		array = lsdRadixSort(array);
		
		

	}
  public static int[] lsdRadixSort(int[] arr) {
        if (arr == null) {
            throw new IllegalArgumentException("Error,arr is null");
        }
        int maxnumber = -999999999;
        int maxlength = 1;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > maxnumber) {
                maxnumber = arr[i];
            }
        }
        while ((maxnumber) >= 10) {
            maxlength++;
            maxnumber = maxnumber / 10;
        }

        List<Integer>[] buckets = new ArrayList[19];
        for (int i = 0; i < 19; i++) {
            buckets[i] = new ArrayList<Integer>();
        }
        int divnumber = 1;

        for (int i = 0; i < maxlength; i++) {
            for (Integer num: arr) {
               
                buckets[((num / divnumber) % 10)+9].add(num);

            }

            int index = 0;
            for (int k = 0; k < buckets.length; k++) {
                for (Integer xx: buckets[k]) {
                    arr[index++] = xx;
                }
                buckets[k].clear();
            }
            divnumber = divnumber * 10;
            for (int ix =0;ix<5 ;ix++ ) {
			System.out.println(arr[ix]);
		}
		System.out.println("next");
        }

    
        return arr;
    }

}


