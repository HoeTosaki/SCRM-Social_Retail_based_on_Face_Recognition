# -*- coding: utf-8 -*-
import sys
import time
from datetime import datetime, timedelta
from typing import Dict, Any
from sklearn import preprocessing
import numpy as np
import pandas as pd
import warnings

warnings.filterwarnings("ignore")

pd.set_option('display.max_columns', None)
pd.set_option('display.max_rows', None)
pd.set_option('max_colwidth', 200)
pd.set_option('expand_frame_repr', False)

today_date = datetime.today().date()
time_offset = dict(week=timedelta(7), month=timedelta(30))
# start_date_str = '2019-03-01'
# end_date_str = '2019-08-28'
# time_offset_key = 'week'
# user_id = 90  # if sys.argv.__len__() > 1 else sys.argv[1]
# goods_name = '12 Ounce Plastic Bowls'
# goods_type = 'snacks'
# goods_id = 20
# statis_index = 'cnt'

nCmd = int(sys.argv[2])

# 数据预处理
goods_field_names = ['goods_id', 'goods_type', 'goods_price', 'goods_cnt']
all_goods_info_from_csv = pd.read_csv(sys.argv[1] + "/py/anal_data/t_goods.csv", names=goods_field_names)
all_goods_info_from_csv = all_goods_info_from_csv.set_index('goods_id')

buy_list_field_name = ['buy_id', 'user_id', 'mngr_id', 'goods_id', 'buy_date', 'buy_cnt']
all_user_buy_list_from_csv = pd.read_csv(sys.argv[1] + "/py/anal_data/t_buy.csv", names=buy_list_field_name,
                                         # usecols=['user_id', 'mngr_id', 'goods_id', 'buy_cnt', 'buy_date', 'buy_id'],
                                         parse_dates=['buy_date'])

all_user_buy_list_raw = pd.merge(all_user_buy_list_from_csv, all_goods_info_from_csv, on='goods_id')
all_user_buy_list_raw.eval('total_pay = buy_cnt * goods_price', inplace=True)
all_user_buy_list_raw.drop(['goods_cnt'], axis=1, inplace=True)

all_user_buy_list = all_user_buy_list_raw.set_index('buy_date')
all_user_buy_list.drop(['goods_id'], axis=1, inplace=True)
all_user_buy_list = all_user_buy_list.sort_index(ascending=False)

tomorrow_date = today_date + timedelta(1)
today_all_user_buy_list = all_user_buy_list[tomorrow_date:today_date]
today_all_user_buy_list = today_all_user_buy_list.reset_index()

hot_goods_list_raw = all_user_buy_list_raw[['buy_date', 'goods_id', 'buy_cnt']]
hot_goods_list = hot_goods_list_raw.set_index('buy_date')
hot_goods_list = hot_goods_list.sort_index(ascending=False)
date = today_date - time_offset['month']
one_month_hot_goods_list = hot_goods_list[today_date: date]
one_month_hot_goods_list = one_month_hot_goods_list.to_period('D')


def cmd_0_function():
    if str(all_user_buy_list['user_id'].dtype) == 'int64':
        some_user_buy_list = all_user_buy_list.loc[all_user_buy_list['user_id'] == int(sys.argv[4])]
    else:
        some_user_buy_list = all_user_buy_list.loc[all_user_buy_list['user_id'] == sys.argv[4]]
    date = today_date - time_offset[sys.argv[5]] + timedelta(1)
    some_user_one_week_or_month_buy_list = some_user_buy_list[today_date: date]
    some_user_one_week_or_month_buy_list = some_user_one_week_or_month_buy_list.resample('D').sum()
    some_user_one_week_or_month_buy_list = some_user_one_week_or_month_buy_list.reset_index()
    some_user_one_week_or_month_buy_list.drop(['buy_cnt', 'goods_price'], axis=1, inplace=True)
    some_user_one_week_or_month_buy_list.rename(columns={'total_pay': 'cost'}, inplace=True)

    date_index = pd.date_range(start=date, end=today_date, name='buy_date')
    some_user_one_week_or_month_cost = pd.DataFrame(index=date_index)
    some_user_one_week_or_month_cost = some_user_one_week_or_month_cost.reset_index()
    some_user_one_week_or_month_cost = pd.merge(some_user_one_week_or_month_cost, some_user_one_week_or_month_buy_list,
                                                on=['buy_date'], how='left')
    some_user_one_week_or_month_cost = some_user_one_week_or_month_cost.fillna(0)
    some_user_one_week_or_month_cost = some_user_one_week_or_month_cost.loc[: int(sys.argv[3])]
    for index, row in some_user_one_week_or_month_cost.iterrows():
        print("{},{}".format(row['buy_date'].strftime('%Y-%m-%d'), row['cost']))


