package com.it.yanxuan.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.it.yanxuan.model.GoodsSpu;
import com.it.yanxuan.search.api.ISearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;

import java.util.HashMap;
import java.util.Map;

/**
 * @auther: cyb
 * @create: 2020/7/21 16:24
 */
@Service
//@Transactional
public class SearchServiceImpl implements ISearchService {
    @Autowired
    private SolrTemplate solrTemplate;

    //根据条件查询Solr服务器中的信息
    @Override
    public Map<String, Object> query(Map queryParams) {
        //获取查询参数
        String keyword = (String) queryParams.get("keywords");
        //创建条件
        Criteria criteria = new Criteria("goods_keywords").contains(keyword);
        SimpleQuery query = new SimpleQuery(criteria);
        //进行查询
        ScoredPage<GoodsSpu> scoredPage = solrTemplate.queryForPage(query, GoodsSpu.class);
        //构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("result", scoredPage.getContent());
        result.put("total", scoredPage.getTotalElements());
        result.put("keywords", keyword);
        return result;
    }


}
