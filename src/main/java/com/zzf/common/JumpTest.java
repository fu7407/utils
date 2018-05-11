package com.zzf.common;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * 青蛙跳问题
 * 有七个位置，左边三个位置上有三个只能向右跳的青蛙，右边三个位置上有三个只能向左跳的青蛙，中间位置为空，
 * 青蛙只能往前一个为空的位置或隔一个为空的位置跳
 * 怎样跳能使左边三个青蛙和右边三个青蛙互换
 * @author 张增福
 *
 */
public class JumpTest {
	
	public static void main(String[] args) {
		JumpTest m = new JumpTest();
		int[] frogs = { 1, 1, 1, 0, -1, -1, -1};//大于0表示只能向右跳，小于0表示只能向左跳
		List<int[]> list =new ArrayList<int[]>();
		m.jump(frogs,list);
	}

	public void jump(int[] state,List<int[]> list) {
		list.add(state);
		if (isSuccess(state)) {
			printState(list);
			return;
		}
		Vector<int[]> v = getPossibleMove(state);
		if (v.size() > 0) {
			for (int i = 0; i < v.size(); i++) {
				int[] possibleState = (int[]) v.get(i);
				jump(possibleState,list);
			}
			list.remove(list.size()-1);
		} else {
			list.remove(list.size()-1);
			return;
		}
	}

	
	public void printState(List<int[]> list) {
		for (int j = 0; j < list.size(); j++) {
			int[] state = (int[]) list.get(j);
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < state.length; i++) {
				sb.append((state[i])+", ");
			}
			if(j<list.size()-1){
				int[] stateNext = (int[]) list.get(j+1);
				sb.append("["+getEmptyIndex(stateNext)+","+getEmptyIndex(state)+"]");
			}else{
				sb.append("�ɹ� ");
			}
			System.out.println(sb.toString());
		}
	}

	public boolean isSuccess(int[] state) {
		if (state[0] < 0 && state[1] < 0 && state[2] < 0 && state[4] > 0
				&& state[5] > 0 && state[6] > 0) {
			return true;
		}
		return false;
	}

	public int getEmptyIndex(int[] state) {
		for (int i = 0; i < 7; i++) {
			if (state[i] == 0) {
				return i;
			}
		}
		return 999;
	}

	public Vector<int[]> getPossibleMove(int[] state) {// 得到所有可能的下一步状态
		Vector<int[]> v = new Vector<int[]>();
		int emptyIndex = getEmptyIndex(state);
		for (int i = 0; i < state.length; i++) {
			if (state[i] > 0) {//只能向右，移动或跳格
				if (i + 1 == emptyIndex) {
					int[] stateTemp = new int[7];
					System.arraycopy(state, 0, stateTemp, 0, 7);
					stateTemp[i + 1] = state[i];
					stateTemp[i] = 0;
					v.add(stateTemp);
				}
				if (i + 2 == emptyIndex) {
					int[] stateTemp = new int[7];
					System.arraycopy(state, 0, stateTemp, 0, 7);
					stateTemp[i + 2] = state[i];
					stateTemp[i] = 0;
					v.add(stateTemp);
				}
			} else if (state[i] < 0) {//只能向左，移动或跳格
				if (i - 1 == emptyIndex) {
					int[] stateTemp = new int[7];
					System.arraycopy(state, 0, stateTemp, 0, 7);
					stateTemp[i - 1] = state[i];
					stateTemp[i] = 0;
					v.add(stateTemp);
				}
				if (i - 2 == emptyIndex) {
					int[] stateTemp = new int[7];
					System.arraycopy(state, 0, stateTemp, 0, 7);
					stateTemp[i - 2] = state[i];
					stateTemp[i] = 0;
					v.add(stateTemp);
				}
			}
		}
		return v;
	}

}
