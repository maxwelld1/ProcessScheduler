package scheduler_sim;

/* The CPU class contains a single Process object, which is said to be the currently executing process. The CPU decrements the remaining time for the current process
 * as it executes. The current process can be set by the Scheduler. */
class CPU 
{
    private Process currentProcess = null;
	
    // Indicates if there is currently a process running in the CPU
    boolean isBusy() {
        if (currentProcess == null) {
            return false;
        } else {
            return true;
        }
    }
	
    // Assigns a process to the CPU
    void setProcess(Process p) {
        currentProcess = p;
    }
	
    // Returns the process currently executing in the CPU
    Process getProcess() {
        return currentProcess;
    }
	
    // Decrements the remaining time for the current process as it executes
    void decProcTime() {
        if (currentProcess == null) return; 
        currentProcess.decreaseRemainingTime();
        // Removes the current process when it has finished executing
        if (currentProcess.remainingTime <= 0) {
            currentProcess.endTime = Simulation.getSimTime();
            System.out.println("   * PROCESS #"+currentProcess.procNumber+" FINISHED EXECUTING\n     start time ("+currentProcess.startTime+"), end time ("+currentProcess.endTime
                                +"), response time ("+currentProcess.responseTime+"), wait time ("+currentProcess.waitTime+"), turnaround time ("+(currentProcess.endTime-currentProcess.admitTime)+")");
            Simulation.writeToOutput(currentProcess.startTime+" "+currentProcess.endTime+" "+currentProcess.procNumber);
            Simulation.averageWaitTime+=currentProcess.waitTime;
            currentProcess = null;
        }
    }
}
