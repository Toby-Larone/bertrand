package uk.co.markg.mimic.markov;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import com.esotericsoftware.kryo.Kryo;

public interface Markov {

  static final WeightedCollection<String> SENTENCE_ENDS = getSentenceEnds();
  static final List<String> VALID_END_WORD_STOPS = List.of("?", "!", ".");
  static final Kryo kryo = initKryo();
  static String FILE_END = ".m";
  static String END_WORD = "END_WORD";

  private static Kryo initKryo() {
    Kryo kryo = new Kryo();
    kryo.setRegistrationRequired(false);
    return kryo;
  }

  /**
   * Creates a collection of sentence ends with probabilities taken from a subset of user messages.
   * 
   * @return The collection of sentence ends
   */
  private static WeightedCollection<String> getSentenceEnds() {
    var collection = new WeightedCollection<String>();
    collection.add(new WeightedElement<String>(".", 4369));
    collection.add(new WeightedElement<String>("!", 1660));
    collection.add(new WeightedElement<String>("?", 2733));
    collection.add(new WeightedElement<String>("!!", 132));
    collection.add(new WeightedElement<String>("??", 114));
    collection.add(new WeightedElement<String>("!?", 27));
    collection.add(new WeightedElement<String>("...", 965));
    return collection;
  }

  /**
   * Convenience method to generate multiple sentences.
   * 
   * @return The sentences joined together by a space character
   */
  default public String generateRandom() {
    int sentences = ThreadLocalRandom.current().nextInt(5) + 1;
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < sentences; i++) {
      sb.append(generate()).append(" ");
    }
    return sb.toString();
  }

  /**
   * Generates a sentence from the markov chain
   * 
   * @return A complete sentence
   */
  default public String generate() {
    return generate("");
  }
  
  /**
   * Checks whether a word can be matched as an end word. i.e. the word ends a sentence.
   * 
   * @param word The word to check
   * @return True if the word can be matched as an end word
   */
  default boolean isEndWord(String word) {
    for (String stop : VALID_END_WORD_STOPS) {
      if (word.endsWith(stop)) {
        return true;
      }
    }
    return false;
  }

  public String generate(String start);

  public void parseInput(String input);

  public void save(String file) throws IOException;

  public String getFileEnd();
  
  public Long getLastMessageId();
  
  public void setLastMessageId(long messageid);

}
