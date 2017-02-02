import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

/**
 * JUnit test cases for the graph algorithms assignment.
 *
 * @version 2
 * @author Timothy J. Aveni
 */

public class Testtt {

	@Rule
	public Timeout globalTimeout = new Timeout(2000L, TimeUnit.MILLISECONDS);
	
	final PrintStream originalSystemOut = System.out;
	CleanOutputStream cleanOutputStream;
	
	private class CleanOutputStream extends OutputStream {

		private boolean clean = true;
		
		@Override
		public void write(int b) throws IOException {
			clean = false;
			originalSystemOut.write(b);
		}
		
		public boolean isClean() {
			return clean;
		}
	}
	
	private void assertException(String message, Class<? extends Exception> exceptionClass, Runnable code) {
		assertException(message, new Class[]{exceptionClass}, code);
	}
	
	private void assertException(String message, Class<? extends Exception>[] exceptionClasses, Runnable code) {
		try {
			code.run();
			Assert.fail(message);
		} catch (Exception e) {
			boolean foundException = false;
			for (Class<? extends Exception> exceptionClass: exceptionClasses) {
				if (exceptionClass.equals(e.getClass())) {
					foundException = true;
				}
			}
			
			if (!foundException) {
				e.printStackTrace();
				Assert.fail(message);
			} else {
				  assertNotNull(
						  "Exception messages must not be empty",
						  e.getMessage());
				  assertNotEquals(
						  "Exception messages must not be empty",
						  "",
						  e.getMessage());
			}
		}
	}
	
	@Before
	public void init() {
		cleanOutputStream = new CleanOutputStream();
		System.setOut(new PrintStream(cleanOutputStream));
	}
	
	@After
	public void checkOutput() {
		assertTrue(
				"You used print statements somewhere in your code. That's forbidden!", 
				cleanOutputStream.isClean());
	}
	
	@Test
	public void bfsExceptions() {
		assertException(
				"Calling BFS with a null vertex should throw an IllegalArgumentException",
				IllegalArgumentException.class,
				() -> GraphAlgorithms.breadthFirstSearch(null, new Graph<String>(new LinkedHashSet<Edge<String>>())));

		assertException(
				"Calling BFS with a null graph should throw an IllegalArgumentException",
				IllegalArgumentException.class,
				() -> GraphAlgorithms.breadthFirstSearch(new Vertex<String>(""), null));
		
		Vertex<String> a = new Vertex<String>("a");
		Vertex<String> b = new Vertex<String>("b");
		
		Vertex<String> c = new Vertex<String>("c");
		Vertex<String> d = new Vertex<String>("d");
		
		Vertex<String> e = new Vertex<String>("e");
		
		Edge<String> ab = new Edge<String>(a, b, 4, true);
		Edge<String> cd = new Edge<String>(c, d, 4, true);
		
		LinkedHashSet<Edge<String>> edgeSet = new LinkedHashSet<Edge<String>>();
		edgeSet.add(ab);
		edgeSet.add(cd);
		
		Graph<String> graph = new Graph<String>(edgeSet);
		
		assertException(
				"Calling BFS with a vertex not in the graph should throw an IllegalArgumentException",
				IllegalArgumentException.class,
				() -> GraphAlgorithms.breadthFirstSearch(e, graph));
	}
	
