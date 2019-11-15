//import java.io.File;
//import java.util.List;
//import java.util.ArrayList;
//
//import org.apache.commons.io.FileUtils;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//public class JSONTest {
//  
//  public static void main(String[] args) throws Exception {
//      File file = new File("./ObjectLoc.json");
//      String content = FileUtils.readFileToString(file, "utf-8");
//      
//      JSONArray oldArray = new JSONArray(content);
//      JSONArray newArray = new JSONArray();
//      
//      List list = new ArrayList();
//      
//      for(int i = 0; i < oldArray.length(); i++) {
//          JSONObject o = oldArray.getJSONObject(i);
//          
//          /* conditions */
//          if(o.getInt("id") > 234) continue;
//          if(o.getInt("x") > 434) continue;
//          
//          list.add(o);
//      }
//      
//      newArray.put(list);
//      String s = newArray.toString(2);
//
//      System.out.println(s);
//  }
//  
//}