import java.util.*;

// Reservation Class (Represents booking intent)
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

    public void displayRequest() {
        System.out.println("Guest: " + guestName + " | Requested Room: " + roomType);
    }
}

// Booking Request Queue (FIFO)
class BookingRequestQueue {
    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    // Add booking request
    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
        System.out.println("Request added for " + reservation.getGuestName());
    }

    // View all queued requests (without removing)
    public void viewRequests() {
        System.out.println("\n===== BOOKING REQUEST QUEUE =====");

        if (queue.isEmpty()) {
            System.out.println("No pending requests.");
            return;
        }

        for (Reservation r : queue) {
            r.displayRequest();
        }
    }

    // Peek next request (FIFO)
    public Reservation peekNextRequest() {
        return queue.peek();
    }

    // Get queue size
    public int getQueueSize() {
        return queue.size();
    }
}

// Main Application
class HotelBookingApp {
    public static void main(String[] args) {

        // Initialize Queue System
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // Simulating Guest Requests (Arrival Order)
        Reservation r1 = new Reservation("Lokesh", "Single Room");
        Reservation r2 = new Reservation("Dhatri", "Suite Room");
        Reservation r3 = new Reservation("Suhani", "Double Room");

        // Add requests to queue (FIFO order preserved)
        bookingQueue.addRequest(r1);
        bookingQueue.addRequest(r2);
        bookingQueue.addRequest(r3);

        // View queued requests
        bookingQueue.viewRequests();

        // Peek next request (should be Lokesh)
        System.out.println("\nNext request to process:");
        Reservation next = bookingQueue.peekNextRequest();
        if (next != null) {
            next.displayRequest();
        }

        System.out.println("\nTotal Requests in Queue: " + bookingQueue.getQueueSize());

        System.out.println("\nNo inventory changes performed at this stage.");
    }
}