import shutil
import sys,os

def doCarry():
    print("one:\t",sys.argv[1]+"/anal_data/t_user.csv")
    print("sec:\t",sys.argv[1]+"/py/anal_data/t_user.csv")
    shutil.move(sys.argv[1]+"/anal_data/t_user.csv", sys.argv[1]+"/py/anal_data/t_user.csv")
    shutil.move(sys.argv[1]+"/anal_data/t_goods.csv", sys.argv[1]+"/py/anal_data/t_goods.csv")
    shutil.move(sys.argv[1]+"/anal_data/t_buy.csv", sys.argv[1]+"/py/anal_data/t_buy.csv")

doCarry()

print("completed!")


