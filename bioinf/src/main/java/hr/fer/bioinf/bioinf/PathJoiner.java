package hr.fer.bioinf.bioinf;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import org.javatuples.Pair;

public class PathJoiner {
	final static int OVERHANG_ADDITIONS_PERCENTAGE = 20;
	SeqHandler seq;
	HashMap<Pair<Integer, Integer>, Path> generated_paths = new HashMap<Pair<Integer,Integer>, Path>();
	ArrayList<Integer> contigs = new ArrayList<Integer>();
	
	HashSet<Integer> visited = new HashSet<Integer>();
	HashMap<Integer, Boolean> solved = new HashMap<Integer, Boolean>();
	ArrayList<Path> scaffolds = new ArrayList<Path>();
	int solution_included_contigs;
	Path solution = new Path();
	
	public PathJoiner(SeqHandler seq) {
		this.seq = seq;
	}
	
	public void constructSolution(HashMap<Pair<Integer, Integer>, Path> generated_paths) {
		this.generated_paths = generated_paths;
		
		contigs = seq.getAllByType(Type.CONTIG);
		Path best_scaffold = new Path(), path = new Path();
		int best_scaffold_included;
		while (true) {
			best_scaffold_included = -1;
			for (int contig : contigs) {
				if (solved.get(contig)) continue;
				
				solution_included_contigs = 0;
				path.length = seq.getLength(contig);
				path.vertices = new ArrayList<Integer>(Arrays.asList(contig));
				path.extension_lengths = new ArrayList<Integer>(Arrays.asList(path.length));
				
				dfsConnectContigs(path, contig, 1);
			}
			System.out.println("Constructed potential scaffold with length: " + solution.length + ", containing " + solution.vertices.size() + " vertices:");
			for (int v : solution.vertices) {
				if (seq.getType(v) == Type.CONTIG) System.out.print(v + " ");
			}
			System.out.print("\n");
			
			if (solution_included_contigs > best_scaffold_included) {
				best_scaffold_included = solution_included_contigs;
				best_scaffold = solution;
			}
			
			if (best_scaffold_included == -1) break;
			
			for (int visited_v : best_scaffold.vertices) {
				if (seq.getType(visited_v) == Type.CONTIG) {
					solved.replace(visited_v, true);
					solved.replace(visited_v^1,true);
				}
			}
			
			System.out.println("Constructed best scaffold with length: " + best_scaffold.length + ", containing:");
			for (int v : best_scaffold.vertices) {
				if (seq.getType(v) == Type.CONTIG) System.out.print(v + " ");
			}
			System.out.print("\n");
			scaffolds.add(best_scaffold);
		}
	}
	
	public void outputSolutions(String path) {
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(path));
			for (int i = 0; i < scaffolds.size(); i++) {
				writer.append(">" + i + "\n");
				writer.append(seq.reconstructPath(scaffolds.get(i)) + "\n");
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void dfsConnectContigs(Path path, int current, int included_contigs) {
		int added_length = 0;
		
		for (int i = 0; i < path.vertices.size(); i++) {
			if (seq.getType(path.vertices.get(i)) == Type.READ) {
				added_length += path.extension_lengths.get(i);
			}
		}
		if (added_length > OVERHANG_ADDITIONS_PERCENTAGE/100.0*path.length) return;
		
		visited.add(current);
		
		Path new_path = new Path();
		
		for (int next : contigs) {
			if (solved.get(next)) continue;
			if (visited.contains(next)) continue;
			if (visited.contains(next^1)) continue;
			
			if (!generated_paths.containsKey(new Pair<Integer, Integer>(current, next))) continue;
			new_path = path;
			new_path = mergePaths(new_path, generated_paths.get(new Pair<Integer, Integer>(current, next)));
			
			dfsConnectContigs(new_path, next, included_contigs + 1);
		}
		
		if ((solution_included_contigs < included_contigs) || ((solution_included_contigs == included_contigs) && (solution.length > path.length))) {
			solution = path;
			solution_included_contigs = included_contigs;
		}
	}

	private Path mergePaths(Path path, Path new_path) {
		path.length += new_path.length;
		for (int i = 1; i < new_path.vertices.size(); i++) {
			path.vertices.add(new_path.vertices.get(i));
			path.extension_lengths.add(new_path.extension_lengths.get(i));
		}
		path.length -= new_path.extension_lengths.get(0);
		return path;
	}
}
