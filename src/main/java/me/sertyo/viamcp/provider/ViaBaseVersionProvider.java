package me.sertyo.viamcp.provider;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.protocols.base.BaseVersionProvider;
import me.sertyo.viamcp.ViaLoadingBase;

public class ViaBaseVersionProvider extends BaseVersionProvider {

    @Override
    public int getClosestServerProtocol(UserConnection connection) throws Exception {
        if (connection.isClientSide()) return ViaLoadingBase.getInstance().getTargetVersion().getVersion();

        return super.getClosestServerProtocol(connection);
    }
}