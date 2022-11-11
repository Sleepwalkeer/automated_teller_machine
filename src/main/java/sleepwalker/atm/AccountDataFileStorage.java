package sleepwalker.atm;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AccountDataFileStorage implements AccountDataStorage {

    final static String FILE_PATH = "src/main/resources/userData.txt";

    @Override
    public List<Account> readAccountData() {
        List<Account> accounts = new ArrayList<>();

        try (BufferedReader input = new BufferedReader(new FileReader(FILE_PATH))) {
            String line = input.readLine();
            while (line != null) {
                String[] currentAccountData = line.split(" ");
                String cardNumber = currentAccountData[0];
                int PIN = Integer.parseInt(currentAccountData[1]);
                BigDecimal balance = new BigDecimal(currentAccountData[2]);
                accounts.add(new Account(cardNumber, PIN, balance));
                line = input.readLine();
            }
        } catch (Exception e) { // РАСПИШИ ИСКЛЮЧЕНИЯ ЛУЧШЕ
            e.printStackTrace();
        }

        return accounts;
    }

    @Override
    public void saveAccountData(List<Account> accountList) {
        try (BufferedWriter output = new BufferedWriter(new FileWriter(FILE_PATH))) {


            for (Account account : accountList) {
                String cardNumber = account.getCardNumber();
                String PIN = String.valueOf(account.getPIN());
                String Balance = account.getBalance().toString();
                output.write(cardNumber + " " + PIN + " " + Balance + "\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }}
