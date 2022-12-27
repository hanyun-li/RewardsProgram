package cloud.lihan.rewardsprogram.config.elasticsearch;

import cloud.lihan.rewardsprogram.common.enums.IndexEnum;
import cloud.lihan.rewardsprogram.common.utils.DocumentUtil;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.UUID;

/**
 * 检查es中所需要的索引是否存在，若不存在，则初始化这些索引
 *
 * @author hanyun.li
 * @createTime 2022/07/18 10:46:00
 */
@Slf4j
@Component
public class IndexInitialization {

    @Autowired
    private ElasticsearchClient esClient;

    @PostConstruct
    public void init() {
        IndexEnum[] values = IndexEnum.values();
        for (IndexEnum indexEnum : values) {
            try {
                // 判断索引是否存在 true:不存在,建立这个索引
                if (this.indexIsNotExist(indexEnum.getIndexName())) {
                    // 初始化索引
                    this.initIndex(indexEnum);
                    log.info("index '" + indexEnum.getIndexName() + "' has been initialized");
                }
            } catch (IOException | InterruptedException e) {
                log.error("Init index '" + indexEnum.getIndexName() + "' fail! errorInfo: {}", e.getMessage());
            }
        }
    }

    /**
     * 判断指定的索引是否存在
     *
     * @param indexName 索引名称
     * @return true:不存在 false:存在
     * @throws IOException 异常信息
     */
    private Boolean indexIsNotExist(String indexName) throws IOException {
        BooleanResponse exists = esClient.indices().exists(e -> e.index(indexName));
        return !exists.value();
    }

    /**
     * 初始化指定索引
     *
     * @param indexEnum 索引枚举
     * @throws IOException 异常信息
     */
    private void initIndex(IndexEnum indexEnum) throws IOException, InterruptedException {
        String id = UUID.randomUUID().toString();
        esClient.create(t -> t
                .id(id)
                .index(indexEnum.getIndexName())
                .document(DocumentUtil.getDocumentByIndexName(indexEnum))
        );

        // 判断是否是帖子索引
        if (IndexEnum.CONTENT_INDEX.equals(indexEnum)) {
            // 删除初始化帖子索引所引入的多余数据
            this.deleteUselessContent(id);
        }
    }

    /**
     * 删除初始化帖子索引所引入的多余数据
     *
     * @throws IOException 异常信息
     */
    private void deleteUselessContent(String id) throws IOException, InterruptedException {
        // 等待帖子索引初始化之后再执行清除无用数据操作
        Thread.sleep(1000);
        esClient.delete(d -> d
                .index(IndexEnum.CONTENT_INDEX.getIndexName())
                .id(id)
        );
    }

}
