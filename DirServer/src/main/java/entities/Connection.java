package entities;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Connection {
    private Device device;
    private boolean isAuth = false;
    @NonNull
    private ChannelHandlerContext channelHandlerContext;

    @Override
    public String toString() {
        return (device.getTitle() != null ? device.getTitle() : "NULL") + ": " + channelHandlerContext;
    }


}
