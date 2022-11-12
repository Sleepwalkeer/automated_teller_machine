package sleepwalker.atm;

import java.math.BigDecimal;

public class ATM {

    //TODO Надо грамотно расставить методы ВЕЗДЕ
    private final static BigDecimal DEPOSIT_LIMIT = BigDecimal.valueOf(1_000_000);
    private BigDecimal atmCashAmount;
    private final Menu menu;
    private final Session session;

    public ATM(BigDecimal atmCashAmount, AccountDataStorage dataStorage) {
        this.atmCashAmount = atmCashAmount;
        session = new Session(dataStorage);
        menu = new ConsoleMenu();
    }

    public void run() {
        menu.start(this, session);
    }

    public void withdraw(Account card, BigDecimal withdrawalAmount) {
        BigDecimal balance = card.getBalance();

        if (withdrawalApproved(balance, withdrawalAmount)) {
            BigDecimal newBalance = balance.subtract(withdrawalAmount);
            card.setBalance(newBalance);

            BigDecimal newAtmCashAmount = this.getAtmCashAmount().subtract(withdrawalAmount);
            this.setAtmCashAmount(newAtmCashAmount);
        }
    }
    public void deposit(Account account, BigDecimal depositAmount) {
        BigDecimal balance = account.getBalance();
        if (depositApproved(depositAmount)) {
            BigDecimal newBalance = balance.add(depositAmount);
            account.setBalance(newBalance);

            BigDecimal newAtmCashAmount = this.getAtmCashAmount().add(depositAmount);
            this.setAtmCashAmount(newAtmCashAmount);
        }
    }
    public BigDecimal getBalance(Account account) {
        return account.getBalance();
    }

    private boolean depositApproved(BigDecimal depositAmount) {              //СКОРЕЕ ВСЕГО НУЖНЫ СВОИ ИСКЛЮЧЕНИЯ
        return depositAmount.compareTo(BigDecimal.ZERO) > 0 &&
                depositAmount.compareTo(DEPOSIT_LIMIT) < 0;
    }
    private boolean withdrawalApproved(BigDecimal balance, BigDecimal withdrawalAmount) { //СКОРЕЕ ВСЕГО НУЖНЫ СВОИ ИСКЛЮЧЕНИЯ
        return withdrawalAmount.compareTo(this.getAtmCashAmount()) < 0 &&
                withdrawalAmount.compareTo(BigDecimal.ZERO) > 0 &&
                balance.compareTo(withdrawalAmount) > 0;
    }
    private BigDecimal getAtmCashAmount() {
        return atmCashAmount;
    }
    private void setAtmCashAmount(BigDecimal atmCashAmount) {
        this.atmCashAmount = atmCashAmount;
    }




}