def cmd_1_function():
    if str(all_user_buy_list['user_id'].dtype) == 'int64':
        some_user_buy_list = all_user_buy_list.loc[all_user_buy_list['user_id'] == int(sys.argv[4])]
    else:
        some_user_buy_list = all_user_buy_list.loc[all_user_buy_list['user_id'] == sys.argv[4]]
    start_date = time.strftime(sys.argv[5])
    end_date = time.strftime(sys.argv[6])
    some_user_specific_time_list = some_user_buy_list[end_date: start_date]
    some_user_specific_time_list = some_user_specific_time_list.resample('D').sum()
    some_user_specific_time_list = some_user_specific_time_list.reset_index()
    some_user_specific_time_list.drop(['buy_cnt', 'goods_price'], axis=1, inplace=True)
    some_user_specific_time_list.rename(columns={'total_pay': 'cost'}, inplace=True)
    date_index = pd.date_range(start=start_date, end=end_date, name='buy_date')
    some_user_specific_time_cost = pd.DataFrame(index=date_index)
    some_user_specific_time_cost = some_user_specific_time_cost.reset_index()
    some_user_specific_time_cost = pd.merge(some_user_specific_time_cost, some_user_specific_time_list,
                                            on=['buy_date'], how='left')
    some_user_specific_time_cost = some_user_specific_time_cost.fillna(0)
    some_user_specific_time_cost = some_user_specific_time_cost.loc[: int(sys.argv[3])]
    for index, row in some_user_specific_time_cost.iterrows():
        print("{},{}".format(row['buy_date'].strftime('%Y-%m-%d'), row['cost']))


def cmd_2_function():
    if str(all_user_buy_list['user_id'].dtype) == 'int64':
        some_user_buy_list = all_user_buy_list.loc[all_user_buy_list['user_id'] == int(sys.argv[4])]
    else:
        some_user_buy_list = all_user_buy_list.loc[all_user_buy_list['user_id'] == sys.argv[4]]
    some_user_monthly_buy_list = some_user_buy_list.resample('M').sum()
    some_user_monthly_buy_list = some_user_monthly_buy_list.to_period('M')
    some_user_monthly_buy_list = some_user_monthly_buy_list.reset_index()
    some_user_monthly_buy_list.drop(['buy_cnt', 'goods_price'], axis=1, inplace=True)
    some_user_monthly_buy_list.rename(columns={'buy_date': 'month', 'total_pay': 'cost'}, inplace=True)
    some_user_monthly_buy_list = some_user_monthly_buy_list.tail(12)
    start_date = today_date - timedelta(365)
    end_date = today_date
    date_index = pd.date_range(start=start_date, end=end_date, name='month')
    some_user_monthly_cost = pd.DataFrame(index=date_index)
    some_user_monthly_cost = some_user_monthly_cost.to_period('M')
    some_user_monthly_cost = some_user_monthly_cost.reset_index()
    some_user_monthly_cost = some_user_monthly_cost.drop_duplicates(keep='first')
    some_user_monthly_cost = pd.merge(some_user_monthly_cost, some_user_monthly_buy_list,
                                      on=['month'], how='left')
    some_user_monthly_cost = some_user_monthly_cost.fillna(0)
    for index, row in some_user_monthly_cost.iterrows():
        print("{},{}".format(row['month'], row['cost']))


