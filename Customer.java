//Adam Buerger
//Customer.java
//CSC 460 Program 1
//Due 31 October 2021

import java.util.concurrent.Semaphore;

public class Customer extends Thread{
	//declare a static counter to track customers
	private static int number = 0;
	//declare a non-static label to later create a unique ID for each customer
	private int myNumber;
	//declare the semaphores that the customers will use
	private volatile Semaphore door;
	private volatile Semaphore service;
	private volatile Semaphore nap;
	//declare a new semaphore to guarantee mutual exclusion for the myNumber variable later
	private static volatile Semaphore numberGrab = new Semaphore(1, true);
	
	public Customer(ThreadGroup group, Semaphore door, Semaphore service, Semaphore nap, String name) {
		super(group, "Thread");
		this.nap = nap;
		this.service = service;
		this.door = door;
	}
	public void run() {
		try {
			System.out.println("A new customer is trying to enter the restaurant.");
			door.acquire();			//gain access to the door semaphore to enter the restaurant
			numberGrab.acquire();	//gain mutual exclusion of the number counter
			myNumber = number++;	//assign the current value of number to myNumber and then increment it
			numberGrab.release();	//release the mutual exclusion
			System.out.println("Customer " + myNumber + " has entered the restaurant and is seated.");
			nap.release();			//wake up the waiter
			System.out.println("Customer " + myNumber + " is waiting for the waiter.");
			service.acquire();		//wait to be served by the waiter
			System.out.println("Customer " + myNumber + " has been served.");
			door.release();			//exit the restaurant
			System.out.println("Customer " + myNumber + " is leaving.");
			
		} catch (InterruptedException e) {}
	}
}