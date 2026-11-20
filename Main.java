// MAIN CLASS: Main
// The program starts here. It shows the menus and connects all the classes.
//
// Flow of the program:
//   Main menu  : Sign Up (first time) or Sign In (email + password)
//   User menu  : Book a charging slot, view stations, history and Excel download, my profile
//
// Concepts used: Scanner, loops, switch, ArrayList, enhanced for loop,
//                lambda expression, anonymous inner class, Comparator, exception handling

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.Scanner;

public class Main
{

    static Scanner sc = new Scanner(System.in);

    // All users, transactions and stations are kept in ArrayLists while the program runs
    static ArrayList<User> users = new ArrayList<User>();
    static ArrayList<Transaction> transactions = new ArrayList<Transaction>();
    static ArrayList<Station> stations = new ArrayList<Station>();

    // 5 areas, and 4 station names in each area (2D array: row = area)
    static String[] areas = {"Andheri", "Kandivali", "Borivali", "Bandra", "Dadar"};
    static String[][] stationNames = {
        {"DN Nagar", "JVLR", "Lokhandwala", "MIDC"},
        {"Thakur Village", "Mahavir Nagar", "Charkop", "Poisar"},
        {"IC Colony", "Gorai", "Shimpoli", "Borivali Station"},
        {"Linking Road", "Bandstand", "BKC", "Hill Road"},
        {"Shivaji Park", "Hindmata", "Dadar TT", "Prabhadevi"}
    };


    public static void main(String[] args)
    {

        // Step 1: prepare folders and load saved data from the files
        FileManager.createFolders();
        users = FileManager.loadUsers();
        transactions = FileManager.loadTransactions();

        // Step 2: create the 20 stations and add random traffic
        setupStations();

        System.out.println();
        System.out.println("  EV CHARGING NETWORK");
        System.out.println("  Smart EV charging across Mumbai");
        System.out.println("  " + stations.size() + " stations, " + (stations.size() * 4) + " chargers, 5 areas");

        // Step 3: main menu, repeats until the user chooses Exit
        boolean running = true;
        while (running) 
        {
            System.out.println();
            System.out.println("MAIN MENU");
            System.out.println("  1. Sign Up  (first time user)");
            System.out.println("  2. Sign In");
            System.out.println("  0. Exit");
            System.out.print("Enter your choice: ");
            int choice = readNumber(0, 2);

            switch (choice) 
            {
                case 1:
                    signUp();
                    break;
                case 2:
                    signIn();
                    break;
                case 0:
                    running = false;
                    System.out.println();
                    System.out.println("Thank you for using EV Charging Network. Drive green!");
                    break;
            }
        }
    }


    // Creates 4 stations in each of the 5 areas and adds random traffic
    static void setupStations()
    {
        Random random = new Random();
        int code = 1;
        for (int a = 0; a < areas.length; a++) 
            {
            for (int s = 0; s < stationNames[a].length; s++)
            {
                Station station = new Station(stationNames[a][s], areas[a], "S" + code);
                station.simulateTraffic(random);
                stations.add(station);
                code++;
            }
        }
    }