def cmd_3_function():
    if str(all_user_buy_list['user_id'].dtype) == 'int64':
        some_user_buy_list = all_user_buy_list.loc[all_user_buy_list['user_id'] == int(sys.argv[4])]
    else:
        some_user_buy_list = all_user_buy_list.loc[all_user_buy_list['user_id'] == sys.argv[4]]
    some_user_seasonly_buy_list = some_user_buy_list.resample('Q').sum()
    some_user_seasonly_buy_list = some_user_seasonly_buy_list.to_period('Q')
    some_user_seasonly_buy_list = some_user_seasonly_buy_list.reset_index()
    some_user_seasonly_buy_list.drop(['buy_cnt', 'goods_price'], axis=1, inplace=True)
    some_user_seasonly_buy_list.rename(columns={'buy_date': 'season', 'total_pay': 'cost'}, inplace=True)
    some_user_seasonly_buy_list = some_user_seasonly_buy_list.tail(4)
    start_date = today_date - timedelta(365)
    end_date = today_date
    date_index = pd.date_range(start=start_date, end=end_date, name='season')
    some_user_seasonly_cost = pd.DataFrame(index=date_index)
    some_user_seasonly_cost = some_user_seasonly_cost.to_period('Q')
    some_user_seasonly_cost = some_user_seasonly_cost.reset_index()
    some_user_seasonly_cost = some_user_seasonly_cost.drop_duplicates(keep='first')
    some_user_seasonly_cost = some_user_seasonly_cost.tail(4)
    some_user_seasonly_cost = pd.merge(some_user_seasonly_cost, some_user_seasonly_buy_list,
                                       on=['season'], how='left')
    some_user_seasonly_cost = some_user_seasonly_cost.fillna(0)
    for index, row in some_user_seasonly_cost.iterrows():
        print("{},{}".format(row['season'], row['cost']))


def cmd_4_function():
    if str(all_user_buy_list['user_id'].dtype) == 'int64':
        some_user_buy_list = all_user_buy_list.loc[all_user_buy_list['user_id'] == int(sys.argv[4])]
    else:
        some_user_buy_list = all_user_buy_list.loc[all_user_buy_list['user_id'] == sys.argv[4]]
    some_user_goods_type_cost = some_user_buy_list.groupby('goods_type')
    some_user_goods_type_cost = some_user_goods_type_cost['total_pay'].agg(np.sum)
    some_user_goods_type_cost_dict = {'goods_type': some_user_goods_type_cost.index,
                                      'cost': some_user_goods_type_cost.values}
    some_user_goods_type_cost = pd.DataFrame(some_user_goods_type_cost_dict)
    some_user_goods_type_cost = some_user_goods_type_cost.sort_values('cost', ascending=False)
    some_user_goods_type_cost = some_user_goods_type_cost.head(10)
    for index, row in some_user_goods_type_cost.iterrows():
        print("{},{}".format(row['goods_type'], row['cost']))


def cmd_5_function():
    if str(all_user_buy_list['user_id'].dtype) == 'int64':
        some_user_buy_list = all_user_buy_list.loc[all_user_buy_list['user_id'] == int(sys.argv[5])]
        today_some_user_buy_list = today_all_user_buy_list.loc[today_all_user_buy_list['user_id'] == int(sys.argv[5])]
    else:
        some_user_buy_list = all_user_buy_list.loc[all_user_buy_list['user_id'] == sys.argv[5]]
        today_some_user_buy_list = today_all_user_buy_list.loc[today_all_user_buy_list['user_id'] == sys.argv[5]]
    this_year_buy_list = some_user_buy_list.resample('Y').sum()
    this_year_buy_list = this_year_buy_list.tail(1)
    this_year_buy_list = this_year_buy_list.reset_index()
    print(this_year_buy_list.at[0, 'total_pay'])
    this_month_some_user_buy_list = some_user_buy_list.resample('M').sum()
    this_month_some_user_buy_list = this_month_some_user_buy_list.tail(1)
    this_month_some_user_buy_list = this_month_some_user_buy_list.reset_index()
    print(this_month_some_user_buy_list.at[0, 'total_pay'])
    today_some_user_buy_list = today_all_user_buy_list.loc[today_all_user_buy_list['user_id'] == sys.argv[5]]
    print(today_some_user_buy_list['total_pay'].sum())
    print("today_some_user_buy_list")
    today_some_user_buy_list = today_some_user_buy_list.loc[int(sys.argv[3]): int(sys.argv[3]) + int(sys.argv[4])]
    for index, row in today_some_user_buy_list.iterrows():
        print(row['buy_id'])


