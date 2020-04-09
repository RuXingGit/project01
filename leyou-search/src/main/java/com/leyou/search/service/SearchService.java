package com.leyou.search.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.*;
import com.leyou.search.client.BrandClient;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.client.SpecificationClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.pojo.SearchResult;
import com.leyou.search.repository.GoodsRepository;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.xml.crypto.dsig.spec.HMACParameterSpec;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author RuXing
 * @create 2020-03-31 15:37
 */
@Service
public class SearchService {

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecificationClient specificationClient;

    @Autowired
    private GoodsRepository goodsRepository;

    // jackson提供的用来进行对象和json之间的转换
    //       readValue              json转化成对象可以传入TypeReference<T>对象指定转换的对象类型
    //       writeValueAsString     将对象转化成json字符串
    private static final ObjectMapper MAPPER = new ObjectMapper();


    // 将Spu对象转化成Goods对象
    public Goods buildGoods(Spu spu) throws IOException {
        Goods goods = new Goods();

        BeanUtils.copyProperties(spu,goods);
        /**
         *  需要自己补全的属性 all、price、skus、spec
         */


        // 查询出sku的分类的名称（各级都要查询）
        List<Long> ids = Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3());
        List<String> names = categoryClient.queryNamesByIds(ids);

        // 查询品牌名称
        Brand brand = brandClient.queryBrandById(spu.getBrandId());

        // 查询sku信息
        List<Sku> skuList = goodsClient.querySkuListBySpuId(spu.getId());
        // 需要展示的sku属性
        List<Map<String,Object>> skuMapList =new ArrayList<>();
        List<Long> prices =new ArrayList<>();


        skuList.forEach(sku -> {
            prices.add(sku.getPrice());

            HashMap<String, Object> map = new HashMap<>();
            map.put("id",sku.getId());
            map.put("title",sku.getTitle());
            map.put("price",sku.getPrice());
            map.put("image",
                    // 如果图片为空就不显示，如果有就显示第一张
                    StringUtils.isBlank(sku.getImage()) ? ""
                            :StringUtils.split(sku.getImage(),",")[0]
            );

            skuMapList.add(map);
        });

        // sku的价格 Sku::getPrice 等价于 sku -> {return getPrice();}
        // List<Long> price = skuList.stream().map(Sku::getPrice).collect(Collectors.toList());
        // 查询可以作为关键字的规格参数
        List<SpecParam> params = specificationClient.queryParams(null, spu.getCid3(), null, true);
        // 获取spuDetail
        SpuDetail spuDetail = goodsClient.querySpuDetailById(spu.getId());
        // 将GenericSpec的json格式字符串转化成为Map
        Map<String,Object> genericSpecMap = MAPPER.readValue(
                spuDetail.getGenericSpec(),
                new TypeReference<Map<String, Object>>() {}
                );

        // 特殊规格参数json格式数据转化为Map
        Map<String, List<Object>> specialSpecMap = MAPPER.readValue(
                spuDetail.getSpecialSpec(),
                new TypeReference<Map<String, List<Object>>>() {
                }
        );
        // 创建Goods对象中spec属性保存的Map对象
        Map<String,Object> specs =new HashMap<>();
        // 封装规格参数成goods可以保存的对象
        params.forEach(param->{
            if(param.getGeneric()){
                // 封装通用参数
                String value = genericSpecMap.get(param.getId().toString()).toString();

                // 如果规格参数是数值的字段，需要进行对数值的区间属性(segments)进行解析
                // 将 1-2 的字符串解析成在页面上可以显示的价格区间、cpu频率区间等等
                if(param.getNumeric()){
                    value = chooseSegment(value, param);
                }
                specs.put(param.getName(),value);
            }else{
                // 对sku的特有规格参数进行封装
                List<Object> list = specialSpecMap.get(param.getId().toString());
                specs.put(param.getName(),list);
            }
        });


        // 用来搜索的关键字
        goods.setAll(
                spu.getTitle()+" "      // spu标题
                + StringUtils.join(names," ") +" "  // 分类信息
                +brand.getName()        // 品牌名称
        );

        // 设置sku价格
        goods.setPrice(prices);
        // 设置sku信息
        goods.setSkus(MAPPER.writeValueAsString(skuList));
        // 设置可作为关键字的规格参数
        goods.setSpecs(specs);

