package com.digitinary.customercare.specification;

import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;

/**
 * هنا انا بنيت يوتاليتي كلاس SpecificationUtils
 * صراحة ai ساعدني كيف نبنيه لكن بطريقة اني افهمه وانا اول مرة بستعمله للامانة
 * قرات عنه وفهمت ليش بستعملوه وكيف تستعمله لكن طريقة البناء او توزيع او الديزاين هاي ما بعرفها مع الخبرة بتيجي
 *
 */

public final class SpecificationUtils {

    private SpecificationUtils() {
    }

    public static <T> Specification<T> equal(String field, Object value) {
        return (root, query, cb) ->
                cb.equal(root.get(field), value);
    }

    public static <T> Specification<T> notEqual(String field, Object value) {
        return (root, query, cb) ->
                cb.notEqual(root.get(field), value);
    }

    public static <T> Specification<T> contains(String field, String value) {
        return (root, query, cb) ->
                cb.like(root.get(field), "%" + value + "%");
    }

    public static <T> Specification<T> startsWith(String field, String value) {
        return (root, query, cb) ->
                cb.like(root.get(field), value + "%");
    }

    public static <T> Specification<T> endsWith(String field, String value) {
        return (root, query, cb) ->
                cb.like(root.get(field), "%" + value);
    }

    public static <T> Specification<T> in(String field, Collection<?> values) {
        return (root, query, cb) ->
                root.get(field).in(values);
    }
}
