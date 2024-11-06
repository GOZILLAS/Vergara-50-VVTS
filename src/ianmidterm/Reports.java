package ianmidterm;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Reports {
    private final Ianmidterm ss; 
    private final Scanner sc;

    public Reports(Ianmidterm ss, Scanner sc) {
        this.ss = ss;
        this.sc = sc; 
    }
  
      public void report() {
        String response = "yes";

        while (response.equalsIgnoreCase("yes")) {
            System.out.println("1. VIEW");
            System.out.println("2. EXIT");

            System.out.print("Enter Action: ");
            int action = 0;
            boolean validAction = false;

            while (!validAction) {
                try {
                    action = sc.nextInt();
                    validAction = true;
                } catch (InputMismatchException e) {
                    System.out.println("Please enter a valid number.");
                    sc.next();
                }
            }
            sc.nextLine();

            switch (action) {
                case 1:
                  ss.viewReports();
                    break;            
                case 2:
                    System.out.println("Exiting to main menu...");
                    return;
                default:
                    System.out.println("Invalid action. Please try again.");
            }
        }
    }

    
}

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

