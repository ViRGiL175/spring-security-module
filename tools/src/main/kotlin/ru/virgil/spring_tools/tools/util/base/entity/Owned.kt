package ru.virgil.spring_tools.tools.util.base.entity

interface Owned<Owner : Identified> {

    var owner: Owner

}
