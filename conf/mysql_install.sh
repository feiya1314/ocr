#wget https://repo.huaweicloud.com/mysql/Downloads/MySQL-5.7/mysql-5.7.37-1.el7.x86_64.rpm-bundle.tar
#
#
##依赖
#[root@VM-8-7-centos mysql]# rpm -qa|grep libaio
#libaio-0.3.109-13.el7.x86_64
#[root@VM-8-7-centos mysql]# rpm -qa|grep net-tools
#net-tools-2.0-0.25.20131004git.el7.x86_64
#
#yum -y install numactl
#
#rpm -ivh mysql-community-common-5.7.37-1.el7.x86_64.rpm --nodeps --force
#rpm -ivh mysql-community-libs-5.7.37-1.el7.x86_64.rpm --nodeps --force
#rpm -ivh mysql-community-server-5.7.37-1.el7.x86_64.rpm --nodeps --force
#rpm -ivh mysql-community-client-5.7.37-1.el7.x86_64.rpm --nodeps --force
#
#systemctl start mysqld
#systemctl status mysqld.service
#

#rpm -e 包名 --删除

#rpm -e mysql-community-server
#rpm -e mysql-community-client
#rpm -e mysql-community-libs
#rpm -e mysql-community-common

#下载 MySQL yum包
wget http://repo.mysql.com/mysql57-community-release-el7-10.noarch.rpm
#安装MySQL源
rpm -Uvh mysql57-community-release-el7-10.noarch.rpm
#安装证书
rpm --import https://repo.mysql.com/RPM-GPG-KEY-mysql-2022
#安装MySQL服务端
yum install -y mysql-community-server
#删除目录已有文件
#mv var/lib/mysql var/lib/mysqlbak
#启动MySQL
systemctl start mysqld.service
#检查是否启动成功
systemctl status mysqld.service
#获取临时密码，MySQL5.7为root用户随机生成了一个密码
grep 'temporary password' /var/log/mysqld.log


#修改密码
ALTER USER'root'@'%'IDENTIFIED BY 'your_password';


CREATE USER 'username'@'host' IDENTIFIED BY 'password';
CREATE USER 'ocr'@'%' IDENTIFIED BY 'pppppp';
GRANT all privileges ON ocr.* TO 'ocr'@'%';