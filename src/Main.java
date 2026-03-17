import java.util.*;

// ✅ Custom Exception
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Reservation
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// Inventory Service
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public void addRoom(String type, int count) {
        inventory.put(type, count);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, -1); // -1 means invalid type
    }

    public void decrement(String type) throws InvalidBookingException {
        int current = getAvailability(type);

        if (current <= 0) {
            throw new InvalidBookingException("Cannot decrement. No rooms available for " + type);
        }

        inventory.put(type, current - 1);
    }

    public boolean isValidRoomType(String type) {
        return inventory.containsKey(type);
    }
}

// ✅ Validator Class
class BookingValidator {

    public static void validate(Reservation reservation, RoomInventory inventory)
            throws InvalidBookingException {

        // Validate guest name
        if (reservation.getGuestName() == null || reservation.getGuestName().trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        // Validate room type
        if (!inventory.isValidRoomType(reservation.getRoomType())) {
            throw new InvalidBookingException("Invalid room type: " + reservation.getRoomType());
        }

        // Validate availability
        if (inventory.getAvailability(reservation.getRoomType()) <= 0) {
            throw new InvalidBookingException("No rooms available for " + reservation.getRoomType());
        }
    }
}

// Booking Service
class BookingService {
    private RoomInventory inventory;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void processBooking(Reservation reservation) {
        try {
            // ✅ Step 1: Validate (Fail-Fast)
            BookingValidator.validate(reservation, inventory);

            // ✅ Step 2: Allocate (only if valid)
            inventory.decrement(reservation.getRoomType());

            System.out.println("✅ Booking successful for " + reservation.getGuestName() +
                    " (" + reservation.getRoomType() + ")");

        } catch (InvalidBookingException e) {
            // ✅ Graceful failure
            System.out.println("❌ Booking Failed: " + e.getMessage());
        }
    }
}

// Main Application
 class HotelBookingApp {
    public static void main(String[] args) {

        // Initialize Inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoom("Single Room", 1);
        inventory.addRoom("Double Room", 0); // No availability

        BookingService bookingService = new BookingService(inventory);

        // Test Cases

        // ✅ Valid booking
        bookingService.processBooking(new Reservation("Lokesh", "Single Room"));

        // ❌ Invalid room type
        bookingService.processBooking(new Reservation("Dhatri", "Luxury Room"));

        // ❌ No availability
        bookingService.processBooking(new Reservation("Suhani", "Double Room"));

        // ❌ Invalid guest name
        bookingService.processBooking(new Reservation("", "Single Room"));

        System.out.println("\nSystem remains stable after handling errors.");
    }
}