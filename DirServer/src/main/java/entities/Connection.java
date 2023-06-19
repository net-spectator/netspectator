package entities;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Connection{
    private TrackedEquipment device;
    private boolean isAuth = false;
    @NonNull
    private ChannelHandlerContext channelHandlerContext;

    @Override
    public String toString() {
        return (device != null ? device.getEquipmentTitle() : "NULL") + ": " + channelHandlerContext;
    }

    public void closeConnection() {
        channelHandlerContext.disconnect();
    }
}
