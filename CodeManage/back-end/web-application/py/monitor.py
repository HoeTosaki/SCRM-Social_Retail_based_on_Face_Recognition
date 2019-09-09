# -*- coding: utf-8 -*-
# @File  : monitor.py
# @Author: Liu Yanhe
# @Date  : 2019/9/7
# @Desc  : 服务器监控脚本


import psutil as pt
import time
import sys
from _datetime import datetime
from decimal import Decimal
from _datetime import datetime


def get_basic_info():
    print(datetime.fromtimestamp(pt.boot_time()).strftime("%Y-%m-%d %H:%M:%S"))  # 获取最近开机时间
    print(pt.cpu_count())  # 获取CPU逻辑数量
    print(pt.cpu_count(logical=False))  # 获取CPU物理核心数量
    print(round(pt.virtual_memory().total/1024/1024/1024, 2))  # 获取内存容量，单位：G
    print(round(pt.disk_usage('/').total/1024/1024/1024, 2))  # 获取磁盘总量信息


def get_real_time_hardware_info():
    pt.cpu_percent()
    time.sleep(1)
    print(pt.cpu_percent())  # 获取当前CPU利用率
    print(pt.virtual_memory().percent)  # 获取当前内存占用率
    print(pt.disk_usage('/').percent)  # 获取当前磁盘占用率


def get_real_time_disk_rate_info():
    read_start = pt.disk_io_counters().read_bytes
    write_start = pt.disk_io_counters().write_bytes
    time_start = time.time()
    time.sleep(1)
    read_end = pt.disk_io_counters().read_bytes
    write_end = pt.disk_io_counters().write_bytes
    now = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    time_end = time.time()
    time_offset = time_end - time_start

    print(now)
    print(round((read_end - read_start)/1024/time_offset, 2))
    print(round((write_end - write_start)/1024/time_offset, 2))


def get_real_time_net_rate_info():
    time_send_start = time.time()
    send_start = pt.net_io_counters().bytes_sent
    time.sleep(1)
    send_end = pt.net_io_counters().bytes_sent
    now = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    time_send_end = time.time()
    send_time_offset = time_send_end - time_send_start

    print(now)
    print(round((send_end - send_start) / 1024 / send_time_offset, 2))


monitor = {
    18: get_basic_info,
    19: get_real_time_hardware_info,
    20: get_real_time_disk_rate_info,
    21: get_real_time_net_rate_info
}


nCmd = int(sys.argv[2])


if __name__ == '__main__':
    monitor[nCmd]()
