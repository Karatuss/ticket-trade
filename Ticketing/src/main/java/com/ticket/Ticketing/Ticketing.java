package com.ticket.Ticketing;

import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.ClusterOptions;
import com.ticket.Ticketing.service.SeatService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


// MAIN
@SpringBootApplication
public class Ticketing {
	// static variables

	private static final String connStr = "127.0.0.1";
	public static SeatService seatService; //TODO 이벤트 쪽으로 옮겨야 함
	public static Cluster cluster;

	// run only once when running application
	static{
		// connect couchbase
		cluster = Cluster.connect(
				connStr,
				ClusterOptions.clusterOptions("adminuser", "adminuser")
		);
    
		// create seat //TODO 나중에 이벤트 생성할 때 실행 해야 함
		seatService = new SeatService();
		seatService.createSeatDocuments();
	}

	public static void main(String[] args) {

		// run main
		SpringApplication.run(Ticketing.class, args);

		// JVM Shutdown Hook Settings
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			// delete all seat info for reset  //TODO 나중에 이벤트 삭제할 때 실행 해야 함
			seatService.deleteAllSeatDocuments();

			// disconnect cluster when terminating application
			cluster.disconnect();
		}));
	}
}
