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

	public static Accuracy currentAccuracyLevel;

	public static Words currentWordsLevel;

	public static Time currentTimeLevel;

	public static Confidence currentConfidenceLevel;

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

	private void setAccuracyLevel(Accuracy accuracyLevel) {
		this.accuracyLevel = accuracyLevel;
	}

	private void setWordsLevel(Words wordsLevel) {
		this.wordsLevel = wordsLevel;
	}

	private void setTimeLevel(Time timeLevel) {
		this.timeLevel = timeLevel;
	}

	private void setConfidenceLevel(Confidence confidenceLevel) {
		this.confidenceLevel = confidenceLevel;
	}

}
