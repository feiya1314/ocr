package cn.easy.ocr.main.service;

import cn.easyocr.common.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.LinkedList;

/**
 * @author : feiya
 * @description :
 * @since : 2023/2/8
 */
public class SimpleTest {

    public static void main(String[] args) {
        LinkedList<String> linkedList = new LinkedList<>();
        linkedList.offer("a");
        linkedList.offer("s");
        linkedList.offer("d");
        linkedList.offer("e");
        linkedList.offer("b");
        linkedList.offer("n");
        linkedList.offer("m");
        String json = JsonUtils.toJson(linkedList);

        System.out.println(json);

        System.out.println(linkedList.peek());
        System.out.println(linkedList.peekLast());
        System.out.println(linkedList.pollLast());
        System.out.println(JsonUtils.toJson(linkedList));
        TypeReference<LinkedList<String>> typeReference = new TypeReference<LinkedList<String>>() {
        };

        LinkedList<String> rr = JsonUtils.jsonToBean(json, typeReference);
        System.out.println(rr.peek());
        System.out.println(rr);
    }
}
