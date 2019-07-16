package com.tian.happyfood.dao.esmapper;

import com.alibaba.fastjson.JSONObject;
import com.tian.common.util.ReflectionUtil;
import com.tian.happyfood.dao.espo.BulkParam;
import com.tian.happyfood.dao.espo.MultiGetParam;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetRequestBuilder;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.*;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * elasticsearch连接
 * Created by tianxiong on 2019/7/5.
 */
@Component
public class BaseESSearcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseESSearcher.class);
    public static volatile TransportClient client;
    /**
     * es中索引名的前缀, 由于新版本的es, 一个索引下面只能放一个类型的type,所在这里我们用一个固定的前缀拼上type当做index,
     *
     */
    @Value("${es_index_pre}")
    protected String esIndexPre;

    /**
     *
     * 指定集群, my-application为集群名, 该名字配置在es服务conf目录下的elasticsearch.yml文件中

    Settings settings = Settings.builder().put("cluster.name", "my-application").build();
    // 创建访问es服务器的客户端
        try {
        client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"),9300));
    } catch (UnknownHostException e) {
        LOGGER.error("init es error", e);
    }

    // 数据查询
    GetResponse response = client.prepareGet("lib1","user","3").execute().actionGet();
    // 得到查询出的数据
        System.out.println(response.getSourceAsString());
    关闭应用
        client.close();
     */
    @PostConstruct
    private void init(){
        /*
         * 指定集群, my-application为集群名, 该名字配置在es服务conf目录下的elasticsearch.yml文件中
         */
        if(client == null){
            synchronized (BaseESSearcher.class){
                if(client == null){
                    Settings settings = Settings.builder().put("cluster.name", "my-application").build();
                    // 创建访问es服务器的客户端
                    try {
                        client = new PreBuiltTransportClient(settings)
                                .addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"),9300));
                    } catch (UnknownHostException e) {
                        LOGGER.error("init es error", e);
                    }
                }
            }
        }
    }

    /**
     * 用于把数据添加到es中.
     * 默认用object中的id属性当作id,如果没有,则需要显示指定其它属性或者传入一个值当id(另一个方法)
     */
    public boolean put(String type, Object object) throws IOException, ExecutionException, InterruptedException {
        if(object == null){
            throw new RuntimeException("object can not be null.");
        }
        Map<String,Object> filedValues = ReflectionUtil.getFiledValues(object, true);
        Object id = filedValues.get("id");
        if(id == null){
            throw new RuntimeException("found no filed of \"id\".");
        }
        IndexResponse response = client.prepareIndex(esIndexPre+type, type)
                //必须为对象单独指定ID
                .setId(id.toString())
                .setSource(filedValues)
                .execute()
                .actionGet();
        return response.status() == RestStatus.CREATED;
    }

    /**
     * 删除一条文档
     * @param type
     * @param id
     * @return
     */
    public boolean delete(String type, String id){
        DeleteResponse response = client.prepareDelete(esIndexPre+type, type, id)
                .execute().actionGet();
        return response.status() == RestStatus.MOVED_PERMANENTLY;
    }

    /**
     * 根据id精确获取文档
     * @param type
     * @param id
     * @return
     */
    public String getById(String type, String id){
        GetResponse response = client.prepareGet(esIndexPre+type, type, id)
                .execute().actionGet();
        return response.getSourceAsString();
    }

    /**
     * 根据精确获取文档,并转换成java对象
     * @param type
     * @param id
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getById(String type, String id, Class<T> clazz){
        GetResponse response = client.prepareGet(esIndexPre+type, type, id)
                .execute().actionGet();
        String jsonStr = response.getSourceAsString();
        T t = JSONObject.parseObject(jsonStr,clazz);
        return t;
    }

    /**
     * 增量修改, 只改部分字段的值
     * @param type
     * @param id
     * @param filedValues
     * @return
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public boolean post(String type, String id, Map<String, String> filedValues) throws IOException, ExecutionException, InterruptedException {
        UpdateRequest request = new UpdateRequest();
        request.index(esIndexPre+type).type(type).id(id);
        XContentBuilder builder = XContentFactory.jsonBuilder().startObject();
        for(Map.Entry<String, String> entry: filedValues.entrySet()){
            builder.field(entry.getKey(), entry.getValue());
        }
        builder = builder.endObject();
        request.doc(builder);

        UpdateResponse response = client.update(request).get();
        return true;
    }

    /**
     * 批量查询, 可以查不同索引,不同类型的
     * @param multiGetParams
     * @return
     */
    public List<String> multiGet(List<MultiGetParam> multiGetParams){
        if(multiGetParams == null || multiGetParams.size()==0){
            return Collections.emptyList();
        }
        MultiGetResponse responses = null;
        MultiGetRequestBuilder builder =  client.prepareMultiGet();
        for(MultiGetParam m: multiGetParams){
            builder.add(m.getIndex(), m.getType(), m.getIdList());
        }
        responses = builder.get();
        // 遍历结果集, 拿所不同类型的影响
        List<String> resultList = new ArrayList<String>();
        for(MultiGetItemResponse item: responses){
            GetResponse gr = item.getResponse();
            if(gr!= null && gr.isExists()){
                resultList.add(gr.getSourceAsString());
            }
        }

        return resultList;
    }

    /**
     * 批量修改文档(新增或者更新)
     */
    public void bulk(List<BulkParam> bulkParamList){
        BulkRequestBuilder bulkBuilder = client.prepareBulk();
        // 添加
        for(BulkParam b: bulkParamList){
            IndexRequestBuilder brb = client.prepareIndex(b.getIndex(), b.getType(), b.getId())
                    .setSource(ReflectionUtil.getFiledValues(b.getSource(), true));
            bulkBuilder.add(brb);
        }
        BulkResponse responses = bulkBuilder.get();
        for(BulkItemResponse item: responses){
            // 可以得到每一个的执行结果
            item.status();
        }
        // 需要的话可以返回结果,失败的还可以返回失败原因
    }

    /**
     * 删除符合查询条件的记录
     */
    public void deleteByQuery(){
//        BulkByScrollResponse response = DeleteByQueryAction.INSTANCE.newResponse().fi
    }

    /**
     * 查询所有
     * @param index
     */
    public void queryAll(String index){
        QueryBuilder builder = new MatchAllQueryBuilder();
        // 这里的setSize可以设置一次查询多少, 默认是10条
        SearchResponse sr = client.prepareSearch(index).setQuery(builder).setSize(3).get();

        SearchHits hits = sr.getHits();
        for(SearchHit hit: hits){
            // 文档内容
            System.out.println(hit.getSourceAsString());
            // 也可以转成map
            for(Map.Entry<String, Object> entry: hit.getSourceAsMap().entrySet()){
                System.out.println("K: "+entry.getKey()+"V: "+entry.getValue());
            }
        }

    }

    /**
     * 条件查询. 一个值匹配多个字段
     * 可以匹配一个字段, 或者多个字段
     */
    public void mathchQuery(String index, String text, String ... filedNames){
        QueryBuilder builder = QueryBuilders.multiMatchQuery(text, filedNames);
        SearchResponse sr = client.prepareSearch(index).setQuery(builder).setSize(3).get();
        SearchHits hits = sr.getHits();
        for(SearchHit hit: hits){
            // 文档内容
            System.out.println(hit.getSourceAsString());
            // 也可以转成map
            for(Map.Entry<String, Object> entry: hit.getSourceAsMap().entrySet()){
                System.out.println("K: "+entry.getKey()+"V: "+entry.getValue());
            }
        }

    }

    /**
     * 一个字段匹配多个值
     * @param index
     * @param filedName
     * @param values
     */
    public void termsQuery(String index, String filedName, Object ... values){
        QueryBuilder builder = QueryBuilders.termsQuery(filedName, values);
        SearchResponse sr = client.prepareSearch(index).setQuery(builder).setSize(3).get();
        SearchHits hits = sr.getHits();
        for(SearchHit hit: hits){
            // 文档内容
            System.out.println(hit.getSourceAsString());
            // 也可以转成map
            for(Map.Entry<String, Object> entry: hit.getSourceAsMap().entrySet()){
                System.out.println("K: "+entry.getKey()+"V: "+entry.getValue());
            }
        }


    }

    /**
     * 范围查询
     * @param type
     * @param start
     * @param end
     */
    public void rangeQuery(String type, Object start, Object end){
        QueryBuilder builder = QueryBuilders.rangeQuery(type).from(start).to(end);
        SearchResponse sr = client.prepareSearch(type).setQuery(builder).setSize(3).get();
        SearchHits hits = sr.getHits();
        for(SearchHit hit: hits){
            // 文档内容
            System.out.println(hit.getSourceAsString());
            // 也可以转成map
            for(Map.Entry<String, Object> entry: hit.getSourceAsMap().entrySet()){
                System.out.println("K: "+entry.getKey()+"V: "+entry.getValue());
            }
        }

    }

    /**
     * 前缀查询
     */
    public void prifixQuery(){
        QueryBuilder builder = QueryBuilders.prefixQuery("filedNmae","prefix");
    }

    /**
     * 类似于正则查询
     */
    public void wildcardQuery(){
        QueryBuilder builder = QueryBuilders.wildcardQuery("name","tian*");

    }

    /**
     * 相似查询
     */
    public void fuzzyQuery(){
        QueryBuilder builder = QueryBuilders.fuzzyQuery("name", "tianxoing");
    }

    /**
     * 类型查询
     */
    public void typeQuery(){
        QueryBuilder builder = QueryBuilders.typeQuery("type");
    }

    /**
     * 一次查询多个id
     */
    public void idQuery(){
        QueryBuilder builder = QueryBuilders.idsQuery().addIds("1","2");
    }

    /**
     * 求某个字段最大值
     */
    public void max(){
        AggregationBuilder agg = AggregationBuilders.max("aggMax").field("age");
        SearchResponse response = client.prepareSearch("lib3").addAggregation(agg).get();
        // 最大
        Max max = response.getAggregations().get("aggMax");
        System.out.println(max.getValue());

    }

    /**
     * 最小值
     */
    public void min(){
        AggregationBuilder agg = AggregationBuilders.min("aggMin").field("age");
        SearchResponse response = client.prepareSearch("lib3").addAggregation(agg).get();
        // 最大
        Min min = response.getAggregations().get("aggMin");
        System.out.println(min.getValue());

    }

    /**
     * 平均值
     */
    public void avg(){
        AggregationBuilder agg = AggregationBuilders.avg("aggAvg").field("age");
        SearchResponse response = client.prepareSearch("lib3").addAggregation(agg).get();
        // 最大
        Avg avg = response.getAggregations().get("aggAvg");
        System.out.println(avg.getValue());

    }

    /**
     * 求和
     */
    public void sum(){
        AggregationBuilder agg = AggregationBuilders.sum("aggSum").field("age");
        SearchResponse response = client.prepareSearch("lib3").addAggregation(agg).get();
        // 最大
        Sum sum = response.getAggregations().get("aggSum");
        System.out.println(sum.getValue());

    }

    public void distinct(){
        AggregationBuilder agg = AggregationBuilders.cardinality("aggCardinality").field("age");
        SearchResponse response = client.prepareSearch("lib3").addAggregation(agg).get();
        // 最大
        Cardinality cardinality = response.getAggregations().get("aggCardinality");
        // 返回的是互不相同的值的个数
        System.out.println(cardinality.getValue());

    }

    /**
     * 组合查询
     */
    public void boolQuery(){
        QueryBuilder builder = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("name","tianxiong"))
                                    .mustNot(QueryBuilders.matchQuery("sex","m"))
                                    .should(QueryBuilders.matchQuery("address","hubei"))
                                    .filter(QueryBuilders.rangeQuery("age").gte(18));

    }

}
