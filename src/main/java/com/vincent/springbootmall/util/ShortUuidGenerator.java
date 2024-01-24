package com.vincent.springbootmall.util;

import com.google.common.io.BaseEncoding;

import java.nio.ByteBuffer;
import java.util.UUID;

public class ShortUuidGenerator {

    public static String generatorShortUuid(){
        UUID uuid = UUID.randomUUID();
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[16]);
        byteBuffer.putLong(uuid.getMostSignificantBits());
        byteBuffer.putLong(uuid.getLeastSignificantBits());

        String encodedString = BaseEncoding.base64Url().omitPadding().encode(byteBuffer.array());
        return encodedString.substring(0, 8);
    }
}
