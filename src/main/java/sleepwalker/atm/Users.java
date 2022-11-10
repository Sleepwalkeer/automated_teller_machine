package sleepwalker.atm;
import java.util.List;

public class Users {
    UserDataStorage dataStorage = new UserDataFile();

    public List<User> getData() {
        return dataStorage.readUserData();
    }

    public void saveData(List<User> users){
        dataStorage.saveUserData(users);
    }







}
