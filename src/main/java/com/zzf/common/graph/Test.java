package com.zzf.common.graph;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		 /**
         * For example, we have a graph:
         * 0 → 1 → 2
         * ↓   ↑   ↓
         * 3   4   5
         * ↓   ↑   ↓
         * 6 → 7 ← 8
         *  here ,all weight is 0, its a DAG(Directed Acyclic Graph) 
         */

        DefaultGraph g=new DefaultGraph(9);
        g.setEdge(0, 1, 0);
        g.setEdge(0, 3, 0);
        g.setEdge(1, 2, 0);
        g.setEdge(4, 1, 0);
        g.setEdge(2, 5, 0);
        g.setEdge(3, 6, 0);
        g.setEdge(7, 4, 0);
        g.setEdge(5, 8, 0);
        g.setEdge(6, 7, 0);
        g.setEdge(8, 7, 0);
        
        GraphVisitor visitor=new GraphVisitor(){
            public void visit(Graph g, int vertex) {
                System.out.print(g.getVertexLabel(vertex)+" ");
            }
        };
        System.out.println("DFS==============:");
        g.deepFirstTravel(visitor);
        System.out.println();
        System.out.println("BFS==============:");
        g.breathFirstTravel(visitor);
        System.out.println();
	}

}
