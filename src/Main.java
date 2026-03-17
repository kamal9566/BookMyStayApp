import java.util.*;

// Reservation (Final form used across system)
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

    public String getGuestName() {
        return guestName;
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

// ✅ Booking History (Stores confirmed bookings)
class BookingHistory {

    // List preserves insertion order
    private List<Reservation> history = new ArrayList<>();

    // Add confirmed booking
    public void addReservation(Reservation reservation) {
        history.add(reservation);
        System.out.println("Added to history: " + reservation.getReservationId());
    }

    // Get all reservations (read-only style usage)
    public List<Reservation> getAllReservations() {
        return Collections.unmodifiableList(history);
    }

    // Display full history
    public void displayHistory() {
        System.out.println("\n===== BOOKING HISTORY =====");

        if (history.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (Reservation r : history) {
            r.display();
        }
    }
}

// ✅ Reporting Service
class BookingReportService {

    // Generate summary report
    public void generateSummaryReport(List<Reservation> reservations) {
        System.out.println("\n===== BOOKING SUMMARY REPORT =====");

        Map<String, Integer> roomTypeCount = new HashMap<>();

        // Count bookings per room type
        for (Reservation r : reservations) {
            roomTypeCount.put(
                    r.getRoomType(),
                    roomTypeCount.getOrDefault(r.getRoomType(), 0) + 1
            );
        }

        // Display report
        for (Map.Entry<String, Integer> entry : roomTypeCount.entrySet()) {
            System.out.println(entry.getKey() + " → Total Bookings: " + entry.getValue());
        }

        System.out.println("Total Reservations: " + reservations.size());
    }
}

// Main Application
 class HotelBookingApp {
    public static void main(String[] args) {

        // Simulating confirmed bookings (from Use Case 6)
        Reservation r1 = new Reservation("RES-101", "Lokesh", "Single Room", "SI-12345");
        Reservation r2 = new Reservation("RES-102", "Dhatri", "Suite Room", "SU-54321");
        Reservation r3 = new Reservation("RES-103", "Suhani", "Single Room", "SI-67890");

        // Booking History
        BookingHistory history = new BookingHistory();

        // Add bookings to history
        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);

        // Admin views full history
        history.displayHistory();

        // Reporting Service
        BookingReportService reportService = new BookingReportService();

        // Generate report (read-only)
        reportService.generateSummaryReport(history.getAllReservations());

        System.out.println("\nReporting completed. No data was modified.");
    }
}