package config;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;

public class Config {

    static Cluster cluster;

    public Config() {
        cluster = Cluster.connect("localhost", "admin", "admin1");;
    }

    public static Cluster getCluster() {
        return cluster;
    }
}
