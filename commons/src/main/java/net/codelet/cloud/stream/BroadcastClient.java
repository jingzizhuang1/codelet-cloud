package net.codelet.cloud.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * 应用于广播的流处理器。
 */
public interface BroadcastClient {

    String INPUT = "codeletCloudBroadcastInput";

    String OUTPUT = "codeletCloudBroadcastOutput";

    @Input(INPUT)
    SubscribableChannel input();

    @Output(OUTPUT)
    MessageChannel output();
}
