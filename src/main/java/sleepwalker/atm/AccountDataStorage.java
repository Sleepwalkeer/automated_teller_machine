package sleepwalker.atm;

import java.util.List;

public interface AccountDataStorage {
    List<Account> readAccountData();
    void saveAccountData(List<Account> userData);
}
