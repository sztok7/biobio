package hr.fer.bioinf.bioinf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;

import org.javatuples.Pair;

public class PathExtractor {
	final static int WINDOW_SIZE = 1000;
	
	public HashMap<Pair<Integer, Integer>, Path> extractPaths(HashMap<Pair<Integer, Integer>, ArrayList<Path>> allPaths) {
		HashMap<Pair<Integer, Integer>, Path> representative_paths = new HashMap<Pair<Integer,Integer>, Path>();
		
		for (Entry<Pair<Integer, Integer>, ArrayList<Path>> contigs_paths : allPaths.entrySet()) {
			Pair <Integer, Integer> between = contigs_paths.getKey();
			ArrayList<Path> paths = contigs_paths.getValue();
			Collections.sort(paths, pathComparator);
			
			int max_freq = 0;
			int max_freq_path_id = -1;
			int first = 0;
			for (int i = 0; i < paths.size(); i++) {
				while (first < i && paths.get(first).length + WINDOW_SIZE < paths.get(i).length) first++;
				int size = i - first +1;
				if (max_freq_path_id == -1 || size > max_freq) {
					max_freq_path_id = first;
					max_freq = size;
				}
			}

			representative_paths.put(between, paths.get(max_freq_path_id));
		}
		
		return representative_paths;
	}
	
	Comparator<Path> pathComparator = new Comparator<Path>() {
		public int compare(Path p1, Path p2) {
			int l1 = p1.length;
			int l2 = p2.length;
			if (l1 == l2) return 0;
			return l1 > l2 ? 1 : -1;
		}
	};
}
