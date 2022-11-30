package edu.caensup.sio.emusic.Converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Converter
public class DateConverter implements AttributeConverter<Date,LocalDateTime> {
    @Override
    public LocalDateTime convertToDatabaseColumn(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    @Override
    public Date convertToEntityAttribute(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
