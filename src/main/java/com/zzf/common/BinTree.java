package com.zzf.common;

import java.util.ArrayList;
import java.util.List;
/**
 * @author  张增福
 */
public class BinTree {
	private Object data;//数据元素
	private BinTree left,right;//指向左、右两个孩子节点的链
	
	public BinTree(){
		
	}
	
	public BinTree(Object data,BinTree left,BinTree right){
		this.data = data;
		this.left = left;
		this.right = right;
	}
	
	public BinTree(Object data){
		this.data = data;
		left = right = null;
	}
	
	public String toString(){
		return data.toString();
	}
	
	/**
	 *前序遍历
	 * @param parent
	 */
	public void preOrder(BinTree parent){
		if(parent==null)return ;
		System.out.println(""+parent.data);
		preOrder(parent.left);
		preOrder(parent.right);
	}
	
	/**
	 * 中序遍历
	 * @param parent
	 */
	public void inOrder(BinTree parent){
		if(parent==null)return ;
		inOrder(parent.left);
		System.out.println(""+parent.data);
		inOrder(parent.right);
	}
	
	/**
	 * 后续遍历
	 * @param parent
	 */
	public void postOrder(BinTree parent){
		if(parent==null)return ;
		postOrder(parent.left);
		postOrder(parent.right);
		System.out.println(""+parent.data);
	}
	
	/**
	 * 层次遍历
	 * @param parent
	 */
	public void layerOrder(BinTree parent){
		List<BinTree> list = new ArrayList<BinTree>();//层次遍历时保存各个节点
		list.add(parent);
		int front = 0;
		while(front<list.size()){
			BinTree obj = (BinTree)list.get(front);
			if(obj.data!=null){
				System.out.println(""+obj.data);
				if(obj.left!=null)
					list.add(obj.left);
				if(obj.right!=null)
					list.add(obj.right);
				front++;
			}
		}
	}
	
	/**
	 *返回树叶节点个数
	 * @return
	 */
	public int leaves(){
		if(this==null)return 0;
		if(left==null && right==null)return 1;
		return (left==null?0:left.leaves())+(right==null?0:right.leaves());
	}
	
	/**
	 * 返回树的高度
	 * @return
	 */
	public int height(){
		if(this==null)return -1;
		int leftHeirht = (left==null?0:left.height());
		int rightHeirht = (right==null?0:right.height());
		int heirhtOfTree = leftHeirht<rightHeirht?rightHeirht:leftHeirht;
		return heirhtOfTree+1;
	}
	
	/**
	 * 如果对象不在树中，返回-1，否则返回该对象在树中所处的层次，规定根节点为第一层
	 * @param object
	 * @return
	 */
	public int level(Object object){
		int levelInTree;
		if(this==null)return -1;
		if(object==data)return 1;//规定根节点为第一层
		int leftLevel = (left==null?-1:left.level(object));
		int rightLevel = (right==null?-1:right.level(object));
		if(leftLevel<0 && rightLevel<0)return -1;
		levelInTree = leftLevel<rightLevel?rightLevel:leftLevel;
		return levelInTree+1;
	}
	
	/**
	 *将树中每个节点的孩子对换位置
	 *
	 */
	public void reflect(){
		if(this==null)return;
		if(left!=null)left.reflect();
		if(right!=null)right.reflect();
		BinTree temp = left;
		left = right;
		right = temp;
	}
	
	/**
	 * 将树中所有节点移走，并输出移走的节点
	 *
	 */
	public void defoliate(){
		if(this==null) return ;
		if(left==null && right==null){//若本节点是叶子节点，则将其移走
			System.out.println(""+this);
			data=null;
			return;
		}
		if(left!=null){//移走左子树
			left.defoliate();
			left=null;
		}
		data=null;
		if(right!=null){//移走右子树
			right.defoliate();
			right=null;
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BinTree e = new BinTree("E");
		BinTree g = new BinTree("G");
		BinTree h = new BinTree("H");
		BinTree i = new BinTree("I");
		BinTree d = new BinTree("D",null,g);
		BinTree f = new BinTree("F",h,i);
		BinTree b = new BinTree("B",d,e);
		BinTree c = new BinTree("C",f,null);
		BinTree tree = new BinTree("A",b,c);
		System.out.println("前序遍历二叉树结果：");
		tree.preOrder(tree);
		System.out.println("中序遍历二叉树结果：");
		tree.inOrder(tree);
		System.out.println("后序遍历二叉树结果：");
		tree.postOrder(tree);
		System.out.println("层序遍历二叉树结果：");
		tree.layerOrder(tree);
		System.out.println("F所在的层次："+tree.level("F"));
		System.out.println("这棵二叉树的高度："+tree.height());
		System.out.println("这棵二叉树的叶子节点的个数："+tree.leaves());
		tree.reflect();
		System.out.println("==============交换每个节点的孩子节点后==================");
		System.out.println("前序遍历二叉树结果：");
		tree.preOrder(tree);
		System.out.println("中序遍历二叉树结果：");
		tree.inOrder(tree);
		System.out.println("后序遍历二叉树结果：");
		tree.postOrder(tree);
		System.out.println("层序遍历二叉树结果：");
		tree.layerOrder(tree);
		System.out.println("F所在的层次："+tree.level("F"));
		System.out.println("这棵二叉树的高度："+tree.height());
		System.out.println("这棵二叉树的叶子节点的个数："+tree.leaves());

	}

}