    // SIGN UP: first time users create an account
    static void signUp() {
        System.out.println();
        System.out.println("SIGN UP");

        System.out.print("Full name: ");
        String name = readText();

        // Email must look like an email and must not be registered already
        System.out.print("Email: ");
        String email = readText().toLowerCase();
        while (!email.contains("@") || !email.contains(".") || findUser(email) != null) {
            if (findUser(email) != null)
            {
                System.out.print("This email is already registered. Use another email: ");
            } else 
            {
                System.out.print("Please enter a valid email (like name@gmail.com): ");
            }
            email = readText().toLowerCase();
        }

        // Password: at least 4 characters, typed twice to confirm
        System.out.print("Create password (at least 4 characters): ");
        String password = readText();
        while (password.length() < 4) {
            System.out.print("Too short. Enter at least 4 characters: ");
            password = readText();
        }
        System.out.print("Confirm password: ");
        String confirm = readText();
        while (!confirm.equals(password)) {
            System.out.print("Passwords do not match. Confirm password again: ");
            confirm = readText();
        }

        // Phone number: exactly 10 digits
        System.out.print("Phone number (10 digits): ");
        String phone = readText();
        while (!isDigits(phone, 10)) 
        {
            System.out.print("Phone number must be exactly 10 digits: ");
            phone = readText();
        }

        // Car number plate: spaces removed, capital letters
        System.out.print("Car number plate (like MH02AB1234): ");
        String carNumber = readText().replace(" ", "").toUpperCase();
        while (carNumber.length() < 6) 
        {
            System.out.print("Number plate is too short. Enter again: ");
            carNumber = readText().replace(" ", "").toUpperCase();
        }

        // Car type decides the battery size
        System.out.println("Car type:");
        System.out.println("  1. Hatchback  (30 kWh battery)");
        System.out.println("  2. Sedan      (40 kWh battery)");
        System.out.println("  3. SUV        (60 kWh battery)");
        System.out.print("Enter your choice: ");
        int typeChoice = readNumber(1, 3);
        String carType = "Hatchback";
        if (typeChoice == 2) 
        {
            carType = "Sedan";
        }
        if (typeChoice == 3)
         {
            carType = "SUV";
        }

        // Create the User object, keep it in the list and save it in the file
        User user = new User(name, email, phone, password, carNumber, carType);
        users.add(user);
        FileManager.saveUser(user);

        System.out.println();
        System.out.println("Account created successfully, " + name + "!");
        System.out.println("Please sign in with your email and password.");
    }


    // SIGN IN: only email and password.
    // Wrong details throw our InvalidLoginException, which is caught here.
    static void signIn() 
    {
        System.out.println();
        System.out.println("SIGN IN");
        System.out.print("Email: ");
        String email = readText().toLowerCase();
        System.out.print("Password: ");
        String password = readText();

        try {
            User user = findUser(email);
            if (user == null) {
                throw new InvalidLoginException("No account found with this email. Please sign up first.");
            }
            user.checkPassword(password);
            userMenu(user);
        } catch (InvalidLoginException e) {
            System.out.println("Sign in failed: " + e.getMessage());
        }
    }

    // Searches the users list for this email. Returns null if not found.
    static User findUser(String email) 
    {
        for (User u : users) {
            if (u.getEmail().equals(email)) {
                return u;
            }
        }
        return null;
    }


    // USER MENU: shown after signing in
    static void userMenu(User user) 
    {
        System.out.println();
        System.out.println("Welcome, " + user.getName() + "!   Car: " + user.getCarNumber() + " (" + user.getCarType() + ")");

        boolean signedIn = true;
        while (signedIn) {
            System.out.println();
            System.out.println("WHAT WOULD YOU LIKE TO DO?");
            System.out.println("  1. Book a charging slot");
            System.out.println("  2. View charging stations");
            System.out.println("  3. Transaction history and Excel download");
            System.out.println("  4. My profile");
            System.out.println("  0. Sign out");
            System.out.print("Enter your choice: ");
            int choice = readNumber(0, 4);

            switch (choice) 
            {
                case 1:
                    bookSlot(user);
                    break;
                case 2:
                    viewStations();
                    break;
                case 3:
                    history(user);
                    break;
                case 4:
                    showProfile(user);
                    break;
                case 0:
                    signedIn = false;
                    System.out.println("Signed out. See you soon, " + user.getName() + "!");
                    break;
            }
        }
    }


    // Shows the 5 areas with how many chargers are free in each, and returns the chosen area
    static String chooseArea() 
    {
        System.out.println();
        System.out.println("CHOOSE AN AREA");
        for (int i = 0; i < areas.length; i++)
        {
            int free = 0;
            for (Station s : stations)
            {
                if (s.getArea().equals(areas[i]))
                {
                    free = free + s.countFree();
                }
            }
            System.out.println("  " + (i + 1) + ". " + areas[i] + "   (" + free + " chargers free)");
        }
        System.out.print("Enter your choice: ");
        return areas[readNumber(1, areas.length) - 1];
    }

