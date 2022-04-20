//Adam Buerger
//main.java
//CSC 460 Program 1
//Due 31 October 2021

import java.util.concurrent.Semaphore;
import java.util.Scanner;
public class main {
	
	//Declare the three semaphores used in the threads
	private static volatile Semaphore door;		//for Customers to acquire to get into the restaurant
	private static volatile Semaphore nap;		//for the waiter to nap between customers
	private static volatile Semaphore service;	//for the waiter to be able to service customers
	public static void main(String[] args) {
		//instantiate the Semaphores.
		door = new Semaphore(15, true);		//only 15 customers can be in the restaurant at any time. The person waiting the longest for the door will enter next.
		nap = new Semaphore(0, true);		//there is only one waiter so it is the only thread that can nap.
		service = new Semaphore(0, true);	//the waiter can only service one customer at a time. The person waiting the longest to be serviced will be serviced next.
		
		//create a Scanner object to detect the user input to start the simulation
		Scanner scan = new Scanner(System.in);
		//create the Waiter thread
		Thread waiter = new Waiter(nap, service);
		//create the Rush Hour Thread Group for the first simulation
		ThreadGroup rushHour = new ThreadGroup("Rush Hour");
		//create the Slow Time Thread Group for the second simulation
		ThreadGroup slow = new ThreadGroup("Slow Time");
		//create an array of Threads to create all the customers for the simulations
		Thread[] threads = new Thread[100];
		
		for(int i = 0; i < 50; i++)		//create the 50 customers for the Rush Hour simulation
			threads[i] = new Customer(rushHour, door, service, nap, "Customer " + i);
		for(int i = 50; i < 100; i++)	//create the 50 customers for the Slow Time simulation
			threads[i] = new Customer(slow, door, service, nap, "Customer " + i);
		
		//prompt the user to start the first simulation
		System.out.println("Press ENTER to begin the Rush Hour simulation.");
		scan.nextLine();
		
		//start the waiter and make it sleep so that it's the only thread running for long enough to sleep
		waiter.start();
		try {
			waiter.sleep(1000);
		} catch (InterruptedException e) {}
		
		//start the Rush Hour processes
		for(int i = 0; i < 50; i++)
			threads[i].start();
		//busy wait while there are active processes in the Rush Hour group
		while(rushHour.activeCount() > 0);
		
		//prompt the user to start the second simulation
		System.out.println("\nPress ENTER to begin the Slow Time sumulation.");
		scan.nextLine();
		
		//start the Slow Time processes
		for(int i = 50; i < 100; i++) {
			try {
				//wait a short but random amount of time between process starts
				waiter.sleep((long) Math.random()%451+50);
				threads[i].start();
			} catch (InterruptedException e) {}
		}
		//busy wait while there are active processes in the Slow Time group
		while(slow.activeCount() > 0);
		//interrupt the waiter in order to end the program
		waiter.interrupt();
	}
}