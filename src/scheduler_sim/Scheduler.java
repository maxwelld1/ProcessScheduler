package scheduler_sim;

import java.util.ArrayList;

/* The Scheduler class maintains an ArrayList of Process objects (the ready queue). It can perform various actions on these processes
 * such as increment their wait times, and schedule them to the CPU. */
class Scheduler 
{
    private final ArrayList<Process> readyQueue = new ArrayList<>();
    private final CPU cpu;
	
    Scheduler(CPU processor) {
            cpu = processor;
	}
	
    // Increase the wait time for each process in the ready queue
    void incWaitTime() {
            for (Process p : readyQueue) {
                p.waitTime++;
            }
	}
	
    // Admit specified process to the ready queue (long term scheduling)
    void admitProcess(Process p) {
        if (readyQueue.isEmpty()) {
            readyQueue.add(p);
        } else {
            for (int i = 0; i < readyQueue.size(); i++) {
                if (p.priority < readyQueue.get(i).priority) {
                   readyQueue.add(i, p);
                   return;
                }
            } 
            readyQueue.add(p);
        }    
    }
	
    // Schedule a process from the ready queue to the CPU based on lowest priority. Preempted processes are returned to the ready queue
    void scheduleProcess() {
        if (!readyQueueIsEmpty() && cpu.isBusy()) {
            if (cpu.getProcess().priority > readyQueue.get(0).priority) {
                System.out.println("   * PROCESS #"+cpu.getProcess().procNumber+" PREEMPTED BY PROCESS #"+readyQueue.get(0).procNumber);
                // Set start time and response time if new process
                if (readyQueue.get(0).startTime == -1) {
                    readyQueue.get(0).startTime = Simulation.getSimTime();
                    readyQueue.get(0).responseTime = Simulation.getSimTime() - readyQueue.get(0).admitTime;
                }
                Simulation.averageResponseTime+=readyQueue.get(0).responseTime;
                Process temp = cpu.getProcess();
                cpu.setProcess(readyQueue.remove(0));
                admitProcess(temp);
            }
        } else if (!readyQueueIsEmpty() && !cpu.isBusy()) {
            System.out.println("   * PROCESS #"+readyQueue.get(0).procNumber+" SCHEDULED TO THE CPU");
            // Set start time and response time if new process
            if (readyQueue.get(0).startTime == -1) {
                    readyQueue.get(0).startTime = Simulation.getSimTime();
                    readyQueue.get(0).responseTime = Simulation.getSimTime() - readyQueue.get(0).admitTime;
            }
            Simulation.averageResponseTime+=readyQueue.get(0).responseTime;
            cpu.setProcess(readyQueue.remove(0)); 
        }
    }
        
    // Indicates if the ready queue is empty
    boolean readyQueueIsEmpty() {
        return readyQueue.isEmpty();
    }
}
