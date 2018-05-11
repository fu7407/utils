package com.zzf.common;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;

/***********************************************
 * * File Name: RemoteFileUtil.java * Created by: JunjieQin * Checked in by: *
 * Date: 2011-9-6 * Revision: 1.7 * Description:操作远程共享文件夹类 * Amendment History *
 * Modified Date：2011-9-16 * Modified By：JunjieQin * Change Description：From
 * local copy files to remote directory *
 ***********************************************/
public class RemoteFileUtil {
	private ArrayList<String> filelist = new ArrayList<String>();
	private String remoteHostIp; // 远程主机IP
	private String account; // 登陆账户
	private String password; // 登陆密码
	private String shareDocName; // 共享文件夹名称

	/**
	 * * 构造函数 * @param remoteHostIp 远程主机Ip * @param account 登陆账户 * @param
	 * password 登陆密码 * @param sharePath 共享文件夹路径
	 * */
	public RemoteFileUtil(String remoteHostIp, String account, String password,
			String shareDocName) {
		this.remoteHostIp = remoteHostIp;
		this.account = account;
		this.password = password;
		this.shareDocName = shareDocName;
	}

	/**
	 * * 对远程共享文件进行读取所有行 * @param remoteFileName 文件名 说明：参数为共享目录下的相对路径 *
	 * 若远程文件的路径为：shareDoc\test.txt,则参数为test.txt(其中shareDoc为共享目录名称); *
	 * 若远程文件的路径为：shareDoc\doc\text.txt,则参数为doc\text.txt; * @return 文件的所有行
	 * */
	public List<String> readFile(String remoteFileName) {
		SmbFile smbFile = null;
		BufferedReader reader = null;
		List<String> resultLines = null;
		// 构建连接字符串,并取得文件连接
		String conStr = null;
		conStr = "smb://" + account + ":" + password + "@" + remoteHostIp + "/"
				+ shareDocName + "/" + remoteFileName;
		try {
			smbFile = new SmbFile(conStr);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		// 创建reader
		try {
			reader = new BufferedReader(new InputStreamReader(
					new SmbFileInputStream(smbFile)));
		} catch (SmbException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		// 循环对文件进行读取
		String line;
		try {
			line = reader.readLine();
			if (line != null && line.length() > 0) {
				resultLines = new ArrayList<String>();
			}
			while (line != null) {
				resultLines.add(line);
				line = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 返回
		return resultLines;
	}

	/**
	 * * 对远程共享文件进行写入 * @param is 本地文件的输入流 * @param remoteFileName 远程文件名
	 * 说明：参数为共享目录下的相对路径 *
	 * 若远程文件的路径为：shareDoc\test.txt,则参数为test.txt(其中shareDoc为共享目录名称); *
	 * 若远程文件的路径为：shareDoc\doc\text.txt,则参数为doc\text.txt; * @return
	 * */
	public boolean writeFile(InputStream is, String remoteFileName) {
		SmbFile smbFile = null;
		OutputStream os = null;
		byte[] buffer = new byte[1024 * 8];
		// 构建连接字符串,并取得文件连接
		String conStr = null;
		conStr = "smb://" + account + ":" + password + "@" + remoteHostIp + "/"
				+ shareDocName + "/" + remoteFileName;
		try {
			smbFile = new SmbFile(conStr);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return false;
		}
		// 获取远程文件输出流并写文件到远程共享文件夹
		try {
			os = new BufferedOutputStream(new SmbFileOutputStream(smbFile));
			while ((is.read(buffer)) != -1) {
				os.write(buffer);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * * 对远程共享文件进行写入重载 * @param localFileName 要写入的本地文件全名 * @param remoteFileName
	 * 远程文件名 说明：参数为共享目录下的相对路径 *
	 * 若远程文件的路径为：shareDoc\test.txt,则参数为test.txt(其中shareDoc为共享目录名称); *
	 * 若远程文件的路径为：shareDoc\doc\text.txt,则参数为doc\text.txt; * @return
	 */
	public boolean writeFile(String localFileFullName, String remoteFileName) {
		try {
			return writeFile(new FileInputStream(new File(localFileFullName)),
					remoteFileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * * 对远程共享文件进行写入重载 * @param localFileName 要写入的本地文件 * @param remoteFileName
	 * 远程文件名 说明：参数为共享目录下的相对路径 *
	 * 若远程文件的路径为：shareDoc\test.txt,则参数为test.txt(其中shareDoc为共享目录名称); *
	 * 若远程文件的路径为：shareDoc\doc\text.txt,则参数为doc\text.txt; * @return
	 * */
	public boolean writeFile(File localFile, String remoteFileName) {
		try {
			return writeFile(new FileInputStream(localFile), remoteFileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * * 对远程共享文件所有文件 * @return 所有文件
	 */
	public List<String> getFiles() {
		SmbFile smbFile = null;
//		BufferedReader reader = null;
		List<String> resultLines = new ArrayList<String>();
		// 构建连接字符串,并取得文件连接
		String conStr = null;
		conStr = "smb://" + account + ":" + password + "@" + remoteHostIp + "/"
				+ shareDocName + "/";
		try {
			smbFile = new SmbFile(conStr);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} // 创建reader
		try {
			String[] a = smbFile.list();
			for (int i = 0; i < a.length; i++) {
				resultLines.add(a[i]);
				System.out.println(a[i]);
			}
		} catch (SmbException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 返回
		return resultLines;
	}

	/**
	 * 在本地为共享计算机创建文件夹 * @param remoteUrl 远程计算机路径
	 * */
	public void smbMkDir(String name) {
		// 注意使用jcifs-1.3.15.jar的时候 操作远程计算机的时候所有类前面须要增加Smb
		// 创建一个远程文件对象
		String conStr = "smb://" + account + ":" + password + "@"
				+ remoteHostIp + "/" + shareDocName;
		SmbFile remoteFile;
		try {
			remoteFile = new SmbFile(conStr + "/" + name);
			if (!remoteFile.exists()) {
				remoteFile.mkdir();
				// 创建远程文件夹
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (SmbException e) {
			e.printStackTrace();
		}
	}

	/**
	 * * 删除文件夹 * @param folderPath 共享文件夹下一个文件夹名 * @return
	 * */
	public void delFolder(String folderPath) {
		// String conStr =
		// "smb://"+LOGIN_ACCOUNT+":"+LOGIN_PASSWORD+"@"+remoteHostIp+"/"+shareDocName;
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			SmbFile myFilePath = new SmbFile(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			String message = ("删除文件夹操作出错");
			System.out.println(message);
		}
	}

	/**
	 * * 删除共享文件夹下一个文件夹名 * @param path 共享文件夹下一个文件夹名 * @return * @return
	 * */
	public boolean delAllFile(String path) {
		boolean bea = false;
		try {
			SmbFile file = new SmbFile(path);
			if (!file.exists()) {
				return bea;
			}
			if (!file.isDirectory()) {
				return bea;
			}
			String[] tempList = file.list();
			SmbFile temp = null;
			for (int i = 0; i < tempList.length; i++) {
				if (path.endsWith("/")) {
					temp = new SmbFile(path + tempList[i]);
				} else {
					temp = new SmbFile(path + "/" + tempList[i]);
				}
				if (temp.isFile()) {
					temp.delete();
				}
				if (temp.isDirectory()) {
					delAllFile(path + "/" + tempList[i] + "/");// 先删除文件夹里面的文件
					delFolder(path + "/" + tempList[i] + "/");// 再删除空文件夹
					bea = true;
				}
			}
			return bea;
		} catch (Exception e) {
			return bea;
		}
	}

	/**
	 * * 复制整个文件夹的内容 * @param oldPath 准备拷贝的目录 * @param newPath 指定绝对路径的新目录 * @return
	 * 
	 * */
	public void copyFolder(String oldPath, String newPath) {
		String conStr = "smb://" + account + ":" + password + "@"
				+ remoteHostIp + "/" + shareDocName;
		System.err.println(conStr);
		try {
			/**
			 * * 如果存在文件夹删除文件 * SmbFile exittemp = new SmbFile(conStr +
			 * "/"+newPath); * if(exittemp.exists()){ *
			 * delFolder(conStr+"/"+newPath+"/"); * }
			 * */
			SmbFile exittemps = new SmbFile(conStr + "/" + newPath);
			if (!exittemps.exists()) {
				exittemps.mkdirs(); // 如果文件夹不存在 则建立新文件夹
			}
			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			for (int i = 0; i < file.length; i++) {
				if (oldPath.endsWith("/")) {
					temp = new File(oldPath + file[i]);
				} else {
					temp = new File(oldPath + "/" + file[i]);
				}
				if (temp.isFile()) {
					if (temp.exists()) {
						writeFile(temp, newPath + "/" + file[i]);
					}
				}
				if (temp.isDirectory()) {// 如果是子文件夹
					copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
				}
			}
		} catch (Exception e) {
			String message = "复制整个文件夹内容操作出错";
			System.out.println(message);
		}
	}

	/**
	 * * 复制文件到远程计算机，如果目标路径不存在则创建，反之不创建 * @param localFileFullName 本地指定文件路径 * @param
	 * targetDir 目标路径
	 * */
	public void copyFileToRemoteDir(String localFileFullName, String targetDir) {
		System.err.println(localFileFullName + "--" + targetDir);
		RemoteFileUtil rf = new RemoteFileUtil("", "", "", "");
		InputStream is = null;
		SmbFile smbFile = null;
		OutputStream os = null;
		byte[] buffer = new byte[1024 * 8];
		// 构建连接字符串,并取得文件连接
		String conStr = null;
		conStr = "smb://" + account + ":" + password + "@" + remoteHostIp + "/"
				+ shareDocName + "/" + targetDir;
		System.err.println(conStr);
		SmbFile sf;
		try {
			sf = new SmbFile("smb://" + account + ":" + password + "@"
					+ remoteHostIp + "/" + shareDocName + "/" + targetDir);
			if (!sf.exists()) {
				// 新建目标目录
				sf.mkdirs();
				is = new FileInputStream(new File(localFileFullName));
				// 获取远程文件输出流并写文件到远程共享文件夹
				os = new BufferedOutputStream(new SmbFileOutputStream(smbFile));
				while ((is.read(buffer)) != -1) {
					os.write(buffer);
				}
			}
		} catch (Exception e) {
			System.err.println("提示：复制整个文件夹内容操作出错。");
		}
		File file = new File(localFileFullName);
		if (file.isFile()) {
			File sourceFile = file; // 源文件
			File targetFile = new File(new File(targetDir).getAbsolutePath()
					+ File.separator + file.getName());// 目标文件
			String name = file.getName();// 文件名
			if (targetDir != null && targetFile != null) {
				rf.writeFile(sourceFile, "/" + targetDir + "/" + name); // 复制文件
			} else if (targetFile != null) {
				rf.writeFile(sourceFile, name); // 复制文件
			}
		}
	}

	/**
	 * * 循环获取文件夹内所有匹配的文件 * @param strPath 路径 * @param subStr 匹配字符 * @return
	 * */
	public ArrayList<String> refreshFileList(String strPath, String subStr) {
		File dir = new File(strPath);
		File[] files = dir.listFiles();
		if (files == null)
			return null;
		for (int i = 0; i < files.length; i++) {
			if (!files[i].isDirectory()) {
//				String strFileName = files[i].getAbsolutePath().toLowerCase();
				if (files[i].getName().indexOf(subStr) >= 0) {
					filelist.add(files[i].getName());
				}
			}
		}
		return filelist;
	}

	// 测试从本地复制文件到远程目标目录，测试已通过
	public static void main(String[] args) {
		RemoteFileUtil util = new RemoteFileUtil("", "", "", "");
		util.copyFileToRemoteDir("", "");
	}

}
