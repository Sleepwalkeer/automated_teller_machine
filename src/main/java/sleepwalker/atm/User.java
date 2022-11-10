package sleepwalker.atm;

import java.math.BigDecimal;

public class User {
    private final String cardNumber;
    private final int PIN;
    private BigDecimal balance;

    public User(String cardNumber, int PIN, BigDecimal balance) {
        this.cardNumber = cardNumber;
        this.PIN = PIN;
        this.balance = balance;
    }
    public String getCardNumber() {
        return cardNumber;
    }

    public int getPIN() {
        return PIN;
    }

    public BigDecimal getBalance() {
        return balance;
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
