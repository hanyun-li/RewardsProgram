package cloud.lihan.rewardsprogram.common.utils;

import cloud.lihan.rewardsprogram.common.enums.IndexEnum;
import cloud.lihan.rewardsprogram.entety.document.UserDocument;
import cloud.lihan.rewardsprogram.entety.document.WishDocument;

/**
 * @author hanyun.li
 * @createTime 2022/07/18 15:44:00
 */
public class DocumentFactory {

    /**
     * 根据索引枚举获取对应文档对象
     *
     * @param indexEnum {@link IndexEnum} 索引枚举
     * @return 文档对象
     */
    public static Object  getDocumentByIndexName(IndexEnum indexEnum) {
        switch (indexEnum) {
            case USER_INDEX: return new UserDocument();
            case WISH_INDEX: return new WishDocument();
            default: return null;
        }
    }
}
