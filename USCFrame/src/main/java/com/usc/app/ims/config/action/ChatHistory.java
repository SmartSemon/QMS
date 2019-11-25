package com.usc.app.ims.config.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usc.app.action.a.AbstractAction;
import com.usc.app.util.Utils;
import com.usc.dto.Dto;
import com.usc.dto.impl.MapDto;
import com.usc.obj.api.USCObject;
import com.usc.obj.util.USCObjectQueryHelper;
import java.util.*;

/**
 * @Author: lwp
 * @DATE: 2019/11/8 15:16
 * @Description: 获取ims聊天记录
 **/
public class ChatHistory extends AbstractAction {

    @Override
    public Object executeAction() throws Exception {
        String userName = (String) context.getExtendInfo("userName");
        //condition中有此人id和聊天类型{id:xxx,type:friend/group}
        String condition = (String) context.getExtendInfo("condition");
        Dto dto = new MapDto();
        if (!Utils.isEmpty(condition)) {
            dto.putAll(new ObjectMapper().readValue(condition, MapDto.class));
        }
        String id = dto.getString("id");
        String type = dto.getString("type");
        List<Dto> chatHistory = new ArrayList<>();
//        if ("group".equals(type)) {
//            Dto param = new MapDto();
//            param.put("receiver", id);
//            param.put("types", type);
//            List<Dto> lists = selectList(param);
//            for (int i = 0; i < lists.size(); i++) {
//                if (lists.get(i).getString("sender").equals(lists.get(i).getString("avatar"))) {
//                    Dto history = new MapDto();
//                    history.put("username", lists.get(i).getString("senderName"));
//                    history.put("id", lists.get(i).getString("sender"));
//                    Date date = df.parse(df.format(Utils.intToDateD(lists.get(i).getDouble("createTime"))));
//                    history.put("timestamp", date.getTime());
//                    history.put("content", lists.get(i).getString("msg"));
//                    chatHistory.add(history);
//                }
//            }
//            dto.put("code", 0);
//            dto.put("msg", "获取成功");
//            dto.put("data", chatHistory);
//            return dto;
//
//        } else {
        String minCondition = "del = 0 AND " + "receiverId = " + "'" + id + "'" + " AND " + "types = " + "'" + type + "'";
        //获取我发送的消息
        USCObject[] minHistoryList = USCObjectQueryHelper.getObjectsByCondition("CHAT_HISTORY", minCondition);
        for (USCObject minHistory : minHistoryList) {
            Dto history = new MapDto();
            history.put("username", minHistory.getFieldValue("sender_name"));
            history.put("id", minHistory.getFieldValue("senderId"));
            history.put("avatar", "http://localhost:8000/api/user/getAvatar/"+minHistory.getFieldValue("senderId"));
            Date dateMe = (Date) minHistory.getFieldValue("ctime");
//            Date dateMe =df.parse( df.format(minHistory.getFieldValueToString("ctime")));
            history.put("timestamp", dateMe.getTime());
            history.put("content", minHistory.getFieldValue("msg"));
            chatHistory.add(history);
        }
        String youCondition = "del = 0 AND " + "senderId = " + "'" + id + "'" + " AND " + "types = " + "'" + type + "'";
        //获取他（他们的）的消息
        USCObject[] youHistoryList = USCObjectQueryHelper.getObjectsByCondition("CHAT_HISTORY", youCondition);
        for (USCObject youHistory : youHistoryList) {
            Dto history = new MapDto();
            history.put("username", youHistory.getFieldValue("sender_name"));
            history.put("id", youHistory.getFieldValue("senderId"));
            history.put("avatar", "http://localhost:8000/api/user/getAvatar/"+youHistory.getFieldValue("senderId"));
            Date dateYou = (Date) youHistory.getFieldValue("ctime");
            history.put("timestamp", dateYou.getTime());
            history.put("content", youHistory.getFieldValue("msg"));
            chatHistory.add(history);
        }
        //根据时间进行排序
        Collections.sort(chatHistory, new Comparator<Dto>() {
            @Override
            public int compare(Dto o1, Dto o2) {
                if (o1.getDouble("timestamp") > o2.getDouble("timestamp")) {
                    return 1;
                }
                if (o1.getDouble("timestamp") > o2.getDouble("timestamp")) {
                    return 0;
                }
                return -1;
            }
        });
        dto.put("code", 0);
        dto.put("msg", "获取成功");
        dto.put("data", chatHistory);
        return dto;
//        }
    }

    @Override
    public boolean disable() throws Exception {
        return false;
    }
}
