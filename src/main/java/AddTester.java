import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class AddTester implements AbstractTester{
    private AbstractTester tester;
    private String document;
    private String connectionString;
    private String databaseName;
    private String collectionName;

    public AddTester(AbstractTester tester, String connectionString, String databaseName, String collectionName, String document){
        this.tester = tester;
        this.document = document;
        this.connectionString = connectionString;
        this.databaseName = databaseName;
        this.collectionName = collectionName;
    }

    @Override
    public void addDocuments() {
        // Подключение к MongoDB
        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
            // Выбор базы данных
            MongoDatabase database = mongoClient.getDatabase(databaseName); // Имя базы данных

            // Выбор коллекции
            MongoCollection<Document> collection = database.getCollection(collectionName); // Имя коллекции


            // Создание документа из JSON
            Document doc = Document.parse(this.document);

            // Вставка документа в коллекцию
            collection.insertOne(doc);

        } catch (Exception e) {
            e.printStackTrace();
        }
        tester.addDocuments();
    }

    @Override
    public boolean checkDocuments() {
        return tester.checkDocuments();
    }
}
