package stroom.widget.util.client;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

import java.util.function.Function;

public class SafeHtmlUtil {

    public static final SafeHtml NBSP = SafeHtmlUtils.fromSafeConstant("&nbsp;");

    private SafeHtmlUtil() {
    }

    public static SafeHtml getSafeHtml(final String string) {
        return SafeHtmlUtils.fromString(nullSafe(string));
    }

    private static String nullSafe(final String string) {
        return string != null
                ? string
                : "";
    }

    /**
     * Replaces \n with &lt;br&gt;
     * Any reserved chars in string will be escaped.
     * Will always return a {@link SafeHtml} object.
     */
    public static SafeHtml withLineBreaks(final String string) {
        if (string == null) {
            return SafeHtmlUtils.fromString("");
        } else {
            String str = string;
            if (str.startsWith("\n")) {
                str = str.substring(1);
            }
            if (str.endsWith("\n")) {
                str = str.substring(0, str.length() - 1);
            }
            // One ...<br> tag at the end of each line
            final SafeHtmlBuilder builder = new SafeHtmlBuilder();
            int lineBreakIdx;
            int lineNo = 1;
            while (!str.isEmpty()) {
                lineBreakIdx = str.indexOf("\n");
                final String line;
                if (lineBreakIdx == -1) {
                    line = str;
                    str = "";
                } else {
                    line = str.substring(0, lineBreakIdx);
                    if (str.length() > lineBreakIdx + 1) {
                        // Remove the line just appended
                        str = str.substring(lineBreakIdx + 1);
                    } else {
                        str = "";
                    }
                }

                if (!line.isEmpty()) {
                    if (lineNo++ != 1) {
                        builder.appendHtmlConstant("<br>");
                    }
                    builder.appendEscaped(line);
                }
            }
            return builder.toSafeHtml();
        }
    }

    /**
     * One html paragraph block for each line where a line is delimited by \n.
     * Any reserved chars in string will be escaped.
     * Will always return a {@link SafeHtml} object.
     */
    public static SafeHtml toParagraphs(final String string) {
        if (string == null) {
            return SafeHtmlUtils.fromString("");
        } else {
            String str = string;
            if (str.startsWith("\n")) {
                str = str.substring(1);
            }
            if (str.endsWith("\n")) {
                str = str.substring(0, str.length() - 1);
            }
            // One <p>...</p> block for each line
            final SafeHtmlBuilder builder = new SafeHtmlBuilder();
            int lineBreakIdx;
            while (!str.isEmpty()) {
                lineBreakIdx = str.indexOf("\n");
                final String line;
                if (lineBreakIdx == -1) {
                    line = str;
                    str = "";
                } else {
                    line = str.substring(0, lineBreakIdx);
                    if (str.length() > lineBreakIdx + 1) {
                        // Remove the line just appended
                        str = str.substring(lineBreakIdx + 1);
                    } else {
                        str = "";
                    }
                }

                if (!line.isEmpty()) {
                    builder
                            .appendHtmlConstant("<p>")
                            .appendEscaped(line)
                            .appendHtmlConstant("</p>");
                }
            }
            return builder.toSafeHtml();
        }
    }

    /**
     * @return Text coloured grey if enabled is false
     */
    public static SafeHtml getSafeHtml(final String string, final boolean enabled) {
        if (enabled) {
            return getSafeHtml(string);
        }
        return getColouredText(string, "grey");
    }

    /**
     * @return Text coloured grey if enabled is false
     */
    public static SafeHtml getColouredText(final String string, final String colour) {
        if (colour == null || colour.isEmpty()) {
            return getSafeHtml(string);
        }

        final SafeHtmlBuilder builder = new SafeHtmlBuilder();
        builder.appendHtmlConstant("<span style=\"color:" + colour + "\">");
        builder.appendEscaped(nullSafe(string));
        builder.appendHtmlConstant("</span>");
        return builder.toSafeHtml();
    }

    public static SafeHtml getColouredText(final String string,
                                           final String colour,
                                           final boolean isColoured) {
        if (isColoured) {
            return getColouredText(string, colour);
        } else {
            return getSafeHtml(string);
        }
    }

    public static SafeHtml from(boolean b) {
        return SafeHtmlUtils.fromTrustedString(String.valueOf(b));
    }

    public static SafeHtml from(byte num) {
        return SafeHtmlUtils.fromTrustedString(String.valueOf(num));
    }

    public static SafeHtml from(char c) {
        return SafeHtmlUtils.fromTrustedString(SafeHtmlUtils.htmlEscape(c));
    }

    public static SafeHtml from(double num) {
        return SafeHtmlUtils.fromTrustedString(String.valueOf(num));
    }

    public static SafeHtml from(float num) {
        return SafeHtmlUtils.fromTrustedString(String.valueOf(num));
    }

    public static SafeHtml from(int num) {
        return SafeHtmlUtils.fromTrustedString(String.valueOf(num));
    }

    public static SafeHtml from(long num) {
        return SafeHtmlUtils.fromTrustedString(String.valueOf(num));
    }

    public static SafeHtml from(Boolean b) {
        if (b == null) {
            return SafeHtmlUtils.EMPTY_SAFE_HTML;
        }
        return SafeHtmlUtils.fromTrustedString(String.valueOf(b));
    }

    public static SafeHtml from(Byte num) {
        if (num == null) {
            return SafeHtmlUtils.EMPTY_SAFE_HTML;
        }
        return SafeHtmlUtils.fromTrustedString(String.valueOf(num));
    }

    public static SafeHtml from(Character c) {
        if (c == null) {
            return SafeHtmlUtils.EMPTY_SAFE_HTML;
        }
        return SafeHtmlUtils.fromTrustedString(SafeHtmlUtils.htmlEscape(c));
    }

    public static SafeHtml from(Double num) {
        if (num == null) {
            return SafeHtmlUtils.EMPTY_SAFE_HTML;
        }
        return SafeHtmlUtils.fromTrustedString(String.valueOf(num));
    }

    public static SafeHtml from(Float num) {
        if (num == null) {
            return SafeHtmlUtils.EMPTY_SAFE_HTML;
        }
        return SafeHtmlUtils.fromTrustedString(String.valueOf(num));
    }

    public static SafeHtml from(Integer num) {
        if (num == null) {
            return SafeHtmlUtils.EMPTY_SAFE_HTML;
        }
        return SafeHtmlUtils.fromTrustedString(String.valueOf(num));
    }

    public static SafeHtml from(Long num) {
        if (num == null) {
            return SafeHtmlUtils.EMPTY_SAFE_HTML;
        }
        return SafeHtmlUtils.fromTrustedString(String.valueOf(num));
    }

    public static SafeHtml from(String string) {
        if (string == null) {
            return SafeHtmlUtils.EMPTY_SAFE_HTML;
        }
        return SafeHtmlUtils.fromString(string);
    }
}
