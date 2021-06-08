import com.couchbase.client.core.error.CouchbaseException;
import com.couchbase.client.core.error.DocumentExistsException;
import com.couchbase.client.core.error.DocumentNotFoundException;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.analytics.AnalyticsResult;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.kv.GetResult;
import com.couchbase.client.java.query.QueryResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.Config;
import model.Animal;

import java.util.Random;
import java.util.Scanner;

import static com.couchbase.client.java.kv.ReplaceOptions.replaceOptions;
import static com.couchbase.client.java.query.QueryOptions.queryOptions;

public class AnimalRepository {

    private final Random random = new Random(System.currentTimeMillis());
    private final static String tableName = "animals";
    Cluster cluster;
    Bucket bucket;
    Collection collection;

    private final static ObjectMapper objectMapper = new ObjectMapper();

    public AnimalRepository() {
        cluster = Config.getCluster();
        bucket = cluster.bucket(tableName);
        //cluster.query("CREATE INDEX name_index ON " + tableName + "(name) USING GSI");
        //cluster.query("create primary index animals_index on animals");
        collection = bucket.defaultCollection();
    }

    public Animal add() {
        Animal animal = enterAnimalInfo();

        try {
            collection.insert(String.valueOf(animal.getId()), animal.toJsonObject());
        } catch (DocumentExistsException ex) {
            System.err.println("The document already exists!");
        } catch (CouchbaseException ex) {
            System.err.println("Something went wrong: " + ex);
        }

        System.out.println(animal);
        return animal;
    }

    public Animal getById(Long id) {
        Animal animal = null;
        try {
            GetResult getResult = collection.get(id.toString());
            JsonObject content = getResult.contentAsObject();
            animal = Animal.parseJsonObject(id, content);
            System.out.println(animal);
        } catch (DocumentNotFoundException ex) {
            System.out.println("Document not found!");
        }
        return animal;
    }

    public void deleteById(Long id) {
        try {
            collection.remove(id.toString());
            System.out.println("Successfully deleted row");
        } catch (DocumentNotFoundException ex) {
            System.out.println("Document did not exist when trying to remove");
        }
    }

    public Animal updateById(Long id) {
        Animal animal = enterAnimalInfo();
        animal.setId(id);
        GetResult result = collection.get(id.toString());
        if (result != null) {
            collection.replace(id.toString(), animal.toJsonObject(), replaceOptions().cas(result.cas()));
            System.out.println(animal);
        } else {
            System.out.println("Id not found");
        }
        return animal;
    }

    public void getByName(String name) {
        try {
            final QueryResult result = cluster.query("select * from " + tableName + " where name = $name",
                    queryOptions().parameters(JsonObject.create().put("name", name)));

            for (JsonObject row : result.rowsAsObject()) {
                System.out.println(row);
            }
        } catch (CouchbaseException ex) {
            ex.printStackTrace();
        }
    }

    public void getAvgAge() {
        try {
            final QueryResult result = cluster
                    .query("select avg(age) as AvgAge from " + tableName + "");

            for (JsonObject row : result.rowsAsObject()) {
                System.out.println("Average age of animals: " + row.get("AvgAge"));
            }
        } catch (CouchbaseException ex) {
            ex.printStackTrace();
        }
    }

    public void getAll() {
        try {
            final QueryResult result = cluster.query("select * from animals");
            for (JsonObject row : result.rowsAsObject()) {
                System.out.println(row);
            }
        } catch (CouchbaseException ex) {
            ex.printStackTrace();
        }
    }

    private Animal enterAnimalInfo() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Name: ");
        String name = scanner.next();
        System.out.print("Species: ");
        String species = scanner.next();
        System.out.print("Age: ");
        int age = scanner.nextInt();
        return new Animal(Math.abs(random.nextLong()), name, species, age);
    }
}
