package sleepwalker.atm;

import java.util.Map;

public class Session {

    private final AccountDataStorage dataStorage;
    private final Map<String,Account> accountData;
    private Account currentAccount;
    private boolean loggedIn = false;


    public Session(AccountDataStorage dataStorage) {
        this.dataStorage = dataStorage;
        this.accountData = dataStorage.readData();
    }

    public Map<String,Account> getAccountData() {
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
            String currentAccCardNumber = accountEntry.getValue().getCardNumber().replace("-", "");
            if (cardNumber.equals(currentAccCardNumber) && pinCode.equals(accountEntry.getValue().getPIN())) {
                currentAccount = accountEntry.getValue();
                loggedIn = true;
                return true;
            }
        }
        return false;
    }
    public void logOut(){
        saveData(this);
        currentAccount = null;
        loggedIn = false;
    }

    public void exit(){
        saveData(this);
        System.exit(0);
    }

    public boolean cardNumberValid(String cardNumberToVerify) { //СКОРЕЕ ВСЕГО НАДО БУДЕТ МЕНЯТЬ ФОРМАТ ВВОДА
        //еще проверка на правильность ввода
        for (Map.Entry<String, Account> accountEntry : accountData.entrySet()) {
            String cardNumber = accountEntry.getValue().getCardNumber().replace("-", "");
            if (cardNumberToVerify.equals(cardNumber)) {
                return true;
            }
        }
        return false;
    }
}
