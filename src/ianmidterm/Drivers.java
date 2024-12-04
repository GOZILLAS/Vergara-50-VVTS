package ianmidterm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Drivers {
    
    private final Ianmidterm ss;
    private final Scanner sc;

    public Drivers(Ianmidterm ss, Scanner sc) {
        this.ss = ss;
        this.sc = sc;
    }

    public void Drivers() {
        String response = "yes";

        while (response.equalsIgnoreCase("yes")) {
            System.out.println("1. ADD DRIVER");
            System.out.println("2. VIEW DRIVERS");
            System.out.println("3. UPDATE DRIVER");
            System.out.println("4. DELETE DRIVER");
            System.out.println("5. EXIT");

            System.out.print("Enter Action: ");
            int action = 0;
            boolean validAction = false;

            while (!validAction) {
                try {
                    action = sc.nextInt();
                    validAction = true;
                } catch (InputMismatchException e) {
                    System.out.println("Enter a Valid No.");
                    sc.next(); 
                }
            }
            sc.nextLine(); 

            switch (action) {
                case 1:
                    addDriver();
                    break;
                case 2:
                    viewDrivers();
                    break;
                case 3:
                    viewDrivers();
                    updateDrivers();
                    viewDrivers();
                    break;
                case 4:
                    viewDrivers();
                    deleteDriver();
                    viewDrivers();
                    break;
                case 5:
                    System.out.println("Exiting to main menu...");
                    return; 
                default:
                    System.out.println("Invalid action. Please try again.");
            }
        }
    }

   public void addDriver() {
    Scanner sc = new Scanner(System.in);
    config conf = new config();

    String fname = "";
    while (true) {
        System.out.print("Driver First Name: ");
        fname = sc.next();
        if (isValidName(fname)) {
            break;
        } else {
            System.out.println("Invalid Name.Please Enter a Valid Name(Starts with a capital letter and letters only).");
        }
    }

    String lname = "";
    while (true) {
        System.out.print("Driver Last Name: ");
        lname = sc.next();
        if (isValidName(lname)) {
            break; 
        } else {
           System.out.println("Invalid Name.Please Enter a Valid Name(Starts with a capital letter and letters only).");
        }
    }

    String email = "";
    while (true) {
        System.out.print("Driver Email: ");
        email = sc.next();
        if (isValidEmail(email)) {
            break;
        } else {
            System.out.println("Invalid email format. Please enter a valid email address.");
        }
    }

    String ct = "";
    while (true) {
        System.out.print("Driver Contact Number: ");
        ct = sc.next();
        if (isValidContact(ct)) {
            break; 
        } else {
            System.out.println("Invalid Format. 11 Characters only!");
        }
    }

    System.out.print("Driver Vehicle: ");
    String vh = sc.next();

   
    String plt = "";
    while (true) {
        System.out.print("Driver Plate: ");
        plt = sc.next();
        if (isValidPlate(plt)) {
            break; 
        } else {
            System.out.println("Invalid plate format. Format should be like this: AAA-1234");
        }
    }

    String sql = "INSERT INTO cvts (d_fname, d_lname, d_email, d_contact, d_vehi, d_platenum) VALUES (?, ?, ?, ?, ?, ?)";
    conf.addRecord(sql, fname, lname, email, ct, vh, plt);     
}
    
//Validation
    public boolean isValidPlate(String plate) {
        return plate != null && plate.matches("^[A-Z]{3}-\\d{4}$");
    }

    public boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }

    public boolean isValidContact(String contact) {
        return contact != null && contact.matches("^\\d{11}$");
    }

    public boolean isValidName(String dname) {
        return dname != null && dname.matches("^[A-Z][a-zA-Z]*$");
    }

   
   
     public void viewDrivers() {
        String qry = "SELECT * FROM cvts";
        String[] hdrs = {"ID", "First Name", "Last Name", "Email", "Contact", "Vehicle", "Plate"};
        String[] clmns = {"d_id", "d_fname", "d_lname", "d_email", "d_contact", "d_vehi", "d_platenum" };

        config conf = new config();
        conf.viewRecords(qry, hdrs, clmns);
    }
    
   public void updateDrivers() {
    Scanner sc = new Scanner(System.in);
    int id;

    while (true) {
        try {
            System.out.print("Enter ID to Update: ");
            id = sc.nextInt();
            sc.nextLine(); 
            if (isDriverIdExists(id)) {
                break;  
            } else {
                System.out.println("Driver ID does not exist. Please enter a valid ID.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a numeric ID.");
            sc.next();  
        }
    }
    
    String nfname = "";
    while (true) {
        System.out.print("Driver New First Name: ");
        nfname = sc.nextLine();
        if (isValidName(nfname)) {
            break;
        } else {
            System.out.println("Invalid Name. Please try again (Starts with a capital and letters only).");
        }
    }
    
    String nlname = "";
    while (true) {
        System.out.print("New Last Name: ");
        nlname = sc.nextLine();
        if (isValidName(nlname)) {
            break;
        } else {
            System.out.println("Invalid Name. Please try again (Starts with a capital and letters only).");
        }
    }

    String nemail = "";
    while (true) {
        System.out.print("New Email: ");
        nemail = sc.nextLine();
        if (isValidEmail(nemail)) {
            break;
        } else {
            System.out.println("Invalid email format. Please enter a valid email.");
        }
    }

    String ncontact = "";
    while (true) {
        System.out.print("New Contact: ");
        ncontact = sc.nextLine();
        if (isValidContact(ncontact)) {
            break;
        } else {
            System.out.println("Invalid contact number. Please enter a valid contact (e.g., 09123456789).");
        }
    }

    String nvehicle = "";
    while (true) {
        System.out.print("New Vehicle: ");
        nvehicle = sc.nextLine();
        if (!nvehicle.isEmpty()) {
            break;
        } else {
            System.out.println("Vehicle cannot be empty. Please enter a valid vehicle.");
        }
    }

    String qry = "UPDATE cvts SET d_fname = ?, d_lname = ?, d_email = ?, d_contact = ?, d_vehi = ? WHERE d_id = ?";

    config conf = new config();
    conf.updateRecord(qry, nfname, nlname, nemail, ncontact, nvehicle, id);

    System.out.println("Driver details updated successfully!");
}

    private boolean isValidUpName(String name) {
        return name.matches("[A-Z][a-zA-Z]*");
    }
    
    private boolean isValidUpEmail(String email) {
        return email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    }

    private boolean isValidUpContact(String contact) {
        return contact.matches("^(09\\d{9})$");  
    }

    private boolean isDriverIdExists(int id) {
    String query = "SELECT COUNT(*) FROM cvts WHERE d_id = ?";
    try (Connection conn = config.connectDB();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setInt(1, id);  
        ResultSet rs = stmt.executeQuery();

        if (rs.next() && rs.getInt(1) > 0) {
            return true;
        } else {
            return false;
        }
    } catch (SQLException e) {
        System.out.println("Error checking Driver ID: " + e.getMessage());
        return false; 
    }
}

   public void deleteDriver() {
    Scanner sc = new Scanner(System.in);

    int id = -1;
    boolean validId = false;
    while (!validId) {
        System.out.print("Enter ID to delete: ");
        try {
            id = sc.nextInt();
            validId = true;
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid integer ID.");
            sc.next(); 
        }
    }

    String checkQry = "SELECT COUNT(*) FROM cvts WHERE d_id = ?";
    boolean driverExists = false;

    try (Connection conn = config.connectDB();
         PreparedStatement pstmt = conn.prepareStatement(checkQry)) {

        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next() && rs.getInt(1) > 0) {
            driverExists = true; 
        }

    } catch (SQLException e) {
        System.out.println("Error checking driver existence: " + e.getMessage());
    }

    if (driverExists) {
        System.out.print("Are you sure you want to delete the driver with ID " + id + "? (yes/no): ");
        String confirmation = sc.next();

        if (confirmation.equalsIgnoreCase("yes")) {
            String qry = "DELETE FROM cvts WHERE d_id = ?";
            config conf = new config();
            conf.deleteRecord(qry, id);
            System.out.println("Driver with ID " + id + " has been deleted.");
        } else {
            System.out.println("Driver deletion cancelled.");
        }
    } else {
        System.out.println("Driver with ID " + id + " does not exist.");
    }
}



    Drivers() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    
}