	@Test
	public void bfsUndirectedNoCycles() {
		Vertex<String> a = new Vertex<String>("a");
		Vertex<String> b = new Vertex<String>("b");
		Vertex<String> c = new Vertex<String>("c");
		Vertex<String> d = new Vertex<String>("d");
		
		Edge<String> ab = new Edge<String>(a, b, 4, false);
		Edge<String> bc = new Edge<String>(b, c, 4, false);
		Edge<String> cd = new Edge<String>(c, d, 4, false);
		
		LinkedHashSet<Edge<String>> edgeSet = new LinkedHashSet<Edge<String>>();
		edgeSet.add(ab);
		edgeSet.add(bc);
		edgeSet.add(cd);
		
		Graph<String> graph = new Graph<String>(edgeSet);
		
		List<Vertex<String>> expectedStartAtA = new LinkedList<Vertex<String>>();
		expectedStartAtA.add(a);
		expectedStartAtA.add(b);
		expectedStartAtA.add(c);
		expectedStartAtA.add(d);
		
		List<Vertex<String>> actualStartAtA = GraphAlgorithms.breadthFirstSearch(a, graph);
		assertEquals(
				"BFS failed in an undirected straight-line graph",
				expectedStartAtA,
				actualStartAtA);
		
		List<Vertex<String>> expectedStartAtB = new LinkedList<Vertex<String>>();
		expectedStartAtB.add(b);
		expectedStartAtB.add(a);
		expectedStartAtB.add(c);
		expectedStartAtB.add(d);
		
		List<Vertex<String>> actualStartAtB = GraphAlgorithms.breadthFirstSearch(b, graph);
		assertEquals(
				"BFS failed in an undirected straight-line graph",
				expectedStartAtB,
				actualStartAtB);
		
		List<Vertex<String>> expectedStartAtC = new LinkedList<Vertex<String>>();
		expectedStartAtC.add(c);
		expectedStartAtC.add(b);
		expectedStartAtC.add(d);
		expectedStartAtC.add(a);
		
		List<Vertex<String>> actualStartAtC = GraphAlgorithms.breadthFirstSearch(c, graph);
		assertEquals(
				"BFS failed in an undirected straight-line graph",
				expectedStartAtC,
				actualStartAtC);
		
		List<Vertex<String>> expectedStartAtD = new LinkedList<Vertex<String>>();
		expectedStartAtD.add(d);
		expectedStartAtD.add(c);
		expectedStartAtD.add(b);
		expectedStartAtD.add(a);
		
		List<Vertex<String>> actualStartAtD = GraphAlgorithms.breadthFirstSearch(d, graph);
		assertEquals(
				"BFS failed in an undirected straight-line graph",
				expectedStartAtD,
				actualStartAtD);
	}
	
	@Test
	public void bfsUndirectedWithCycle() {
		Vertex<String> a = new Vertex<String>("a");
		Vertex<String> b = new Vertex<String>("b");
		Vertex<String> c = new Vertex<String>("c");
		Vertex<String> d = new Vertex<String>("d");
		
		Edge<String> ab = new Edge<String>(a, b, 4, false);
		Edge<String> bc = new Edge<String>(b, c, 4, false);
		Edge<String> cd = new Edge<String>(c, d, 4, false);
		Edge<String> ad = new Edge<String>(a, d, 4, false);
		
		LinkedHashSet<Edge<String>> edgeSet = new LinkedHashSet<Edge<String>>();
		edgeSet.add(ab);
		edgeSet.add(bc);
		edgeSet.add(cd);
		edgeSet.add(ad);
		
		Graph<String> graph = new Graph<String>(edgeSet);
		
		List<Vertex<String>> expectedStartAtA = new LinkedList<Vertex<String>>();
		expectedStartAtA.add(a);
		expectedStartAtA.add(b);
		expectedStartAtA.add(d);
		expectedStartAtA.add(c);
		
		List<Vertex<String>> actualStartAtA = GraphAlgorithms.breadthFirstSearch(a, graph);
		assertEquals(
				"BFS failed in an undirected graph with a cycle",
				expectedStartAtA,
				actualStartAtA);
		
		List<Vertex<String>> expectedStartAtB = new LinkedList<Vertex<String>>();
		expectedStartAtB.add(b);
		expectedStartAtB.add(a);
		expectedStartAtB.add(c);
		expectedStartAtB.add(d);
		
		List<Vertex<String>> actualStartAtB = GraphAlgorithms.breadthFirstSearch(b, graph);
		assertEquals(
				"BFS failed in an undirected graph with a cycle",
				expectedStartAtB,
				actualStartAtB);
		
		List<Vertex<String>> expectedStartAtC = new LinkedList<Vertex<String>>();
		expectedStartAtC.add(c);
		expectedStartAtC.add(b);
		expectedStartAtC.add(d);
		expectedStartAtC.add(a);
		
		List<Vertex<String>> actualStartAtC = GraphAlgorithms.breadthFirstSearch(c, graph);
		assertEquals(
				"BFS failed in an undirected graph with a cycle",
				expectedStartAtC,
				actualStartAtC);
		
		List<Vertex<String>> expectedStartAtD = new LinkedList<Vertex<String>>();
		expectedStartAtD.add(d);
		expectedStartAtD.add(c);
		expectedStartAtD.add(a);
		expectedStartAtD.add(b);
		
		List<Vertex<String>> actualStartAtD = GraphAlgorithms.breadthFirstSearch(d, graph);
		assertEquals(
				"BFS failed in an undirected graph with a cycle",
				expectedStartAtD,
				actualStartAtD);
	}
	
