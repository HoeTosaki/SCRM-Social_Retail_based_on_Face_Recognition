import numpy as np
import pandas as pd
import sys

def get_rcmd(goodsid):
#     print(gds_lst)
    t_buy = pd.read_csv(sys.argv[1]+"/py/anal_data/t_buy.csv")
    gds_lst = t_buy["goods_id"].unique()
    gds_similar = np.load(sys.argv[1]+"/py/gds_similar.npy")
    sim_rate_arr = gds_similar[goodsid]
    sim_rate_arr[goodsid] = 0
#     print(sim_rate_arr)
    arr_rcmd = np.zeros((2,gds_lst.size+1))
    arr_rcmd[0] = sim_rate_arr
    arr_rcmd[1] = np.arange(gds_lst.size+1)
#     print(arr_rcmd)
    ind_rmcd = np.argsort(arr_rcmd,axis = 1)
    sorted_rcmd = arr_rcmd[:,ind_rmcd[0]]
    # print("rcmd res:",sorted_rcmd)
    ret = sorted_rcmd[1,1:]
    ret = ret.astype(int)
    return ret

if int(sys.argv[3]) == 0:
    # all goods rcmd
    lst = get_rcmd(int(sys.argv[2]))
    if lst == []:
        print("[]")
    else:
        print(lst[::-1][int(sys.argv[4]):int(sys.argv[4]+sys.argv[5])].tolist())
    # print("why")
# get_rcmd(1)
