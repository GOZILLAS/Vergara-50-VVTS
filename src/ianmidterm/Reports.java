package ianmidterm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Reports {
    private final Ianmidterm ss; 
    private final Scanner sc;

    public Reports(Ianmidterm ss, Scanner sc) {
        this.ss = ss;
        this.sc = sc; 
    }
  
      public void Report() {
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
                  viewReports();
                    break;            
                case 2:
                    System.out.println("Exiting to main menu...");
                    return;
                default:
                    System.out.println("Invalid action. Please try again.");
            }
        }
    }

    public void viewReports() {
    String qry = "SELECT d.d_id, COUNT(v.v_id) AS ViolationCount, d.d_lname " +
              "FROM cvts d " +
              "LEFT JOIN violate v ON d.d_id = v.d_id " +
              "GROUP BY d.d_id";

        String[] hdrs = {"Dri-ID", "Number of Violations", "Last Name"};
        String[] clmns = {"d_id", "ViolationCount", "d_lname"};

    config conf = new config();
    conf.viewRecords(qry, hdrs, clmns);

    Scanner sc = new Scanner(System.in);
    boolean exit = false;

    while (!exit) {
        System.out.println("1. View More Details");
        System.out.println("2. Exit view");

        System.out.print("Enter Action: ");
        int choice = -1;

        while (choice == -1) {
            try {
                choice = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                sc.next();
            }
        }

       switch (choice) {
    case 1:
        int driverId = -1;

        while (true) {
            System.out.print("Enter Driver ID to view details: ");
            try {
                driverId = sc.nextInt();
                sc.nextLine(); 
                break; 
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a numeric Driver ID.");
                sc.nextLine();
            }
        }

        String detailQry = "SELECT d.d_id, d.d_fname, d.d_lname, d.d_contact, d.d_email, d.d_vehi, d.d_platenum " +
                           "FROM cvts d WHERE d.d_id = ?";
        String[] driverClmns = {"d_id", "d_fname", "d_lname", "d_contact", "d_email", "d_vehi", "d_platenum"};

        
        String violationQry = "SELECT v.v_id, l.l_name AS violation_name, v.v_fine, v.v_status, v.v_date " +
                              "FROM violate v " +
                              "JOIN law l ON v.l_id = l.l_id " +
                              "WHERE v.d_id = ?";
        String[] violationHdrs = {"Violation ID", "Violation Name", "Fine", "Status", "Date"};
        String[] violationClmns = {"v_id", "violation_name", "v_fine", "v_status", "v_date"};

        try (Connection conn = config.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(detailQry)) {
            pstmt.setInt(1, driverId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("\nDriver's Details:");
                System.out.println("Driver ID: " + rs.getString("d_id"));
                System.out.println("First Name: " + rs.getString("d_fname"));
                System.out.println("Last Name: " + rs.getString("d_lname"));
                System.out.println("Contact: " + rs.getString("d_contact"));
                System.out.println("Email: " + rs.getString("d_email"));
                System.out.println("Vehicle: " + rs.getString("d_vehi"));
                System.out.println("Plate Number: " + rs.getString("d_platenum"));
            } else {
                System.out.println("No details found for Driver ID: " + driverId);
                break;
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving driver details: " + e.getMessage());
            break;
        }

        try {
            System.out.println("\nViolations:");
            config conf2 = new config();
            conf2.viewRecords(violationQry, violationHdrs, violationClmns, driverId);
        } catch (Exception e) {
            System.out.println("Error retrieving violations: " + e.getMessage());
        }
        break;

    case 2:
        exit = true;
        System.out.println("Exiting view...");
        break;

    default:
        System.out.println("Invalid choice. Please try again.");
        break;
}

    }
    }
}
   