    // Returns the stations of one area, with the most free chargers first
    static ArrayList<Station> stationsInArea(String area)
    {
        ArrayList<Station> list = new ArrayList<Station>();
        for (Station s : stations)
        {
            if (s.getArea().equals(area))
            {
                list.add(s);
            }
        }
        // Lambda expression used as a Comparator: more free chargers come first
        list.sort((a, b) -> b.countFree() - a.countFree());
        return list;
    }


    // BOOK A SLOT: the main feature
    static void bookSlot(User user)
    {

        // 1. Area
        String area = chooseArea();
        ArrayList<Station> list = stationsInArea(area);

        if (list.get(0).countFree() == 0) 
        {
            System.out.println("Sorry, every station in " + area + " is full right now. Please try another area.");
            return;
        }

        // 2. Station (full stations cannot be chosen)
        System.out.println();
        System.out.println("STATIONS IN " + area.toUpperCase());
        for (int i = 0; i < list.size(); i++)
        {
            System.out.println("  " + (i + 1) + ". " + list.get(i).getStationName() + "   (" + list.get(i).getStatus() + ")");
        }

        Station station = null;
        boolean chosen = false;
        while (!chosen) 
        {
            System.out.print("Enter your choice: ");
            station = list.get(readNumber(1, list.size()) - 1);
            if (station.countFree() > 0) 
            {
                chosen = true;
            } else 
            {
                System.out.println("This station is full. Next charger is free in "
                        + station.shortestWait(0) + " minutes. Please choose another station.");
            }
        }
        station.displayDetails();

        // 3. Charger type
        System.out.println();
        System.out.println("CHOOSE A CHARGER");
        System.out.println("  1. AC charger     Rs. 12 per kWh, slower        (" + station.countFree(1) + " free)");
        System.out.println("  2. Fast charger   Rs. 20 per kWh + Rs. 50 fee   (" + station.countFree(2) + " free)");
        System.out.print("Enter your choice: ");
        int type = readNumber(1, 2);

        ChargingPoint charger;
        try
        {
            charger = station.findFreeCharger(type);
        } catch (ChargerNotAvailableException e) 
        {
            System.out.println(e.getMessage());
            System.out.println("Please choose the other charger type or another station.");
            return;
        }

        // 4. Battery level: how much energy is needed
        double battery = user.getBatteryCapacity();
        System.out.println();
        System.out.println("Your " + user.getCarType() + " has a " + battery + " kWh battery.");
        System.out.print("Current battery level in % (0 to 99): ");
        int current = readNumber(0, 99);
        System.out.print("Charge up to what % (" + (current + 1) + " to 100): ");
        int target = readNumber(current + 1, 100);
        double units = battery * (target - current) / 100;

        // 5. Booking summary before paying
        String id = "T" + (1001 + transactions.size());
        Transaction t = new Transaction(id, user, station, charger, units);

        System.out.println();
        System.out.println("BOOKING SUMMARY");
        System.out.println("  Station        : " + station.getStationName() + ", " + area);
        System.out.println("  Charger        : " + charger.getPointId() + " (" + charger.getChargerType() + ")");
        System.out.println("  Battery        : " + current + "% to " + target + "%");
        System.out.println("  Energy needed  : " + t.getUnits() + " kWh");
        System.out.println("  Charging time  : about " + t.getMinutes() + " minutes");
        System.out.println("  Energy cost    : Rs. " + t.getEnergyCost());
        System.out.println("  GST (18%)      : Rs. " + t.getGst());
        System.out.println("  Total to pay   : Rs. " + t.getTotalAmount());
        System.out.println();
        System.out.println("  1. Confirm and pay");
        System.out.println("  2. Cancel");
        System.out.print("Enter your choice: ");
        if (readNumber(1, 2) == 2) {
            System.out.println("Booking cancelled.");
            return;
        }

        // 6. Payment: the variable type is the interface Payable (runtime polymorphism)
        System.out.println();
        System.out.println("CHOOSE A PAYMENT METHOD");
        System.out.println("  1. UPI");
        System.out.println("  2. Card");
        System.out.println("  3. Cash at station");
        System.out.print("Enter your choice: ");
        int payChoice = readNumber(1, 3);

        Payable payment;
        if (payChoice == 1) {
            System.out.print("Enter your UPI ID (like name@okbank): ");
            String upiId = readText();
            while (!upiId.contains("@")) {
                System.out.print("A UPI ID must contain @. Enter again: ");
                upiId = readText();
            }
            payment = new UpiPayment(upiId);
        } else if (payChoice == 2) {
            System.out.print("Enter your 16 digit card number: ");
            String cardNumber = readText().replace(" ", "");
            while (!isDigits(cardNumber, 16)) {
                System.out.print("Card number must be 16 digits. Enter again: ");
                cardNumber = readText().replace(" ", "");
            }
            payment = new CardPayment(cardNumber);
        } else {
            payment = new CashPayment();
        }

        System.out.println();
        payment.pay(t.getTotalAmount());
        t.setPaymentMethod(payment.getMethodName());

        // 7. Charger becomes busy, transaction is saved, bill file is written
        charger.occupy(t.getMinutes());
        transactions.add(t);
        FileManager.saveTransaction(t);
        String billFile = FileManager.writeBill(t);

        t.printBill();
        System.out.println();
        System.out.println("Booking confirmed! Your bill is saved as " + billFile);
    }


