package net.codelet.cloud.vo;

/**
 * 图像缩放方式。
 */
public enum ImageProportion {
    CROP, // 裁剪（最大化）
    STRETCH, // 拉伸
    KEEP // 保持横高比（最小化）
}
