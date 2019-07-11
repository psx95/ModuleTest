package com.psx.logging;

// Error Logs => Synced to backend
public enum LoggingLevel {
    NONE, //Nothing will be saved to DB
    ERRORS_ONLY, // Only errors will be logged and saved to DB
    DEBUG_ONLY, // Only debug logs will be saved to DB and logged
    DEBUG_INFO_ONLY, // Only debug and info logs will be saved to DB and syned
    VERBOSE, // All logs will be saved and synced with the DB
}
