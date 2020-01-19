package hr.fer.bioinf.bioinf;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.javatuples.Pair;

/**
 * @author filip
 *
 */
public class FastaReader {
	private ArrayList<Pair<Integer, String>> entries = new ArrayList<Pair<Integer, String>>();
	
	/**
	 * Initializes object with the fasta object
	 * @param path - path to fasta file
	 */
	public FastaReader(String path) {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(path));
			String id, seq;
			while (true) {
				id = reader.readLine();
				if (id == null) break;
				id = id.substring(1);
				seq = reader.readLine();
				entries.add(new Pair<Integer, String>(id.hashCode(), seq));
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * @returns - a collection of read sequences
	 */
	public ArrayList<Pair<Integer, String>> getEntries() {
		return entries;
	}
}
