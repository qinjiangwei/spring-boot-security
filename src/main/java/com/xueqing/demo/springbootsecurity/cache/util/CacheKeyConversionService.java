package com.xueqing.demo.springbootsecurity.cache.util;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.lang.Nullable;

import java.util.Arrays;

public class CacheKeyConversionService implements ConversionService {
    @Override
    public boolean canConvert(@Nullable Class<?> sourceType, Class<?> targetType) {
        return true;
    }

    @Override
    public boolean canConvert(@Nullable TypeDescriptor sourceType, TypeDescriptor targetType) {
        return true;
    }

    @Nullable
    @Override
    public <T> T convert(@Nullable Object source, Class<T> targetType) {
        return (T) convert(source);
    }

    @Nullable
    @Override
    public Object convert(@Nullable Object source, @Nullable TypeDescriptor sourceType, TypeDescriptor targetType) {
        return convert(source);
    }

    private Object convert(Object source) {
        if (source instanceof CacheHashCode) {
            return ((CacheHashCode) source).hashString();
        }
        return CacheHashCode.of(source).hashString();
    }


    /**
     * Hash code generator.
     */
    public static class CacheHashCode {
        private Object[] params;
        private int code;

        private CacheHashCode(Object[] params) {
            this.params = params;
            this.code = Arrays.deepHashCode(params);
        }

        public static CacheHashCode of(Object object) {
            //return new CacheHashCode(ArrayUtil.isArray(object) ? ArrayUtil.toObjectArray(object) : new Object[]{object});
            return new CacheHashCode(new Object[]{object});
        }

        @Override
        public int hashCode() {
            return code;
        }

        public String hashString() {
            return code + "";
        }

        @Override
        public String toString() {
            return "CacheHashCode{" +
                    "params=" + Arrays.toString(params) +
                    ", code=" + code +
                    '}';
        }
    }
}


