package com.mycompany.myapp.domain;

import com.ingenico.connect.gateway.sdk.java.ApiResource;
import com.ingenico.connect.gateway.sdk.java.Communicator;
import com.ingenico.connect.gateway.sdk.java.logging.CommunicatorLogger;
import com.ingenico.connect.gateway.sdk.java.logging.LoggingCapable;
import com.ingenico.connect.gateway.sdk.java.merchant.MerchantClient;
import java.io.Closeable;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

public class Client extends ApiResource implements Closeable, LoggingCapable {

    public static final String API_VERSION = "v1";

    public Client(Communicator communicator) {
        this(communicator, null);
    }

    private Client(Communicator communicator, String clientMetaInfo) {
        super(communicator, clientMetaInfo, null);
    }

    public Client withClientMetaInfo(String clientMetaInfo) {
        if (this.clientMetaInfo == null && clientMetaInfo == null) {
            return this;
        } else if (clientMetaInfo == null) {
            return new Client(communicator, null);
        } else {
            communicator.getMarshaller().unmarshal(clientMetaInfo, Object.class);

            if (clientMetaInfo.equals(this.clientMetaInfo)) {
                return this;
            } else {
                return new Client(communicator, clientMetaInfo);
            }
        }
    }

    public void closeIdleConnections(long idleTime, TimeUnit timeUnit) {
        communicator.closeIdleConnections(idleTime, timeUnit);
    }

    public void closeExpiredConnections() {
        communicator.closeExpiredConnections();
    }

    @Override
    public void enableLogging(CommunicatorLogger communicatorLogger) {
        communicator.enableLogging(communicatorLogger);
    }

    @Override
    public void disableLogging() {
        communicator.disableLogging();
    }

    @Override
    public void close() throws IOException {
        communicator.close();
    }

    public MerchantClient merchant(String merchantId) {
        Map<String, String> subContext = new TreeMap<String, String>();
        subContext.put("merchantId", merchantId);
        return new MerchantClient(this, subContext);
    }
}
