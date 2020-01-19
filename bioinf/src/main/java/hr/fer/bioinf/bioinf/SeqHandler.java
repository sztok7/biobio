package hr.fer.bioinf.bioinf;

import java.util.ArrayList;
import java.util.HashMap;

public class SeqHandler {
	private HashMap<Integer, String> entries = new HashMap<Integer, String>();
	private HashMap<Integer, Type> ids_type = new HashMap<Integer, Type>();

	public SeqHandler() {
		// TODO Auto-generated constructor stub
	}

	public void addSequences(HashMap<Integer, String> entries, Type type) {
		for (int key : entries.keySet()) {
			ids_type.put(key, type);
			this.entries.put(key, entries.get(key));
			
			int rev_id = reverseId(key);
			ids_type.put(rev_id, type);
			this.entries.put(rev_id, conjugateAndReverse(entries.get(key)));
		}
		
	}
	
	public Type getType(Integer id) {
		if (ids_type.containsKey(id)) return ids_type.get(id);
		System.err.println("No such id");
		return null;
	}
	
	public ArrayList<Integer> getAllByType(Type type) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i : ids_type.keySet()) {
			if (ids_type.get(i) == type) {
				list.add(i);
			}
		}
		return list;
	}
	
	public int getLength(int id) {
		return entries.get(id).length();
	}
	
	public String reconstructPath(Path path) {
		String dna = "", new_strain;
		for (int i = 0; i < path.vertices.size(); i++) {
			new_strain = entries.get(path.vertices.get(i));
			assert new_strain.length() >= path.extension_lengths.get(i) : "Can't reconstruct path";
			dna += new_strain.substring(new_strain.length() - path.extension_lengths.get(i));
		}
		return dna;
	}

	private String conjugateAndReverse(String strand) {
		String reverse = new StringBuilder(strand).reverse().toString();
		String new_strand = "";
		for (int i = 0; i < reverse.length(); i++) {
			switch (reverse.charAt(i)) {
				case 'A':
					new_strand += 'T';
					break;
				case 'T':
					new_strand += 'A';
					break;
				case 'G':
					new_strand += 'C';
					break;
				case 'C':
					new_strand += 'G';
					break;
				default:
					System.err.println(strand + " " + reverse.charAt(i));
			}
		}
		return new_strand;
		
	}

	private int reverseId(int id) {
		return id ^ 1;
	}


}
