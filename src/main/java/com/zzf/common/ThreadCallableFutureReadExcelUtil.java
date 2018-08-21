package com.zzf.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class ThreadCallableFutureReadExcelUtil {

	public static final int PAGESIZE = 20;

	// 设置线程池，池中5个线程，最多10个线程（超过放入队列等着），空闲线程最多存活500毫秒
	private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 10, 500, TimeUnit.SECONDS,
			new LinkedBlockingQueue<Runnable>());

	private static Map<String, String> concurrentmap = new ConcurrentHashMap<String, String>();

	public static void main(String[] args) throws Exception {
		// String filePath = System.getProperty("user.dir") +
		// "src/main/resources" + "info.xls";
		String filePath = "f:/info.xls";
		ThreadCallableFutureReadExcelUtil.deal(filePath);
	}

	public static void deal(String filePath) throws Exception {
		long start = System.currentTimeMillis();
		Sheet sheet = null;
		Workbook wb = getExcelWb(filePath);
		if (wb != null) {
			sheet = wb.getSheet(0);
			int rows = sheet.getRows();
			int pageNum = rows % PAGESIZE == 0 ? rows / PAGESIZE : (rows / PAGESIZE + 1);
			List<Future<Map<String, String>>> list = new ArrayList<Future<Map<String, String>>>();
			for (int i = 0; i < pageNum; i++) {
				System.out.println("线程池中线程数目：" + threadPoolExecutor.getPoolSize() + "队列中等待执行的任务数目："
						+ threadPoolExecutor.getQueue().size() + "已执行完成的任务数目："
						+ threadPoolExecutor.getCompletedTaskCount());
				int endIndex = (i + 1) * PAGESIZE >= rows ? rows : ((i + 1) * PAGESIZE);
				ReadExcelThread threadtask = new ReadExcelThread(i * PAGESIZE, endIndex, sheet);
				Future<Map<String, String>> future = threadPoolExecutor.submit(threadtask);
				list.add(future);
			}
			while (true) {
				for (int i = 0; i < list.size(); i++) {
					concurrentmap.putAll(list.get(i).get());
					list.remove(i);
					i--;
				}
				if (list.size() == 0) {
					threadPoolExecutor.shutdown();
					break;
				}
			}
		}
		long end = System.currentTimeMillis();
		System.out.println("总计数量：" + concurrentmap.size() + ",time=" + (end - start));
		// for (Entry<String, String> map : concurrentmap.entrySet()) {
		// System.out.println(map.getValue());
		// }
	}

	public static Workbook getExcelWb(String filePath) {
		Workbook wb = null;
		if (StringUtils.isBlank(filePath)) {
			return null;
		}
		// String fileType = filePath.substring(filePath.lastIndexOf("."));
		InputStream is = null;
		try {
			is = new FileInputStream(filePath);
			return wb = Workbook.getWorkbook(is);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return wb;
	}

	static class ReadExcelThread implements Callable<Map<String, String>> {
		private int startIndex;
		private int endIndex;
		private Sheet sheet;

		public ReadExcelThread(int startIndex, int endIndex, Sheet sheet) {
			this.startIndex = startIndex;
			this.endIndex = endIndex;
			this.sheet = sheet;
		}

		@Override
		public Map<String, String> call() throws Exception {
			Map<String, String> rowmap = batchReadExcelRow(startIndex, endIndex, sheet);
			return rowmap;
		}

		public Map<String, String> batchReadExcelRow(int startIndex, int endIndex, Sheet sheet) {
			Cell cell = null;
			String rowval = "";
			Map<String, String> map = new HashMap<String, String>();
			for (int i = startIndex; i < endIndex; i++) {
				int columns = sheet.getColumns();
				for (int j = 0; j < columns; j++) {
					cell = sheet.getCell(j, i);
					rowval += cell.getContents() + "#";
				}
				map.put((i + 1) + "", rowval);
			}
			return map;
		}

	}

}
