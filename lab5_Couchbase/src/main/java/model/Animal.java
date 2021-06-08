package model;

import com.couchbase.client.java.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Animal {

    private long id;
    private String name;
    private String species;
    private int age;

    public JsonObject toJsonObject() {
        return JsonObject.create()
                .put("name", this.getName())
                .put("species", this.getSpecies())
                .put("age", this.getAge());
    }

    public static Animal parseJsonObject(Long id, JsonObject content) {
        return new Animal(
                id,
                content.getString("name"),
                content.getString("species"),
                content.getInt("age"));
    }

}
