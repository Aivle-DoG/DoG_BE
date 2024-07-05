package aivle.dog.global;

import lombok.Data;

@Data
public class Message {
    private StateEnum status;
    private String message;
    private String data;

    public Message() {
        this.status = StateEnum.BAD_REQUEST;
        this.data = null;
        this.message = null;
    }

}
