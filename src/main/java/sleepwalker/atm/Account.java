package sleepwalker.atm;

import java.math.BigDecimal;
import java.util.Date;

public class Account {
    private final String cardNumber;
    private final String PIN;
    private BigDecimal balance;
    private boolean blocked;
    private Date unblockDate;



    public Account(String cardNumber, String PIN, BigDecimal balance, boolean blocked, Date unblockDate) {
        this.cardNumber = cardNumber;
        this.PIN = PIN;
        this.balance = balance;
        this.blocked = blocked;
        this.unblockDate = unblockDate;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getPIN() {
        return PIN;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
    public Date getUnblockDate() {
        return unblockDate;
    }
    public void setUnblockDate(Date unblockDate){
        this.unblockDate = unblockDate;
    }


}
