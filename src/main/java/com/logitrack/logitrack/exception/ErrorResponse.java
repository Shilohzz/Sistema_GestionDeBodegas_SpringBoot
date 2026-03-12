package com.logitrack.logitrack.exception;

import java.time.LocalDateTime;

/**
 * {
 *     "timestamp": 2012...,
 *     "status":....
 *     "message":....
 *     "errorCode":....
 * }
 * */

// Lo que va en los parámetros es lo que yo quiero mostrar cuando este error suceda.
// No es necesario llevar todos esos, pero es nice standar.
public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String message,
        String errorCode
) {}
