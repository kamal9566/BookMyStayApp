import java.util.*;

// Reservation
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private String roomId;

    public Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomId() {
        return roomId;
    }

    public void display() {
        System.out.println("Reservation ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room Type: " + roomType +
                " | Room ID: " + roomId);
    }
}

// Inventory Service
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public void addRoom(String type, int count) {
        inventory.put(type, count);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    public void increment(String type) {
        inventory.put(type, inventory.get(type) + 1);
    }

    public void displayInventory() {
        System.out.println("\n===== INVENTORY STATUS =====");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " → Available: " + entry.getValue());
        }
    }
}

// Booking History (acts as storage)
class BookingHistory {
    private Map<String, Reservation> history = new HashMap<>();

    public void addReservation(Reservation r) {
        history.put(r.getReservationId(), r);
    }

    public Reservation getReservation(String id) {
        return history.get(id);
    }

    public void removeReservation(String id) {
        history.remove(id);
    }

    public boolean exists(String id) {
        return history.containsKey(id);
    }
}

// ✅ Cancellation Service
class CancellationService {

    private RoomInventory inventory;
    private BookingHistory history;

    // Stack for rollback tracking (LIFO)
    private Stack<String> rollbackStack = new Stack<>();

    public CancellationService(RoomInventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
    }

    public void cancelBooking(String reservationId) {

        System.out.println("\nProcessing cancellation for: " + reservationId);

        // Step 1: Validate existence
        if (!history.exists(reservationId)) {
            System.out.println("❌ Cancellation Failed: Reservation not found.");
            return;
        }

        // Step 2: Fetch reservation
        Reservation reservation = history.getReservation(reservationId);

        // Step 3: Push roomId to rollback stack
        rollbackStack.push(reservation.getRoomId());

        // Step 4: Restore inventory
        inventory.increment(reservation.getRoomType());

        // Step 5: Remove from history
        history.removeReservation(reservationId);

        // Step 6: Confirm cancellation
        System.out.println("✅ Cancellation Successful!");
        System.out.println("Released Room ID: " + reservation.getRoomId());
    }

    // Optional: View rollback stack
    public void showRollbackStack() {
        System.out.println("\nRollback Stack (Recent Releases): " + rollbackStack);
    }
}

// Main Application
class HotelBookingApp {
    public static void main(String[] args) {

        // Initialize Inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoom("Single Room", 1);

        // Booking History
        BookingHistory history = new BookingHistory();

        // Simulate confirmed booking
        Reservation r1 = new Reservation("RES-101", "Lokesh", "Single Room", "SI-12345");
        history.addReservation(r1);

        // Inventory after booking (manually reduced)
        // (In real system this happens in booking service)
        System.out.println("Initial Inventory:");
        inventory.displayInventory();

        // Cancellation Service
        CancellationService cancellationService =
                new CancellationService(inventory, history);

        // Perform cancellation
        cancellationService.cancelBooking("RES-101");

        // Try invalid cancellation
        cancellationService.cancelBooking("RES-999");

        // Check inventory after rollback
        inventory.displayInventory();

        // View rollback stack
        cancellationService.showRollbackStack();

        System.out.println("\nSystem state restored successfully.");
    }
}