package hr.fer.bioinf.bioinf;

import org.javatuples.Pair;

public class PafEntry {
	private int[] ids = new int[2], len = new int[2], start = new int[2], end = new int[2];
	private String orientation;
	private double similarity;

	public PafEntry(String[] ids, int[] len, int[] start, int[] end, String orientation, double similarity) {
		for (int i = 0; i < 2; i++) {
			this.ids[i] = ids[i].hashCode();
		}
		this.len = len;
		this.start = start;
		this.end = end;
		this.orientation = orientation;
		this.similarity = similarity;
	}

	public int[] getIds() {
		return ids;
	}

	public void setIds(int[] ids) {
		this.ids = ids;
	}

	public int[] getLen() {
		return len;
	}

	public void setLen(int[] len) {
		this.len = len;
	}

	public int[] getStart() {
		return start;
	}

	public void setStart(int[] start) {
		this.start = start;
	}

	public int[] getEnd() {
		return end;
	}

	public void setEnd(int[] end) {
		this.end = end;
	}

	public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	public void setSimilarity(double similarity) {
		this.similarity = similarity;
	}
	
	public double getSimilarity() {
		return similarity;
	}
	
	public Continuation getContinuation() {
		return orientation == "+" ? Continuation.NORMAL : Continuation.CONJUGATED;
	}
	
	public boolean valid() {
		return getRelations() != Relation.FIRST_CONTAIN && getRelations() != Relation.SECOND_CONTAIN;
	}

	private Relation getRelations() {
		if (start[0] <= start[1] && len[0] - end[0] <= len[1] - end[1]) return Relation.FIRST_CONTAIN;
		if (start[0] >= start[1] && len[0] - end[0] >= len[1] - end[1]) return Relation.SECOND_CONTAIN;
		if (start[0] > start[1]) return Relation.FTOS_OVER;
		return Relation.STOF_OVER;
	}

	public Pair<Integer, Integer> key() {
		return new Pair<Integer, Integer>(ids[0], ids[1]);
	}

}
