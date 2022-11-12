package sleepwalker.atm;

import java.math.BigDecimal;

public class Account {
    private final String cardNumber;
    private final String PIN;
    private BigDecimal balance;


    public Account(String cardNumber, String PIN, BigDecimal balance) {
        this.cardNumber = cardNumber;
        this.PIN = PIN;
        this.balance = balance;
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

    @Override
    public String toString() {
        return "User{" +
                "cardNumber='" + cardNumber + '\'' +
                ", PIN=" + PIN +
                ", balance=" + balance +
                '}';
    }
}
