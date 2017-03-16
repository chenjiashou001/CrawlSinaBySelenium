package demo;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class OnlineConnectMongodb {

	public static void main(String[] args) {
		MongoClient mongoClient = new MongoClient( "192.168.10.142" , 27017 );
        // 连接到数据库
        MongoDatabase mongoDatabase = mongoClient.getDatabase("sina");  
        System.out.println("Connect to database successfully");
        MongoCollection<Document> collection = mongoDatabase.getCollection("user_program_data");
        FindIterable<Document> findIterable = collection.find();  
        MongoCursor<Document> mongoCursor = findIterable.iterator();  
        while(mongoCursor.hasNext()){  
           System.out.println(mongoCursor.next());  
        }  
	}

}
