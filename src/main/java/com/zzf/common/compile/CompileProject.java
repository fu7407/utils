package com.zzf.common.compile;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNProperties;
import org.tmatesoft.svn.core.SVNProperty;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNDiffClient;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNStatus;
import org.tmatesoft.svn.core.wc.SVNStatusType;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;


public class CompileProject {
	private static final String username = "zhangzhengfu";
	private static final String password = "888888";
	private static final String sourcePath = "http://svn.easycodes.net:81/svn/YuMingWeb/trunk/YuMingWeb" ;// 工程源地址
	private static final String targetPath = "d:/YuMingWeb";// 编译工程存放地址
	private static SVNClientManager ourClientManager;// 声明SVN客户端管理类
	
	public void getSVNClientManager(){
		// 初始化支持svn://协议的库。必须先执行此操作。
		SVNRepositoryFactoryImpl.setup();
		ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
		ourClientManager = SVNClientManager.newInstance((DefaultSVNOptions) options, username, password);// 实例化客户端管理类
	}
	
	/**
	 * 此类执行的操作是把本地目录下的内容上传到版本库中。
	 */  
	public void doImport(String sourcePath) throws SVNException{
		getSVNClientManager();
		SVNURL repositoryURL = SVNURL.parseURIEncoded(sourcePath);
		// 要把此目录中的内容导入到版本库
		File impDir = new File(targetPath);
		// 执行导入操作
		SVNCommitInfo commitInfo = ourClientManager.getCommitClient().doImport(
				impDir, repositoryURL, "import operation!", null, false, false,
				SVNDepth.INFINITY);
		System.out.println(commitInfo.toString()); 
	}
	
	
	/**
	 * 此类执行的操作是把版本库中的内容check out 到本地目录中
	 */
	public void checkout(String sourcePath) throws SVNException{
		getSVNClientManager();
		SVNURL repositoryURL = SVNURL.parseURIEncoded(sourcePath);
		File wcDir = new File(targetPath);//把版本库的内容check out到的目录
		SVNUpdateClient updateClient = ourClientManager.getUpdateClient();// 通过客户端管理类获得updateClient类的实例。
		updateClient.setIgnoreExternals(false);//sets externals not to be ignored during the checkout
		long workingVersion = updateClient.doCheckout(repositoryURL, wcDir,SVNRevision.HEAD, SVNRevision.HEAD, SVNDepth.INFINITY, false);// 执行check out 操作，返回工作副本的版本号。
		System.out.println("把版本：" + workingVersion + " check out 到目录：" + wcDir + "中。");
	}
	
	/**
	 * 此类的操作是把工作副本的某个文件提交到版本库中
	 * 注意：执行此操作要先执行checkout操作。因为本地需要有工作副本此范例才能运行。
	 * @throws SVNException
	 */
	public void doCommit() throws SVNException{
		getSVNClientManager();
		// 要提交的文件
		File commitFile = new File("e:/svntest/wc/impProject/juniper_config.txt");
		// 获取此文件的状态（是文件做了修改还是新添加的文件？）
		SVNStatus status = ourClientManager.getStatusClient().doStatus(commitFile, true);
		// 如果此文件是新增加的则先把此文件添加到版本库，然后提交。
		if (status.getContentsStatus() == SVNStatusType.STATUS_UNVERSIONED) {
			// 把此文件增加到版本库中
			ourClientManager.getWCClient().doAdd(commitFile, false, false,false, SVNDepth.INFINITY, false, false);
			// 提交此文件
			ourClientManager.getCommitClient().doCommit(new File[] { commitFile }, true, "", null, null, true,false, SVNDepth.INFINITY);
			System.out.println("add");
		}else {// 如果此文件不是新增加的，直接提交。
//			 ourClientManager.getCommitClient().doCommit(
//			 new File[] { commitFile }, true, "", false, true);
			ourClientManager.getCommitClient().doCommit(new File[] { commitFile }, true, "", null, null, true,false, SVNDepth.INFINITY);
			System.out.println("commit");
		}
		System.out.println(status.getContentsStatus());
	}

	/**
	 * 此类用来把版本库中文件的某个版本更新到工作副本中
	 * 注意：执行此操作要先执行checkout操作。因为本地需要有工作副本此范例才能运行。
	 * @throws SVNException
	 */
	public void doUpdate() throws SVNException{
		getSVNClientManager();
		//要更新的文件
		File updateFile=new File("e:/svntest/wc/impProject/juniper_config.txt"); 
		//获得updateClient的实例
		SVNUpdateClient updateClient = ourClientManager.getUpdateClient(); 
		updateClient.setIgnoreExternals(false); 
		//执行更新操作
		long versionNum= updateClient.doUpdate(updateFile, SVNRevision.HEAD,SVNDepth.INFINITY,false,false); 
		System.out.println("工作副本更新后的版本："+versionNum); 
	}

