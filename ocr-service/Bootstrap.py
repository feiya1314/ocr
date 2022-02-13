from sanic import Sanic
from sanic.response import text, html, json
from sanic_cors import CORS
from paddleocr import PaddleOCR, draw_ocr
from ocr import OcrCore
app = Sanic("ocr")
CORS(app)


@app.route("/ocr", methods=["POST"])
async def ocrImage(request):
    print("receive data")

    formData = request.form
    fileData = request.files.get('file')
    fileName = fileData.name
    fileType = fileData.type

    saveImg(fileData)

    result = OcrCore.parseImage(fileName)
    return text(result)


def saveImg(file):
    imgPath = file.name
    with open(imgPath, 'wb')as f:  # 打开图片，存储
        f.write(file.body)


@app.route("/gettest", methods=["GET"])
async def ocrIndex(request):
    print("receive data %s :" % (str(request)))
    return text('Hello World!')

if __name__ == "__main__":
    app.run(host="127.0.0.1", port=8000)