	@Test
	public void bfsDisconnected() {
		Vertex<String> a = new Vertex<String>("a");
		Vertex<String> b = new Vertex<String>("b");
		Vertex<String> c = new Vertex<String>("c");
		Vertex<String> d = new Vertex<String>("d");
		
		Edge<String> ab = new Edge<String>(a, b, 4, false);
		Edge<String> cd = new Edge<String>(c, d, 4, false);
		
		LinkedHashSet<Edge<String>> edgeSet = new LinkedHashSet<Edge<String>>();
		edgeSet.add(ab);
		edgeSet.add(cd);
		
		Graph<String> graph = new Graph<String>(edgeSet);
		
		List<Vertex<String>> expected = new LinkedList<Vertex<String>>();
		expected.add(a);
		expected.add(b);
		
		List<Vertex<String>> actual = GraphAlgorithms.breadthFirstSearch(a, graph);
		assertEquals(
				"BFS failed when the graph was not connected",
				expected,
				actual);   	
	}
	
	@Test
	public void bfsDirectedPartiallyDisconnected() {
		Vertex<String> a = new Vertex<String>("a");
		Vertex<String> b = new Vertex<String>("b");
		Vertex<String> c = new Vertex<String>("c");
		Vertex<String> d = new Vertex<String>("d");
		Vertex<String> e = new Vertex<String>("e");
		Vertex<String> f = new Vertex<String>("f");
		
		Edge<String> ab = new Edge<String>(a, b, 0, true);
		Edge<String> bc = new Edge<String>(b, c, 0, true);
		Edge<String> ca = new Edge<String>(c, a, 0, true);
		
		Edge<String> df = new Edge<String>(d, f, 0, true);
		Edge<String> fe = new Edge<String>(f, e, 0, true);
		Edge<String> ed = new Edge<String>(e, d, 0, true);
		
		Edge<String> bd = new Edge<String>(b, d, 0, true);
		Edge<String> ce = new Edge<String>(c, e, 0, true);
		
		LinkedHashSet<Edge<String>> edgeSet = new LinkedHashSet<Edge<String>>();
		edgeSet.add(ab);
		edgeSet.add(bc);
		edgeSet.add(ca);
		edgeSet.add(df);
		edgeSet.add(fe);
		edgeSet.add(ed);
		edgeSet.add(bd);
		edgeSet.add(ce);
		
		Graph<String> graph = new Graph<String>(edgeSet);
		
		List<Vertex<String>> expectedStartAtA = new LinkedList<Vertex<String>>();
		expectedStartAtA.add(a);
		expectedStartAtA.add(b);
		expectedStartAtA.add(c);
		expectedStartAtA.add(d);
		expectedStartAtA.add(e);
		expectedStartAtA.add(f);
		
		List<Vertex<String>> actualStartAtA = GraphAlgorithms.breadthFirstSearch(a, graph);
		assertEquals(
				"BFS failed on a directed graph",
				expectedStartAtA,
				actualStartAtA);
		
		List<Vertex<String>> expectedStartAtF = new LinkedList<Vertex<String>>();
		expectedStartAtF.add(f);
		expectedStartAtF.add(e);
		expectedStartAtF.add(d);
		
		List<Vertex<String>> actualStartAtF = GraphAlgorithms.breadthFirstSearch(f, graph);
		assertEquals(
				"BFS failed on a directed graph",
				expectedStartAtF,
				actualStartAtF);
	}
	
	@Test
	public void dfsExceptions() {
		assertException(
				"Calling DFS with a null vertex should throw an IllegalArgumentException",
				IllegalArgumentException.class,
				() -> GraphAlgorithms.depthFirstSearch(null, new Graph<String>(new LinkedHashSet<Edge<String>>())));

		assertException(
				"Calling DFS with a null graph should throw an IllegalArgumentException",
				IllegalArgumentException.class,
				() -> GraphAlgorithms.depthFirstSearch(new Vertex<String>(""), null));
		
		
		Vertex<String> a = new Vertex<String>("a");
		Vertex<String> b = new Vertex<String>("b");
		
		Vertex<String> c = new Vertex<String>("c");
		Vertex<String> d = new Vertex<String>("d");
		
		Vertex<String> e = new Vertex<String>("e");
		
		Edge<String> ab = new Edge<String>(a, b, 4, true);
		Edge<String> cd = new Edge<String>(c, d, 4, true);
		
		LinkedHashSet<Edge<String>> edgeSet = new LinkedHashSet<Edge<String>>();
		edgeSet.add(ab);
		edgeSet.add(cd);
		
		Graph<String> graph = new Graph<String>(edgeSet);
		
		assertException(
				"Calling DFS with a vertex not in the graph should throw an IllegalArgumentException",
				IllegalArgumentException.class,
				() -> GraphAlgorithms.depthFirstSearch(e, graph));
	}
	
