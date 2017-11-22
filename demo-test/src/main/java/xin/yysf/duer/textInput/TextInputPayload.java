package xin.yysf.duer.textInput;

import com.baidu.duer.dcs.framework.message.Payload;

public class TextInputPayload extends Payload {
    private String query;


    public TextInputPayload(){

    }

    public TextInputPayload(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
