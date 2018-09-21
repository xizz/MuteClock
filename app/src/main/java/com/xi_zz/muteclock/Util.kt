package com.xi_zz.muteclock

import java.util.Optional

val <T>Optional<T>.value: T? get() = if (isPresent) get() else null