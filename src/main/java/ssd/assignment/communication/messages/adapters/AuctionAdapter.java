package ssd.assignment.communication.messages.adapters;

import com.google.gson.*;
import ssd.assignment.communication.messages.Message;
import ssd.assignment.communication.messages.MessageType;

import java.lang.reflect.Type;

public class AuctionAdapter implements JsonSerializer<Message>, JsonDeserializer<Message> {

    @Override
    public JsonElement serialize(Message src, Type typeOfSrc, JsonSerializationContext context) {

        JsonObject object = new JsonObject();

        object.addProperty("AUCTION_TYPE", src.getData().getType().ordinal());
        object.add("DATA", context.serialize(src.getData()));

        return object;
    }

    @Override
    public Message deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = (JsonObject) json;

        int type = object.get("AUCTION_TYPE").getAsInt();
        JsonElement data = object.get("DATA");
        MessageType value = MessageType.values()[type];

        return new Message(context.deserialize(data, value.getTypeClass()));
    }
}
