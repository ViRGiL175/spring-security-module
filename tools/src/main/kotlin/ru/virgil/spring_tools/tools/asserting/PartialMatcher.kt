package ru.virgil.spring_tools.tools.asserting

interface PartialMatcher {

    val assertUtils: AssertUtils

    infix fun Any.shouldBePartialEquals(other: Any): Any {
        assertUtils.partialEquals(this, other)
        return this
    }
}
