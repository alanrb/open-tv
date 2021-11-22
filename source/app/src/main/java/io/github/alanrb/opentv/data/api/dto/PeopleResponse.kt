package io.github.alanrb.opentv.data.api.dto

import io.github.alanrb.opentv.domain.entities.PeopleModel

/**
 * Created by Tuong (Alan) on 6/14/21.
 * Copyright (c) 2021 Buuuk. All rights reserved.
 */

data class PeopleResponse(
    val cast: List<PeopleModel>,
    val directing: List<PeopleModel>,
)