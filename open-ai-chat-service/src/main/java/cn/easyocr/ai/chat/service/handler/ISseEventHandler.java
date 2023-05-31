package cn.easyocr.ai.chat.service.handler;

/**
 * @author : feiya
 * @date : 2023/5/22
 * @description :
 */
public interface ISseEventHandler<T>{
    void accept(T t);

    void onClose();

    void onFailure(String msg);
}
