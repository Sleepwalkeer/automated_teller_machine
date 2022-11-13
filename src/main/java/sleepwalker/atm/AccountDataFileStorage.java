package sleepwalker.atm;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

public class AccountDataFileStorage implements AccountDataStorage {

    final static Path FILE_PATH = Path.of("src/main/resources/userData.txt");
    final static String SPACE = " ";

    @Override
    public Map<String, Account> readData() {
        Map<String, Account> accounts = new HashMap<>();

        try (Stream<String> lines = Files.lines(FILE_PATH)) {
            lines.forEach(s -> addEntry(s, accounts));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accounts;

    }

    private void addEntry(String line, Map<String, Account> accounts) {
        String[] currentAccountData = line.split(SPACE);
        String cardNumber = currentAccountData[0];
        String PIN = currentAccountData[1];
        BigDecimal balance = new BigDecimal(currentAccountData[2]);
        boolean blocked = Boolean.parseBoolean(currentAccountData[3]);
        Date unblockDate = new Date(Long.parseLong(currentAccountData[4]));
        accounts.put(cardNumber, new Account(cardNumber, PIN, balance, blocked, unblockDate));
    }

    @Override
    public void saveData(Session session) {
        try (BufferedWriter output = new BufferedWriter(new FileWriter(FILE_PATH.toFile()))) {
            for (Map.Entry<String, Account> accountEntry : session.getAccountData().entrySet()) {
                String cardNumber = accountEntry.getValue().getCardNumber();
                String PIN = String.valueOf(accountEntry.getValue().getPIN());
                String Balance = accountEntry.getValue().getBalance().toString();
                boolean blocked = accountEntry.getValue().isBlocked();
                long unblockDate = accountEntry.getValue().getUnblockDate().getTime();
                output.write(cardNumber + SPACE + PIN + SPACE + Balance + SPACE + blocked + SPACE + unblockDate + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
