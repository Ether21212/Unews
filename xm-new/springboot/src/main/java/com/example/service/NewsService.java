package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.unit.DataUnit;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.News;
import com.example.mapper.NewsMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 新闻信息业务处理
 **/
@Service
public class NewsService {

    @Resource
    private NewsMapper newsMapper;

    /**
     * 新增
     */
    public void add(News news) {

        news.setTime(DateUtil.now());
        Account currentUser = TokenUtils.getCurrentUser();
        if(currentUser.getRole().equals(RoleEnum.ADMIN.name())){
            news.setStatus("通过");
            news.setType("common");
        }

        newsMapper.insert(news);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        newsMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            newsMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(News news) {
        newsMapper.updateById(news);
    }

    /**
     * 根据ID查询
     */
    public News selectById(Integer id) {
        return newsMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<News> selectAll(News news) {
        return newsMapper.selectAll(news);
    }

    /**
     * 分页查询
     */
    public PageInfo<News> selectPage(News news, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<News> list = newsMapper.selectAll(news);
        return PageInfo.of(list);
    }

    public List<News> selectHot() {
        List<News> newsList = newsMapper.selectAll(null);
        newsList = newsList.stream().sorted((n1,n2) -> n2.getReadCount() - n1.getReadCount()).limit(6).collect(Collectors.toList());
        Collections.shuffle(newsList);  // 打乱数据
        newsList = newsList.stream().limit(6).collect(Collectors.toList());
        return newsList;
    }
    public void updateCount(Integer id) {
        newsMapper.updateCount(id);
    }

}