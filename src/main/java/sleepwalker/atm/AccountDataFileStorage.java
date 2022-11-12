package sleepwalker.atm;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

public class AccountDataFileStorage implements AccountDataStorage {

    final static Path FILE_PATH = Path.of("src/main/resources/userData.txt");
    final static String DELIMITER = " ";

    @Override
    public Map<String, Account> readData() {
        Map<String, Account> accounts = new HashMap<>();

        try (Stream<String> lines = Files.lines(FILE_PATH)) {
            lines.forEach(s -> addEntry(s, accounts));
        } catch (Exception e) { // РАСПИШИ ИСКЛЮЧЕНИЯ ЛУЧШЕ
            e.printStackTrace();
        }
        return accounts;
    }

    private void addEntry(String line, Map<String, Account> accounts) {
        String[] currentAccountData = line.split(DELIMITER);
        String cardNumber = currentAccountData[0];
        String PIN = currentAccountData[1];
        BigDecimal balance = new BigDecimal(currentAccountData[2]);
        accounts.put(cardNumber, new Account(cardNumber, PIN, balance));
    }

    @Override
    public void saveData(Session session) {
        try (BufferedWriter output = new BufferedWriter(new FileWriter(FILE_PATH.toFile()))) {
            for (Map.Entry<String, Account> accountEntry : session.getAccountData().entrySet()) {
                String cardNumber = accountEntry.getValue().getCardNumber();
                String PIN = String.valueOf(accountEntry.getValue().getPIN());
                String Balance = accountEntry.getValue().getBalance().toString();
                output.write(cardNumber + DELIMITER + PIN + DELIMITER + Balance + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