    // VIEW STATIONS: every charger in one area, free or busy
    static void viewStations() {
        String area = chooseArea();
        System.out.println();
        System.out.println("LIVE STATUS IN " + area.toUpperCase());
        for (Station s : stationsInArea(area)) {
            s.displayDetails();
        }
        System.out.println();
        System.out.print("Press Enter to go back...");
        sc.nextLine();
    }


    // HISTORY: see past transactions and download them as an Excel (CSV) file
    static void history(User user) {

        // Collect only this user's transactions
        ArrayList<Transaction> myList = new ArrayList<Transaction>();
        for (Transaction t : transactions) {
            if (t.getUserEmail().equals(user.getEmail())) {
                myList.add(t);
            }
        }

        if (myList.size() == 0) 
        {
            System.out.println();
            System.out.println("You have no transactions yet. Book your first charging slot!");
            return;
        }

        // Sort newest first using an ANONYMOUS INNER CLASS that implements Comparator
        Collections.sort(myList, new Comparator<Transaction>() {
            @Override
            public int compare(Transaction a, Transaction b) {
                // date + time + bill number, so two bookings in the same minute are still in order
                String first = a.getDate() + a.getTime() + a.getTransactionId();
                String second = b.getDate() + b.getTime() + b.getTransactionId();
                return second.compareTo(first);
            }
        });

        double totalSpent = 0;
        for (Transaction t : myList) {
            totalSpent = totalSpent + t.getTotalAmount();
        }

        // Part of the email before @, used in file names, for example "prerak"
        String shortName = user.getEmail().substring(0, user.getEmail().indexOf("@"));

        boolean inHistory = true;
        while (inHistory) {
            System.out.println();
            System.out.println("TRANSACTION HISTORY");
            System.out.println("  You have " + myList.size() + " transactions. Total spent: Rs. " + Math.round(totalSpent * 100) / 100.0);
            System.out.println();
            System.out.println("  1. My last transaction");
            System.out.println("  2. Transactions between two dates");
            System.out.println("  3. All my transactions");
            System.out.println("  0. Back");
            System.out.print("Enter your choice: ");
            int choice = readNumber(0, 3);

            if (choice == 1) {
                // Last transaction: show its full bill, then download it
                Transaction last = myList.get(0);
                last.printBill();
                ArrayList<Transaction> one = new ArrayList<Transaction>();
                one.add(last);
                download(one, "reports/" + shortName + "_last_transaction.csv",
                        "Last transaction of " + user.getName());

            } else if (choice == 2) {
                // Date range: keep only transactions between the two dates
                LocalDate from = readDate("From date (yyyy-mm-dd, like 2026-10-01): ");
                LocalDate to = readDate("To date   (yyyy-mm-dd, like 2026-10-31): ");
                while (to.isBefore(from)) {
                    System.out.println("The To date cannot be before the From date.");
                    to = readDate("To date   (yyyy-mm-dd): ");
                }

                ArrayList<Transaction> range = new ArrayList<Transaction>();
                for (Transaction t : myList) {
                    LocalDate d = LocalDate.parse(t.getDate());
                    if (!d.isBefore(from) && !d.isAfter(to)) {
                        range.add(t);
                    }
                }

                if (range.size() == 0) {
                    System.out.println("No transactions between " + from + " and " + to + ".");
                } else {
                    download(range, "reports/" + shortName + "_" + from + "_to_" + to + ".csv",
                            "Transactions of " + user.getName() + " from " + from + " to " + to);
                }

            } else if (choice == 3) {
                download(myList, "reports/" + shortName + "_all_transactions.csv",
                        "All transactions of " + user.getName());

            } else {
                inHistory = false;
            }
        }
    }

