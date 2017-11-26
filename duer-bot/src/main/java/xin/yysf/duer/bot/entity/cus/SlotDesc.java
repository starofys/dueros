package xin.yysf.duer.bot.entity.cus;

/**
 * 槽描述
 */
public class SlotDesc {
    /**
     * 慒名称
     */
    private String name;
    /**
     * 是否必须
     */
    private boolean required;
    /**
     * 如果不存在，需要返回给用户的消息
     */
    private String msg;
    public SlotDesc(){}

    public SlotDesc(String name, boolean required) {
        this.name = name;
        this.required = required;
    }

    public SlotDesc(String name, boolean required, String msg) {
        this.name = name;
        this.required = required;
        this.msg = msg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
