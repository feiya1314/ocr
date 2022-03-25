from sanic import Sanic
from sanic.response import text, html, json
from sanic_cors import CORS
from paddleocr import PaddleOCR, draw_ocr
from ocr import OcrCore
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
    fileType = fileData.type

    if ocrLang is None:
        ocrLang = 'ch'
    saveImg(fileData)

    result = OcrCore.parseImage(fileName, lang=ocrLang)
    return text(result)


def saveImg(file):
    imgPath = file.name
    with open(imgPath, 'wb')as f:  # 打开图片，存储
        f.write(file.body)


if __name__ == "__main__":
    app.run(host="127.0.0.1", port=8000)
