###
# 执行基于用户协同过滤的预测脚本
# @author 王浩宇
#date：8.30

import numpy as np
import pandas as pd
import sys
import warnings

warnings.filterwarnings("ignore", category=Warning)

#根据用户ID获取相思用户列表
def get_sim_user_lst(userid):
    sim_user = np.load(sys.argv[1] + "/py/sim_user.npy", allow_pickle=True)
    sim_user_lst = np.load(sys.argv[1] + "/py/sim_user_lst.npy", allow_pickle=True)
    if userid not in sim_user_lst.tolist():
        return []
    ind = sim_user_lst.tolist().index(userid)
    user_arr = sim_user[ind]
    user_arr_with_id = np.zeros((2, user_arr.size))
    user_arr_with_id[0] = user_arr
    user_arr_with_id[1] = np.arange(user_arr.size)
    #     print(user_arr_with_id)
    ind_user_arr = np.argsort(user_arr_with_id, axis=1)
    #     print(ind_user_arr[0])
    user_arr_new = user_arr_with_id[:, ind_user_arr[0]]
    return user_arr_new[1, -4:-1]


#     print(user_arr_new)

#根据用户ID获取商品推荐
def get_rcmd(userid):
    user_vec = np.load(sys.argv[1] + "/py/sim_user_vec.npy", allow_pickle=True)
    userid = userid.astype(int)
    #     print(type(userid[0]))
    #     print(userid)
    #     print(user_vec[userid])
    #     print("user_vec[0].size",user_vec[0].size)
    user_vec = user_vec[userid]
    user_vec_with_gds_id = np.zeros((user_vec[:, 0].size + 2, user_vec[0].size))
    user_vec_with_gds_id[0:user_vec[:, 0].size, 0:user_vec[0].size] = user_vec
    user_vec_with_gds_id[user_vec[:, 0].size, :] = user_vec_with_gds_id.sum(axis=0)
    #     print(user_vec_with_gds_id[user_vec[:,0].size,:])
    #     print('deli')
    user_vec_with_gds_id[user_vec[:, 0].size + 1, :] = np.arange(user_vec[0].size)
    #     print(user_vec_with_gds_id)
    #     print('deli')
    #     all_rcmd = user_vec_with_gds_id[-2:,user_vec_with_gds_id[0] == 0]
    all_rcmd = user_vec_with_gds_id[-2:, :]
    rcmd_sorted_index = np.argsort(all_rcmd, axis=1)
    rcmd_sorted = all_rcmd[:, rcmd_sorted_index[0]].copy()
    #     print(rcmd_sorted[1])
    ret = rcmd_sorted[1]
    ret = ret.astype(int)
    return ret


#     print("rcmd:",all_rcmd.size)
#     print("all:",user_vec_with_gds_id.size)
#     user_vec.sum(axis=0)
#     goods

#执行用户推荐
def doUserRcmd(userid):
    sim_user_lst = get_sim_user_lst(userid)
    if sim_user_lst == []:
        return []
    # print("得到相似用户的id在列表中的位置（升序）：",sim_user_lst)
    ret = get_rcmd(sim_user_lst)
    # print("得到推荐的商品id在列表中的位置(升序)：",ret)
    return ret

#通过类型获取商品推荐
def doUserRcmdByType(userid, gdstype):
    goods_field_names = ['goods_id', 'goods_type', 'goods_price', 'goods_cnt']
    t_goods = pd.read_csv(sys.argv[1] + "/py/anal_data/t_goods.csv", names=goods_field_names)
    gds_lst = doUserRcmd(userid)
    # print("gds_lst.size = ",gds_lst.size)
    if gds_lst == []:
        return []
    arr_type = np.array(t_goods["goods_type"].tolist())
    #      t_goods["goods_type"]
    gds_lst = gds_lst[1:]
    gds_lst = gds_lst.astype(int)
    gds_lst_ind = gds_lst - 1
    arr_type = arr_type[gds_lst_ind]
    # print(arr_type)
    gds_lst_new = gds_lst[arr_type == gdstype]
    # print(arr_type=="beverages")
    # print(gds_lst[0])
    # print(gds_lst_new)
    return gds_lst_new


# print("指定类别商品推荐：",doUserRcmdByType("8","beverages"))

if int(sys.argv[3]) == 0:
    # all goods rcmd
    lst1 = doUserRcmd(sys.argv[2])
    if lst1 == []:
        print("[]")
    else:
        print(lst1[::-1][int(sys.argv[4]):int(sys.argv[4] + sys.argv[5])].tolist())
else:
    # type based rcmd
    lst2 = doUserRcmdByType(sys.argv[2], sys.argv[6])
    if lst2 == []:
        print("[]")
    else:
        print(lst2[::-1][int(sys.argv[4]):int(sys.argv[4] + sys.argv[5])].tolist())
    # print("why")

# print(doUserRcmd("12"))
