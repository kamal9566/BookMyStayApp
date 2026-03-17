import java.util.*;

// Reservation (Now includes reservationId)
class Reservation {
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

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// ✅ Add-On Service Class
class AddOnService {
    private String serviceName;
    private double cost;

    public AddOnService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }

    public void displayService() {
        System.out.println(serviceName + " (₹" + cost + ")");
    }
}

// ✅ Add-On Service Manager
class AddOnServiceManager {

    // Map<ReservationID, List of Services>
    private Map<String, List<AddOnService>> serviceMap = new HashMap<>();

    // Add service to reservation
    public void addService(String reservationId, AddOnService service) {
        serviceMap
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);

        System.out.println("Service added to Reservation " + reservationId + ": " + service.getServiceName());
    }

    // View services for a reservation
    public void viewServices(String reservationId) {
        System.out.println("\nServices for Reservation: " + reservationId);

        List<AddOnService> services = serviceMap.get(reservationId);

        if (services == null || services.isEmpty()) {
            System.out.println("No add-on services selected.");
            return;
        }

        for (AddOnService s : services) {
            s.displayService();
        }
    }

    // Calculate total cost of services
    public double calculateTotalCost(String reservationId) {
        List<AddOnService> services = serviceMap.get(reservationId);

        if (services == null) return 0;

        double total = 0;
        for (AddOnService s : services) {
            total += s.getCost();
        }
        return total;
    }
}

// Main Application
 class HotelBookingApp {
    public static void main(String[] args) {

        // Existing reservation (from Use Case 6)
        Reservation reservation = new Reservation("RES-101", "Lokesh", "Suite Room");

        // Add-On Services
        AddOnService breakfast = new AddOnService("Breakfast", 500);
        AddOnService wifi = new AddOnService("WiFi", 200);
        AddOnService spa = new AddOnService("Spa Access", 1500);

        // Service Manager
        AddOnServiceManager serviceManager = new AddOnServiceManager();

        // Guest selects services
        serviceManager.addService(reservation.getReservationId(), breakfast);
        serviceManager.addService(reservation.getReservationId(), wifi);
        serviceManager.addService(reservation.getReservationId(), spa);

        // View selected services
        serviceManager.viewServices(reservation.getReservationId());

        // Calculate additional cost
        double totalCost = serviceManager.calculateTotalCost(reservation.getReservationId());

        System.out.println("\nTotal Add-On Cost: ₹" + totalCost);

        System.out.println("\nCore booking and inventory remain unchanged.");
    }
}