def cmd_6_function():
    if str(all_user_buy_list['user_id'].dtype) == 'int64':
        some_user_buy_list = all_user_buy_list.loc[all_user_buy_list['user_id'] == int(sys.argv[5])]
    else:
        some_user_buy_list = all_user_buy_list.loc[all_user_buy_list['user_id'] == sys.argv[5]]
    date = today_date - time_offset[sys.argv[6]]
    one_week_or_month_some_user_buy_list = some_user_buy_list[today_date:date]
    one_week_or_month_some_user_buy_list = one_week_or_month_some_user_buy_list.reset_index()
    one_week_or_month_some_user_buy_list.drop(['user_id'], axis=1, inplace=True)
    one_week_or_month_some_user_buy_list = one_week_or_month_some_user_buy_list.set_index('buy_date')
    one_week_or_month_some_user_buy_list = one_week_or_month_some_user_buy_list.reset_index()
    one_week_or_month_some_user_buy_list = one_week_or_month_some_user_buy_list.loc[int(sys.argv[3]): int(sys.argv[3])
                                                                                                      + int(sys.argv[4])]
    one_week_or_month_some_user_buy_list_group = one_week_or_month_some_user_buy_list.groupby('buy_date')
    index_array = []
    offset = 0
    length = len(one_week_or_month_some_user_buy_list)
    for name, group in one_week_or_month_some_user_buy_list_group:
        index_array.insert(0, '{}'.format(length - offset))
        offset += len(group)
    print(','.join(index_array))
    print("one_week_or_month_some_user_buy_list")
    for index, row in one_week_or_month_some_user_buy_list.iterrows():
        print(row['buy_id'])



def cmd_7_function():
    if str(all_user_buy_list['user_id'].dtype) == 'int64':
        some_user_buy_list = all_user_buy_list.loc[all_user_buy_list['user_id'] == int(sys.argv[5])]
    else:
        some_user_buy_list = all_user_buy_list.loc[all_user_buy_list['user_id'] == sys.argv[5]]
    start_date = time.strftime(sys.argv[6])
    end_date = time.strftime(sys.argv[7])
    specific_time_some_user_buy_list = some_user_buy_list[end_date:start_date]
    specific_time_some_user_buy_list = specific_time_some_user_buy_list.reset_index()
    specific_time_some_user_buy_list.drop(['user_id'], axis=1, inplace=True)
    specific_time_some_user_buy_list = specific_time_some_user_buy_list.set_index('buy_date')
    specific_time_some_user_buy_list = specific_time_some_user_buy_list.reset_index()
    specific_time_some_user_buy_list = specific_time_some_user_buy_list.loc[int(sys.argv[3]): int(sys.argv[3])
                                                                                              + int(sys.argv[4])]
    specific_time_some_user_buy_list_group = specific_time_some_user_buy_list.groupby('buy_date')
    index_array = []
    offset = 0
    length = len(specific_time_some_user_buy_list)
    for name, group in specific_time_some_user_buy_list_group:
        index_array.insert(0, '{}'.format(length - offset))
        offset += len(group)
    print(','.join(index_array))
    print("specific_time_some_user_buy_list")
    for index, row in specific_time_some_user_buy_list.iterrows():
        print(row['buy_id'])


