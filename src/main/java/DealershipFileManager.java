import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;

public class DealershipFileManager {

    private static final Path INVENTORY_FILE = Path.of("inventory.csv");

    public Dealership getDealership() {

        try (BufferedReader reader = Files.newBufferedReader(INVENTORY_FILE)) {

            String dealershipLine = reader.readLine();

            if (dealershipLine == null) {
                throw new IllegalArgumentException("inventory.csv is empty.");
            }

            String[] dealershipInfo = dealershipLine.split("\\|", -1);

            if (dealershipInfo.length != 3) {
                throw new IllegalArgumentException(
                        "Invalid dealership header in inventory.csv.");
            }

            Dealership dealership = new Dealership(
                    dealershipInfo[0],
                    dealershipInfo[1],
                    dealershipInfo[2]
            );

            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {

                lineNumber++;

                if (line.isBlank()) {
                    continue;
                }

                String[] details = line.split("\\|", -1);

                if (details.length != 8) {
                    throw new IllegalArgumentException(
                            "Expected 8 vehicle fields on line " + lineNumber);
                }

                Vehicle vehicle = new Vehicle(Integer.parseInt(details[0]), Integer.parseInt(details[1]),
                        details[2],
                        details[3],
                        details[4],
                        details[5],
                        Integer.parseInt(details[6]),
                        new BigDecimal(details[7])
                );

                dealership.addVehicle(vehicle);
            }

            return dealership;

        } catch (IOException e) {

            throw new IllegalStateException(
                    "Could not read " + INVENTORY_FILE.toAbsolutePath(), e);
        }
    }

    public void saveDealership(Dealership dealership) {

    }
}