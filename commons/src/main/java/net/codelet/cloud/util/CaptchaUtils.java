package net.codelet.cloud.util;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.codelet.cloud.dto.BaseDTO;
import net.codelet.cloud.dto.ContextDTO;
import net.codelet.cloud.error.AccessDeniedError;
import net.codelet.cloud.error.AccessTokenExpiredError;
import net.codelet.cloud.error.AccessTokenInvalidError;

import javax.validation.constraints.NotBlank;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 人机测试工具。
 */
public class CaptchaUtils {

    // 图形验证码字符集
    private static final String CHARSET = "34578acdefhkmnprtwxyzACEFGHJKLMNPRWXYZ";

    // 接受的字体文件类型
    private static final Pattern ACCEPTED_FONT_NAME = Pattern.compile("^.+\\.(ttf)$", Pattern.CASE_INSENSITIVE);

    // 加密数据格式
    private static final Pattern ENCRYPTED_DATA = Pattern.compile("^([0-9a-f]+)_([0-9a-f]+)$");

    // 字体文件路径
    private static List<String> FONTS = new ArrayList<>();

    // 图形验证码几何信息
    private static final int WIDTH = 120;
    private static final int HEIGHT = 60;
    private static final String IMAGE_SIZE = WIDTH + "x" + HEIGHT;
    private static final String[] BACK_COLOR_RANGE = {"BF", "FF"};
    private static final double TEXT_LAYER_PADDING = 0.15;
    private static final double TEXT_LAYER_CONTENT = 1 - TEXT_LAYER_PADDING * 2;
    private static final double[] TEXT_SCALE_RANGE = {1.0, 1.25};
    private static final double[] TEXT_ROTATION_RANGE = {-15.0, 15.0};
    private static final double[] TEXT_SKEW_RANGE = {-15.0, 15.0};
    private static final String[] TEXT_COLOR_RANGE = {"00", "9F"};
    private static final String[] NOISE_COLOR_RANGE = {"7F", "DF"};

    /**
     * 设置字体文件。
     * @param path 字体文件路径
     */
    public static void setFonts(String path) {
        File[] fonts = (new File(path)).listFiles(
            (dir, name) -> ACCEPTED_FONT_NAME.matcher(name).matches()
        );
        if (fonts == null || fonts.length == 0) {
            return;
        }
        FONTS.clear();
        for (File font : fonts) {
            FONTS.add(font.getAbsolutePath());
        }
    }

    /**
     * 生成默认长度随机文本的图形验证码。
     * @param context 请求上下文对象
     * @param ttl     有效时长（毫秒）
     * @return 人机验证数据
     */
    public static CaptchaData generateImageOfText(final ContextDTO context, final long ttl) {
        return generateImageOfText(context, ttl, 4);
    }

    /**
     * 生成指定长度随机文本的图形验证码。
     * @param context  请求上下文对象
     * @param ttl      有效时长（毫秒）
     * @param length   文本长度
     * @return 人机验证数据
     */
    public static CaptchaData generateImageOfText(final ContextDTO context, final long ttl, final int length) {
        return generateImageOfText(context, ttl, RandomUtils.text(CHARSET, length));
    }