    // Shows the transactions on screen (oldest first) and writes them into the Excel file
    static void download(ArrayList<Transaction> list, String fileName, String title) {
        ArrayList<Transaction> sorted = new ArrayList<Transaction>(list);
        // Lambda expression: sort by date and time, oldest first, so the sheet reads date-wise
        sorted.sort((a, b) -> (a.getDate() + a.getTime() + a.getTransactionId())
                .compareTo(b.getDate() + b.getTime() + b.getTransactionId()));

        System.out.println();
        for (Transaction t : sorted) {
            t.printShort();
        }

        String saved = FileManager.exportToExcel(sorted, fileName, title);
        if (saved != null) {
            System.out.println();
            System.out.println("Excel file downloaded: " + saved);
            System.out.println("Open it from the reports folder. It opens directly in Excel.");
        }
    }


    // PROFILE: user details plus a small summary of their charging
    static void showProfile(User user) {
        int count = 0;
        double units = 0;
        double spent = 0;
        for (Transaction t : transactions) {
            if (t.getUserEmail().equals(user.getEmail())) {
                count++;
                units = units + t.getUnits();
                spent = spent + t.getTotalAmount();
            }
        }

        System.out.println();
        System.out.println("MY PROFILE");
        user.displayDetails();
        System.out.println();
        System.out.println("Total bookings : " + count);
        System.out.println("Total energy   : " + Math.round(units * 100) / 100.0 + " kWh");
        System.out.println("Total spent    : Rs. " + Math.round(spent * 100) / 100.0);
    }


    // INPUT HELPERS: they keep asking until the input is correct, so the program never crashes

    // Reads a whole number between min and max
    static int readNumber(int min, int max) {
        while (true) {
            try {
                int number = Integer.parseInt(sc.nextLine().trim());
                if (number >= min && number <= max) {
                    return number;
                }
                System.out.print("Please enter a number from " + min + " to " + max + ": ");
            } catch (NumberFormatException e) {
                System.out.print("That is not a number. Try again: ");
            }
        }
    }

    // Reads text that is not empty. Commas are removed because our files use commas to separate values.
    static String readText() {
        String text = sc.nextLine().trim();
        while (text.isEmpty()) {
            System.out.print("This cannot be empty. Try again: ");
            text = sc.nextLine().trim();
        }
        return text.replace(",", " ");
    }

    // Reads a date like 2026-10-01. A wrong format throws DateTimeParseException, which we catch.
    static LocalDate readDate(String message) {
        while (true) {
            System.out.print(message);
            try {
                return LocalDate.parse(sc.nextLine().trim());
            } catch (DateTimeParseException e) {
                System.out.println("Wrong date format. Please type it like 2026-10-01.");
            }
        }
    }

    // Checks that the text has exactly "length" characters and all of them are digits
    static boolean isDigits(String text, int length) {
        if (text.length() != length) {
            return false;
        }
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (ch < '0' || ch > '9') {
                return false;
            }
        }
        return true;
    }
}
