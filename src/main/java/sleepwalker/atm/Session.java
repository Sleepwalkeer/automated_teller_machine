package sleepwalker.atm;

import sleepwalker.exception.*;

import java.util.Date;
import java.util.Map;

public class Session {

    public final static byte MAX_PIN_CODE_ENTER_ATTEMPTS = 3;
    public final static long HOUR = 3_600_000;
    private final static long BLOCK_TIME = 86_400_000;
    private final AccountDataStorage dataStorage;
    private byte pinCodeAttempts;
    private final Map<String, Account> accountData;
    private Account currentAccount;
    private boolean loggedIn = false;

    public Session(AccountDataStorage dataStorage) {
        pinCodeAttempts = 0;
        this.dataStorage = dataStorage;
        this.accountData = dataStorage.readData();
    }

    public byte getPinCodeAttempts() {
        return pinCodeAttempts;
    }

    public void increasePinCodeAttempts() {
        pinCodeAttempts++;
    }

    public Map<String, Account> getAccountData() {
        return accountData;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public Account getCurrentAccount() {
        return currentAccount;
    }

    public void saveData(Session session) { //ТУТ НАДО БУДЕТ МЕНЯТЬ
        accountData.put(currentAccount.getCardNumber(), currentAccount);
        dataStorage.saveData(session);
    }


    public boolean logIn(String cardNumber, String pinCode) {
        for (Map.Entry<String, Account> accountEntry : accountData.entrySet()) {
            String currentAccCardNumber = accountEntry.getKey().replace("-", "");
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
        for (Map.Entry<String, Account> accountEntry : accountData.entrySet()) {
            String cardNumber = accountEntry.getKey().replace("-", "");
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
        for (Map.Entry<String, Account> accountEntry : accountData.entrySet()) {
            String cardNumber = accountEntry.getKey().replace("-", "");
            if (cardNumberToBlock.equals(cardNumber)) {
                accountEntry.getValue().setBlocked(true);
                accountEntry.getValue().setUnblockDate(new Date(new Date().getTime() + BLOCK_TIME));
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
