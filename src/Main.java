import java.util.HashMap;
import java.util.Map;

// Abstract Class (Same as Use Case 2)
abstract class Room {
    private String type;
    private int beds;
    private double size;
    private double price;

    public Room(String type, int beds, double size, double price) {
        this.type = type;
        this.beds = beds;
        this.size = size;
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void displayDetails() {
        System.out.println("Room Type: " + type);
        System.out.println("Beds: " + beds);
        System.out.println("Size: " + size + " sq.ft");
        System.out.println("Price: ₹" + price);
    }
}

// Concrete Classes
class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 150.0, 2000.0);
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 250.0, 3500.0);
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 500.0, 7000.0);
    }
}

// ✅ Centralized Inventory Class
class RoomInventory {
    private Map<String, Integer> inventory;

    // Constructor initializes inventory
    public RoomInventory() {
        inventory = new HashMap<>();
    }

    // Register room type with availability
    public void addRoom(String roomType, int count) {
        inventory.put(roomType, count);
    }

    // Get availability
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Update availability (controlled)
    public void updateAvailability(String roomType, int newCount) {
        if (inventory.containsKey(roomType)) {
            inventory.put(roomType, newCount);
        } else {
            System.out.println("Room type not found!");
        }
    }

    // Display entire inventory
    public void displayInventory() {
        System.out.println("\n===== ROOM INVENTORY =====");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " → Available: " + entry.getValue());
        }
    }
}

// Main Application
 class HotelBookingApp {
    public static void main(String[] args) {

        // Room objects (Domain Model)
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Initialize Inventory (Single Source of Truth)
        RoomInventory inventory = new RoomInventory();

        inventory.addRoom(single.getType(), 10);
        inventory.addRoom(doubleRoom.getType(), 5);
        inventory.addRoom(suite.getType(), 2);

        // Display Room Details + Availability
        System.out.println("===== HOTEL ROOM DETAILS =====\n");

        single.displayDetails();
        System.out.println("Available: " + inventory.getAvailability(single.getType()));
        System.out.println("-----------------------------");

        doubleRoom.displayDetails();
        System.out.println("Available: " + inventory.getAvailability(doubleRoom.getType()));
        System.out.println("-----------------------------");

        suite.displayDetails();
        System.out.println("Available: " + inventory.getAvailability(suite.getType()));
        System.out.println("-----------------------------");

        // Update Example
        System.out.println("\nUpdating Single Room Availability...\n");
        inventory.updateAvailability("Single Room", 8);

        // Display Updated Inventory
        inventory.displayInventory();

        System.out.println("\nApplication Terminated.");
    }
}