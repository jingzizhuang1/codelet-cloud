package net.codelet.cloud.util;

import net.codelet.cloud.error.BusinessError;
import net.codelet.cloud.vo.ImageProportion;
import net.codelet.cloud.constant.FileSizeValues;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 图像处理工具。
 */
public class ImageUtils {

    /**
     * 取得图像文件的大小。
     * @param source 图像文件
     * @return [宽度, 高度]
     */
    public static int[] getImageSize(File source) {
        return getImageSize(source.getAbsolutePath());
    }

    /**
     * 取得图像文件的大小。
     * @param source 图像文件路径
     * @return [宽度, 高度]
     */
    public static int[] getImageSize(String source) {
        try {
            String[] result = StringUtils.fromInputStream(
                ShellUtils
                    .exec("magick", "identify", "-format", "%w %h", source)
                    .getInputStream()
            ).split("\\s+");

            if (result.length != 2) {
                return new int[] {0, 0};
            }

            return new int[] {
                Integer.parseInt(result[0]),
                Integer.parseInt(result[1])
            };
        } catch (IOException e) {
            throw new BusinessError(e.getMessage());
        }
    }

    /**
     * 压缩图像。
     * @param source     源文件
     * @param target     目标文件路径
     * @param width      宽度
     * @param height     高度
     * @param proportion 大小调整方式
     */
    public static void compress(
        File source,
        String target,
        int width,
        int height,
        ImageProportion proportion
    ) {
        compress(source, target, width, height, 0L, proportion);
    }

    /**
     * 压缩图像。
     * @param source     源文件
     * @param target     目标文件路径
     * @param width      宽度
     * @param height     高度
     * @param extentSize 文件大小阈值
     * @param proportion 大小调整方式
     */
    public static void compress(
        File source,
        String target,
        int width,
        int height,
        double extentSize,
        ImageProportion proportion
    ) {
        int[] size = getImageSize(source);
        width = Math.min(size[0], width);
        height = Math.min(size[1], height);
        String resize = width + "x" + height;
        String resizeWithTags;

        switch (proportion) {
            case STRETCH:
                resizeWithTags = resize + ">!";
                break;
            case CROP:
                resizeWithTags = resize + "^";
                break;
            default:
                resizeWithTags = resize + ">";
        }

        List<String> command = new ArrayList<>(Arrays.asList(
            "magick",
            source.getAbsolutePath() + "[0]",
            "-resize", resizeWithTags
        ));

        if (proportion == ImageProportion.CROP) {
            command.addAll(Arrays.asList(
                "-gravity", "center",
                "-extent", resize
            ));
        }

        command.addAll(Arrays.asList(
            "-flatten",
            "-interlace", "line"
        ));

        if (target.toLowerCase().matches("\\.jpe?g$") && extentSize > 0L) {
            command.addAll(Arrays.asList(
                "-define",
                "jpeg:extent=" + Math.round(extentSize / FileSizeValues.KB) + "kb"
            ));
        }

        command.add(target);

        try {
            Process process = ShellUtils.exec(command);
            InputStream stdout = process.getInputStream();
            InputStream stderr = process.getErrorStream();

            int exitCode = process.waitFor();

            if (exitCode != 0) {
                ConsoleUtils.log(String.join(" ", command));
                ConsoleUtils.log(StringUtils.fromInputStream(stdout));
                ConsoleUtils.log(StringUtils.fromInputStream(stderr));
                throw new BusinessError("error.imagemagick[" + exitCode + "]"); // TODO
            }
        } catch (IOException | InterruptedException e) {
            throw new BusinessError(e.getMessage());
        }
    }
}
