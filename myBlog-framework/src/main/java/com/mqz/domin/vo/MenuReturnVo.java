package com.mqz.domin.vo;

import com.mqz.annotation.CopySourceName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuReturnVo {
    private List<MenuReturnVo> children;
    private Long id;
    @CopySourceName(value = "menuName")
    private String label;
    private Long parentId;
}
