package com.zzf.common.sort;

import java.util.*;

/**
 * @author 张增福
 */
public class SortDemo {
	
	/**
	 * 插入法排序
	 * @param list
	 * @return
	 */
	public List insertSort(List list) {
		Integer tempInt;
		int MaxSize = 1;
		for (int i=1; i<list.size();i++) {
			tempInt = (Integer) list.remove(i);
			if (tempInt.intValue()>=((Integer)list.get(MaxSize-1)).intValue()){
				list.add(MaxSize, tempInt);
				MaxSize++;
			} else {
				for(int j=0; j<MaxSize; j++) {
					if(((Integer)list.get(j)).intValue()>=tempInt.intValue()){
						list.add(j, tempInt);
						MaxSize++;
						break;
					}
				}
			}
		}
		return list;
	}
	
	/**
	 * 冒泡排序（升序）
	 * 如降序则将if(array[i]>array[j])改为if(array[i]<array[j])
	 * 基本思路：对未排序的各元素从头到尾依次比较相邻的两个元素是否逆序（与欲排序相反），若逆序则交换这两个元素,
	 * 		经过第一轮比较排序后便可把最大（或最小）的元素排好，然后再用同样的方法把剩下的元素逐个进行比较，就得到了你所要的排序
	 * @param array
	 * @return
	 */
	public int[] ebullitionSort(int [] array){
		for(int i=0;i<array.length;i++){
			for(int j=i+1;j<array.length;j++){
				if(array[i]>array[j]){
					int temp = array[j];
					array[j] = array[i];
					array[i] = temp;
				}
			}
		}
		return array;
	}
	
	/**
	 * 选择排序（升序）
	 * 如降序则将if(array[j]<array[lowIndex])改为if(array[j]>array[lowIndex])
	 * 基本思路：从所有元素中选择一个最小元素a[i]放在a[0]（即让最小元素a[0]与a[i]交换），作为第一轮；
	 * 		第二轮是从a[1]开始到最后的各个元素中选择一个最小的元素，放在a[1]中；...；依此类推即可得到
	 * @param array
	 * @return
	 */
	public int [] selectSort(int [] array){
		int temp;
		for(int i=0;i<array.length;i++){
			int lowIndex = i;
			for(int j=i+1;j<array.length;j++){
				if(array[j]<array[lowIndex]){
					lowIndex=j;
				}
			}
			temp = array[i];
			array[i] = array[lowIndex];
			array[lowIndex] = temp;
		}
		return array;
	}
	
	/**
	 * 插入排序（升序）
	 * 如降序则将array[j]<array[j-1]改为array[j]>array[j-1]
	 * 基本思路：每拿到一个元素，都要将这个元素与所有它之前的元素遍历比较一遍，让符合排序顺序的元素
	 * 		挨个移动到当前范围内它最应该出现的位置
	 * @param array
	 * @return
	 */
	public int [] insertSort(int [] array) {
		int temp;
		for(int i=1;i<array.length;i++){//第一个元素认为已经排好序了
			for(int j=i;(j>0)&&(array[j]<array[j-1]);j--){
					temp = array[j];
					array[j] = array[j-1];
					array[j-1] = temp;
			}
		}
		return array;
	}
	
	/**
	 * 希尔排序（升序）
	 * 如降序则将temp<array[j-increment]改为temp>array[j-increment]
	 * 基本思路：先将数据按照固定的间隔分组，例如每隔4个分为一组，然后排序各分组的数据，形成以分组来看，数据已经排序，
	 * 		从全部来看，较小值已经在前面，较大值已经在后面。将初步处理了的分组再用插入排序来排序，那么数据交换和移动
	 * 		的次数会减少。可以得到比插入排序更高的效率.
	 * @param array
	 * @return
	 */
	public int [] shellSort(int [] array) {
		int j=0;
		int temp=0;
		for(int increment=array.length/2;increment>0;increment/=2){
			for(int i=increment;i<array.length;i++){
				temp=array[i];
				for(j=i;j>=increment;j-=increment){
					if(temp<array[j-increment]){
						array[j]=array[j-increment];
					}else{
						break;
					}
				}
				array[j]=temp;
			}
		}
		return array;
	}
	
