package com.somesky;

import java.io.IOException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.somesky.Exec.Result;

/**
 * @goal exec
 * @requiresDependencyResolution install
 * @execute phase="package"
 * @description remote deploy project with ssh
 *
 */
public class ExecMojo extends AbstractMojo {
	/**
	 * 
     * @parameter
     */
	private String[] commands;
	/**
	 * 
     * @parameter
     */
	private String[] precommands;
	/**
	 * 
     * @parameter 
     * @required
     */
	private String host;
	/**
	 * 
     * @parameter 
     * @required
     */
	private String user;
	/**
	 * 
     * @parameter 
     * @required
     */
	private int port;
	/**
	 * 
     * @parameter 
     */
	private String passwd;
	/**
	 * 
     * @parameter 
     */
	private String identify;
	/**
	 * 
     * @parameter 
     * @required
     */
	private String destDirectory;
	
	/**
     * The maven project.
     *
     * @parameter expression="${project}"
     *  @readonly
     */
    protected MavenProject project;
	
	public void execute() throws MojoExecutionException, MojoFailureException {
		try {
			upload();
		} catch (JSchException e) {
			throw new MojoExecutionException("upload failure",e);
		} catch (IOException e) {
			throw new MojoExecutionException("can not connect ssh or execute command err:",e);
		} 
	}
	
	public void upload() throws JSchException, IOException, MojoExecutionException{
		Log log=this.getLog();
		boolean hasKey=true;
		boolean hasPasswd=true;
		if(identify!=null&&!identify.equals("")){
			hasKey=true;
		}
		if(passwd!=null&&!passwd.equals("")){
			hasPasswd=true;
		}
		if(!hasKey&&!hasPasswd){
			throw new MojoExecutionException("can not find passwd or identify set:");
		}
		
		Exec exec=new Exec();
	    if(hasKey){
	    	exec.connectWithIdentify(host, port, user, identify);
	    }else{
	    	exec.connectWithPasswd(host, port, user, passwd);
	    }
		
		if(precommands!=null){
		    Result ret=new Result();
			for(String s:precommands){
				log.info("Command:"+s);
				ret=exec.exec(s); 
				if(ret.status>0){
					log.info("Error:"+ret.err);
					throw new IOException(ret.err);
				}
				log.debug(ret.msg);
			}
		}
		
		String version=project.getVersion();
		String artifactId=project.getArtifactId();
		String source=project.getBasedir().getAbsolutePath()
				+"\\target\\"+artifactId+"-"+version;
		log.info("source dir:"+source);
		Sftp sf = new Sftp();
		if(hasKey){
			sf.connectWithIdentify(host, port, user, identify);
	    }else{
	    	sf.connectWithPasswd(host, port, user, passwd);
	    }
		log.info("destDirectory:"+destDirectory);
		sf.uploadDirectory(destDirectory, source);
		sf.close();
		
		if(commands!=null){
		    Result ret=new Result();
			for(String s:commands){
				log.info("Command:"+s);
				ret=exec.exec(s); 
				if(ret.status>0){
					log.info("Error:"+ret.err);
					throw new IOException(ret.err);
				}
				log.debug(ret.msg);
			}
		}
	}

}
