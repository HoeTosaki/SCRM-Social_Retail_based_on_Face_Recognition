import os
import sys
import logging as log
import matplotlib.pyplot as plt
import common
import numpy as np
from tensorflow.examples.tutorials.mnist import input_data
import tensorflow as tf
import cv2

PUBLIC_PATH = sys.argv[1] + '/py'
TRAIN_DIR = os.path.join(PUBLIC_PATH+'/', 'image/train_faces')
sys.path.append(PUBLIC_PATH)
OTHER_PATH = 'Users/why/anaconda3/envs/tensorflowlow/lib/python3.6/site-packages'#第三方库路径
sys.path.append(OTHER_PATH)
# LIBRARY_PATH = 'D:/anaconda/envs/tfenv/Library/bin'
# sys.path.append(LIBRARY_PATH)

savepath = os.path.join(PUBLIC_PATH+'/', 'check_point/face.ckpt')

SIZE = 64
x_data = tf.placeholder(tf.float32, [None, SIZE, SIZE, 3])
y_data = tf.placeholder(tf.float32, [None, None])

keep_prob_5 = tf.placeholder(tf.float32)
keep_prob_75 = tf.placeholder(tf.float32)

'''定义权重变量（卷积核）'''
def weightVariable(shape):
    ''' build weight variable'''
    init = tf.random_normal(shape, stddev=0.01)
    #init = tf.truncated_normal(shape, stddev=0.01)
    return tf.Variable(init)

'''定义偏置变量'''
def biasVariable(shape):
    ''' build bias variable'''
    init = tf.random_normal(shape)
    #init = tf.truncated_normal(shape, stddev=0.01)
    return tf.Variable(init)

'''卷积层'''
def conv2d(x, W):
    ''' conv2d by 1, 1, 1, 1'''
    return tf.nn.conv2d(x, W, strides=[1, 1, 1, 1], padding='SAME')

'''池化层'''
def maxPool(x):
    ''' max pooling'''
    return tf.nn.max_pool(x, ksize=[1, 2, 2, 1], strides=[1, 2, 2, 1], padding='SAME')

'''Dropout算法'''
def dropout(x, keep):
    #drop out，随机失活，防止过拟合
    return tf.nn.dropout(x, keep)

def cnnLayer(classnum):
    ''' create cnn layer'''
    #print('in cnnLayer, start')
    # ///
    # 第一层
    W1 = weightVariable([3, 3, 3, 32]) # 卷积核大小(3,3)， 输入通道(3)， 输出通道(32)
    b1 = biasVariable([32])
    conv1 = tf.nn.relu(conv2d(x_data, W1) + b1)
    pool1 = maxPool(conv1)
    # 减少过拟合，随机让某些权重不更新
    drop1 = dropout(pool1, keep_prob_5) # 32 * 32 * 32 多个输入channel 被filter内积掉了

    # 第二层
    W2 = weightVariable([3, 3, 32, 64])
    b2 = biasVariable([64])
    conv2 = tf.nn.relu(conv2d(drop1, W2) + b2)
    pool2 = maxPool(conv2)
    drop2 = dropout(pool2, keep_prob_5) # 64 * 16 * 16

    # 第三层
    W3 = weightVariable([3, 3, 64, 64])
    b3 = biasVariable([64])
    conv3 = tf.nn.relu(conv2d(drop2, W3) + b3)
    pool3 = maxPool(conv3)
    drop3 = dropout(pool3, keep_prob_5) # 64 * 8 * 8

    # 全连接层
    Wf = weightVariable([8*16*32, 512])
    bf = biasVariable([512])
    drop3_flat = tf.reshape(drop3, [-1, 8*16*32])
    dense = tf.nn.relu(tf.matmul(drop3_flat, Wf) + bf)
    dropf = dropout(dense, keep_prob_75)

    # 输出层
    Wout = weightVariable([512, classnum])
    bout = weightVariable([classnum])
    #out = tf.matmul(dropf, Wout) + bout
    out = tf.add(tf.matmul(dropf, Wout), bout)
    #print('in cnnLayer, done')
    return out


def train(train_x, train_y, tfsavepath, train_times):
    ''' train'''
    log.debug('train')
    out = cnnLayer(train_y.shape[1])
    # 计算loss
    cross_entropy = tf.reduce_mean(tf.nn.softmax_cross_entropy_with_logits(logits=out, labels=y_data))
    # 利用自适应学习率的Adam优化算法，学习率设为0.01
    train_step = tf.train.AdamOptimizer(0.01).minimize(cross_entropy)

    accuracy = tf.reduce_mean(tf.cast(tf.equal(tf.argmax(out, 1), tf.argmax(y_data, 1)), tf.float32))

    saver = tf.train.Saver()
    with tf.Session() as sess:
        sess.run(tf.global_variables_initializer())
        batch_size = 10
        num_batch = len(train_x) // 10
        for n in range(10):
            r = np.random.permutation(len(train_x))
            train_x = train_x[r, :]
            train_y = train_y[r, :]

            for i in range(num_batch):
                batch_x = train_x[i*batch_size : (i+1)*batch_size]
                batch_y = train_y[i*batch_size : (i+1)*batch_size]
                _, loss = sess.run([train_step, cross_entropy],\
                                   feed_dict={x_data:batch_x, y_data:batch_y,
                                              keep_prob_5:0.75, keep_prob_75:0.75})

                print(n*num_batch+i, loss)

        # 获取测试数据的准确率
        acc_stand = accuracy.eval({x_data:train_x, y_data:train_y, keep_prob_5:1.0, keep_prob_75:1.0})
        print('after 10 times run: accuracy is ', acc_stand)
        saver.save(sess, tfsavepath)

    for run_times in range(train_times - 1):
        print('new training starts.......................')
        saver = tf.train.Saver()
        with tf.Session() as sess:
            sess.run(tf.global_variables_initializer())
            saver.restore(sess, savepath)#恢复模型
            #在之前的基础上进行下一轮训练.........................................
            batch_size = 10
            num_batch = len(train_x) // 10
            for n in range(10):
                r = np.random.permutation(len(train_x))
                train_x = train_x[r, :]
                train_y = train_y[r, :]

                for i in range(num_batch):
                    batch_x = train_x[i * batch_size: (i + 1) * batch_size]
                    batch_y = train_y[i * batch_size: (i + 1) * batch_size]
                    _, loss = sess.run([train_step, cross_entropy], \
                                       feed_dict={x_data: batch_x, y_data: batch_y,
                                                  keep_prob_5: 0.75, keep_prob_75: 0.75})

                    print(n * num_batch + i, loss)

            # 获取测试数据的准确率
            acc = accuracy.eval({x_data: train_x, y_data: train_y, keep_prob_5: 1.0, keep_prob_75: 1.0})
            print('after 10 times run: accuracy is ', acc)
            #若准确率上升，更新训练模型
            if acc > acc_stand:
                acc_stand = acc
                saver.save(sess, tfsavepath)
            #.....................................................

def validate(test_x, tfsavepath):
    ''' validate '''
    output = cnnLayer(2)
    predict = output

    saver = tf.train.Saver()
    with tf.Session() as sess:
        saver.restore(sess, tfsavepath)
        res = sess.run([predict, tf.argmax(output, 1)],
                       feed_dict={x_data: test_x, keep_prob_5:1.0, keep_prob_75: 1.0})
        return res

if __name__ == '__main__':
    pass