package com.somesky;
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class Exec{
	
	private Session session;
	private JSch jsch;
	
	public void connect(String host, int port, String user) throws JSchException {
		session=jsch.getSession(user, host, port);
		Properties sshConfig = new Properties();
		sshConfig.put("StrictHostKeyChecking", "no");
		session.setConfig(sshConfig);
		session.connect();
	}
	
	public void connectWithPasswd(String host, int port, String user,
			String password) throws JSchException {
		jsch=new JSch();  
		session.setPassword(password);
		connect(host,port,user);
	}
	
	public void connectWithIdentify(String host, int port, String user,
			String key) throws JSchException {
		jsch=new JSch(); 
		jsch.addIdentity(key);
		connect(host,port,user);
	}
	
	public void close(){
		 session.disconnect();
	}
	
	public Result exec(String command) throws JSchException, IOException{
		Channel channel=session.openChannel("exec");
	    ((ChannelExec)channel).setCommand(command);
	    channel.setInputStream(null);
	    InputStream in=channel.getInputStream();
	    InputStream err=((ChannelExec)channel).getErrStream();
	    channel.connect();
	    Result r=out(in, err, channel);
	    channel.disconnect();
	    return r;
	}

	public Result out(InputStream in,InputStream err,Channel channel) throws IOException {
		Result ret=new Result();
		StringBuffer msg=new StringBuffer();
		StringBuffer errMsg=new StringBuffer();
		byte[] tmpIn=new byte[1024];
		byte[] tmpErr=new byte[1024];
		while(true){
			while(in.available()>0){
				int i=in.read(tmpIn, 0, 1024);
				if(i<0)break;
				msg.append(new String(tmpIn, 0, i));
	        }
			while(err.available()>0){
				int i=err.read(tmpErr, 0, 1024);
				if(i<0)break;
				errMsg.append(new String(tmpErr, 0, i));
	        }
	        if(channel.isClosed()){
	        	if(in.available()>0) continue;
	        	ret.status=channel.getExitStatus();
	        	break;
	        }
	        try{Thread.sleep(1000);}catch(Exception ee){}
	      }
	      ret.msg=msg.toString();
	      ret.err=errMsg.toString();
	      return ret;
	}
  
	public static class Result{
		int status;
		String msg;
		String err;
	}

	  public static void main(String[] arg) throws Exception{
		  String host="10.0.0.70";
	      String user="root";
	      // String passwd="76712144";
	      String key="C:/Users/Administrator/Desktop/key/prox/id_rsa";
	      int port=22;
	      Exec exec=new Exec();
	      exec.connectWithIdentify(host, port, user, key);
	      Result r=exec.exec("ps -ef");
	      System.out.println("Msg:"+r.msg);
	      System.out.println("Error:"+r.err);
	      System.out.println("Status:"+r.status);
	      exec.close();
	  }
	
}