def cmd_8_function():
    start_date = time.strftime(sys.argv[4])
    end_date = time.strftime(sys.argv[5])
    monthly_sale_list = all_user_buy_list.resample('M').sum()
    monthly_sale_list = monthly_sale_list.to_period('M')
    monthly_sale_list = monthly_sale_list[start_date:end_date]
    monthly_sale_list = monthly_sale_list.reset_index()
    monthly_sale_list.drop(['buy_cnt', 'goods_price'], axis=1, inplace=True)
    monthly_sale_list.rename(columns={'buy_date': 'month', 'total_pay': 'sale'}, inplace=True)

    date_index = pd.date_range(start=start_date, end=end_date, name='month')
    monthly_sale = pd.DataFrame(index=date_index)
    monthly_sale = monthly_sale.to_period('M')
    monthly_sale = monthly_sale.reset_index()
    monthly_sale = monthly_sale.drop_duplicates(keep='first')
    monthly_sale = pd.merge(monthly_sale, monthly_sale_list,
                            on=['month'], how='left')
    monthly_sale = monthly_sale.fillna(0)
    monthly_sale = monthly_sale.loc[: int(sys.argv[3])]
    for index, row in monthly_sale.iterrows():
        print('{},{}'.format(row['month'], row['sale']))


def cmd_9_function():
    seansonly_sale_list = all_user_buy_list.resample('Q').sum()
    seansonly_sale_list = seansonly_sale_list.to_period('Q')
    seansonly_sale_list = seansonly_sale_list.reset_index()
    seansonly_sale_list.drop(['buy_cnt', 'goods_price'], axis=1, inplace=True)
    seansonly_sale_list.rename(columns={'buy_date': 'season', 'total_pay': 'sale'}, inplace=True)
    seansonly_sale_list = seansonly_sale_list.tail(4)

    start_date = today_date - timedelta(365)
    end_date = today_date
    date_index = pd.date_range(start=start_date, end=end_date, name='season')
    seansonly_sale = pd.DataFrame(index=date_index)
    seansonly_sale = seansonly_sale.to_period('Q')
    seansonly_sale = seansonly_sale.reset_index()
    seansonly_sale = seansonly_sale.drop_duplicates(keep='first')
    seansonly_sale = seansonly_sale.tail(4)
    seansonly_sale = pd.merge(seansonly_sale, seansonly_sale_list, on=['season'], how='left')
    seansonly_sale = seansonly_sale.fillna(0)
    seansonly_sale = seansonly_sale.loc[: int(sys.argv[3])]
    for index, row in seansonly_sale.iterrows():
        print('{},{}'.format(row['season'], row['sale']))


def cmd_10_function():
    some_type_monthly_sale_list = all_user_buy_list.loc[all_user_buy_list['goods_type'] == sys.argv[4]]
    start_date = time.strftime(sys.argv[6])
    end_date = time.strftime(sys.argv[7])
    some_type_monthly_sale_list = some_type_monthly_sale_list.resample('M').sum()
    some_type_monthly_sale_list = some_type_monthly_sale_list.to_period('M')
    some_type_monthly_sale_list = some_type_monthly_sale_list[start_date:end_date]
    some_type_monthly_sale_list = some_type_monthly_sale_list.reset_index()
    some_type_monthly_sale_list.drop(['goods_price'], axis=1, inplace=True)
    if sys.argv[5] == 'sale':
        some_type_monthly_sale_list.drop(['buy_cnt'], axis=1, inplace=True)
        some_type_monthly_sale_list.rename(columns={'buy_date': 'month',
                                                    'total_pay': 'sale'}, inplace=True)
    elif sys.argv[5] == 'cnt':
        some_type_monthly_sale_list.drop(['total_pay'], axis=1, inplace=True)
        some_type_monthly_sale_list.rename(columns={'buy_date': 'month', 'buy_cnt': 'cnt'}, inplace=True)

    date_index = pd.date_range(start=start_date, end=end_date, name='month')
    some_type_monthly_sale = pd.DataFrame(index=date_index)
    some_type_monthly_sale = some_type_monthly_sale.to_period('M')
    some_type_monthly_sale = some_type_monthly_sale.reset_index()
    some_type_monthly_sale = some_type_monthly_sale.drop_duplicates(keep='first')
    some_type_monthly_sale = pd.merge(some_type_monthly_sale, some_type_monthly_sale_list, on=['month'], how='left')
    some_type_monthly_sale = some_type_monthly_sale.fillna(0)
    some_type_monthly_sale = some_type_monthly_sale.loc[: int(sys.argv[3])]
    for index, row in some_type_monthly_sale.iterrows():
        print('{},{}'.format(row['month'], row['sale']))


