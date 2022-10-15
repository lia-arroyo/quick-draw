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

/**
 * This class handles all the functionalities related to the definitions, such as connecting to the
 * dictionary API and extracting the string from the JSON.
 */
public class Dictionary {

  private static final String API_URL = "https://api.dictionaryapi.dev/api/v2/entries/en/";

  private String word;
  private Boolean definitionExists = true;
  private String definition;

  /**
   * The constructor for the Dictionary class. When a new instance of a dictionary is made, the
   * definition of the word is found immediately.
   *
   * @param word
   * @throws IOException if the file is not found
   * @throws CsvException if the csv file cannot be read
   * @throws URISyntaxException if the URI is incorrect
   */
  public Dictionary(String word) throws IOException, CsvException, URISyntaxException {

    this.word = word;
    findDefinition();
  }

  /**
   * This method finds the definition of the word by calling an API and extracting the first
   * definition from the response.
   *
   * @throws IOException if the file cannot be opened
   */
  private void findDefinition() throws IOException {

    // Getting the response from the API
    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder().url(API_URL + word).build();
    Response response = client.newCall(request).execute();
    ResponseBody responseBody = response.body();

    String jsonString = responseBody.string();

    // Checking if the word does not have a definition
    if (jsonString.contains("No Definitions Found")) {
      definitionExists = false;
    } else {
      // If it does, then we extract the definition from the JSON response by
      // following the JSON structure.
      Object obj = JSONValue.parse(jsonString);
      JSONArray jArray = (JSONArray) obj;
      JSONObject jsonObj = (JSONObject) jArray.get(0);

      JSONArray meaningArray = (JSONArray) jsonObj.get("meanings");

      // Getting the first element of 'meanings' which takes the 'noun' version of the
      // word as the game only contains noun words.
      JSONObject meaningObj = (JSONObject) meaningArray.get(0);
      JSONArray definitionArray = (JSONArray) meaningObj.get("definitions");

      // Getting the very first definition and setting it to the definition string
      // variable.
      JSONObject definitionObj = (JSONObject) definitionArray.get(0);
      definition = definitionObj.get("definition").toString();
    }
  }

  /**
   * This is the getter method for the definition of the word
   *
   * @return the definition of the word
   */
  public String getDefinition() {
    return definition;
  }

  /**
   * This is the getter method for the boolean variable 'definitionExists'. It returns true if the
   * definition of a word exists, and false if not.
   *
   * @return the boolean value of 'definitionExists'
   */
  public Boolean getDefinitionExists() {
    return definitionExists;
  }
}
