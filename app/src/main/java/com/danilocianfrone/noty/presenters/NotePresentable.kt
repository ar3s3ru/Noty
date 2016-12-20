package com.danilocianfrone.noty.presenters

import com.danilocianfrone.noty.models.Priority

sealed class NotePresentable {
    interface Priorited { fun withPriority(): Priority }
    interface Queried   { fun onQueryText():  String   }
}