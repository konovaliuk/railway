package com.liashenko.app.persistance.dao;

import java.io.Serializable;

/**
 * Интерфейс идентифицируемых объектов.
 */
public interface Identified<PK extends Serializable> {

    /**
     * Возвращает идентификатор объекта
     */
    PK getId();
}