def cmd_11_function():
    goods_name = sys.argv[4].replace('_', ' ')
    goods_by_name_monthly_sale_list = all_user_buy_list.loc[all_user_buy_list['goods_name'] == goods_name]
    start_date = time.strftime(sys.argv[6])
    end_date = time.strftime(sys.argv[7])
    goods_by_name_monthly_sale_list = goods_by_name_monthly_sale_list.resample('M').sum()
    goods_by_name_monthly_sale_list = goods_by_name_monthly_sale_list.to_period('M')
    goods_by_name_monthly_sale_list = goods_by_name_monthly_sale_list[start_date:end_date]
    goods_by_name_monthly_sale_list = goods_by_name_monthly_sale_list.reset_index()
    goods_by_name_monthly_sale_list.drop(['goods_price'], axis=1, inplace=True)

    if sys.argv[5] == 'sale':
        goods_by_name_monthly_sale_list.drop(['buy_cnt'], axis=1, inplace=True)
        goods_by_name_monthly_sale_list.rename(columns={'buy_date': 'month', 'total_pay': 'sale'}, inplace=True)
    elif sys.argv[5] == 'cnt':
        goods_by_name_monthly_sale_list.drop(['total_pay'], axis=1, inplace=True)
        goods_by_name_monthly_sale_list.rename(columns={'buy_date': 'month', 'buy_cnt': 'cnt'}, inplace=True)

    date_index = pd.date_range(start=start_date, end=end_date, name='month')
    goods_by_name_monthly_sale = pd.DataFrame(index=date_index)
    goods_by_name_monthly_sale = goods_by_name_monthly_sale.to_period('M')
    goods_by_name_monthly_sale = goods_by_name_monthly_sale.reset_index()
    goods_by_name_monthly_sale = goods_by_name_monthly_sale.drop_duplicates(keep='first')
    goods_by_name_monthly_sale = pd.merge(goods_by_name_monthly_sale, goods_by_name_monthly_sale_list, on=['month'],
                                          how='left')
    goods_by_name_monthly_sale = goods_by_name_monthly_sale.fillna(0)
    goods_by_name_monthly_sale = goods_by_name_monthly_sale.loc[: int(sys.argv[3])]
    for index, row in goods_by_name_monthly_sale.iterrows():
        print('{},{}'.format(row['month'], row['sale']))


