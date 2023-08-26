import org.example.storage.DataBaseInitPopulateService;
import org.example.storage.Database;

public class App {
    public static void main(String[] args) {
        Database database=Database.getInstance();
        new DataBaseInitPopulateService().initDb(database);
    }
}


