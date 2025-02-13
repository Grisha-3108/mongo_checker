

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.IOException;
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

            Runtime.getRuntime().exec(command);

        }catch (IOException e){
            e.printStackTrace();
        }


        //Очистка базы
//        try (MongoClient mongoClient = MongoClients.create(args[1])) {
//            MongoDatabase database = mongoClient.getDatabase(databaseName); // Имя базы данных
//            //Очистка базы
//            database.drop();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        //Проверка и вывод результата
        System.out.println(tester.checkDocuments());

    }
}
