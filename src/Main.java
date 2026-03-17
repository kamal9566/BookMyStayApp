import java.io.*;
import java.util.*;

// Reservation (Serializable)
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }

    public void display() {
        System.out.println(reservationId + " | " + guestName + " | " + roomType);
    }
}

// Inventory (Serializable)
class RoomInventory implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, Integer> inventory = new HashMap<>();

    public void addRoom(String type, int count) {
        inventory.put(type, count);
    }

    public Map<String, Integer> getInventory() {
        return inventory;
    }

    public void display() {
        System.out.println("Inventory: " + inventory);
    }
}

// Booking History (Serializable)
class BookingHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Reservation> reservations = new ArrayList<>();

    public void addReservation(Reservation r) {
        reservations.add(r);
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void display() {
        System.out.println("\nBooking History:");
        for (Reservation r : reservations) {
            r.display();
        }
    }
}

// Wrapper class (entire system state)
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    RoomInventory inventory;
    BookingHistory history;

    public SystemState(RoomInventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
    }
}

// ✅ Persistence Service
class PersistenceService {

    private static final String FILE_NAME = "system_state.dat";

    // Save state
    public static void save(SystemState state) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            oos.writeObject(state);
            System.out.println("\n✅ System state saved successfully.");

        } catch (IOException e) {
            System.out.println("❌ Error saving state: " + e.getMessage());
        }
    }

    // Load state
    public static SystemState load() {
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            System.out.println("\n✅ System state loaded successfully.");
            return (SystemState) ois.readObject();

        } catch (FileNotFoundException e) {
            System.out.println("\n⚠ No previous data found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("\n❌ Error loading state. Starting with safe defaults.");
        }

        // Return safe default state
        return new SystemState(new RoomInventory(), new BookingHistory());
    }
}

// Main Application
class HotelBookingApp {
    public static void main(String[] args) {

        // 🔄 Step 1: Load previous state
        SystemState state = PersistenceService.load();

        RoomInventory inventory = state.inventory;
        BookingHistory history = state.history;

        // If fresh start → initialize data
        if (inventory.getInventory().isEmpty()) {
            inventory.addRoom("Single Room", 2);
            inventory.addRoom("Double Room", 1);
        }

        // Simulate new booking
        Reservation r1 = new Reservation("RES-201", "Lokesh", "Single Room");
        history.addReservation(r1);

        // Display current state
        inventory.display();
        history.display();

        // 💾 Step 2: Save state before shutdown
        PersistenceService.save(new SystemState(inventory, history));

        System.out.println("\nSystem shutdown complete. Restart to verify recovery.");
    }
}