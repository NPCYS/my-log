package com.mqz.domin.dto;

import com.mqz.domin.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuDto extends Menu {
    private MenuDto[] children;
}
