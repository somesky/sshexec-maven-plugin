sshexec-maven-plugin
====================

基于Jsch的ssh的文件上传和命令执行的插件

pom.xml配置
&lt;plugin&gt;  
	&lt;groupId&gt;com.somesky&lt;/groupId&gt;
	&lt;artifactId&gt;sshexec&lt;/artifactId&gt;
	&lt;version&gt;1.0.3&lt;/version&gt;
	&lt;configuration&gt;
		&lt;host&gt;192.168.206.154&lt;/host&gt;
		&lt;user&gt;root&lt;/user&gt;
		&lt;passwd&gt;root&lt;/passwd&gt;
		&lt;port&gt;49171&lt;/port&gt;
		&lt;destDirectory&gt;/data/webapp&lt;/destDirectory&gt;
		&lt;commands&gt;
			&lt;command&gt;service tomcat7 restart&lt;/command&gt;
		&lt;/commands&gt;
	&lt;/configuration&gt;
	&lt;executions&gt;  
	  &lt;execution&gt;
		&lt;goals&gt;  
		  &lt;goal&gt;exec&lt;/goal&gt;  
		&lt;/goals&gt;  
	  &lt;/execution&gt;  
	&lt;/executions&gt;  
&lt;/plugin&gt; 
      
建议单独使用该插件
mvn com.somesky:sshexec:1.0.3:exec
      
      
      
