package com.psx.commons;

/**
 * This enum must contain the name of all the modules included in the app.
 * In other words, all the modules that have a dependency on commons Module.
 * These names are used by the exchange object to identify the modules communicating
 * with each other.
 */
public enum Modules {
    AFTER_EFFECTS,
    MAIN_APP,
    SIMPLE_MATHS,
    LOGGING;
}
