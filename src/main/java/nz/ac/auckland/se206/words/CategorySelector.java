package nz.ac.auckland.se206.words;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import nz.ac.auckland.se206.profiles.UserProfileManager;

/** This class is for all things to do with word selection for each round. */
public class CategorySelector {

  public enum Difficulty {
    E,
    M,
    H
  }

  public static String chosenWord;
  public static Difficulty currentDifficulty;
  private Map<Difficulty, List<String>> difficultyToCategories;

  /**
   * This constructor essentially reads the csv file full of words and parses it into a hashmap that
   * has each key as the 3 different difficulties - E, M, and H.
   *
   * @throws IOException {@inheritDoc}
   * @throws CsvException {@inheritDoc}
   * @throws URISyntaxException {@inheritDoc}
   */
  public CategorySelector() throws IOException, CsvException, URISyntaxException {
    difficultyToCategories = new HashMap<>();

    // initialising each key,value pair for hashmap
    for (Difficulty difficulty : Difficulty.values()) {
      difficultyToCategories.put(difficulty, new ArrayList<>());
    }

    // reading the file and adding each word to the hashmap.
    for (String[] line : getLines()) {
      difficultyToCategories.get(Difficulty.valueOf(line[1])).add(line[0]);
    }
  }

  /**
   * This method returns a random category (word) when called.
   *
   * @param difficulty the difficulty of the category (E, M or H)
   * @return the chosen category
   */
  public String getRandomCategory(Difficulty difficulty) {

    // a clone of the word list
    List<String> wordListCopy = difficultyToCategories.get(difficulty);

    // removing all played words from the cloned list
    wordListCopy.removeAll(UserProfileManager.currentProfile.getWordHistory());

    // Returns a random word from the updated word list without played words
    return wordListCopy.get(new Random().nextInt(difficultyToCategories.get(difficulty).size()));
  }

  /**
   * This method returns all the lines from the csv file.
   *
   * @return the list of all lines as strings
   * @throws IOException if the file is not found
   * @throws CsvException all exceptions for opencsv
   * @throws URISyntaxException if a string cannot be used as a URI
   */
  protected List<String[]> getLines() throws IOException, CsvException, URISyntaxException {
    File fileName =
        new File(CategorySelector.class.getResource("/category_difficulty.csv").toURI());

    try (FileReader fr = new FileReader(fileName, StandardCharsets.UTF_8);
        CSVReader reader = new CSVReader(fr)) {
      return reader.readAll();
    }
  }

  /**
   * This method sets a new chosen category and updates the static variable.
   *
   * @param difficulty the difficultly of the category (E, M or H)
   */
  public void setNewChosenWord(Difficulty difficulty) {
    CategorySelector.chosenWord = getRandomCategory(difficulty);
    CategorySelector.currentDifficulty = difficulty;
  }

  /**
   * This method returns the chosen word.
   *
   * @return the chosen word
   */
  public String getChosenWord() {
    return CategorySelector.chosenWord;
  }

  /**
   * This method calculates the amount of words in the hashmap given the difficulty.
   *
   * @param difficulty difficulty of word list desired
   * @return the number of entries in this word list
   */
  public int getTotalWordCount(Difficulty difficulty) {
    return difficultyToCategories.get(difficulty).size();
  }
}
