sshexec-maven-plugin
====================

基于Jsch的ssh的文件上传和命令执行的插件

pom.xml配置
&lt;plugin&gt;  
&nbsp;&lt;groupId&gt;com.somesky&lt;/groupId&gt;
&nbsp;&lt;artifactId&gt;sshexec&lt;/artifactId&gt;
&nbsp;&lt;version&gt;1.0.3&lt;/version&gt;
&nbsp;&lt;configuration&gt;
&nbsp;&nbsp;&lt;host&gt;192.168.206.154&lt;/host&gt;
&nbsp;&nbsp;&lt;user&gt;root&lt;/user&gt;
&nbsp;&nbsp;&lt;passwd&gt;root&lt;/passwd&gt;
&nbsp;&nbsp;&lt;port&gt;49171&lt;/port&gt;
&nbsp;&nbsp;&lt;destDirectory&gt;/data/webapp&lt;/destDirectory&gt;
&nbsp;&nbsp;&lt;commands&gt;
&nbsp;&nbsp;&nbsp;&lt;command&gt;service tomcat7 restart&lt;/command&gt;
&nbsp;&nbsp;&lt;/commands&gt;
&nbsp;&lt;/configuration&gt;
&nbsp;&lt;executions&gt;  
&nbsp;  &lt;execution&gt;
&nbsp;&nbsp;&lt;goals&gt;  
&nbsp;&nbsp;  &lt;goal&gt;exec&lt;/goal&gt;  
&nbsp;&nbsp;&lt;/goals&gt;  
&nbsp;  &lt;/execution&gt;  
&nbsp;&lt;/executions&gt;  
&lt;/plugin&gt;
      
建议单独使用该插件
mvn com.somesky:sshexec:1.0.3:exec
      
      
      
