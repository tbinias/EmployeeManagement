FROM tomcat
MAINTAINER thorsten.binias

ADD dist/EmployeeManagement.war /usr/local/tomcat/webapps/

CMD ["catalina.sh", "run"]
