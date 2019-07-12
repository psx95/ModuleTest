package com.psx.logging;

import org.graylog2.gelfclient.GelfConfiguration;
import org.graylog2.gelfclient.GelfMessage;
import org.graylog2.gelfclient.GelfMessageBuilder;
import org.graylog2.gelfclient.GelfMessageLevel;
import org.graylog2.gelfclient.GelfTransports;
import org.graylog2.gelfclient.transport.GelfTransport;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Map;

import timber.log.Timber;

public class Gelf {

    private static GelfTransport transport;
    private static GelfMessageBuilder builder;

    static void initGelf(String ipAddress, LoggingLevel loggingLevel) {
        try {
            Timber.d("Initialising gelf");
            GelfMessageLevel gelfMessageLevel = computeGelfMessageLevel(loggingLevel);
            GelfConfiguration gelfConfiguration = createGelfConfig(ipAddress);
            transport = GelfTransports.create(gelfConfiguration);
            builder = new GelfMessageBuilder("", ipAddress)
                    .level(gelfMessageLevel);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static GelfMessageLevel computeGelfMessageLevel(LoggingLevel loggingLevel) {
        switch (loggingLevel) {
            case VERBOSE:
                return GelfMessageLevel.NOTICE;
            case DEBUG_ONLY:
                return GelfMessageLevel.DEBUG;
            case DEBUG_INFO_ONLY:
                return GelfMessageLevel.INFO;
            case ERRORS_ONLY:
                return GelfMessageLevel.ERROR;
            default:
                return GelfMessageLevel.ALERT;
        }
    }

    private static GelfConfiguration createGelfConfig(String ipAddress) throws UnknownHostException, InterruptedException {
        //InetAddress address = InetAddress.getByName(ipAddress);
        Timber.d("Creating Gelf config");
        return new GelfConfiguration(new InetSocketAddress(ipAddress, 12401))
                .transport(GelfTransports.TCP)
                .queueSize(512)
                .connectTimeout(5000)
                .reconnectDelay(1000)
                .tcpNoDelay(true)
                .sendBufferSize(1048576);

    }

    static void sendGelfMessage(String message) throws InterruptedException {
        final GelfMessage gelfMessage = builder.message(message).build();
        transport.send(gelfMessage);
    }

    static void sendGelfMessage(String message, Map<String, Object> additionalFields) throws InterruptedException {
        Timber.d("Sending Gelf messages");
        final GelfMessage gelfMessage = builder.message(message).additionalFields(additionalFields).build();
        boolean success = transport.trySend(gelfMessage);
        if (success)
            Timber.i("SUCCESS SEND");
        else
            Timber.i("FAILURE SEND");
    }
}
