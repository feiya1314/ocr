cd /opt/
wget http://nginx.org/download/nginx-1.24.0.tar.gz

# 解压安装包
tar -zxvf nginx-1.24.0.tar.gz

cd nginx-1.24.0

./configure --with-http_gzip_static_module --with-http_stub_status_module --with-http_ssl_module

make && make install

nginx_path=/usr/local/nginx
nginx_cmd=/usr/local/nginx/sbin/nginx

mkdir /home/ocr/nginx/
mkdir /home/ocr/nginx/conf/
mkdir /home/ocr/nginx/log/
mkdir /home/ocr/nginx/ssl/

chown -R ocr:ocr /home/ocr/nginx
useradd -m ocr


mkdir /home/ocr/jdk17
cd /home/ocr/jdk17
wget https://repo.huaweicloud.com/openjdk/17.0.2/openjdk-17.0.2_linux-x64_bin.tar.gz

tar -zxvf openjdk-17.0.2_linux-x64_bin.tar.gz
chown -R ocr:ocr /home/ocr/jdk17