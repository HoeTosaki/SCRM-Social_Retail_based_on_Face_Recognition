import os
import sys
import cv2
import numpy as np
import image_base64_parse as bspar
import face_pic_recog as myFcReg
import warnings
warnings.filterwarnings("ignore")

IMGSIZE = 64
PUBLIC_PATH = sys.argv[1] + '/py'
TEMP_DIR = os.path.join(PUBLIC_PATH+'/', 'image/temp_faces')
TRAIN_DIR = os.path.join(PUBLIC_PATH+'/', 'image/train_faces')
TEST_DIR = os.path.join(PUBLIC_PATH+'/', 'image/test_faces')
CASE_PATH = os.path.join(PUBLIC_PATH+'/', "haarcascade_frontalface_default.xml")
face_cascade = cv2.CascadeClassifier(CASE_PATH)

sys.path.append(PUBLIC_PATH)
OTHER_PATH = '/Users/why/anaconda3/envs/tensorflowlow/lib/python3.6/site-packages'#第三方库路径
sys.path.append(OTHER_PATH)

# LIBRARY_PATH = 'D:/anaconda/envs/tfenv/Library/bin'
# sys.path.append(LIBRARY_PATH)

# CASE_PATH = "haarcascade_frontalface_default.xml"
# savepath = './check_point/face.ckpt'
#print('in python_manage')

'''获取所有用户id'''
def getAllUser():
    users = []
    image_list = os.listdir(TRAIN_DIR)
    for image_path in image_list:
        users.append(image_path)
    return users

'''获取某用户的人脸图片数'''
def getFaceNum(user_id):
    img_count = 0;
    img_list = os.listdir(os.path.join(TRAIN_DIR+'/', user_id))
    for img_path in img_list:
        img_count += 1
    return img_count

'''检测图片是否存在人脸'''
def containFace(img_base64_path, user_id):
    image_raw = cv2.imread(img_base64_path)
    gray = cv2.cvtColor(image_raw, cv2.COLOR_BGR2GRAY)
    faces = face_cascade.detectMultiScale(gray, scaleFactor=1.2, minNeighbors=5, minSize=(2, 2), )
    for f_x, f_y, f_w, f_h in faces:
        face = image_raw[f_y:f_y + f_h, f_x:f_x + f_w]
        face = cv2.resize(face, (IMGSIZE, IMGSIZE))
        addFaceForTrain(face, user_id)#人脸保存至训练集
        return True
    return False

'''人脸保存至训练集'''
def addFaceForTrain(img, user_id):
    if not os.path.exists(os.path.join(TRAIN_DIR+'/', user_id)):
        myFcReg.mkDir(os.path.join(TRAIN_DIR + '/', user_id))  # 确保文件夹存在

    pic_num = getFaceNum(user_id) + 1#获取该用户已保存的人脸数目
    cv2.imwrite(os.path.join(TRAIN_DIR+'/', user_id+'/',  user_id +'_'+str(pic_num)+'.jpg'), img)

# '''添加新用户'''
# def addUser(user_id, *img_base64_str):
#     if not mkDir(os.path.join(TRAIN_DIR, user_id)):#创建失败，用户已经存在
#         #print('user already exists')
#         return False
#     else:
#         img_count = 1
#         for img in img_base64_str:
#             #print(TRAIN_DIR)
#             #print(os.path.join(TRAIN_DIR+'/', user_id+'/', user_id+'_'+str(img_count)+'.jpg'))
#             bspar.parse_base64_str(img, os.path.join(TRAIN_DIR+'/', user_id+'/', user_id+'_'+str(img_count)+'.jpg'))
#             #bspar.test_parse(img, os.path.join(TRAIN_DIR+'/', user_id+'/', user_id+'_'+str(img_count)+'.jpg'))
#             img_count += 1
#         return True

'''删除用户'''
def deleteUser(user_id):
    delete_path = os.path.join(TRAIN_DIR+'/', user_id)
    myFcReg.deleteAll(delete_path)
    return True

'''更新用户'''
def updateUser(user_id, img_base64_str):
    #初始化temp_faces目录
    myFcReg.deleteAll(TEMP_DIR)
    myFcReg.mkDir(TEMP_DIR)
    # 将base64转为jpg，存入temp_faces目录
    temp_face_path = os.path.join(TEMP_DIR + '/', user_id  + '.jpg')
    bspar.parse_base64_str(img_base64_str, temp_face_path)
    # 验证图片是否能检测到人脸,若检测到，存入用户训练文件夹
    return containFace(temp_face_path, user_id)

'''验证人脸'''
def faceRecog(*img_base64_str):
    if os.path.exists(TEST_DIR):
        myFcReg.deleteAll(TEST_DIR)
    myFcReg.mkDir(TEST_DIR)
    img_count = 1
    for img in img_base64_str:
        #先将人脸保存至验证文件夹
        bspar.parse_base64_str(img, os.path.join(TEST_DIR + '/', str(img_count) + '.jpg'))
        #print('............................save!')
        img_count += 1
    #验证人脸,返回每张图片的用户id
    #print('in python faceRecog, user_id###########')
    #print('in python faceRecog, user_id......', myFcReg.pic_recog(TEST_DIR))
    return myFcReg.pic_recog(TEST_DIR)[0]

base64_liu_str = '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'

def main():
    # print('why1'+'\t'+sys.argv[2])
    if (sys.argv[2] == 'UPDATE'):
        # print('python call update')
        if updateUser(sys.argv[3], sys.argv[4]):
            print("SUC")
            #...........................myFcReg.retrainAll()#训练放在什么时候？
        else:
            print("FAIL")
    elif (sys.argv[2] == 'DELETE'):
        if deleteUser(sys.argv[3]):
            print("SUC")
        else:
            print("FAIL")
    elif (sys.argv[2] == 'QUERY'):
        # print('why11')
        #print('in python')
        # for user_id in faceRecog(sys.argv[2]):
        #     print(user_id)
        #print(sys.argv[2])
        #print(faceRecog(base64_liu_str))
        print(faceRecog(sys.argv[3]))

#main()
# print('why3')
if __name__ == '__main__':
    # print('why2')
    main()
    #print(faceRecog(base64_liu_str))
    # myFcReg.retrainAll()
#     print('in python, arg_0...', sys.argv[0])
    # print('arg_2...', sys.argv[2])
    # print('arg_3...', sys.argv[3])
#     users = getAllUser()
#     print(users)
    #mkDir(os.path.join(PUBLIC_PATH, "mkDir_test"))
#base64_liu_str = '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'
#updateUser('test_liu', base64_liu_str)
    #deleteUser('test_liu')
    # print(faceRecog(base64_liu_str))
