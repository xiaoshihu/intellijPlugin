package com.esen;

import com.intellij.openapi.project.Project;

import javax.swing.*;
import java.io.IOException;

/**
 * The function of this class is run in a thread listen the edirot text change and get all text,
 * render code to html,and put it the editorPane1
 *
 * @author XIAOSHIHU
 * @date 2019/5/13 0:09
 */
public class ListenEditor implements Runnable {
    private JEditorPane editorPane1;
    private Project project;


    public void run() {
        // TODO: 2019/5/13 if the panel is visible,loop start?And should i check the status per 0.1s?
        //  because this is driver by the status.
        // TODO: 2019/5/13 mabey i make a mistake,intellij has provide a listen about editor,so l don't need
        //  writer a thread to deal the change of editor

            if (iseditorPanelvisible()){
                System.out.println("in thread");
                showcontent();
            }
            else {
                System.out.println("in visible");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }



    }

    private void showcontent(){
        String html = "\n" +
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
                "<body>\n" +
                "<div class=\"info\">\n" +
                "<h2>A.sikuli</h2> <a href=\"A.zip\">(Download this script)</a>\n" +
                "</div>\n" +
                "<pre class=\"sikuli-code\">\n" +
                "<span class=\"cmt\">#进入浏览器</span>\n" +
                "<span class=\"skw\">doubleClick</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1555415499452.png\" />)\n" +
                "<span class=\"skw\">wait</span>(<span class=\"dig\">1</span>)\n" +
                "<span class=\"cmt\">#浏览器最大化</span>\n" +
                "<span class=\"kw\">if</span> <span class=\"kw\">not</span> exists(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1555482662894.png\" />):\n" +
                "    <span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1555464580038.png\" />)\n" +
                "<span class=\"skw\">wait</span>(<span class=\"dig\">1</span>)\n" +
                "<span class=\"cmt\">#输出环境URL</span>\n" +
                "paste(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1555464527971.png\" />,<span class=\"str\">'http://172.21.50.189:8081/abitest'</span>)\n" +
                "<span class=\"skw\">type</span>(Key.ENTER)\n" +
                "<span class=\"cmt\">#登录设置</span>\n" +
                "<span class=\"kw\">if</span> exists(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1555472275476.png\" />):\n" +
                "    <span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1555472275476.png\" />)\n" +
                "<span class=\"cmt\">#进入报表目录</span>\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1555464967379.png\" />)\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1555465007887.png\" />)\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1555465024133.png\" />)\n" +
                "<span class=\"skw\">wait</span>(<span class=\"dig\">1</span>)\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1555465043308.png\" />)\n" +
                "<span class=\"skw\">wait</span>(<span class=\"dig\">1</span>)\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1555641763207.png\" />)\n" +
                "<span class=\"skw\">wait</span>(<span class=\"dig\">1</span>)\n" +
                "<span class=\"cmt\">#新建固定报表E</span>\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1555465213973.png\" />)\n" +
                "<span class=\"skw\">wait</span>(<span class=\"dig\">1</span>)\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1555465261540.png\" />)\n" +
                "<span class=\"kw\">if</span> exists(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1555465311953.png\" />):\n" +
                "        <span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1555465311953.png\" />)\n" +
                "<span class=\"cmt\"># 拖入分析区表格</span>\n" +
                "p = <span class=\"skw\">find</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554189735736.png\" />)\n" +
                "<span class=\"skw\">dragDrop</span>(p,Location(p.x+<span class=\"dig\">500</span>, p.y-<span class=\"dig\">20</span>))\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554191787130.png\" />)\n" +
                "<span class=\"skw\">rightClick</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554194759619.png\" />)\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554194784478.png\" />)\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554194856071.png\" />)\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554194884884.png\" />)\n" +
                "<span class=\"skw\">type</span>(Key.SHIFT)\n" +
                "<span class=\"cmt\"># 第一列税种</span>\n" +
                "paste(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554193060435.png\" />, <span class=\"str\">u\"税种\"</span>)\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554192536769.png\" />)\n" +
                "paste(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554193932080.png\" />, <span class=\"str\">u\"合计行\"</span>)\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554193988427.png\" />)\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554192256722.png\" />)\n" +
                "paste(<span class=\"str\">\"COPY_FSZHZB.SZ\"</span>)\n" +
                "\n" +
                "<span class=\"cmt\"># 第二列注册类型</span>\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554193499799.png\" />)\n" +
                "paste(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554193736023.png\" />, <span class=\"str\">u\"注册类型\"</span>)\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554193789458.png\" />)\n" +
                "<span class=\"skw\">doubleClick</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554194239484.png\" />)\n" +
                "paste(<span class=\"str\">\"--\"</span>)\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554194099149.png\" />)\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554192256722.png\" />)\n" +
                "paste(<span class=\"str\">\"COPY_FSZHZB.DJZCLX\"</span>)\n" +
                "<span class=\"cmt\"># 第三列应缴税额</span>\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554194302771.png\" />)\n" +
                "paste(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554194356808.png\" />, <span class=\"str\">u\"应缴税额\"</span>)\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554194432883.png\" />)\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554275439770.png\" />)\n" +
                "p = <span class=\"skw\">find</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554267330673-1.png\" />)\n" +
                "<span class=\"skw\">dragDrop</span>(p, Location(p.x+<span class=\"dig\">50</span>, p.y+<span class=\"dig\">150</span>))\n" +
                "paste(<span class=\"str\">\"sum(\"</span>)\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554276433466.png\" />)\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554276464273.png\" />)\n" +
                "paste(<span class=\"str\">\"$)\"</span>)\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554276683060.png\" />)\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554194527369.png\" />)\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554192256722.png\" />)\n" +
                "paste(<span class=\"str\">\"COPY_FSZHZB.OJSE\"</span>)\n" +
                "<span class=\"cmt\"># 第四列计提销售额</span>\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554194968213.png\" />)\n" +
                "paste(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554194998506.png\" />, <span class=\"str\">u\"计提销售额\"</span>)\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554195118738.png\" />)\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554275439770.png\" />)\n" +
                "p = <span class=\"skw\">find</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554267330673-1.png\" />)\n" +
                "<span class=\"skw\">dragDrop</span>(p, Location(p.x+<span class=\"dig\">50</span>, p.y+<span class=\"dig\">150</span>))\n" +
                "paste(<span class=\"str\">\"sum(\"</span>)\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554276786514.png\" />)\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554276464273.png\" />)\n" +
                "paste(<span class=\"str\">\"$)\"</span>)\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554276683060.png\" />)\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554195164318.png\" />)\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554192256722.png\" />)\n" +
                "<span class=\"skw\">type</span>(<span class=\"str\">\"COPY_FSZHZB.JTXSE\"</span>)\n" +
                "\n" +
                "<span class=\"cmt\"># 第五列税率</span>\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554195213817.png\" />)\n" +
                "paste(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554195250222.png\" />, <span class=\"str\">u\"税率\"</span>)\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554195470733-1.png\" />)\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554267874277.png\" />)\n" +
                "p = <span class=\"skw\">find</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554267330673.png\" />)\n" +
                "<span class=\"skw\">dragDrop</span>(p, Location(p.x+<span class=\"dig\">50</span>, p.y+<span class=\"dig\">150</span>))\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554276948481.png\" />)\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554274971418.png\" />)\n" +
                "paste(<span class=\"str\">\"/\"</span>)\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554276956682.png\" />)\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554275001544.png\" />)\n" +
                "paste(<span class=\"str\">\"*100\"</span>)\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554276683060.png\" />)\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554275108826.png\" />)\n" +
                "paste(<span class=\"str\">\"self.txt+'%'\"</span>)\n" +
                "\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554195557671.png\" />)\n" +
                "<span class=\"skw\">doubleClick</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554195373055.png\" />)\n" +
                "paste(<span class=\"str\">\"--\"</span>)\n" +
                "\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554196233141.png\" />)\n" +
                "<span class=\"skw\">wait</span>(<span class=\"dig\">1</span>)\n" +
                "<span class=\"cmt\"># 设置税种为浮动维单元格</span>\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554195656152.png\" />)\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554195677667.png\" />)\n" +
                "\n" +
                "hover(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554203808481.png\" />)\n" +
                "<span class=\"skw\">dragDrop</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554203808481.png\" />, <img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554203953503.png\" />)\n" +
                "\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554195794441.png\" />)\n" +
                "paste(<span class=\"str\">\"(copy_fszhzb.OJSE &lt;&gt; 0) &amp; (copy_fszhzb.JTXSE &lt;&gt; 0)\"</span>)\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554195933511.png\" />)\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554198754116.png\" />)\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554195984217.png\" />)\n" +
                "paste(<span class=\"str\">\"E2\"</span>)\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554196020686.png\" />)\n" +
                "paste(<span class=\"str\">\"4\"</span>)\n" +
                "<span class=\"cmt\"># 设置注册类型为浮动维单元格</span>\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554195704208.png\" />)\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554195677667.png\" />)\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554196152559.png\" />)\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554204488672.png\" />)\n" +
                "<span class=\"skw\">rightClick</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554196613448.png\" />)\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554196632248.png\" />)\n" +
                "<span class=\"skw\">wait</span>(<span class=\"dig\">1</span>)\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554196233141.png\" />)\n" +
                "<span class=\"skw\">wait</span>(<span class=\"dig\">1</span>)\n" +
                "<span class=\"cmt\"># 添加维下拉组件</span>\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554198663076.png\" />)\n" +
                "p=<span class=\"skw\">find</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554196290989-1.png\" />)\n" +
                "<span class=\"skw\">dragDrop</span>(p, Location(p.x+<span class=\"dig\">400</span>, p.y-<span class=\"dig\">400</span>))\n" +
                "<span class=\"skw\">sleep</span>(<span class=\"dig\">1</span>)\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554199033154.png\" />)\n" +
                "<span class=\"skw\">type</span>(<span class=\"str\">\"a\"</span>, KEY_CTRL)\n" +
                "<span class=\"skw\">type</span>(Key.DELETE)\n" +
                "\n" +
                "paste(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554197921041.png\" />, <span class=\"str\">u\"注册类型\"</span>)\n" +
                "p=<span class=\"skw\">find</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554196942678.png\" />)\n" +
                "<span class=\"skw\">dragDrop</span>(p, Location(p.x, p.y+<span class=\"dig\">400</span>))\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554197028517.png\" />)\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554277484306.png\" />)\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554197097555.png\" />)\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554199555620.png\" />)\n" +
                "<span class=\"skw\">wait</span>(<span class=\"dig\">1</span>)\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554266171335.png\" />)\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554266198433.png\" />)\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554196233141-1.png\" />)\n" +
                "<span class=\"skw\">wait</span>(<span class=\"dig\">1</span>)\n" +
                "<span class=\"cmt\"># 计算</span>\n" +
                "<span class=\"cmt\"># click(\"1554198420631.png\")</span>\n" +
                "<span class=\"cmt\"># click(\"1554199242692.png\")</span>\n" +
                "<span class=\"cmt\"># click(\"1554199267991.png\")</span>\n" +
                "<span class=\"cmt\"># click(\"1554199288247.png\")</span>\n" +
                "<span class=\"cmt\"># 拖入饼图</span>\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554257580346.png\" />)\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554257632236.png\" />)\n" +
                "<span class=\"skw\">type</span>(<span class=\"str\">\"a\"</span>, KEY_CTRL)\n" +
                "<span class=\"skw\">type</span>(Key.DELETE)\n" +
                "<span class=\"skw\">type</span>(<span class=\"str\">\"0\"</span>)\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554257719847.png\" />)\n" +
                "<span class=\"skw\">type</span>(<span class=\"str\">\"a\"</span>, KEY_CTRL)\n" +
                "<span class=\"skw\">type</span>(Key.DELETE)\n" +
                "<span class=\"skw\">type</span>(<span class=\"str\">\"80\"</span>)\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554258068252.png\" />)\n" +
                "<span class=\"cmt\"># waitVanish(\"1554258586195.png\", 5)</span>\n" +
                "<span class=\"skw\">wait</span>(<span class=\"dig\">1</span>)\n" +
                "<span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554277897063.png\" />)\n" +
                "p = <span class=\"skw\">find</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554257946705.png\" />)\n" +
                "<span class=\"skw\">dragDrop</span>(p, Location(p.x+<span class=\"dig\">700</span>, p.y))\n" +
                "<span class=\"kw\">if</span> exists(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554258795817.png\" />, <span class=\"dig\">10</span>):\n" +
                "    <span class=\"skw\">doubleClick</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554258795817.png\" />)\n" +
                "    <span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554259776017.png\" />)\n" +
                "    <span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554258905298.png\" />)\n" +
                "    <span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554258953403.png\" />)\n" +
                "    <span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554259016091.png\" />)\n" +
                "    <span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554259036025.png\" />)\n" +
                "    <span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554259067755.png\" />)\n" +
                "    <span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554259097330.png\" />)\n" +
                "    <span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554259036025.png\" />)\n" +
                "    <span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554259273369.png\" />)\n" +
                "    <span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554259318608.png\" />)\n" +
                "    <span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554260182808.png\" />)\n" +
                "    <span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554260335944.png\" />)\n" +
                "    <span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554260583744.png\" />)\n" +
                "    <span class=\"kw\">if</span> exists(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554260776208.png\" />):\n" +
                "        <span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554259437178.png\" />)\n" +
                "        <span class=\"skw\">type</span>(<span class=\"str\">\"%\"</span>)\n" +
                "        <span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554259477530.png\" />)\n" +
                "        <span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554260624800.png\" />)\n" +
                "        <span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554259534905.png\" />)\n" +
                "        <span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554259554081.png\" />)\n" +
                "        <span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554260293946.png\" />)\n" +
                "        <span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554259595529.png\" />)\n" +
                "        <span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554259615362.png\" />)\n" +
                "        <span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554259664602.png\" />)\n" +
                "        <span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554259689088.png\" />)\n" +
                "        <span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554258068252.png\" />)\n" +
                "        waitVanish(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554258586195.png\" />, <span class=\"dig\">20</span>)\n" +
                "        <span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554261015177.png\" />)\n" +
                "        <span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554261195834.png\" />)\n" +
                "        <span class=\"skw\">type</span>(<span class=\"str\">\"a\"</span>, KEY_CTRL)\n" +
                "        <span class=\"skw\">type</span>(Key.DELETE)\n" +
                "        <span class=\"skw\">type</span>(<span class=\"str\">\"380\"</span>)\n" +
                "        <span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554261210192.png\" />)\n" +
                "        <span class=\"skw\">type</span>(<span class=\"str\">\"a\"</span>, KEY_CTRL)\n" +
                "        <span class=\"skw\">type</span>(Key.DELETE)\n" +
                "        <span class=\"skw\">type</span>(<span class=\"str\">\"50\"</span>)\n" +
                "        <span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554261262240.png\" />)\n" +
                "        <span class=\"skw\">type</span>(<span class=\"str\">\"a\"</span>, KEY_CTRL)\n" +
                "        <span class=\"skw\">type</span>(Key.DELETE)\n" +
                "        <span class=\"skw\">type</span>(<span class=\"str\">\"500px\"</span>)\n" +
                "        <span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554261286992.png\" />)\n" +
                "        <span class=\"skw\">type</span>(<span class=\"str\">\"a\"</span>, KEY_CTRL)\n" +
                "        <span class=\"skw\">type</span>(Key.DELETE)\n" +
                "        <span class=\"skw\">type</span>(<span class=\"str\">\"500px\"</span>)\n" +
                "        <span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554258068252.png\" />)\n" +
                "        <span class=\"cmt\"># waitVanish(\"1554258586195.png\", 10)</span>\n" +
                "        <span class=\"skw\">wait</span>(<span class=\"dig\">1</span>)\n" +
                "        <span class=\"cmt\"># 计算</span>\n" +
                "        <span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554259724129.png\" />)\n" +
                "        <span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554278664182.png\" />)\n" +
                "        <span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554278786767.png\" />)\n" +
                "        <span class=\"skw\">click</span>(<img src=\"H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\1554199288247-1.png\" />)\n" +
                "</pre>\n" +
                "</body>\n" +
                "</html>\n";
        editorPane1.setEditable(false);
        editorPane1.setContentType("text/html;charset=utf-8");
        try {
            editorPane1.setPage("file:///H:\\learn\\新建文件夹 (2)\\report_template\\A.sikuli\\A.html");
        } catch (IOException e) {
            e.printStackTrace();
        }

//        Messages.showInputDialog(project, "图片名称已经存在，请输入新的图片名称", "Picname", Messages.getQuestionIcon());

        try {
            editorPane1.setPage("file:///H:\\learn\\新建文件夹 (2)\\report_template\\B.sikuli\\B.html");
        } catch (IOException e) {
            e.printStackTrace();
        }

        editorPane1.repaint();
    }

    private Boolean iseditorPanelvisible(){
        return editorPane1.isValid();
    }

    public static ListenEditor getgetInstance(Project project, JEditorPane editorPane1) {
        if (project != null) {
            SingletonHolder.instance.project = project;
        }
        SingletonHolder.instance.editorPane1 = editorPane1;
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        //静态初始化器，由JVM来保证线程安全
        private static ListenEditor instance = new ListenEditor();
    }
}
