#!/usr/bin/python3
# encoding: utf-8
""" 
@version: v1.0 
@author: W_H_J 
@license: Apache Licence  
@contact: 415900617@qq.com 
@software: PyCharm 
@file: gzipAndBase64.py
@time: 2019/7/9 10:09 
@describe: gzip方式压缩与解压
"""
import gzip  # 导入python gzip模块，注意名字为全小写
import sys
import os
import base64
sys.path.append(os.path.abspath(os.path.dirname(__file__) + '/' + '..'))
sys.path.append("..")


def gzip_zip_base64(content):
    """
    gzip+base64压缩
        这种方式在压缩对象比较短/小的时候效果并不明显，因为base64之后反而会变大，但是压缩对象比较大时才有效果
    :param content:
    :return:
    """
    bytes_com = gzip.compress(str(content).encode("utf-8"))
    base64_data = base64.b64encode(bytes_com)
    back = str(base64_data.decode())
    return back, sys.getsizeof(back)
    # 对gzip文件的读写操作
    # with gzip.open('./log/zip-test.gz', 'wb') as write:
    #     write.write(str(back).encode("utf-8"))


def gzip_unzip_base64(content):
    """
    base64+gzip解压
    :param content:
    :return:
    """
    base64_data = base64.b64decode(content)
    bytes_decom = gzip.decompress(base64_data)
    str_unzip = bytes_decom.decode()
    return str_unzip, sys.getsizeof(str_unzip)


def gzip_zip(content):
    """
    gzip方式压缩数据
    :param content: 要压缩对象
    :return: [0, 1]: 0:压缩对象；1：压缩后大小
    """
    bytes_com = gzip.compress(str(content).encode("utf-8"))
    return bytes_com, sys.getsizeof(bytes_com)
    # print(bytes_com)
    # with gzip.open('./lognew/zip-base-test.gz', 'wb') as write:
    #     write.write(str(content).encode("utf-8"))


def gzip_unzip(content):
    """
    gzip unzip 方式解压数据
    :param content: 要解压对象
    :return:
    """
    bytes_decom = gzip.decompress(content).decode()
    return bytes_decom, sys.getsizeof(bytes_decom)


if __name__ == '__main__':
    str_test = "python 实现gzip压缩并base编码"
    # sys.getsizeof()  # 返回该对象的在内存中所占字节大小

    # 原始方式压缩结果
    long_str = len(str_test)
    size_str = sys.getsizeof(str_test)
    gzip_str = gzip_zip(str_test)
    gunzip_str = gzip_unzip(gzip_str[0])
    print("原始字符串：{0}； \n压缩后字符串：{1}；\n解压后对象：{2}；".format(
        str_test, gzip_str[0], gunzip_str[0]))
    print("原始字符串长度：{0}；\n原始字符串占内存字节大小：{1}；\n压缩后占内存字节大小：{2}；\n解压后占内存大小：{3}".format(
        long_str, size_str, gzip_str[1], gunzip_str[1]))
