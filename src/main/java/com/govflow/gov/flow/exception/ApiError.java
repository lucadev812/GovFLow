package com.govflow.gov.flow.exception;

import java.time.LocalDateTime;

public record ApiError(LocalDateTime timeStamp, int status, String message, String path, String requestURI) {
}

