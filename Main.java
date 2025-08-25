import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

// -------- Room Class --------
class Room {
    int roomId;
    String category;
    double price;
    boolean isAvailable;

    Room(int roomId, String category, double price) {
        this.roomId = roomId;
        this.category = category;
        this.price = price;
        this.isAvailable = true; // By default, available
    }

    public String toString() {
        return "Room ID: " + roomId + ", Category: " + category + ", Price: " + price + ", Available: " + isAvailable;
    }
}

// -------- Reservation Class --------
class Reservation {
    int reservationId;
    String customerName;
    Room room;
    boolean paymentDone;

    Reservation(int reservationId, String customerName, Room room) {
        this.reservationId = reservationId;
        this.customerName = customerName;
        this.room = room;
        this.paymentDone = false; // Default unpaid
    }

    public String toString() {
        return "Reservation ID: " + reservationId + ", Name: " + customerName +
                ", Room: " + room.category + ", Payment: " + (paymentDone ? "Done" : "Pending");
    }
}

// -------- Hotel Reservation System Class --------
class HotelReservationSystem {
    ArrayList<Room> rooms = new ArrayList<>();
    ArrayList<Reservation> reservations = new ArrayList<>();
    int reservationCounter = 1;

    // Constructor → add rooms initially
    HotelReservationSystem() {
        rooms.add(new Room(101, "Standard", 2000));
        rooms.add(new Room(102, "Deluxe", 3500));
        rooms.add(new Room(103, "Suite", 5000));
        rooms.add(new Room(104, "Standard", 2000));
        rooms.add(new Room(105, "Deluxe", 3500));
    }

    // Show available rooms
    void showAvailableRooms() {
        System.out.println("\n--- Available Rooms ---");
        for (Room r : rooms) {
            if (r.isAvailable) {
                System.out.println(r);
            }
        }
    }

    // Make a reservation
    void makeReservation(String customerName, int roomId) {
        for (Room r : rooms) {
            if (r.roomId == roomId && r.isAvailable) {
                Reservation res = new Reservation(reservationCounter++, customerName, r);
                reservations.add(res);
                r.isAvailable = false;
                System.out.println("✅ Booking successful! " + res);
                saveReservationsToFile();
                return;
            }
        }
        System.out.println("❌ Room not available!");
    }

    // Cancel reservation
    void cancelReservation(int reservationId) {
        for (Reservation res : reservations) {
            if (res.reservationId == reservationId) {
                res.room.isAvailable = true;
                reservations.remove(res);
                System.out.println("✅ Reservation cancelled successfully.");
                saveReservationsToFile();
                return;
            }
        }
        System.out.println("❌ Reservation ID not found!");
    }

    // Make payment
    void makePayment(int reservationId) {
        for (Reservation res : reservations) {
            if (res.reservationId == reservationId) {
                res.paymentDone = true;
                System.out.println("💰 Payment completed for Reservation ID: " + reservationId);
                saveReservationsToFile();
                return;
            }
        }
        System.out.println("❌ Reservation not found!");
    }

    // View reservations
    void viewReservations() {
        System.out.println("\n--- All Reservations ---");
        if (reservations.isEmpty()) {
            System.out.println("No reservations yet.");
        } else {
            for (Reservation res : reservations) {
                System.out.println(res);
            }
        }
    }

    // Save reservations to file
    void saveReservationsToFile() {
        try {
            FileWriter fw = new FileWriter("bookings.txt");
            for (Reservation res : reservations) {
                fw.write(res + "\n");
            }
            fw.close();
        } catch (Exception e) {
            System.out.println("⚠️ Error saving reservations.");
        }
    }
}

// -------- Main Class (User Menu) --------
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        HotelReservationSystem hrs = new HotelReservationSystem();

        while (true) {
            System.out.println("\n===== Hotel Reservation System =====");
            System.out.println("1. Show Available Rooms");
            System.out.println("2. Make Reservation");
            System.out.println("3. Cancel Reservation");
            System.out.println("4. Make Payment");
            System.out.println("5. View Reservations");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    hrs.showAvailableRooms();
                    break;
                case 2:
                    System.out.print("Enter your name: ");
                    sc.nextLine(); // consume newline
                    String name = sc.nextLine();
                    System.out.print("Enter Room ID to book: ");
                    int roomId = sc.nextInt();
                    hrs.makeReservation(name, roomId);
                    break;
                case 3:
                    System.out.print("Enter Reservation ID to cancel: ");
                    int resId = sc.nextInt();
                    hrs.cancelReservation(resId);
                    break;
                case 4:
                    System.out.print("Enter Reservation ID for payment: ");
                    int payId = sc.nextInt();
                    hrs.makePayment(payId);
                    break;
                case 5:
                    hrs.viewReservations();
                    break;
                case 6:
                    System.out.println("👋 Exiting... Thank you!");
                    sc.close();
                    return;
                default:
                    System.out.println("❌ Invalid choice! Try again.");
            }
        }
    }
}