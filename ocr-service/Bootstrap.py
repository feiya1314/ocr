from sanic import Sanic
from sanic.response import text, html, json
from sanic_cors import CORS

app = Sanic("ocr")
CORS(app)


@app.route("/ocr", methods=["POST"])
async def ocrIndex(request):
    print("receive data")
    print("receive data %s :" %(str(request.json)))
    return text('Hello World!')


@app.route("/gettest", methods=["GET"])
async def ocrIndex(request):
    print("receive data %s :" %(str(request)))
    return text('Hello World!')

if __name__ == "__main__":
    app.run(host="127.0.0.1", port=8000)
