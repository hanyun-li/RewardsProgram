package cloud.lihan.rewardsprogram.config.file;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 头像属性配置
 *
 * @author hanyun.li
 * @createTime 2022/12/26 16:37:00
 */
@Data
@Configuration
public class AvatarPropertiesConfig {

    /**
     * 头像上传绝对路径
     */
    @Value("${upload.avatar.path}")
    private String avatarPath;

    /**
     * 头像访问目录
     */
    @Value("${upload.avatar.prefix}")
    private String avatarPrefix;

}
