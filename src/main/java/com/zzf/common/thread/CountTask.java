package com.zzf.common.thread;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

/**
 * 理解Fork/Join框架的设计。
 * 第一步分割任务。首先我们需要有一个fork类来把大任务分割成子任务，有可能子任务还是很大，所以还需要不停的分割，直到分割出的子任务足够小。
 * 第二步执行任务并合并结果。分割的子任务分别放在双端队列里，然后几个启动线程分别从双端队列里获取任务执行。子任务执行完的结果都统一放在一个队列里，启动一个线程从队列里拿数据，然后合并这些数据。
 * Fork/Join使用两个类来完成以上两件事情：
 * ForkJoinTask：我们要使用ForkJoin框架，必须首先创建一个ForkJoin任务。它提供在任务中执行fork()和join()操作的机制，
 * 				通常情况下我们不需要直接继承ForkJoinTask类，而只需要继承它的子类，Fork/Join框架提供了以下两个子类：
 * 		RecursiveAction：用于没有返回结果的任务。
 * 		RecursiveTask ：用于有返回结果的任务。
 * ForkJoinPool ：ForkJoinTask需要通过ForkJoinPool来执行，任务分割出的子任务会添加到当前工作线程所维护的双端队列中，
 * 				进入队列的头部。当一个工作线程的队列里暂时没有任务时，它会随机从其他工作线程的队列的尾部获取一个任务。
 * @author zhangzengfu
 *
 */
public class CountTask extends RecursiveTask<Integer> {

	private static final int THRESHOLD = 2;// 阈值

	private int start;
	private int end;

	public CountTask(int start, int end) {
		this.start = start;
		this.end = end;
	}

	@Override
	protected Integer compute() {
		int sum = 0;
		// 如果任务足够小，就计算任务
		boolean canCompute = (end - start) < THRESHOLD;
		if (canCompute) {
			for (int i = start; i <= end; i++) {
				sum += i;
				System.out.println("result 1:sum(" + sum + ")=" + (sum - i) + "+" + i);
			}
		} else {
			// 如果任务大于阈值，就分裂为两个任务计算
			int middle = (start + end) / 2;
			CountTask leftTask = new CountTask(start, middle);
			CountTask rightTask = new CountTask(middle + 1, end);
			// 执行子任务
			leftTask.fork();
			rightTask.fork();
			// 等待子任务执行完，并执行其结果
			int leftResult = leftTask.join();
			int rightResult = rightTask.join();
			// 合并子任务
			sum = leftResult + rightResult;
			System.out.println("result 2:sum(" + sum + ")=" + leftResult + "+" + rightResult);
		}
		return sum;
	}

	public static void main(String[] args) throws Exception {
		ForkJoinPool forkJoinPool = new ForkJoinPool();
		CountTask task = new CountTask(1, 10);
		Future result = forkJoinPool.submit(task);
		System.out.println(result.get());
	}

}
