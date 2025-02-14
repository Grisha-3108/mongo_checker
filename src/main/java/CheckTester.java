import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.IOException;

public class CheckTester implements AbstractTester{
    private AbstractTester tester;
    private String document;
    private String connectionString;
    private String databaseName;
    private String collectionName;
    private long amount;

    public CheckTester(AbstractTester tester, String connectionString, String databaseName, String collectionName, long amount, String document){
        this.tester = tester;
        this.document = document;
        this.connectionString = connectionString;
        this.databaseName = databaseName;
        this.collectionName = collectionName;
        this.amount = amount;
    }

    @Override
    public void addDocuments() {
        tester.addDocuments();
    }

    @Override
    public boolean checkDocuments() {
        boolean result =  tester.checkDocuments();
        // Подключение к MongoDB
        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
            // Выбор базы данных
            MongoDatabase database = mongoClient.getDatabase(databaseName); // Имя базы данных

            // Выбор коллекции
            MongoCollection<Document> collection = database.getCollection(collectionName); // Имя коллекции
            System.out.println("Количество документов в коллекции " + collection.countDocuments());

            // Создание документа из JSON
            Document doc = Document.parse(document);

            //Подсчёт документов, удовлетворяющих условию
            long count = collection.countDocuments(doc);

            result = result && (count == amount);


        } catch (MongoException e) {
            e.printStackTrace();
        }
        return result;
    }
}