	/**
	 * 快速排序
	 * @param array
	 * @return
	 */
	 public int [] quickSort(int[] array) {
		int temp;
		int[] stack = new int[4096];
		int top = -1;
		int pivot;
		int pivotIndex, l, r;
		stack[++top] = 0;
		stack[++top] = array.length - 1;
		while (top > 0) {
			int j = stack[top--];
			int i = stack[top--];
			pivotIndex = (i + j) / 2;
			pivot = array[pivotIndex];
			temp = array[pivotIndex];
			array[pivotIndex] = array[j];
			array[j] = temp;
			l = i - 1;
			r = j;
			do {
				while (array[++l] < pivot);
				while ((r != 0) && (array[--r] > pivot));
				temp = array[l];
				array[l] = array[r];
				array[r] = temp;
			} while (l < r);
			temp = array[l];
			array[l] = array[r];
			array[r] = temp;
			temp = array[l];
			array[l] = array[j];
			array[j] = temp;
			if ((l - i) > 10) {
				stack[++top] = i;
				stack[++top] = l - 1;
			}
			if ((j - l) > 10) {
				stack[++top] = l + 1;
				stack[++top] = j;
			}
		}
		return insertSort(array);
	}
	 
	/**
	 * 归并排序
	 * @param array
	 * @return
	 */
	public int [] mergeSort(int[] array) {
		int[] temp = new int[array.length];
		mergeSort(array, temp, 0, array.length - 1);
		return array;
	}
	private void mergeSort(int[] array, int[] temp, int l, int r) {
		int mid = (l+r)/2;
		if (l == r)
			return;
		mergeSort(array, temp, l, mid);
		mergeSort(array, temp, mid+1, r);
		for (int i=l; i<=r; i++) {
			temp[i] = array[i];
		}
		int i1 = l;
		int i2 = mid + 1;
		for (int cur = l; cur <= r; cur++) {
			if (i1 == mid + 1)
				array[cur] = temp[i2++];
			else if (i2 > r)
				array[cur] = temp[i1++];
			else if (temp[i1] < temp[i2])
				array[cur] = temp[i1++];
			else
				array[cur] = temp[i2++];
		}
	} 
	
	/**
	 * 堆排序???
	 * @param array
	 * @return
	 */
	public int[] heapSort(int[] array) {
		MaxHeap h = new MaxHeap();
		h.init(array);
		for (int i = 0; i < array.length; i++)
			h.remove();
		System.arraycopy(h.queue, 1, array, 0, array.length);
		return array;
	} 

	private static class MaxHeap {
		private int size = 0;
		private int[] queue;
		void init(int[] array) {
			this.queue = new int[array.length + 1];
			for (int i = 0; i < array.length; i++) {
				queue[++size] = array[i];
				fixUp(size);
			}
		}
		public int get() {
			return queue[1];
		}
		public void remove() {
			size--;
			int temp = queue[1];
			queue[1] = queue[size];
			queue[size] = temp;
			fixDown(1);
		}
		private void fixDown(int k) {
			int j;
			while ((j = k << 1) <= size) {
				if (j < size && queue[j] < queue[j + 1])
					j++;
				if (queue[k] > queue[j]) // 不用交换
					break;
				int temp = queue[j];
				queue[j] = queue[k];
				queue[k] = temp;
				k=j;
			}
		}
		private void fixUp(int k) {
			while (k > 1) {
				int j = k >> 1;
				if (queue[j] > queue[k])
					break;
				int temp = queue[j];
				queue[j] = queue[k];
				queue[k] = temp;
				k = j;
			}
		}
	} 
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SortDemo test=new SortDemo();
		Random rand = new Random(); 
		
		/*
		 * List list = new ArrayList(10); for(int i=0;i<10;i++ ) { list.add(new
		 * Integer(Math.abs(rand.nextInt())%100+1)); } System.out.println("The
		 * ArrayList Sort Before:"+list.toString()); list =
		 * test.insertSort(list); System.out.println("The ArrayList Sort
		 * After:"+list.toString());
		 */
		
		int [] array = new int[10];
		for(int i=0;i<10;i++) { 
			array[i]=Math.abs(rand.nextInt())%100+1;
		}  
		for(int i=0;i<array.length;i++){
			if(i==0)
				System.out.print("The Array Sort Before:["+array[i]); 
			else if(i==array.length-1)
				System.out.println(","+array[i]+"]"); 
			else
				System.out.print(","+array[i]); 
		}
		test.mergeSort(array);
		for(int i=0;i<array.length;i++){
			if(i==0)
				System.out.print("The Array Sort After:["+array[i]); 
			else if(i==array.length-1)
				System.out.println(","+array[i]+"]"); 
			else
				System.out.print(","+array[i]); 
		}
	}
}
