package sleepwalker.atm;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class UserDataFile implements UserDataStorage {

    final static String FILE_PATH = "src/main/resources/userData.txt";

    @Override
    public List<User> readUserData() {
        List<User> users = new ArrayList<>();

        try (BufferedReader input = new BufferedReader(new FileReader(FILE_PATH))) {
            String line = input.readLine();
            while (line != null) {
                String[] currentUserData = line.split(" ");
                String cardNumber = currentUserData[0];
                int PIN = Integer.parseInt(currentUserData[1]);
                BigDecimal balance = new BigDecimal(currentUserData[2]);
                users.add(new User(cardNumber, PIN, balance));
                line = input.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }

    @Override
    public void saveUserData(List<User> userData) {
        try (BufferedWriter output = new BufferedWriter(new FileWriter(FILE_PATH))) {


            for (User user : userData) {
                String cardNumber = user.getCardNumber();
                String PIN = String.valueOf(user.getPIN());
                String Balance = user.getBalance().toString();
                output.write(cardNumber + " " + PIN + " " + Balance + "\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }}
