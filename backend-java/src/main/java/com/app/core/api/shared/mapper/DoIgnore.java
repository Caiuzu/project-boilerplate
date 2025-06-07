package com.app.core.api.shared.mapper;

import org.mapstruct.Qualifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotação personalizada utilizada como {@link Qualifier} do MapStruct para marcar
 * métodos auxiliares de mapeamento que devem ser ignorados automaticamente pelo
 * processo de mapeamento principal.
 *
 * Normalmente aplicada em métodos `default` de mappers que inicializam manualmente
 * o {@link CycleAvoidingMappingContext}.
 */
@Qualifier
@Target(ElementType.METHOD)
@Retention(value = RetentionPolicy.CLASS)
public @interface DoIgnore {
}
