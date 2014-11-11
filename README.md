sshexec-maven-plugin
====================

基于Jsch的ssh的文件上传和命令执行的插件

pom.xml配置
<pre>
<plugin>  
	<groupId>com.somesky</groupId>
	<artifactId>sshexec</artifactId>
	<version>1.0.3</version>
	<configuration>
		<host>192.168.206.154</host>
		<user>root</user>
		<passwd>root</passwd>
		<port>49171</port>
		<destDirectory>/data/webapp</destDirectory>
		<commands>
			<command>service tomcat7 restart</command>
		</commands>
	</configuration>
	<executions>  
	  <execution>
		<goals>  
		  <goal>exec</goal>  
		</goals>  
	  </execution>  
	</executions>  
</plugin>
</pre>
      
建议单独使用该插件
mvn com.somesky:sshexec:1.0.3:exec
      
      
      