	@Test
	public void dfsUndirectedNoCycles() {
		Vertex<String> a = new Vertex<String>("a");
		Vertex<String> b = new Vertex<String>("b");
		Vertex<String> c = new Vertex<String>("c");
		Vertex<String> d = new Vertex<String>("d");
		
		Edge<String> ab = new Edge<String>(a, b, 4, false);
		Edge<String> bc = new Edge<String>(b, c, 4, false);
		Edge<String> cd = new Edge<String>(c, d, 4, false);
		
		LinkedHashSet<Edge<String>> edgeSet = new LinkedHashSet<Edge<String>>();
		edgeSet.add(ab);
		edgeSet.add(bc);
		edgeSet.add(cd);
		
		Graph<String> graph = new Graph<String>(edgeSet);
		
		List<Vertex<String>> expectedStartAtA = new LinkedList<Vertex<String>>();
		expectedStartAtA.add(a);
		expectedStartAtA.add(b);
		expectedStartAtA.add(c);
		expectedStartAtA.add(d);
		
		List<Vertex<String>> actualStartAtA = GraphAlgorithms.depthFirstSearch(a, graph);
		assertEquals(
				"DFS failed in an undirected straight-line graph",
				expectedStartAtA,
				actualStartAtA);
		
		List<Vertex<String>> expectedStartAtB = new LinkedList<Vertex<String>>();
		expectedStartAtB.add(b);
		expectedStartAtB.add(a);
		expectedStartAtB.add(c);
		expectedStartAtB.add(d);
		
		List<Vertex<String>> actualStartAtB = GraphAlgorithms.depthFirstSearch(b, graph);
		assertEquals(
				"DFS failed in an undirected straight-line graph",
				expectedStartAtB,
				actualStartAtB);
		
		List<Vertex<String>> expectedStartAtC = new LinkedList<Vertex<String>>();
		expectedStartAtC.add(c);
		expectedStartAtC.add(b);
		expectedStartAtC.add(a);
		expectedStartAtC.add(d);
		
		List<Vertex<String>> actualStartAtC = GraphAlgorithms.depthFirstSearch(c, graph);
		assertEquals(
				"DFS failed in an undirected straight-line graph",
				expectedStartAtC,
				actualStartAtC);
		
		List<Vertex<String>> expectedStartAtD = new LinkedList<Vertex<String>>();
		expectedStartAtD.add(d);
		expectedStartAtD.add(c);
		expectedStartAtD.add(b);
		expectedStartAtD.add(a);
		
		List<Vertex<String>> actualStartAtD = GraphAlgorithms.depthFirstSearch(d, graph);
		assertEquals(
				"DFS failed in an undirected straight-line graph",
				expectedStartAtD,
				actualStartAtD);
	}
	
