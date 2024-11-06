package ianmidterm;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Ianmidterm {

public static void main(String[] args) {
    try (Scanner sc = new Scanner(System.in)) {
        Ianmidterm sr = new Ianmidterm();
        String response = "yes"; 

        while (response.equalsIgnoreCase("yes")) {
            System.out.println("1. DRIVER");
            System.out.println("2. VIOLATION");
            System.out.println("3. REPORT");
            System.out.println("4. EXIT");

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
                    drivers.Driver();
                    break;
                case 2:
                  
                    Violation violation = new Violation(sr, sc);
                    violation.Violate();
                    break;
                case 3:
                     Reports reports = new Reports(sr, sc);
                    reports.report();
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


    //-----------------------------------------------
    //DRIVER
    //-----------------------------------------------   

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
        System.out.print("Driver Contact: ");
        ct = sc.next();
        if (isValidContact(ct)) {
            break; 
        } else {
            System.out.println("Invalid contact format. Please enter a valid contact number.");
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
     
     
     public void updateDrivers(){
         Scanner sc = new Scanner(System.in);
         System.out.println("Enter id to update: ");
         int id = sc.nextInt();
         
         String nfname = "";
    while (true) {
        System.out.print("Driver New First Name: ");
        nfname = sc.next();
        if (isValidName(nfname)) {
            break;
        } else {
            System.out.println("Invalid Name.Please Enter a Valid Name(Starts with a capital and letters only).");
        }
    }
         System.out.println("New Last Name: ");
         String nlname = sc.next();
         System.out.println("New Email: ");
         String nemail = sc.next();
         System.out.println("New Contact: ");
         String ncontact = sc.next();
         System.out.println("New Vehicle: ");
         String nvehicle = sc.next();
                         
         String qry = "UPDATE cvts SET d_fname = ?, d_lname = ?, d_email = ?, d_contact = ?, d_vehi = ?  WHERE d_id = ?";
        
         config conf = new config();
        conf.deleteRecord(qry, nfname, nlname, nemail, ncontact, nvehicle, id);
     }
     
     
    public void deleteDriver(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter id to delete:");
        int id = sc.nextInt();
        
        String qry = "DELETE FROM cvts WHERE d_id = ?";
        
        config conf = new config();
        conf.deleteRecord(qry, id);
    }
    
 
    
    //-----------------------------------------------
    //VIOLATION
    //-----------------------------------------------   
    
    public void addViolation() {
        Scanner sc = new Scanner(System.in);
        config conf = new config();
       System.out.println("Driver's ID:");
       int did = sc.nextInt();
       sc.nextLine(); 
       System.out.print("Violation Name: ");
       String vname = sc.nextLine(); 
       System.out.print("Violation Fine: ");
       String vfine = sc.nextLine();
       System.out.print("Enter the date of violation (YYYY-MM-DD): ");
       String vdate = sc.nextLine(); 
       System.out.print("Violation Status (Pending/Paid): ");
       String vstatus = sc.nextLine(); 

       String sql = "INSERT INTO violate (d_id, v_name, v_fine, v_date, v_status) VALUES (?, ?, ?, ?, ?)";

       conf.addRecord(sql, did, vname, vfine, vdate, vstatus);     
  }

    
   public void viewViolation() {
    String qry = "SELECT v.v_id, v.d_id, v.v_name, v.v_fine, v.v_date, v.v_status, d.d_lname " +
                 "FROM violate v " +
                 "JOIN cvts d ON v.d_id = d.d_id"; 

    String[] hdrs = {"Vio-ID", "Dri-ID", "Violation", "Fine", "Date", "Status", "Driver Last Name"};
    String[] clmns = {"v_id", "d_id", "v_name", "v_fine", "v_date", "v_status", "d_lname"};

    config conf = new config();
    conf.viewRecords(qry, hdrs, clmns);
}




     public void updateViolation(){
         Scanner sc = new Scanner(System.in);
         System.out.println("Enter id to update: ");
         int id = sc.nextInt();
         
         System.out.println("New  Violation: ");
         String nvname = sc.next();
         System.out.println("New Fine: ");
         String nfiname = sc.next();
         
         
         String qry = "UPDATE violate SET v_name = ?, v_fine = ? WHERE d_id = ?";
        
         config conf = new config();
        conf.deleteRecord(qry, nvname, nfiname, id);
     }
     
    public void deleteViolation(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter id to delete:");
        int id = sc.nextInt();
        
        String qry = "DELETE FROM violate WHERE d_id = ?";
        
        config conf = new config();
        conf.deleteRecord(qry, id);
    }

    
    //-----------------------------------------------
    //REPORTS
    //-----------------------------------------------   
         

    public void viewReports() {
    // SQL query to retrieve all drivers and the number of violations, including drivers without violations
    String qry = "SELECT d.d_id, GROUP_CONCAT(v.v_id) AS Violations, COUNT(v.v_id) AS ViolationCount, d.d_lname " +
                 "FROM cvts d " +
                 "LEFT JOIN violate v ON d.d_id = v.d_id " +  // Use LEFT JOIN to include all drivers
                 "GROUP BY d.d_id";  

    // Headers and columns for the report
    String[] hdrs = {"Dri-ID", "Violation IDs", "Number of Violations", "Last Name"};
    String[] clmns = {"d_id", "Violations", "ViolationCount", "d_lname"};

    config conf = new config();
    conf.viewRecords(qry, hdrs, clmns);  // Calls the viewRecords method to display the data

        Scanner sc = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("\nOptions:");
            System.out.println("1. View More Details ");
            System.out.println("2. Exit view");

            System.out.print("Enter Action: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter Driver ID to view details: ");
                    int driverId = sc.nextInt();

                    String detailQry = "SELECT d.d_id, d.d_fname, d.d_lname, d.d_contact, d.d_email, d.d_vehi, d.d_platenum, " +
                                       "GROUP_CONCAT(v.v_id) AS violation_ids, GROUP_CONCAT(v.v_fine) AS fines, " +
                                       "GROUP_CONCAT(v.v_status) AS statuses, GROUP_CONCAT(v.v_date) AS dates " +
                                       "FROM cvts d " +
                                       "LEFT JOIN violate v ON d.d_id = v.d_id " +
                                       "WHERE d.d_id = ? " +
                                       "GROUP BY d.d_id";

                    String[] detailHdrs = {"Dri-ID", "First Name", "Last Name", "Contact", "Email", "Vehicle", "Plate Number", "Violation IDs", "Fines", "Status", "Date"};
                    String[] detailClmns = {"d_id", "d_fname", "d_lname", "d_contact", "d_email", "d_vehi", "d_platenum", "violation_ids", "fines", "statuses", "dates"};

                    conf.viewRecord(detailQry, detailHdrs, detailClmns, driverId); 
                case 2:
                    exit = true;
                    System.out.println("Exiting view...");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}




    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    





    
   