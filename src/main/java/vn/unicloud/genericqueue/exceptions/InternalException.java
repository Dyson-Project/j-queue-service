package vn.unicloud.genericqueue.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class InternalException extends Throwable {
    int code;
    String message;
    StackTraceElement[] stackTrace;
}
