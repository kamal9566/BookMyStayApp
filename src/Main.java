import java.util.*;

// Abstract Room Class
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

    public double getPrice() {
        return price;
    }

    public void displayDetails() {
        System.out.println("Room Type: " + type);
        System.out.println("Beds: " + beds);
        System.out.println("Size: " + size + " sq.ft");
        System.out.println("Price: ₹" + price);
    }
}

// Concrete Room Classes
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

// Centralized Inventory (Same as Use Case 3)
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public void addRoom(String roomType, int count) {
        inventory.put(roomType, count);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Read-only view (important for safety)
    public Map<String, Integer> getAllAvailability() {
        return Collections.unmodifiableMap(inventory);
    }
}

// ✅ Search Service (Read-Only Logic)
class SearchService {

    public void searchAvailableRooms(List<Room> rooms, RoomInventory inventory) {
        System.out.println("\n===== AVAILABLE ROOMS =====\n");

        for (Room room : rooms) {
            int available = inventory.getAvailability(room.getType());

            // Defensive check: show only available rooms
            if (available > 0) {
                room.displayDetails();
                System.out.println("Available: " + available);
                System.out.println("-----------------------------");
            }
        }
    }
}

// Main Application
 class HotelBookingApp {
    public static void main(String[] args) {

        // Domain Objects
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        List<Room> rooms = Arrays.asList(single, doubleRoom, suite);

        // Inventory Initialization
        RoomInventory inventory = new RoomInventory();
        inventory.addRoom(single.getType(), 10);
        inventory.addRoom(doubleRoom.getType(), 0); // Not available
        inventory.addRoom(suite.getType(), 2);

        // Search Service
        SearchService searchService = new SearchService();

        // Guest performs search (Read-only)
        searchService.searchAvailableRooms(rooms, inventory);

        System.out.println("\nSystem state remains unchanged.");
    }
}