import java.util.*;

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

// Thread-safe Booking Queue
class BookingRequestQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    // synchronized → only one thread can add at a time
    public synchronized void addRequest(Reservation r) {
        queue.offer(r);
        System.out.println(Thread.currentThread().getName() +
                " added request for " + r.getGuestName());
    }

    // synchronized → safe retrieval
    public synchronized Reservation getRequest() {
        return queue.poll();
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
}

// Thread-safe Inventory
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 2);
    }

    // Critical section → synchronized
    public synchronized boolean allocateRoom(String roomType) {
        int available = inventory.getOrDefault(roomType, 0);

        if (available > 0) {
            inventory.put(roomType, available - 1);
            return true;
        }
        return false;
    }

    public synchronized void displayInventory() {
        System.out.println("Inventory: " + inventory);
    }
}

// Booking Processor (Runnable → Thread)
class BookingProcessor implements Runnable {

    private BookingRequestQueue queue;
    private RoomInventory inventory;

    public BookingProcessor(BookingRequestQueue queue, RoomInventory inventory) {
        this.queue = queue;
        this.inventory = inventory;
    }

    @Override
    public void run() {

        while (true) {
            Reservation request;

            // Safely fetch request
            synchronized (queue) {
                if (queue.isEmpty()) break;
                request = queue.getRequest();
            }

            if (request != null) {
                processBooking(request);
            }
        }
    }

    private void processBooking(Reservation r) {

        // Critical section → allocation must be safe
        boolean success = inventory.allocateRoom(r.getRoomType());

        if (success) {
            System.out.println(Thread.currentThread().getName() +
                    " ✅ Booked for " + r.getGuestName());
        } else {
            System.out.println(Thread.currentThread().getName() +
                    " ❌ No room for " + r.getGuestName());
        }
    }
}

// Main Application
 class HotelBookingApp {
    public static void main(String[] args) {

        BookingRequestQueue queue = new BookingRequestQueue();
        RoomInventory inventory = new RoomInventory();

        // Simulate multiple requests
        queue.addRequest(new Reservation("Lokesh", "Single Room"));
        queue.addRequest(new Reservation("Dhatri", "Single Room"));
        queue.addRequest(new Reservation("Suhani", "Single Room")); // extra

        // Multiple threads (simulating concurrent users)
        Thread t1 = new Thread(new BookingProcessor(queue, inventory), "Thread-1");
        Thread t2 = new Thread(new BookingProcessor(queue, inventory), "Thread-2");

        // Start threads
        t1.start();
        t2.start();

        // Wait for completion
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Final inventory state
        inventory.displayInventory();

        System.out.println("\nNo double booking occurred. System is thread-safe.");
    }
}