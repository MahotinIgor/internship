package ru.mahotin.web.mapper;

public interface TaskMapper<E, D, G> {
    E entityFromDto(D dto);
    G dtoFromEntity(E entity);
}