	/**
	 * 此类用来比较某个文件两个版本的差异
	 * 注意：执行此操作要先执行checkout操作。因为本地需要有工作副本此范例才能运行。
	 * @throws SVNException
	 * @throws IOException 
	 */
	public void doDiff() throws SVNException, IOException{
		getSVNClientManager();
		// 要比较的文件
		File compFile = new File("e:/svntest/wc/impProject/juniper_config.txt"); 
		// 获得SVNDiffClient类的实例。
		SVNDiffClient diff = ourClientManager.getDiffClient(); 
		// 保存比较结果的输出流
		BufferedOutputStream result = new BufferedOutputStream(new FileOutputStream("E:/result.txt")); 
		// 比较compFile文件的SVNRevision.WORKING版本和SVNRevision.HEAD版本的差异，结果保存在E:/result.txt文件中。
		//SVNRevision.WORKING版本指工作副本中当前内容的版本，SVNRevision.HEAD版本指的是版本库中最新的版本。
		diff.doDiff(compFile, SVNRevision.HEAD, SVNRevision.WORKING,SVNRevision.HEAD, SVNDepth.INFINITY, true, result, null);
		result.close();
		System.out.println("比较的结果保存在E:/result.txt文件中！");
	}
	
	/**
	 * 此类用来显示版本库的树状结构。 
	 * 此类用底层API（Low Level API）直接访问版本库。  
	 * 此程序框架于1-5的示例（High Level API）稍有不同
	 * @throws SVNException
	 */
	public void displayRepositoryTree()throws SVNException{
		// 初始化支持svn://协议的库。 必须先执行此操作。
		SVNRepositoryFactoryImpl.setup();
		// 定义svn版本库的URL。
		SVNURL repositoryURL = null;
		// 定义版本库。
		SVNRepository repository = null;
		/* * 实例化版本库类 * */
		try {
			// 获取SVN的URL。
			repositoryURL = SVNURL.parseURIEncoded(sourcePath);
			// 根据URL实例化SVN版本库。
			repository = SVNRepositoryFactory.create(repositoryURL);
		} catch (SVNException svne) {
			/** 打印版本库实例创建失败的异常。 */
			System.err.println("创建版本库实例时失败，版本库的URL是 '" + sourcePath + "': "+ svne.getMessage());
			System.exit(1);
		}
		/** 对版本库设置认证信息。 */
		ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(username, password);
		repository.setAuthenticationManager(authManager);
		/*
		 * 上面的代码基本上是固定的操作。 下面的部门根据任务不同，执行不同的操作。 *
		 */
		try {
			// 打印版本库的根
			System.out.println("Repository Root: " + repository.getRepositoryRoot(true));
			// 打印出版本库的UUID
			System.out.println("Repository UUID: " + repository.getRepositoryUUID(true));
			System.out.println("");
			// 打印版本库的目录树结构
			listEntries(repository, "");
		} catch (SVNException svne) {
			System.err.println("打印版本树时发生错误: " + svne.getMessage());
			System.exit(1);
		}
		/* * 获得版本库的最新版本树 */
		long latestRevision = -1;
		try {
			latestRevision = repository.getLatestRevision();
		} catch (SVNException svne) {
			System.err.println("获取最新版本号时出错: " + svne.getMessage());
			System.exit(1);
		}
		System.out.println("");
		System.out.println("---------------------------------------------");
		System.out.println("版本库的最新版本是: " + latestRevision);
		System.exit(0);
	}
	
	 /** 
      * 此函数递归的获取版本库中某一目录下的所有条目。      
      */ 
    public void listEntries(SVNRepository repository, String path)throws SVNException { 
        //获取版本库的path目录下的所有条目。参数－1表示是最新版本。        
    	Collection<?> entries = repository.getDir(path, -1, null,(Collection<?>) null); 
        Iterator<?> iterator = entries.iterator();         
        while (iterator.hasNext()) { 
            SVNDirEntry entry = (SVNDirEntry) iterator.next(); 
            System.out.println("/" + (path.equals("") ? "" : path + "/")+ entry.getName() + " (author: '" + entry.getAuthor() + "'; revision: " + entry.getRevision() + "; date: " + entry.getDate() + ")");             
            /** 检查此条目是否为目录，如果为目录递归执行              */ 
            if (entry.getKind() == SVNNodeKind.DIR) { 
                listEntries(repository, (path.equals("")) ? entry.getName(): path + "/" + entry.getName());            
            }         
        }     
    }
    
