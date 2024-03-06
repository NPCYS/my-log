package com.mqz.domin.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;


import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuVo {
    private List<MenuVo> children;
    private Long id;
    private String menuName;
    private Long parentId;
}
