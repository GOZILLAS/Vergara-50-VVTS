package ianmidterm;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Drivers {

    private final Ianmidterm ss;
    private final Scanner sc;

    public Drivers(Ianmidterm ss, Scanner sc) {
        this.ss = ss;
        this.sc = sc;
    }
 
    public void Driver() {
        String response = "yes";

        while (response.equalsIgnoreCase("yes")) {
            System.out.println("1. ADD DRIVER");
            System.out.println("2. VIEW");
            System.out.println("3. UPDATE");
            System.out.println("4. DELETE");
            System.out.println("5. EXIT");

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
                    ss.addDriver();
                    break;
                case 2:
                    ss.viewDrivers();
                    break;
                case 3:
                    ss.viewDrivers();
                    ss.updateDrivers();
                    ss.viewDrivers();
                    break;
                case 4:
                    ss.viewDrivers();
                    ss.deleteDriver();
                    ss.viewDrivers();
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
