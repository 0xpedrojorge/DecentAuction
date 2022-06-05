package ssd.assignment.communication.messages.adapters;

import com.google.gson.*;
import ssd.assignment.communication.messages.Message;
import ssd.assignment.communication.messages.MessageType;

import java.lang.reflect.Type;

public class MessageAdapter implements JsonSerializer<Message>, JsonDeserializer<Message> {

    @Override
    public JsonElement serialize(Message message, Type typeOfMessage, JsonSerializationContext context) {
        JsonObject object = new JsonObject();

        object.addProperty("TYPE", message.getData().getType().ordinal());
        object.add("DATA", context.serialize(message.getData()));

        return object;
    }

    @Override
    public Message deserialize(JsonElement json, Type typeOfObject, JsonDeserializationContext context) {
        JsonObject object = (JsonObject) json;

        int type = object.get("TYPE").getAsInt();
        JsonElement data = object.get("DATA");
        MessageType value = MessageType.values()[type];

        return new Message(context.deserialize(data, value.getTypeClass()));
    }
}
