from loguru import logger

logger.add("./logs/paddle-ocr-service{time}.log", rotation="50MB",
           encoding="utf-8", enqueue=True, compression="zip", retention="10 days")

# 超过200M就新生成一个文件
# logger.add("size.log", rotation="200 MB")
# 每天中午12点生成一个新文件
# logger.add("time.log", rotation="12:00")
# 一周生成一个新文件
# logger.add("size.log", rotation="1 week")


def info(text, *args, **kwargs):
    logger.info(text, *args, **kwargs)


def debug(text, *args, **kwargs):
    logger.debug(text, *args, **kwargs)


def warning(text, *args, **kwargs):
    logger.warning(text, *args, **kwargs)


def error(text, *args, **kwargs):
    logger.error(text, *args, **kwargs)

def exception(text, *args, **kwargs):
    logger.exception(text, *args, **kwargs)
    
if __name__ == "__main__":
    info("ceshi info {},{}", "s1", "s2")
    error("error test")
