public class Tester implements AbstractTester {

    private String connectionString;


    public Tester(String connectionString) {

        this.connectionString = connectionString;

    }
    @Override
    public void addDocuments() {}

    @Override
    public boolean checkDocuments() {
        return true;
    }
}
