package scheduler_sim;

/* An object of the Process class represents a single process in the simulation. Each process has a process number and priority, as well as
 * timing information such as start time, end time, remaining time, and wait time. */
class Process 
{
    int procNumber, priority, remainingTime, waitTime, admitTime, responseTime, startTime, endTime;
	
    Process(int pNum, int pr, int remT, int waitT, int admitT) {
	procNumber = pNum;
	priority = pr;
	remainingTime = remT;
	waitTime = waitT;
        admitTime = admitT;
        startTime = -1;
    }
	
    // Increments wait time when a process is not in the CPU
    void increaseWaitTime() {
            waitTime++;
	}
	
    // Decrements time remaining when a process is executing
    void decreaseRemainingTime() {
            remainingTime--;
	}
}
