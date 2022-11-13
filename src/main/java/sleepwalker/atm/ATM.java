package sleepwalker.atm;

import sleepwalker.exception.*;
import java.math.BigDecimal;

public class ATM {
    private final static BigDecimal DEPOSIT_LIMIT = BigDecimal.valueOf(1_000_000);
    private BigDecimal atmCashAmount;
    private final Menu menu;

    public ATM(BigDecimal atmCashAmount) {
        this.atmCashAmount = atmCashAmount;
        menu = new ConsoleMenu();
    }
    public void run(AccountDataStorage dataStorage) {
        menu.start(this, new Session(dataStorage));
    }
    public void withdraw(Account card, BigDecimal withdrawalAmount)
            throws atmNotEnoughMoneyException, IncorrectAmountEnteredException, InsufficientBalanceException {
        BigDecimal balance = card.getBalance();

        if (withdrawalApproved(balance, withdrawalAmount)) {
            BigDecimal newBalance = balance.subtract(withdrawalAmount);
            card.setBalance(newBalance);

            BigDecimal newAtmCashAmount = this.getAtmCashAmount().subtract(withdrawalAmount);
            this.setAtmCashAmount(newAtmCashAmount);
        }
    }
    public void deposit(Account account, BigDecimal depositAmount)
            throws IncorrectAmountEnteredException, DepositLimitExceededException {
        if (depositApproved(depositAmount)) {
            BigDecimal newBalance = account.getBalance().add(depositAmount);
            account.setBalance(newBalance);

            BigDecimal newAtmCashAmount = this.getAtmCashAmount().add(depositAmount);
            this.setAtmCashAmount(newAtmCashAmount);
        }
    }
    public BigDecimal getBalance(Account account) {
        return account.getBalance();
    }
    private boolean depositApproved(BigDecimal depositAmount)
            throws IncorrectAmountEnteredException, DepositLimitExceededException {
        if (depositAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IncorrectAmountEnteredException();
        } else if (depositAmount.compareTo(DEPOSIT_LIMIT) > 0) {
            throw new DepositLimitExceededException();
        } else {
            return true;
        }
    }
    private boolean withdrawalApproved(BigDecimal balance, BigDecimal withdrawalAmount)
            throws atmNotEnoughMoneyException, IncorrectAmountEnteredException, InsufficientBalanceException {
        if (withdrawalAmount.compareTo(this.getAtmCashAmount()) > 0) {
            throw new atmNotEnoughMoneyException();
        } else if (withdrawalAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IncorrectAmountEnteredException();
        } else if (balance.compareTo(withdrawalAmount) < 0) {
            throw new InsufficientBalanceException();
        } else {
            return true;
        }
    }
    private BigDecimal getAtmCashAmount() {
        return atmCashAmount;
    }
    private void setAtmCashAmount(BigDecimal atmCashAmount) {
        this.atmCashAmount = atmCashAmount;
    }
}