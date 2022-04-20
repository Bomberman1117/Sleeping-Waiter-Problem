//Adam Buerger
//Waiter.java
//CSC 460 Program 1
//Due 31 October 2021

import java.util.concurrent.Semaphore;

public class Waiter extends Thread{
	//declare the local semaphores that the waiter will use
	private volatile Semaphore nap;
	private volatile Semaphore service;
	//declare a counter variable for the waiter to track customers
	private int customerNumber = 0;
	public Waiter(Semaphore nap, Semaphore service) {
		super();
		this.nap = nap;
		this.service = service;
	}
	public void run() {
		//the waiter must always be running
		while(true) {
			try {
				if(!nap.tryAcquire()) {	//the waiter sees if it can take a nap
					//announce that the waiter is going to sleep
					System.out.println("The Waiter is going to sleep.");
					//try to nap
					nap.acquire();
					//announce that the waiter is waking up
					System.out.println("The Waiter is now AWAKE.");	
				}
				//the waiter takes some time to wait on the oldest customer
				this.sleep((long) (Math.random()%451 + 50));
				System.out.println("The Waiter is servicing Customer " + customerNumber++ + ".");
				//service the oldest customer
				service.release();
			} catch(InterruptedException Ex) {
				//the waiter is only interrupted like this when main needs to end so print a message announcing that the interrupt is successful and end the method
				System.out.println("The Waiter has been interrupted. Goodbye.");
				return;
			}
		}
	}
}