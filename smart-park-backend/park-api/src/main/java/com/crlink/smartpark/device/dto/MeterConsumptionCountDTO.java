package com.crlink.smartpark.device.dto;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
public class MeterConsumptionCountDTO implements Serializable, Comparable<MeterConsumptionCountDTO> {
    private BigDecimal consumption;
    private LocalDateTime time;

    public MeterConsumptionCountDTO() {
    }

    public MeterConsumptionCountDTO(BigDecimal consumption, LocalDateTime time) {
        this.consumption = consumption;
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeterConsumptionCountDTO that = (MeterConsumptionCountDTO) o;
        return Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(time);
    }

    @Override
    public int compareTo(@NotNull MeterConsumptionCountDTO o) {
        return this.time.isBefore(o.time) ? -1 : 1;
    }
}