	@Test
	public void dfsUndirectedWithCycle() {
		Vertex<String> a = new Vertex<String>("a");
		Vertex<String> b = new Vertex<String>("b");
		Vertex<String> c = new Vertex<String>("c");
		Vertex<String> d = new Vertex<String>("d");
		
		Edge<String> ab = new Edge<String>(a, b, 4, false);
		Edge<String> bc = new Edge<String>(b, c, 4, false);
		Edge<String> cd = new Edge<String>(c, d, 4, false);
		Edge<String> ad = new Edge<String>(a, d, 4, false);
		
		LinkedHashSet<Edge<String>> edgeSet = new LinkedHashSet<Edge<String>>();
		edgeSet.add(ab);
		edgeSet.add(bc);
		edgeSet.add(cd);
		edgeSet.add(ad);
		
		Graph<String> graph = new Graph<String>(edgeSet);
		
		List<Vertex<String>> expectedStartAtA = new LinkedList<Vertex<String>>();
		expectedStartAtA.add(a);
		expectedStartAtA.add(b);
		expectedStartAtA.add(c);
		expectedStartAtA.add(d);
		
		List<Vertex<String>> actualStartAtA = GraphAlgorithms.depthFirstSearch(a, graph);
		assertEquals(
				"DFS failed in an undirected graph with a cycle",
				expectedStartAtA,
				actualStartAtA);
		
		List<Vertex<String>> expectedStartAtB = new LinkedList<Vertex<String>>();
		expectedStartAtB.add(b);
		expectedStartAtB.add(a);
		expectedStartAtB.add(d);
		expectedStartAtB.add(c);
		
		List<Vertex<String>> actualStartAtB = GraphAlgorithms.depthFirstSearch(b, graph);
		assertEquals(
				"DFS failed in an undirected graph with a cycle",
				expectedStartAtB,
				actualStartAtB);
		
		List<Vertex<String>> expectedStartAtC = new LinkedList<Vertex<String>>();
		expectedStartAtC.add(c);
		expectedStartAtC.add(b);
		expectedStartAtC.add(a);
		expectedStartAtC.add(d);
		
		List<Vertex<String>> actualStartAtC = GraphAlgorithms.depthFirstSearch(c, graph);
		assertEquals(
				"DFS failed in an undirected graph with a cycle",
				expectedStartAtC,
				actualStartAtC);
		
		List<Vertex<String>> expectedStartAtD = new LinkedList<Vertex<String>>();
		expectedStartAtD.add(d);
		expectedStartAtD.add(c);
		expectedStartAtD.add(b);
		expectedStartAtD.add(a);
		
		List<Vertex<String>> actualStartAtD = GraphAlgorithms.depthFirstSearch(d, graph);
		assertEquals(
				"DFS failed in an undirected graph with a cycle",
				expectedStartAtD,
				actualStartAtD);
	}
	

	
	@Test
	public void dfsDisconnected() {
		Vertex<String> a = new Vertex<String>("a");
		Vertex<String> b = new Vertex<String>("b");
		Vertex<String> c = new Vertex<String>("c");
		Vertex<String> d = new Vertex<String>("d");
		
		Edge<String> ab = new Edge<String>(a, b, 4, false);
		Edge<String> cd = new Edge<String>(c, d, 4, false);
		
		LinkedHashSet<Edge<String>> edgeSet = new LinkedHashSet<Edge<String>>();
		edgeSet.add(ab);
		edgeSet.add(cd);
		
		Graph<String> graph = new Graph<String>(edgeSet);
		
		List<Vertex<String>> expected = new LinkedList<Vertex<String>>();
		expected.add(a);
		expected.add(b);
		
		List<Vertex<String>> actual = GraphAlgorithms.depthFirstSearch(a, graph);
		assertEquals(
				"DFS failed when the graph was not connected",
				expected,
				actual);
	}
	
	@Test
	public void dfsDirectedPartiallyDisconnected() {
		Vertex<String> a = new Vertex<String>("a");
		Vertex<String> b = new Vertex<String>("b");
		Vertex<String> c = new Vertex<String>("c");
		Vertex<String> d = new Vertex<String>("d");
		Vertex<String> e = new Vertex<String>("e");
		Vertex<String> f = new Vertex<String>("f");
		
		Edge<String> ab = new Edge<String>(a, b, 0, true);
		Edge<String> bc = new Edge<String>(b, c, 0, true);
		Edge<String> ca = new Edge<String>(c, a, 0, true);
		
		Edge<String> df = new Edge<String>(d, f, 0, true);
		Edge<String> fe = new Edge<String>(f, e, 0, true);
		Edge<String> ed = new Edge<String>(e, d, 0, true);
		
		Edge<String> bd = new Edge<String>(b, d, 0, true);
		Edge<String> ce = new Edge<String>(c, e, 0, true);
		
		LinkedHashSet<Edge<String>> edgeSet = new LinkedHashSet<Edge<String>>();
		edgeSet.add(ab);
		edgeSet.add(bc);
		edgeSet.add(ca);
		edgeSet.add(df);
		edgeSet.add(fe);
		edgeSet.add(ed);
		edgeSet.add(bd);
		edgeSet.add(ce);
		
		Graph<String> graph = new Graph<String>(edgeSet);
		
		List<Vertex<String>> expectedStartAtA = new LinkedList<Vertex<String>>();
		expectedStartAtA.add(a);
		expectedStartAtA.add(b);
		expectedStartAtA.add(c);
		expectedStartAtA.add(e);
		expectedStartAtA.add(d);
		expectedStartAtA.add(f);
		
		List<Vertex<String>> actualStartAtA = GraphAlgorithms.depthFirstSearch(a, graph);
		assertEquals(
				"DFS failed on a directed graph",
				expectedStartAtA,
				actualStartAtA);
		
		List<Vertex<String>> expectedStartAtF = new LinkedList<Vertex<String>>();
		expectedStartAtF.add(f);
		expectedStartAtF.add(e);
		expectedStartAtF.add(d);
		
		List<Vertex<String>> actualStartAtF = GraphAlgorithms.depthFirstSearch(f, graph);
		assertEquals(
				"DFS failed on a directed graph",
				expectedStartAtF,
				actualStartAtF);
	}
	
