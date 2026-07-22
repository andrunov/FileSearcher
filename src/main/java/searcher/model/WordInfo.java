package searcher.model;

import java.util.Map;
import java.util.Objects;

/**
 * Represents a word extracted from a file name and its similarity metadata.
 */
public class WordInfo {

    /**
     * unique identifier
     * */
    private int ID;

    /**
     * contains word itself
     * */
    private final String word;

    /**
     * quantity ow this word in representation
     * */
    private int quantity;

    /**
     * weight of this word in representation.
     * Then bigger weight, than more unique word.
     * Small value means that word is very spread in representation.
     * */
    private double weight;


    /**
     * words similar to this one. Key - similar word itself,
     * value - similarity coefficient (100% means that word is equal)
     * */
    private Map<WordInfo, Integer> similarWords;

    /**
     * Creates a word entry with an initial quantity of one.
     *
     * @param word the normalized word value
     */
    public WordInfo(String word) {
        this.word = word;
        this.quantity = 1;
    }

    /**
     * Returns the word value.
     *
     * @return the stored word
     */
    public String getWord() {
        return word;
    }

    /**
     * Returns how often the word appears in the current representation.
     *
     * @return the quantity of the word
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the number of occurrences for the word.
     *
     * @param quantity the new occurrence count
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Returns the word weight used to rank how unique it is.
     *
     * @return the weight value
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Sets the weight for the word.
     *
     * @param weight the new weight
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }

    /**
     * Returns the unique identifier of the word entry.
     *
     * @return the identifier
     */
    public int getID() {
        return ID;
    }

    /**
     * Sets the unique identifier of the word entry.
     *
     * @param ID the new identifier
     */
    public void setID(Integer ID) {
        this.ID = ID;
    }

    /**
     * Returns the map of similar words and their similarity scores.
     *
     * @return the similarity map
     */
    public Map<WordInfo, Integer> getSimilarWords() {
        return similarWords;
    }

    /**
     * Sets the map of similar words and their similarity scores.
     *
     * @param similarWords the similarity map to store
     */
    public void setSimilarWords(Map<WordInfo, Integer> similarWords) {
        this.similarWords = similarWords;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WordInfo wordInfo = (WordInfo) o;
        return ID == wordInfo.ID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ID);
        sb.append('\t');
        sb.append(word);
        sb.append('\t');
        sb.append(quantity);
        sb.append('\t');
        sb.append(weight);
        return sb.toString();
    }
}