def cmd_12_function():
    goods_by_id_monthly_sale_list = all_user_buy_list_raw.loc[all_user_buy_list_raw['goods_id'] == int(sys.argv[4])]
    goods_by_id_monthly_sale_list = goods_by_id_monthly_sale_list.set_index('buy_date')
    start_date = time.strftime(sys.argv[6])
    end_date = time.strftime(sys.argv[7])
    goods_by_id_monthly_sale_list = goods_by_id_monthly_sale_list.resample('M').sum()
    goods_by_id_monthly_sale_list = goods_by_id_monthly_sale_list.to_period('M')
    goods_by_id_monthly_sale_list = goods_by_id_monthly_sale_list[start_date:end_date]
    goods_by_id_monthly_sale_list = goods_by_id_monthly_sale_list.reset_index()
    goods_by_id_monthly_sale_list.drop(['goods_price', 'goods_id'],
                                       axis=1, inplace=True)

    if sys.argv[5] == 'sale':
        goods_by_id_monthly_sale_list.drop(['buy_cnt'], axis=1, inplace=True)
        goods_by_id_monthly_sale_list.rename(columns={'buy_date': 'month', 'total_pay': 'sale'}, inplace=True)
        date_index = pd.date_range(start=start_date, end=end_date, name='month')
        goods_by_id_monthly_sale = pd.DataFrame(index=date_index)
        goods_by_id_monthly_sale = goods_by_id_monthly_sale.to_period('M')
        goods_by_id_monthly_sale = goods_by_id_monthly_sale.reset_index()
        goods_by_id_monthly_sale = goods_by_id_monthly_sale.drop_duplicates(keep='first')
        goods_by_id_monthly_sale = pd.merge(goods_by_id_monthly_sale, goods_by_id_monthly_sale_list,
                                            on=['month'], how='left')
        goods_by_id_monthly_sale = goods_by_id_monthly_sale.fillna(0)
        goods_by_id_monthly_sale = goods_by_id_monthly_sale.loc[: int(sys.argv[3])]
        for index, row in goods_by_id_monthly_sale.iterrows():
            print('{},{}'.format(row['month'], row['sale']))
    elif sys.argv[5] == 'cnt':
        goods_by_id_monthly_sale_list.drop(['total_pay'], axis=1, inplace=True)
        goods_by_id_monthly_sale_list.rename(columns={'buy_date': 'month', 'buy_cnt': 'cnt'}, inplace=True)
        date_index = pd.date_range(start=start_date, end=end_date, name='month')
        goods_by_id_monthly_sale = pd.DataFrame(index=date_index)
        goods_by_id_monthly_sale = goods_by_id_monthly_sale.to_period('M')
        goods_by_id_monthly_sale = goods_by_id_monthly_sale.reset_index()
        goods_by_id_monthly_sale = goods_by_id_monthly_sale.drop_duplicates(keep='first')
        goods_by_id_monthly_sale = pd.merge(goods_by_id_monthly_sale, goods_by_id_monthly_sale_list,
                                            on=['month'], how='left')
        goods_by_id_monthly_sale = goods_by_id_monthly_sale.fillna(0)
        goods_by_id_monthly_sale = goods_by_id_monthly_sale.loc[: int(sys.argv[3])]
        for index, row in goods_by_id_monthly_sale.iterrows():
            print('{},{}'.format(row['month'], row['cnt']))


def cmd_13_function():
    this_year_buy_list = all_user_buy_list.resample('Y').sum()
    this_year_buy_list = this_year_buy_list.tail(1)
    this_year_buy_list = this_year_buy_list.reset_index()
    print(this_year_buy_list.at[0, 'total_pay'])
    this_season_buy_list = all_user_buy_list.resample('Q').sum()
    this_season_buy_list = this_season_buy_list.tail(1)
    this_season_buy_list = this_season_buy_list.reset_index()
    print(this_season_buy_list.at[0, 'total_pay'])
    this_month_buy_list = all_user_buy_list.resample('M').sum()
    this_month_buy_list = this_month_buy_list.tail(1)
    this_month_buy_list = this_month_buy_list.reset_index()
    print(this_month_buy_list.at[0, 'total_pay'])
    print(today_all_user_buy_list['total_pay'].sum())
    today_all_user_buy_list_res = today_all_user_buy_list.loc[int(sys.argv[3]): int(sys.argv[3]) + int(sys.argv[4])]
    print("today_all_user_buy_list")
    for index, row in today_all_user_buy_list_res.iterrows():
        print(row['buy_id'])


def cmd_14_function():
    date = today_date - time_offset[sys.argv[5]]
    one_week_or_month_all_user_buy_list = all_user_buy_list[today_date:date]
    one_week_or_month_all_user_buy_list = one_week_or_month_all_user_buy_list.reset_index()
    one_week_or_month_all_user_buy_list = one_week_or_month_all_user_buy_list.loc[int(sys.argv[3]): int(sys.argv[3])
                                                                                                    + int(sys.argv[4])]
    print("one_week_or_month_all_user_buy_list")
    for index, row in one_week_or_month_all_user_buy_list.iterrows():
        print(row['buy_id'])


