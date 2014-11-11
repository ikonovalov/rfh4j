package ru.codeunited.wmq.restapi;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 07.11.14.
 */
@XmlRootElement(name = "message")
public class Message {

    private String payload;

    public Message() {
    }

    public Message(String payload) {
        this.payload = payload;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
