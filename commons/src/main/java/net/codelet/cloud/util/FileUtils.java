package net.codelet.cloud.util;

import net.codelet.cloud.error.BusinessError;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * 文件工具。
 */
public class FileUtils {

    // 文件字符集
    private static final String DEFAULT_CHARSET = "UTF-8";

    // 文件扩展名格式
    private static final Pattern FILENAME_PATTERN = Pattern.compile("^(.+)?(\\.[0-9a-zA-Z]+)$");

    /**
     * 设置文件的访问许可。
     * @param file 文件
     */
    private static void setAsRWX(File file) {
        file.setReadable(true, false);
        file.setWritable(true, false);
        file.setExecutable(true, false);
    }

    /**
     * 取得文件的扩展名。
     * @param file 文件
     * @return 扩展名
     */
    public static String extName(File file) {
        return extName(file.getName());
    }

    /**
     * 取得文件的扩展名。
     * @param filename 文件名
     * @return 扩展名
     */
    public static String extName(String filename) {
        Matcher m = FILENAME_PATTERN.matcher(filename);
        return !m.matches() ? "" : m.group(2);
    }

    /**
     * 读取文件内容。
     * @param path 文件路径
     * @return 文件内容
     */
    public static String read(String path) {
        try {
            return org.apache.commons.io.FileUtils.readFileToString(
                new File(path),
                DEFAULT_CHARSET
            );
        } catch (IOException e) {
            throw new BusinessError(e.getMessage());
        }
    }

    /**
     * 将字符串写入文件。
     * @param path 文件路径
     * @param data 数据
     */
    public static void write(String path, String data) {
        try {
            File file = new File(path);

            org.apache.commons.io.FileUtils.write(
                file,
                data,
                DEFAULT_CHARSET
            );

            setAsRWX(file);
        } catch (IOException e) {
            throw new BusinessError(e.getMessage());
        }
    }

    /**
     * 复制文件。
     * @param source 源文件
     * @param target 目标路径
     * @throws IOException
     */
    public static void copy(File source, String target) throws IOException {
        InputStream is = new FileInputStream(source);
        OutputStream os = new FileOutputStream(new File(target));
        byte[] buffer = new byte[8192];
        while (is.read(buffer) > -1) {
            os.write(buffer);
        }
    }

    /**
     * 将文件复制到目标路径下。
     * @param is 输入流
     * @param targetDir 目标路径
     * @param salt 用于生成文件名的 MD5 算法盐
     * @return 文件名
     */
    public static String copy(InputStream is, String targetDir, String salt) {
        try {
            File target = new File(
                targetDir,
                Long.toString(
                    System.currentTimeMillis() * 1000000
                        + Math.abs(System.nanoTime()) % 1000000,
                    16
                )
            );

            OutputStream os = new FileOutputStream(target);
            MessageDigest md = MessageDigest.getInstance("MD5");
            DigestInputStream dis = new DigestInputStream(is, md);

            byte[] buffer = new byte[8192];

            while (dis.read(buffer) > -1) {
                os.write(buffer);
            }

            dis.close();

            md.update(salt.getBytes());

            byte[] md5 = md.digest();

            StringBuilder sb = new StringBuilder();

            for (byte b : md5) {
                sb.append(String.format("%02X", b));
            }

            String filename = sb.toString().toLowerCase();

            move(target, targetDir, filename);

            return filename;
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new BusinessError(); // TODO 使用合适的错误
        }
    }

    /**
     * 将通过 HTTP 请求上传的文件写入到磁盘。
     * @param multipartFile 通过 HTTP 请求上传的文件
     * @param dir 目标路径
     * @return 文件实例
     */
    public static File save(MultipartFile multipartFile, String dir) {
        return save(multipartFile, dir, CryptoUtils.uniqueId());
    }

    /**
     * 将通过 HTTP 请求上传的文件写入到磁盘。
     * @param multipartFile 通过 HTTP 请求上传的文件
     * @param dir 目标路径
     * @param name 文件名
     * @return 文件实例
     */
    public static File save(
        MultipartFile multipartFile,
        String dir,
        String name
    ) {
        File diskFile = new File(dir, name);

        try {
            org.apache.commons.io.FileUtils.copyInputStreamToFile(
                multipartFile.getInputStream(),
                diskFile
            );
            setAsRWX(diskFile);
        } catch (IOException e) {
            throw new BusinessError(e.getMessage());
        }

        return diskFile;
    }

    /**
     * 重命名文件。
     * @param source 文件
     * @param filename 文件名
     * @return 重命名后的文件
     */
    public static File rename(File source, String filename) {
        return move(source, source.getParentFile().getAbsolutePath(), filename);
    }

    /**
     * 移动文件。
     * @param source 文件
     * @param dir 目标路径
     * @return 重命名后的文件
     */
    public static File move(File source, String dir) {
        return move(source, dir, source.getName());
    }

    /**
     * 移动文件。
     * @param source 文件
     * @param dir 目标路径
     * @param filename 文件名
     * @return 重命名后的文件
     */
    public static File move(File source, String dir, String filename) {
        File target = new File(dir, filename);

        try {
            Files.move(source.toPath(), target.toPath(), REPLACE_EXISTING);
            setAsRWX(target);
        } catch (IOException e) {
            return source;
        }

        return target;
    }

    /**
     * 删除文件。
     * @param path 文件路径
     * @return 是否删除成功
     */
    public static boolean remove(String path) {
        if (path == null) {
            return false;
        }

        return remove(new File(path));
    }

    /**
     * 删除文件。
     * @param file 文件
     * @return 是否删除成功
     */
    public static boolean remove(File file) {
        if (file == null) {
            return false;
        }

        try {
            return file.delete();
        } catch (SecurityException e) {
            return false;
        }
    }

    /**
     * 取得相对路径。
     * @param base 基准路径
     * @param path 文件路径
     * @return 文件相对路径
     */
    public static String relative(String base, String path) {
        return "/" + (new File(base))
            .toURI()
            .relativize((new File(path)).toURI())
            .getPath();
    }

    /**
     * 连接路径。
     * @param paths
     * @return 路径
     */
    public static String join(String... paths) {
        return String
            .join(File.separator, paths)
            .replaceAll("[\\\\/]+", File.separator);
    }

    /**
     * 文件是否为图像。
     * @param mimeType 文件的 MIME 类型
     * @return 是否为图像
     */
    public static boolean isImage(String mimeType) {
        return mimeType.toLowerCase().matches("^image/[^/]+$");
    }

    /**
     * 文件是否为音频。
     * @param mimeType 文件的 MIME 类型
     * @return 是否为图像
     */
    public static boolean isAudio(String mimeType) {
        return mimeType.toLowerCase().matches("^audio/[^/]+$");
    }

    /**
     * 文件是否为视频。
     * @param mimeType 文件的 MIME 类型
     * @return 是否为图像
     */
    public static boolean isVideo(String mimeType) {
        return mimeType.toLowerCase().matches("^video/[^/]+$");
    }
}
