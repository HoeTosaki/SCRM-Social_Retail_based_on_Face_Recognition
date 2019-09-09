#from PIL import Image
import base64
import os
import sys

img_jpg_path = 'image/test_faces/2.jpg'
img_base64_path = 'image/2_base64.txt'
img_jpg_path_new = 'image/2_new_faces.jpg'

base64_liu_str = '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'
def parse_jpg(source_path, save_path):
    with open(source_path, "rb") as f:  # 转为二进制格式
        base64_data = base64.b64encode(f.read()).decode()  # 使用base64进行加密
        # print(base64_data)
        file = open(save_path, 'wt')  # 写成文本格式
        file.write(base64_data)
        file.close()
    #print('jpg to base64, done.')

def parse_base64_txt(source_path, save_path):
    with open(source_path, "r") as f:
        #print('base64 is opening with python')
        imgdata = base64.b64decode(f.read())
        file = open(save_path, 'wb')
        file.write(imgdata)
        file.close()
    #print('python is done')

def parse_base64_str(source_str, save_path):
    #print(save_path)
    #print('base64 is opening with python')
    with open(save_path,'wb') as f:
        f.write(base64.b64decode(source_str))
    #print('base64 has been saved as jpg with python')

def test_parse(source_str, save_path):
    # print('save_path:......'+save_path)
    # save_path = 'C:/Users/94843/PycharmProjects/FaceLearning/image/train_faces/test_liu/test_liu_1.jpg'
    # source_str = base64_liu_str
    parse_base64_str(source_str, save_path)

if __name__ == '__main__':
    #parse_jpg(img_jpg_path, img_base64_path)
    #parse_base64_txt(img_base64_path, img_jpg_path_new)
    #test_parse('a', 'b')
    pass
    #parse_base64_str(base64_liu_str, img_jpg_path_new)
    #liu_path = 'C:/Users/94843/PycharmProjects/FaceLearning/image/train_faces/test_liu/test_liu_1.jpg'
    #parse_base64_str(base64_liu_str, liu_path)