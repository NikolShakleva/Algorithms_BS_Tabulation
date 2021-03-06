import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/****************************************************************************************
 * EXPERIMENT
 * 
 * Compilation:     javac Experiment.java
 * Execution:       java Experiment
 * Dependencies:    Seed.java,   Producer.java,   Benchmark.java,  Timer.java,
 *                  Search.java  Tabulation.java, BinarySearch.java
 *                  
 * 
 * This Experiment Class tests the runningtime for Binary Search.
 * A new direcotry will be made and filled with algorithm * mode files with the test data
 * 
 * To add a new algorithm to the test, make sure that it implements the Search interface,
 * add it as a string to the algorithms String array in this class
 * and add an if statement to the searchObject method in the Benchmarking Class.
 * 
 * @author  Emelie Skörvald emsk@itu.dk
 * @author  Nikol Shakleva  nikv@itu.dk
 * @author  Szilvia Gaspar  szga@itu.dk
 * 
 ***************************************************************************************/

public class ExperimentInsertion {

    //private static String[] algorithms      = {"QuickSortClassic","DualPivotQuickSort","ThreePivotQuickSort"};
    //private static String[] algorithms      ={"ThreePivotQuickSort", "Standard"};
    private static String[] algorithms        = {"QuickSortClassic","InsertionSort", "DualPivotQuickSort","ThreePivotQuickSort"};
    //private static final String[] modeArray =  {"increasing", "decreasing", "same", "random", "semi-sorted"}; //,"equal" };
    private static final String[] modeArray   = {"random"};
    //private static final int[] N            = { 100, 20, 500, 1000};

    //private static final int[] N            = {20_000, 50_000, 100_000, 1_000_000, 5_000_000};
    private static final int[] N            = {10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30};

    private static final int n = 10;
    private static final int seed = 1234;
    private static final int runPerSeed = 5;
    private static final int iterations = 1_000;
    private static String dir;
   
/**
 * 
 * @param algorithms String array of the algorithms to call the experiment on
 * @param modeArray String array of the input types to call the experiment with
 * @param N int array of input sizes
 */
    public static void experiment(String[] algorithms, String[] modeArray, int[] N) {
        systemInfo();                                                       // Prints info about the machine doing the experiment
        warmUp();                                                           // Warmup for the Benchmark                                                              
        createDir();                                                        // Creates a directory string for test data

        // RUNNING THE EXPERIMENT
        System.out.println();
        System.out.println("Running the experiment...");
        try   { 
                FileWriter file = createFile("InsertionsSort");          
                StringBuilder sb = new StringBuilder();   

                System.out.println();
                for (int a = 0; a < algorithms.length; a++) {               // FOR EACH ALGORITHM ////////////
                    
                    int[] seedArray = Seed.createSeed(seed);      
   
                    for (int j = 0; j < N.length; j++) {       
                        double mean = 0.0;
                        double sDev = 0.0;                
                        for (int i = 0; i < modeArray.length; i++) {   
                            System.out.println("-------------------------------------");
                            System.out.println(algorithms[a] + " with mode: " + modeArray[i]);      
                            for (int l = 0; l < seedArray.length; l++) {                 // FOR EACH SEED ///////////////
                                System.out.println("-------------------------------------");
                                System.out.println("N: " + N[j] + " and Seed: " + seedArray[l]);
                                System.out.println("-------------------------------------");

                                for (int r = 0; r < runPerSeed; r++) {           
                                    int[] inputArray = Producer.generate(modeArray[i], N[j], seedArray[l]);
                                    Benchmark.run(inputArray, iterations, n, algorithms[a]);

                                    double tempMean = Benchmark.getMean();
                                    double tempSdev = Benchmark.getSdev();
                                    mean += tempMean;
                                    sDev += tempSdev;
                                }   
                            }
                        } 
                    double totalMean = mean / (runPerSeed* seedArray.length*modeArray.length);        // Dividing mean with RunPerSeed
                    double totalSdev = sDev / (runPerSeed* seedArray.length*modeArray.length);        // Dividing sDev with RunPerSeed
                    sb.append(algorithms[a] + " " + N[j] + " " + totalMean + " " +     // Adding test data with N, mean, sDev
                                                totalSdev + "\n");

                    }
                  
                                            
                }
                // Adding all the measurements to the data files
                addMeasurements(sb, file); 
        } catch (IOException e) { e.printStackTrace();} 

        end();                                                   // Prints end-statment for the experiment
    }

    /**
     * Warms up the Benchmarking iterations * n times
     */
    public static void warmUp() {

        System.out.println("Running the warm-up...");
        String correctness = "";
        for (int i = 0; i < modeArray.length; i ++) {
            for(int a = 0; a < algorithms.length ; a++){
                for (int j = 0; j < 2; j++) {                                          
                    int[] inputArray = Producer.generate(modeArray[i], N[j], seed);

                    correctness = Benchmark.warmUp(inputArray,(iterations * n), algorithms[a]);
                }
            } 
            System.out.println("Warm-up " + (i+1) + "/" + (modeArray.length) + " done! " + correctness);
        }
    }

/** HELPER FUNCTIONS */

    /** Adds all the test data to the the file and closes the FileWriter
     * 
     * @param sb a StringBuilder 
     * @param fw a FileWriter 
     * @throws IOException
     */
    public static void addMeasurements(StringBuilder sb, FileWriter fw) throws IOException {
                fw.append(sb, 0, sb.length());
                fw.flush();
                fw.close();
    }

    /** Creates a directory that will contain all the test data files
     * 
     * @return a string for the newly created directory
     */
    public static String createDir() {
        dir = new SimpleDateFormat("yyyy.MM.dd").format(new Date()) + "-"
                + new SimpleDateFormat("HH.mm.ss").format(new Date());
        File s = new File(dir);
        s.mkdirs();
        return dir;
    }

    /** Creates Files for each algorithm and mode
     * 
     * @param m string name of the algorithm
     * @return returns the writer
     * @throws IOException
     */
    public static FileWriter createFile(String m) throws IOException {

        File file = new File(dir + "/" + m + "_" + ".table");
        FileWriter writer = new FileWriter(file);
        writer.write("Algorithm N mean sdev\n");

        return writer;
    }

    /**
     * Prints information of the system creating the test
     */
    public static void systemInfo() {
        System.out.println();
        System.out.printf("# OS:   %s; %s; %s%n", 
                          System.getProperty("os.name"), 
                          System.getProperty("os.version"), 
                          System.getProperty("os.arch"));
        System.out.printf("# JVM:  %s; %s%n", 
                          System.getProperty("java.vendor"), 
                          System.getProperty("java.version"));
        // The processor identifier works only on MS Windows:
        System.out.printf("# CPU:  %s; %d \"cores\"%n", 
                          System.getenv("PROCESSOR_IDENTIFIER"),
                          Runtime.getRuntime().availableProcessors());
        java.util.Date now = new java.util.Date();
        System.out.printf("# Date: %s%n", 
          new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(now));
          System.out.println();
          System.out.println("BENCHMARKING Quick Sort");
          System.out.println("-------------------------------------");
          System.out.println();
      }
    /**
     * Prints a statement that the test has finished and where to find
     * the test data
     */
    public static void end(){
        System.out.println("-------------------------------------");
        System.out.println("Experiment all Done!");
        System.out.println("Find the results here:");
        System.out.println(System.getProperty("user.dir") +"/"+ dir);
        System.out.println();
    }
}


