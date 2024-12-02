package ru.mahotin.web.mapper;

public interface TaskMapper<E, D> {
    E entityFromDto(D dto);
    D dtoFromEntity(E entity);
}
