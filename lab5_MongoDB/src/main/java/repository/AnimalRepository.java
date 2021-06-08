package repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import config.Config;
import model.Animal;
import org.bson.Document;

import java.util.Random;
import java.util.Scanner;

import static com.mongodb.client.model.Filters.*;

public class AnimalRepository {

    private final Random random = new Random(System.currentTimeMillis());
    private final static String tableName = "animals";
    MongoDatabase db;
    MongoCollection<Document> collection;
    private final static ObjectMapper objectMapper = new ObjectMapper();

    public AnimalRepository() {
        db = Config.getDB();
        //db.getCollection(tableName).drop();
        collection = db.getCollection(tableName);

    }

    public Animal addAnimal() {
        Animal animal = enterAnimalInfo();

        Document newAnimal = new Document("_id", animal.getId())
                .append("name", animal.getName())
                .append("species", animal.getSpecies())
                .append("age", animal.getAge());
        collection.insertOne(newAnimal);

        System.out.println(animal);
        return animal;
    }

    public void getById(Long id) {
        Document found = collection.find(eq("_id", id)).first();

        if (found != null) {
            System.out.println(found);
        } else {
            System.out.println("Id not found");
        }
    }

    public void deleteById(Long id) {
        DeleteResult del = collection.deleteOne(eq("_id", id));
        if (del.getDeletedCount() > 0) {
            System.out.println("Successfully removed");
        } else {
            System.out.println("Id not found");
        }
    }

    public Animal updateById(Long id) {
        Animal animal = enterAnimalInfo();
        animal.setId(id);
        UpdateResult res = collection.updateOne(eq("_id", id),
                new Document("$set", new Document("name", animal.getName())
                        .append("species", animal.getSpecies())
                        .append("age", animal.getAge())));

        if (res.isModifiedCountAvailable()) {
            System.out.println(animal);
        } else {
            System.out.println("Id not found");
        }

        return animal;
    }

    public void getByName(String name) {

        FindIterable<Document> findings = collection.find(eq("name", name));
        for (Document doc: findings) {
            System.out.println(doc);
        }
    }

    public void getAvgAge() {
        int sum = 0;
        int n = 0;
        for (Document doc : collection.find()) {
            sum += Integer.parseInt(doc.get("age").toString());
            n++;
        }
            System.out.println("Average age of animals: " + (float)sum/n);
    }

    public void getAll() {
        for (Document doc: collection.find()) {
            System.out.println(doc);
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
