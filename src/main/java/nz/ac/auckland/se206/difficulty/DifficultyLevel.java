package nz.ac.auckland.se206.difficulty;

public class DifficultyLevel {

	public enum Accuracy {
		E, M, H
	}

	public enum Words {
		E, M, H
	}

	public enum Time {
		E, M, H
	}

	public enum Confidence {
		E, M, H
	}

	private Accuracy accuracyLevel;

	private Words wordsLevel;

	private Time timeLevel;

	private Confidence confidenceLevel;

	public DifficultyLevel() {
		this.accuracyLevel = Accuracy.E;
		this.wordsLevel = Words.E;
		this.timeLevel = Time.E;
		this.confidenceLevel = Confidence.E;
	}

	public Accuracy getAccuracyLevel() {
		return this.accuracyLevel;
	}

	public Words getWordsLevel() {
		return this.wordsLevel;
	}

	public Time getTimeLevel() {
		return this.timeLevel;
	}

	public Confidence getConfidenceLevel() {
		return this.confidenceLevel;
	}

	public void setAccuracyLevel(Accuracy accuracyLevel) {
		this.accuracyLevel = accuracyLevel;
	}

	public void setWordsLevel(Words wordsLevel) {
		this.wordsLevel = wordsLevel;
	}

	public void setTimeLevel(Time timeLevel) {
		this.timeLevel = timeLevel;
	}

	public void setConfidenceLevel(Confidence confidenceLevel) {
		this.confidenceLevel = confidenceLevel;
	}

}
