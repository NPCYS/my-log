package com.mqz.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.mqz.domin.ResponseResult;
import com.mqz.domin.dto.CategoryListDto;
import com.mqz.domin.entity.Category;
import com.mqz.domin.vo.ExcelCategoryVo;
import com.mqz.service.CategoryService;
import com.mqz.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;

import static com.mqz.enums.AppHttpCodeEnum.SYSTEM_ERROR;

@RestController
@RequestMapping("/content/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory(){
        return categoryService.listAllCategory();
    }
    @GetMapping("/list")
    public ResponseResult getCategoryList(Integer pageNum, Integer pageSize, CategoryListDto categoryListDto){
        return categoryService.CategoryList(pageNum,pageSize,categoryListDto);
    }
    @PostMapping()
    public ResponseResult addCategory(@RequestBody Category category){
        return categoryService.addCategory(category);
    }
    @GetMapping("/{id}")
    public ResponseResult getOneCategory(@PathVariable Long id){
        return categoryService.getOneCategory(id);
    }
    @PutMapping()
    public ResponseResult updateCategory(@RequestBody Category category){
        return categoryService.updateCategory(category);
    }
    @DeleteMapping("/{ids}")
    public ResponseResult deleteCategory(@PathVariable List<Long> ids){
        return categoryService.deleteCategory(ids);
    }
    @PreAuthorize("@ps.hasPermission('content:category:export')")
    @GetMapping("/export")
    public void downloadCategory(HttpServletResponse response){

        try {
            //填写下载文件请求头信息
            WebUtils.setDownLoadHeader("分类",response);
            //获取全部分类信息
            List<ExcelCategoryVo> excelCategoryVos=categoryService.listExcelCategory();
            //下载文件
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("分类数据")
                    .doWrite(excelCategoryVos);
        } catch (Exception e) {
//            throw new RuntimeException(e);
            //错误信息返回成json(用自己的WEBUtils也可以哦)
            ResponseResult responseResult = ResponseResult.errorResult(SYSTEM_ERROR);
            WebUtils.renderStringWeb(response,JSON.toJSONString(responseResult));
        }
    }
}
