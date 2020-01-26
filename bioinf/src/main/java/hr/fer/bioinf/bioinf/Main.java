package hr.fer.bioinf.bioinf;

import java.util.ArrayList;
import java.util.HashMap;

import org.javatuples.Pair;

public class Main {

	public static void main(String[] args) {
		/*if (args.length != 4) {
			System.err.println("Invalid number of arguments.\n");
			System.err.println("<reads.fasta> <contigs.fasta> <overlaps_rr.paf> <overlaps_cr.paf>");
			return;
		}*/
		
		FastaReader rr = new FastaReader(/*args[0]*/"/home/filip/Desktop/biobioinf/EColisynthetic/ecoli_test_reads.fasta");
		System.out.println("Fasta1 finito");
		FastaReader cr = new FastaReader(/*args[1]*/"/home/filip/Desktop/biobioinf/EColisynthetic/ecoli_test_contigs.fasta");
		System.out.println("Fasta2 finito");
		PafReader rro = new PafReader(/*args[2]*/"/home/filip/Desktop/biobioinf/EColisynthetic/overlaps_rr.paf");
		System.out.println("Paf1 finito");
		PafReader cro = new PafReader(/*args[3]*/"/home/filip/Desktop/biobioinf/EColisynthetic/overlaps_cr.paf");
		System.out.println("Paf2 finito");
		
		SeqHandler seq = new SeqHandler();
		System.out.println("SeqHandler made");
		seq.addSequences(rr.getEntries(), Type.READ);
		System.out.println("SeqHan added reads");
		seq.addSequences(cr.getEntries(), Type.CONTIG);
		System.out.println("SeqHan added contigs");
		
		HashMap<Integer, ArrayList<Edge>> graph = new HashMap<Integer, ArrayList<Edge>>();
		graph = mergeGraphs(graph, rro.getGraph());
		graph = mergeGraphs(graph, cro.getGraph());
		
		System.out.println("Graph size: " + graph.size());
		
		DfsPathGenerator path_generator = new DfsPathGenerator(graph, seq);
		System.out.println("DfsPathGenerator made");
		HashMap<Pair<Integer, Integer>, ArrayList<Path>> paths = path_generator.generatePaths();
		
		System.out.println(paths.size());
		
		PathExtractor path_extractor = new PathExtractor();
		PathJoiner path_joiner = new PathJoiner(seq);
		path_joiner.constructSolution(path_extractor.extractPaths(paths));
		path_joiner.outputSolutions("/home/filip/Desktop/biobioinf/EColisynthetic/dna.fasta");
	}

	private static HashMap<Integer, ArrayList<Edge>> mergeGraphs(HashMap<Integer, ArrayList<Edge>> graph,
			HashMap<Integer, ArrayList<Edge>> graph2) {
		graph.putAll(graph2);
		return graph;
	}

}
