package sleepwalker.atm;

import java.util.Map;

public interface AccountDataStorage {
    Map<String, Account> readData();
    void saveData(Session session);
}