	@Test
	public void dijkstraExceptions() {
		assertException(
				"Calling Dijkstra's with a null vertex should throw an IllegalArgumentException",
				IllegalArgumentException.class,
				() -> GraphAlgorithms.dijkstras(null, new Graph<String>(new LinkedHashSet<Edge<String>>())));

		assertException(
				"Calling Dijkstra's with a null graph should throw an IllegalArgumentException",
				IllegalArgumentException.class,
				() -> GraphAlgorithms.dijkstras(new Vertex<String>(""), null));
		
		
		Vertex<String> a = new Vertex<String>("a");
		Vertex<String> b = new Vertex<String>("b");
		
		Vertex<String> c = new Vertex<String>("c");
		Vertex<String> d = new Vertex<String>("d");
		
		Vertex<String> e = new Vertex<String>("e");
		
		Edge<String> ab = new Edge<String>(a, b, 4, true);
		Edge<String> cd = new Edge<String>(c, d, 4, true);
		
		LinkedHashSet<Edge<String>> edgeSet = new LinkedHashSet<Edge<String>>();
		edgeSet.add(ab);
		edgeSet.add(cd);
		
		Graph<String> graph = new Graph<String>(edgeSet);
		
		assertException(
				"Calling Dijkstra's with a vertex not in the graph should throw an IllegalArgumentException",
				IllegalArgumentException.class,
				() -> GraphAlgorithms.dijkstras(e, graph));
	}
	
	@Test
	public void dijkstraSimpleUndirected() {
		Vertex<String> a = new Vertex<String>("a");
		Vertex<String> b = new Vertex<String>("b");
		Vertex<String> c = new Vertex<String>("c");
		
		Vertex<String> d = new Vertex<String>("d");
		Vertex<String> e = new Vertex<String>("e");
		
		Edge<String> ab = new Edge<String>(a, b, 2, false);
		Edge<String> bc = new Edge<String>(b, c, 2, false);
		Edge<String> ca = new Edge<String>(c, a, 3, false);
		Edge<String> de = new Edge<String>(d, e, 4, false);
		
		LinkedHashSet<Edge<String>> edgeSet = new LinkedHashSet<Edge<String>>();
		edgeSet.add(ab);
		edgeSet.add(bc);
		edgeSet.add(ca);
		edgeSet.add(de);
		
		Graph<String> graph = new Graph<String>(edgeSet);
		
		Map<Vertex<String>, Integer> expectedStartAtA = new HashMap<Vertex<String>, Integer>();
		expectedStartAtA.put(a, 0);
		expectedStartAtA.put(b, 2);
		expectedStartAtA.put(c, 3);
		expectedStartAtA.put(d, Integer.MAX_VALUE);
		expectedStartAtA.put(e, Integer.MAX_VALUE);
		
		Map<Vertex<String>, Integer> actualStartAtA = GraphAlgorithms.dijkstras(a, graph);
		
		assertEquals(
				"A simple run of Dijkstra's failed on an undirected graph",
				expectedStartAtA,
				actualStartAtA);
		
		Map<Vertex<String>, Integer> expectedStartAtD = new HashMap<Vertex<String>, Integer>();
		expectedStartAtD.put(a, Integer.MAX_VALUE);
		expectedStartAtD.put(b, Integer.MAX_VALUE);
		expectedStartAtD.put(c, Integer.MAX_VALUE);
		expectedStartAtD.put(d, 0);
		expectedStartAtD.put(e, 4);
		
		Map<Vertex<String>, Integer> actualStartAtD = GraphAlgorithms.dijkstras(d, graph);
		
		assertEquals(
				"A simple run of Dijkstra's failed on an undirected graph",
				expectedStartAtD,
				actualStartAtD);
	}
	
