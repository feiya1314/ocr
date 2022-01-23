from sanic import Sanic
from sanic.response import text, html, json

app = Sanic("ocr")


@app.route("/", methods=["POST",])
async def ocrIndex(request):
    print("receive data %s :" %(str(request.json)))
    return text('Hello World!')


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8000)