def cmd_15_function():
    start_date = time.strftime(sys.argv[5])
    end_date = time.strftime(sys.argv[6])
    specific_time_all_buy_list = all_user_buy_list[end_date:start_date]
    specific_time_all_buy_list = specific_time_all_buy_list.reset_index()
    specific_time_all_buy_list = specific_time_all_buy_list.loc[int(sys.argv[3]): int(sys.argv[3])
                                                                                  + int(sys.argv[4])]
    print("specific_time_all_buy_list")
    for index, row in specific_time_all_buy_list.iterrows():
        print(row['buy_id'])


def cmd_16_function():
    one_month_hot_goods_list_groups = one_month_hot_goods_list.groupby('goods_id')
    one_month_top_30_goods_list = pd.DataFrame(columns=['goods_id', 'sale_cnt'])
    for name, group in one_month_hot_goods_list_groups:
        one_month_top_30_goods_list = one_month_top_30_goods_list.append(
            [{'goods_id': name, 'sale_cnt': group['buy_cnt'].sum()}])
    one_month_top_30_goods_list = one_month_top_30_goods_list.sort_values('sale_cnt', ascending=False).head(30)
    one_month_top_30_goods_id = one_month_top_30_goods_list['goods_id'].tolist()
    one_month_top_30_goods_sale_cnt = one_month_top_30_goods_list['sale_cnt'].tolist()
    goods_sale_cnt_scaled = preprocessing.scale(one_month_top_30_goods_sale_cnt)
    goods_sale_cnt_scaled = goods_sale_cnt_scaled.tolist()
    for i in range(len(one_month_top_30_goods_list)):
        print('{},{},{}'.format(one_month_top_30_goods_id[i], one_month_top_30_goods_sale_cnt[i],
                                goods_sale_cnt_scaled[i]))
    one_month_top_30_goods_id.sort()
    gds_similar = np.load(sys.argv[1]+'/py/gds_similar.npy')
    gds_similar_scaled = preprocessing.scale(gds_similar)
    for i in range(len(one_month_top_30_goods_id)):
        j = i + 1
        while j < len(one_month_top_30_goods_id):
            rel_num = gds_similar_scaled[one_month_top_30_goods_id[i], one_month_top_30_goods_id[j]]
            if rel_num > 2:
                print("{},{},{}".format(one_month_top_30_goods_id[i], one_month_top_30_goods_id[j], rel_num))
            j += 1


def cmd_17_function():
    one_month_mngr_list = all_user_buy_list[today_date: today_date-time_offset['month']]
    one_month_mngr_list_groups = one_month_mngr_list.groupby('mngr_id')
    one_month_top_5_mnger_list = pd.DataFrame(columns=['mngr_id', 'sale'])
    for name, group in one_month_mngr_list_groups:
        one_month_top_5_mnger_list = one_month_top_5_mnger_list.append(
            [{'mngr_id': name, 'sale': group['total_pay'].sum()}]
        )
    one_month_top_5_mngr_list = one_month_top_5_mnger_list.sort_values('sale', ascending=False)
    for index, row in one_month_top_5_mngr_list.iterrows():
        print('{},{}'.format(row['mngr_id'], row['sale']))


processor: Dict[Any, None] = {
    0: cmd_0_function,
    1: cmd_1_function,
    2: cmd_2_function,
    3: cmd_3_function,
    4: cmd_4_function,
    5: cmd_5_function,
    6: cmd_6_function,
    7: cmd_7_function,
    8: cmd_8_function,
    9: cmd_9_function,
    10: cmd_10_function,
    11: cmd_11_function,
    12: cmd_12_function,
    13: cmd_13_function,
    14: cmd_14_function,
    15: cmd_15_function,
    16: cmd_16_function,
    17: cmd_17_function
}


if __name__ == '__main__':
    processor[nCmd]()
    # print(data.to_json())
