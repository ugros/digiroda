/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ussoft;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import static digiroda.DigiController.loggerLevel;

public class USHtmlFormatter extends Formatter {
    // this method is called for every log records

    public String format(LogRecord rec) {
        StringBuffer buf = new StringBuffer(1000);
        buf.append("<tr>\n");

        // colorize any levels >= WARNING in red
        if (rec.getLevel().intValue() >= Level.WARNING.intValue()) {
            buf.append("\t<td style=\"color:red\">");
            buf.append("<b>");
            buf.append(rec.getLevel());
            buf.append("</b>");
        } else if (rec.getLevel().intValue() == Level.INFO.intValue()) {
            buf.append("\t<td style=\"color:green\">");
            buf.append(rec.getLevel());
        } else if (rec.getLevel().intValue() == Level.CONFIG.intValue()) {
            buf.append("\t<td style=\"color:blue\">");
            buf.append(rec.getLevel());
        } else if (rec.getLevel().intValue() >= Level.FINER.intValue()) {
            buf.append("\t<td style=\"color:black\">");
            buf.append(rec.getLevel());
        } else if (rec.getLevel().intValue() == Level.FINEST.intValue()) {
            buf.append("\t<td style=\"color:#D3D3D3\">");
            buf.append(rec.getLevel());
        } else {
            buf.append("\t<td>");
            buf.append(rec.getLevel());
        }

        buf.append("</td>\n");
        buf.append("\t<td>");
        buf.append(calcDate(rec.getMillis()));
        buf.append("</td>\n");

        if (rec.getLevel().intValue() >= Level.WARNING.intValue()) {
            buf.append("\t<td style=\"color:red\">");
            buf.append("<b>");
            buf.append(formatMessage(rec));
            buf.append("</b>");
        } else if (rec.getLevel().intValue() == Level.INFO.intValue()) {
            buf.append("\t<td style=\"color:green\">");
            buf.append(formatMessage(rec));
        } else if (rec.getLevel().intValue() == Level.CONFIG.intValue()) {
            buf.append("\t<td style=\"color:blue\">");
            buf.append(formatMessage(rec));
        } else if (rec.getLevel().intValue() >= Level.FINER.intValue()) {
            buf.append("\t<td style=\"color:black\">");
            buf.append(formatMessage(rec));
        } else if (rec.getLevel().intValue() == Level.FINEST.intValue()) {
            buf.append("\t<td style=\"color:#D3D3D3\">");
            buf.append(formatMessage(rec));
        } else {
            buf.append("\t<td>");
            buf.append(formatMessage(rec));
        }
        buf.append("</td>\n");
        if (loggerLevel.equals(Level.FINEST)) {
            buf.append("<td>");
            buf.append("Class: "+rec.getSourceClassName());
            buf.append("</br>Method: "+rec.getSourceMethodName()+"()");
            buf.append("</td>\n");
        }
        buf.append("</tr>\n");

        return buf.toString();
    }

    private String calcDate(long millisecs) {
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy. MMM dd., HH:mm:ss");
        Date resultdate = new Date(millisecs);
        return date_format.format(resultdate);
    }

    // this method is called just after the handler using this
    // formatter is created
    public String getHead(Handler h) {
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy. MMM dd., HH:mm:ss");
        Date d = new Date();
        return "<!DOCTYPE html>\n<head>\n<style>\n" + "table { width: 100% }\n" + "th { font:bold 10pt Tahoma; }\n"
                + "td { font:normal 10pt Tahoma; }\n" + "h1 {font:normal 11pt Tahoma;}\n" + "</style>\n" + "</head>\n"
                + "<body>\n" + "<h1>" + date_format.format(d) + "</h1>\n"
                + "<table border=\"0\" cellpadding=\"5\" cellspacing=\"3\">\n" + "<tr align=\"left\">\n"
                + "\t<th align=\"left\" style=\"width:10%\">Loglevel</th>\n"
                + "\t<th align=\"left\" style=\"width:15%\">Time</th>\n"
                + "\t<th align=\"left\" style=\"width:75%\">Log Message</th>\n"
                + "\t<th align=\"left\" style=\"width:75%\">Thrown by</th>\n"
                + "</tr>\n";
    }

    // this method is called just after the handler using this
    // formatter is closed
    public String getTail(Handler h) {
        return "</table>\n</body>\n</html>";
    }
}
