package aivle.dog.global.enums;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EnumValueConvertUtils {
    public static <E extends Enum<E> & EnumValue<T>, T> E ofDesc(Class<E> enumClass,
                                                                 String desc) {
        if (!StringUtils.hasText(desc))
            return null;

        return EnumSet.allOf(enumClass).stream()
                .filter(v -> v.getDesc().equals(desc))
                .findAny()
                .orElseThrow(() -> new RuntimeException(String.
                        format("enum=[%s], desc=[%s]가 존재하지 않습니다.", enumClass.getName(), desc)));
    }

    public static <E extends Enum<E> & EnumValue<T>, T> E ofCode(Class<E> enumClass,
                                                                 T code) {
        if (code == null)
            return null;

        return EnumSet.allOf(enumClass).stream()
                .filter(v -> v.getDbValue().equals(code))
                .findAny()
                .orElseThrow(() -> new RuntimeException(String.
                        format("enum=[%s], code=[%s]가 존재하지 않습니다.", enumClass.getName(), code)));
    }

    public static <E extends Enum<E> & EnumValue<T>, T> EnumSet<E> ofCode(Class<E> enumClass,
                                                                          T[] codeList) {
        EnumSet<E> enumSet = EnumSet.noneOf(enumClass);
        if (codeList == null || codeList.length == 0)
            return enumSet;

        Arrays.asList(codeList).forEach(v -> enumSet.add(ofCode(enumClass, v)));

        return enumSet;
    }

    public static <E extends Enum<E> & EnumValue<?>> EnumSet<E> ofBitCode(Class<E> enumClass,
                                                                          Integer code) {
        EnumSet<E> enumSet = EnumSet.noneOf(enumClass);
        if (code == null || code == 0)
            return enumSet;

        EnumSet.allOf(enumClass)
                .stream()
                .filter(v -> ((1 << v.ordinal()) & code) == 1 << v.ordinal())
                .forEach(enumSet::add);

        return enumSet;
    }

    public static <E extends Enum<E> & EnumValue<T>, T> EnumSet<E> ofBoolListCode(Class<E> enumClass,
                                                                                  boolean[] codeList) {
        EnumSet<E> enumSet = EnumSet.noneOf(enumClass);
        if (codeList == null || codeList.length == 0)
            return enumSet;
        EnumSet.allOf(enumClass).stream()
                .filter(v -> codeList[v.ordinal()])
                .forEach(enumSet::add);

        return enumSet;
    }

    public static <E extends Enum<E> & EnumValue<T>, T> T toCode(E enumValue) {
        if (enumValue == null)
            return null;

        return enumValue.getDbValue();
    }

    public static <E extends Enum<E> & EnumValue<T>, T> T[] toCode(Class<E> enumClass, EnumSet<E> enumSet) {
        List<T> list = new ArrayList<>(EnumSet.allOf(enumClass).size());

        enumSet.forEach(v -> list.add(v.getDbValue()));

        return (T[]) list.toArray();
    }


    public static <E extends Enum<E> & EnumValue<?>> Integer toBitCode(EnumSet<E> enumSet) {
        if (enumSet == null || enumSet.size() == 0)
            return 0;
        return enumSet.stream()
                .mapToInt(v -> 1 << v.ordinal())
                .sum();
    }

    public static <E extends Enum<E> & EnumValue<?>> boolean[] toBoolListCode(Class<E> enumClass, EnumSet<E> enumSet) {
        boolean[] list = new boolean[EnumSet.allOf(enumClass).size()];
        if (enumSet == null || enumSet.isEmpty())
            return list;

        enumSet.forEach(v -> list[v.ordinal()] = true);

        return list;
    }

    public static <E extends Enum<E> & EnumValue<?>> List<String> getEnumToDescList(Class<E> enumClass) {
        EnumSet<E> enumSet = EnumSet.allOf(enumClass);
        return enumSet.stream().map(E::getDesc).toList();
    }
}