import java.util.*;


/***********************************************************
 * PRODUCER
 * 
 * @author  Emelie Skörvald emsk@itu.dk
 * @author  Nikol Shakleva  nikv@itu.dk
 * @author  Szilvia Gaspar  szga@itu.dk
 * 
 ***********************************************************/

public class Producer {
    
    /**
     * 
     * @param mode String to define the input type
     * @param N    int for the size of the generated input
     * @param seed int seed
     * @return
     */
    public static int[] generate (String mode, int N, int seed) {;
        final Random R = new Random();
        R.setSeed(seed + N);

        if( N <= 1) {
			System.err.println("The list is alredy sorted "+ N);
			System.exit(1);
		}
		final int[] vals = new int [N];


		switch (mode) {
            // the elements in the generated input array are in increasing order
            case "increasing": 
                vals[0]= R.nextInt();
                for(int i = 1 ; i < N ; i++) {
                   vals[i] = vals[i-1] + 1;
                }
            break;

            // the elements in the generated input array are in decreasing order
            case "decreasing":
                vals[0] = R.nextInt();
                for(int i = 1 ; i < N ; i++) {
                    vals[i] = vals[i-1] - 1;
             }
            break;

            // all the elements in the generated input array are the same
            case "same":
                vals[0]= R.nextInt();
                for(int i = 1; i<N; i++) {
                    vals[i] = vals[0];
                }
            break;

            case "random": 
                for(int i = 0; i < N ; i++) {
                    vals[i] = R.nextInt();
                }
            break;

            case "semi-sorted":
            // every second element in the generated input array is bigger than the previous one by 1
            // check how many filled items are in the array
                int count = 0;
                    for(int i = 0; i + 2 < N ; i += 2) {
                        int n = R.nextInt();
                        vals[i]   = n;
                        vals[i+1] = n+1;
                        //vals[i+2] = n+2;
                        count = count + 2;
                    }
                // if the count is smaller then N we need to fill the rest of the array
                if(count!=N) {
                    for(int i = count; i<N; i++) {
                        vals[i] = R.nextInt();
                    }
                }
            break;
                
            case "equal":
            // N/10 elements in the generated array are the same
        
            // the index of the last inserted element in the array 
                int index = 0;
                for(int i = 0; i < N ; i = i + N/10) {
                    int num = R.nextInt();
                    for(int j = 0 ; j <  N/10 ; j++) {
                        vals[index] = num;
                        index++;
                    } 
                }
            break;

            default:
                System.err.println("Unknown mode: " + mode);
		}

        return vals;
    }

        public static void main(String[] args) {
            var test = generate("semi-sorted", 30, 1234);
            System.out.println(Arrays.toString(test));
        }
    }