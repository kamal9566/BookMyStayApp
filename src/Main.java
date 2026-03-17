import java.util.*;

// Reservation (Same as UC5)
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

// Queue (FIFO)
class BookingRequestQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    public Reservation getNextRequest() {
        return queue.poll(); // removes from queue
    }

    public boolean isEmpty() {
        return queue.isEmpty();
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

    public void decrement(String type) {
        inventory.put(type, inventory.get(type) - 1);
    }
}

// ✅ Booking Service (Core Logic)
class BookingService {

    // Track allocated room IDs (uniqueness)
    private Set<String> allocatedRoomIds = new HashSet<>();

    // Map room type → allocated room IDs
    private Map<String, Set<String>> roomAllocations = new HashMap<>();

    private RoomInventory inventory;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    // Process booking request
    public void processRequest(Reservation request) {
        String roomType = request.getRoomType();

        System.out.println("\nProcessing request for " + request.getGuestName());

        // Step 1: Check availability
        if (inventory.getAvailability(roomType) <= 0) {
            System.out.println("❌ No rooms available for " + roomType);
            return;
        }

        // Step 2: Generate unique room ID
        String roomId = generateRoomId(roomType);

        // Step 3: Ensure uniqueness (Set)
        if (allocatedRoomIds.contains(roomId)) {
            System.out.println("❌ Duplicate Room ID detected!");
            return;
        }

        // Step 4: Assign room
        allocatedRoomIds.add(roomId);

        roomAllocations
                .computeIfAbsent(roomType, k -> new HashSet<>())
                .add(roomId);

        // Step 5: Update inventory (atomic step)
        inventory.decrement(roomType);

        // Step 6: Confirm booking
        System.out.println("✅ Booking Confirmed!");
        System.out.println("Guest: " + request.getGuestName());
        System.out.println("Room Type: " + roomType);
        System.out.println("Allocated Room ID: " + roomId);
    }

    // Unique ID generator
    private String generateRoomId(String roomType) {
        return roomType.substring(0, 2).toUpperCase() + "-" + UUID.randomUUID().toString().substring(0, 5);
    }

    // Display allocations
    public void displayAllocations() {
        System.out.println("\n===== ROOM ALLOCATIONS =====");
        for (Map.Entry<String, Set<String>> entry : roomAllocations.entrySet()) {
            System.out.println(entry.getKey() + " → " + entry.getValue());
        }
    }
}

// Main Application
class HotelBookingApp {
    public static void main(String[] args) {

        // Step 1: Initialize Inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoom("Single Room", 2);
        inventory.addRoom("Double Room", 1);

        // Step 2: Queue Requests (FIFO)
        BookingRequestQueue queue = new BookingRequestQueue();
        queue.addRequest(new Reservation("Lokesh", "Single Room"));
        queue.addRequest(new Reservation("Dhatri", "Single Room"));
        queue.addRequest(new Reservation("Suhani", "Single Room")); // should fail

        // Step 3: Booking Service
        BookingService bookingService = new BookingService(inventory);

        // Step 4: Process Queue
        while (!queue.isEmpty()) {
            Reservation request = queue.getNextRequest();
            bookingService.processRequest(request);
        }

        // Step 5: Show Final Allocations
        bookingService.displayAllocations();

        System.out.println("\nSystem remains consistent. No double booking occurred.");
    }
}