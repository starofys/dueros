/*
 * Copyright (c) 2017 Baidu, Inc. All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.baidu.duer.dcs.framework.message;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 网络请求的消息体
 * <p>
 * Created by wuruisheng on 2017/6/1.
 */
@JsonSerialize
public class DcsRequestBody {
    public static class Bot{
        public Bot(String id){
            this.id=id;
        }
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
    private ArrayList<ClientContext> clientContext;
    private Event event;
    private Map<String,Object> debug;
    private String requestId;

    public DcsRequestBody(Event event) {
        this.event = event;
        debug=new HashMap<>(1);
        debug.put("bot",new Bot("f0b8d11f-f237-0ec0-7d88-18904b5c0fc0"));
        debug.put("simulator",true);
        requestId="wp"+System.currentTimeMillis();
    }

    public void setClientContext(ArrayList<ClientContext> clientContexts) {
        this.clientContext = clientContexts;
    }

    public ArrayList<ClientContext> getClientContext() {
        return clientContext;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }

    public Map<String, Object> getDebug() {
        return debug;
    }

    public void setDebug(Map<String, Object> debug) {
        this.debug = debug;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}