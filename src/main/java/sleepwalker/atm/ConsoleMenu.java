package sleepwalker.atm;

import sleepwalker.exception.*;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Scanner;

public class ConsoleMenu implements Menu {
    private final static Scanner scanner = new Scanner(System.in);
    private final static DecimalFormat MONEY = new DecimalFormat("$#,###.00");
    private final static String INCORRECT_AMOUNT_INPUT = "Incorrect amount entered.";
    private ATM atm;
    private Session currentSession;

    @Override
    public void start(ATM atm, Session session) {
        this.atm = atm;
        this.currentSession = session;
        authorize();
    }

    private void authorize() {
        while (!currentSession.isLoggedIn()) {
            System.out.println("Welcome to the ATM app.");
            try {
                boolean loggedIn = tryLogIn();
                if (loggedIn) {
                    showHomeScreen();
                }
            } catch (AccountBlockedException e) {
                long hoursToUnban = e.timeToUnban() / Session.HOUR_IN_MILLS;
                System.out.println("This account has been blocked and will be unblocked in " + hoursToUnban + " hours.");
                System.out.println();

            } catch (AccountNotFoundException e) {
                System.out.println("The card number you have entered is invalid.");
                System.out.println();
            }
            currentSession.reset();
        }
    }

    private boolean tryLogIn() throws AccountBlockedException, AccountNotFoundException {
        System.out.println("Please, enter your card number:");
        String cardNumberEntered = scanner.nextLine();
        currentSession.validateCardNumber(cardNumberEntered);

        while (currentSession.getPinCodeAttempts() < Session.MAX_PIN_CODE_ENTER_ATTEMPTS) {
            System.out.println("Please, enter your PIN code:");
            String pinCodeEntered = scanner.nextLine();
            if (currentSession.accoundDataConfirmed(cardNumberEntered, pinCodeEntered)) {
                return true;
            } else {
                currentSession.increasePinCodeAttempts();
                byte attemptsLeft = (byte) (Session.MAX_PIN_CODE_ENTER_ATTEMPTS - currentSession.getPinCodeAttempts());
                System.out.println("Incorrect PIN code, you have " + attemptsLeft + " more attempts left.");
            }
        }
        blockAccount(cardNumberEntered);
        return false;
    }

    private void blockAccount(String cardNumber) {
        currentSession.blockAccount(cardNumber);
        System.out.println("Your account has been blocked for 24 hours.");
        System.out.println();
    }

    private void showHomeScreen() {
        System.out.println();
        System.out.println("You have been successfully logged in.");
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
        System.out.println("5 - Exit");
    }

    private void checkBalanceActionHandler() {
        Account currentAccount = currentSession.getCurrentAccount();
        System.out.println("Your balance = " + MONEY.format(atm.getBalance(currentAccount)) + " .");
    }

    private void depositActionHandler() {
        Account currentAccount = currentSession.getCurrentAccount();
        System.out.println("Please, enter deposit amount:");
        try {
            BigDecimal depositAmount = readBigDecimal();
            atm.deposit(currentAccount, depositAmount);
            System.out.println(MONEY.format(depositAmount) + " has been successfully deposited to your account.");
        } catch (IncorrectAmountEnteredException e) {
            System.out.println(INCORRECT_AMOUNT_INPUT);
        } catch (DepositLimitExceededException e) {
            System.out.println("Deposit amount can not exceed $1,000,000.00 .");
        }
    }

    private void withdrawActionHandler() {
        Account currentAccount = currentSession.getCurrentAccount();
        System.out.println("Please, enter withdrawal amount:");
        try {
            BigDecimal withdrawalAmount = readBigDecimal();
            atm.withdraw(currentAccount, withdrawalAmount);
            System.out.println(MONEY.format(withdrawalAmount) + " has been successfully withdrawn from your account.");
        } catch (IncorrectAmountEnteredException e) {
            System.out.println(INCORRECT_AMOUNT_INPUT);
        } catch (InsufficientBalanceException e) {
            System.out.println("You have insufficient funds.");
        } catch (atmNotEnoughMoneyException e) {
            System.out.println("There is not enough money in the ATM to fulfill your request.");
        }
    }

    private void logOutActionHandler() {
        currentSession.logOut();
        authorize();
    }

    private void exitActionHandler() {
        System.out.println("thank you for using the services of our bank.");
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

    private BigDecimal readBigDecimal() throws IncorrectAmountEnteredException {
        try {
            return new BigDecimal(scanner.nextLine().replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new IncorrectAmountEnteredException();
        }
    }
}