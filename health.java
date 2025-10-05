import java.time.LocalDate;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *  The main class that drives the Campus Lost and Found System. [cite: 38]
 * It handles user interaction, manages the collection of items, and contains the main application loop.
 */
public class LostFoundSystem {

    private final ArrayList<Item> items = new ArrayList<>();  // Manages data in-memory [cite: 64]
    private final Scanner scanner = new Scanner(System.in);

    /**
     * The main entry point of the application.
     */
    public static void main(String[] args) {
        LostFoundSystem system = new LostFoundSystem();
        system.run();
    }

    /**
     *  Contains the main loop that displays the menu and handles user choices. [cite: 72]
     */
    public void run() {
        boolean isRunning = true;
        while (isRunning) {
            printMenu();
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character left by nextInt()

                switch (choice) {
                    case 1:
                        reportAnItem("Lost");  // Functionality to report a lost item [cite: 53]
                        break;
                    case 2:
                        reportAnItem("Found");  // Functionality to report a found item [cite: 55]
                        break;
                    case 3:
                        viewAllItems();  // Functionality to view all reported items [cite: 57]
                        break;
                    case 4:
                        searchItems();  // Functionality to search for an item [cite: 59]
                        break;
                    case 5:
                        matchItems();  // Functionality to check for potential matches [cite: 21]
                        break;
                    case 6:
                        markItemAsReturned();  // Admin functionality to mark an item as returned [cite: 65, 66]
                        break;
                    case 7:
                        isRunning = false;
                        System.out.println("Thank you for using the Campus Lost and Found System!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 7.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");  // Error handling for non-numeric input [cite: 73]
                scanner.nextLine(); // Clear the invalid input from the scanner
            }
        }
        scanner.close();
    }

    /**
     * Prints the main menu to the console.
     */
    private void printMenu() {
        System.out.println("\n--- Campus Lost and Found System ---");
        System.out.println("1. Report a Lost Item");
        System.out.println("2. Report a Found Item");
        System.out.println("3. View All Reported Items");
        System.out.println("4. Search for an Item by Name");
        System.out.println("5. Find Potential Matches");
        System.out.println("6. Mark an Item as Returned (Admin)");
        System.out.println("7. Exit");
        System.out.print("Enter your choice: ");
    }

    /**
     *  Handles the logic for reporting both lost and found items. [cite: 19]
     * @param status The status of the item to be reported ("Lost" or "Found").
     */
    private void reportAnItem(String status) {
        System.out.print("Enter item name: ");
        String name = scanner.nextLine();
        System.out.print("Enter a brief description: ");
        String description = scanner.nextLine();
        System.out.print("Enter the location where the item was " + status.toLowerCase() + ": ");
        String location = scanner.nextLine();

        Item newItem = new Item(name, description, location, status);
        items.add(newItem);

        System.out.println("\nSuccessfully reported! The new Item ID is: " + newItem.getItemId());
    }

    /**
     *  Displays all items currently stored in the system. [cite: 58]
     */
    private void viewAllItems() {
        System.out.println("\n--- All Reported Items ---");
        if (items.isEmpty()) {
            System.out.println("No items have been reported yet.");
            return;
        }
        for (Item item : items) {
            System.out.println(item); // Uses the toString() method from the Item class
        }
    }

    /**
     *  Searches for items by name, ignoring case. [cite: 60]
     */
    private void searchItems() {
        System.out.print("Enter the name of the item to search for: ");
        String query = scanner.nextLine();
        ArrayList<Item> results = new ArrayList<>();

        for (Item item : items) {
            if (item.getItemName().equalsIgnoreCase(query)) {
                results.add(item);
            }
        }

        System.out.println("\n--- Search Results ---");
        if (results.isEmpty()) {
            System.out.println("No items found with that name.");
        } else {
            for (Item item : results) {
                System.out.println(item);
            }
        }
    }

    /**
     *  A simple matching algorithm that finds "Found" items whose names match "Lost" items. [cite: 82]
     */
    private void matchItems() {
        System.out.println("\n--- Potential Matches ---");
        boolean matchFound = false;
        for (Item lostItem : items) {
            if (lostItem.getStatus().equals("Lost")) {
                for (Item foundItem : items) {
                    if (foundItem.getStatus().equals("Found") && lostItem.getItemName().equalsIgnoreCase(foundItem.getItemName())) {
                        System.out.println("Potential Match Found:");
                        System.out.println("  Lost Item -> " + lostItem);
                        System.out.println("  Found Item -> " + foundItem);
                        matchFound = true;
                    }
                }
            }
        }
        if (!matchFound) {
            System.out.println("No potential matches found at this time.");
        }
    }

    /**
     *  An admin function to update an item's status to "Returned". [cite: 44]
     */
    private void markItemAsReturned() {
        System.out.print("Enter the ID of the item to mark as returned: ");
        try {
            int itemId = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            for (Item item : items) {
                if (item.getItemId() == itemId) {
                    item.setStatus("Returned");
                    System.out.println("Item ID " + itemId + " has been marked as Returned.");
                    return;
                }
            }
            System.out.println("Item with ID " + itemId + " not found.");
        } catch (InputMismatchException e) {
            System.out.println("Invalid ID. Please enter a number.");
            scanner.nextLine(); // Clear the invalid input
        }
    }
}

/**
 *  Represents the details of a lost or found item. [cite: 29, 30]
 *  This class demonstrates good encapsulation practices. [cite: 46, 71]
 */
class Item {
     // Attributes for an item [cite: 31]
    private static int idCounter = 1; // Used to auto-generate unique IDs
    private final int itemId;
    private final String itemName;
    private final String description;
    private final String location;
    private final LocalDate date;
    private String status; // "Lost", "Found", or "Returned"

    /**
     *  Constructor to create a new Item object. [cite: 70]
     */
    public Item(String itemName, String description, String location, String status) {
        this.itemId = idCounter++;
        this.itemName = itemName;
        this.description = description;
        this.location = location;
        this.date = LocalDate.now(); // Sets the date to the current date
        this.status = status;
    }

    // --- Getters ---
    public int getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public String getStatus() {
        return status;
    }

    // --- Setter ---
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     *  Provides a string representation of the item for easy display. [cite: 32]
     */
    @Override
    public String toString() {
        return "------------------------------------------\n" +
               "ID: " + itemId + " | Item: " + itemName + " | Status: " + status + "\n" +
               "Description: " + description + "\n" +
               "Location: " + location + " | Date Reported: " + date;
    }
}

/**
 *  Represents a user (student or staff) of the system. [cite: 34]
 */
class User {
     // Attributes for a user [cite: 35]
    private final int userId;
    private final String name;
    private final String contactDetails;

    public User(int userId, String name, String contactDetails) {
        this.userId = userId;
        this.name = name;
        this.contactDetails = contactDetails;
    }

    // Getters for user details
    public int getUserId() { return userId; }
    public String getName() { return name; }
    public String getContactDetails() { return contactDetails; }
}

/**
 * Represents an administrator with special privileges.
 *  This class demonstrates the principle of Inheritance. [cite: 43, 47]
 */
class Admin extends User {
    public Admin(int userId, String name, String contactDetails) {
        // Calls the constructor of the parent class (User)
        super(userId, name, contactDetails);
    }
}