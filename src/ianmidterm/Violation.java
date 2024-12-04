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
        try {
            System.out.print("Enter Driver ID: ");
            did = sc.nextInt();
            if (isDriverIdExists(did)) {
                break;  
            } else {
                System.out.println("ID doesn't exist. Please Enter again.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a numeric Driver ID.");
            sc.next(); 
        }
    }

    System.out.println(
    "==========================================================\n" +   
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
        "11. Emissions Violations\n" +
    "==========================================================\n"
    );

    int lid = -1;
    while (lid < 1 || lid > 11) {
        try {
            System.out.print("Choose Violation: ");
            lid = sc.nextInt();
            if (lid < 1 || lid > 11) {
                System.out.println("Choose one from the options above. Try again.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Enter a numeric Violation ID.");
            sc.next(); 
        }
    }
    sc.nextLine();

    String vfine;
    while (true) {
        try {
            System.out.print("Violation Fine: ");
            vfine = sc.nextLine();
            if (!vfine.matches("\\d+(\\.\\d{1,2})?")) {
                throw new IllegalArgumentException("Invalid fine format. Use numeric values (e.g., 100, 100.00).");
            }
            break;  
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    String vdate;
    while (true) {
        try {
            System.out.print("Enter the Date (Year-Month-Day): ");
            vdate = sc.nextLine();
            if (!vdate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                throw new IllegalArgumentException("Invalid date format. Use YYYY-MM-DD.");
            }
            break;  
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    String vstatus;
    while (true) {
        try {
            System.out.print("Enter Status: ");
            vstatus = sc.nextLine().trim();
            if (!vstatus.equalsIgnoreCase("Pending") && !vstatus.equalsIgnoreCase("Paid")) {
                throw new IllegalArgumentException("Invalid status. Enter 'Pending' or 'Paid'.");
            }
            break;  
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    try {
        String sql = "INSERT INTO violate (d_id, l_id, v_fine, v_date, v_status) VALUES (?, ?, ?, ?, ?)";
        conf.addRecord(sql, did, lid, vfine, vdate, vstatus);
        System.out.println("Violation added successfully!");
    } catch (Exception e) {
        System.out.println("Error adding violation: " + e.getMessage());
    }
}

    
   public void viewViolation() {
    String qry = "SELECT v.v_id, v.d_id, l.l_name, v.v_fine, v.v_date, v.v_status, d.d_lname " +
                 "FROM violate v " +
                 "JOIN law l ON v.l_id = l.l_id " +
                 "JOIN cvts d ON v.d_id = d.d_id";

    String[] hdrs = {"Violator-ID", "Driver-ID", "Violation", "Fine", "Date", "Status", "Driver Last Name"};
    String[] clmns = {"v_id", "d_id", "l_name", "v_fine", "v_date", "v_status", "d_lname"};

    config conf = new config();
    try {
        conf.viewRecords(qry, hdrs, clmns);
    } catch (Exception e) {
        System.out.println("Error retrieving violations: " + e.getMessage());
    }
}

    
    public void updateViolation() {
    Scanner sc = new Scanner(System.in);
    System.out.print("Enter Violator ID: ");
    
    int vid = -1;
    while (true) {
        try {
            System.out.print("");
            vid = sc.nextInt();
            sc.nextLine();  

            if (isViolatorIdExists(vid)) {
                break; 
            } else {
                System.out.println("Violation ID doens't exist. Please Try Again" );
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please Try again ");
            sc.next(); 
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
        try {
            System.out.print("Enter Violation ID to delete: ");
            vid = sc.nextInt();
            sc.nextLine();  
            if (isViolationsIdExists(vid)) {
                break;
            } else {
                System.out.println("Violation ID does not exist. Please try again.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a numeric Violation ID.");
            sc.next();  
        }
    }

    String confirmation = "";
    while (true) {
        try {
            System.out.print("Are you sure you want to delete " + vid + "? (Yes/No): ");
            confirmation = sc.nextLine().trim();
            if (confirmation.equalsIgnoreCase("Yes") || confirmation.equalsIgnoreCase("No")) {
                break;  
            } else {
                throw new IllegalArgumentException("Invalid response. Please enter 'Yes' or 'No'.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    if (confirmation.equalsIgnoreCase("Yes")) {
        try {
            String qry = "DELETE FROM violate WHERE v_id = ?";
            config conf = new config();
            conf.deleteRecord(qry, vid);

            System.out.println("Violation with ID " + vid + " has been deleted successfully.");
        } catch (Exception e) {
            System.out.println("Error while deleting violation: " + e.getMessage());
        }
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
        return rs.next(); 
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
