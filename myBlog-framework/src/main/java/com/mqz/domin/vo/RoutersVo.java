package com.mqz.domin.vo;

import com.mqz.domin.dto.MenuDto;
import com.mqz.domin.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoutersVo {

    private List<MenuDto> menus;
}