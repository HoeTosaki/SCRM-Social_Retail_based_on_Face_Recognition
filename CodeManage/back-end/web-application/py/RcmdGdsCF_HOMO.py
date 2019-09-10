###
# 执行基于商品协同过滤的建模脚本
# @author 王浩宇
#date：8.29

import numpy as np
import pandas as pd
import math
import sys

#数据导入
user_field_names = ['user_id']
t_user = pd.read_csv(sys.argv[1] + "/py/anal_data/t_user.csv", names=user_field_names, dtype={
    "user_id": str
})

goods_field_names = ['goods_id', 'goods_type', 'goods_price', 'goods_cnt']
t_goods = pd.read_csv(sys.argv[1] + "/py/anal_data/t_goods.csv", names=goods_field_names)

buy_list_field_name = ['buy_id', 'user_id', 'mngr_id', 'goods_id', 'buy_date', 'buy_cnt']
t_buy = pd.read_csv(sys.argv[1] + "/py/anal_data/t_buy.csv", names=buy_list_field_name, dtype={
    "user_id": str
})
gds_lst = t_buy["goods_id"].unique()

t_buy_cnt = t_buy[["user_id", "goods_id"]].copy().groupby(by=["user_id"]).count()

t_buy_cnt.rename(columns={
    "goods_id": "goods_cnt"
}, inplace=True)

rev_gds = {}

#构建商品倒置字典
def add_dict(x):
    #     print(x["user_id"],"\t",x["goods_id"])
    #     print("x为：",x)
    if x["goods_id"] in rev_gds:
        dict_user = rev_gds[x["goods_id"]]
        if x["user_id"] in dict_user:
            dict_user[x["user_id"]] += 1.0 / t_buy_cnt.loc[x["user_id"], "goods_cnt"]
        else:
            dict_user[x["user_id"]] = 1.0 / t_buy_cnt.loc[x["user_id"], "goods_cnt"]
    else:
        rev_gds[x["goods_id"]] = {}
        rev_gds[x["goods_id"]][x["user_id"]] = 1.0 / t_buy_cnt.loc[x["user_id"], "goods_cnt"]


#     print("当前字典状态：",rev_gds)


t_buy.apply(add_dict, axis=1)
pass
# print(rev_gds)

gds_frm = pd.DataFrame(rev_gds)
gds_frm = gds_frm.fillna(value=0)

gds_sim = np.zeros((t_goods["goods_id"].size + 1, t_goods["goods_id"].size + 1))

#计算商品相似度
def gen_sim_rate(x):
    #     print(x.iloc[0],"\t",x.iloc[1])
    if x.iloc[0] == 0:
        return 0
    elif x.iloc[1] == 0:
        return 0
    elif x.iloc[0] == x.iloc[1]:
        return x.iloc[0]
    else:
        return math.sqrt(x.iloc[0] * x.iloc[1])


# def gen_sim_rate_ona(x):
# #     print(x.iloc[0],"\t",x.iloc[1])
#     return math.sqrt(x.iloc[0]*x.iloc[0])

for i in range(gds_frm.columns.size):
    for j in range(gds_frm.columns.size):
        if i == j:
            #             cur_frm = gds_frm.iloc[:,[i]]
            #             new_frm = cur_frm.apply(gen_sim_rate_ona,axis=1)
            new_frm = gds_frm.iloc[:, i]
            #             print("单列：",new_frm)
            gds_sim[gds_frm.columns[i], gds_frm.columns[i]] = new_frm.sum(axis=0)
        else:
            cur_frm = gds_frm.iloc[:, [i, j]]
            new_frm = cur_frm.apply(gen_sim_rate, axis=1)
            gds_sim[gds_frm.columns[i], gds_frm.columns[j]] = new_frm.sum()
    print("1one pass for ", i)

#构建商品相似矩阵
gds_similar = np.zeros((t_goods.size + 1, t_goods.size + 1))
for i in range(1, t_goods.iloc[:, 0].size + 1):
    for j in range(1, t_goods.iloc[:, 0].size + 1):
        q = math.sqrt(gds_sim[i, i]) * math.sqrt(gds_sim[j, j])
        if q != 0:
            gds_similar[i, j] = gds_sim[i, j] / q
        else:
            gds_similar[i, j] = 0
    print("2one pass for", i)


#保存商品相似矩阵
np.save("gds_similar.npy", gds_similar)
