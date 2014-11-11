package com.somesky;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class Sftp {

	private Session sshSession;
	private Channel channel;
	private ChannelSftp sftp;
	/**
	 * 连接sftp服务器
	 * 
	 * @param host
	 *            主机
	 * @param port
	 *            端口
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @return
	 */
	public ChannelSftp connect(String host, int port, String username,
			String password) {
		try {
			JSch jsch = new JSch();
			jsch.getSession(username, host, port);
			sshSession = jsch.getSession(username, host, port);
			System.out.println("Session created.");
			sshSession.setPassword(password);
			Properties sshConfig = new Properties();
			sshConfig.put("StrictHostKeyChecking", "no");
			sshSession.setConfig(sshConfig);
			sshSession.connect();
			System.out.println("Session connected.");
			System.out.println("Opening Channel.");
			channel = sshSession.openChannel("sftp");
			channel.connect();
			sftp = (ChannelSftp) channel;
			System.out.println("Connected to " + host + ".");
		} catch (Exception e) {

		}
		return sftp;
	}

	public void close(){
		sftp.exit();
		channel.disconnect();
		sshSession.disconnect();
	}
	
	/**
	 * 上传文件
	 * 
	 * @param directory
	 *            上传的目录
	 * @param uploadFile
	 *            要上传的文件
	 * @param sftp
	 */
	public void uploadFile(String destDirectory, String sourceFile, ChannelSftp sftp) {
		try {
			File file = new File(sourceFile);
			System.out.println("destDirectory:"+destDirectory);
			System.out.println("sourceFile:"+sourceFile);
			sftp.put(new FileInputStream(file), file.getName());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void uploadDirectory(String destDirectory, String sourceDirectory, ChannelSftp sftp) {
		String baseDir=destDirectory;
		String curPath="";
		File sourceFile=new File(sourceDirectory);
		if(!sourceFile.exists()){
			throw new RuntimeException("Directory need upload does not exist:"+sourceDirectory);
		}
		File[] subFile=sourceFile.listFiles();
		for(File itemFile:subFile){
			if(itemFile.isDirectory()){
				curPath=getPath(sourceDirectory,itemFile.getAbsolutePath());
				String remotePath=baseDir+curPath;
				cdRemoteDirectory(remotePath);
				uploadDirectory(remotePath,itemFile.getPath(),sftp);
			}else{
				cdRemoteDirectory(baseDir);
				uploadFile(baseDir,itemFile.getAbsolutePath(),sftp);
			}
		}
	}

	public void cdRemoteDirectory(String remotePath){
		//enter remote directory
		String remoteDir=getParentPath(remotePath);
		try {
			sftp.cd(remoteDir);
		} catch (SftpException e) {
			throw new RuntimeException("cd "+remoteDir+" has error!");
		}
		String dir=remotePath.replace(remoteDir+"/", "");
		try{
		    sftp.cd(dir);
		}catch(SftpException sException){
		    if(ChannelSftp.SSH_FX_NO_SUCH_FILE == sException.id){
			    try {
					sftp.mkdir(dir);
					sftp.cd(dir);
				} catch (SftpException e) {
					throw new RuntimeException(remoteDir+" mkdir "+dir+"on  has error!");
				}
		    }
		}
	}
	
	public String getParentPath(String dir){
		dir=dir.replace("\\", "/");
		if(dir.endsWith("/")) dir=dir.substring(0,dir.length()-1);
		int index=dir.lastIndexOf('/');
		return dir.substring(0,index);
	}
	
	public String getPath(String base,String filePath){
		filePath=filePath.replace("\\", "/");
		base=base.replace("\\", "/");
		String curPath=filePath.replace(base, "");
		return curPath;
	}

	public static void main(String[] args) {
		Sftp sf = new Sftp();
		String host = "192.168.206.154";
		int port = 49171;
		String username = "root";
		String password = "root";
		String directory = "/data/webapp";
		String source="F:/opensource/crm/target/crm-0.0.1-SNAPSHOT";
		ChannelSftp sftp = sf.connect(host, port, username, password);
		sf.uploadDirectory(directory, source, sftp);
		sf.close();
	}
}