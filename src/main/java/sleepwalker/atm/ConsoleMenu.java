package sleepwalker.atm;

public class ConsoleMenu implements Menu {

    private ATM atm;
    private Account account;

    @Override
    public void start(ATM atm) {
        this.atm = atm;
        logIn();
    }


    private void logIn() {
        System.out.println();
    }

}
