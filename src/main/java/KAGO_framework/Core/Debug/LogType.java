package KAGO_framework.Core.Debug;

/**
 * Log: (logs) general information <br></br>
 * Info: (logs) framework specific information <br></br>
 * Success: expected behaviour <br></br>
 * Warning: expected fail <br></br>
 * Error: unexpected fail <br></br>
 * Fatal: unexpected fatal fail <br></br>
 */
public enum LogType {
    LOG,
    INFO,
    SUCCESS,
    WARNING,
    ERROR,
    FATAL
}
