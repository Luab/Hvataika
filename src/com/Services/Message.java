package com.Services;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by Lua_b on 01.11.2015.
 */
public class Message {
    public Message(Chat chat, Integer message_id, Integer date) {
        this.chat = chat;
        this.message_id = message_id;
        this.date = date;
    }

    public Message(com.Services.Chat chat, Integer message_id, Integer date, String text, com.Services.User from) {
        this.chat = chat;
        this.message_id = message_id;
        this.date = date;
        this.text = text;
        this.from = from;
    }

    public Message(com.Services.Chat chat, Integer message_id, Integer date, com.Services.User from) {
        this.chat = chat;
        this.message_id = message_id;
        this.date = date;
        this.from = from;
    }

    public Integer getMessageId() {
        return message_id;
    }

    public void setMessage_id(Integer message_id) {
        this.message_id = message_id;
    }

    public Integer getDate() {
        return date;
    }

    public void setDate(Integer date) {
        this.date = date;
    }

    public com.Services.Chat getChat() {
        return chat;
    }

    public void setChat(com.Services.Chat chat) {
        this.chat = chat;
    }

    /**
     * All we need inside message. Required
     */
    private com.Services.Chat chat;
    private Integer message_id;
    private Integer date;

    /**
     * Everything optional
     */
    private com.Services.User from;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public com.Services.User getFrom() {
        return from;
    }

    public void setFrom(com.Services.User from) {
        this.from = from;
    }

    private String text;

    /**
     *
     * @param message JSON object to parse from
     * @return parsed message
     */
    public static com.Services.Message getMessage(JSONObject message){
        Integer message_id = message.getInt("message_id");
        Integer date = message.getInt("date");
        com.Services.User from = com.Services.User.getUserSender(message.getJSONObject("from"));
        com.Services.Chat chat = com.Services.Chat.getChat(message.getJSONObject("chat"));
        if ( message.has("text")){
            String text = message.getString("text");
            return new com.Services.Message(chat,message_id,date,text,from);
        }
        else{
            return new com.Services.Message(chat,message_id,date,from);
        }
    }

    public static com.Services.Message getMessageFromResult(JSONObject result){
        return getMessage(result.getJSONObject("result"));
    }
    /**
     * @param text
     * @param replyTo may be <code>null</code>
     * @param chatId
     */
   public static com.Services.Message sendMessage(String text, Integer replyTo, int chatId) throws IOException {
        String s = "https://api.telegram.org/bot"+ com.Services.BuildVars.BOT_TOKEN+"/sendMessage?";
        com.Services.QueryString q = new com.Services.QueryString();
        // chat_id=47289384&text=Test&reply_to_message_id=52
        q.add("chat_id", String.valueOf(chatId));
        q.add("text", text);
        if (replyTo != null) {
            q.add("reply_to_message_id", replyTo.toString());
        }
        URL url = new URL(s + q.getQuery());
        java.util.Scanner scanner = null;
        try {
            scanner = new java.util.Scanner(url.openStream()).useDelimiter("\\A");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.err.println(url);
       String response = "";
       while(scanner.hasNext()){
           response+=scanner.next();
       }
        return getMessageFromResult(new JSONObject(response));
    }
    /**
     * Sends a photo
     * @param photo photo to send (file)
     * @param chatId ID of chat to reply
     * @return send message
     */
    public static Message sendPhoto(byte[] photo, String name, int chatId){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost uploadFile = new HttpPost("https://api.telegram.org/bot"+ com.Services.BuildVars.BOT_TOKEN+"/sendPhoto");
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("chat_id", String.valueOf(chatId), ContentType.TEXT_PLAIN);
        builder.addBinaryBody("photo", photo, ContentType.APPLICATION_OCTET_STREAM, name);
        HttpEntity multipart = builder.build();
        uploadFile.setEntity(multipart);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(uploadFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpEntity responseEntity = response.getEntity();

        java.util.Scanner s = null;
        try {
            s = new java.util.Scanner(responseEntity.getContent()).useDelimiter("\\A");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getMessageFromResult(new JSONObject(s.hasNext() ? s.next() : ""));
    }
    /**
     * Sends a photo
     * @param photo photo to send (file)
     * @param chatId ID of chat to reply
     * @return send message
     */
    public static com.Services.Message sendPhoto(File photo, int chatId){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost uploadFile = new HttpPost("https://api.telegram.org/bot"+ com.Services.BuildVars.BOT_TOKEN+"/sendPhoto");
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("chat_id", "chatId", ContentType.TEXT_PLAIN);
        builder.addBinaryBody("photo", photo, ContentType.APPLICATION_OCTET_STREAM, photo.getName());
        HttpEntity multipart = builder.build();
        uploadFile.setEntity(multipart);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(uploadFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpEntity responseEntity = response.getEntity();

        java.util.Scanner s = null;
        try {
            s = new java.util.Scanner(responseEntity.getContent()).useDelimiter("\\A");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getMessageFromResult(new JSONObject(s.hasNext() ? s.next() : ""));
    }
    /**
     * Sends a photo
     * @param photo photo to send (filepath)
     * @param chatId ID of chat to reply
     * @return send message
     */
    public static com.Services.Message sendPhoto(String photo, int chatId){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost uploadFile = new HttpPost("https://api.telegram.org/bot"+ com.Services.BuildVars.BOT_TOKEN+"/sendPhoto");
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("chat_id", "chatId", ContentType.TEXT_PLAIN);
        builder.addBinaryBody("photo", new File("photo"), ContentType.APPLICATION_OCTET_STREAM, "photo");
        HttpEntity multipart = builder.build();
        uploadFile.setEntity(multipart);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(uploadFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpEntity responseEntity = response.getEntity();

        java.util.Scanner s = null;
        try {
            s = new java.util.Scanner(responseEntity.getContent()).useDelimiter("\\A");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getMessage(new JSONObject(s.hasNext() ? s.next() : ""));
    }
}