	@Test
	public void dijkstrasDirectedPartiallyDisconnected() {
		Vertex<String> a = new Vertex<String>("a");
		Vertex<String> b = new Vertex<String>("b");
		Vertex<String> c = new Vertex<String>("c");
		Vertex<String> d = new Vertex<String>("d");
		Vertex<String> e = new Vertex<String>("e");
		Vertex<String> f = new Vertex<String>("f");
		
		Edge<String> ab = new Edge<String>(a, b, 1, true);
		Edge<String> bc = new Edge<String>(b, c, 2, true);
		Edge<String> ca = new Edge<String>(c, a, 2, true);
		
		Edge<String> df = new Edge<String>(d, f, 3, true);
		Edge<String> fe = new Edge<String>(f, e, 0, true);
		Edge<String> ed = new Edge<String>(e, d, 4, true);
		
		Edge<String> bd = new Edge<String>(b, d, 9, true);
		Edge<String> ce = new Edge<String>(c, e, 2, true);
		
		LinkedHashSet<Edge<String>> edgeSet = new LinkedHashSet<Edge<String>>();
		edgeSet.add(ab);
		edgeSet.add(bc);
		edgeSet.add(ca);
		edgeSet.add(df);
		edgeSet.add(fe);
		edgeSet.add(ed);
		edgeSet.add(bd);
		edgeSet.add(ce);
		
		Graph<String> graph = new Graph<String>(edgeSet);
		
		Map<Vertex<String>, Integer> expectedStartAtA = new HashMap<Vertex<String>, Integer>();
		expectedStartAtA.put(a, 0);
		expectedStartAtA.put(b, 1);
		expectedStartAtA.put(c, 3);
		expectedStartAtA.put(e, 5);
		expectedStartAtA.put(d, 9);
		expectedStartAtA.put(f, 12);
		
		Map<Vertex<String>, Integer> actualStartAtA = GraphAlgorithms.dijkstras(a, graph);
		assertEquals(
				"Dijkstra's failed on a directed graph",
				expectedStartAtA,
				actualStartAtA);
		
		Map<Vertex<String>, Integer> expectedStartAtF = new HashMap<Vertex<String>, Integer>();
		expectedStartAtF.put(f, 0);
		expectedStartAtF.put(e, 0);
		expectedStartAtF.put(d, 4);
		expectedStartAtF.put(a, Integer.MAX_VALUE);
		expectedStartAtF.put(b, Integer.MAX_VALUE);
		expectedStartAtF.put(c, Integer.MAX_VALUE);
		
		Map<Vertex<String>, Integer> actualStartAtF = GraphAlgorithms.dijkstras(f, graph);
		assertEquals(
				"Dijkstra's failed on a directed graph",
				expectedStartAtF,
				actualStartAtF);
	}
	
	@Test
	public void kruskalExceptions() {
		assertException(
				"Calling Kruskal's with a null graph should throw an IllegalArgumentException",
				IllegalArgumentException.class,
				() -> GraphAlgorithms.kruskals(null));
	}
	
	@Test
	public void simpleMST() {
		Vertex<String> a = new Vertex<String>("a");
		Vertex<String> b = new Vertex<String>("b");
		
		Edge<String> ab = new Edge<String>(a, b, 4, false);
		
		LinkedHashSet<Edge<String>> edgeSet = new LinkedHashSet<Edge<String>>();
		edgeSet.add(ab);
		
		Graph<String> graph = new Graph<String>(edgeSet);
		
		Set<Edge<String>> mst = GraphAlgorithms.kruskals(graph);
		
		assertEquals(
				"A simple application of Kruskal's algorithm failed",
				edgeSet,
				mst);
	}
	
	@Test
	public void simpleMST2() {
		Vertex<String> a = new Vertex<String>("a");
		Vertex<String> b = new Vertex<String>("b");
		Vertex<String> c = new Vertex<String>("c");

		Edge<String> ab = new Edge<String>(a, b, 4, false);
		Edge<String> bc = new Edge<String>(b, c, 3, false);
		Edge<String> ac = new Edge<String>(a, c, 2, false);
		
		LinkedHashSet<Edge<String>> edgeSet = new LinkedHashSet<Edge<String>>();
		edgeSet.add(ab);
		edgeSet.add(bc);
		edgeSet.add(ac);
		
		Set<Edge<String>> expected = new HashSet<Edge<String>>();
		expected.add(bc);
		expected.add(ac);
		
		Graph<String> graph = new Graph<String>(edgeSet);
		
		Set<Edge<String>> mst = GraphAlgorithms.kruskals(graph);
		
		assertEquals(
				"A simple application of Kruskal's algorithm failed",
				expected,
				mst);
	}
	
