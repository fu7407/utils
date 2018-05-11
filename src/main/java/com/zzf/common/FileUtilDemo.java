package com.zzf.common;
import java.io.*;


/**
 * @author 张增福
 */
public class FileUtilDemo {
	
	
	public void createFile(){
		String path = "e:"+File.separator+"abc.txt";
		File file = new File(path);
		try{
			file.createNewFile();
		}catch(Exception e){
			
		}
	}
	
	public void deleteFile(){
		try{
			String path = "e:"+File.separator+"abc.txt";
			File file = new File(path);
			if(file.exists()){//文件存在
				file.delete();//删除文件
			}else{
				file.createNewFile();//创建文件 
			}
		}catch(Exception e){
			
		}
	}
	
	/**
	 * 列出目录下的所有文件
	 *
	 */
	public void dirFile(){
		try{
			File file = new File("e:"+File.separator);
			if(file.isDirectory()){//是目录
				String []s = file.list();//只列出目录下的文件夹或文件名称
				for(int i=0;i<s.length;i++){
					System.out.println(s[i]);
				}
				
				File [] fs = file.listFiles();//列出的时目录下的文件夹或文件名称的决对路径
				for(int i=0;i<fs.length;i++){
					System.out.println(fs[i]);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 复制文件
	 * @throws Exception
	 */
	public void copyFile()throws Exception{
		File file1 = new File("e:"+File.separator+"test.txt");//源文件
		File file2 = new File("e:"+File.separator+"testdest.txt");//目标文件
		if(!file1.exists())return;//源文件不存在
		InputStream input = new FileInputStream(file1);
		OutputStream output = new FileOutputStream(file2);
		if(input!=null && output!=null){
			int temp=0;
			while((temp=input.read())!=-1){
				output.write(temp);
			}
		}
		input.close();
		output.close();
	}
	
	/**
	 * 字节输出流outputStream（直接操作文件本身）
	 * 向文件写字符串
	 * @throws FileNotFoundException 
	 */
	public void outputStream() throws Exception{
		File file = new File("e:"+File.separator+"test.txt");
		//OutputStream out = new FileOutputStream(file);//覆盖文件中原有的内容
		OutputStream out = new FileOutputStream(file,true);//在文件末尾追加内容
		String str = "hello world !!";//增加换行则需要加入"\r\n"
		byte [] b = str.getBytes();
		//方法一
		out.write(b);
		//方法二
		for(int i=0;i<b.length;i++){
			out.write(b[i]);
		}
		out.close();
	}
	
	/**
	 * 字节输入流（直接操作文件本身）
	 * 从文件中读取内容
	 * @throws Exception
	 */
	public void inputStream()throws Exception{
		File file = new File("e:"+File.separator+"test.txt");
		InputStream input = new FileInputStream(file);
		
		//方法一：如果没有1024个字符，将被空格替换
		byte [] b = new byte[1024];
		input.read(b);
		System.out.println("内容为："+new String(b));
		//方法二：不会出现空格
		byte [] b2 = new byte[1024];
		int len = input.read(b2);
		System.out.println("内容为："+new String(b2,0,len));
		//方法三：开辟指定大小的byte数组
		byte [] b3 = new byte[(int)file.length()];
		input.read(b3);
		System.out.println("内容为："+new String(b3));
		//方法四:使用read()通过循环读取
		byte [] b4 = new byte[(int)file.length()];
		for(int i=0;i<b4.length;i++){
			b4[i] = (byte)input.read();
		}
		System.out.println("内容为："+new String(b4));
		//方法五：
		int len5 = 0;
		byte [] b5 = new byte[1024];
		int temp=0;
		while((temp=input.read())!=-1){
			b5[len5] = (byte)temp;
			len5++;
		}
		System.out.println("内容为："+new String(b5,0,len));

		input.close();//关闭输入流
	}
	
	/**
	 * 从文件中读取数据
	 */
	public void read(){
		try {
			File file = new File("e:"+File.separator+"test.txt");
			
			//方法一
			/*BufferedReader br = new BufferedReader(new FileReader(file));
			String line = "";
			while((line=br.readLine()) != null) {
				String [] str = line.split(":");
				System.out.println(str[0]+"="+str[1].substring(0, str[1].length()-1).trim()+";"+
						str[1].substring(str[1].length()-1, str[1].length())+"="+str[2]+";");
			}
			br.close();*/
			
			//方法二(字节流)
			/*InputStream input = new FileInputStream(file);
			int c;
			while((c=input.read())!=-1){
				System.out.print((char)c);
			}
			input.close();*/
			
			//方法三(字符流)
			Reader input = new FileReader(file);
			int c;
			while((c=input.read())!=-1){
				System.out.print((char)c);
			}
			input.close();
			//方法四(ByteArrayInputStream内存输入流)
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 向文件中写入数据
	 */
	public void writer(){
		try {
			File file = new File("e:"+File.separator+"testNew.txt");
			//方法一
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			for(int i=0;i<10;i++){
				bw.write("a:000000");
				bw.write("b:111111");
				bw.newLine();
			}
			bw.close();
			
			//方法二(字节流：直接操作文件本身，不调用close()方法文件中也会有数据)
			OutputStream out = new FileOutputStream(file);
			out.write("Hello World!".getBytes());
			out.close();
			
			//方法三(字符流：有缓冲区，不调用close()方法文件中不会有数据，但也可以通过调用flush()方法强制性清空缓冲区来往文件中真正写入数据)
			Writer out2 = new FileWriter(file);
			out2.write("Hello World?");
			out2.flush();
			out2.close();
//			方法四(ByteArrayOutputStream内存输出流)
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 打印流
	 *
	 */
	public void prStream(){
		try {
			PrintStream ps = new PrintStream(new FileOutputStream(new File("e:"+File.separator+"testNew.txt")));
			ps.print("hello");
			ps.println("\t\nWORLD");//
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void getResult(){
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			boolean flag = true;
			String str = "";
			int x =0;
			int y =0;
			System.out.println("请输入第一个数字：");
			while(flag){
				str = br.readLine();
				if(str.matches("\\d+")){
					x = Integer.parseInt(str);
					flag = false;
				}else{
					System.out.println("输入不是数字请重新输入：");
				}
			}
			flag = true;
			System.out.println("请输入第二个数字：");
			while(flag){
				str = br.readLine();
				if(str.matches("\\d+")){
					y = Integer.parseInt(str);
					flag = false;
				}else{
					System.out.println("输入不是数字请重新输入：");
				}
			}
			System.out.println("计算结果："+x+"+"+y+"="+(x+y));
			//System.getProperties().list(System.out);//得到系统相关属性
			//"Hello World?".getBytes("ISO8859-1");//重新设置编码
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FileUtilDemo fileUtil = new FileUtilDemo();
		fileUtil.getResult();
		//fileUtil.writer();
	}

}
