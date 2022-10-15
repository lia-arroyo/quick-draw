package nz.ac.auckland.se206.words;

import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import java.net.URISyntaxException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Dictionary {

  private static final String API_URL = "https://api.dictionaryapi.dev/api/v2/entries/en/";

  private String word;
  private Boolean definitionExists = true;
  private String definition;

  public Dictionary(String word) throws IOException, CsvException, URISyntaxException {
    this.word = word;
    findDefinition();
  }

  private void findDefinition() throws IOException {
    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder().url(API_URL + word).build();
    Response response = client.newCall(request).execute();
    ResponseBody responseBody = response.body();

    String jsonString = responseBody.string();

    if (jsonString.contains("No Definitions Found")) {
      definitionExists = false;
    } else {

      Object obj = JSONValue.parse(jsonString);

      JSONArray jArray = (JSONArray) obj;

      JSONObject jsonObj = (JSONObject) jArray.get(0);

      JSONArray meaningArray = (JSONArray) jsonObj.get("meanings");

      JSONObject meaningObj = (JSONObject) meaningArray.get(0);
      JSONArray definitionArray = (JSONArray) meaningObj.get("definitions");

      JSONObject definitionObj = (JSONObject) definitionArray.get(0);

      definition = definitionObj.get("definition").toString();
    }
  }

  public String getDefinition() {
    return definition;
  }

  public Boolean getDefinitionExists() {
    return definitionExists;
  }
}