	@Test
	public void simpleMST3() {
		Vertex<String> a = new Vertex<String>("a");
		Vertex<String> b = new Vertex<String>("b");
		Vertex<String> c = new Vertex<String>("c");

		Edge<String> ab = new Edge<String>(a, b, 4, false);
		Edge<String> bc = new Edge<String>(b, c, 3, false);
		
		LinkedHashSet<Edge<String>> edgeSet = new LinkedHashSet<Edge<String>>();
		edgeSet.add(ab);
		edgeSet.add(bc);
		
		Graph<String> graph = new Graph<String>(edgeSet);
		
		Set<Edge<String>> mst = GraphAlgorithms.kruskals(graph);
		
		assertEquals(
				"A simple application of Kruskal's algorithm failed",
				edgeSet,
				mst);
	}
	
	@Test
	public void noMST() {
		Vertex<String> a = new Vertex<String>("a");
		Vertex<String> b = new Vertex<String>("b");
		
		Vertex<String> c = new Vertex<String>("c");
		Vertex<String> d = new Vertex<String>("d");
		
		Edge<String> ab = new Edge<String>(a, b, 4, false);
		Edge<String> cd = new Edge<String>(c, d, 4, false);
		
		LinkedHashSet<Edge<String>> edgeSet = new LinkedHashSet<Edge<String>>();
		edgeSet.add(ab);
		edgeSet.add(cd);
		
		Graph<String> graph = new Graph<String>(edgeSet);

		Set<Edge<String>> mst = GraphAlgorithms.kruskals(graph);
		
		assertEquals(
				"When no MST exists, Kruskal's should return null",
				null,
				mst);
	}
	
	@Test
	public void noMSTLoop() {
		Vertex<String> a = new Vertex<String>("a");
		Vertex<String> b = new Vertex<String>("b");
		
		Vertex<String> c = new Vertex<String>("c");
		
		Edge<String> ab = new Edge<String>(a, b, 4, false);
		Edge<String> cc = new Edge<String>(c, c, 4, false);
		
		LinkedHashSet<Edge<String>> edgeSet = new LinkedHashSet<Edge<String>>();
		edgeSet.add(ab);
		edgeSet.add(cc);
		
		Graph<String> graph = new Graph<String>(edgeSet);

		Set<Edge<String>> mst = GraphAlgorithms.kruskals(graph);
		
		assertEquals(
				"When no MST exists, Kruskal's should return null",
				null,
				mst);
	}
	
	@Test
	public void noMSTSingleLoop() {
		Vertex<String> a = new Vertex<String>("a");
		
		Edge<String> aa = new Edge<String>(a, a, 4, false);
		
		LinkedHashSet<Edge<String>> edgeSet = new LinkedHashSet<Edge<String>>();
		edgeSet.add(aa);
		
		Graph<String> graph = new Graph<String>(edgeSet);

		Set<Edge<String>> mst = GraphAlgorithms.kruskals(graph);
		
		assertEquals(
				"When no MST exists (because there is only one vertex, and it has a self-loop), Kruskal's should return the empty set",
				new HashSet<Edge<String>>(),
				mst);
	}
	
	@Test
	public void biggerMST() {
		Vertex<String> a = new Vertex<String>("a");
		Vertex<String> b = new Vertex<String>("b");
		
		Vertex<String> c = new Vertex<String>("c");
		Vertex<String> d = new Vertex<String>("d");

		Edge<String> ab = new Edge<String>(a, b, -3, false);
		Edge<String> bc = new Edge<String>(b, c, 8, false);
		Edge<String> cd = new Edge<String>(c, d, 3, false);
		Edge<String> ad = new Edge<String>(d, a, 6, false);
		Edge<String> bd = new Edge<String>(b, d, 3, false);
		Edge<String> aa = new Edge<String>(a, a, -5, false);
		
		LinkedHashSet<Edge<String>> edgeSet = new LinkedHashSet<Edge<String>>();
		edgeSet.add(ab);
		edgeSet.add(bc);
		edgeSet.add(cd);
		edgeSet.add(ad);
		edgeSet.add(bd);
		edgeSet.add(aa);
		
		Graph<String> graph = new Graph<String>(edgeSet);
		
		Set<Edge<String>> expected = new HashSet<Edge<String>>();
		expected.add(ab);
		expected.add(cd);
		expected.add(bd);
		
		Set<Edge<String>> mst = GraphAlgorithms.kruskals(graph);
		
		assertEquals(
				"A slightly more complicated graph failed to work with Kruskal's algorithm",
				expected,
				mst);
	}
}