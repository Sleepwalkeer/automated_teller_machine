package sleepwalker.atm;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Scanner;

public class ConsoleMenu implements Menu {
    private ATM atm;
    private Session currentSession;
    private final static Scanner scanner = new Scanner(System.in);
    private final static DecimalFormat MONEY = new DecimalFormat("$#,###.00");
    private final static BigDecimal INCORRECT_DECIMAL_INPUT = BigDecimal.ZERO;
    private final static String INCORRECT_AMOUNT_INPUT = "Error! Incorrect amount";

    @Override
    public void start(ATM atm, Session session) {
        this.atm = atm;
        this.currentSession = session;
        authorize();
    }
    private void authorize() {
        while (!currentSession.isLoggedIn()) {
            System.out.println("Welcome to the ATM app.");
            System.out.print("Please, enter your card number:");
            String cardNumberEntered = scanner.nextLine();
            if (currentSession.cardNumberValid(cardNumberEntered)) {
                System.out.print("Please, enter your PIN code:");
                String pinCodeEntered = scanner.nextLine();
                if (currentSession.logIn(cardNumberEntered, pinCodeEntered)) {
                    showHomeScreen();
                } else {
                    System.out.println("The PIN code you have entered is invalid.");
                    System.out.println();
                }
            } else {
                System.out.println("The card number you have entered is invalid.");
                System.out.println();
            }
        }
    }
    private void showHomeScreen() {
        System.out.println();
        System.out.println("You have been successfully logged in");
        while (true) {
            showActionsAvailable();
            switch (readNextAction()) {
                case CHECK_BALANCE -> checkBalanceActionHandler();
                case WITHDRAW -> withdrawActionHandler();
                case DEPOSIT -> depositActionHandler();
                case LOGOUT -> logOutActionHandler();
                case EXIT -> exitActionHandler();
                default -> System.out.println("Incorrect action. Please, try again.");
            }
        }
    }

    private void showActionsAvailable() {
        System.out.println();
        System.out.println("Please, enter the number corresponding to the desired action:");
        System.out.println("1 - Balance Enquiry");
        System.out.println("2 - Cash Withdrawal");
        System.out.println("3 - Cash Deposit");
        System.out.println("4 - Log Out");
        System.out.println("5 - Exit ");
    }
    private void checkBalanceActionHandler() {
        System.out.println("Your balance = " + MONEY.format(atm.getBalance(currentSession.getCurrentAccount())));
    }
    private void depositActionHandler() {
        System.out.println("Please, enter the amount to deposit");
        BigDecimal depositAmount = readBigDecimal();
        if (depositAmount.compareTo(INCORRECT_DECIMAL_INPUT) > 0) {
            atm.deposit(currentSession.getCurrentAccount(), depositAmount);
            System.out.println(MONEY.format(depositAmount) + " has been successfully deposited");
        } else {
            System.out.println(INCORRECT_AMOUNT_INPUT);
        }
    }

    private void withdrawActionHandler() {
        System.out.println("Please, enter the amount to withdraw");

        BigDecimal withdrawalAmount = readBigDecimal();
        if (withdrawalAmount.compareTo(INCORRECT_DECIMAL_INPUT) > 0) {
            atm.withdraw(currentSession.getCurrentAccount(), withdrawalAmount);
            System.out.println(MONEY.format(withdrawalAmount) + " has been successfully withdrawn");
        } else {
            System.out.println(INCORRECT_AMOUNT_INPUT);
        }
    }

    private void logOutActionHandler() {
        currentSession.logOut();
        authorize();
    }

    private void exitActionHandler() {
        currentSession.exit();
    }

    private BankAction readNextAction() {
        BankAction action;
        try {
            int actionChosen = Integer.parseInt(scanner.nextLine());
            action = BankAction.values()[actionChosen];
        } catch (Exception e) {
            action = BankAction.ERROR;
        }
        return action;
    }

    private BigDecimal readBigDecimal() {
        try {
            return new BigDecimal(scanner.nextLine().replace(',', '.'));
        } catch (NumberFormatException e) {
            return INCORRECT_DECIMAL_INPUT;
        }
    }

}
