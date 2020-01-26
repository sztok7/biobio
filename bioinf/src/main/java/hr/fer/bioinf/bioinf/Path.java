/**
 * 
 */
package hr.fer.bioinf.bioinf;

import java.util.ArrayList;

/**
 * @author filip
 *
 */
public class Path {
	int length;
	ArrayList<Integer> vertices = new ArrayList<Integer>(), extension_lengths = new ArrayList<Integer>();
	
	public Path() {
	}
	
	public Path (int length, ArrayList<Integer> vertices, ArrayList<Integer> extension_lengths) {
		this.length = length;
		this.vertices.addAll(vertices);
		this.extension_lengths.addAll(extension_lengths);
	}
}
