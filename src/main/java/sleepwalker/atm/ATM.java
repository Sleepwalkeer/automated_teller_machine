package sleepwalker.atm;

public class ATM {

    public static void main(String[] args) {

        new ATM().run();
    }

    private void run() {
        new Menu().start();
    }
}
