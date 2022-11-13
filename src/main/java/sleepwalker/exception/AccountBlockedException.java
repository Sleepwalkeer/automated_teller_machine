package sleepwalker.exception;

import java.util.Date;

public class AccountBlockedException extends Exception{

    private final Date dateNow = new Date();
    private final Date unbanDate;

    public AccountBlockedException(Date unbanDate) {
        this.unbanDate = unbanDate;
    }
    public long timeToUnban(){
        return unbanDate.getTime() - dateNow.getTime();
    }
}