    /**
     * 生成指定文本的图形验证码。
     * @param context 请求上下文对象
     * @param ttl     有效时长（毫秒）
     * @param text    图形验证码中的文本
     * @return 人机验证数据
     */
    public static CaptchaData generateImageOfText(
        final ContextDTO context,
        final long ttl,
        final String text
    ) {
        long validUntil = System.currentTimeMillis() + ttl;
        int charCount = text.length();
        double textLayerWidth = Math.round(WIDTH / (TEXT_LAYER_PADDING * (charCount + 1) + TEXT_LAYER_CONTENT * charCount));
        String textLayerSize = textLayerWidth + "x" + HEIGHT;
        long textOffset = Math.round(textLayerWidth * TEXT_LAYER_PADDING);
        long textWidth = Math.round(textLayerWidth * TEXT_LAYER_CONTENT);
        long[] textOffsetRange = new long[] {-1 * textOffset, textOffset};
        char character;
        List<String> args;
        List<List<String>> commands = new ArrayList<>();

        // 绘制背景
        commands.add(Arrays.asList(
            "magick",
            "-size", IMAGE_SIZE,
            "gradient:"
                + RandomUtils.color(BACK_COLOR_RANGE)
                + "-"
                + RandomUtils.color(BACK_COLOR_RANGE),
            "png:-"
        ));

        // 逐一绘制字符
        for (int index = 0; index < text.length(); index++) {

            character = text.charAt(index);

            // 绘制字符
            args = new ArrayList<>(Arrays.asList(
                "magick",
                "png:-",
                "(",
                "-size", textLayerSize,
                "canvas:none",
                "-pointsize", "" + textWidth,
                "-fill", RandomUtils.color(TEXT_COLOR_RANGE),
                "-font", RandomUtils.randomItem(FONTS, "DejaVu-Sans-Mono-Bold"),
                "-draw", "gravity Center scale "
                    + RandomUtils.between(TEXT_SCALE_RANGE)
                    + ","
                    + RandomUtils.between(TEXT_SCALE_RANGE)
                    + " rotate "
                    + RandomUtils.between(TEXT_ROTATION_RANGE)
                    + " skewX "
                    + RandomUtils.between(TEXT_SKEW_RANGE)
                    + " skewY "
                    + RandomUtils.between(TEXT_SKEW_RANGE)
                    + " text 0,0 '" + character + "'",
                ")",
                "-geometry", textLayerSize
                    + "+"
                    + ((textOffset + textWidth) * index + RandomUtils.between(textOffsetRange))
                    + "+"
                    + RandomUtils.between(textOffsetRange),
                "-composite",
                "png:-"
            ));

            commands.add(args);

            // 绘制噪点
            args = new ArrayList<>(Arrays.asList(
                "magick",
                "png:-",
                "(",
                "-size", IMAGE_SIZE,
                "canvas:none",
                "-stroke", RandomUtils.color(NOISE_COLOR_RANGE),
                "-strokewidth", "0.5",
                "-draw", "line "
                    + RandomUtils.between(0, WIDTH)
                    + ","
                    + RandomUtils.between(0, HEIGHT)
                    + " "
                    + RandomUtils.between(0, WIDTH)
                    + ","
                    + RandomUtils.between(0, HEIGHT),
                ")",
                "-geometry", IMAGE_SIZE + "+0+0",
                "-composite"
            ));

            if (index + 1 < text.length()) {
                args.add("png:-");
            } else {
                args.add("-quality");
                args.add("75");
                args.add("jpeg:-");
            }

            commands.add(args);
        }

        String imageData;

        // 生成图像的 Base64 字符串
        try {
            InputStream inputStream = ShellUtils.chain(commands).getInputStream();
            imageData = "data:image/jpeg;base64," + CryptoUtils.encodeBase64(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace(System.err);
            imageData = null;
        }

        return new CaptchaData(context, text, validUntil, imageData);
    }

    /**
     * 校验用户识别结果。
     * @param context    请求上下文对象
     * @param captchaDTO 图形验证码校验数据传输对象
     */
    public static void verify(final ContextDTO context, final CaptchaDTO captchaDTO) {
        if (captchaDTO == null
            || StringUtils.isEmpty(captchaDTO.getText())
            || StringUtils.isEmpty(captchaDTO.getEncryptedData())) {
            throw new AccessDeniedError("error.captcha.required");
        }

        Matcher matcher = ENCRYPTED_DATA.matcher(captchaDTO.getEncryptedData());

        if (!matcher.matches()) {
            throw new AccessTokenInvalidError("error.captcha.incorrect");
        }

        long validUntil = Long.parseLong(matcher.group(1), 16);

        if (System.currentTimeMillis() > validUntil) {
            throw new AccessTokenExpiredError("error.captcha.expired");
        }

        String encryptedData = CryptoUtils.md5(
            captchaDTO.getText().toUpperCase()
                + context.getRemoteAddr()
                + context.getUserAgent()
                + validUntil
        );

        if (!encryptedData.equals(matcher.group(2))) {
            throw new AccessTokenInvalidError("error.captcha.incorrect");
        }
    }

    /**
     * 图形验证码数据。
     */
    public static class CaptchaData extends BaseDTO {

        private static final long serialVersionUID = -9115384920989333509L;

        @Getter
        @Setter
        @ApiModelProperty("有效截止时间")
        private Long validUntil;

        @Getter
        @Setter
        @ApiModelProperty("图像 Base64 字符串")
        private String imageData;

        @Getter
        @Setter
        @ApiModelProperty("内容加密数据")
        private String encryptedData;

        /**
         * 构造方法。
         * @param context    请求上下文对象
         * @param text       内容文本
         * @param validUntil 有效截止时间（Unix Epoch 时间，毫秒）
         * @param imageData  图像 Base64 字符串
         */
        private CaptchaData(
            final ContextDTO context,
            final String text,
            final long validUntil,
            final String imageData
        ) {
            super();
            this.imageData = imageData;
            this.validUntil = validUntil;
            this.encryptedData = String.format(
                "%s_%s",
                Long.toHexString(validUntil),
                CryptoUtils.md5(
                    text.toUpperCase()
                        + context.getRemoteAddr()
                        + context.getUserAgent()
                        + validUntil
                )
            );
        }
    }

    /**
     * 图形验证码数据传输对象。
     */
    public static class CaptchaDTO extends BaseDTO {

        private static final long serialVersionUID = 1111569689804560049L;

        @Getter
        @Setter
        @NotBlank
        @ApiModelProperty("图形验证码内容")
        private String text;

        @Getter
        @Setter
        @NotBlank
        @ApiModelProperty("内容加密数据")
        private String encryptedData;
    }
}
