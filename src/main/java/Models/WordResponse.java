package Models;

public class WordResponse
{
    private String word;
    private Integer score;

    public WordResponse() {
    }

    public WordResponse(String word, Integer score) {
        this.word = word;
        this.score = score;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