        return goods;
    }

    // 工具方法，用来将数据库中用来表示范围区间的字符串转换成便于页面显示的格式
    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if(segs.length == 2){
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if(val >= begin && val < end){
                if(segs.length == 1){
                    result = segs[0] + p.getUnit() + "以上";
                }else if(begin == 0){
                    result = segs[1] + p.getUnit() + "以下";
                }else{
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }

    public SearchResult search(SearchRequest request) {
        // 检查查询关键字
        if(StringUtils.isEmpty(request.getKey())){
            return null;
        }
        // 自定义查询构建器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

//        // 创建查询
//        QueryBuilder basicQuery = QueryBuilders
//                .matchQuery(        // 基本查询
//                "all",          // 查询的属性
//                request.getKey()        // 查询的值
//        ).operator(Operator.AND);       // 查询取交集

        // 创建基本查询，能够进行过滤
        QueryBuilder basicQuery = buildBoolQueryBuilder(request);

        // 添加基本查询
        queryBuilder.withQuery(basicQuery);
        // 添加分页 注意：这里个方法中页码是从0开始的
        queryBuilder.withPageable(PageRequest.of(request.getPage()-1,request.getSize()));
        // 对结果集进行过滤 ，过滤不需要的属性
        String[] includes = {"id", "skus","subTitle"};               // 需要的属性
        queryBuilder.withSourceFilter(new FetchSourceFilter(includes,null));

        // 添加分类和品牌的聚合
        String categoryAggName = "categories";
        String brandAggName = "brands";
        queryBuilder.addAggregation(
                AggregationBuilders.terms(categoryAggName).field("cid3")
        );
        queryBuilder.addAggregation(
                AggregationBuilders.terms(brandAggName).field("brandId")
        );
        // 查询
        AggregatedPage<Goods> goodsPage = (AggregatedPage<Goods>)goodsRepository.search(queryBuilder.build());

        // 解析聚合的结果
        List<Map<String,Object>> categories = getCategoryAggResult(goodsPage.getAggregation(categoryAggName));
        List<Brand> brands = getBrandAggResult(goodsPage.getAggregation(brandAggName));



        List<Map<String,Object>> specs = new ArrayList<>();
        // 只有在分类只有一个的时候才进行规格参数聚合，不然规格参数太多了
        if(!CollectionUtils.isEmpty(categories)&&categories.size()==1){
            // 对规格参数进行聚合
            specs = getParamAggResult(
                    // 获取分类的id
                    (Long)categories.get(0).get("id"),
                    // 查询条件
                    basicQuery
            );
        }


        return new SearchResult(
                goodsPage.getTotalElements(),
                goodsPage.getTotalPages(),      // 总记录数
                goodsPage.getContent(),         // 查询的数据
                categories,                     // 分类
                brands,                          // 品牌
                specs                           // 规格参数
        );
    }

    private QueryBuilder buildBoolQueryBuilder(SearchRequest request) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        boolQuery.must(
                // 基本查询
                QueryBuilders.matchQuery("all",request.getKey()).operator(Operator.AND)
        );

        // 获取过滤信息
        Map<String, Object> filter = request.getFilter();
        // 添加过滤条件
        for (Map.Entry<String, Object> entry : filter.entrySet()) {
            String key = entry.getKey();

            // 根据不同类型的过滤，对key值进行对应解析
            if(StringUtils.equals("品牌",key)){
                key="brandId";
            }else if(StringUtils.equals("分类",key)){
                key="cid3";
            }else{
                key="specs."+key+".keyword";
            }
            // 添加过滤
            boolQuery.filter(QueryBuilders.termQuery(key,entry.getValue()));
        }

        return boolQuery;
    }

    // 对规格参数进行聚合
    private List<Map<String, Object>> getParamAggResult(Long id, QueryBuilder basicQuery) {
        // 自定义查询构建器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 添加基本查询条件
        queryBuilder.withQuery(basicQuery);
        // 查询需要聚合的规格参数
        List<SpecParam> params = specificationClient.queryParams(
                null,
                id,             // 分类id
                null,
                true   // 可以作为关键字的字段
        );
        // 添加规格参数聚合查询
        params.forEach(param->{
            queryBuilder.addAggregation(
                    AggregationBuilders.terms(param.getName())
                            .field("specs."+param.getName()+".keyword")
            );
        });

        // 添加结果集过滤（不需要普通的查询结果）
        String[] includes = {}; // 不包含任何字段
        queryBuilder.withSourceFilter(new FetchSourceFilter(includes,null));
        // elasticsearch查询
        AggregatedPage<Goods> goodsPage = (AggregatedPage<Goods>) goodsRepository.search(queryBuilder.build());

        // 用来返回结果
        List<Map<String, Object>> specs =new ArrayList<>();

        // 解析结果集        key->规格参数名,value->聚合对象
        Map<String, Aggregation> aggs = goodsPage.getAggregations().asMap();
        for (Map.Entry<String, Aggregation> agg : aggs.entrySet()) {
            // 初始化一个map  key  option
            Map<String,Object> map=new HashMap<>();
            map.put("key",agg.getKey());

            // 转化成解析字符串的Aggregation子类
            StringTerms terms = (StringTerms) agg.getValue();
            // 获取桶中的数据
            List<String> options = terms.getBuckets().stream().map(bucket -> {
                return bucket.getKeyAsString();
            }).collect(Collectors.toList());
            map.put("options",options);

            // 收集解析结果
            specs.add(map);
        }


        return specs;
    }

    // 解析品牌聚合结果集
    private List<Brand> getBrandAggResult(Aggregation aggregation) {
        LongTerms terms = (LongTerms) aggregation;

        // 获取聚合中的桶
        /*List<Brand> brands=new ArrayList<>();
        terms.getBuckets().forEach(bucket -> {
            Brand brand = brandClient.queryBrandById(bucket.getKeyAsNumber().longValue());
            brands.add(brand);
        });*/

        return terms.getBuckets().stream().map(bucket -> {
            return brandClient.queryBrandById(bucket.getKeyAsNumber().longValue());
        }).collect(Collectors.toList());
    }

    // 解析品牌聚合结果集
    private List<Map<String, Object>> getCategoryAggResult(Aggregation aggregation) {
        LongTerms terms = (LongTerms) aggregation;

        // 对桶中的内容解析
        List<Map<String, Object>> categories = terms.getBuckets().stream().map(bucket -> {
            Map<String, Object> map = new HashMap<>();

            // 获取聚合后分类的id
            Long id = bucket.getKeyAsNumber().longValue();
            // 查询分类名称
            List<String> names = categoryClient.queryNamesByIds(Arrays.asList(id));
            map.put("id", id);
            map.put("name", names.get(0));
            return map;
        }).collect(Collectors.toList());

        return categories;
    }

    // 更新索引库
    public void save(Long id) throws IOException {
        Spu spu = goodsClient.querySpuById(id);
        Goods goods = buildGoods(spu);
        goodsRepository.save(goods);
    }

    public void delete(Long id) {


        goodsRepository.deleteById(id);
    }
}
