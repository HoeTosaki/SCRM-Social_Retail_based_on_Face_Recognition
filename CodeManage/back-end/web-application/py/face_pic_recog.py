import os
import cv2
import face_cnn as myconv
import sys
# import face_cam_recog as mycam
import tensorflow as tf
import numpy as np

PUBLIC_PATH = sys.argv[1] + '/py'
TRAIN_DIR = os.path.join(PUBLIC_PATH+'/', 'image/train_faces')
sys.path.append(PUBLIC_PATH)
OTHER_PATH = 'Users/why/anaconda3/envs/tensorflowlow/lib/python3.6/site-packages'#第三方库路径
sys.path.append(OTHER_PATH)
# LIBRARY_PATH = 'D:/anaconda/envs/tfenv/Library/bin'
# sys.path.append(LIBRARY_PATH)

IMGSIZE = 64
TEST_FACES_DIR = os.path.join(PUBLIC_PATH+'/', 'image/test_faces')
CASE_PATH = os.path.join(PUBLIC_PATH+'/', "haarcascade_frontalface_default.xml")
savepath = os.path.join(PUBLIC_PATH+'/', 'check_point/face.ckpt')

'''创建文件夹'''
def mkDir(dir_path):
    # 去除首位空格
    dir_path = dir_path.strip()
    # 去除尾部 \ 符号
    path = dir_path.rstrip("\\")
    # if not os.path.exists(dir_path):#路径不存在
    #     os.mkdir(dir_path)
    #     #print('make dir success')
    #     return True
    # else:
    #     #print('dir_path already exists')
    #     return False
    # print('ljk st')
    os.mkdir(dir_path)
    # print('ljk ed')
    return True


'''删除文件夹下所有事物'''
def deleteAll(del_path):
    if not os.path.exists(del_path):#文件夹不存在，无需删除
        return True;
    sub_path_list = os.listdir(del_path)
    #print(sub_path_list)
    for sub_path in sub_path_list:
        sub_abs_path = os.path.join(del_path, sub_path)
        if os.path.isdir(sub_abs_path):
            #print('dir...', sub_abs_path)
            deleteAll(sub_abs_path)
        else:
            #print('path...', sub_abs_path)
            os.remove(sub_abs_path)
    os.rmdir(del_path)
    return True


def onehot(numlist):
    ''' get one hot return host matrix is len * max+1 demensions'''
    b = np.zeros([len(numlist), max(numlist) + 1])
    b[np.arange(len(numlist)), numlist] = 1
    return b.tolist()

def getfileandlabel(filedir):
    ''' get path and host paire and class index to name'''
    dictdir = dict([[name, os.path.join(filedir, name)] \
                    for name in os.listdir(filedir) if os.path.isdir(os.path.join(filedir, name))])
    # for (path, dirnames, _) in os.walk(filedir) for dirname in dirnames])

    dirnamelist, dirpathlist = dictdir.keys(), dictdir.values()
    indexlist = list(range(len(dirnamelist)))

    return list(zip(dirpathlist, onehot(indexlist))), dict(zip(indexlist, dirnamelist))

def getfilesinpath(filedir):
    ''' get all file from file directory'''
    for (path, dirnames, filenames) in os.walk(filedir):
        for filename in filenames:
            if filename.endswith('.jpg'):
                yield os.path.join(path, filename)
        for diritem in dirnames:
            getfilesinpath(os.path.join(path, diritem))

def readimage(pairpathlabel):
    '''read image to list'''
    imgs = []
    labels = []
    for filepath, label in pairpathlabel:
        for fileitem in getfilesinpath(filepath):
            img = cv2.imread(fileitem)
            imgs.append(img)
            labels.append(label)
    return np.array(imgs), np.array(labels)

def pic_recog(test_dir_path):
    user_may = []

    pathlabelpair, indextoname = getfileandlabel(os.path.join(PUBLIC_PATH+'/', 'image/train_faces'))
    output = myconv.cnnLayer(len(pathlabelpair))
    valid_x, valid_y = readimage(pathlabelpair)

    predict = output

    saver = tf.train.Saver()
    # print('in pic_recog do...............##############################################################')
    with tf.Session() as sess:
        #print('in pic_recog open...............#######################################################################')
        sess.run(tf.global_variables_initializer())
        # model_file = tf.train.latest_checkpoint(os.path.join(PUBLIC_PATH+'/', 'check_point/'))
        # saver.restore(sess, model_file)
        saver.restore(sess, savepath)
        # print('in pic_recog catch...............#######################################################################')
        face_cascade = cv2.CascadeClassifier(CASE_PATH)

        image_list = os.listdir(test_dir_path)  # 列出文件夹下所有的目录与文件
        #print('in pic_recog do...............#######################################################################')
        for image_path in image_list:
            #print(os.path.join(TEST_FACES_DIR, image_path))
            image_raw = cv2.imread(os.path.join(TEST_FACES_DIR, image_path))
            gray = cv2.cvtColor(image_raw, cv2.COLOR_BGR2GRAY)
            faces = face_cascade.detectMultiScale(gray,
                                                  scaleFactor=1.2,
                                                  minNeighbors=5,
                                                  minSize=(2, 2), )
            for f_x, f_y, f_w, f_h in faces:
                face = image_raw[f_y:f_y + f_h, f_x:f_x + f_w]
                face = cv2.resize(face, (IMGSIZE, IMGSIZE))
                # could deal with face to train
                test_x = np.array([face])
                test_x = test_x.astype(np.float32) / 255.0

                res = sess.run([predict, tf.argmax(output, 1)], \
                               feed_dict={myconv.x_data: test_x, \
                                          myconv.keep_prob_5: 1.0, myconv.keep_prob_75: 1.0})
                #cv2.imwrite(os.path.join(TEST_FACES_DIR, image_path+'_head.jpg'), face)

                #print(res)
                #print('res[1][0]:......................', indextoname[res[1][0]])
                user_may.append(indextoname[res[1][0]])
                #cv2.putText(image_raw, indextoname[res[1][0]], (f_x, f_y - 20), cv2.FONT_HERSHEY_SIMPLEX, 1, 255, 2)  # 显示名字
                image_raw = cv2.rectangle(image_raw, (f_x, f_y), (f_x + f_w, f_y + f_h), (255, 0, 0), 2)
            #print('............................................in pic_recog')
        return user_may

def retrainAll():
    #初始化check_point文件夹
    deleteAll(os.path.join(PUBLIC_PATH+'/', 'check_point'))
    mkDir(os.path.join(PUBLIC_PATH+'/', 'check_point'))

    pathlabelpair, indextoname = getfileandlabel(TRAIN_DIR)

    train_x, train_y = readimage(pathlabelpair)
    train_x = train_x.astype(np.float32) / 255.0
    myconv.train(train_x, train_y, savepath, 3)#进行三轮训练
    return True

if __name__ == '__main__':
    #print(pic_recog(TEST_FACES_DIR))
    pass