    /**
     * 此类用来显示版本库中文件的内容。 
     * 此类用底层API（Low Level API）直接访问版本库。  
     * 此程序框架与1-5的示例稍有不同
     */
    public void displayFile(){
    	//初始化库。 必须先执行此操作。具体操作封装在setupLibrary方法中。         
    	setupLibrary();                 
        String filePath = "impProject/juniper_config.txt";         
        //定义svn版本库的URL。         
        SVNURL repositoryURL = null;         
        //定义版本库。 
        SVNRepository repository = null;         
        try { 
	        //获取SVN的URL。 
	        repositoryURL=SVNURL.parseURIEncoded(sourcePath);          
	        //根据URL实例化SVN版本库。 
	        repository = SVNRepositoryFactory.create(repositoryURL);         
        } catch (SVNException svne) {             
        	/** 打印版本库实例创建失败的异常。              */             
        	System.err.println("创建版本库实例时失败，版本库的URL是 '" + sourcePath + "': " + svne.getMessage()); 
            System.exit(1);         
        }  
        /** 对版本库设置认证信息。          */ 
        ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(username, password);         
        repository.setAuthenticationManager(authManager);  
        //此变量用来存放要查看的文件的属性名/属性值列表。         
        SVNProperties fileProperties = new SVNProperties();         
        //此输出流用来存放要查看的文件的内容。 
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        try { 
            //获得版本库中文件的类型状态（是否存在、是目录还是文件），参数-1表示是版本库中的最新版本。 
            SVNNodeKind nodeKind = repository.checkPath(filePath, -1);              
            if (nodeKind == SVNNodeKind.NONE) { 
                System.err.println("要查看的文件在 '" + sourcePath + "'中不存在.");                 
                System.exit(1); 
            } else if (nodeKind == SVNNodeKind.DIR) { 
                System.err.println("要查看对应版本的条目在 '" + sourcePath + "'中是一个目录.");                 
                System.exit(1);             
            } 
           //获取要查看文件的内容和属性，结果保存在baos和fileProperties变量中。             
            repository.getFile(filePath, -1, fileProperties, baos);  
        } catch (SVNException svne) { 
            System.err.println("在获取文件内容和属性时发生错误: " + svne.getMessage()); 
            System.exit(1);         
        }  
        //获取文件的mime-type 
        String mimeType = fileProperties.getStringValue(SVNProperty.MIME_TYPE);         
        //判断此文件是否是文本文件         
        boolean isTextType = SVNProperty.isTextMimeType(mimeType);         
        /** 显示文件的所有属性          */ 
        Iterator<?> iterator = fileProperties.nameSet().iterator();         
        while (iterator.hasNext()) { 
            String propertyName = (String) iterator.next(); 
            String propertyValue = fileProperties.getStringValue(propertyName);             
            System.out.println("文件的属性: " + propertyName + "=" + propertyValue);         
        }         
		/** 如果文件是文本类型，则把文件的内容显示到控制台。 */
		if (isTextType) {
			System.out.println("File contents:");
			System.out.println();
			try {
				baos.writeTo(System.out);
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		} else {
			System.out.println("因为文件不是文本文件，无法显示！");
		}
		/** 获得版本库的最新版本号。 */
		long latestRevision = -1;
		try {
			latestRevision = repository.getLatestRevision();
		} catch (SVNException svne) {
			System.err.println("获取最新版本号时出错: " + svne.getMessage());
			System.exit(1);
		}
		System.out.println("");
		System.out.println("---------------------------------------------");
		System.out.println("版本库的最新版本号: " + latestRevision);
		System.exit(0); 
    }
    
    /** 
     * 初始化库      
     */ 
    private void setupLibrary() {         
    	/* 
         * For using over http:// and https:// 
         */ 
        DAVRepositoryFactory.setup();         
        /* 
         * For using over svn:// and svn+xxx://          
         */ 
        SVNRepositoryFactoryImpl.setup();                  
        /* 
         * For using over file:///          
         */ 
        FSRepositoryFactory.setup();     
    }
    

	public static void main(String[] args) throws Exception {
		CompileProject cp = new CompileProject();
		cp.displayRepositoryTree();
	}

}
