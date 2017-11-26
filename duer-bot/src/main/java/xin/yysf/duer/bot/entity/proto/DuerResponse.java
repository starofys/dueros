package xin.yysf.duer.bot.entity.proto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DuerResponse extends DuerProto{


    public static class OutputSpeech{
        public String type;
        public String text;
        public String ssml;
    }
//    public static class Reprompt{
//        public OutputSpeech outputSpeech;
//    }

    public static class UpdatedIntent{
        public String name;
        public String confirmationStatus;
        public Map<String,Slot> slots;
    }

    /**
     * <a href="https://dueros.baidu.com/didp/doc/dueros-bot-platform/dbp-custom/dialog-directive_markdown">对话指令</a>
     */
    public static class Directive{
        private Directive(){}
        public String type;
        public String slotToElicit;
        public UpdatedIntent updatedIntent;
    }

    public static class ResponseType{
        public OutputSpeech outputSpeech;
        public List<Directive> directives;
        public boolean expectSpeech;
        public OutputSpeech reprompt;
        public boolean shouldEndSession;
    }


    public ResponseType response;

    private DuerResponse(){};

    /**
     * <a href="https://dueros.baidu.com/didp/doc/dueros-bot-platform/dbp-custom/dialog-directive_markdown">对话指令</a>
     */
    public static class DirectiveBuilder{
        private Directive directive=new Directive();
        public static DirectiveBuilder newBuilder(){
            return new DirectiveBuilder();
        }
        public Directive build(){
            return directive;
        }

        /**
         * 编辑槽
         * @param slotName
         * @return
         */
        public DirectiveBuilder toElicitSlot(String slotName){
            directive.type="Dialog.ElicitSlot";
            directive.slotToElicit=slotName;
            return this;
        }
        public DirectiveBuilder toConfirmSlot(String slotName){
            directive.type="Dialog.ConfirmSlot";
            directive.slotToElicit=slotName;
            return this;
        }
        public DirectiveBuilder toConfirmIntent(){
            directive.type="Dialog.ConfirmIntent";
            return this;
        }

        /**
         * 对于没有的槽交给百度处理
         * @return
         */
        public DirectiveBuilder toDelegate(){
            directive.type="Dialog.Delegate";
            return this;
        }

        /**
         * 返回为百度修改后的意图
         * @param intent
         * @return
         */
        public DirectiveBuilder updatedIntent(DuerRequest.Intent intent){
            UpdatedIntent updatedIntent = directive.updatedIntent = new UpdatedIntent();
            updatedIntent.name=intent.name;
            updatedIntent.confirmationStatus=intent.confirmationStatus;
            updatedIntent.slots=intent.slots;
            return this;
        }
    }
    public static class Builder{
        private DuerResponse rs;
        private Builder(){}

        public static Builder newBuilder(){
            return buildFromRequest(null);
        }

        public static Builder buildFromRequest(DuerRequest request){
            Builder builder=new Builder();
            DuerResponse response=builder.rs=new DuerResponse();
            response.response=new ResponseType();
            response.version="2.0";

            //自定义Session 百度会原样返回
            Session session=response.session=new Session();
            Map<String, String> attr = session.attributes = new HashMap<>(5);
            attr.put("aaa","bbb");
            return builder;
        }

        /**
         * 返回要说出的话
         * @param message
         * @return
         */
        public Builder textMessage(String message){
            OutputSpeech out = rs.response.outputSpeech = new OutputSpeech();
            out.text=message;
            out.type="PlainText";

            rs.response.reprompt=out;
            return this;
        }

        /**
         * 是否结束本次意图，如果本次意图处理完成 则true
         * @param endSession
         * @return
         */
        public Builder shouldEndSession(boolean endSession){
            rs.response.shouldEndSession=endSession;
            return this;
        }

        /**
         * 添加一个意图处理结果
         * @param directive
         * @return
         */
        public Builder addDirective(Directive directive){
            List<Directive> directives=rs.response.directives;
            if(directives==null){
                directives=rs.response.directives=new ArrayList<>(5);
            }
            directives.add(directive);
            return this;
        }
        public Builder newSession(boolean newSession) {
            rs.session.bNew=newSession;
            return this;
        }
        public DuerResponse build(){
            return rs;
        }


    }

}
