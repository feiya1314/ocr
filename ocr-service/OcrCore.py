from paddleocr import PaddleOCR, draw_ocr
from PIL import Image

# 自定义模型地址
# det_model_dir='./inference/ch_ppocr_server_v2.0_det_train',
#                rec_model_dir='./inference/ch_ppocr_server_v2.0_rec_pre',
#                cls_model_dir='./inference/ch_ppocr_mobile_v2.0_cls_train',
ocr = PaddleOCR(use_angle_cls=True, lang="ch")


def parseImage(imagePath: str) -> str:
    parseResult = ocr.ocr(imagePath, cls=True)
    result = []
    
    for line in parseResult:
        text = line[1][0]
        result.append(text)
        result.append('\n')
        
    return ''.join(result)


if __name__ == '__main__':
    print("test")
    img = 'C:\\Users\\86135\\Pictures\\test4.png'
    print(parseImage(img))
