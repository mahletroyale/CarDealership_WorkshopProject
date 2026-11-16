
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class UserInterface {

    private Dealership dealership;
    private Scanner scanner = new Scanner(System.in);

    public UserInterface() {

    }

    public void display() {

        init();

        boolean running = true;

        while (running) {

            System.out.println();
            System.out.println("WELCOME TO " + dealership.getName());
            System.out.println("================================");
            System.out.println("1 - Find vehicles by price");
            System.out.println("2 - Find vehicles by make/model");
            System.out.println("3 - Find vehicles by year");
            System.out.println("4 - Find vehicles by color");
            System.out.println("5 - Find vehicles by mileage");
            System.out.println("6 - Find vehicles by type");
            System.out.println("7 - List all vehicles");
            System.out.println("8 - Add a vehicle");
            System.out.println("9 - Remove a vehicle");
            System.out.println("99 - Quit");
            System.out.println("================================");

            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {

                case "1":
                    processGetByPriceRequest();
                    break;

                case "2":
                    processGetByMakeModelRequest();
                    break;

                case "3":
                    processGetByYearRequest();
                    break;

                case "4":
                    processGetByColorRequest();
                    break;

                case "5":
                    processGetByMileageRequest();
                    break;

                case "6":
                    processGetByVehicleTypeRequest();
                    break;

                case "7":
                    processGetAllVehiclesRequest();
                    break;

                case "8":
                    processAddVehicleRequest();
                    break;

                case "9":
                    processRemoveVehicleRequest();
                    break;

                case "99":
                    System.out.println("Thank you for visiting!");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void init() {

        DealershipFileManager fileManager =
                new DealershipFileManager();

        dealership = fileManager.getDealership();
    }

    private void displayVehicles(List<Vehicle> vehicles) {

        if (vehicles == null || vehicles.isEmpty()) {
            System.out.println("No vehicles found.");
            return;
        }

        System.out.println();
        System.out.println("AVAILABLE VEHICLES");
        System.out.println("==============================");

        for (Vehicle vehicle : vehicles) {

            System.out.println("VIN: " + vehicle.getVin());
            System.out.println("Year: " + vehicle.getYear());
            System.out.println("Make: " + vehicle.getMake());
            System.out.println("Model: " + vehicle.getModel());
            System.out.println("Type: " + vehicle.getVehicleType());
            System.out.println("Color: " + vehicle.getColor());
            System.out.println("Mileage: " + vehicle.getOdometer());
            System.out.println("Price: $" + vehicle.getPrice());
            System.out.println("------------------------------");
        }
    }

    private void processGetByPriceRequest() {

        System.out.println("\nSEARCH BY PRICE");

        BigDecimal min = readPrice("Enter minimum price: ");
        BigDecimal max = readPrice("Enter maximum price: ");

        List<Vehicle> vehicles =
                dealership.getVehiclesByPrice(min, max);

        displayVehicles(vehicles);
    }

    private void processGetByMakeModelRequest() {

        System.out.println("\nSEARCH BY MAKE AND MODEL");

        System.out.print("Enter make: ");
        String make = scanner.nextLine();

        System.out.print("Enter model: ");
        String model = scanner.nextLine();

        List<Vehicle> vehicles =
                dealership.getVehiclesByMakeModel(make, model);

        displayVehicles(vehicles);
    }

    private void processGetByYearRequest() {

        System.out.println("\nSEARCH BY YEAR");

        int min = readInt("Enter minimum year: ");
        int max = readInt("Enter maximum year: ");

        List<Vehicle> vehicles =
                dealership.getVehiclesByYear(min, max);

        displayVehicles(vehicles);
    }

    private void processGetByColorRequest() {

        System.out.println("\nSEARCH BY COLOR");

        System.out.print("Enter color: ");
        String color = scanner.nextLine();

        List<Vehicle> vehicles =
                dealership.getVehiclesByColor(color);

        displayVehicles(vehicles);
    }

    private void processGetByMileageRequest() {

        System.out.println("\nSEARCH BY MILEAGE");

        int min = readInt("Enter minimum mileage: ");
        int max = readInt("Enter maximum mileage: ");

        List<Vehicle> vehicles =
                dealership.getVehiclesByMileage(min, max);

        displayVehicles(vehicles);
    }

    private void processGetByVehicleTypeRequest() {

        System.out.println("\nSEARCH BY VEHICLE TYPE");

        System.out.print("Enter type (car, truck, SUV, van): ");
        String vehicleType = scanner.nextLine();

        List<Vehicle> vehicles =
                dealership.getVehiclesByType(vehicleType);

        displayVehicles(vehicles);
    }

    private void processGetAllVehiclesRequest() {

        List<Vehicle> vehicles = dealership.getAllVehicles();

        displayVehicles(vehicles);
    }

    // Option 8 - Add a vehicle
    private void processAddVehicleRequest() {

        System.out.println("\nADD A VEHICLE");
        System.out.println("==============================");

        int vin = readInt("Enter VIN: ");

        // Check whether this VIN already exists
        for (Vehicle existingVehicle : dealership.getAllVehicles()) {

            if (existingVehicle.getVin() == vin) {
                System.out.println("A vehicle with this VIN already exists.");
                return;
            }
        }

        int year = readInt("Enter year: ");

        System.out.print("Enter make: ");
        String make = scanner.nextLine();

        System.out.print("Enter model: ");
        String model = scanner.nextLine();

        System.out.print("Enter vehicle type (car, truck, SUV, van): ");
        String vehicleType = scanner.nextLine();

        System.out.print("Enter color: ");
        String color = scanner.nextLine();

        int odometer = readInt("Enter mileage: ");

        BigDecimal price = readPrice("Enter price: ");

        // Create a new Vehicle object
        Vehicle vehicle = new Vehicle(
                vin,
                year,
                make,
                model,
                vehicleType,
                color,
                odometer,
                price
        );

        // Add vehicle to the inventory
        dealership.addVehicle(vehicle);

        // Save the updated inventory to the CSV file
        DealershipFileManager fileManager =
                new DealershipFileManager();

        fileManager.saveDealership(dealership);

        System.out.println();
        System.out.println("Vehicle added to inventory!");
    }

    // Option 9 - Remove a vehicle
    private void processRemoveVehicleRequest() {

        System.out.println("\nREMOVE A VEHICLE");
        System.out.println("==============================");

        int vin = readInt("Enter VIN of vehicle to remove: ");

        Vehicle vehicleToRemove = null;

        // Search for the vehicle
        for (Vehicle vehicle : dealership.getAllVehicles()) {

            if (vehicle.getVin() == vin) {
                vehicleToRemove = vehicle;
                break;
            }
        }

        if (vehicleToRemove == null) {
            System.out.println("Vehicle not found.");
            return;
        }

        // Remove the vehicle
        dealership.removeVehicle(vehicleToRemove);

        // Check that the removal succeeded
        if (dealership.getAllVehicles().contains(vehicleToRemove)) {
            System.out.println("Removal is not yet implemented in Dealership.");
            return;
        }

        // Save changes to the CSV file
        DealershipFileManager fileManager =
                new DealershipFileManager();

        fileManager.saveDealership(dealership);

        System.out.println("Vehicle removed from inventory!");
    }

    // Helper method to safely read integer input
    private int readInt(String message) {

        while (true) {

            System.out.print(message);

            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    // Helper method to safely read prices
    private BigDecimal readPrice(String message) {

        while (true) {

            System.out.print(message);

            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid price.");
            }
        }
    }
}
