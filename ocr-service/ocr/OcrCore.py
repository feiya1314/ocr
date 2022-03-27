from paddleocr import PaddleOCR, draw_ocr
from PIL import Image
import numpy as np

# 自定义模型地址
# det_model_dir='./inference/ch_ppocr_server_v2.0_det_train',
#                rec_model_dir='./inference/ch_ppocr_server_v2.0_rec_pre',
#                cls_model_dir='./inference/ch_ppocr_mobile_v2.0_cls_train',

ocrMap = {}


def getOcrInstance(ocrLang: str):
    ocr = ocrMap.get(ocrLang)
    if(ocr is not None):
        return ocr
    ocr = PaddleOCR(use_angle_cls=True, lang=ocrLang)
    ocrMap[ocrLang] = ocr

    return ocr


def parseImageWithPath(imagePath: str, lang: str = 'ch') -> str:
    ocr = getOcrInstance(lang)
    parseResult = ocr.ocr(imagePath, cls=True)
    result = []
    print('language = %s' % (lang))
    for line in parseResult:
        text = line[1][0]
        result.append(text)
        result.append('\n')

    return ''.join(result)


def parseImage(imageArray: np.ndarray, lang: str = 'ch') -> str:
    ocr = getOcrInstance(lang)
    parseResult = ocr.ocr(imageArray, cls=True)
    result = []
    print('language = %s' % (lang))
    
    print('ocr origin result : %s' % (str(parseResult)))
    
    for line in parseResult:
        text = line[1][0]
        result.append(text)
        result.append('\n')

    resultStr = ''.join(result)
    print('ocr result : %s' % (resultStr))
    
    return resultStr


if __name__ == '__main__':
    print("test")
    img = 'C:\\Users\\86135\\Pictures\\test4.png'
    print(parseImage(img))
