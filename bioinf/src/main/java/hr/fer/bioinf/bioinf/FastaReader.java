package hr.fer.bioinf.bioinf;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.javatuples.Pair;

/**
 * @author filip
 *
 */
public class FastaReader {
	private HashMap<Integer, String> entries = new HashMap<Integer, String>();
	
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
				entries.put(id.hashCode()*2, seq);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * @returns - a collection of read sequences
	 */
	public HashMap<Integer, String> getEntries() {
		return entries;
	}
}
