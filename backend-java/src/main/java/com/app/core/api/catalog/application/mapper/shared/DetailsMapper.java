package com.app.core.api.catalog.application.mapper.shared;

import com.app.core.api.catalog.domain.vo.Details;
import org.mapstruct.Named;

/**
 * Mapper que converte um record Details para strings e vice-versa.
 */
public class DetailsMapper {

    @Named("mapDetailsToName")
    public static String mapDetailsToName(Details details) {
        return details != null ? details.name() : null;
    }

    @Named("mapDetailsToDescription")
    public static String mapDetailsToDescription(Details details) {
        return details != null ? details.description() : null;
    }

    @Named("mapNameAndDescriptionToDetails")
    public static Details mapNameAndDescriptionToDetails(String name, String description) {
        if (name == null || description == null) {
            return null;
        }
        return new Details(name, description);
    }
}
