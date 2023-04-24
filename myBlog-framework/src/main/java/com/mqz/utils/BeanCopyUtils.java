package com.mqz.utils;

import com.mqz.domin.entity.Article;
import com.mqz.domin.vo.HotArticleVo;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class BeanCopyUtils {
    private BeanCopyUtils() {
    }
    public static <V>V copyBean(Object source,Class<V> clazz) {
        V result;
        try {
            result = clazz.newInstance();
            BeanUtils.copyProperties(source, result);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return result;

    }

    public static <O,V>List<V> copyBeanList(List<O> source,Class<V> clazz){
        return  source.stream()
                .map(o -> copyBean(o, clazz))
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        Article article = new Article();
        article.setId(1L);
        article.setViewCount(100L);
        HotArticleVo hotArticleVo = copyBean(article, HotArticleVo.class);
        System.out.println(hotArticleVo);
    }
}
