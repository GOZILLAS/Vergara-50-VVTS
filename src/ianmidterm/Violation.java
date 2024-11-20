package ianmidterm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Violation {
    private Set<Integer> validDriverIds;
    private final Ianmidterm ss; 
    private final Scanner sc;

    public Violation(Ianmidterm ss, Scanner sc) {
        this.ss = ss;
        this.sc = sc; 
    }

    public void Violate() {
        String response = "yes"; 

        while (response.equalsIgnoreCase("yes")) {
            System.out.println("1. ADD Violation");
            System.out.println("2. VIEW Violation");
            System.out.println("3. UPDATE Violation");
            System.out.println("4. DELETE Violation");
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
                    ss.viewDrivers();
                    addViolation(); 
                    break;
                case 2:
                    viewViolation(); 
                    break;
                case 3:
                    viewViolation(); 
                    updateViolation();
                    viewViolation(); 
                    break;
                case 4:
                    viewViolation();
                    deleteViolation();
                    viewViolation();
                    break;
                case 5:
                    System.out.println("Exiting to main menu...");
                    return; 
                default:
                    System.out.println("Invalid action. Please try again.");
            }
        }
    }
    
    public void addViolation() {
        Scanner sc = new Scanner(System.in);
        config conf = new config();

    int did = 0;
    while (true) {
        System.out.print("Enter Driver ID: ");
        did = sc.nextInt();
        
        if (isDriverIdExists(did)) {
            break;  
        } else {
            System.out.println("ID doesn't exist. Please Enter again.");
        }
    }

        System.out.println(
        "1. Speeding\n" +
        "2. Counterflowing\n" +
        "3. Running a Red Light\n" +
        "4. Illegal Parking\n" +
        "5. Driving Without a License\n" +
        "6. Driving Under the Influence (DUI)\n" +
        "7. Failure to Wear Seatbelt\n" +
        "8. Use of Mobile Devices While Driving\n" +
        "9. Obstruction\n" +
        "10. Driving an Unregistered Vehicle\n" +
        "11. Emissions Violations"
    );

    System.out.print("Violation ID: ");
    int lid = -1;
    while (lid < 1 || lid > 11) {
        if (sc.hasNextInt()) {
            lid = sc.nextInt();
            if (lid < 1 || lid > 11) {
                System.out.print("Choose one from the Display above. Try again: ");
            }
        } else {
            System.out.print("Invalid input. Enter a Violation ID: ");
            sc.next();
        }
    }
    sc.nextLine();

    System.out.print("Violation Fine: ");
    String vfine = sc.nextLine();
    while (!vfine.matches("\\d+(\\.\\d{1,2})?")) {
        System.out.print("Invalid Input. Enter a valid fine (100, 100.00):");
        vfine = sc.nextLine();
    }

    System.out.print("Enter the Date(Year-Month-Day): ");
    String vdate = sc.nextLine();
    while (!vdate.matches("\\d{4}-\\d{2}-\\d{2}")) {
        System.out.print("Invalid Format. 0000-00-00: ");
        vdate = sc.nextLine();
    }

    System.out.print("Enter Status: ");
    String vstatus = sc.nextLine().trim();
    while (!vstatus.equalsIgnoreCase("Pending") && !vstatus.equalsIgnoreCase("Paid")) {
        System.out.print("Invalid. Enter 'Pending' or 'Paid':");
        vstatus = sc.nextLine().trim();
    }

    String sql = "INSERT INTO violate (d_id, l_id, v_fine, v_date, v_status) VALUES (?, ?, ?, ?, ?)";
    conf.addRecord(sql, did, lid, vfine, vdate, vstatus);
}

    
    public void viewViolation() {
        String qry = "SELECT v.v_id, v.d_id, l.l_name, v.v_fine, v.v_date, v.v_status, d.d_lname " +
                     "FROM violate v " +
                     "JOIN law l ON v.l_id = l.l_id " +
                     "JOIN cvts d ON v.d_id = d.d_id";

        String[] hdrs = {"Violator-ID", "Driver-ID", "Violation", "Fine", "Date", "Status", "Driver Last Name"};
        String[] clmns = {"v_id", "d_id", "l_name", "v_fine", "v_date", "v_status", "d_lname"};

        config conf = new config();
        conf.viewRecords(qry, hdrs, clmns);
    }

    
    public void updateViolation() {
    Scanner sc = new Scanner(System.in);
    System.out.print("Enter Violator ID: ");
    
    int vid = -1;
    while (true) {
        System.out.print("");
        vid = sc.nextInt();
        sc.nextLine();  
        
        if (isViolatorIdExists(vid)) {
            break; 
        } else {
            System.out.println("Violation ID does not exist. Please Try Again");
        }
    }

    double nFine = -1;
    while (nFine <= 0) {
        System.out.print("Enter New Fine Amount: ");
        if (sc.hasNextDouble()) {
            nFine = sc.nextDouble();
            if (nFine <= 0) {
                System.out.println("Invalid Amount. Please Try Again");
            }
        } else {
            System.out.println("Invalid Amount. Please Try Again");
            sc.next(); 
        }
    }
    sc.nextLine(); 
    
    String nStatus = "";
    while (!nStatus.equalsIgnoreCase("Pending") && !nStatus.equalsIgnoreCase("Paid")) {
        System.out.print("Enter new status (Pending/Paid): ");
        nStatus = sc.nextLine().trim();
        if (!nStatus.equalsIgnoreCase("Pending") && !nStatus.equalsIgnoreCase("Paid")) {
            System.out.println("Invalid status. Enter 'Pending' or 'Paid'.");
        }
    }
    
    String qry = "UPDATE violate SET v_fine = ?, v_status = ? WHERE v_id = ?";
    config conf = new config();
    conf.updateRecord(qry, nFine, nStatus, vid);

    System.out.println("Violation updated successfully!");
}

    private boolean isViolatorIdExists(int vid) {
        String query = "SELECT v_id FROM violate WHERE v_id = ?";
        try (Connection conn = config.connectDB();  
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, vid);  
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return true;
            } else {
                return false;  
            }
        } catch (SQLException e) {
            System.out.println("Error checking Violation ID: " + e.getMessage());
            return false; 
        }
    }

    
    public void deleteViolation() {
        Scanner sc = new Scanner(System.in);

    int vid = -1;
    while (true) {
        System.out.print("Enter Violation ID to delete: ");
        vid = sc.nextInt();
        sc.nextLine(); 
        if (isViolationsIdExists(vid)) {
            break; 
        } else {
            System.out.println("Violation ID does not exist. Please try again with a valid ID.");
        }
    }

    System.out.print("Are you sure you want to delete the violation with ID " + vid + "? (Yes/No): ");
    String confirmation = sc.nextLine().trim();

    while (!confirmation.equalsIgnoreCase("Yes") && !confirmation.equalsIgnoreCase("No")) {
        System.out.println("Invalid response. Please enter 'Yes' or 'No'.");
        System.out.print("Are you sure you want to delete the violation with ID " + vid + "? (Yes/No): ");
        confirmation = sc.nextLine().trim();
    }

    if (confirmation.equalsIgnoreCase("Yes")) {
        String qry = "DELETE FROM violate WHERE v_id = ?";

        config conf = new config();
        conf.deleteRecord(qry, vid);

        System.out.println("Violation with ID " + vid + " has been deleted successfully.");
    } else {
        System.out.println("Deletion canceled.");
    }
}

    private boolean isViolationsIdExists(int vid) {
        String query = "SELECT v_id FROM violate WHERE v_id = ?";
        try (Connection conn = config.connectDB();  
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, vid);  
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            } else {
                return false;  
            }
        } catch (SQLException e) {
            System.out.println("Error checking Violation ID: " + e.getMessage());
            return false;  
        }
        }

    
    
    //Sa addViolation ni dli mogana basta wla ni
    
    public boolean isDriverIdExists(int driverId) {
    String query = "SELECT d_id FROM cvts WHERE d_id = ?";
    try (Connection conn = config.connectDB(); 
         PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setInt(1, driverId);  
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            return true;
        } else {
            return false;
        }
    } catch (SQLException e) {
        System.out.println("Error checking Driver ID: " + e.getMessage());
        return false;  
    }
}

}
