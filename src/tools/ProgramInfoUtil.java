package tools;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import mongodb.Jdbc;

/**
 * 处理programinfo
 * @author chenhuan001
 *
 */
public class ProgramInfoUtil {

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		saveToFile(Jdbc.find("program_info"));
		getToFile();
	}

	public static List<Document> getToFile() {
		List<Document> docs = new ArrayList<Document>();
		List<String> str_docs = FileUtil.readLogByList("program_info.txt");
		for (int i = 0; i < str_docs.size(); i++) {
			Document doc = Document.parse(str_docs.get(i));
			docs.add(doc);
		}
		return docs;
	}

	public static void saveToFile(List<Document> docs) {
		for (int i = 0; i < docs.size(); i++) {
			Document doc = docs.get(i);
			String str_doc = doc.toJson();
			//System.out.println(doc.toString());
			//Document new_doc = new Document();
			//new_doc = Document.parse(str_doc);
			
			FileUtil.writeLog("program_info.txt", str_doc);
		}		
	}

	
}


