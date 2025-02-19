

import com.mongodb.MongoException;
import com.mongodb.client.*;
import org.bson.Document;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    //3 параметра - 1 сценарий для проверки, 2 - адрес сервера("mongodb://localhost:27017"), адрес оболочки("C:\\Program Files\\MongoDB\\Server\\4.2\\bin\\mongo.exe")
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AbstractTester tester = new Tester(args[1]);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] tokens = line.split(" ");
            if(tokens[0].equals("add")) {
                tester = new AddTester(tester, args[1], tokens[1], tokens[2], line.substring(line.indexOf("{")));
            }
            try {
                if (tokens[0].equals("check")) {
                    tester = new CheckTester(tester, args[1], tokens[1], tokens[2], Integer.parseInt(tokens[3]), line.substring(line.indexOf("{")));
                }
            }catch (NumberFormatException e) {
                System.out.println("Ошибка извлечения числа документов из параметров!");
            }
        }
        //добавление записей в базу
        tester.addDocuments();

        //Выполнение сценария пользователя
        try{
            String[] command = {args[2], args[0]};
            Process mongoExecute = Runtime.getRuntime().exec(command);
            InputStream in = mongoExecute.getErrorStream();
            mongoExecute.waitFor();
        }catch (IOException e){
            e.printStackTrace();
        }catch (InterruptedException e){
            System.out.println("Ошибка! Выполнение сценария пользователя было прервано!");
            e.printStackTrace();
        }
        //Проверка и вывод результата
        System.out.println(tester.checkDocuments());

        //Очистка базы
        try (MongoClient mongoClient = MongoClients.create(args[1])) {
            List<String> systemDatabases = new ArrayList<>(List.of("local", "admin", "config"));
            //Получаем список баз
            MongoIterable<String> databases =  mongoClient.listDatabaseNames();
            //Выполняем очистку всех баз
            for(var base: databases){
                //Проверка того, что база не является системной
                if(!systemDatabases.contains(base)){
                    mongoClient.getDatabase(base).drop();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
