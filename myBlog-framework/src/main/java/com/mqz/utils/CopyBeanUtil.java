package com.mqz.utils;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.mqz.annotation.CopySourceName;
import org.ehcache.impl.internal.classes.commonslang.ClassUtils;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.beans.BeanUtils.getPropertyDescriptor;
import static org.springframework.beans.BeanUtils.getPropertyDescriptors;

public class CopyBeanUtil {

    public static Object copyProperties(Object source, Object target) throws BeansException {
        return copyProperties(source, target, (Class)null, (String[])null);
    }
    private static Object copyProperties(Object source, Object target, @Nullable Class<?> editable, @Nullable String... ignoreProperties) throws BeansException {
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");
        Class<?> actualEditable = target.getClass();
        if (editable != null) {
            if (!editable.isInstance(target)) {
                throw new IllegalArgumentException("Target class [" + target.getClass().getName() + "] not assignable to Editable class [" + editable.getName() + "]");
            }

            actualEditable = editable;
        }

        PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
        List<String> ignoreList = ignoreProperties != null ? Arrays.asList(ignoreProperties) : null;
        PropertyDescriptor[] var7 = targetPds;
        int var8 = targetPds.length;

        //获取自定义注解的值
        HashMap<String, String> copySourceNameMap = new HashMap<>();
        for (Field field : actualEditable.getDeclaredFields()) {
            CopySourceName annotation = field.getAnnotation(CopySourceName.class);
            if (annotation != null) {
                copySourceNameMap.put(field.getName(), annotation.value());
            }
        }

        for(int var9 = 0; var9 < var8; ++var9) {
            PropertyDescriptor targetPd = var7[var9];
            Method writeMethod = targetPd.getWriteMethod();
            String name = targetPd.getName();
            String sourceFieldName = copySourceNameMap.get(name);
            //将需要拷贝的字段名称保持一致
            if (sourceFieldName != null) {
                name = sourceFieldName;
            }
            if (writeMethod != null && (ignoreList == null || !ignoreList.contains(name))) {
                //将需要拷贝的字段名称保持一致
                PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), name);
                if (sourcePd != null) {
                    Method readMethod = sourcePd.getReadMethod();
                    if (readMethod != null && ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType())) {
                        try {
                            if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                                readMethod.setAccessible(true);
                            }

                            Object value = readMethod.invoke(source);
                            if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                writeMethod.setAccessible(true);
                            }

                            writeMethod.invoke(target, value);

                        } catch (Throwable var15) {
                            throw new FatalBeanException("Could not copy property '" + targetPd.getName() + "' from source to target", var15);
                        }
                    }
                }
            }
        }
        return target;
    }
}
