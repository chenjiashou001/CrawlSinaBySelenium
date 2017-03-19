package mongodb;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import tools.FileUtil;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;


/**
 * 用来操作mongodb数据库
 * @author chenjiashou
 *
 */
public class Jdbc {
	// localhost  , 192.168.10.142
	static final String CONNECT_DATABASE = "192.168.10.142";
	static MongoDatabase MDB = null;
	static final String database_name = "sina";//这个程序默认只会使用这一个数据库
	
	/**
	 * 保证整个程序只进行连接一次。
	 * @param database
	 * @return
	 */
	public static MongoDatabase getDataBase() {
		if (null == MDB) {
			 MongoClient mongoClient = new MongoClient(CONNECT_DATABASE , 27017 );
			 MDB= mongoClient.getDatabase(database_name);
		}
		return MDB;
	}
	
	/**
	 * 得到MongoColleciton by collectionName
	 * @param collectionName
	 * @return
	 */
	public static MongoCollection<Document> getCollection(String collectionName) {
		if (MDB == null) {
			getDataBase();
		}
		return MDB.getCollection(collectionName);
	}
	
	/**
	 * 插入doc 到 collectionName中
	 * @param collectionName
	 * @param doc
	 * @return 成功返回true,否则返回false。
	 */
	public static boolean insert_doc(String collectionName, Document doc) {
		MongoCollection<Document> coll = getCollection(collectionName);
		coll.insertOne(doc);
		return true;
	}
	
	/**
	 * 插入many doc into collection
	 * @param collectionName
	 * @param docs
	 * @return
	 */
	public static boolean insert_docs(String collectionName, List<Document> docs) {
		MongoCollection<Document> coll = getCollection(collectionName);
		coll.insertMany(docs);
		return true;
	}
	
	/**
	 * 得到collectionName所有项
	 * @param collectionName
	 * @return List<Document>
	 */
	public static List<Document> find(String collectionName) {
		MongoCollection<Document> coll = getCollection(collectionName);
		FindIterable<Document> findIterable = coll.find();
		MongoCursor<Document> mongoCursor = findIterable.iterator();
		List<Document> docs = new ArrayList<Document>();
		while(mongoCursor.hasNext()){  
			Document doc = mongoCursor.next();
			docs.add(doc);
		}
		return docs;
	}
	
	/**
	 * 将要查询的信息写入find_doc中
	 * @param collectionName
	 * @param find_doc
	 * @return List<Document>
	 */
	public static List<Document> find(String collectionName, Document find_doc) {
		MongoCollection<Document> coll = getCollection(collectionName);
		
		FindIterable<Document> iter = coll.find(find_doc);
		MongoCursor<Document> mongoCursor = iter.iterator();
		
		List<Document> docs = new ArrayList<Document>();
		while(mongoCursor.hasNext()){  
			Document doc = mongoCursor.next();
			docs.add(doc);
		}
		return docs;
	}
	
	/**
	 * 删除一个collection
	 */
	public static void deleteCollection(String collectionName) {
		MongoCollection<Document> coll = getCollection(collectionName);
		Document doc = new Document();
		coll.deleteMany(doc);
	}
	
	/**
	 * 从文件中读取docs，插入Collection中去
	 */
	public static void InsertToCollectionFromFile(String file_path, String collection_name) {
		List<String> file_lines = FileUtil.readLogByList(file_path);
		for (int i = 0; i < file_lines.size(); i++) {
			String file_line = file_lines.get(i);
			Document doc = null;
			try{
				doc = Document.parse(file_line);
			} catch(Exception e) {
				System.out.println("InsertToCollectionFromFile 中, " + file_line + "无法转换为Document");
			}
			if (doc != null) {
				insert_doc(collection_name, doc);//插进去
			}
		}
	} 
	
	/**
	 * 将collection保存至文件中
	 */
	public static void SaveToFileFromCollection(String file_save_path, String collection_name) {
		List<Document> docs = find(collection_name);
		for (int i = 0; i < docs.size(); i++) {
			Document doc = docs.get(i);
			String str_doc = doc.toJson();
			FileUtil.writeLog(file_save_path, str_doc);
		}
	}
	
	/**
	 * update a doc by filter 
	 * if do not have then:
	 * 		do nothing
	 * @param collection_name
	 * @param filter
	 * @param update_value
	 */
	public static void UpdateOneByKey_Vaule(String collection_name, Document filter, Document update_value) {
		MongoCollection<Document> col = getCollection(collection_name);
		col.updateMany(filter, new Document("$set", update_value));
	}
	
	public static void main(String args[]) {
		Document filter = new Document();
		filter.append("id", 100);
		Document update_value = new Document();
		update_value.append("careenough", "1");
		UpdateOneByKey_Vaule("loginuser", filter, update_value);
	}
	
}

