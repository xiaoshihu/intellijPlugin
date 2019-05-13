package com.esen;

import java.util.LinkedList;
import java.util.List;

public class RenderCode {

    private String header = "\n" +
            "<html>\n" +
            "   <head>\n" +
            "      <style type=\"text/css\">\n" +
            "         .sikuli-code {\n" +
            "            font-size: 20px;\n" +
            "            font-family: \"Osaka-mono\", Monospace;\n" +
            "            line-height: 1.5em;\n" +
            "            display:table-cell;\n" +
            "            white-space: pre-wrap;       /* css-3 */\n" +
            "            white-space: -moz-pre-wrap !important;  /* Mozilla, since 1999 */\n" +
            "            white-space: -pre-wrap;      /* Opera 4-6 */\n" +
            "            white-space: -o-pre-wrap;    /* Opera 7 */\n" +
            "            word-wrap: break-word;       /* Internet Explorer 5.5+ */\n" +
            "            width: 99%;   /* remove horizontal scroll-bar when viewing in IE7 */\n" +
            "         }\n" +
            "         .sikuli-code img {\n" +
            "            vertical-align: middle;\n" +
            "            margin: 2px;\n" +
            "            border: 1px solid #ccc;\n" +
            "            padding: 2px;\n" +
            "            -moz-border-radius: 5px;\n" +
            "            -webkit-border-radius: 5px;\n" +
            "            -moz-box-shadow: 1px 1px 1px gray;\n" +
            "            -webkit-box-shadow: 1px 1px 2px gray;\n" +
            "         }\n" +
            "         .kw {\n" +
            "            color: blue;\n" +
            "         }\n" +
            "         .skw {\n" +
            "            color: rgb(63, 127, 127);\n" +
            "         }\n" +
            "\n" +
            "         .str {\n" +
            "            color: rgb(128, 0, 0);\n" +
            "         }\n" +
            "\n" +
            "         .dig {\n" +
            "            color: rgb(128, 64, 0);\n" +
            "         }\n" +
            "\n" +
            "         .cmt {\n" +
            "            color: rgb(200, 0, 200);\n" +
            "         }\n" +
            "\n" +
            "         h2 {\n" +
            "            display: inline;\n" +
            "            font-weight: normal;\n" +
            "         }\n" +
            "\n" +
            "         .info {\n" +
            "            border-bottom: 1px solid #ddd;\n" +
            "            padding-bottom: 5px;\n" +
            "            margin-bottom: 20px;\n" +
            "            display: none;\n" +
            "         }\n" +
            "\n" +
            "         a {\n" +
            "            color: #9D2900;\n" +
            "         }\n" +
            "\n" +
            "         body {\n" +
            "            font-family: \"Trebuchet MS\", Arial, Sans-Serif;\n" +
            "         }\n" +
            "\n" +
            "      </style>\n" +
            "   </head>\n" +
            "<body>\n"+
            "   <pre class=\"sikuli-code\">\n";

    private String tail = "</pre>\n" +
            "\n" +
            "</body>\n" +
            "\n" +
            "</html>";

    // TODO: 2019/5/13 how should i deal with the text?
    public String RenderCode(String text) {
        String[] strings = text.split("\n");
        // TODO: 2019/5/13 in java a list is diffcult to use
//        List<String> list = new LinkedList<String>();
        StringBuilder renderstrings = new StringBuilder();
        renderstrings.append(header);
        for (String line : strings) {
            String leftline = leftTrim(line);
            int space_num = line.length() - leftline.length();
            String addspace = addSpace(space_num, "");

            if (leftline.startsWith("#")) {
                String head = "<span class=\"cmt\">";
                String end = "</span>";
                String rendertext = "\n" + addspace + head + line.trim() + end + "\n";
                renderstrings.append(rendertext);
            } else {
                String head = "<span class=\"cmt\">";
                String end = "</span>";
                String rendertext = "\n" + addspace + head + line.trim() + end + "\n";
                renderstrings.append(rendertext);
//                renderstrings.append(line);
            }
        }
        renderstrings.append(tail);
        return renderstrings.toString();
    }

    // TODO: 2019/5/13 add space of editor
    public String addSpace(int space_num, String addspace) {
        for (int i = 0; i < space_num; i++) {
            addspace += " ";
        }
        return addspace;
    }

    /*去左空格*/
    public static String leftTrim(String str) {
        if (str == null || str.equals("")) {
            return str;
        } else {
            return str.replaceAll("^[　 ]+", "");
        }
    }

    /*去右空格*/
    public static String rightTrim(String str) {
        if (str == null || str.equals("")) {
            return str;
        } else {
            return str.replaceAll("[　 ]+$", "");
        }
    }

    /*左右空格都去掉*/
    public static String trim(String str) {
        if (str == null || str.equals("")) {
            return str;
        } else {
            return str.replaceAll("^[　 ]+|[　 ]+$", "");
        }
    }
}
