import numpy as np
import pandas as pd
import sys
from sklearn.metrics.pairwise import cosine_similarity

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

user_lst = t_buy["user_id"].unique()

user_vec = np.zeros((user_lst.size, t_goods.iloc[:, 0].size + 1))

print("proc gds cnt")

buy_user = t_buy.set_index("user_id")
for ind, user in enumerate(user_lst):
    ret_gds = buy_user.loc[user]
    if (type(ret_gds).__name__ == 'int'):
        user_vec[ind, ret_gds] += 1
    else:
        gds_lst = ret_gds["goods_id"].tolist()
        if (type(gds_lst).__name__ == 'int'):
            user_vec[ind, gds_lst] += 1
        else:
            for gds in gds_lst:
                user_vec[ind, gds] += 1

np.save(sys.argv[1] + "/py/sim_user_vec.npy", user_vec)

print("proc sim cal")

sim_user = cosine_similarity(user_vec)

print("proc save")

np.save(sys.argv[1] + "/py/sim_user.npy", sim_user)
np.save(sys.argv[1] + "/py/sim_user_lst.npy", user_lst)

print("proc end")
