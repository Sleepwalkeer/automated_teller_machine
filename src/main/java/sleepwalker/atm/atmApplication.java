package sleepwalker.atm;

import java.math.BigDecimal;

public class atmApplication {
    public static void main(String[] args) {
        new ATM(BigDecimal.valueOf(500_000)).run();
    }
}
