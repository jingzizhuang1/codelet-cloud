package net.codelet.cloud.util;

import lombok.Getter;

import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 控制台工具。
 */
public class ConsoleUtils {

    /**
     * 生成时间戳。
     * @return 时间戳
     */
    private static String timestamp() {
        return LocalDateTime.now().toString().replace('T', ' ');
    }

    /**
     * 打印日志。
     * @param text 日志内容
     * @param foreColor 文字颜色
     * @param backColor 背景颜色
     */
    public static void log(String text, Color foreColor, Color backColor) {
        (new ConsoleTextBuilder())
            .text(timestamp() + " " + text).fore(foreColor).back(backColor)
            .print(System.out);
    }

    /**
     * 打印日志。
     * @param text 日志内容
     * @param foreColor 文字颜色
     */
    public static void log(String text, Color foreColor) {
        log(text, foreColor, Color.CYAN);
    }

    /**
     * 打印日志。
     * @param text 日志内容
     */
    public static void log(String text) {
        log(text, Color.BLACK, Color.CYAN);
    }

    /**
     * 打印警告信息。
     * @param text 日志内容
     */
    public static void warn(String text) {
        log(text, Color.BLACK, Color.RED);
    }

    /**
     * 创建控制台格式化文本构造对象。
     * @param text 日志内容
     * @return ConsoleTextBuilder
     */
    public static ConsoleTextBuilder text(Object text) {
        return (new ConsoleTextBuilder()).text(text);
    }

    /**
     * 创建控制台格式化文本构造对象。
     * @param text      文本
     * @param foreColor 文字颜色
     * @return ConsoleTextBuilder
     */
    public static ConsoleTextBuilder text(Object text, Color foreColor) {
        return (new ConsoleTextBuilder()).text(text, false, foreColor, null);
    }

    /**
     * 创建控制台格式化文本构造对象。
     * @param text      文本
     * @param foreColor 文字颜色
     * @param backColor 背景颜色
     * @return ConsoleTextBuilder
     */
    public static ConsoleTextBuilder text(Object text, Color foreColor, Color backColor) {
        return (new ConsoleTextBuilder()).text(text, false, foreColor, backColor);
    }

    /**
     * 创建控制台格式化文本构造对象。
     * @param text      文本
     * @param bold      是否加粗
     * @param foreColor 文字颜色
     * @return ConsoleTextBuilder
     */
    public static ConsoleTextBuilder text(Object text, boolean bold, Color foreColor) {
        return (new ConsoleTextBuilder()).text(text, bold, foreColor, null);
    }

    /**
     * 创建控制台格式化文本构造对象。
     * @param text      文本
     * @param bold      是否加粗
     * @param foreColor 文字颜色
     * @param backColor 背景颜色
     * @return ConsoleTextBuilder
     */
    public static ConsoleTextBuilder text(Object text, boolean bold, Color foreColor, Color backColor) {
        return (new ConsoleTextBuilder()).text(text, bold, foreColor, backColor);
    }

    /**
     * 控制台颜色代码。
     */
    public enum Color {
        BLACK         (30,  40),
        RED           (31,  41),
        GREEN         (32,  42),
        YELLOW        (33,  43),
        BLUE          (34,  44),
        MAGENTA       (35,  45),
        CYAN          (36,  46),
        GRAY          (37,  47),
        DARK_GRAY     (90, 100),
        LIGHT_RED     (91, 101),
        LIGHT_GREEN   (92, 102),
        LIGHT_YELLOW  (93, 103),
        LIGHT_BLUE    (94, 104),
        LIGHT_MAGENTA (95, 105),
        LIGHT_CYAN    (96, 106),
        LIGHT_GRAY    (97, 107);

        @Getter
        private int foreColor;

        @Getter
        private int backColor;

        Color(int foreColor, int backColor) {
            this.foreColor = foreColor;
            this.backColor = backColor;
        }
    }

    /**
     * 控制台文本构造器。
     */
    public static class ConsoleTextBuilder {

        private StringBuilder builder = new StringBuilder();
        private Boolean bold = null;
        private Integer foreColor = null;
        private Integer backColor = null;
        private String text = null;

        public ConsoleTextBuilder text(Object text, Color foreColor) {
            return text(text, false, foreColor, null);
        }

        public ConsoleTextBuilder text(Object text, Color foreColor, Color backColor) {
            return text(text, false, foreColor, backColor);
        }

        public ConsoleTextBuilder text(Object text, boolean bold, Color foreColor) {
            return text(text, bold, foreColor, null);
        }

        public ConsoleTextBuilder text(Object text, boolean bold, Color foreColor, Color backColor) {
            text(text);
            if (bold) {
                bold();
            }
            if (foreColor != null) {
                fore(foreColor);
            }
            if (backColor != null) {
                back(backColor);
            }
            return this;
        }

        public ConsoleTextBuilder text(Object text) {
            List<String> style = new ArrayList<>();
            if (this.text != null) {
                if (bold != null && bold) {
                    style.add("1");
                }
                if (foreColor != null) {
                    style.add("" + foreColor);
                }
                if (backColor != null) {
                    style.add("" + backColor);
                }
                if (style.size() > 0) {
                    builder
                        .append("\u001B[")
                        .append(String.join(";", style))
                        .append("m")
                        .append(this.text)
                        .append("\u001B[0m");
                } else {
                    builder.append(this.text);
                }
                this.bold = null;
                this.foreColor = null;
                this.backColor = null;
                this.text = null;
            }
            if (text != null) {
                this.text = text.toString();
            }
            return this;
        }

        public ConsoleTextBuilder bold() {
            this.bold = true;
            return this;
        }

        public ConsoleTextBuilder fore(Color color) {
            this.foreColor = color.getForeColor();
            return this;
        }

        public ConsoleTextBuilder back(Color color) {
            this.backColor = color.getBackColor();
            return this;
        }

        public void print() {
            print(System.out);
        }

        public void print(PrintStream ps) {
            ps.println(this.text("").toString());
        }

        @Override
        public String toString() {
            this.text("");
            return builder.toString();
        }
    }
}
