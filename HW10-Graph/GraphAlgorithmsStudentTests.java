import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

/**
 * Student tests for the GraphAlgorithms class.
 *
 * @author CS 1332 TAs
 * @version 1.0
 */
public class GraphAlgorithmsStudentTests {

    private static final int TIMEOUT = 200;
    private Graph<String> graph;
    private Vertex<String> a = new Vertex<>("a");
    private Vertex<String> b = new Vertex<>("b");
    private Vertex<String> c = new Vertex<>("c");
    private Vertex<String> d = new Vertex<>("d");
    private Vertex<String> e = new Vertex<>("e");
    private Vertex<String> f = new Vertex<>("f");

    private Graph<Integer> directedGraph;
    private Vertex<Integer> one = new Vertex<>(1);
    private Vertex<Integer> two = new Vertex<>(2);
    private Vertex<Integer> three = new Vertex<>(3);
    private Vertex<Integer> four = new Vertex<>(4);
    private Vertex<Integer> five = new Vertex<>(5);
    private Vertex<Integer> six = new Vertex<>(6);
    private Vertex<Integer> seven = new Vertex<>(7);

    @Before
    public void setUp() {
        LinkedHashSet<Edge<String>> edges = new LinkedHashSet<>();

        edges.add(new Edge<String>(a, b, 3, false));
        edges.add(new Edge<String>(a, c, 5, false));
        edges.add(new Edge<String>(a, d, 4, false));
        edges.add(new Edge<String>(b, e, 3, false));
        edges.add(new Edge<String>(b, f, 5, false));
        edges.add(new Edge<String>(c, d, 2, false));
        edges.add(new Edge<String>(d, e, 1, false));
        edges.add(new Edge<String>(e, f, 2, false));

        graph = new Graph<>(edges);

        LinkedHashSet<Edge<Integer>> edgesDirected = new LinkedHashSet<>();

        edgesDirected.add(new Edge<Integer>(one, two, 1, true));
        edgesDirected.add(new Edge<Integer>(one, three, 1, true));
        edgesDirected.add(new Edge<Integer>(one, four, 1, true));
        edgesDirected.add(new Edge<Integer>(three, five, 1, true));
        edgesDirected.add(new Edge<Integer>(five, four, 1, true));
        edgesDirected.add(new Edge<Integer>(four, six, 1, true));
        edgesDirected.add(new Edge<Integer>(five, seven, 1, true));
        edgesDirected.add(new Edge<Integer>(seven, six, 1, true));

        directedGraph = new Graph<Integer>(edgesDirected);
    }

    @Test(timeout = TIMEOUT)
    public void testBFS() {
        List<Vertex<String>> bfsCorrect = new ArrayList<>();
        bfsCorrect.add(a);
        bfsCorrect.add(b);
        bfsCorrect.add(c);
        bfsCorrect.add(d);
        bfsCorrect.add(e);
        bfsCorrect.add(f);
        List<Vertex<String>> bfsAnswer = GraphAlgorithms
                .breadthFirstSearch(a, graph);
        assertEquals(bfsCorrect, bfsAnswer);
    }

    @Test(timeout = TIMEOUT)
    public void testBFSDirected() {
        List<Vertex<Integer>> bfsCorrect = new ArrayList<>();
        bfsCorrect.add(one);
        bfsCorrect.add(two);
        bfsCorrect.add(three);
        bfsCorrect.add(four);
        bfsCorrect.add(five);
        bfsCorrect.add(six);
        bfsCorrect.add(seven);
        List<Vertex<Integer>> bfsAnswer = GraphAlgorithms
                .breadthFirstSearch(one, directedGraph);
        assertEquals(bfsCorrect, bfsAnswer);
    }

    @Test(timeout = TIMEOUT)
    public void testDFS() {
        List<Vertex<String>> dfsCorrect = new ArrayList<>();
        dfsCorrect.add(a);
        dfsCorrect.add(b);
        dfsCorrect.add(e);
        dfsCorrect.add(d);
        dfsCorrect.add(c);
        dfsCorrect.add(f);
        List<Vertex<String>> dfsAnswer = GraphAlgorithms
                .depthFirstSearch(a, graph);
        assertEquals(dfsCorrect, dfsAnswer);
    }

    @Test(timeout = TIMEOUT)
    public void testDFSDirected() {
        List<Vertex<Integer>> dfsCorrect = new ArrayList<>();
        dfsCorrect.add(one);
        dfsCorrect.add(two);
        dfsCorrect.add(three);
        dfsCorrect.add(five);
        dfsCorrect.add(four);
        dfsCorrect.add(six);
        dfsCorrect.add(seven);
        List<Vertex<Integer>> dfsAnswer = GraphAlgorithms
                .depthFirstSearch(one, directedGraph);
        assertEquals(dfsCorrect, dfsAnswer);
    }

    @Test(timeout = TIMEOUT)
    public void testDijkstrasA() {
        Map<Vertex<String>, Integer> dijkstrasAnswer =
                GraphAlgorithms.dijkstras(a, graph);
        assertEquals(0, dijkstrasAnswer.get(a).intValue());
        assertEquals(3, dijkstrasAnswer.get(b).intValue());
        assertEquals(5, dijkstrasAnswer.get(c).intValue());
        assertEquals(4, dijkstrasAnswer.get(d).intValue());
        assertEquals(5, dijkstrasAnswer.get(e).intValue());
        assertEquals(7, dijkstrasAnswer.get(f).intValue());
    }

    @Test(timeout = TIMEOUT)
    public void testDijkstrasC() {
        Map<Vertex<String>, Integer> dijkstrasAnswer =
                GraphAlgorithms.dijkstras(c, graph);
        assertEquals(5, dijkstrasAnswer.get(a).intValue());
        assertEquals(6, dijkstrasAnswer.get(b).intValue());
        assertEquals(0, dijkstrasAnswer.get(c).intValue());
        assertEquals(2, dijkstrasAnswer.get(d).intValue());
        assertEquals(3, dijkstrasAnswer.get(e).intValue());
        assertEquals(5, dijkstrasAnswer.get(f).intValue());
    }

    @Test(timeout = TIMEOUT)
    public void testKruskals() {
        Set<Edge<String>> kruskalsAnswer =
                GraphAlgorithms.kruskals(graph);
        Set<Edge<String>> kruskalsCorrect = new HashSet<>();
        kruskalsCorrect.add(new Edge<String>(a, b, 3, false));
        kruskalsCorrect.add(new Edge<String>(b, e, 3, false));
        kruskalsCorrect.add(new Edge<String>(e, d, 1, false));
        kruskalsCorrect.add(new Edge<String>(d, c, 2, false));
        kruskalsCorrect.add(new Edge<String>(e, f, 2, false));
        assertEquals(kruskalsCorrect, kruskalsAnswer);
    }

}
