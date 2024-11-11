package boomerang.global.utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.ArrayList;
import java.util.List;

@Converter
public class BooleanListConverter implements AttributeConverter<List<Boolean>, String> {

    private static final String SPLIT_CHAR = ",";

    // List<Boolean>을 String으로 변환
    @Override
    public String convertToDatabaseColumn(List<Boolean> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        // Boolean 리스트를 "true,false,true" 같은 형식의 String으로 변환
        List<String> stringList = new ArrayList<>();
        for (Boolean bool : list) {
            stringList.add(bool.toString());
        }
        return String.join(SPLIT_CHAR, stringList);
    }

    // String을 List<Boolean>으로 변환
    @Override
    public List<Boolean> convertToEntityAttribute(String joined) {
        if (joined == null || joined.isEmpty()) {
            return new ArrayList<>();
        }
        // "true,false,true" 같은 형식의 String을 Boolean 리스트로 변환
        List<Boolean> booleanList = new ArrayList<>();
        for (String value : joined.split(SPLIT_CHAR)) {
            booleanList.add(Boolean.parseBoolean(value));
        }
        return booleanList;
    }
}
