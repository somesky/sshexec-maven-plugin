sshexec-maven-plugin
====================

基于Jsch的ssh的文件上传和命令执行的插件

pom.xml配置</br>
&lt;plugin&gt; </br> 
&nbsp;&lt;groupId&gt;com.somesky&lt;/groupId&gt;</br>
&nbsp;&lt;artifactId&gt;sshexec&lt;/artifactId&gt;</br>
&nbsp;&lt;version&gt;1.0.3&lt;/version&gt;</br>
&nbsp;&lt;configuration&gt;</br>
&nbsp;&nbsp;&lt;host&gt;192.168.206.154&lt;/host&gt;</br>
&nbsp;&nbsp;&lt;user&gt;root&lt;/user&gt;</br>
&nbsp;&nbsp;&lt;passwd&gt;root&lt;/passwd&gt;</br>
&nbsp;&nbsp;&lt;port&gt;49171&lt;/port&gt;</br>
&nbsp;&nbsp;&lt;destDirectory&gt;/data/webapp&lt;/destDirectory&gt;</br>
&nbsp;&nbsp;&lt;commands&gt;</br>
&nbsp;&nbsp;&nbsp;&lt;command&gt;service tomcat7 restart&lt;/command&gt;</br>
&nbsp;&nbsp;&lt;/commands&gt;</br>
&nbsp;&lt;/configuration&gt;</br>
&nbsp;&lt;executions&gt;  </br>
&nbsp;&nbsp;&lt;execution&gt;</br>
&nbsp;&nbsp;&lt;goals&gt;  </br>
&nbsp;&nbsp;&nbsp; &lt;goal&gt;exec&lt;/goal&gt;  </br>
&nbsp;&nbsp;&lt;/goals&gt;  </br>
&nbsp;&nbsp;&lt;/execution&gt; </br> 
&nbsp;&lt;/executions&gt;  </br>
&lt;/plugin&gt; </br>
      
建议单独使用该插件
mvn com.somesky:sshexec:1.0.3:exec
      
      
      
