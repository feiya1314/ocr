from sanic import Sanic
from sanic.response import text, html, json
from sanic_cors import CORS
from paddleocr import PaddleOCR, draw_ocr
from ocr import OcrCore
from PIL import Image
import io
import numpy as np
import base64
app = Sanic("ocr")
CORS(app)
# java 可以用Tesseract库


@app.route("/ocr", methods=["POST"])
async def ocrImage(request):
    print("receive data")

    formData = request.form
    fileData = request.files.get('file')
    fileName = fileData.name
    ocrLang = formData.get('ocrLang')
    base64Img = formData.get('base64Img')
    print(base64Img)

    fileType = fileData.type
    base64Img = base64Img.split(',')[1]
    image = base64.b64decode(base64Img)
    image = io.BytesIO(image)
    image = Image.open(image)

    imageArray = np.array(image)
 
    if ocrLang is None:
        ocrLang = 'ch'
    saveImg(fileData)

    result = OcrCore.parseImageWithPath(fileName, lang=ocrLang)
    #result = OcrCore.parseImage(imageArray, lang=ocrLang)
    return text(result)


def saveImg(file):
    imgPath = file.name
    with open(imgPath, 'wb')as f:  # 打开图片，存储
        f.write(file.body)


if __name__ == "__main__":
    app.run(host="127.0.0.1", port=8000)
