package ru.virgil.spring_tools.examples.system.rest

/**
 * Можно выделять постоянно повторяемые в API параметры в отдельный объект.
 * Это защитит от расхождения в названиях, типа pages и page.
 * И позволит не повторять названия параметров в каждом контроллере.
 */
object RestValues {

    const val pageParam = "page"
    const val pageSizeParam = "size"

}
