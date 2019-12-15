package net.codelet.cloud.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

/**
 * Shell 工具。
 */
public class ShellUtils {

    // 根据操作系统选择 Shell 命令
    private static final String COMMAND = !System.getProperty("os.name")
        .toLowerCase().startsWith("windows") ? "/bin/bash" : "cmd.exe";

    /**
     * 执行命令，取得输出流。
     * @param args 命令参数
     * @return 输入流
     */
    public static Process exec(String... args) throws IOException {
        return exec(Arrays.asList(args));
    }

    /**
     * 执行命令，取得输出流。
     * @param args 命令参数
     * @return 输入流
     */
    public static Process exec(List<String> args) throws IOException {
        return (new ProcessBuilder(COMMAND))
            .command(args)
            .redirectErrorStream(true)
            .start();
    }

    /**
     * 对接两个进程的输出流与输入流。
     * @param sourceProcess 源进程
     * @param targetProcess 目标进程
     */
    private static void pipe(
        final Process sourceProcess,
        final Process targetProcess
    ) {
        new Thread(() -> {
            InputStream stdout = sourceProcess.getInputStream();
            OutputStream stdin = targetProcess.getOutputStream();
            int byteCount;
            byte[] bytes = new byte[1024];
            try {
                while ((byteCount = stdout.read(bytes, 0, 1024)) != -1) {
                    stdin.write(bytes, 0, byteCount);
                }
                stdout.close();
                stdin.close();
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }).start();
    }

    /**
     * 连续执行命令，并将前一个命令的输出作为下一个命令的输入。
     * @param commands 命令列表
     * @return 最终结果（输入流）
     */
    public static Process chain(List<List<String>> commands) throws IOException {
        Process previousProcess = null;
        Process currentProcess;
        for (List<String> args : commands) {
            currentProcess = exec(args);
            if (previousProcess != null) {
                pipe(previousProcess, currentProcess);
            }
            previousProcess = currentProcess;
        }
        return previousProcess;
    }

}
