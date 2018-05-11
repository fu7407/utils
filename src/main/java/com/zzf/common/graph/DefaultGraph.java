package com.zzf.common.graph;

import java.util.Arrays;

public class DefaultGraph implements Graph {
	private static class _Edge implements Edge{
		private static final _Edge NullEdge = new _Edge();
		int from;
		int to;
		int weight;
		_Edge nextEdge;
		
		private _Edge() {
			weight = Integer.MAX_VALUE;
		}
		
		_Edge(int from, int to, int weight) {
			this.from = from;
			this.to = to;
			this.weight = weight;
		}
		
		public int getWeight() {
			return weight;
		}
	}

	private static class _EdgeStaticQueue {
		_Edge first;
		_Edge last;
	}

	private int numVertexes;//顶点个数
	private String[] labels;//保存定点标号
	private int numEdges;//边的个数
	private _EdgeStaticQueue[] edgeQueues;
	private boolean[] visitTags;//访问标示

	public DefaultGraph(int numVertexes) {
		if (numVertexes < 1) {
			throw new IllegalArgumentException();
		}
		this.numVertexes = numVertexes;
		this.visitTags = new boolean[numVertexes];
		this.labels = new String[numVertexes];
		for (int i = 0; i < numVertexes; i++) {
			labels[i] = i + "";
		}
		this.edgeQueues = new _EdgeStaticQueue[numVertexes];
		for (int i = 0; i < numVertexes; i++) {
			edgeQueues[i] = new _EdgeStaticQueue();
			edgeQueues[i].first = edgeQueues[i].last = _Edge.NullEdge;
		}
		this.numEdges = 0;
	}

	public void assignLabels(String[] labels) {
		System.arraycopy(labels, 0, this.labels, 0, labels.length);
	}

	public int edgeNum() {
		return numEdges;
	}

	public Edge firstEdge(int vertex) {
		if(vertex>=numVertexes)    throw new IllegalArgumentException();
        return edgeQueues[vertex].first;
	}

	public int fromVertex(Edge edge) {
		return ((_Edge)edge).from;
	}

	public String getVertexLabel(int vertex) {
		return labels[vertex];
	}

	public boolean isEdge(Edge edge) {
		return (edge!=_Edge.NullEdge);
	}

	public Edge nextEdge(Edge edge) {
		return ((_Edge)edge).nextEdge;
	}

	public void setEdge(int from, int to, int weight) {
		if(from<0||from>=numVertexes||to<0||to>=numVertexes||weight<0||from==to)throw new IllegalArgumentException();
        _Edge edge=new _Edge(from,to,weight);
        edge.nextEdge=_Edge.NullEdge;
        if(edgeQueues[from].first==_Edge.NullEdge)
            edgeQueues[from].first=edge;
        else 
            edgeQueues[from].last.nextEdge=edge;
        edgeQueues[from].last=edge;
        numEdges++;
	}

	public int toVertex(Edge edge) {
		return ((_Edge)edge).to;
	}

	public int vertexesNum() {
		return numVertexes;
	}

	public void deepFirstTravel(GraphVisitor visitor) {
		Arrays.fill(visitTags, false);//设置所有顶点都没有访问过
        for(int i=0;i<numVertexes;i++){
            if(!visitTags[i]){
                do_DFS(i,visitor);
            }
        }
	}
	
	private final void do_DFS(int v, GraphVisitor visitor) {
        visitor.visit(this, v);
        visitTags[v]=true;
        for(Edge e=firstEdge(v);isEdge(e);e=nextEdge(e)){
            if(!visitTags[toVertex(e)]){
                do_DFS(toVertex(e),visitor);
            }
        }
    }

	public void breathFirstTravel(GraphVisitor visitor) {
		Arrays.fill(visitTags, false);//设置所有顶点都没有访问过
        for(int i=0;i<numVertexes;i++)
        {
            if(!visitTags[i])do_BFS(i,visitor);
        }
	}
	
	private static class _IntQueue{
        _IntQueueNode first;
        _IntQueueNode last;
        
        private static class _IntQueueNode{
            _IntQueueNode next;
            int value;
        }
        
        void add(int i){
            _IntQueueNode node=new _IntQueueNode();
            node.value=i;
            node.next=null;
            if(first==null)first=node;
            else last.next=node;
            last=node;
        }
        
        boolean isEmpty(){
            return first==null;
        }
        
        int remove(){
            int val=first.value;
            if(first==last)
                first=last=null;
            else
                first=first.next;
            return val;
        }
    }
	
	private void do_BFS(int v, GraphVisitor visitor) {
        _IntQueue queue=new _IntQueue();
        queue.add(v);
        while(!queue.isEmpty()){
            int fromV=queue.remove();
            visitor.visit(this, fromV);
            visitTags[fromV]=true;
            for(Edge e=firstEdge(fromV);isEdge(e);e=nextEdge(e)){
                if(!visitTags[toVertex(e)]){
                    queue.add(toVertex(e));
                }
            }
        }
    }

}
