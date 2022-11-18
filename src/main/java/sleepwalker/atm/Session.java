package sleepwalker.atm;

import sleepwalker.exception.*;

import java.util.Date;
import java.util.Map;

public class Session {
    public final static byte MAX_PIN_CODE_ENTER_ATTEMPTS = 3;
    public final static long HOUR_IN_MILLS = 3_600_000;
    private final static long BLOCK_TIME_IN_MILLS = 86_400_000;
    private final AccountDataStorage dataStorage;
    private final Map<String, Account> accounts;
    private byte pinCodeAttempts;
    private Account currentAccount;
    private boolean loggedIn = false;

    public Session(AccountDataStorage dataStorage) {
        pinCodeAttempts = 0;
        this.dataStorage = dataStorage;
        this.accounts = dataStorage.readData();
    }

    public byte getPinCodeAttempts() {
        return pinCodeAttempts;
    }

    public void increasePinCodeAttempts() {
        pinCodeAttempts++;
    }

    public Map<String, Account> getAccounts() {
        return accounts;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public Account getCurrentAccount() {
        return currentAccount;
    }

    public void saveData(Session session) { //ТУТ НАДО БУДЕТ МЕНЯТЬ
        accounts.put(currentAccount.getCardNumber(), currentAccount);
        dataStorage.saveData(session);
    }


    public boolean accoundDataConfirmed(String cardNumber, String pinCode) {
        for (Map.Entry<String, Account> accountEntry : accounts.entrySet()) {
            String currentAccCardNumber = accountEntry.getKey();
            if (cardNumber.equals(currentAccCardNumber) && pinCode.equals(accountEntry.getValue().getPIN())) {
                currentAccount = accountEntry.getValue();
                loggedIn = true;
                return true;
            }
        }
        return false;
    }

    public void logOut() {
        saveData(this);
        reset();
    }


    public void validateCardNumber(String cardNumberToVerify)
            throws AccountBlockedException, AccountNotFoundException {
        for (Map.Entry<String, Account> accountEntry : accounts.entrySet()) {
            String cardNumber = accountEntry.getKey();
            if (cardNumberToVerify.equals(cardNumber)) {
                if (isBlocked(accountEntry.getValue())) {
                    throw new AccountBlockedException(accountEntry.getValue().getUnblockDate());
                } else {
                    return;
                }
            }
        }
        throw new AccountNotFoundException();
    }

    private boolean isBlocked(Account account) {
        if (account.isBlocked()) {
            if (account.getUnblockDate().getTime() < new Date().getTime()) {
                unblockAccount(account);
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public void exit() {
        saveData(this);
        System.exit(0);
    }

    public void blockAccount(String cardNumberToBlock) {
        pinCodeAttempts = 0;
        for (Map.Entry<String, Account> accountEntry : accounts.entrySet()) {
            String cardNumber = accountEntry.getKey();
            if (cardNumberToBlock.equals(cardNumber)) {
                accountEntry.getValue().setBlocked(true);
                accountEntry.getValue().setUnblockDate(new Date(new Date().getTime() + BLOCK_TIME_IN_MILLS));
            }
        }
    }

    private void unblockAccount(Account account) {
        account.setBlocked(false);
    }

    public void reset() {
        currentAccount = null;
        loggedIn = false;
        pinCodeAttempts = 0;
    }
}
