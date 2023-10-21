package com.ticket.Ticketing;

import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.ClusterOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


// MAIN
@SpringBootApplication
public class Ticketing {

	// static variables
	private static final String connStr = "127.0.0.1";
	public static Cluster cluster;

	public static void main(String[] args) {

		// connect couchbase
		cluster = Cluster.connect(
				connStr,
				ClusterOptions.clusterOptions("adminuser", "adminuser")
		);

		SpringApplication.run(Ticketing.class, args);

		// JVM Shutdown Hook Settings
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			// disconnect cluster when terminating application
			cluster.disconnect();
		}));
	}

}
