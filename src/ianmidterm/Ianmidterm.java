package ianmidterm;

import java.util.InputMismatchException;
import java.util.Scanner;


public class Ianmidterm {

public static void main(String[] args) {
    try (Scanner sc = new Scanner(System.in)) {
        Ianmidterm sr = new Ianmidterm();
        String response = "yes"; 

        while (response.equalsIgnoreCase("yes")) {
            System.out.println("=====================================");
            System.out.println("1. DRIVER");
            System.out.println("2. VIOLATION");
            System.out.println("3. REPORT");
            System.out.println("4. EXIT");
              System.out.println("=====================================");

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
                    Drivers drivers = new Drivers(sr, sc); 
                    drivers.Drivers();
                    break;
                case 2:
                  
                    Violation violation = new Violation(sr, sc);
                    violation.Violate();
                    break;
                case 3:
                    Reports reports = new Reports(sr, sc);
                    reports.Report();
                    break;
                case 4:
                    System.out.println("Exiting...");
                    response = "no"; 
                    break;
                default:
                    System.out.println("Invalid action. Please try again.");
                    break;
            }
            if (action != 4 && action >= 1 && action <= 3) {
                System.out.print("Do you want to continue? (yes/no): ");
                response = sc.next();
            }
        }
    }
}
 
    public void viewDrivers() {
        String qry = "SELECT * FROM cvts";
        String[] hdrs = {"ID", "First Name", "Last Name", "Email", "Contact", "Vehicle", "Plate"};
        String[] clmns = {"d_id", "d_fname", "d_lname", "d_email", "d_contact", "d_vehi", "d_platenum" };

        config conf = new config();
        conf.viewRecords(qry, hdrs, clmns);
    }
    
}