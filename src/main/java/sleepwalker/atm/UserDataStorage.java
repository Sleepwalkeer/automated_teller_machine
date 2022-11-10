package sleepwalker.atm;

import java.util.List;

public interface UserDataStorage {
    List<User> readUserData();
    void saveUserData(List<User> userData);
}
