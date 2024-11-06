package ianmidterm;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Violation {
    private final Ianmidterm ss; 
    private final Scanner sc;

    public Violation(Ianmidterm ss, Scanner sc) {
        this.ss = ss;
        this.sc = sc; 
    }

    public void Violate() {
        String response = "yes"; 

        while (response.equalsIgnoreCase("yes")) {
            System.out.println("1. Add Violation");
            System.out.println("2. View Violation");
            System.out.println("3. Update");
            System.out.println("4. Delete");
            System.out.println("5. Exit");

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
                     ss.viewDrivers();
                    ss.addViolation(); 
                    break;
                case 2:
                    ss.viewViolation(); 
                    break;
                case 3:
                     ss.viewDrivers();
                    ss.updateViolation();
                    ss.viewDrivers();
                    break;
                case 4:
                    ss.viewViolation();
                    ss.deleteViolation();
                    ss.viewViolation();
                    break;
                case 5:
                    System.out.println("Exiting to main menu...");
                    return; 
                default:
                    System.out.println("Invalid action. Please try again.");
            }
        }
    }
}
