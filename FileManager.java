// CLASS: FileManager
// Does all the reading and writing of files. This is our simple "database".
//
//   data/users.txt          one user per line
//   data/transactions.txt   one transaction per line
//   bills/Bill_T1001.txt    one bill file for every payment
//   reports/....csv         transaction history that opens in Excel
//
// Concepts used: File class, FileReader, FileWriter, BufferedReader, BufferedWriter,
//                try, catch, finally, IOException, ArrayList

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileManager {

    static String usersFile = "data/users.txt";
    static String transactionsFile = "data/transactions.txt";

    // Creates the folders if they do not exist yet (File class: exists and mkdir)
    public static void createFolders() {
        String[] folders = {"data", "bills", "reports"};
        for (String name : folders) {
            File folder = new File(name);
            if (!folder.exists()) {
                folder.mkdir();
            }
        }
    }

    // Adds one line at the end of a file.
    // "true" in FileWriter means APPEND: the old lines are kept.
    private static void appendLine(String fileName, String line) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(fileName, true));
            bw.write(line);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error writing to " + fileName);
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException e) {
                System.out.println("Error closing " + fileName);
            }
        }
    }

    // Reads every line of a file into an ArrayList
    private static ArrayList<String> readLines(String fileName) {
        ArrayList<String> lines = new ArrayList<String>();
        File file = new File(fileName);
        if (!file.exists()) {
            return lines;    // first run: no file yet, so return an empty list
        }

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading " + fileName);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                System.out.println("Error closing " + fileName);
            }
        }
        return lines;
    }

    // Writes a list of lines into a NEW file (any old file with that name is replaced)
    private static boolean writeFile(String fileName, ArrayList<String> lines) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(fileName));
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            System.out.println("Could not write " + fileName + ". If it is open in Excel, close it and try again.");
            return false;
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException e) {
                System.out.println("Error closing " + fileName);
            }
        }
    }

    // USERS

    public static void saveUser(User user) {
        appendLine(usersFile, user.toFileLine());
    }

    public static ArrayList<User> loadUsers() {
        ArrayList<User> users = new ArrayList<User>();
        for (String line : readLines(usersFile)) {
            String[] p = line.split(",");
            users.add(new User(p[0], p[1], p[2], p[3], p[4], p[5]));
        }
        return users;
    }

    // TRANSACTIONS

    public static void saveTransaction(Transaction t) {
        appendLine(transactionsFile, t.toFileLine());
    }

    public static ArrayList<Transaction> loadTransactions() {
        ArrayList<Transaction> list = new ArrayList<Transaction>();
        for (String line : readLines(transactionsFile)) {
            list.add(new Transaction(line));    // uses the second (overloaded) constructor
        }
        return list;
    }

    // BILL FILE: written right after every payment

    public static String writeBill(Transaction t) {
        String fileName = "bills/Bill_" + t.getTransactionId() + ".txt";
        writeFile(fileName, t.getBillLines());
        return fileName;
    }

    // EXCEL FILE (CSV): a CSV file has values separated by commas.
    // Excel opens it directly and puts every value in its own column.

    public static String exportToExcel(ArrayList<Transaction> list, String fileName, String title) {
        ArrayList<String> lines = new ArrayList<String>();

        lines.add(title);
        lines.add("");
        lines.add("Bill No.,Date,Time,Customer,Car Number,Car Type,Area,Station,Charger,"
                + "Units (kWh),Charging Time (min),Energy Cost (Rs),GST 18% (Rs),Total Paid (Rs),Payment");

        double totalUnits = 0;
        double totalPaid = 0;
        for (Transaction t : list) {
            lines.add(t.toExcelRow());
            totalUnits = totalUnits + t.getUnits();
            totalPaid = totalPaid + t.getTotalAmount();
        }

        lines.add("");
        lines.add("Number of transactions," + list.size());
        lines.add("Total energy (kWh)," + Math.round(totalUnits * 100) / 100.0);
        lines.add("Total amount paid (Rs)," + Math.round(totalPaid * 100) / 100.0);

        if (writeFile(fileName, lines)) {
            return fileName;
        }
        return null;
    }
}
