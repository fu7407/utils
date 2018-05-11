package com.zzf.common.graph;

public interface Graph {
	public static interface Edge {
		public int getWeight();
	}

	/**
	 * 顶点个数
	 * @return
	 */
	int vertexesNum();

	/**
	 * 边个数
	 * @return
	 */
	int edgeNum();

	boolean isEdge(Edge edge);

	void setEdge(int from, int to, int weight);

	/**
	 * 返回指定节点的边的链表里存的第一条边
	 * @param vertex
	 * @return
	 */
	Edge firstEdge(int vertex);

	/**
	 * 顺着边链表返回下一条边
	 * @param edge
	 * @return
	 */
	Edge nextEdge(Edge edge);

	/**
	 * 返回该边的终结顶点
	 * @param edge
	 * @return
	 */
	int toVertex(Edge edge);

	/**
	 * 返回该边的起始顶点
	 * @param edge
	 * @return
	 */
	int fromVertex(Edge edge);

	/**
	 * 返回该定点对应的标号
	 * @param vertex
	 * @return
	 */
	String getVertexLabel(int vertex);

	/**
	 * 给所有顶点标上号
	 * @param labels
	 */
	void assignLabels(String[] labels);

	/**
	 * 深度优先周游
	 * @param visitor
	 */
	void deepFirstTravel(GraphVisitor visitor);

	/**
	 * 广度优先周游
	 * @param visitor
	 */
	void breathFirstTravel(GraphVisitor visitor);
}
