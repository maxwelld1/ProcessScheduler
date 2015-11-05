/* Darrell Maxwell (n02776371) - Preemptive, Lowest Priority Scheduling Simulation
 * Operating Systems, Fall 2015, SUNY New Paltz
 * Professor: Dr. Hanh Pham
 * Due: November 4th, 2015 */

package scheduler_sim;

import java.io.BufferedReader;
import java.io.*;
import java.util.ArrayList;

/* Serves as an entry point for the entire application. The Simulation class provides an environment for the other components of
 * the simulation to interact within. Additionally, processes i/o. */
class Simulation 
{
    // Variables used for writing output
    private static FileWriter fw;
    private static BufferedWriter bw;
    private static File output;
    
    // Simulation timer
    private static Integer simTime = 0;
    
            
    // Variables used to calculate average wait time and average response time
    public static double averageWaitTime = 0, averageResponseTime = 0;
    
    public static void main(String[] args) {
		
        // Variables used in retrieving input
        FileReader fr;
        BufferedReader br;
	String line;
	String[] tokens;
		
        // Variables read from input
	boolean preemptive;
	int n = 0, q = 0;
		
        /* Parallel arrays: - jobPool contains new process waiting to be admitted to the ready queue
                            - arrivalTimes is used by the simulation to keep track of when they should be admitted */
	ArrayList<Process> jobPool = new ArrayList<>();
	ArrayList<Integer> arrivalTimes = new ArrayList<>();
        
        System.out.println("Process Scheduling Simulation (preemptive, low-priority):\n");
                
        // Initialize output file
        try {
            output = new File("./output.data");
            if (output.exists()) {
                output.delete();
                output.createNewFile();
                System.out.println("Output File Created (output.data).\n");
            }
        } catch (IOException e) {
            System.out.println("Error Initialzing Output File!");
        }

        // Initialize the job pool with processes from the input file
	try {
            System.out.println("Retrieving Input...");
            fr = new FileReader("./input.data");
            br = new BufferedReader(fr);
			
            // Get number of processes
            n = Integer.parseInt(br.readLine());
			
            // Get preemptive / quantum
            line = br.readLine();
            if (line.charAt(0) == '1') {
                preemptive = true;
            } else {
                preemptive = false;
            }
                        
            // Check if preemptive
            if (!preemptive) {
                System.out.println("Error: Input is for non-preemptive scheduler!");
                System.exit(1);
            }
            System.out.println("~ Preemptive: Yes");
            q = Integer.parseInt(line.substring(2));
            System.out.println("~ Time Quantum: 1");
           			
            // Create processes from input and add to job pool 
            System.out.println("~ Initializing Job Pool:");
            for (int i = 1; i <= n; i++) {
                line = br.readLine();
                tokens = line.split(" ");
                System.out.println("   * Adding Process --> Process #"+(i)+": arrival time ("+Integer.parseInt(tokens[0])+
                                "), execution time ("+Integer.parseInt(tokens[2])+"), PRIORITY - "+Integer.parseInt(tokens[1]));
                Process p = new Process(i, Integer.parseInt(tokens[2]), Integer.parseInt(tokens[1]), 0, Integer.parseInt(tokens[0]));
                jobPool.add(p);
                arrivalTimes.add(Integer.parseInt(tokens[0]));
            }
            System.out.println("~ "+n+" processes to be scheduled.");
            System.out.println("Input Processed.\n");
        } catch (Exception e) {
            System.out.println("Input File Could Not Be Read!");
            System.exit(1);
        }
		
	// Initialize CPU and scheduler
        System.out.println("Initializing CPU and Process Scheduler.\n");
	CPU processor = new CPU();
        Scheduler scheduler = new Scheduler(processor);
		
        // Begin simulation loop
        System.out.println("Beginning Simulation...\n~ Scheduler:");
        while (!jobPool.isEmpty() || !scheduler.readyQueueIsEmpty() || processor.isBusy()) {
            // Admit processes from the job pool
            for (int i = 0; i < arrivalTimes.size(); i++) {
                if (arrivalTimes.get(i).equals(simTime)) {
                    Process p = jobPool.remove(i);
                    System.out.println("   * Admitting Process --> Process #"+p.procNumber+": ARRIVAL TIME - "+simTime);
                    scheduler.admitProcess(p);
                    arrivalTimes.remove(i);
                    i--;
                }
            }
            if (processor.isBusy()) {
                // Decrease remaining time for current process in the CPU
                processor.decProcTime();
            }
            // Schedule/preempt process based on priority
            scheduler.scheduleProcess();
            // Increase wait time for ready processes, and increment simulation time
            scheduler.incWaitTime();
            simTime++;
        }
       System.out.println("End of Simulation.\n"); 
       averageWaitTime = averageWaitTime / n;
       averageResponseTime = averageResponseTime / n;
       System.out.println("Average Wait Time: "+averageWaitTime+"\nAverage Response Time: "+averageResponseTime);
    }	
    
    // Gets the current simulation time
    public static int getSimTime() {
        return simTime;
    }
    
    // Writes a string to the output file
    public static void writeToOutput(String outputLine) {
        try {
            fw = new FileWriter(output.getAbsoluteFile(), true);
            bw = new BufferedWriter(fw);
            System.out.println("     Writing to Output File - "+outputLine);
            bw.write(outputLine);
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            System.out.println("Error Writing to Output File!");
        }
    }
}
