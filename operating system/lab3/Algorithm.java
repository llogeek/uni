package taskB;

import javafx.util.Pair;

import java.util.Collections;
import java.util.LinkedList;

public class Algorithm {
    static int INF = 10000000;
    static MyPanel pane;
    static Test test;
    Algorithm(MyPanel _pane){
        pane = _pane;
    }
    Algorithm(Test _test){test = _test;};
    public void findShortestWay(Test test) {
        synchronized (Algorithm.class) {
            int size = test.getN() + 1;
            int s = test.getA(); // стартовая вершина
            int[] d = new int[size];
            for (int i = 0; i < d.length; i++) d[i] = INF + 2;
            int[] p = new int[size];
            d[0] = 0;
            d[s] = 0;
            LinkedList<Pair<Integer, Integer>> q = new LinkedList<>();
            q.addLast(new Pair(0, s));
            while (!q.isEmpty()) {
                Pair<Integer, Integer> temp = q.pollLast();
                int v = temp.getValue(), cur_d = -temp.getKey();
                if (cur_d > d[v]) continue;
                for (int j = 0; j < test.arcs[v].size(); ++j) {
                    int to = test.arcs[v].get(j).getKey(),
                            len = test.arcs[v].get(j).getValue();
                    if (d[v] + len < d[to]) {
                        d[to] = d[v] + len;
                        p[to] = v;
                        q.addLast(new Pair(-d[to], to));
                    }
                }
            }
            for (int i = 0; i < d.length; i++)
                if (d[i] == INF + 2) d[i] = 0;
            test.getSolution().setLength(d[test.getB()]);
            int i = test.getB();
            if (test.getSolution().getLength() != 0) {
                while (i >= s) {
                    test.getSolution().addWay(i);
                    i = p[i];
                }
            }
            Collections.reverse(test.getSolution().getWay());
            if (pane != null) {
                pane.addSolution(test);
            }
        }
    }
    Object sync = new Object();
}
