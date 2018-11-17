:: 使用前需要配置svn和mvn命令环境变量
E:
cd F:\github\commonWeb
svn update
call mvn clean
call mvn compile
call mvn package
cd target
move /y commonWeb.war e:\
