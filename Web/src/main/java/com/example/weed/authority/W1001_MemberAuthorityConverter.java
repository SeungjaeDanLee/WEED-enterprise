package com.example.weed.authority;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class W1001_MemberAuthorityConverter implements AttributeConverter<W1001_MemberAuthority, String> {
    @Override
    public String convertToDatabaseColumn(W1001_MemberAuthority authority) {
        if (authority == null) {
            return null;
        }

        return authority.getName();
    }

    @Override
    public W1001_MemberAuthority convertToEntityAttribute(String authority) {
        if (authority == null) {
            return null;
        }

        return Stream.of(W1001_MemberAuthority.values())
                .filter(memberRole -> memberRole.getName().equals(authority))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}